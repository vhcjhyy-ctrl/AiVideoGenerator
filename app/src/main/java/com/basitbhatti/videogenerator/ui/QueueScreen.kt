package com.basitbhatti.videogenerator.ui

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.basitbhatti.videogenerator.R
import com.basitbhatti.videogenerator.db.TextRequest
import com.basitbhatti.videogenerator.utils.STATUS_FAILED
import com.basitbhatti.videogenerator.utils.STATUS_IN_QUEUE
import com.basitbhatti.videogenerator.utils.STATUS_SUCCESS
import com.basitbhatti.videogenerator.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private val Papyrus = Color(0xFFF4E8CE)
private val Gold = Color(0xFFD6AE55)
private val DarkNile = Color(0xFF101B1B)
private val CardNile = Color(0xFF1C2A28)

@Composable
fun QueueScreen(modifier: Modifier = Modifier, viewModel: MainViewModel) {
    LaunchedEffect(Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.updateList()
            viewModel.updateRequests()
        }
    }

    val requests = viewModel.listRequests.observeAsState()

    requests.value?.forEach {
        Log.d("TAGRequests", "${it.prompt}")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkNile)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 22.dp, vertical = 22.dp)
        ) {
            Text(
                text = "سجل الفيديوهات",
                color = Papyrus,
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = "تابع طلباتك ونتائج الفيديو التي أنشأتها",
                color = Color(0xFFB9A77F),
                fontSize = 12.sp
            )
        }

        if (!requests.value.isNullOrEmpty()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    start = 8.dp, end = 8.dp, bottom = 18.dp
                )
            ) {
                items(requests.value!!) { item ->
                    QueueItem(item = item, onDelete = {
                        viewModel.deleteRequest(item)
                    })
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 28.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "𓂀", color = Gold, fontSize = 44.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "لا توجد طلبات حتى الآن",
                        color = Papyrus,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "ستظهر هنا الفيديوهات التي تطلب إنشاءها.",
                        color = Color(0xFFB9A77F),
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
fun QueueItem(modifier: Modifier = Modifier, item: TextRequest, onDelete: () -> Unit) {
    val context = LocalContext.current

    val statusBg = when (item.requestStatus) {
        STATUS_SUCCESS -> Color(0xFF315842)
        STATUS_IN_QUEUE -> Color(0xFF6E592D)
        STATUS_FAILED -> Color(0xFF653B37)
        else -> Color(0xFF3A4541)
    }
    val statusLabel = when (item.requestStatus) {
        STATUS_SUCCESS -> "اكتمل"
        STATUS_IN_QUEUE -> "قيد الانتظار"
        STATUS_FAILED -> "تعذّر التنفيذ"
        else -> item.requestStatus
    }

    Card(
        modifier = modifier
            .padding(vertical = 7.dp, horizontal = 10.dp)
            .fillMaxWidth()
            .height(132.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = CardNile),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF293936)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.video),
                    contentDescription = "Video",
                    modifier = Modifier.size(53.dp)
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 13.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = item.prompt,
                    color = Papyrus,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2
                )
                Spacer(Modifier.height(10.dp))
                Row(
                    modifier = Modifier.height(34.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = statusLabel,
                        color = Papyrus,
                        modifier = Modifier
                            .clip(RoundedCornerShape(30.dp))
                            .background(statusBg)
                            .padding(horizontal = 11.dp, vertical = 6.dp),
                        fontSize = 11.sp
                    )

                    if (item.requestStatus.equals(STATUS_SUCCESS)) {
                        Spacer(Modifier.width(8.dp))
                        Row(
                            modifier = Modifier
                                .clickable {
                                    val intent = Intent(ACTION_VIEW)
                                    intent.setData(Uri.parse(item.url))
                                    context.startActivity(intent)
                                }
                                .clip(RoundedCornerShape(30.dp))
                                .background(Gold)
                                .padding(horizontal = 9.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Image(
                                painter = painterResource(R.drawable.ic_play),
                                contentDescription = "Play",
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "عرض",
                                color = DarkNile,
                                modifier = Modifier.padding(start = 4.dp),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(28.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.delete),
                    contentDescription = "Delete",
                    modifier = Modifier
                        .size(19.dp)
                        .clickable { onDelete() }
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    QueueItem(
        item = TextRequest(
            0,
            "Create an ad video for my clothing brand",
            STATUS_SUCCESS,
            "inQueue",
            "1234"
        ),
        onDelete = {}
    )
}
