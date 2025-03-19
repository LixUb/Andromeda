package icb.sch.projectrayy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.HourglassEmpty
import androidx.compose.material.icons.outlined.Pending
import androidx.compose.material.icons.outlined.PlayCircleOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import icb.sch.projectrayy.ui.theme.ProjectRayyTheme
import icb.sch.projectrayy.ui.theme.SkyBlue
import java.text.SimpleDateFormat
import java.util.*

class HistoryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProjectRayyTheme {
                HistoryScreen(onNavigateUp = { finish() })
            }
        }
    }
}

data class ReportItem(
    val id: String,
    val title: String,
    val description: String,
    val location: String,
    val date: String,
    val status: ReportStatus,
    val priority: ReportPriority = ReportPriority.MEDIUM
)

enum class ReportStatus(val label: String, val icon: ImageVector, val color: Color) {
    PENDING("Pending", Icons.Outlined.Pending, Color(0xFFFFC107)),
    IN_PROGRESS("In Progress", Icons.Outlined.PlayCircleOutline, Color(0xFF2196F3)),
    COMPLETED("Completed", Icons.Outlined.CheckCircle, Color(0xFF4CAF50)),
    IN_REVIEW("In Review", Icons.Outlined.HourglassEmpty, Color(0xFFFF5722))
}

enum class ReportPriority(val label: String, val color: Color) {
    LOW("Low", Color(0xFF8BC34A)),
    MEDIUM("Medium", Color(0xFFFFC107)),
    HIGH("High", Color(0xFFF44336))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(onNavigateUp: () -> Unit) {
    // Sample data
    val reportItems = listOf(
        ReportItem(
            "1",
            "Pintu Asrama Rusak",
            "Pintu belakang kamar 19 rusak (gagangnya lepas).",
            "Room 19 Aslam",
            "17/03/2025",
            ReportStatus.PENDING,
            ReportPriority.MEDIUM
        ),
        ReportItem(
            "2",
            "XI D Smart TV",
            "Smart TV kelas XI D rusak tidak bisa dijalankan",
            "XI D CLASS",
            "15/03/2025",
            ReportStatus.COMPLETED,
            ReportPriority.HIGH
        ),
        ReportItem(
            "3",
            "Plafon Masjid Rusak",
            "Plafon masjid rusak, bagian bannat plafonnya berlubang",
            "Masjid Miftahul Ulum",
            "14/03/2025",
            ReportStatus.PENDING,
            ReportPriority.HIGH
        ),
        ReportItem(
            "4",
            "AC Perpustakaan",
            "AC perpustakaan tidak dingin, perlu service",
            "Perpustakaan Lantai 2",
            "12/03/2025",
            ReportStatus.IN_PROGRESS,
            ReportPriority.MEDIUM
        ),
        ReportItem(
            "5",
            "Kebocoran Atap Lab Komputer",
            "Atap laboratorium komputer bocor ketika hujan deras",
            "Lab Komputer 2",
            "10/03/2025",
            ReportStatus.IN_REVIEW,
            ReportPriority.HIGH
        )
    )

    var filterStatus by remember { mutableStateOf<ReportStatus?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredReports = remember(filterStatus, reportItems, searchQuery) {
        reportItems.filter { report ->
            (filterStatus == null || report.status == filterStatus) &&
                    (searchQuery.isEmpty() || report.title.contains(searchQuery, ignoreCase = true) ||
                            report.description.contains(searchQuery, ignoreCase = true) ||
                            report.location.contains(searchQuery, ignoreCase = true))
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Report History", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SkyBlue
                ),
                actions = {
                    IconButton(onClick = { /* Search functionality */ }) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = { /* Filter functionality */ }) {
                        Icon(
                            imageVector = Icons.Filled.FilterList,
                            contentDescription = "Filter",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF5F7FA))
        ) {
            // Status Filter Chips
            StatusFilterChips(
                selectedStatus = filterStatus,
                onStatusSelected = { filterStatus = it }
            )

            // Report List
            if (filteredReports.isEmpty()) {
                EmptyStateMessage()
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    items(filteredReports) { report ->
                        ReportCard(report)
                    }
                }
            }
        }
    }
}

@Composable
fun StatusFilterChips(
    selectedStatus: ReportStatus?,
    onStatusSelected: (ReportStatus?) -> Unit
) {
    val statuses = ReportStatus.values().toList()

    ScrollableTabRow(
        selectedTabIndex = if (selectedStatus == null) 0 else statuses.indexOf(selectedStatus) + 1,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        edgePadding = 16.dp,
        containerColor = Color.Transparent,
        divider = {},
        indicator = {}
    ) {
        FilterChip(
            selected = selectedStatus == null,
            onClick = { onStatusSelected(null) },
            label = { Text("All") },
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = SkyBlue,
                selectedLabelColor = Color.White
            ),
            modifier = Modifier.padding(end = 8.dp)
        )

        statuses.forEach { status ->
            FilterChip(
                selected = selectedStatus == status,
                onClick = { onStatusSelected(status) },
                label = { Text(status.label) },
                leadingIcon = {
                    Icon(
                        imageVector = status.icon,
                        contentDescription = null,
                        tint = status.color
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = status.color,
                    selectedLabelColor = Color.White
                ),
                modifier = Modifier.padding(end = 8.dp)
            )
        }
    }
}

@Composable
fun EmptyStateMessage() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Filled.HistoryToggleOff,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = Color.Gray.copy(alpha = 0.5f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No reports found",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Adjust your filters or create a new report",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun ReportCard(report: ReportItem) {
    val formattedDate = remember(report.date) {
        formatDate(report.date)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header with title and status
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = report.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                StatusBadge(report.status)
            }

            // Priority badge
            Spacer(modifier = Modifier.height(8.dp))
            PriorityBadge(report.priority)

            // Description
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = report.description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = MaterialTheme.colorScheme.surfaceVariant)
            Spacer(modifier = Modifier.height(16.dp))

            // Footer with metadata
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Location
                Icon(
                    imageVector = Icons.Filled.LocationOn,
                    contentDescription = "Location",
                    modifier = Modifier.size(16.dp),
                    tint = Color.Gray
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = report.location,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // Date
                Icon(
                    imageVector = Icons.Filled.DateRange,
                    contentDescription = "Date",
                    modifier = Modifier.size(16.dp),
                    tint = Color.Gray
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = formattedDate,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun StatusBadge(status: ReportStatus) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(status.color.copy(alpha = 0.15f))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = status.icon,
            contentDescription = null,
            tint = status.color,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = status.label,
            color = status.color,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun PriorityBadge(priority: ReportPriority) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(priority.color.copy(alpha = 0.1f))
            .padding(horizontal = 6.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(priority.color)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = priority.label,
            color = priority.color,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Medium
        )
    }
}

fun formatDate(dateStr: String): String {
    // Sample implementation - could be expanded for more sophisticated date formatting
    return try {
        val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val date = inputFormat.parse(dateStr)
        outputFormat.format(date ?: return dateStr)
    } catch (e: Exception) {
        dateStr
    }
}