package com.example.logiclyst.ime.comps

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.logiclyst.ime.KeyboardState
import kotlinx.coroutines.delay

@Composable
fun RowScope.RepeatingKey(
    modifier: Modifier = Modifier,
    label: String? = null,
    icon: Int? = null,
    backgroundColor: Color,
    textColor: Color = Color.Black,
    iconColor: Color = Color.Black,
    weight: Float = 1f,
    onClick: (String) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val haptic = LocalHapticFeedback.current
    val isHapticEnabled by KeyboardState.isHapticEnabled

    LaunchedEffect(isPressed) {
        if (isPressed) {
            onClick(label ?: "ICON_ACTION")
            if (isHapticEnabled) haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)

            delay(450)
            while (isPressed) {
                onClick(label ?: "ICON_ACTION")
                if (isHapticEnabled) haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                delay(60)
            }
        }
    }

    Box(
        modifier = modifier
            .padding(horizontal = 2.dp, vertical = 4.dp)
            .weight(weight)
            .height(50.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(backgroundColor)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {}
            ),
        contentAlignment = Alignment.Center
    ) {
        if (icon != null) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(22.dp)
            )
        } else if (label != null) {
            Text(
                text = if (label == "space") "" else label,
                color = textColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}