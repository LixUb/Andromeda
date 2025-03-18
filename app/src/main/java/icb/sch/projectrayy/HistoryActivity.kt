package icb.sch.projectrayy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import icb.sch.projectrayy.ui.theme.ProjectRayyTheme

class HistoryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProjectRayyTheme {
                HistoryScreen(onBackPressed = { finish() })
            }
        }
    }
}

data class HistoryItem(
    val id: String,
    val title: String,
    val date: String,
    val location: String,
    val status: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(onBackPressed: () -> Unit) {
    // Sample history data
    val historyItems = listOf(
        HistoryItem("1", "Perbaikan Jalan Berlubang", "12/03/2025", "Jl. Raya Kamal No. 15", "Completed"),
        HistoryItem("2", "Penerangan Jalan", "05/03/2025", "Jl. Pahlawan Blok C", "In Progress"),
        HistoryItem("3", "Saluran Air Tersumbat", "27/02/2025", "Jl. Melati RT 04/02", "Under Review"),
        HistoryItem("4", "Pohon Tumbang", "21/02/2025", "Taman Kota Blok A", "Completed"),
        HistoryItem("5", "Kabel Listrik Putus", "15/02/2025", "Perumahan Indah Blok F5", "Completed")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Report History", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(Icons.Filled.ArrowBack, "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = customGreen
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(historyItems) { item ->
                    HistoryItemCard(item)
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun HistoryItemCard(item: HistoryItem) {
    val statusColor = when (item.status) {
        "Completed" -> Color(0xFF4CAF50)  // Green
        "In Progress" -> Color(0xFF2196F3) // Blue
        else -> Color(0xFFFFA000)          // Amber for Under Review
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.CalendarToday,
                    contentDescription = "Date",
                    tint = Color.Gray,
                    modifier = Modifier.width(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = item.date,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.LocationOn,
                    contentDescription = "Location",
                    tint = Color.Gray,
                    modifier = Modifier.width(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = item.location,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            Divider()
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Status:",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = item.status,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = statusColor
                )
            }
        }
    }
}