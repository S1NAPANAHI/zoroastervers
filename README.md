# ZoroasterVers - Sacred Texts & Wisdom

<div align="center">
  <img src="https://img.shields.io/badge/Platform-Android-brightgreen" alt="Platform">
  <img src="https://img.shields.io/badge/Language-Kotlin-blue" alt="Language">
  <img src="https://img.shields.io/badge/UI-Jetpack%20Compose-orange" alt="UI Framework">
  <img src="https://img.shields.io/badge/Version-1.0.0-red" alt="Version">
</div>

## 📖 About

ZoroasterVers is a modern Android application dedicated to making the sacred texts and teachings of Zoroastrianism accessible to contemporary readers. Built with Jetpack Compose, the app combines ancient wisdom with modern user experience design.

## ✨ Features

### 🎨 **Modern UI/UX**
- **Sidebar Navigation**: Replaced traditional header navigation with an intuitive sidebar drawer
- **Material 3 Design**: Clean, modern interface following Google's latest design guidelines
- **Responsive Layout**: Optimized for various screen sizes and orientations
- **Smooth Animations**: Fluid transitions and micro-interactions throughout the app

### 👤 **User Profile & Authentication**
- **Complete User Profiles**: Manage personal information and preferences
- **Sign In/Sign Up**: Secure authentication with email and password
- **Guest Access**: Use the app without registration for basic features
- **Account Management**: Change passwords, update profile information
- **Premium Status**: Visual indicators for premium membership

### 🎨 **Theme & Customization**
- **Multiple App Themes**: Light, Dark, and System default modes
- **Reading Themes**: 5 specialized reading themes:
  - Default (Classic white)
  - Sepia (Warm, eye-friendly)
  - Dark (Low light reading)
  - Black (OLED optimized)
  - Night Blue (Gentle blue tones)
- **Typography Controls**: 
  - Adjustable font size (12-24sp)
  - Line spacing customization (1.0x-2.5x)
  - Real-time preview of changes

### 📚 **Content & Reading**
- **Sacred Text Library**: Complete collection of Zoroastrian verses and teachings
- **Advanced Reader**: Immersive reading experience with customization options
- **Search Functionality**: Find specific texts, verses, or topics
- **Bookmarking**: Save favorite passages for quick access
- **Reading Progress**: Track completion status across texts

### ⭐ **Premium Features**
- **Offline Content**: Download texts for reading without internet
- **5GB Storage**: Ample space for complete sacred text collections
- **Priority Updates**: First access to new content and features
- **Exclusive Content**: Premium-only texts and commentary
- **Download Management**: 
  - Progress tracking for downloads
  - Storage usage monitoring
  - Batch download options

### 📖 **Educational Content**
- **About Zoroastrianism**: Comprehensive information about the faith
- **Historical Timeline**: Interactive timeline of Zoroastrian history
- **Character Profiles**: Learn about important figures and deities
- **Cultural Context**: Understanding the historical and cultural significance

## 🏗️ **Architecture & Technology**

### **Built With**
- **Kotlin**: Modern, concise programming language
- **Jetpack Compose**: Android's modern declarative UI toolkit
- **Material 3**: Latest Material Design components and theming
- **MVVM Architecture**: Clean separation of concerns
- **Hilt**: Dependency injection framework
- **Room Database**: Local data persistence
- **Retrofit**: Network communication
- **Coroutines**: Asynchronous programming

### **Key Libraries**
```kotlin
// UI & Navigation
implementation "androidx.compose.material3:material3"
implementation "androidx.navigation:navigation-compose"
implementation "androidx.compose.material:material-icons-extended"

// Architecture
implementation "androidx.lifecycle:lifecycle-viewmodel-compose"
implementation "androidx.hilt:hilt-navigation-compose"

// Data & Storage
implementation "androidx.room:room-runtime"
implementation "androidx.room:room-ktx"
implementation "androidx.datastore:datastore-preferences"

// Networking
implementation "com.squareup.retrofit2:retrofit"
implementation "com.squareup.retrofit2:converter-gson"
```

## 🚀 **Getting Started**

### **Prerequisites**
- Android Studio Hedgehog (2023.1.1) or later
- Kotlin 1.9.0+
- Minimum SDK: API 24 (Android 7.0)
- Target SDK: API 34 (Android 14)

### **Installation**

1. **Clone the repository**
   ```bash
   git clone https://github.com/S1NAPANAHI/zoroastervers.git
   cd zoroastervers
   ```

2. **Open in Android Studio**
   - Launch Android Studio
   - Select "Open an existing project"
   - Navigate to the cloned repository

3. **Build and Run**
   ```bash
   ./gradlew build
   ./gradlew installDebug
   ```

## 📱 **App Navigation**

### **Main Sections**
- **Library**: Browse and access sacred texts
- **Reader**: Immersive reading experience
- **Timeline**: Historical events and periods
- **Characters**: Important figures in Zoroastrianism
- **Offline Content** (Premium): Downloaded texts management
- **Settings**: App preferences and customization
- **About**: Information about the app and faith

### **User Journey**
1. **First Launch**: Animated splash screen introduction
2. **Library Access**: Browse available texts as guest or signed-in user
3. **Account Creation**: Optional sign-up for enhanced features
4. **Premium Upgrade**: Access to offline content and exclusive features
5. **Personalization**: Customize themes and reading preferences

