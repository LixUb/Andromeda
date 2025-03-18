package icb.sch.projectrayy

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import icb.sch.projectrayy.ui.theme.ProjectRayyTheme
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

data class DrawerItem(val title: String, val icon: ImageVector, val onClick: () -> Unit)
data class NavItem(val title: String, val icon: ImageVector)

// Define a custom green color
val customGreen = Color(0xFF2E7D32) // Dark Green

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
                    placeholder = { Text("Search", color = Color.White.copy(alpha = 0.7f)) },
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
                Text(text = title, color = Color.White)
            }
        },
        navigationIcon = {
            if(!showSearchBar.value) {
                IconButton(onClick = {
                    scope.launch {
                        drawerState.open()
                    }
                }) {
                    Icon(Icons.Filled.Menu, "Menu", tint = Color.White)
                }
            }
        },
        actions = {
            if(!showSearchBar.value) {
                IconButton(onClick = {
                    showSearchBar.value = true
                }) {
                    Icon(Icons.Filled.Search, "Search", tint = Color.White)
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = customGreen
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadBottomSheet(onDismiss: () -> Unit) {
    val sheetState = rememberModalBottomSheetState()
    val context = LocalContext.current
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    var date by remember { mutableStateOf("") }
    val datePickerState = rememberDatePickerState()

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { selectedImageUri = it }
    }

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = sheetState,
        containerColor = Color.White,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Upload Report",
                    style = MaterialTheme.typography.headlineSmall
                )

                IconButton(onClick = { onDismiss() }) {
                    Icon(Icons.Filled.Close, contentDescription = "Close")
                }
            }

            // Image selection
            Button(
                onClick = { imagePickerLauncher.launch("image/*") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Filled.PhotoCamera,
                    contentDescription = "Select Image",
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(text = if (selectedImageUri != null) "Change Image" else "Select Image")
            }

            if (selectedImageUri != null) {
                Text(
                    text = "Image selected",
                    style = MaterialTheme.typography.bodyMedium,
                    color = customGreen
                )
            }

            // Name field
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )

            // Description field
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            // Location field
            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Location") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = "Location"
                    )
                }
            )

            // Date field
            OutlinedTextField(
                value = date,
                onValueChange = { date = it },
                label = { Text("Date") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(
                            imageVector = Icons.Filled.CalendarToday,
                            contentDescription = "Select Date"
                        )
                    }
                },
                readOnly = true
            )

            // Submit button
            Button(
                onClick = { onDismiss() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Upload Report")
            }

            // Add padding at the bottom
            Spacer(modifier = Modifier.height(32.dp))
        }

        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    Button(onClick = {
                        datePickerState.selectedDateMillis?.let {
                            val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                                .format(Date(it))
                            date = formattedDate
                        }
                        showDatePicker = false
                    }) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDatePicker = false }) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
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
    var showUploadSheet by remember { mutableStateOf(false) }

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

    // Navigation items (swapped position of History and Account)
    val navItems = listOf(
        NavItem("History", Icons.Filled.History),
        NavItem("Upload", Icons.Filled.Upload),
        NavItem("Account", Icons.Filled.AccountCircle)
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .fillMaxWidth(0.65f) // A little more than half the screen width
                    .fillMaxHeight(),
                drawerContainerColor = customGreen // Make entire drawer green
            ) {
                // Add some padding at the top of the drawer
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 48.dp) // Add more top padding
                ) {
                    // No text header here as requested
                }

                // Drawer menu items with custom colors
                drawerItems.forEach { item ->
                    NavigationDrawerItem(
                        label = { Text(item.title, color = Color.White) }, // White text
                        icon = {
                            Icon(
                                item.icon,
                                contentDescription = item.title,
                                tint = Color.White // White icons
                            )
                        },
                        selected = false,
                        onClick = item.onClick,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                        colors = NavigationDrawerItemDefaults.colors(
                            unselectedContainerColor = customGreen, // Keep background green
                            unselectedIconColor = Color.White,
                            unselectedTextColor = Color.White
                        )
                    )
                }
            }
        },
    ) {
        Scaffold(
            topBar = { MyTopAppBarWithSearch(title = "Lapor Mas KAMAL", drawerState = drawerState) },
            bottomBar = {
                NavigationBar(
                    containerColor = customGreen,
                    contentColor = Color.White
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

                                // Handle navigation based on which item was clicked
                                when (index) {
                                    0 -> { // History page
                                        context.startActivity(Intent(context, HistoryActivity::class.java))
                                    }
                                    1 -> { // Upload - show bottom sheet
                                        showUploadSheet = true
                                    }
                                    2 -> { // Account/Login page
                                        context.startActivity(Intent(context, LoginActivity::class.java))
                                    }
                                }
                            }
                        )
                    }
                }
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { showUploadSheet = true },
                    containerColor = customGreen,
                    contentColor = Color.White
                ) {
                    Icon(Icons.Filled.Upload, contentDescription = "Upload")
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                // Main content area
                Text(
                    text = "Main Content Area",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }
        }

        // Show upload sheet if requested
        if (showUploadSheet) {
            UploadBottomSheet(onDismiss = { showUploadSheet = false })
        }
    }
}