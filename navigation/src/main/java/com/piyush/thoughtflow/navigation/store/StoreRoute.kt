package com.piyush.thoughtflow.navigation.store

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.piyush.thoughtflow.ui.components.CosmicBackground
import com.piyush.thoughtflow.ui.components.GlassCard
import com.piyush.thoughtflow.ui.components.GlassIconButton
import com.piyush.thoughtflow.ui.components.GradientButton
import com.piyush.thoughtflow.ui.theme.BrandGradient
import com.piyush.thoughtflow.ui.theme.PurplePrimary
import com.piyush.thoughtflow.ui.theme.TextPrimary
import com.piyush.thoughtflow.ui.theme.TextSecondary

@Composable
fun StoreRoute(onBack: () -> Unit) {
    CosmicBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 28.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                GlassIconButton(onClick = onBack, contentDescription = "Back") {
                    Icon(Icons.AutoMirrored.Outlined.ArrowBack, null, tint = TextPrimary)
                }
                Spacer(Modifier.weight(1f))
                Text("Store", color = TextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                Spacer(Modifier.weight(1f))
                Box(Modifier.size(46.dp))
            }

            Spacer(Modifier.height(20.dp))
            GlassCard(contentPadding = PaddingValues(22.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(BrandGradient),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(Icons.Outlined.AutoAwesome, null, tint = TextPrimary, modifier = Modifier.size(48.dp))
                }
                Spacer(Modifier.height(16.dp))
                Text("Premium Templates", color = TextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 22.sp)
                Text(
                    "Unlock polished structures for proposals, decks, and plans.",
                    color = TextSecondary,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 6.dp),
                )
                Spacer(Modifier.height(16.dp))
                GradientButton(text = "Unlock Premium", onClick = {})
            }

            Spacer(Modifier.height(22.dp))
            Text("Popular", color = TextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 17.sp)
            Spacer(Modifier.height(12.dp))
            listOf(
                "Pitch Deck Outline" to "₹199",
                "Business Case" to "₹249",
                "Research Brief" to "₹149",
            ).forEach { (title, price) ->
                GlassCard(
                    contentPadding = PaddingValues(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(title, color = TextPrimary, modifier = Modifier.weight(1f), fontWeight = FontWeight.Medium)
                        Text(price, color = PurplePrimary, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}
