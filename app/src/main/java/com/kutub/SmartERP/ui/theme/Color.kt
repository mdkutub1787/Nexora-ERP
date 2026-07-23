package com.kutub.smarterp.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * SmartERP POS & Inventory - Premium Indigo / Violet / Slate Palette
 * Inspired by modern financial dashboards.
 */

// ─── Brand Palette ─────────────────────────────────────────────────────────────
val PrimaryIndigo      = Color(0xFF6366F1)  // Indigo-500
val PrimaryIndigoLight = Color(0xFF818CF8)  // Indigo-400
val PrimaryIndigoDark  = Color(0xFF4F46E5)  // Indigo-600

val AccentViolet       = Color(0xFF8B5CF6)  // Violet-500
val AccentPurple       = Color(0xFFA855F7)  // Purple-500

// ─── Semantic Colors ───────────────────────────────────────────────────────────
val IncomeGreen        = Color(0xFF10B981)  // Emerald-500
val IncomeGreenLight   = Color(0xFF34D399)  // Emerald-400
val ExpenseRed         = Color(0xFFEF4444)  // Red-500
val ExpenseRedLight    = Color(0xFFF87171)  // Red-400
val SavingsBlue        = Color(0xFF3B82F6)  // Blue-500

val WarningAmber       = Color(0xFFF59E0B)  // Amber-500

// ─── Light Surface Palette (Slate) ─────────────────────────────────────────────
val LightBackground    = Color(0xFFF8FAFC)  // Soft Slate Light Background
val LightSurface       = Color(0xFFFFFFFF)  // Card/Surface
val LightSurfaceVariant = Color(0xFFF1F5F9) // Elevated container
val LightBorderColor   = Color(0xFFE2E8F0)  // Border stroke
val LightOutline       = Color(0xFFCBD5E1)

val TextPrimary        = Color(0xFF0F172A)  // Slate 900
val TextSecondary      = Color(0xFF64748B)  // Slate 500
val TextHint           = Color(0xFF94A3B8)  // Slate 400

// ─── Dark Surface Palette ──────────────────────────────────────────────────────
val DarkBackground     = Color(0xFF0F0F1A)  // Deep Midnight
val DarkSurface        = Color(0xFF1A1A2E)  // Elevated Surface
val DarkSurfaceVariant = Color(0xFF252545)  // Container
val DarkBorderColor    = Color(0xFF2D2D4E)

val DarkTextPrimary    = Color(0xFFF1F1FF)
val DarkTextSecondary  = Color(0xFF9B9BC8)

// ─── Reusable Gradients ───────────────────────────────────────────────────────
val HeroGradient = Brush.linearGradient(
    colors = listOf(PrimaryIndigo, AccentViolet)
)

val HeroDarkGradient = Brush.linearGradient(
    colors = listOf(PrimaryIndigoDark, AccentPurple)
)

val GreenGradient = Brush.linearGradient(
    colors = listOf(IncomeGreen, Color(0xFF059669))
)

val AmberGradient = Brush.linearGradient(
    colors = listOf(WarningAmber, Color(0xFFD97706))
)


