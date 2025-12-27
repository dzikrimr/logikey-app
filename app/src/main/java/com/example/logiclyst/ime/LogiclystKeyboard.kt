package com.example.logiclyst.ime

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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.emoji2.emojipicker.EmojiPickerView
import com.example.logiclyst.R
import com.example.logiclyst.ime.comps.*
import com.example.logiclyst.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun LogiclystKeyboard(
    detectedFallacy: String? = null,
    onKeyClick: (String) -> Unit,
    onMicClick: () -> Unit
) {
    var viewMode by remember { mutableStateOf(0) }
    val response by KeyboardState.fullResponse
    var showDetail by remember { mutableStateOf(false) }

    val isDarkMode by KeyboardState.isDarkMode
    val isHapticEnabled by KeyboardState.isHapticEnabled
    val haptic = LocalHapticFeedback.current

    var shiftState by remember { mutableStateOf(0) }
    var isSymbolMode by remember { mutableStateOf(false) }
    var symbolPage by remember { mutableStateOf(0) }
    var lastShiftClickTime by remember { mutableStateOf(0L) }

    val bgColor = if (isDarkMode) Color(0xFF121212) else KeyboardBackground
    val keyBgColor = if (isDarkMode) Color(0xFF2C2C2C) else Color.White
    val functionalKeyBg = if (isDarkMode) Color(0xFF3D3D3D) else KeyGray
    val mainTextColor = if (isDarkMode) Color.White else Color.Black
    val iconTint = if (isDarkMode) Color.White else Color.Black

    val handleKeyClick: (String) -> Unit = { label ->
        onKeyClick(label)
    }

    val numericRow = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0")
    val qwertyRows = listOf(
        listOf("Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"),
        listOf("A", "S", "D", "F", "G", "H", "J", "K", "L"),
        listOf("Z", "X", "C", "V", "B", "N", "M")
    )
    val symbolRowsPage1 = listOf(
        listOf("@", "#", "$", "%", "&", "-", "+", "(", ")"),
        listOf("*", "\"", "'", ":", ";", "!", "?", "/", "=")
    )
    val symbolRowsPage2 = listOf(
        listOf("_", "~", "`", "|", "•", "√", "π", "÷", "×"),
        listOf("^", "°", "<", ">", "{", "}", "[", "]", "\\"),
        listOf("£", "¢", "€", "©", "®", "™", "∆", "∞")
    )

    Box(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(bgColor)
                .padding(bottom = 8.dp)
        ) {
            KeyboardTopBar(
                detectedFallacy = detectedFallacy,
                onShowDetail = {
                    if (isHapticEnabled) haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    showDetail = true
                },
                onMicClick = {
                    if (isHapticEnabled) haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onMicClick()
                }
            )

            if (viewMode == 1) {
                Box(modifier = Modifier.height(250.dp).fillMaxWidth()) {
                    AndroidView(
                        factory = { context ->
                            EmojiPickerView(context).apply {
                                setOnEmojiPickedListener { emoji -> handleKeyClick(emoji.emoji) }
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp)) {
                    KeyboardKey(label = "ABC", backgroundColor = functionalKeyBg, textColor = mainTextColor, weight = 1.5f, onClick = { viewMode = 0 })
                    RepeatingKey(label = "space", backgroundColor = keyBgColor, textColor = mainTextColor, weight = 3f, onClick = { handleKeyClick(" ") })
                    BackspaceKey(onKeyClick = handleKeyClick, bgColor = functionalKeyBg)
                }
            } else {
                Column(modifier = Modifier.padding(horizontal = 1.dp, vertical = 4.dp)) {

                    if (!isSymbolMode || (isSymbolMode && symbolPage == 0)) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            numericRow.forEach { num ->
                                RepeatingKey(
                                    label = num,
                                    backgroundColor = keyBgColor,
                                    textColor = mainTextColor,
                                    onClick = { handleKeyClick(num) }
                                )
                            }
                        }
                    }

                    val processLabel: (String) -> String = { char ->
                        if (!isSymbolMode && shiftState == 0) char.lowercase() else char
                    }

                    val rowA = if (isSymbolMode) {
                        if (symbolPage == 0) symbolRowsPage1[0] else symbolRowsPage2[0]
                    } else {
                        qwertyRows[0]
                    }

                    val rowB = if (isSymbolMode) {
                        if (symbolPage == 0) symbolRowsPage1[1] else symbolRowsPage2[1]
                    } else {
                        qwertyRows[1]
                    }

                    Row(modifier = Modifier.fillMaxWidth()) {
                        rowA.map { processLabel(it) }.forEach { label ->
                            RepeatingKey(label = label, backgroundColor = keyBgColor, textColor = mainTextColor, onClick = { handleKeyClick(label) })
                        }
                    }

                    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = if (isSymbolMode) 4.dp else 18.dp)) {
                        rowB.map { processLabel(it) }.forEach { label ->
                            RepeatingKey(label = label, backgroundColor = keyBgColor, textColor = mainTextColor, onClick = { handleKeyClick(label) })
                        }
                    }

                    Row(modifier = Modifier.fillMaxWidth()) {
                        KeyboardKey(
                            icon = if (isSymbolMode) null else R.drawable.ic_arrow_up,
                            label = if (isSymbolMode) (if (symbolPage == 0) "1/2" else "2/2") else null,
                            backgroundColor = if (shiftState > 0 || isSymbolMode) Color.White else functionalKeyBg,
                            iconColor = when {
                                shiftState == 2 -> Color.Blue
                                shiftState == 1 -> DeepIndigo
                                else -> iconTint
                            },
                            weight = 1.5f,
                            onClick = {
                                if (isHapticEnabled) haptic.performHapticFeedback(HapticFeedbackType.LongPress)

                                if (isSymbolMode) {
                                    symbolPage = if (symbolPage == 0) 1 else 0
                                } else {
                                    val currentTime = System.currentTimeMillis()
                                    if (currentTime - lastShiftClickTime < 300) {
                                        shiftState = if (shiftState == 2) 0 else 2
                                    } else {
                                        shiftState = if (shiftState == 0) 1 else 0
                                    }
                                    lastShiftClickTime = currentTime
                                }
                            }
                        )

                        val rowC = if (isSymbolMode) {
                            if (symbolPage == 1) symbolRowsPage2[2] else emptyList()
                        } else {
                            qwertyRows[2]
                        }

                        rowC.forEach { char ->
                            val label = processLabel(char)
                            RepeatingKey(label = label, backgroundColor = keyBgColor, textColor = mainTextColor, onClick = {
                                handleKeyClick(label)
                                if (shiftState == 1) shiftState = 0
                            })
                        }

                        if (isSymbolMode && symbolPage == 0) {
                            Spacer(modifier = Modifier.weight(1f))
                        }

                        BackspaceKey(onKeyClick = handleKeyClick, bgColor = functionalKeyBg)
                    }

                    Row(modifier = Modifier.fillMaxWidth()) {
                        KeyboardKey(label = if (isSymbolMode) "ABC" else "?123", backgroundColor = functionalKeyBg, textColor = mainTextColor, weight = 1.5f, onClick = {
                            if (isHapticEnabled) haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            isSymbolMode = !isSymbolMode; symbolPage = 0
                        })
                        RepeatingKey(label = ",", backgroundColor = keyBgColor, textColor = mainTextColor, weight = 1f, onClick = { handleKeyClick(",") })
                        RepeatingKey(label = "space", backgroundColor = keyBgColor, weight = 4f, textColor = Color.Gray, onClick = { handleKeyClick(" ") })
                        RepeatingKey(label = ".", backgroundColor = keyBgColor, textColor = mainTextColor, weight = 1f, onClick = { handleKeyClick(".") })
                        KeyboardKey(icon = R.drawable.ic_emoji, backgroundColor = functionalKeyBg, iconColor = iconTint, weight = 1f, onClick = {
                            if (isHapticEnabled) haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            viewMode = 1
                        })
                        KeyboardKey(label = "enter", backgroundColor = DeepIndigo, weight = 1.5f, textColor = Color.White, onClick = { handleKeyClick("ENTER") })
                    }
                }
            }
        }

        if (showDetail && response != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 0.dp, max = 600.dp)
                    .background(Color.Transparent)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.4f))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { showDetail = false }
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { }
                ) {
                    FallacyDetailSheet(
                        response = response!!,
                        onDismiss = { showDetail = false }
                    )
                }
            }
        }
    }
}

