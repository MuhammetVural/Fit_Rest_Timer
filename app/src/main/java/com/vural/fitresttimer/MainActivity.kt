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
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
            var selectedMode by remember { mutableStateOf("bodybuilding") }
            var lastSentMode by remember { mutableStateOf("bodybuilding") }
            LaunchedEffect(Unit) {
                startTimerService(selectedMode)
                lastSentMode = selectedMode
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
                    onPowerButtonClick = { startTimerService(selectedMode) }
                )
            }
        }
    }
}
@Composable
fun ModeSelectionScreen(
    selectedMode: String,
    onModeSelected: (String) -> Unit,
    onPowerButtonClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            if (selectedMode == "bodybuilding") {
                // Seçili (aktif) olan buton outlined ve silik görünüyor
                OutlinedButton(
                    onClick = { /* seçili olduğu için tıklanamaz */ },
                    enabled = false,
                    colors = ButtonDefaults.outlinedButtonColors(
                        // Açık griye yakın bir arka plan tonu ver
                        disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        // Yazı rengini gri ama koyu tut
                        disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                ) {
                    Text(
                        "Bodybuilding\n(30/45/60)",
                        // Yazı tipi istersen daha ince veya italik yapılabilir
                    )
                }
                Spacer(Modifier.width(16.dp))
                // Pasif (tıklanabilir) olan buton canlı ve dolu görünüyor
                Button(
                    onClick = { onModeSelected("powerlifting") },
                    enabled = true,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("Powerlifting\n(1.5dk/2dk/3dk)")
                }
            } else {
                // Pasif (tıklanabilir) olan buton canlı ve dolu görünüyor
                Button(
                    onClick = { onModeSelected("bodybuilding") },
                    enabled = true,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("Bodybuilding\n(30/45/60)")
                }
                Spacer(Modifier.width(16.dp))
                // Seçili (aktif) olan buton outlined ve silik görünüyor
                OutlinedButton(
                    onClick = { onModeSelected("powerlifting") },
                    enabled = false,
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary)
                ) {
                    Text("Powerlifting\n(1.5dk/2dk/3dk)")
                }
            }
        }
        Spacer(Modifier.height(32.dp))
        Button(
            onClick = onPowerButtonClick,
            modifier = Modifier
        ) {
            Text("Geri Sayımı Yeniden Aktif Et", fontSize = TextUnit(12f, TextUnitType.Sp))
        }
        Spacer(Modifier.height(16.dp))
        Text("Aktif Mod: $selectedMode")
    }
}