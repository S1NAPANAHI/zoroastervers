package com.example.zoroastervers.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun AnimatedSplashScreen(
    onAnimationComplete: () -> Unit = {}
) {
    var animationPhase by remember { mutableStateOf(0) }
    
    // Animation sequences
    LaunchedEffect(Unit) {
        // Phase 1: Logo appearance
        animationPhase = 1
        delay(1500)
        
        // Phase 2: Text appearance
        animationPhase = 2
        delay(1000)
        
        // Phase 3: Complete
        animationPhase = 3
        delay(500)
        
        onAnimationComplete()
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        MaterialTheme.colorScheme.surface,
                        MaterialTheme.colorScheme.background
                    ),
                    radius = 1200f
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Animated Logo
            AnimatedLogo(
                isVisible = animationPhase >= 1,
                isComplete = animationPhase >= 3
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // App Title with Animation
            AnimatedTitle(
                isVisible = animationPhase >= 2
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Subtitle
            AnimatedSubtitle(
                isVisible = animationPhase >= 2
            )
            
            Spacer(modifier = Modifier.height(48.dp))
            
            // Loading Animation
            if (animationPhase < 3) {
                LoadingAnimation()
            }
        }
    }
}

@Composable
fun AnimatedLogo(
    isVisible: Boolean,
    isComplete: Boolean
) {
    val scale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "logo_scale"
    )
    
    val rotation by animateFloatAsState(
        targetValue = if (isComplete) 360f else 0f,
        animationSpec = tween(
            durationMillis = 1000,
            easing = FastOutSlowInEasing
        ),
        label = "logo_rotation"
    )
    
    val infiniteRotation by rememberInfiniteTransition(label = "infinite_rotation").animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "infinite_rotation_value"
    )
    
    Box(
        modifier = Modifier
            .size(120.dp)
            .scale(scale)
            .rotate(if (isComplete) rotation else infiniteRotation * 0.1f),
        contentAlignment = Alignment.Center
    ) {
        // Outer ring
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            val center = Offset(size.width / 2, size.height / 2)
            val radius = size.minDimension / 2 - 8.dp.toPx()
            
            // Draw animated ring
            drawCircle(
                color = Color(0xFF5E81AC),
                radius = radius,
                center = center,
                style = Stroke(
                    width = 3.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(
                        floatArrayOf(10f, 5f), 
                        phase = infiniteRotation
                    )
                )
            )
            
            // Draw inner sacred symbols
            drawSacredSymbol(
                center = center,
                radius = radius * 0.6f,
                rotation = infiniteRotation * 0.2f
            )
        }
        
        // Center icon
        Card(
            modifier = Modifier.size(80.dp),
            shape = CircleShape,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Z",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

fun DrawScope.drawSacredSymbol(
    center: Offset,
    radius: Float,
    rotation: Float
) {
    val path = Path()
    val points = 8
    val angleStep = 2 * PI / points
    
    for (i in 0 until points) {
        val angle = i * angleStep + rotation * PI / 180
        val x = center.x + radius * cos(angle).toFloat()
        val y = center.y + radius * sin(angle).toFloat()
        
        if (i == 0) {
            path.moveTo(x, y)
        } else {
            path.lineTo(x, y)
        }
    }
    path.close()
    
    drawPath(
        path = path,
        color = Color(0xFF88C0D0).copy(alpha = 0.3f),
        style = Stroke(width = 1.dp.toPx())
    )
}

@Composable
fun AnimatedTitle(
    isVisible: Boolean
) {
    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(
            durationMillis = 800,
            delayMillis = 200,
            easing = EaseOutCubic
        ),
        label = "title_alpha"
    )
    
    val slideY by animateFloatAsState(
        targetValue = if (isVisible) 0f else 50f,
        animationSpec = tween(
            durationMillis = 800,
            delayMillis = 200,
            easing = EaseOutCubic
        ),
        label = "title_slide"
    )
    
    Text(
        text = "Zoroastervers",
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .alpha(alpha)
            .offset(y = slideY.dp)
    )
}

@Composable
fun AnimatedSubtitle(
    isVisible: Boolean
) {
    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(
            durationMillis = 800,
            delayMillis = 400,
            easing = EaseOutCubic
        ),
        label = "subtitle_alpha"
    )
    
    val slideY by animateFloatAsState(
        targetValue = if (isVisible) 0f else 30f,
        animationSpec = tween(
            durationMillis = 800,
            delayMillis = 400,
            easing = EaseOutCubic
        ),
        label = "subtitle_slide"
    )
    
    Text(
        text = "Interactive E-book Reader",
        fontSize = 16.sp,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
        textAlign = TextAlign.Center,
        modifier = Modifier
            .alpha(alpha)
            .offset(y = slideY.dp)
    )
}

@Composable
fun LoadingAnimation() {
    val infiniteTransition = rememberInfiniteTransition(label = "loading")
    
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1200,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "loading_rotation"
    )
    
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = EaseInOutCubic
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "loading_scale"
    )
    
    Canvas(
        modifier = Modifier
            .size(32.dp)
            .rotate(rotation)
            .scale(scale)
    ) {
        val strokeWidth = 3.dp.toPx()
        val radius = size.minDimension / 2 - strokeWidth / 2
        
        drawArc(
            color = Color(0xFF5E81AC),
            startAngle = 0f,
            sweepAngle = 270f,
            useCenter = false,
            style = Stroke(
                width = strokeWidth,
                cap = StrokeCap.Round
            ),
            topLeft = Offset(
                strokeWidth / 2,
                strokeWidth / 2
            ),
            size = androidx.compose.ui.geometry.Size(
                radius * 2,
                radius * 2
            )
        )
    }
}