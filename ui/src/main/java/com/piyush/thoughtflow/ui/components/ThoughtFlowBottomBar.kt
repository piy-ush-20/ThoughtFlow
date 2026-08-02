package com.piyush.thoughtflow.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.piyush.thoughtflow.ui.theme.BlueElectric
import com.piyush.thoughtflow.ui.theme.CosmicBorder
import com.piyush.thoughtflow.ui.theme.CosmicSurface
import com.piyush.thoughtflow.ui.theme.PurpleDeep
import com.piyush.thoughtflow.ui.theme.PurplePrimary
import com.piyush.thoughtflow.ui.theme.TextMuted

enum class ThoughtFlowTab {
    Home,
    Documents,
    Create,
    Templates,
    Profile,
}

@Composable
fun ThoughtFlowBottomBar(
    selected: ThoughtFlowTab,
    onSelect: (ThoughtFlowTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .clip(shape)
            .background(CosmicSurface.copy(alpha = 0.96f))
            .border(1.dp, CosmicBorder, shape)
            .padding(horizontal = 10.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        TabIcon(
            icon = Icons.Outlined.Home,
            selected = selected == ThoughtFlowTab.Home,
            onClick = { onSelect(ThoughtFlowTab.Home) },
            contentDescription = "Home",
        )
        TabIcon(
            icon = Icons.Outlined.Description,
            selected = selected == ThoughtFlowTab.Documents,
            onClick = { onSelect(ThoughtFlowTab.Documents) },
            contentDescription = "Documents",
        )
        CreateFab(onClick = { onSelect(ThoughtFlowTab.Create) })
        TabIcon(
            icon = Icons.Outlined.GridView,
            selected = selected == ThoughtFlowTab.Templates,
            onClick = { onSelect(ThoughtFlowTab.Templates) },
            contentDescription = "Templates",
        )
        TabIcon(
            icon = Icons.Outlined.PersonOutline,
            selected = selected == ThoughtFlowTab.Profile,
            onClick = { onSelect(ThoughtFlowTab.Profile) },
            contentDescription = "Profile",
        )
    }
}

@Composable
private fun RowScope.TabIcon(
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
    contentDescription: String,
) {
    Box(
        modifier = Modifier
            .weight(1f)
            .height(48.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = if (selected) PurplePrimary else TextMuted,
            modifier = Modifier.size(24.dp),
        )
    }
}

@Composable
private fun CreateFab(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .size(56.dp)
            .shadow(18.dp, CircleShape, ambientColor = PurplePrimary.copy(alpha = 0.55f), spotColor = PurplePrimary)
            .clip(CircleShape)
            .background(Brush.linearGradient(listOf(PurpleDeep, PurplePrimary, BlueElectric)))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(Icons.Outlined.Add, contentDescription = "Create", tint = Color.White, modifier = Modifier.size(28.dp))
    }
}
