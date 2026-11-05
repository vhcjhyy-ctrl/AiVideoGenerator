package com.basitbhatti.videogenerator.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.basitbhatti.videogenerator.model.TextRequestBody
import com.basitbhatti.videogenerator.viewmodel.MainViewModel

private val Papyrus = Color(0xFFF4E8CE)
private val DeepNile = Color(0xFF101B1B)
private val Gold = Color(0xFFD6AE55)
private val MutedGold = Color(0xFF9B7B3E)

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel
) {
    var showDialog by remember { mutableStateOf(false) }
    var prompt by remember { mutableStateOf("") }
    val context = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF0B1414), Color(0xFF172625), Color(0xFF0B1414))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 22.dp, vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "𓂀",
                color = Gold,
                fontSize = 52.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "TUTANKHAMUN",
                color = Papyrus,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 3.sp
            )
            Text(
                text = "AI VIDEO STUDIO",
                color = Gold,
                fontSize = 11.sp,
                letterSpacing = 4.sp
            )
            Spacer(modifier = Modifier.height(24.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1C2A28)),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "اصنع مشهدك القادم",
                        color = Papyrus,
                        fontSize = 21.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(7.dp))
                    Text(
                        text = "اكتب وصف الفيديو الذي تتخيله، ودع الاستوديو يحوّله إلى مشهد.",
                        color = Color(0xFFC7BFAE),
                        fontSize = 13.sp,
                        lineHeight = 21.sp
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "وصف الفيديو",
                        color = Gold,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = prompt,
                        onValueChange = { prompt = it },
                        placeholder = {
                            Text("مثال: مشهد سينمائي للأهرامات وقت الغروب...")
                        },
                        minLines = 4,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Papyrus,
                            unfocusedTextColor = Papyrus,
                            focusedBorderColor = Gold,
                            unfocusedBorderColor = MutedGold,
                            focusedPlaceholderColor = Color(0xFF9F9A8D),
                            unfocusedPlaceholderColor = Color(0xFF9F9A8D),
                            cursorColor = Gold
                        )
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Gold,
                            contentColor = DeepNile
                        ),
                        onClick = {
                            if (prompt.isBlank()) {
                                Toast.makeText(context, "اكتب وصف الفيديو أولاً", Toast.LENGTH_SHORT).show()
                            } else {
                                viewModel.sendTextRequest(TextRequestBody(prompt.trim()))
                                showDialog = true
                            }
                        }
                    ) {
                        Text(
                            text = "إنشاء الفيديو",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = "من الفكرة إلى المشهد — خطوة واحدة",
                color = Color(0xFFB9A77F),
                fontSize = 12.sp
            )
            CustomDialog(
                showDialog = showDialog,
                onDismissRequest = { showDialog = false }
            ) {
                ProgressDialog { showDialog = false }
            }
        }
    }
}
