package com.example.id_card

import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.id_card.ui.theme.ID_CardTheme
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ID_CardTheme {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    NdejjeUniversityID()
                }
            }
        }
    }
}

/* ---------------- BARCODE GENERATOR ---------------- */

fun generateBarcode(text: String, width: Int, height: Int): Bitmap? {
    return try {
        val bitMatrix = MultiFormatWriter().encode(text, BarcodeFormat.CODE_128, width, height)
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(
                    x, y,
                    if (bitMatrix.get(x, y)) android.graphics.Color.BLACK else android.graphics.Color.TRANSPARENT
                )
            }
        }
        bitmap
    } catch (e: Exception) { null }
}

@Composable
fun DynamicBarcode(regNumber: String) {
    val barcodeBitmap = remember(regNumber) {
        generateBarcode(regNumber, 800, 100)
    }
    barcodeBitmap?.let {
        Image(
            bitmap = it.asImageBitmap(),
            contentDescription = "Student Barcode",
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(25.dp),
            contentScale = ContentScale.FillBounds
        )
    }
}

/* ---------------- STUDENT ID CARD ---------------- */

@Composable
fun NdejjeUniversityID() {
    val maroonColor = Color(0xFF800000)
    val regNo = "24/2/306/D/049"

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White)
    ) {
        Box(modifier = Modifier.aspectRatio(1.486f)) {

            // [✓] Faded Watermarks (Background logos)
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                modifier = Modifier
                    .size(140.dp)
                    .align(Alignment.BottomStart)
                    .offset(x = (-40).dp, y = -10.dp)
                    .alpha(0.06f)
            )
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                modifier = Modifier
                    .size(140.dp)
                    .align(Alignment.BottomEnd)
                    .offset(x = 40.dp, y = -10.dp)
                    .alpha(0.06f)
            )

            // Top maroon band
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(75.dp)
                    .background(maroonColor)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header Area Box - allows precise bottom alignment to reduce space to name
                Box(modifier = Modifier.fillMaxWidth().height(110.dp)) {
                    // [✓] NDU Logo Oval Background
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .width(85.dp)
                            .height(105.dp)
                            .background(Color.White, CircleShape)
                            .clip(CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = "NDU Logo",
                            modifier = Modifier
                                .size(115.dp)
                                .offset(y = (-10).dp),
                            contentScale = ContentScale.Fit
                        )
                    }

                    // [✓] Photo Stack - Positioned at BottomCenter of header area
                    Box(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(105.dp)
                                .background(maroonColor, CircleShape)
                        )
                        Box(modifier = Modifier.size(98.dp)) {
                            Image(
                                painter = painterResource(id = R.drawable.mine1),
                                contentDescription = "Student Photo",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                                    .border(3.dp, Color.White, CircleShape),
                                contentScale = ContentScale.Crop
                            )
                            Image(
                                painter = painterResource(id = R.drawable.logo),
                                contentDescription = "Watermark",
                                modifier = Modifier
                                    .size(24.dp)
                                    .align(Alignment.BottomEnd)
                                    .padding(bottom = 4.dp, end = 4.dp)
                                    .alpha(0.85f)
                            )
                        }
                    }

                    // Ugandan Flag - Top Right
                    Image(
                        painter = painterResource(id = R.drawable.uganda_flag),
                        contentDescription = "Flag",
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 8.dp)
                            .size(65.dp, 40.dp)
                    )
                }

                // Tight space between photo and name
                Text(
                    text = "MUGISHA MICHEAL",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Black,
                    color = Color.Black,
                    modifier = Modifier.padding(top = 1.dp)
                )

                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Programme: ")
                        }
                        append("BSc in Computer Science")
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Black
                )

                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Registration Number: ")
                        }
                        append(regNo)
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.weight(1f))

                HorizontalDivider(modifier = Modifier.padding(horizontal = 24.dp),
                    color = Color.LightGray)

                Row(modifier = Modifier.padding(horizontal = 24.dp, vertical = 2.dp)) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("Date of Issue: ")
                            }
                            append("01/02/2026   ")
                        },
                        fontSize = 9.sp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("Expiry Date: ")
                            }
                            append("01/02/2029")
                        },
                        fontSize = 9.sp
                    )
                }

                DynamicBarcode(regNo)
            }

            // Bottom decorative band
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(maroonColor)
                    .align(Alignment.BottomCenter)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewID() {
    ID_CardTheme {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            NdejjeUniversityID()
        }
    }
}