## 🎯 **User Personas**

### **Primary Users**
- **Zoroastrian Community Members**: Access to authentic sacred texts
- **Religious Scholars**: Research and study materials
- **Students**: Educational content about ancient religions
- **General Readers**: Interest in world religions and philosophy

### **Usage Scenarios**
- **Daily Devotion**: Regular reading of sacred texts
- **Academic Research**: Scholarly study of Zoroastrian literature
- **Cultural Learning**: Understanding Persian and Zoroastrian heritage
- **Offline Reading**: Access to texts without internet connectivity

## 🔧 **Development**

### **Project Structure**
```
app/src/main/java/com/example/zoroastervers/
├── ui/
│   ├── components/
│   │   └── AppSidebar.kt
│   ├── screens/
│   │   ├── UserProfileScreen.kt
│   │   ├── ThemeSettingsScreen.kt
│   │   ├── AboutScreen.kt
│   │   ├── SignUpScreen.kt
│   │   ├── EnhancedSignInScreen.kt
│   │   ├── OfflineContentScreen.kt
│   │   └── ...
│   └── theme/
├── data/
├── di/
├── network/
└── worker/
```

### **Key Features Implementation**

#### **Sidebar Navigation**
```kotlin
@Composable
fun AppSidebar(
    currentRoute: String,
    isLoggedIn: Boolean,
    isPremiumUser: Boolean,
    onNavigateToRoute: (String) -> Unit
) {
    // Navigation drawer with user profile, menu items, and premium upgrade
}
```

#### **Theme Management**
```kotlin
enum class AppTheme {
    LIGHT, DARK, SYSTEM
}

enum class ReaderTheme {
    DEFAULT, SEPIA, DARK, BLACK, NIGHT_BLUE
}
```

#### **User Authentication State**
```kotlin
data class UserState(
    val isLoggedIn: Boolean = false,
    val isPremiumUser: Boolean = false,
    val userName: String = "Guest User",
    val userEmail: String = "guest@zoroaster.app"
)
```

## 🎨 **Design System**

### **Color Palette**
- **Primary**: Deep blue representing wisdom and spirituality
- **Secondary**: Warm gold reflecting sacred fire
- **Surface**: Clean whites and subtle grays
- **Error**: Carefully chosen reds for warnings

### **Typography**
- **Headlines**: Bold, clear hierarchy
- **Body Text**: Optimized for reading comfort
- **Captions**: Subtle, informative

### **Icons & Imagery**
- **Material Icons Extended**: Consistent iconography
- **Symbolic Elements**: Fire, light, and celestial motifs
- **Cultural Sensitivity**: Respectful representation of religious symbols

## 🌟 **Premium Features Breakdown**

### **Offline Content Management**
- **Download Progress**: Real-time download tracking
- **Storage Management**: Visual storage usage indicators
- **Content Organization**: Categorized offline libraries
- **Automatic Updates**: Background sync when online

### **Exclusive Content**
- **Complete Avesta Collection** (1.2 GB)
- **The Gathas with Commentary** (450 MB)
- **Historical Manuscripts** (680 MB)
- **Scholarly Analysis** (Premium-only insights)

## 📊 **Future Roadmap**

### **Version 1.1**
- [ ] Audio narration of sacred texts
- [ ] Multi-language support (Persian, Gujarati)
- [ ] Social features (sharing quotes, discussions)
- [ ] Advanced search with filters

### **Version 1.2**
- [ ] Tablet optimization
- [ ] Widget support for daily verses
- [ ] Cloud synchronization
- [ ] Community features

### **Version 2.0**
- [ ] AI-powered content recommendations
- [ ] Augmented reality features
- [ ] Virtual temple visits
- [ ] Educational games and quizzes

## 🤝 **Contributing**

We welcome contributions from the community! Please read our contributing guidelines:

1. **Fork** the repository
2. **Create** a feature branch (`git checkout -b feature/amazing-feature`)
3. **Commit** your changes (`git commit -m 'Add amazing feature'`)
4. **Push** to the branch (`git push origin feature/amazing-feature`)
5. **Open** a Pull Request

### **Development Guidelines**
- Follow Kotlin coding standards
- Use meaningful commit messages
- Add appropriate documentation
- Test your changes thoroughly
- Respect cultural and religious sensitivity

## 📄 **License**

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 **Acknowledgments**

- **Zoroastrian Communities**: For guidance and cultural authenticity
- **Scholars and Translators**: Who preserved these sacred texts
- **Open Source Community**: For the amazing libraries and tools
- **Beta Testers**: For valuable feedback and suggestions

## 📞 **Contact & Support**

- **Developer**: SINA PANAHI
- **Email**: support@zoroastervers.app
- **Website**: www.zoroastervers.app
- **Issues**: [GitHub Issues](https://github.com/S1NAPANAHI/zoroastervers/issues)

---

<div align="center">
  <p><strong>Made with ❤️ for the Zoroastrian Community</strong></p>
  <p><em>"Good Thoughts, Good Words, Good Deeds"</em></p>
  <p>© 2025 ZoroasterVers. All rights reserved.</p>
</div>