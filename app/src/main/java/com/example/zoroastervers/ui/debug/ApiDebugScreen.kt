package com.example.zoroastervers.ui.debug

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zoroastervers.data.repository.BackendAuthRepository
import com.example.zoroastervers.network.model.SignInRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApiDebugViewModel @Inject constructor(
    private val repository: BackendAuthRepository
) : ViewModel() {
    
    private val _debugState = MutableStateFlow(ApiDebugState())
    val debugState = _debugState.asStateFlow()
    
    fun testConnectivity() {
        viewModelScope.launch {
            _debugState.value = _debugState.value.copy(
                isLoading = true,
                connectivityResult = "Testing..."
            )
            
            repository.testConnectivity().collect { result ->
                _debugState.value = _debugState.value.copy(
                    isLoading = false,
                    connectivityResult = if (result.isSuccess && result.getOrNull() == true) {
                        "✅ API is accessible"
                    } else {
                        "❌ API is not accessible"
                    }
                )
            }
        }
    }
    
    fun testSignIn(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _debugState.value = _debugState.value.copy(
                signInResult = "❌ Please enter email and password"
            )
            return
        }
        
        viewModelScope.launch {
            _debugState.value = _debugState.value.copy(
                isLoading = true,
                signInResult = "Testing sign in..."
            )
            
            repository.signIn(email, password).collect { result ->
                _debugState.value = _debugState.value.copy(
                    isLoading = false,
                    signInResult = if (result.isSuccess) {
                        val user = result.getOrNull()?.user
                        "✅ Sign in successful!\nUser: ${user?.email}\nRole: ${user?.role}"
                    } else {
                        "❌ Sign in failed: ${result.exceptionOrNull()?.message}"
                    }
                )
            }
        }
    }
    
    fun testSignUp(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _debugState.value = _debugState.value.copy(
                signUpResult = "❌ Please enter email and password"
            )
            return
        }
        
        viewModelScope.launch {
            _debugState.value = _debugState.value.copy(
                isLoading = true,
                signUpResult = "Testing sign up..."
            )
            
            repository.signUp(email, password).collect { result ->
                _debugState.value = _debugState.value.copy(
                    isLoading = false,
                    signUpResult = if (result.isSuccess) {
                        val user = result.getOrNull()?.user
                        "✅ Sign up successful!\nUser: ${user?.email}\nRole: ${user?.role}"
                    } else {
                        "❌ Sign up failed: ${result.exceptionOrNull()?.message}"
                    }
                )
            }
        }
    }
    
    fun clearResults() {
        _debugState.value = ApiDebugState()
    }
}

data class ApiDebugState(
    val isLoading: Boolean = false,
    val connectivityResult: String = "",
    val signInResult: String = "",
    val signUpResult: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApiDebugScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ApiDebugViewModel = hiltViewModel()
) {
    val debugState by viewModel.debugState.collectAsStateWithLifecycle()
    
    var testEmail by remember { mutableStateOf("test@example.com") }
    var testPassword by remember { mutableStateOf("password123") }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("API Debug") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.clearResults() }) {
                        Icon(Icons.Default.Refresh, "Clear")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // API Info Card
            Card {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Backend API Information",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Base URL: https://webcite-for-new-authors.onrender.com/api/",
                        fontFamily = FontFamily.Monospace,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "This tool tests connectivity and authentication endpoints.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            // Connectivity Test
            Card {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "1. Connectivity Test",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Button(
                        onClick = { viewModel.testConnectivity() },
                        enabled = !debugState.isLoading,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (debugState.isLoading && debugState.connectivityResult.contains("Testing")) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        Text("Test API Connectivity")
                    }
                    
                    if (debugState.connectivityResult.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = if (debugState.connectivityResult.startsWith("✅")) {
                                    MaterialTheme.colorScheme.primaryContainer
                                } else {
                                    MaterialTheme.colorScheme.errorContainer
                                }
                            )
                        ) {
                            Text(
                                text = debugState.connectivityResult,
                                modifier = Modifier.padding(12.dp),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
            
            // Test Credentials
            Card {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "2. Test Credentials",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    OutlinedTextField(
                        value = testEmail,
                        onValueChange = { testEmail = it },
                        label = { Text("Test Email") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    OutlinedTextField(
                        value = testPassword,
                        onValueChange = { testPassword = it },
                        label = { Text("Test Password") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            
            // Sign In Test
            Card {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "3. Sign In Test",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Button(
                        onClick = { viewModel.testSignIn(testEmail, testPassword) },
                        enabled = !debugState.isLoading,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (debugState.isLoading && debugState.signInResult.contains("Testing")) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        Text("Test Sign In")
                    }
                    
                    if (debugState.signInResult.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = if (debugState.signInResult.startsWith("✅")) {
                                    MaterialTheme.colorScheme.primaryContainer
                                } else {
                                    MaterialTheme.colorScheme.errorContainer
                                }
                            )
                        ) {
                            Text(
                                text = debugState.signInResult,
                                modifier = Modifier.padding(12.dp),
                                style = MaterialTheme.typography.bodyMedium,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            }
            
            // Sign Up Test
            Card {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "4. Sign Up Test",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Button(
                        onClick = { viewModel.testSignUp(testEmail, testPassword) },
                        enabled = !debugState.isLoading,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (debugState.isLoading && debugState.signUpResult.contains("Testing")) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        Text("Test Sign Up")
                    }
                    
                    if (debugState.signUpResult.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = if (debugState.signUpResult.startsWith("✅")) {
                                    MaterialTheme.colorScheme.primaryContainer
                                } else {
                                    MaterialTheme.colorScheme.errorContainer
                                }
                            )
                        ) {
                            Text(
                                text = debugState.signUpResult,
                                modifier = Modifier.padding(12.dp),
                                style = MaterialTheme.typography.bodyMedium,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            }
            
            // Instructions
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Debug Instructions",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "1. First test connectivity to ensure the API is reachable\n" +
                                "2. Use existing credentials from your website to test sign in\n" +
                                "3. Try sign up with a new email to test registration\n" +
                                "4. Check the logs in Android Studio for detailed error messages",
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Start
                    )
                }
            }
        }
    }
}