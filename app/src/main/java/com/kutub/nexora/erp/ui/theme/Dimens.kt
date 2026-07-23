package com.kutub.nexora.erp.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Dimens(
    val default: Dp = 0.dp,
    val paddingTiny: Dp = 4.dp,
    val paddingSmall: Dp = 8.dp,
    val paddingMedium: Dp = 16.dp,
    val paddingLarge: Dp = 24.dp,
    val paddingExtraLarge: Dp = 32.dp,
    val paddingHuge: Dp = 48.dp,
    
    val cornerRadiusSmall: Dp = 8.dp,
    val cornerRadiusMedium: Dp = 16.dp,
    val cornerRadiusLarge: Dp = 24.dp,
    
    val iconSizeSmall: Dp = 16.dp,
    val iconSizeMedium: Dp = 24.dp,
    val iconSizeLarge: Dp = 32.dp,
    val iconSizeExtraLarge: Dp = 48.dp,
    
    val buttonHeight: Dp = 50.dp,
    val cardElevation: Dp = 4.dp,
    
    // Grid sizes
    val gridCellMinSize: Dp = 160.dp
)

val CompactDimens = Dimens()

val MediumDimens = Dimens(
    paddingSmall = 12.dp,
    paddingMedium = 20.dp,
    paddingLarge = 32.dp,
    paddingExtraLarge = 48.dp,
    
    cornerRadiusSmall = 12.dp,
    cornerRadiusMedium = 20.dp,
    cornerRadiusLarge = 28.dp,
    
    iconSizeMedium = 28.dp,
    iconSizeLarge = 40.dp,
    
    buttonHeight = 56.dp,
    
    gridCellMinSize = 200.dp
)

val ExpandedDimens = Dimens(
    paddingSmall = 16.dp,
    paddingMedium = 24.dp,
    paddingLarge = 40.dp,
    paddingExtraLarge = 64.dp,
    
    cornerRadiusSmall = 16.dp,
    cornerRadiusMedium = 24.dp,
    cornerRadiusLarge = 32.dp,
    
    iconSizeMedium = 32.dp,
    iconSizeLarge = 48.dp,
    
    buttonHeight = 64.dp,
    
    gridCellMinSize = 240.dp
)

val LocalDimens = staticCompositionLocalOf { CompactDimens }
