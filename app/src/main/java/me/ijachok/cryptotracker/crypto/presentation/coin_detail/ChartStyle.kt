package me.ijachok.cryptotracker.crypto.presentation.coin_detail

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit

class ChartStyle(
    val chartLineColor:Color,
    val selectedColor:Color,
    val unselectedColor:Color,
    val helperLinesThicknessPx:Float,
    val selectedHelperLinesThicknessPx:Float = helperLinesThicknessPx*2,
    val chartLineThicknessPx:Float,
    val textStyle:TextStyle,
    val minYLabelSpacing: Dp,
    val verticalPadding: Dp,
    val horizontalPadding: Dp,
    val xAxisLabelSpacing: Dp,
) {
}