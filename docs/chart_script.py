# Create an improved architecture diagram with better layout and clarity
diagram_code = """
flowchart TD
    %% UI Layer - arranged horizontally
    subgraph UI["🎨 UI Layer (Jetpack Compose)"]
        direction LR
        RS[Reading Screen]
        LS[Library Screen]
        AU[Authentication]
        SE[Settings]
        CP[Character Profiles]
        TV[Timeline View]
    end
    
    %% Domain Layer - arranged horizontally
    subgraph DL["⚙️ Domain Layer (Business Logic)"]
        direction LR
        RUC[Reading Use Cases]
        AUC[Auth Use Cases]
        CUC[Content Use Cases]
        SUC[Sync Use Cases]
    end
    
    %% Data Layer - arranged horizontally
    subgraph DAL["📊 Data Layer"]
        direction LR
        RP[Repository Pattern]
        AC[API Client]
        LDB[Local Database]
        FM[File Manager]
    end
    
    %% External Services - arranged horizontally
    subgraph ES["🌐 External Services"]
        direction LR
        ZA[Zoroastervers API]
        SP[Stripe Payments]
        FS[File Storage]
    end
    
    %% Local Storage - arranged horizontally
    subgraph LS_LOCAL["💾 Local Storage"]
        direction LR
        RD[Room Database]
        DC[Downloaded Content]
        UP[User Preferences]
        RPG[Reading Progress]
    end
    
    %% Background Services - arranged horizontally
    subgraph BS["🔄 Background Services"]
        direction LR
        WM[WorkManager]
        SS[Sync Service]
        DM[Download Manager]
        NO[Notifications]
    end
    
    %% Main data flow - simplified connections
    UI --> DL
    DL --> DAL
    DAL --> ES
    DAL --> LS_LOCAL
    
    %% Background services connections
    BS --> DAL
    BS --> LS_LOCAL
    
    %% Key specific connections (reduced for clarity)
    RS -.-> RUC
    AU -.-> AUC
    LS -.-> CUC
    
    RP --> AC
    RP --> LDB
    AC --> ZA
    LDB --> RD
    FM --> DC
    
    WM --> SS
    WM --> DM
    SS -.-> ZA
    DM -.-> FS
    
    %% Add some spacing
    UI ~~~ DL
    DL ~~~ DAL
    DAL ~~~ ES
    DAL ~~~ LS_LOCAL
    LS_LOCAL ~~~ BS
"""

# Create the improved diagram
png_path, svg_path = create_mermaid_diagram(diagram_code, 'architecture_diagram.png', 'architecture_diagram.svg', width=1400, height=1000)

print(f"Improved architecture diagram saved as {png_path} and {svg_path}")