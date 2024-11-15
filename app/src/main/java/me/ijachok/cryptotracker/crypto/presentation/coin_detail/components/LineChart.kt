package me.ijachok.cryptotracker.crypto.presentation.coin_detail.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEachIndexed
import me.ijachok.cryptotracker.crypto.domain.CoinPrice
import me.ijachok.cryptotracker.crypto.presentation.coin_detail.ChartStyle
import me.ijachok.cryptotracker.crypto.presentation.coin_detail.DataPoint
import me.ijachok.cryptotracker.crypto.presentation.coin_detail.ValueLabel
import me.ijachok.cryptotracker.ui.theme.CryptoTrackerTheme
import me.ijachok.cryptotracker.ui.theme.bodyFontFamily
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random

@Composable
fun LineChart(
    modifier: Modifier = Modifier,
    dataPoints: List<DataPoint>,
    style: ChartStyle,
    visibleDataPointsIndices: IntRange,
    unitSign: String,
    showHelperLines: Boolean = true,
    selectedDataPoint: DataPoint? = null,
    onSelectedDataPoint: (DataPoint) -> Unit = {},
    onXLabelWidthChange: (Float) -> Unit = {}

) {
    val visibleDataPoints = remember(dataPoints, visibleDataPointsIndices) {
        dataPoints.slice(visibleDataPointsIndices)
    }
    val maxYValue = remember(visibleDataPoints) {
        visibleDataPoints.maxOfOrNull { it.y } ?: 0f
    }
    val minYValue = remember(visibleDataPoints) {
        visibleDataPoints.minOfOrNull { it.y } ?: 0f
    }
    val measurer = rememberTextMeasurer()
    var xLabelWidth by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(key1 = xLabelWidth) { onXLabelWidthChange(xLabelWidth) }
    val selectedDataPointIndex =
        remember(selectedDataPoint) { dataPoints.indexOf(selectedDataPoint) }
    var drawPoints by remember { mutableStateOf(listOf<DataPoint>()) }
    var isShowingDataPoints by remember { mutableStateOf(selectedDataPoint != null) }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(drawPoints, xLabelWidth) {
                detectHorizontalDragGestures { change, _ ->
                    val newSelectedDataPointIndex = getSelectedDataPointIndex(
                        touchOffsetX = change.position.x,
                        triggerWidth = xLabelWidth,
                        drawPoints = drawPoints
                    )
                    isShowingDataPoints =
                        (newSelectedDataPointIndex + visibleDataPointsIndices.first) in visibleDataPointsIndices
                    if (isShowingDataPoints) onSelectedDataPoint(dataPoints[newSelectedDataPointIndex])
                }
            }
    ) {
        val minYLabelSpacingPx = style.minYLabelSpacing.toPx()
        val verticalPaddingPx = style.verticalPadding.toPx()
        val horizontalPaddingPx = style.horizontalPadding.toPx()
        val xAxisLabelSpacingPx = style.xAxisLabelSpacing.toPx()
        // x label calculations
        val xLabelTextLayoutResults = visibleDataPoints.map {
            measurer.measure(
                text = it.xLabel,
                style = style.textStyle.copy(textAlign = TextAlign.Center)
            )
        }

        val maxXLabelHeight = xLabelTextLayoutResults.maxOfOrNull { it.size.height } ?: 0
        val maxXLabelWidth = xLabelTextLayoutResults.maxOfOrNull { it.size.width } ?: 0
        val maxXLabelLineCount = xLabelTextLayoutResults.maxOfOrNull { it.lineCount } ?: 1

        val xLabelLineHeight = maxXLabelHeight / maxXLabelLineCount

        // y label calculations
        val viewPortHeightPx =
            size.height - (maxXLabelHeight + 2 * verticalPaddingPx + xLabelLineHeight + xAxisLabelSpacingPx)

        val labelViewPortHeightPx = viewPortHeightPx + xLabelLineHeight
        val labelCountWithoutLast =
            (labelViewPortHeightPx / (xLabelLineHeight + minYLabelSpacingPx)).toInt()
        val valueIncrement = (maxYValue - minYValue) / labelCountWithoutLast
        val yLabels = (0..labelCountWithoutLast).map {
            ValueLabel(value = maxYValue - valueIncrement * it, unit = unitSign)
        }
        val yLabelLayoutResults = yLabels.map {
            measurer.measure(
                text = it.formatted(),
                style = style.textStyle
            )
        }
        val maxYLabelWidth = yLabelLayoutResults.maxOfOrNull { it.size.width } ?: 0

        // chart viewport calculations
        val viewPortTopY = verticalPaddingPx + xLabelLineHeight + 10f
        val viewPortRightX = size.width - horizontalPaddingPx
        val viewPortBottomY = viewPortTopY + viewPortHeightPx
        val viewPortLeftX = 2f * horizontalPaddingPx + maxYLabelWidth

        val viewPortRect = Rect(
            top = viewPortTopY,
            bottom = viewPortBottomY,
            right = viewPortRightX,
            left = viewPortLeftX
        )


        xLabelWidth = maxXLabelWidth + xAxisLabelSpacingPx
        xLabelTextLayoutResults.forEachIndexed { index, textLayoutResult ->
            val x =
                viewPortLeftX + xAxisLabelSpacingPx / 2f + xLabelWidth * index + maxXLabelWidth / 2
            drawText(
                textLayoutResult = textLayoutResult,
                color = if (selectedDataPointIndex == index) style.selectedColor else style.unselectedColor,
                topLeft = Offset(
                    x = x - maxXLabelWidth / 2,
                    y = viewPortBottomY + verticalPaddingPx
                )
            )

            if (showHelperLines) {
                drawLine(
                    color = if (selectedDataPointIndex == index) style.selectedColor else style.unselectedColor,
                    start = Offset(
                        x = x,
                        y = viewPortTopY
                    ),
                    end = Offset(
                        x = x,
                        y = viewPortBottomY
                    ),
                    strokeWidth =
                    if (selectedDataPointIndex == index) style.helperLinesThicknessPx * 2
                    else style.helperLinesThicknessPx
                )
            }

            if (selectedDataPointIndex == index) {
                val valueLabel = ValueLabel(value = visibleDataPoints[index].y, unit = unitSign)
                val valueResult = measurer.measure(
                    text = valueLabel.formatted(),
                    style = style.textStyle.copy(color = style.selectedColor),
                    maxLines = 1
                )
                val textPositionX = if (selectedDataPointIndex == visibleDataPointsIndices.last)
                    x - valueResult.size.width
                else
                    x - valueResult.size.width / 2
                drawText(
                    textLayoutResult = valueResult,
                    color = style.selectedColor,
                    topLeft = Offset(
                        x = textPositionX,
                        y = viewPortTopY - xLabelLineHeight *1.5f
                    )
                )
            }
        }


        yLabelLayoutResults.forEachIndexed { index, textLayoutResult ->
            val y =
                viewPortTopY - xLabelLineHeight / 2f + viewPortHeightPx / (labelCountWithoutLast) * index
            drawText(
                textLayoutResult = textLayoutResult,
                color = style.unselectedColor,
                topLeft = Offset(
                    x = horizontalPaddingPx + maxYLabelWidth - textLayoutResult.size.width,
                    y = y
                )
            )
            if (showHelperLines) {
                drawLine(
                    color = style.unselectedColor,
                    start = Offset(
                        x = viewPortLeftX,
                        y = y + xLabelLineHeight / 2f
                    ),
                    end = Offset(
                        x = viewPortRightX,
                        y = y + xLabelLineHeight / 2f
                    ),
                    strokeWidth = style.helperLinesThicknessPx
                )
            }
        }

        drawPoints = visibleDataPointsIndices.map {
            val x =
                viewPortLeftX + (it - visibleDataPointsIndices.first) *
                        xLabelWidth + xLabelWidth / 2f
            val yRatio = (dataPoints[it].y - minYValue) / (maxYValue - minYValue)
            val y = viewPortBottomY - (yRatio * viewPortHeightPx)
            DataPoint(x = x, y = y, xLabel = dataPoints[it].xLabel)
        }

        val connectionPoints1 = mutableListOf<DataPoint>()
        val connectionPoints2 = mutableListOf<DataPoint>()
        for (i in 1 until drawPoints.size) {
            val p0 = drawPoints[i - 1]
            val p1 = drawPoints[i]

            val x = (p1.x + p0.x) / 2f
            val y1 = p0.y
            val y2 = p1.y

            connectionPoints1.add(DataPoint(x, y1, ""))
            connectionPoints2.add(DataPoint(x, y2, ""))
        }

        val linePath = Path().apply {
            if (drawPoints.isNotEmpty()) {
                moveTo(drawPoints.first().x, drawPoints.first().y)

                for (i in 1 until drawPoints.size) {
                    cubicTo(
                        x1 = connectionPoints1[i - 1].x,
                        y1 = connectionPoints1[i - 1].y,
                        x2 = connectionPoints2[i - 1].x,
                        y2 = connectionPoints2[i - 1].y,
                        x3 = drawPoints[i].x,
                        y3 = drawPoints[i].y

                    )
                }
            }
        }
        drawPath(
            path = linePath,
            color = style.chartLineColor,
            style = Stroke(width = style.chartLineThicknessPx)
        )

        if (isShowingDataPoints) {
            drawPoints.fastForEachIndexed { index, dataPoint ->
                drawCircle(
                    color = if (selectedDataPointIndex == index) style.selectedColor else style.chartLineColor,
                    radius = 10f,
                    center = Offset(x = dataPoint.x, y = dataPoint.y)
                )
            }
        }
    }
}

