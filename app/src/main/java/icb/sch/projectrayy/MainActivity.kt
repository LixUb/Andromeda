package icb.sch.projectrayy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import icb.sch.projectrayy.ui.theme.ProjectRayyTheme
import kotlinx.coroutines.launch

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
fun MainScreen() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

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
            topBar = { MyTopAppBarWithSearch(title = "Lapor Mas KAMAL", drawerState = drawerState) }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(Color.White)
            ) {
                // Main content area
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Main content (you can put your content here)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Main Content Area", style = MaterialTheme.typography.bodyLarge)
                    }

                    // Bottom navigation buttons
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Account Login button (Left)
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            IconButton(
                                onClick = { /* Handle account login click */ },
                                modifier = Modifier
                                    .background(customGreen.copy(alpha = 0.1f), CircleShape)
                                    .padding(8.dp)
                            ) {
                                Icon(
                                    Icons.Filled.AccountCircle,
                                    contentDescription = "Account Login",
                                    tint = customGreen,
                                    modifier = Modifier.padding(4.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "Account",
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.Center
                            )
                        }

                        // Upload button (Middle)
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            FloatingActionButton(
                                onClick = { /* Handle upload click */ },
                                containerColor = customGreen,
                                contentColor = Color.White
                            ) {
                                Icon(
                                    Icons.Filled.Upload,
                                    contentDescription = "Upload",
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "Upload",
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.Center
                            )
                        }

                        // History button (Right)
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            IconButton(
                                onClick = { /* Handle history click */ },
                                modifier = Modifier
                                    .background(customGreen.copy(alpha = 0.1f), CircleShape)
                                    .padding(8.dp)
                            ) {
                                Icon(
                                    Icons.Filled.History,
                                    contentDescription = "History",
                                    tint = customGreen,
                                    modifier = Modifier.padding(4.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "History",
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}