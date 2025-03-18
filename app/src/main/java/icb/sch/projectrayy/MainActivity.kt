package icb.sch.projectrayy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
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
                    placeholder = { Text("Search") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    trailingIcon = {
                        if (searchText.value.isNotEmpty()) {
                            IconButton(onClick = {
                                searchText.value = ""
                            }) {
                                Icon(Icons.Filled.Clear, "Clear")
                            }
                        }
                    },
                    leadingIcon = {
                        IconButton(onClick = {
                            showSearchBar.value = false
                            searchText.value = ""
                            focusManager.clearFocus()
                        }) {
                            Icon(Icons.Filled.ArrowBack, "Back")
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = { focusManager.clearFocus() }
                    )
                )
            } else {
                Text(text = title)
            }
        },
        navigationIcon = {
            if(!showSearchBar.value) {
                IconButton(onClick = {
                    scope.launch {
                        drawerState.open()
                    }
                }) {
                    Icon(Icons.Filled.Menu, "Menu")
                }
            }
        },
        actions = {
            if(!showSearchBar.value) {
                IconButton(onClick = {
                    showSearchBar.value = true
                }) {
                    Icon(Icons.Filled.Search, "Search")
                }
            }
        }
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
            ModalDrawerSheet {
                drawerItems.forEach { item ->
                    NavigationDrawerItem(
                        label = { Text(item.title) },
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        selected = false,
                        onClick = item.onClick,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                }
            }
        },
    ) {
        Scaffold(
            topBar = { MyTopAppBarWithSearch(title = "Lapor Mas KAMAL", drawerState = drawerState) }
        ) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                // Your main screen content goes here
            }
        }
    }
}