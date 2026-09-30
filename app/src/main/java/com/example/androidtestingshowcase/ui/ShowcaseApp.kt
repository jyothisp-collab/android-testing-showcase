package com.example.androidtestingshowcase.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.androidtestingshowcase.features.home.HomeRoute
import com.example.androidtestingshowcase.features.items.ItemsRoute

@Composable
fun ShowcaseApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.Home) {
        composable(Routes.Home) {
            HomeRoute(onOpenItems = { navController.navigate(Routes.Items) })
        }
        composable(Routes.Items) {
            ItemsRoute(
                onBack = { navController.popBackStack() },
                onOpenDetail = { id -> navController.navigate(Routes.detail(id)) },
            )
        }
        composable(
            route = Routes.Detail,
            arguments = listOf(navArgument("itemId") { type = NavType.StringType }),
        ) {
            com.example.androidtestingshowcase.features.items.ItemDetailRoute(
                onBack = { navController.popBackStack() },
            )
        }
    }
}

object Routes {
    const val Home = "home"
    const val Items = "items"
    const val Detail = "items/{itemId}"

    fun detail(itemId: String) = "items/$itemId"
}
