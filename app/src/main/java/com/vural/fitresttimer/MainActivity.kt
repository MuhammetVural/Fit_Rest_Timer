package com.vural.fitresttimer
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.vural.fitresttimer.ui.theme.FitRestTimerTheme

class MainActivity : ComponentActivity() {
    private fun requestNotificationPermissionIfNeeded() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            startTimerService("bodybuilding")
        }
    }

    private fun startTimerService(selectedMode: String) {
        val intent = Intent(this, TimerService::class.java)
        intent.putExtra("mode", selectedMode)
        startForegroundService(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestNotificationPermissionIfNeeded()
        setContent {
            var isBodybuildingAlertEnabled by remember { mutableStateOf(true) }
            var isPowerliftingAlertEnabled by remember { mutableStateOf(false) }
            var selectedMode by remember { mutableStateOf("bodybuilding") }
            var lastSentMode by remember { mutableStateOf("bodybuilding") }
            LaunchedEffect(Unit) {
                if (ActivityCompat.checkSelfPermission(
                        this@MainActivity,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    startTimerService(selectedMode)
                    lastSentMode = selectedMode
                }
            }

            FitRestTimerTheme {
                ModeSelectionScreen(
                    selectedMode = selectedMode,
                    onModeSelected = { mode ->
                        if (mode != lastSentMode) {     // Mod değiştiyse bildirimi güncelle
                            selectedMode = mode
                            startTimerService(mode)
                            lastSentMode = mode
                        }
                    },
                    onPowerButtonClick = { startTimerService(selectedMode) },
                    isAlertEnabled = isBodybuildingAlertEnabled,
                    onAlertToggle = {isChecked -> isBodybuildingAlertEnabled = isChecked
                        if (isChecked){ isPowerliftingAlertEnabled = false
                        startTimerService("bodybuilding")
                            selectedMode = "bodybuilding"
                        }
                        else if (!isPowerliftingAlertEnabled) {
                            isPowerliftingAlertEnabled = true
                            selectedMode = "powerlifting"
                            startTimerService("powerlifting")
                        }
                                    },
                    isPowerliftingAlertEnabled = isPowerliftingAlertEnabled,
                    onPowerliftingToggle = { isChecked ->
                        isPowerliftingAlertEnabled = isChecked
                        if (isChecked) {
                            isBodybuildingAlertEnabled = false
                            selectedMode = "powerlifting"  // 💥 aktif mod güncellendi
                            startTimerService("powerlifting")
                        } else if (!isBodybuildingAlertEnabled) {
                            isBodybuildingAlertEnabled = true
                            selectedMode = "bodybuilding"  // 💥 aktif mod değişti
                            startTimerService("bodybuilding")
                        }
                    }

                )
            }
        }
    }
}

@Composable

fun ToggleCardRow(
    title: String,
    isChecked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .shadow(
                elevation = 15.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = Color.Black.copy(alpha = 0.8f),
                spotColor = Color.Black.copy(alpha = 0.38f)
            )
            .background(Color(0xFFF9F9F9), shape = RoundedCornerShape(16.dp))
            .clickable { onCheckedChange(!isChecked) } // toggle when clicked
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = title, style = MaterialTheme.typography.bodyLarge)
            Switch(
                checked = isChecked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedTrackColor = Color(0xFF000000),
                    uncheckedTrackColor = Color(0xFFEDEDED),
                    checkedThumbColor = Color.White,
                    uncheckedThumbColor = Color.White
                )
            )
        }
    }
}
@Preview(
    name = "Full Screen Preview",
    showSystemUi = true,
    showBackground = true,
    device = "spec:width=411dp,height=891dp,dpi=420"
)
@Composable
fun FullModeSelectionScreenPreview() {
    FitRestTimerTheme {
        ModeSelectionScreen(
            selectedMode = "bodybuilding",
            onModeSelected = {},
            onPowerButtonClick = {},
            isAlertEnabled = true,
            onAlertToggle = {},
            isPowerliftingAlertEnabled = false,
            onPowerliftingToggle = {}

        )
    }
}

@Composable
fun ModeSelectionScreen(
    selectedMode: String,
    onModeSelected: (String) -> Unit,
    onPowerButtonClick: () -> Unit,
    isAlertEnabled: Boolean,
    onAlertToggle: (Boolean) -> Unit,
    isPowerliftingAlertEnabled: Boolean,
    onPowerliftingToggle: (Boolean) -> Unit
) {
    Column(

        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F2F7)),

        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(120.dp)
                    .shadow(
                        elevation = 10.dp,
                        shape = RoundedCornerShape(24.dp),
                        ambientColor = Color.Black.copy(alpha = 0.08f),
                        spotColor = Color.Black.copy(alpha = 0.08f)
                    )
                    .background(Color.White, shape = RoundedCornerShape(24.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Aktif Mod",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    Text(
                        text = selectedMode.replaceFirstChar { it.uppercaseChar() },
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.Black
                    )
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(120.dp)
                    .clickable { onPowerButtonClick() }
                    .shadow(
                        elevation = 10.dp,
                        shape = RoundedCornerShape(24.dp),
                        ambientColor = Color.Black.copy(alpha = 0.08f),
                        spotColor = Color.Black.copy(alpha = 0.08f)
                    )
                    .background(Color.White, shape = RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center
            ) {

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Send Notification",
                        tint = Color.Black,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Yeniden Bildirim Gönder",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.Black
                    )
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        ToggleCardRow(
            title = "Powerlifting Modu",
            isChecked = isPowerliftingAlertEnabled,
            onCheckedChange = onPowerliftingToggle
        )
        ToggleCardRow(
            title = "Bodybuilding Modu",
            isChecked = isAlertEnabled,
            onCheckedChange = onAlertToggle
        )
        // Card-styled Aktif Mod block

        Spacer(Modifier.height(24.dp))

    }
}