package com.piyush.thoughtflow.navigation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.piyush.thoughtflow.navigation.settings.SettingsViewModel
import com.piyush.thoughtflow.ui.components.CosmicBackground
import com.piyush.thoughtflow.ui.components.GlassCard
import com.piyush.thoughtflow.ui.theme.BrandGradient
import com.piyush.thoughtflow.ui.theme.DangerRed
import com.piyush.thoughtflow.ui.theme.PurplePrimary
import com.piyush.thoughtflow.ui.theme.SuccessGreen
import com.piyush.thoughtflow.ui.theme.TextMuted
import com.piyush.thoughtflow.ui.theme.TextPrimary
import com.piyush.thoughtflow.ui.theme.TextSecondary

@Composable
fun ProfileRoute(
    onOpenSettings: () -> Unit,
    onOpenStore: () -> Unit,
    contentBottomPadding: Int = 0,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val capabilities by viewModel.capabilities.collectAsStateWithLifecycle()
    ProfileScreen(
        onDeviceSummary = capabilities.summaryLabel(),
        onOpenSettings = onOpenSettings,
        onOpenStore = onOpenStore,
        onOpenAiPreferences = onOpenSettings,
        contentBottomPadding = contentBottomPadding,
    )
}

@Composable
fun ProfileScreen(
    onDeviceSummary: String,
    onOpenSettings: () -> Unit,
    onOpenStore: () -> Unit,
    onOpenAiPreferences: () -> Unit,
    contentBottomPadding: Int = 0,
) {
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
            Text("Profile", color = TextPrimary, fontSize = 28.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(20.dp))

            GlassCard(contentPadding = PaddingValues(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(BrandGradient),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text("TF", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    }
                    Spacer(Modifier.size(14.dp))
                    Column {
                        Text("ThoughtFlow User", color = TextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                        Box(
                            modifier = Modifier
                                .padding(top = 6.dp)
                                .clip(RoundedCornerShape(50))
                                .background(PurplePrimary.copy(alpha = 0.2f))
                                .padding(horizontal = 10.dp, vertical = 4.dp),
                        ) {
                            Text("Offline-first", color = PurplePrimary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }

            Spacer(Modifier.height(14.dp))
            GlassCard(contentPadding = PaddingValues(16.dp)) {
                Text("On-device AI", color = TextPrimary, fontWeight = FontWeight.Medium)
                Text(
                    onDeviceSummary,
                    color = SuccessGreen,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 6.dp),
                )
            }

            Spacer(Modifier.height(22.dp))
            ProfileRow(Icons.Outlined.Psychology, "AI Preferences", onOpenAiPreferences)
            ProfileRow(Icons.Outlined.Mic, "Voice Settings", onOpenSettings)
            ProfileRow(Icons.Outlined.Cloud, "Sync & Backup", onOpenSettings, badge = "Private Sync")
            ProfileRow(Icons.Outlined.Storefront, "Premium Store", onOpenStore)
            ProfileRow(Icons.Outlined.Settings, "Settings", onOpenSettings)
            ProfileRow(Icons.Outlined.HelpOutline, "Help", onOpenSettings)
            ProfileRow(Icons.Outlined.Info, "About", onOpenSettings)

            Spacer(Modifier.height(20.dp))
            Text(
                "Sign Out",
                color = DangerRed,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { }
                    .padding(vertical = 12.dp),
            )
        }
    }
}

@Composable
private fun ProfileRow(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit,
    badge: String? = null,
) {
    GlassCard(
        onClick = onClick,
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 14.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = PurplePrimary, modifier = Modifier.size(22.dp))
            Spacer(Modifier.size(12.dp))
            Text(title, color = TextPrimary, modifier = Modifier.weight(1f), fontWeight = FontWeight.Medium)
            if (badge != null) {
                Text(badge, color = TextMuted, fontSize = 11.sp, modifier = Modifier.padding(end = 8.dp))
            }
            Icon(Icons.AutoMirrored.Outlined.KeyboardArrowRight, null, tint = TextMuted)
        }
    }
}
