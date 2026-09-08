package com.piyush.thoughtflow.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.piyush.thoughtflow.domain.model.ThemeMode
import com.piyush.thoughtflow.navigation.ThoughtFlowNavHost
import com.piyush.thoughtflow.ui.theme.ThoughtFlowTheme

@Composable
fun ThoughtFlowRoot(
    viewModel: MainViewModel = hiltViewModel(),
) {
    val prefs by viewModel.preferences.collectAsStateWithLifecycle()
    val darkTheme = when (prefs.themeMode) {
        ThemeMode.Light -> false
        ThemeMode.Dark -> true
        ThemeMode.System -> isSystemInDarkTheme()
    }
    ThoughtFlowTheme(darkTheme = darkTheme) {
        ThoughtFlowNavHost()
    }
}
