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
                .height(45.dp),
            contentScale = ContentScale.FillBounds
        )
    }
}

/* ---------------- STUDENT ID CARD ---------------- */

@Composable
fun NdejjeUniversityID() {
    val maroonColor = Color(0xFF800000)
    val regNo = "24/S/1234/AS"

    // [✓] The Outer Shell: ElevatedCard, RoundedCornerShape(16.dp), elevation 8.dp
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White)

    ) {
        Box(modifier = Modifier.aspectRatio(1.000f)) {
            // Edge-to-edge maroon band background
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(115.dp)
                    .background(maroonColor)
            )

            // [✓] The Main Container: Column, horizontalAlignment = CenterHorizontally, padding 16.dp
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // [✓] The Header Row: SpaceBetween pushes Logo left and Flag right
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    // NDU Logo in white circular surface
                    Surface(
                        shape = CircleShape,
                        color = Color.White,
                        modifier = Modifier
                            .offset(y = 20.dp)

                            .size(100.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = "NDU Logo",
                            modifier = Modifier
                                .offset(y = 20.dp)
                                .padding(5.dp),
                            contentScale = ContentScale.Fit
                        )
                    }

                    // [✓] The Photo Stack: Box with Student Photo + Watermark Logo
                    Box(contentAlignment = Alignment.Center) {
                        // Outer maroon ring
                        Box(
                            modifier = Modifier
                                .size(138.dp)
                                .background(maroonColor, CircleShape)
                        )
                        // Photo container
                        Box(modifier = Modifier.size(130.dp)) {
                            // Bottom layer: Student Photo clipped to circle
                            Image(
                                painter = painterResource(id = R.drawable.mine1),
                                contentDescription = "Student Photo",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                                    .border(4.dp, Color.White, CircleShape),
                                contentScale = ContentScale.Crop
                            )
                            // Top layer: NDU logo watermark aligned to BottomEnd
                            Image(
                                painter = painterResource(id = R.drawable.logo),
                                contentDescription = "Watermark",
                                modifier = Modifier
                                    .size(32.dp)
                                    .align(Alignment.BottomEnd)
                                    .padding(bottom = 6.dp, end = 6.dp)
                                    .alpha(0.85f)
                            )
                        }
                    }

                    // Ugandan Flag
                    Image(
                        painter = painterResource(id = R.drawable.uganda_flag),
                        contentDescription = "Flag",
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .size(70.dp, 45.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // [✓] Typography: headlineSmall for student's name
                Text(
                    text = "ATUHAIRE BRENDA",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Black,
                    color = Color.Black
                )

                // [✓] Typography: FontWeight.Bold for labels
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Programme: ")
                        }
                        append("BSc in Computer Science")
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray
                )

                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Registration Number: ")
                        }
                        append(regNo)
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(8.dp))

                // [✓] The Footer: HorizontalDivider() followed by dates and barcode
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                    color = Color.LightGray
                )

                Row {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("Date of Issue: ")
                            }
                            append("01/02/2026   ")
                        },
                        fontSize = 11.sp
                    )
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("Expiry Date: ")
                            }
                            append("01/02/2029")
                        },
                        fontSize = 11.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Placeholder/Implementation for the barcode
                DynamicBarcode(regNo)

                Text(
                    text = regNo,
                    fontSize = 10.sp,
                    letterSpacing = 2.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))
            }

            // Decorative bottom maroon band
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
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
