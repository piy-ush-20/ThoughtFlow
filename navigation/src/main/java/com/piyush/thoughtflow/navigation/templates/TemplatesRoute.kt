package com.piyush.thoughtflow.navigation.templates

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.piyush.thoughtflow.ui.components.CosmicBackground
import com.piyush.thoughtflow.ui.components.FilterChip
import com.piyush.thoughtflow.ui.components.GlassCard
import com.piyush.thoughtflow.ui.components.GradientButton
import com.piyush.thoughtflow.ui.components.SectionHeader
import com.piyush.thoughtflow.ui.theme.BrandGradient
import com.piyush.thoughtflow.ui.theme.PurplePrimary
import com.piyush.thoughtflow.ui.theme.TextMuted
import com.piyush.thoughtflow.ui.theme.TextPrimary
import com.piyush.thoughtflow.ui.theme.TextSecondary

data class TemplateItem(
    val id: String,
    val title: String,
    val category: String,
    val premium: Boolean,
    val priceLabel: String? = null,
)

private val demoTemplates = listOf(
    TemplateItem("1", "Meeting Notes", "Documents", false),
    TemplateItem("2", "Project Proposal", "Documents", false),
    TemplateItem("3", "Weekly Plan", "Planning", false),
    TemplateItem("4", "Pitch Deck Outline", "Presentations", true, "₹199"),
    TemplateItem("5", "Business Case", "Business", true, "₹249"),
    TemplateItem("6", "Research Brief", "Documents", true, "₹149"),
)

@Composable
fun TemplatesRoute(
    onUseTemplate: () -> Unit,
    onOpenStore: () -> Unit,
    contentBottomPadding: Int = 0,
) {
    var filter by remember { mutableStateOf("All") }
    val filters = listOf("All", "Documents", "Presentations", "Planning", "Business")
    val free = demoTemplates.filter { !it.premium && (filter == "All" || it.category == filter) }
    val premium = demoTemplates.filter { it.premium && (filter == "All" || it.category == filter) }
    val freeRows = ((free.size + 1) / 2).coerceAtLeast(1)
    // Card ≈ 14+72+10+title+category+14 ≈ 140dp; include row gaps.
    val freeGridHeight = (freeRows * 140 + (freeRows - 1) * 12).dp

    CosmicBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = contentBottomPadding.dp + 16.dp),
        ) {
            Spacer(Modifier.height(16.dp))
            Text("Templates", color = TextPrimary, fontSize = 28.sp, fontWeight = FontWeight.SemiBold)
            Text(
                "Smart structures for faster drafting",
                color = TextSecondary,
                fontSize = 13.sp,
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp),
            )
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(filters.size) { i ->
                    FilterChip(filters[i], filter == filters[i]) { filter = filters[i] }
                }
            }
            Spacer(Modifier.height(20.dp))
            SectionHeader(title = "Free Templates")
            Spacer(Modifier.height(12.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.height(freeGridHeight),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                userScrollEnabled = false,
            ) {
                items(free) { item ->
                    TemplateCard(item, onClick = onUseTemplate)
                }
            }
            Spacer(Modifier.height(18.dp))
            SectionHeader(title = "Premium Templates", actionLabel = "Store", onAction = onOpenStore)
            Spacer(Modifier.height(12.dp))
            premium.take(2).forEach { item ->
                GlassCard(
                    onClick = onOpenStore,
                    contentPadding = PaddingValues(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(BrandGradient),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(Icons.Outlined.Lock, null, tint = TextPrimary, modifier = Modifier.size(18.dp))
                        }
                        Spacer(Modifier.size(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text(item.title, color = TextPrimary, fontWeight = FontWeight.Medium)
                            Text("Premium · ${item.priceLabel}", color = PurplePrimary, fontSize = 12.sp)
                        }
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            GradientButton(text = "Browse Store", onClick = onOpenStore)
        }
    }
}

@Composable
private fun TemplateCard(item: TemplateItem, onClick: () -> Unit) {
    GlassCard(onClick = onClick, contentPadding = PaddingValues(14.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(PurplePrimary.copy(alpha = 0.16f)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(Icons.Outlined.Description, null, tint = PurplePrimary)
        }
        Spacer(Modifier.height(10.dp))
        Text(item.title, color = TextPrimary, fontWeight = FontWeight.Medium, fontSize = 13.sp, maxLines = 2)
        Text(item.category, color = TextMuted, fontSize = 11.sp)
    }
}
