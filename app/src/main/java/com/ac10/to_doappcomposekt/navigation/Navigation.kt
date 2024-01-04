package com.ac10.to_doappcomposekt.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.ac10.to_doappcomposekt.navigation.destinations.listComposable
import com.ac10.to_doappcomposekt.navigation.destinations.taskComposable
import com.ac10.to_doappcomposekt.ui.viewmodel.SharedViewModel
import com.ac10.to_doappcomposekt.util.Constants.LIST_SCREEN

@Composable
fun SetupNavigation(
    navHostController: NavHostController,
    sharedViewModel: SharedViewModel
) {
    val screen = remember(navHostController) {
        Screens(navHostController = navHostController)
    }

    NavHost(
        navController = navHostController,
        startDestination = LIST_SCREEN
    ) {

        listComposable(
            navigationToTaskScreen = screen.list,
            sharedViewModel = sharedViewModel
        )

        taskComposable(
            navigationToListScreen = screen.task,
            sharedViewModel = sharedViewModel
        )

    }

}