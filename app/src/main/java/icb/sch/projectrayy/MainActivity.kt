package icb.sch.projectrayy

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
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
import androidx.compose.ui.window.Dialog
import icb.sch.projectrayy.ui.theme.ProjectRayyTheme
import icb.sch.projectrayy.ui.theme.SkyBlue
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
            containerColor = SkyBlue
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadDialog(onDismiss: () -> Unit, onSubmit: (String, String, String, Uri?) -> Unit) {
    var description by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var selectedImage by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val showDatePicker = remember { mutableStateOf(false) }

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImage = uri
    }

    val datePickerState = rememberDatePickerState()

    if (showDatePicker.value) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker.value = false },
            confirmButton = {
                Button(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        date = formatter.format(Date(it))
                    }
                    showDatePicker.value = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                Button(onClick = { showDatePicker.value = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Upload Report",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )

                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Location") },
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        Icon(Icons.Filled.LocationOn, contentDescription = "Location")
                    }
                )

                OutlinedTextField(
                    value = date,
                    onValueChange = { date = it },
                    label = { Text("Date") },
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker.value = true }) {
                            Icon(Icons.Filled.DateRange, contentDescription = "Select date")
                        }
                    },
                    readOnly = true
                )

                Button(
                    onClick = { pickImageLauncher.launch("image/*") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Filled.Photo, contentDescription = "Pick Image")
                        Text(selectedImage?.let { "Image Selected" } ?: "Select Image")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f).padding(end = 8.dp)
                    ) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = { onSubmit(name, description, location, selectedImage) },
                        modifier = Modifier.weight(1f).padding(start = 8.dp),
                        enabled = name.isNotEmpty() && description.isNotEmpty() && location.isNotEmpty() && date.isNotEmpty() && selectedImage != null
                    ) {
                        Text("Submit")
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
                drawerContainerColor = SkyBlue // Update to Sky Blue
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
                            unselectedContainerColor = SkyBlue, // Update to Sky Blue
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
                    containerColor = SkyBlue, // Update to Sky Blue
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
                            }
                        )
                    }
                }
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = { showUploadDialog = true },
                    icon = { Icon(Icons.Filled.Upload, contentDescription = "Upload") },
                    text = { Text("Upload Report") },
                    containerColor = SkyBlue, // Update to Sky Blue
                    contentColor = Color.White
                )
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

            if (showUploadDialog) {
                UploadDialog(
                    onDismiss = { showUploadDialog = false },
                    onSubmit = { name, description, location, imageUri ->
                        // Handle upload logic here
                        // In a real app, you would process the upload with these parameters
                        showUploadDialog = false
                    }
                )
            }
        }
    }
}