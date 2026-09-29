package com.rollinkxx.stockitydemo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rollinkxx.stockitydemo.domain.MarketDataStatus
import com.rollinkxx.stockitydemo.domain.SignalEngine

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { StockityDemoApp() }
    }
}

@Composable
private fun StockityDemoApp() {
    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            DashboardScreen()
        }
    }
}

@Composable
private fun DashboardScreen() {
    val context = LocalContext.current
    val decision = SignalEngine.evaluate(MarketDataStatus.UNCONFIGURED)

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Text("STOCKITY DEMO TRADER", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text("DEMO-ONLY APP · NO LIVE ORDER ROUTE", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)

        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)) {
            Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("ACCOUNT MODE: UNKNOWN", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("No Stockity session is connected. Unknown or real accounts are always blocked from trading.")
            }
        }

        Card {
            Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("MARKET DATA", fontWeight = FontWeight.Bold)
                    Text("UNCONFIGURED", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                }
                Text("Live Stockity quotes and candles are unavailable. No prices are shown or synthesized.")
                Text("Last update: —     Data latency: —")
            }
        }

        Card {
            Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("SIGNAL", fontWeight = FontWeight.Bold)
                Text(decision.signal.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Text("Confidence: ${decision.confidence}%")
                decision.reasons.forEach { Text("• $it") }
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {}, enabled = false, modifier = Modifier.fillMaxWidth()) {
                Text("Start Auto Demo Trading — unavailable")
            }
            OutlinedButton(onClick = {}, enabled = false, modifier = Modifier.fillMaxWidth()) {
                Text("Place Demo Trade — blocked")
            }
        }

        Card {
            Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Why are trades disabled?", fontWeight = FontWeight.Bold)
                Text("A trusted official demo-account check and documented, permitted live-data interface have not been verified. The app fails closed: no account is assumed to be DEMO, and no trade request is sent.")
            }
        }

        val registrationIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://stockity.com/trading"))
        OutlinedButton(onClick = { context.startActivity(registrationIntent) }, modifier = Modifier.fillMaxWidth()) {
            Text("Open official Stockity website")
        }
        Text(
            "Practice / research foundation only. No investment advice. The current build does not connect to Stockity, display market prices, or execute trades.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(12.dp))
    }
}
