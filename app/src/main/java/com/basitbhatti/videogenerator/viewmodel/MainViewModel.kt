package com.basitbhatti.videogenerator.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.basitbhatti.videogenerator.api.RetrofitInstance
import com.basitbhatti.videogenerator.db.TextRequest
import com.basitbhatti.videogenerator.model.TextRequestBody
import com.basitbhatti.videogenerator.repository.RequestRepository
import com.basitbhatti.videogenerator.utils.STATUS_FAILED
import com.basitbhatti.videogenerator.utils.STATUS_IN_QUEUE
import com.basitbhatti.videogenerator.utils.STATUS_SUCCESS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(val repository: RequestRepository) : ViewModel() {

    val textApi = RetrofitInstance.textApi

    private val _list = MutableLiveData<List<TextRequest>>()
    val listRequests: LiveData<List<TextRequest>> = _list

    fun sendTextRequest(body: TextRequestBody) {
        viewModelScope.launch(Dispatchers.IO) {
            val request = createRequest(body)
            repository.insertRequest(request)
            updateList()
        }
    }

    fun deleteRequest(request: TextRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteRequest(request)
            updateList()
        }
    }

    private suspend fun createRequest(body: TextRequestBody): TextRequest {
        return try {
            val response = textApi.sendTextRequest(body)
            val initialResponse = response.body()

            if (response.isSuccessful && initialResponse != null &&
                initialResponse.uuid.isNotBlank()
            ) {
                Log.d(
                    "TAGRESPONSE",
                    "Generation request accepted: status=${initialResponse.status}"
                )
                TextRequest(
                    0,
                    body.text_prompt,
                    STATUS_IN_QUEUE,
                    initialResponse.status,
                    initialResponse.uuid
                )
            } else {
                val errorDetails = response.errorBody()?.string()?.take(300).orEmpty()
                Log.e(
                    "TAGRESPONSE",
                    "Generation request rejected: HTTP ${response.code()} $errorDetails"
                )
                TextRequest(0, body.text_prompt, STATUS_FAILED, "", "")
            }
        } catch (e: Exception) {
            Log.e("TAGRESPONSE", "Could not submit generation request", e)
            TextRequest(0, body.text_prompt, STATUS_FAILED, "", "")
        }
    }

    suspend fun updateList() {
        val requests = withContext(Dispatchers.IO) {
            repository.getRequests()
        }
        _list.postValue(requests)
    }

    /**
     * Refreshes queued generation jobs once. QueueScreen calls this repeatedly while visible,
     * so the existing Runway status endpoint is polled until a terminal state is returned.
     */
    suspend fun updateRequests() = withContext(Dispatchers.IO) {
        val requests = repository.getRequests()
            .filter { it.requestStatus == STATUS_IN_QUEUE }

        for (request in requests) {
            if (request.uuid.isBlank()) {
                request.requestStatus = STATUS_FAILED
                repository.updateRequest(request)
                continue
            }

            try {
                val response = textApi.getStatus(request.uuid)
                val result = response.body()

                if (response.isSuccessful && result != null) {
                    val apiStatus = result.status.trim().lowercase()

                    when {
                        apiStatus.contains("success") ||
                            apiStatus.contains("complete") -> {
                            if (result.url.isNotBlank()) {
                                request.requestStatus = STATUS_SUCCESS
                                request.url = result.url
                            }
                        }

                        apiStatus.contains("fail") ||
                            apiStatus.contains("error") ||
                            apiStatus.contains("cancel") -> {
                            request.requestStatus = STATUS_FAILED
                        }

                        // "submitted", "pending", "processing", "in queue",
                        // and unknown non-terminal states remain queued.
                        else -> request.requestStatus = STATUS_IN_QUEUE
                    }

                    repository.updateRequest(request)
                    Log.d(
                        "TAGSTATUS",
                        "Request ${request.uuid}: ${result.status}, progress=${result.progress}"
                    )
                } else {
                    Log.w(
                        "TAGSTATUS",
                        "Status check failed: HTTP ${response.code()} for request ${request.uuid}"
                    )
                    // Authentication/permission/not-found errors are terminal for this request.
                    if (response.code() == 401 ||
                        response.code() == 403 ||
                        response.code() == 404
                    ) {
                        request.requestStatus = STATUS_FAILED
                        repository.updateRequest(request)
                    }
                    // Transient failures (including rate limits and server errors) stay queued.
                }
            } catch (e: Exception) {
                // Network interruptions must not falsely mark a still-running job as failed.
                Log.w("TAGSTATUS", "Temporary status check error for ${request.uuid}", e)
            }
        }

        _list.postValue(repository.getRequests())
    }
}
