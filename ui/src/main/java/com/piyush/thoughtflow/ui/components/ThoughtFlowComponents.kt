package com.piyush.thoughtflow.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.piyush.thoughtflow.ui.theme.BrandGradient
import com.piyush.thoughtflow.ui.theme.CosmicBorder
import com.piyush.thoughtflow.ui.theme.CosmicCard
import com.piyush.thoughtflow.ui.theme.CosmicSurfaceElevated
import com.piyush.thoughtflow.ui.theme.PurplePrimary
import com.piyush.thoughtflow.ui.theme.TextMuted
import com.piyush.thoughtflow.ui.theme.TextPrimary
import com.piyush.thoughtflow.ui.theme.TextSecondary

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    content: @Composable () -> Unit,
) {
    val shape = RoundedCornerShape(22.dp)
    Box(
        modifier = modifier
            .clip(shape)
            .background(CosmicCard.copy(alpha = 0.92f))
            .border(1.dp, CosmicBorder, shape)
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onClick,
                    )
                } else {
                    Modifier
                },
            )
            .padding(contentPadding),
    ) {
        content()
    }
}

@Composable
fun GradientButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: ImageVector? = null,
) {
    val shape = RoundedCornerShape(18.dp)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp)
            .shadow(
                elevation = if (enabled) 16.dp else 0.dp,
                shape = shape,
                ambientColor = PurplePrimary.copy(alpha = 0.45f),
                spotColor = PurplePrimary.copy(alpha = 0.55f),
            )
            .clip(shape)
            .background(if (enabled) BrandGradient else Brush.horizontalGradient(listOf(Color(0xFF3A3A48), Color(0xFF3A3A48))))
            .clickable(enabled = enabled, onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        if (leadingIcon != null) {
            Icon(leadingIcon, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
            Box(Modifier.width(8.dp))
        }
        Text(
            text = text,
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp,
        )
    }
}

@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            color = TextPrimary,
            fontWeight = FontWeight.SemiBold,
            fontSize = 17.sp,
            modifier = Modifier.weight(1f),
        )
        if (actionLabel != null && onAction != null) {
            Text(
                text = actionLabel,
                color = PurplePrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable(onClick = onAction),
            )
        }
    }
}

@Composable
fun FilterChip(
    label: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(50)
    Box(
        modifier = modifier
            .clip(shape)
            .background(if (selected) PurplePrimary else CosmicSurfaceElevated)
            .border(1.dp, if (selected) PurplePrimary else CosmicBorder, shape)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp),
    ) {
        Text(
            text = label,
            color = if (selected) Color.White else TextSecondary,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
        )
    }
}

@Composable
fun QuickActionTile(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    accent: Color = PurplePrimary,
) {
    GlassCard(
        modifier = modifier,
        onClick = onClick,
        contentPadding = PaddingValues(vertical = 18.dp, horizontal = 12.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(accent.copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(icon, contentDescription = null, tint = accent, modifier = Modifier.size(22.dp))
            }
            Text(title, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun SearchField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Search",
    trailing: (@Composable () -> Unit)? = null,
) {
    val shape = RoundedCornerShape(50)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(shape)
            .background(CosmicSurfaceElevated)
            .border(1.dp, CosmicBorder, shape)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = TextStyle(color = TextPrimary, fontSize = 14.sp),
            modifier = Modifier.weight(1f),
            decorationBox = { inner ->
                if (value.isEmpty()) {
                    Text(placeholder, color = TextMuted, fontSize = 14.sp)
                }
                inner()
            },
        )
        trailing?.invoke()
    }
}
