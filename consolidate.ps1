$dest = "E:\SmartERP-ERP\app\src\main\java\com\kutub\SmartERP\erp"

# Ensure destination exists
if (!(Test-Path $dest)) { New-Item -ItemType Directory -Force -Path $dest }

# Create target directories
New-Item -ItemType Directory -Force -Path "$dest\ui\auth"
New-Item -ItemType Directory -Force -Path "$dest\ui\dashboard"
New-Item -ItemType Directory -Force -Path "$dest\ui\navigation"
New-Item -ItemType Directory -Force -Path "$dest\data\database"
New-Item -ItemType Directory -Force -Path "$dest\data\model"
New-Item -ItemType Directory -Force -Path "$dest\di"

# Move files from feature modules
Move-Item -Path "E:\SmartERP-ERP\feature-auth\src\main\java\com\smarterp\feature\auth\*" -Destination "$dest\ui\auth" -Force
Move-Item -Path "E:\SmartERP-ERP\feature-dashboard\src\main\java\com\smarterp\feature\dashboard\*" -Destination "$dest\ui\dashboard" -Force

# Move files from core-database
Move-Item -Path "E:\SmartERP-ERP\core-database\src\main\java\com\smarterp\core\database\entity\*" -Destination "$dest\data\model" -Force
Move-Item -Path "E:\SmartERP-ERP\core-database\src\main\java\com\smarterp\core\database\dao\*" -Destination "$dest\data\database" -Force
Move-Item -Path "E:\SmartERP-ERP\core-database\src\main\java\com\smarterp\core\database\SmartErpDatabase.kt" -Destination "$dest\data\database" -Force
Move-Item -Path "E:\SmartERP-ERP\core-database\src\main\java\com\smarterp\core\database\di\*" -Destination "$dest\di" -Force

# Move SmartERPApp.kt and AppNavGraph.kt (if they exist in app/com/smarterp)
if (Test-Path "E:\SmartERP-ERP\app\src\main\java\com\smarterp\SmartERPApp.kt") {
    Move-Item -Path "E:\SmartERP-ERP\app\src\main\java\com\smarterp\SmartERPApp.kt" -Destination "$dest" -Force
}

# Replace all com.smarterp with com.kutub.smarterp in the moved files
Get-ChildItem -Path $dest -Recurse -File -Filter "*.kt" | ForEach-Object {
    $content = Get-Content $_.FullName -Raw
    # Handle the specific feature packages first
    $content = $content -replace "package com.smarterp.feature.auth", "package com.kutub.smarterp.ui.auth"
    $content = $content -replace "import com.smarterp.feature.auth", "import com.kutub.smarterp.ui.auth"
    
    $content = $content -replace "package com.smarterp.feature.dashboard", "package com.kutub.smarterp.ui.dashboard"
    $content = $content -replace "import com.smarterp.feature.dashboard", "import com.kutub.smarterp.ui.dashboard"
    
    $content = $content -replace "package com.smarterp.core.database.entity", "package com.kutub.smarterp.data.model"
    $content = $content -replace "import com.smarterp.core.database.entity", "import com.kutub.smarterp.data.model"
    
    $content = $content -replace "package com.smarterp.core.database.dao", "package com.kutub.smarterp.data.database"
    $content = $content -replace "import com.smarterp.core.database.dao", "import com.kutub.smarterp.data.database"
    
    $content = $content -replace "package com.smarterp.core.database.di", "package com.kutub.smarterp.di"
    $content = $content -replace "import com.smarterp.core.database.di", "import com.kutub.smarterp.di"
    
    $content = $content -replace "package com.smarterp.core.database", "package com.kutub.smarterp.data.database"
    $content = $content -replace "import com.smarterp.core.database", "import com.kutub.smarterp.data.database"
    
    # Generic replace for any remaining com.smarterp
    $content = $content -replace "com.smarterp", "com.kutub.smarterp"
    
    Set-Content -Path $_.FullName -Value $content -Encoding UTF8
}

# Update MainActivity.kt
$mainActivity = "$dest\MainActivity.kt"
if (Test-Path $mainActivity) {
    $content = Get-Content $mainActivity -Raw
    $content = $content -replace "com.smarterp", "com.kutub.smarterp"
    Set-Content -Path $mainActivity -Value $content -Encoding UTF8
}


