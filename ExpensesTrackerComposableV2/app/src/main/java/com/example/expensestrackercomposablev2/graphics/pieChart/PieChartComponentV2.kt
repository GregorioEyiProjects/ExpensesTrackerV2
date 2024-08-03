package com.example.expensestrackercomposablev2.graphics.pieChart

import android.graphics.Paint
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expensestrackercomposablev2.models.ItemPurchase
import com.example.expensestrackercomposablev2.models.ItemPurchaseV2
import com.example.expensestrackercomposablev2.ui.theme.gray
import com.example.expensestrackercomposablev2.ui.theme.white
import kotlin.math.PI
import kotlin.math.atan2

@Composable
fun PieChartComponentV2(
    modifier: Modifier = Modifier,
    radius: Float = 400f, // how big it will be by default (external radios)
    innerRadios: Float = 200f,
    transparentWith: Float = 70f,
    inputData: List<ItemPurchaseV2>,
    centerText: String = ""
) {

    var circleCenter by remember { mutableStateOf(Offset.Zero) }
    var inputList by remember { mutableStateOf(inputData) }
    var isCenterTapped by remember { mutableStateOf(false) }

    Log.d("Size of the list is", "${inputList.size}")

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = modifier
                .fillMaxSize()
                .pointerInput(true) {
                    detectTapGestures(
                        onTap = {offset->
                            val tapAngleInDegrees = (-atan2(
                                x = circleCenter.y - offset.y,
                                y = circleCenter.x - offset.x
                            ) * (180f / PI).toFloat() - 90f).mod(360f)

                            //Check if the user has tapped in the inner circle or on one the item outside the pie
                            val centerClicked = if( tapAngleInDegrees < 90){
                                offset.x < circleCenter.x + innerRadios && offset.y < circleCenter.y + innerRadios
                            }else if (tapAngleInDegrees < 180){
                                offset.x > circleCenter.x - innerRadios && offset.y < circleCenter.y + innerRadios
                            }else if (tapAngleInDegrees < 270){
                                offset.x > circleCenter.x - innerRadios && offset.y > circleCenter.y - innerRadios
                            }else{
                                offset.x < circleCenter.x + innerRadios && offset.y > circleCenter.y - innerRadios
                            }

                            if (centerClicked){ // if center circle
                                inputList = inputList.map {item->
                                    item.copy(isTapped = !isCenterTapped)
                                }
                                isCenterTapped = !isCenterTapped
                            }else{ // if outside the center circle

                                val anglePerValue = 360f/inputData.sumOf { it.value }
                                var currentAngle = 0f
                                inputList.forEach { item ->
                                    currentAngle += item.value * anglePerValue
                                    if(tapAngleInDegrees < currentAngle){
                                        val description = item.description
                                        inputList = inputList.map {
                                            if (description == it.description){
                                                it.copy(isTapped = !it.isTapped)
                                            }else{
                                                it.copy(isTapped = false)
                                            }
                                        }
                                        return@detectTapGestures // to get out of the loop
                                    }
                                }
                            }
                        }
                    )
                }
        ) {
            //Values for the Canvas
            val with = size.width
            val height = size.height
            circleCenter = Offset(x = with / 2f, y = height / 2f)
            val totalValue = inputList.sumOf { item ->
                item.value
            } // The total value of the list
            val anglePerValue = 360f / totalValue
            var currentStartAngle = 0f

            //-------------------- (START) For each element on the list
            inputList.forEach { item ->

                val scaleValue = if (item.isTapped) 1.1f else 1.0f
                val angleToDraw = item.value * anglePerValue

                scale(scaleValue) {
                    drawArc(
                        color = item.color,
                        startAngle = currentStartAngle,
                        sweepAngle = angleToDraw,
                        useCenter = true,
                        size = Size(
                            width = radius * 2f,
                            height = radius * 2f
                        ),
                        topLeft = Offset(
                            (with - radius * 2f) / 2f,
                            (height - radius * 2f) / 2f
                        )
                    )
                    currentStartAngle += angleToDraw
                }

                //(START) To be able to display the percentages in the Pie chart
                var rotateAngle = currentStartAngle - angleToDraw / 2f - 90f // with parenthesis it does not work properly
                var factor = 1f // for the text in the top and button of the pie
                if (rotateAngle > 90f) {
                    rotateAngle = (rotateAngle + 180).mod(360f)
                    factor = -0.92f // the different between the pie chart and the text
                }

                val percentage = (item.value / totalValue.toFloat() * 100).toInt()

                drawContext.canvas.nativeCanvas.apply {
                    if (percentage > 0) {
                        rotate(rotateAngle) {
                            drawText(
                                "$percentage %",
                                circleCenter.x,
                                circleCenter.y + (radius - (radius - innerRadios) / 2f) * factor,
                                Paint().apply {
                                    textSize = 13.sp.toPx()
                                    textAlign = Paint.Align.CENTER
                                    color = white.toArgb()
                                }
                            )
                        }
                    }
                }
                //(END) To be able to display the percentages in the Pie chart

                /* (START) To te able to tab on an item and display and Scale it out*/
                if (item.isTapped) {

                    val tabRotation = currentStartAngle - angleToDraw - 90f

                    //Line on the left of the pie
                    rotate(tabRotation) {
                        drawRoundRect(
                            topLeft = circleCenter,
                            size = Size(12f, radius * 1.2f),
                            color = gray,
                            cornerRadius = CornerRadius(15f, 15f)
                        )
                    }

                    //Line on the right of the pie
                    rotate(tabRotation + angleToDraw) {// error here not *, should be +
                        drawRoundRect(
                            topLeft = circleCenter,
                            size = Size(12f, radius * 1.2f),
                            color = gray,
                            cornerRadius = CornerRadius(15f, 15f)
                        )
                    }

                    //The description text
                    rotate(rotateAngle) {
                        drawContext.canvas.nativeCanvas.apply {
                            drawText(
                                "${item.description} : ${item.value} THB",
                                circleCenter.x,
                                circleCenter.y + radius * 1.3f * factor,
                                Paint().apply {
                                    textSize = 15.sp.toPx()
                                    textAlign = Paint.Align.CENTER
                                    color = white.toArgb()
                                    isFakeBoldText = true
                                }
                            )
                        }
                    }
                }
                /* (END) To te able to tab on an item and display and Scale it out*/

            }
            //-------------------- (END) For each element on the list

            //For the first item that will be tapped to display (additional code since when tapping on some of the item, it does not display the drawRoundRect() correctly )
            if(inputList.first().isTapped){
                rotate(-90f){ // not 90f it should be -90f
                    drawRoundRect(
                        topLeft = circleCenter,
                        size = Size(12f, radius * 1.2f),
                        color = gray,
                        cornerRadius = CornerRadius(15f, 15f)
                    )
                }
            }

            // Where the Inner center circle is drawn
            drawContext.canvas.nativeCanvas.apply {
                drawCircle(
                    circleCenter.x,
                    circleCenter.y,
                    innerRadios,
                    Paint().apply {
                        color = white.copy(alpha = 0.3f).toArgb() // to show the background
                        setShadowLayer(5f, 0f, 0f, gray.toArgb())
                    }
                )
            }

            //The transparent circle around the innerCenter circle
            drawCircle(
                color = white.copy(alpha = 0.5f),
                radius = (innerRadios + transparentWith) / 2f
            )

        }

        //Text that goes in the center of the pie
        Text(
            text = centerText,
            modifier = Modifier
                .width(Dp(innerRadios / 1.5f))
                .padding(20.dp),
            fontSize = 17.sp,
            textAlign = TextAlign.Center
        )

    }

}