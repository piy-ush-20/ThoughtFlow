package com.piyush.thoughtflow.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.piyush.thoughtflow.editor.EditorRoute
import com.piyush.thoughtflow.navigation.history.HistoryRoute
import com.piyush.thoughtflow.navigation.home.HomeRoute
import com.piyush.thoughtflow.navigation.processing.ProcessingRoute
import com.piyush.thoughtflow.navigation.settings.SettingsRoute

@Composable
fun ThoughtFlowNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = Routes.HOME,
        modifier = modifier,
    ) {
        composable(Routes.HOME) {
            HomeRoute(
                onOpenHistory = { navController.navigate(Routes.HISTORY) },
                onOpenSettings = { navController.navigate(Routes.SETTINGS) },
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
        composable(Routes.HISTORY) {
            HistoryRoute(
                onBack = { navController.popBackStack() },
                onOpenDocument = { id ->
                    navController.navigate(Routes.editor(id))
                },
            )
        }
        composable(Routes.SETTINGS) {
            SettingsRoute(onBack = { navController.popBackStack() })
        }
    }
}
