package com.piyush.thoughtflow.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.piyush.thoughtflow.editor.EditorRoute
import com.piyush.thoughtflow.navigation.create.CreateRoute
import com.piyush.thoughtflow.navigation.documents.DocumentsRoute
import com.piyush.thoughtflow.navigation.home.HomeRoute
import com.piyush.thoughtflow.navigation.processing.ProcessingRoute
import com.piyush.thoughtflow.navigation.profile.ProfileRoute
import com.piyush.thoughtflow.navigation.settings.SettingsRoute
import com.piyush.thoughtflow.navigation.store.StoreRoute
import com.piyush.thoughtflow.navigation.templates.TemplatesRoute
import com.piyush.thoughtflow.navigation.voice.VoiceRoute
import com.piyush.thoughtflow.ui.components.ThoughtFlowBottomBar
import com.piyush.thoughtflow.ui.components.ThoughtFlowTab

@Composable
fun ThoughtFlowNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val backStack by navController.currentBackStackEntryAsState()
    val route = backStack?.destination?.route
    val showBottomBar = route in setOf(
        Routes.HOME,
        Routes.DOCUMENTS,
        Routes.CREATE,
        Routes.TEMPLATES,
        Routes.PROFILE,
    )
    val selectedTab = when (route) {
        Routes.DOCUMENTS -> ThoughtFlowTab.Documents
        Routes.CREATE -> ThoughtFlowTab.Create
        Routes.TEMPLATES -> ThoughtFlowTab.Templates
        Routes.PROFILE -> ThoughtFlowTab.Profile
        else -> ThoughtFlowTab.Home
    }

    Box(modifier = modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = Routes.HOME,
            modifier = Modifier.fillMaxSize(),
        ) {
            composable(Routes.HOME) {
                HomeRoute(
                    onOpenDocuments = { navController.navigateTab(Routes.DOCUMENTS) },
                    onOpenVoice = { navController.navigate(Routes.VOICE) },
                    onOpenCreate = { navController.navigateTab(Routes.CREATE) },
                    onOpenTemplates = { navController.navigateTab(Routes.TEMPLATES) },
                    onOpenDocument = { id -> navController.navigate(Routes.editor(id)) },
                    contentBottomPadding = if (showBottomBar) 88 else 0,
                )
            }
            composable(Routes.DOCUMENTS) {
                DocumentsRoute(
                    onOpenDocument = { id -> navController.navigate(Routes.editor(id)) },
                    contentBottomPadding = 88,
                )
            }
            composable(Routes.CREATE) {
                CreateRoute(
                    onVoiceInput = { navController.navigate(Routes.VOICE) },
                    onBlankDocument = { id ->
                        navController.navigate(Routes.editor(id)) {
                            launchSingleTop = true
                        }
                    },
                    onOpenTemplates = { navController.navigateTab(Routes.TEMPLATES) },
                    contentBottomPadding = 88,
                )
            }
            composable(Routes.TEMPLATES) {
                TemplatesRoute(
                    onUseTemplate = { navController.navigate(Routes.VOICE) },
                    onOpenStore = { navController.navigate(Routes.STORE) },
                    contentBottomPadding = 88,
                )
            }
            composable(Routes.PROFILE) {
                ProfileRoute(
                    onOpenSettings = { navController.navigate(Routes.SETTINGS) },
                    onOpenStore = { navController.navigate(Routes.STORE) },
                    contentBottomPadding = 88,
                )
            }
            composable(Routes.VOICE) {
                VoiceRoute(
                    onBack = { navController.popBackStack() },
                    onNavigateProcessing = {
                        navController.navigate(Routes.PROCESSING) {
                            launchSingleTop = true
                        }
                    },
                    onNavigateEditor = { id ->
                        navController.navigate(Routes.editor(id)) {
                            popUpTo(Routes.HOME)
                            launchSingleTop = true
                        }
                    },
                )
            }
            composable(Routes.PROCESSING) {
                ProcessingRoute(
                    onNavigateEditor = { id ->
                        navController.navigate(Routes.editor(id)) {
                            popUpTo(Routes.HOME)
                            launchSingleTop = true
                        }
                    },
                    onBackHome = {
                        navController.popBackStack(Routes.HOME, inclusive = false)
                    },
                )
            }
            composable(
                route = Routes.EDITOR,
                arguments = listOf(navArgument("documentId") { type = NavType.StringType }),
            ) {
                EditorRoute(
                    onBack = {
                        navController.popBackStack(Routes.HOME, inclusive = false)
                    },
                )
            }
            composable(Routes.STORE) {
                StoreRoute(onBack = { navController.popBackStack() })
            }
            composable(Routes.SETTINGS) {
                SettingsRoute(onBack = { navController.popBackStack() })
            }
        }

        if (showBottomBar) {
            ThoughtFlowBottomBar(
                selected = selectedTab,
                onSelect = { tab ->
                    val target = when (tab) {
                        ThoughtFlowTab.Home -> Routes.HOME
                        ThoughtFlowTab.Documents -> Routes.DOCUMENTS
                        ThoughtFlowTab.Create -> Routes.CREATE
                        ThoughtFlowTab.Templates -> Routes.TEMPLATES
                        ThoughtFlowTab.Profile -> Routes.PROFILE
                    }
                    navController.navigateTab(target)
                },
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }
    }
}

private fun NavHostController.navigateTab(route: String) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
