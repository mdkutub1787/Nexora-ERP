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
    
    val cornerRadiusSmall: Dp = 10.dp,
    val cornerRadiusMedium: Dp = 18.dp,
    val cornerRadiusLarge: Dp = 24.dp,
    val cornerRadiusExtraLarge: Dp = 32.dp,
    
    val iconSizeSmall: Dp = 18.dp,
    val iconSizeMedium: Dp = 24.dp,
    val iconSizeLarge: Dp = 36.dp,
    val iconSizeExtraLarge: Dp = 52.dp,
    
    val buttonHeight: Dp = 52.dp,
    val cardElevation: Dp = 6.dp,
    
    // Grid sizes
    val gridCellMinSize: Dp = 165.dp
)

val CompactDimens = Dimens()

val MediumDimens = Dimens(
    paddingSmall = 12.dp,
    paddingMedium = 20.dp,
    paddingLarge = 32.dp,
    paddingExtraLarge = 48.dp,
    
    cornerRadiusSmall = 14.dp,
    cornerRadiusMedium = 22.dp,
    cornerRadiusLarge = 28.dp,
    cornerRadiusExtraLarge = 36.dp,
    
    iconSizeMedium = 28.dp,
    iconSizeLarge = 42.dp,
    
    buttonHeight = 58.dp,
    cardElevation = 8.dp,
    
    gridCellMinSize = 210.dp
)

val ExpandedDimens = Dimens(
    paddingSmall = 16.dp,
    paddingMedium = 24.dp,
    paddingLarge = 40.dp,
    paddingExtraLarge = 64.dp,
    
    cornerRadiusSmall = 16.dp,
    cornerRadiusMedium = 26.dp,
    cornerRadiusLarge = 34.dp,
    cornerRadiusExtraLarge = 40.dp,
    
    iconSizeMedium = 32.dp,
    iconSizeLarge = 48.dp,
    
    buttonHeight = 64.dp,
    cardElevation = 10.dp,
    
    gridCellMinSize = 250.dp
)

val LocalDimens = staticCompositionLocalOf { CompactDimens }
