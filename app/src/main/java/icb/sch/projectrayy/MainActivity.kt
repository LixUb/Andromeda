package icb.sch.projectrayy

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Upload
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import icb.sch.projectrayy.ui.theme.ProjectRayyTheme
import icb.sch.projectrayy.ui.theme.SkyBlue
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProjectRayyTheme {
                MainScreen()
            }
        }
    }
}

// Data class for school complaints
data class SchoolComplaint(
    val id: String,
    val title: String,
    val description: String,
    val location: String,
    val date: String,
    val status: String,
    val category: String
)

data class DrawerItem(val title: String, val icon: ImageVector, val onClick: () -> Unit)
data class NavItem(val title: String, val icon: ImageVector)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBarWithSearch(title: String, drawerState: DrawerState) {
    val showSearchBar = remember { mutableStateOf(false) }
    val searchText = remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()

    TopAppBar(
        title = {
            if (showSearchBar.value) {
                TextField(
                    value = searchText.value,
                    onValueChange = { searchText.value = it },
                    placeholder = { Text("Find issues...", color = Color.White.copy(alpha = 0.7f)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    trailingIcon = {
                        if (searchText.value.isNotEmpty()) {
                            IconButton(onClick = {
                                searchText.value = ""
                            }) {
                                Icon(Icons.Filled.Clear, "Clear", tint = Color.White)
                            }
                        }
                    },
                    leadingIcon = {
                        IconButton(onClick = {
                            showSearchBar.value = false
                            searchText.value = ""
                            focusManager.clearFocus()
                        }) {
                            Icon(Icons.Filled.ArrowBack, "Back", tint = Color.White)
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = { focusManager.clearFocus() }
                    )
                )
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = title,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "MAN IC",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 14.sp
                    )
                }
            }
        },
        navigationIcon = {
            if(!showSearchBar.value) {
                IconButton(
                    onClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    },
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f))
                ) {
                    Icon(Icons.Rounded.Menu, "Menu", tint = Color.White)
                }
            }
        },
        actions = {
            if(!showSearchBar.value) {
                IconButton(
                    onClick = { showSearchBar.value = true },
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f))
                ) {
                    Icon(Icons.Filled.Search, "Search", tint = Color.White)
                }
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = { /* Handle notifications */ },
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha =.2f))
                ) {
                    Icon(Icons.Rounded.Notifications, "Notifications", tint = Color.White)
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = SkyBlue
        ),
        modifier = Modifier.shadow(8.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfessionalReportDialog(onDismiss: () -> Unit, onSubmit: (String, String, String, String, String, Uri?) -> Unit) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var selectedImage by remember { mutableStateOf<Uri?>(null) }
    var showErrors by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val showDatePicker = remember { mutableStateOf(false) }
    val showCategoryDialog = remember { mutableStateOf(false) }

    val titleError = title.isEmpty() && showErrors
    val descriptionError = description.isEmpty() && showErrors
    val locationError = location.isEmpty() && showErrors
    val dateError = date.isEmpty() && showErrors
    val categoryError = category.isEmpty() && showErrors

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImage = uri
    }

    val datePickerState = rememberDatePickerState()

    val categories = listOf("SarPras", "Keasramaan", "Akademik", "Kesiswaan", "Humas", "Lainnya")

    if (showDatePicker.value) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker.value = false },
            confirmButton = {
                Button(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                            date = formatter.format(Date(it))
                        }
                        showDatePicker.value = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SkyBlue)
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDatePicker.value = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showCategoryDialog.value) {
        AlertDialog(
            onDismissRequest = { showCategoryDialog.value = false },
            title = { Text("Select Category") },
            text = {
                Column {
                    categories.forEach { cat ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    category = cat
                                    showCategoryDialog.value = false
                                }
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = when (cat) {
                                    "SarPras" -> Icons.Filled.Home
                                    "Keasramaan" -> Icons.Filled.Person
                                    "Akademik" -> Icons.Filled.School
                                    "Kebersihan" -> Icons.Filled.CheckCircle
                                    "Keamanan" -> Icons.Filled.LocationOn
                                    else -> Icons.Filled.Info
                                },
                                contentDescription = cat,
                                tint = SkyBlue
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(cat)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showCategoryDialog.value = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 16.dp)
            ) {
                // Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SkyBlue)
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Report School Issue",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                // Content
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .weight(1f, fill = false),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Title field
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Issue Title") },
                        placeholder = { Text("Describe the issue briefly") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        isError = titleError,
                        supportingText = {
                            if (titleError) {
                                Text("Title is required", color = MaterialTheme.colorScheme.error)
                            }
                        },
                        leadingIcon = {
                            Icon(Icons.Filled.Info, contentDescription = "Title", tint = SkyBlue)
                        },
                        singleLine = true
                    )

                    // Category selection
                    OutlinedTextField(
                        value = category,
                        onValueChange = { },
                        label = { Text("Category") },
                        placeholder = { Text("Select issue category") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        isError = categoryError,
                        supportingText = {
                            if (categoryError) {
                                Text("Category is required", color = MaterialTheme.colorScheme.error)
                            }
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = when (category) {
                                    "SarPras" -> Icons.Filled.Home
                                    "Keasramaan" -> Icons.Filled.Person
                                    "Akademik" -> Icons.Filled.School
                                    "Kebersihan" -> Icons.Filled.CheckCircle
                                    "Keamanan" -> Icons.Filled.LocationOn
                                    else -> Icons.Filled.Menu
                                },
                                contentDescription = "Category",
                                tint = SkyBlue
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { showCategoryDialog.value = true }) {
                                Icon(Icons.Filled.ArrowBack,
                                    contentDescription = "Select category",
                                    modifier = Modifier.rotate(270f)
                                )
                            }
                        },
                        readOnly = true
                    )

                    // Description field
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description") },
                        placeholder = { Text("Provide detailed information about the issue") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        maxLines = 5,
                        shape = RoundedCornerShape(8.dp),
                        isError = descriptionError,
                        supportingText = {
                            if (descriptionError) {
                                Text("Description is required", color = MaterialTheme.colorScheme.error)
                            } else {
                                Text("${description.length}/500 characters")
                            }
                        }
                    )

                    // Location field
                    OutlinedTextField(
                        value = location,
                        onValueChange = { location = it },
                        label = { Text("Location") },
                        placeholder = { Text("Specify where in the school") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        isError = locationError,
                        supportingText = {
                            if (locationError) {
                                Text("Location is required", color = MaterialTheme.colorScheme.error)
                            }
                        },
                        leadingIcon = {
                            Icon(Icons.Filled.LocationOn, contentDescription = "Location", tint = SkyBlue)
                        },
                        singleLine = true
                    )

                    // Date field
                    OutlinedTextField(
                        value = date,
                        onValueChange = { date = it },
                        label = { Text("Date Observed") },
                        placeholder = { Text("When was the issue noticed") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        isError = dateError,
                        supportingText = {
                            if (dateError) {
                                Text("Date is required", color = MaterialTheme.colorScheme.error)
                            }
                        },
                        leadingIcon = {
                            Icon(Icons.Filled.DateRange, contentDescription = "Date", tint = SkyBlue)
                        },
                        trailingIcon = {
                            IconButton(onClick = { showDatePicker.value = true }) {
                                Icon(Icons.Filled.DateRange, contentDescription = "Select date")
                            }
                        },
                        readOnly = true
                    )

                    // Image selection section
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = 1.dp,
                                color = if (selectedImage == null && showErrors) MaterialTheme.colorScheme.error else Color.LightGray,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Photo,
                                contentDescription = "Image",
                                tint = SkyBlue
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Evidence Photo",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        if (selectedImage != null) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(150.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.LightGray)
                            ) {
                                // This would display the image in a real app
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(SkyBlue.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.CheckCircle,
                                            contentDescription = "Image Selected",
                                            tint = SkyBlue
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Image Selected", color = SkyBlue)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedButton(
                                onClick = { pickImageLauncher.launch("image/*") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, SkyBlue)
                            ) {
                                Text("Change Image", color = SkyBlue)
                            }
                        } else {
                            Button(
                                onClick = { pickImageLauncher.launch("image/*") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = SkyBlue)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(Icons.Filled.Photo, contentDescription = "Upload Photo")
                                    Text("Upload Photo")
                                }
                            }

                            if (selectedImage == null && showErrors) {
                                Text(
                                    "Photo evidence is required",
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(top = 4.dp, start = 4.dp)
                                )
                            }
                        }
                    }
                }

                Divider(modifier = Modifier.padding(16.dp))

                // Action buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, Color.Gray)
                    ) {
                        Text("Cancel", color = Color.Gray)
                    }

                    Button(
                        onClick = {
                            showErrors = true
                            if (title.isNotEmpty() && description.isNotEmpty() &&
                                location.isNotEmpty() && date.isNotEmpty() &&
                                category.isNotEmpty() && selectedImage != null) {
                                onSubmit(title, description, location, date, category, selectedImage)
                            }
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SkyBlue)
                    ) {
                        Text("Submit Report")
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedNavItem by remember { mutableIntStateOf(1) } // Default to center item (Upload)
    val context = LocalContext.current
    var showUploadDialog by remember { mutableStateOf(false) }

    // Sample data for school complaints
    val schoolComplaints = listOf(
        SchoolComplaint("1", "Pintu Asrama Rusak", "Pintu belakang kamar 19 rusak (gagangnya lepas).", "room 19 Aslam", "17/03/2025", "Pending", "SarPras"),
        SchoolComplaint("2", "XI D Smart TV", "Smart TV kelas XI D rusak tidak bisa dijalankan", "XI D CLASS", "15/03/2025", "Completed", "SarPras"),
        SchoolComplaint("3", "Plafon Masjid Rusak", "Plafon masjid rusak, bagian bannat plafonnya berlubang", "Masjid Miftahul Ulum", "14/03/2025", "In Progress", "SarPras"),
        SchoolComplaint("4", "Ketertiban Kantin", "Siswa MAN INSAN CENDEKIA tidak tertib dalam mengambil makanan dan minuman dari kantin", "Kantin MAN IC Batam", "13/03/2025", "Completed", "Keasramaan"),
    )

    val drawerItems = listOf(
        DrawerItem("Home", Icons.Filled.Home) {
            scope.launch {
                drawerState.close()
            }
        },
        DrawerItem("Class Manager", Icons.Filled.ManageAccounts) {
            scope.launch {
                drawerState.close()
            }
        },
        DrawerItem("Admin Interface", Icons.Filled.Person) {
            scope.launch {
                drawerState.close()
            }
        },
        DrawerItem("Contact", Icons.Filled.Info) {
            scope.launch {
                drawerState.close()
            }
        }
    )

    // Navigation items
    val navItems = listOf(
        NavItem("History", Icons.Filled.History),
        NavItem("Upload", Icons.Rounded.Upload),
        NavItem("Account", Icons.Filled.AccountCircle)
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .fillMaxWidth(0.65f)
                    .fillMaxHeight(),
                drawerContainerColor = SkyBlue
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    SkyBlue.copy(alpha = 0.7f),
                                    SkyBlue
                                )
                            )
                        )
                        .padding(vertical = 48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Filled.School,
                            contentDescription = "School Logo",
                            tint = Color.White,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                drawerItems.forEach { item ->
                    NavigationDrawerItem(
                        label = { Text(item.title, color = Color.White) },
                        icon = {
                            Icon(
                                item.icon,
                                contentDescription = item.title,
                                tint = Color.White
                            )
                        },
                        selected = false,
                        onClick = item.onClick,
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        colors = NavigationDrawerItemDefaults.colors(
                            unselectedContainerColor = SkyBlue,
                            unselectedIconColor = Color.White,
                            unselectedTextColor = Color.White
                        )
                    )
                }
            }
        },
    ) {
        Scaffold(
            topBar = { MyTopAppBarWithSearch(title = "Laporin", drawerState = drawerState) },
            bottomBar = {
                NavigationBar(
                    containerColor = SkyBlue,
                    contentColor = Color.White,
                    tonalElevation = 8.dp,
                    modifier = Modifier.shadow(8.dp)
                ) {
                    navItems.forEachIndexed { index, item ->
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.title
                                )
                            },
                            label = { Text(item.title) },
                            selected = selectedNavItem == index,
                            onClick = {
                                selectedNavItem = index
                                when (index) {
                                    0 -> { // History
                                        context.startActivity(Intent(context, HistoryActivity::class.java))
                                    }
                                    1 -> { // Upload - stay on home and show upload dialog
                                        showUploadDialog = true
                                    }
                                    2 -> { // Account
                                        context.startActivity(Intent(context, LoginActivity::class.java))
                                    }
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.White,
                                selectedTextColor = Color.White,
                                indicatorColor = Color.White.copy(alpha = 0.2f),
                                unselectedIconColor = Color.White.copy(alpha = 0.7f),
                                unselectedTextColor = Color.White.copy(alpha = 0.7f)
                            )
                        )
                    }
                }
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = { showUploadDialog = true },
                    icon = { Icon(Icons.Rounded.Upload, contentDescription = "Upload") },
                    text = { Text("Report Issue") },
                    containerColor = SkyBlue,
                    contentColor = Color.White,
                    modifier = Modifier.shadow(8.dp, shape = RoundedCornerShape(16.dp))
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(Color(0xFFF5F5F5)) // Light gray background
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Category chips would go here
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Recent Issues",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "View All",
                            color = SkyBlue,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    LazyColumn(
                        contentPadding = PaddingValues(bottom = 80.dp) // Add padding for FAB
                    ) {
                        itemsIndexed(schoolComplaints) { index, complaint ->
                            SchoolComplaintCard(complaint, index)
                        }
                    }
                }
            }

            if (showUploadDialog) {
                ProfessionalReportDialog(
                    onDismiss = { showUploadDialog = false },
                    onSubmit = { title, description, location, date, category, imageUri ->
                        // Handle the form submission with all fields
                        // In a real app, you would process the upload with these parameters
                        showUploadDialog = false
                    }
                )
            }
        }
    }
}
@Composable
fun SchoolComplaintCard(complaint: SchoolComplaint, index: Int) {
    val statusColor = when (complaint.status) {
        "Completed" -> Color(0xFF4CAF50) // Green
        "In Progress" -> Color(0xFF2196F3) // Blue
        "Pending" -> Color(0xFFFFC107) // Yellow
        else -> Color(0xFFFF5722) // Orange for "In Review" or others
    }

    // Staggered animation for cards
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = true) {
        delay(100L * index)
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(300))
    ) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                // Header with category badge
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    SkyBlue,
                                    SkyBlue.copy(alpha = 0.8f)
                                )
                            )
                        )
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = complaint.category,
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )

                        Box(
                            modifier = Modifier
                                .background(Color.White, shape = RoundedCornerShape(12.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.CheckCircle,
                                    contentDescription = "Status",
                                    tint = statusColor,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = complaint.status,
                                    color = statusColor,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                // Content
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.School,
                            contentDescription = "School Complaint",
                            tint = SkyBlue,
                            modifier = Modifier.size(24.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = complaint.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = complaint.description,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Divider(color = Color.LightGray, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.LocationOn,
                                contentDescription = "Location",
                                modifier = Modifier.size(16.dp),
                                tint = SkyBlue
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = complaint.location,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.DateRange,
                                contentDescription = "Date",
                                modifier = Modifier.size(16.dp),
                                tint = SkyBlue
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = complaint.date,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}
