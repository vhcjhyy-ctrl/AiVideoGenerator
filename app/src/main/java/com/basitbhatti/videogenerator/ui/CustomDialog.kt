package com.basitbhatti.videogenerator.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.basitbhatti.videogenerator.R

private val DialogGold = Color(0xFFD6AE55)
private val DialogPapyrus = Color(0xFFF4E8CE)
private val DialogNile = Color(0xFF1C2A28)

@Composable
fun CustomDialog(
    showDialog: Boolean,
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit,
) {
    AnimatedVisibility(
        visible = showDialog,
        enter = fadeIn(animationSpec = tween(durationMillis = 200)),
        exit = fadeOut(animationSpec = tween(durationMillis = 200))
    ) {
        Dialog(
            onDismissRequest = onDismissRequest,
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    Modifier
                        .pointerInput(Unit) { detectTapGestures { } }
                        .shadow(12.dp, shape = RoundedCornerShape(22.dp))
                        .width(320.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .background(MaterialTheme.colorScheme.surface),
                    contentAlignment = Alignment.Center
                ) {
                    content()
                }
            }
        }
    }
}

@Composable
fun ProgressDialog(
    modifier: Modifier = Modifier, onDismissRequest: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(260.dp),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = DialogNile)
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 22.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.loading),
                contentDescription = "Loading",
                modifier = Modifier.size(68.dp)
            )
            Spacer(modifier = Modifier.height(17.dp))
            Text(
                text = "تم إرسال طلبك",
                color = DialogPapyrus,
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "يمكنك متابعة حالة الفيديو من سجل الطلبات",
                color = Color(0xFFC7BFAE),
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(17.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .height(49.dp),
                shape = RoundedCornerShape(13.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DialogGold,
                    contentColor = Color(0xFF101B1B)
                ),
                onClick = { onDismissRequest() }
            ) {
                Text(text = "حسنًا", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun ErrorDialog(
    modifier: Modifier = Modifier, onDismissRequest: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(260.dp),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = DialogNile)
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 22.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.warning),
                contentDescription = "Warning",
                modifier = Modifier.size(68.dp)
            )
            Spacer(modifier = Modifier.height(17.dp))
            Text(
                text = "تعذّر إكمال الطلب",
                color = DialogPapyrus,
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "حدثت مشكلة أثناء تنفيذ العملية. حاول مرة أخرى.",
                color = Color(0xFFC7BFAE),
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(17.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .height(49.dp),
                shape = RoundedCornerShape(13.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DialogGold,
                    contentColor = Color(0xFF101B1B)
                ),
                onClick = { onDismissRequest() }
            ) {
                Text(text = "حسنًا", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    CustomDialog(true, {}) {
        ErrorDialog {}
    }
}
