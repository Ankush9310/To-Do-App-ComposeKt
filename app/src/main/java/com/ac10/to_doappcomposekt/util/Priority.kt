package com.ac10.to_doappcomposekt.util

import androidx.compose.ui.graphics.Color
import com.ac10.to_doappcomposekt.ui.theme.HighPriorityColor
import com.ac10.to_doappcomposekt.ui.theme.LowPriorityColor
import com.ac10.to_doappcomposekt.ui.theme.MediumPriorityColor
import com.ac10.to_doappcomposekt.ui.theme.NonePriorityColor

enum class Priority(
    val color: Color
) {
    HIGH(HighPriorityColor),
    MEDIUM(MediumPriorityColor),
    LOW(LowPriorityColor),
    NONE(NonePriorityColor)

}