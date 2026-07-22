package com.kutub.nexora.erp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class DialogType {
    SUCCESS, ERROR, WARNING, INFO
}

@Composable
fun NexoraGlobalDialog(
    showDialog: Boolean,
    type: DialogType,
    title: String,
    message: String,
    confirmText: String = "OK",
    dismissText: String? = null,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    if (showDialog) {
        val (icon, tintColor) = when (type) {
            DialogType.SUCCESS -> Icons.Default.CheckCircle to Color(0xFF4CAF50)
            DialogType.ERROR -> Icons.Default.Error to MaterialTheme.colorScheme.error
            DialogType.WARNING -> Icons.Default.Warning to Color(0xFFFF9800)
            DialogType.INFO -> Icons.Default.Info to MaterialTheme.colorScheme.primary
        }

        AlertDialog(
            onDismissRequest = onDismiss,
            shape = RoundedCornerShape(16.dp),
            containerColor = MaterialTheme.colorScheme.surface,
            icon = {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = tintColor,
                    modifier = Modifier.size(48.dp)
                )
            },
            title = {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            text = {
                Text(
                    text = message,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp)
                )
            },
            confirmButton = {
                Button(
                    onClick = onConfirm,
                    colors = ButtonDefaults.buttonColors(containerColor = tintColor)
                ) {
                    Text(confirmText, color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                if (dismissText != null) {
                    OutlinedButton(onClick = onDismiss) {
                        Text(dismissText, color = MaterialTheme.colorScheme.onSurface)
                    }
                }
            }
        )
    }
}
