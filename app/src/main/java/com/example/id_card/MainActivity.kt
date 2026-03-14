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
                    if (bitMatrix.get(x, y)) android.graphics.Color.BLACK else android.graphics.Color.WHITE
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
                .fillMaxWidth(0.9f)
                .height(40.dp),
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
        Box(modifier = Modifier.aspectRatio(1.586f)) {
            // Top maroon band
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .background(maroonColor)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    // [✓] NDU Logo - White oval background overlapping the maroon band
                    Box(
                        modifier = Modifier
                            .align(Alignment.Bottom)
                            .width(85.dp)
                            .height(100.dp)
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

                    // [✓] Photo Stack
                    Box(contentAlignment = Alignment.Center) {
                        Box(
                            modifier = Modifier
                                .size(85.dp)
                                .background(maroonColor, CircleShape)
                        )
                        Box(modifier = Modifier.size(78.dp)) {
                            Image(
                                painter = painterResource(id = R.drawable.mine1),
                                contentDescription = "Student Photo",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                                    .border(2.dp, Color.White, CircleShape),
                                contentScale = ContentScale.Crop
                            )
                            Image(
                                painter = painterResource(id = R.drawable.logo),
                                contentDescription = "Watermark",
                                modifier = Modifier
                                    .size(20.dp)
                                    .align(Alignment.BottomEnd)
                                    .padding(bottom = 2.dp, end = 2.dp)
                                    .alpha(0.85f)
                            )
                        }
                    }

                    // Ugandan Flag
                    Image(
                        painter = painterResource(id = R.drawable.uganda_flag),
                        contentDescription = "Flag",
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .size(55.dp, 35.dp)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "MUGISHA MICHEAL",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    color = Color.Black
                )

                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Programme: ")
                        }
                        append("BSc in Computer Science")
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.DarkGray
                )

                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("REG NO: ")
                        }
                        append(regNo)
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.weight(1f))

                HorizontalDivider(color = Color.LightGray)

                Row(modifier = Modifier.padding(vertical = 2.dp)) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("Issue: ")
                            }
                            append("01/02/2026   ")
                        },
                        fontSize = 9.sp
                    )
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("Expiry: ")
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