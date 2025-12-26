package com.example.logiclyst.ime.comps

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.logiclyst.ime.KeyboardState

@Composable
fun RowScope.KeyboardKey(
    label: String? = null,
    icon: Int? = null,
    weight: Float = 1f,
    backgroundColor: Color = Color.White,
    textColor: Color = Color.Black,
    iconColor: Color = Color.Unspecified,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onClick: () -> Unit
) {
    val isDarkMode = KeyboardState.isDarkMode.value
    val rippleColor = if (isDarkMode) Color.White.copy(alpha = 0.1f) else Color.Gray.copy(alpha = 0.2f)

    Box(
        modifier = Modifier
            .weight(weight)
            .height(54.dp)
            .padding(1.5.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(bounded = true, color = rippleColor),
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        if (label != null) {
            Text(
                text = label,
                color = textColor,
                fontSize = 18.sp
            )
        } else if (icon != null) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}