@Composable
fun RowScope.RepeatingKey(
    label: String,
    backgroundColor: Color,
    textColor: Color = Color.Black,
    weight: Float = 1f,
    onClick: (String) -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val isHapticEnabled by KeyboardState.isHapticEnabled
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    LaunchedEffect(isPressed) {
        if (isPressed) {
            if (isHapticEnabled) haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            onClick(label)
            delay(450)
            while (isPressed) {
                if (isHapticEnabled) haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                onClick(label)
                delay(60)
            }
        }
    }

    KeyboardKey(
        label = label,
        backgroundColor = backgroundColor,
        textColor = textColor,
        weight = weight,
        interactionSource = interactionSource,
        onClick = {}
    )
}

@Composable
fun RowScope.BackspaceKey(onKeyClick: (String) -> Unit, bgColor: Color) {
    val haptic = LocalHapticFeedback.current
    val isHapticEnabled by KeyboardState.isHapticEnabled
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isDarkMode by KeyboardState.isDarkMode

    LaunchedEffect(isPressed) {
        if (isPressed) {
            if (isHapticEnabled) haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            onKeyClick("BACKSPACE")
            delay(500)
            while (isPressed) {
                if (isHapticEnabled) haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                onKeyClick("BACKSPACE")
                delay(60)
            }
        }
    }

    KeyboardKey(
        icon = R.drawable.ic_backspace,
        backgroundColor = bgColor,
        iconColor = if (isDarkMode) Color.White else Color.Black,
        weight = 1.5f,
        interactionSource = interactionSource,
        onClick = {}
    )
}