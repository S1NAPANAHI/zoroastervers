# Backend Authentication Integration Setup Guide

## 🎯 Overview

This guide walks you through integrating your ZoroasterVers Android app with your Webcite-for-new-authors backend for seamless authentication.

## 📁 What's Been Added

### 1. **Data Models** (`app/src/main/java/com/example/zoroastervers/network/model/ApiModels.kt`)
- `AuthResponse` - Authentication response from backend
- `User` - User model matching your backend
- `SignInRequest/SignUpRequest` - Request models
- `RefreshResponse` - Token refresh response
- All models with proper JSON serialization

### 2. **API Service** (`app/src/main/java/com/example/zoroastervers/network/BackendAuthApiService.kt`)
- Retrofit interface for all auth endpoints
- Supports sign-in, sign-up, token refresh, user info
- Ready for Google OAuth integration

### 3. **Secure Token Management** (`app/src/main/java/com/example/zoroastervers/data/local/TokenManager.kt`)
- Encrypted SharedPreferences for token storage
- Automatic token expiration checking
- Secure user data persistence

### 4. **Repository Layer** (`app/src/main/java/com/example/zoroastervers/data/repository/BackendAuthRepository.kt`)
- Complete authentication flow management
- Error handling and token refresh
- Flow-based reactive programming

### 5. **ViewModel** (`app/src/main/java/com/example/zoroastervers/BackendAuthViewModel.kt`)
- State management for authentication
- Input validation
- Loading states and error handling

### 6. **Modern UI Screens**
- `BackendLoginScreen.kt` - Beautiful login interface
- `BackendSignUpScreen.kt` - Complete registration flow
- Material 3 design with proper validation

### 7. **Network Configuration** (`app/src/main/java/com/example/zoroastervers/network/BackendNetworkModule.kt`)
- Hilt dependency injection setup
- Automatic token attachment
- Logging for development

## 🚀 Quick Start

### Step 1: Checkout the Branch
```bash
git checkout feature/backend-authentication
```

### Step 2: Update Backend URL
In `BackendNetworkModule.kt`, update the base URL:
```kotlin
private const val BACKEND_BASE_URL = "https://your-actual-backend-url.com/api/"
```

### Step 3: Add Internet Permission
In `app/src/main/AndroidManifest.xml`, add:
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

### Step 4: Update Your Navigation
Replace your existing login screen with the new backend-integrated screens:

```kotlin
// In your navigation setup
composable("login") {
    BackendLoginScreen(
        onLoginSuccess = { 
            navController.navigate("main") {
                popUpTo("login") { inclusive = true }
            }
        },
        onNavigateToSignUp = { navController.navigate("signup") }
    )
}

composable("signup") {
    BackendSignUpScreen(
        onSignUpSuccess = { 
            navController.navigate("main") {
                popUpTo("signup") { inclusive = true }
            }
        },
        onNavigateBack = { navController.popBackStack() }
    )
}
```

### Step 5: Build and Test
```bash
./gradlew clean build
```

## 🔧 Backend Configuration

Your backend is already set up correctly! The integration uses these existing endpoints:

- `POST /api/auth/signup` - User registration
- `POST /api/auth/signin` - User login  
- `POST /api/auth/refresh` - Token refresh
- `GET /api/auth/me` - Get current user
- `POST /api/auth/signout` - Sign out

## 🔐 Security Features

✅ **Encrypted Token Storage** - Uses Android's EncryptedSharedPreferences
✅ **Automatic Token Refresh** - Handles expired tokens seamlessly
✅ **Secure Network Communication** - HTTPS with proper headers
✅ **Input Validation** - Client-side validation before API calls
✅ **Error Handling** - Comprehensive error messages

## 📱 User Experience Features

✅ **Modern Material 3 UI** - Beautiful, accessible design
✅ **Loading States** - Progress indicators during API calls
✅ **Form Validation** - Real-time input validation
✅ **Password Strength** - Visual password requirements
✅ **Guest Access** - Option to continue without account
✅ **Responsive Design** - Works on all screen sizes

## 🔄 Authentication Flow

1. **User opens app** → Check stored tokens
2. **Token valid** → Navigate to main app
3. **Token expired** → Attempt refresh
4. **Refresh successful** → Navigate to main app
5. **Refresh failed** → Show login screen
6. **User signs in** → Store tokens, navigate to main app

## 🧪 Testing

### Test Accounts
You can create test accounts through the app or directly test with:
- Valid email format required
- Password minimum 6 characters
- All fields validated before submission

### Debug Features
- Network requests logged in debug builds
- Detailed error messages in development
- Token expiration testing

## 🚀 Google Sign-In (Future Enhancement)

The code is ready for Google Sign-In integration. To add it:

1. Add Google Sign-In dependency:
```kotlin
implementation 'com.google.android.gms:play-services-auth:21.0.0'
```

2. Update your backend to handle Google OAuth (already has endpoint)

3. Implement Google Sign-In in the UI screens (placeholder ready)

## 🔧 Customization

### Changing App Theme
Update colors in your theme files to match your brand.

### Custom Validation
Modify validation rules in `BackendAuthViewModel.kt`.

### Additional User Fields
Add fields to `SignUpRequest` model and update UI accordingly.

## 📞 Support

If you encounter any issues:
1. Check the network logs in Android Studio
2. Verify your backend URL is correct
3. Ensure your backend is running and accessible
4. Check internet permissions in manifest

## 🎉 Next Steps

1. **Test the integration** with your backend
2. **Customize the UI** to match your app's design
3. **Add Google Sign-In** for easier user onboarding
4. **Implement biometric authentication** for enhanced security
5. **Add user profile management** screens

---

**Your ZoroasterVers app is now ready for seamless backend authentication!** 🎊

The integration maintains your existing app architecture while adding robust, secure authentication capabilities.