private fun getSelectedDataPointIndex(
    touchOffsetX: Float,
    triggerWidth: Float,
    drawPoints: List<DataPoint>
): Int {
    val triggerRangeLeft = touchOffsetX - triggerWidth / 2f
    val triggerRangeRight = touchOffsetX + triggerWidth / 2f
    return drawPoints.indexOfFirst {
        it.x in triggerRangeLeft..triggerRangeRight
    }
}

@PreviewLightDark
@Composable
private fun LCPrev() {
    CryptoTrackerTheme {
        val coinHistoryRandom = remember {
            (1..20).map {
                CoinPrice(
                    priceUsd = Random.nextDouble() * 1000.0,
                    dateTime = ZonedDateTime.now().plusHours(it.toLong())
                )
            }
        }
        val style = ChartStyle(
            chartLineColor = MaterialTheme.colorScheme.tertiary,
            unselectedColor = MaterialTheme.colorScheme.outline,
            selectedColor = MaterialTheme.colorScheme.primary,
            helperLinesThicknessPx = 1f,
            chartLineThicknessPx = 5f,
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 14.sp,
                fontFamily = bodyFontFamily
            ),
            minYLabelSpacing = 25.dp,
            verticalPadding = 8.dp,
            horizontalPadding = 8.dp,
            xAxisLabelSpacing = 8.dp
        )
        val dataPoints = remember {
            coinHistoryRandom.map {
                DataPoint(
                    x = it.dateTime.hour.toFloat(),
                    y = it.priceUsd.toFloat(),
                    xLabel = DateTimeFormatter.ofPattern("ha\nM/d").format(it.dateTime)
                )
            }
        }

        LineChart(
            modifier = Modifier
                .width(500.dp)
                .height(300.dp)
                .background(MaterialTheme.colorScheme.surface),
            dataPoints = dataPoints,
            style = style,
            visibleDataPointsIndices = 0..19,
            unitSign = "$",
            selectedDataPoint = dataPoints[1],
        )
    }
}