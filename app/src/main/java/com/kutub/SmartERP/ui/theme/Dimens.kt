package com.kutub.smarterp.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Dimens(
    val default: Dp = 0.dp,
    val paddingTiny: Dp = 4.dp,
    val paddingSmall: Dp = 8.dp,
    val paddingMedium: Dp = 12.dp, // Reduced from 16
    val paddingLarge: Dp = 16.dp,  // Reduced from 24
    val paddingExtraLarge: Dp = 24.dp, // Reduced from 32
    val paddingHuge: Dp = 32.dp,
    
    val cornerRadiusSmall: Dp = 8.dp,
    val cornerRadiusMedium: Dp = 12.dp,
    val cornerRadiusLarge: Dp = 16.dp, // Reduced from 24
    val cornerRadiusExtraLarge: Dp = 24.dp,
    
    val iconSizeSmall: Dp = 16.dp,
    val iconSizeMedium: Dp = 20.dp,
    val iconSizeLarge: Dp = 32.dp,
    val iconSizeExtraLarge: Dp = 44.dp,
    
    val buttonHeight: Dp = 48.dp,
    val cardElevation: Dp = 4.dp,
    
    // Grid sizes
    val gridCellMinSize: Dp = 150.dp
)

val CompactDimens = Dimens()

val MediumDimens = Dimens(
    paddingSmall = 10.dp,
    paddingMedium = 16.dp,
    paddingLarge = 24.dp,
    paddingExtraLarge = 32.dp,
    
    cornerRadiusSmall = 10.dp,
    cornerRadiusMedium = 16.dp,
    cornerRadiusLarge = 22.dp,
    cornerRadiusExtraLarge = 28.dp,
    
    iconSizeMedium = 24.dp,
    iconSizeLarge = 36.dp,
    
    buttonHeight = 52.dp,
    cardElevation = 6.dp,
    
    gridCellMinSize = 180.dp
)

val ExpandedDimens = Dimens(
    paddingSmall = 12.dp,
    paddingMedium = 20.dp,
    paddingLarge = 32.dp,
    paddingExtraLarge = 48.dp,
    
    cornerRadiusSmall = 12.dp,
    cornerRadiusMedium = 20.dp,
    cornerRadiusLarge = 28.dp,
    cornerRadiusExtraLarge = 32.dp,
    
    iconSizeMedium = 28.dp,
    iconSizeLarge = 42.dp,
    
    buttonHeight = 56.dp,
    cardElevation = 8.dp,
    
    gridCellMinSize = 220.dp
)

val LocalDimens = staticCompositionLocalOf { CompactDimens }


