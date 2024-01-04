package com.ac10.to_doappcomposekt.navigation.destinations

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ac10.to_doappcomposekt.ui.screens.list.ListScreen
import com.ac10.to_doappcomposekt.ui.viewmodel.SharedViewModel
import com.ac10.to_doappcomposekt.util.Action
import com.ac10.to_doappcomposekt.util.Constants.LIST_ARGUMENT_KEY
import com.ac10.to_doappcomposekt.util.Constants.LIST_SCREEN
import com.ac10.to_doappcomposekt.util.toAction


@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
fun NavGraphBuilder.listComposable(
    navigationToTaskScreen: (taskId: Int) -> Unit,
    sharedViewModel: SharedViewModel
) {
    composable(
        route = LIST_SCREEN,
        arguments = listOf(navArgument(LIST_ARGUMENT_KEY) {
            type = NavType.StringType
        })
    ) { navBackStackEntry ->
        val action = navBackStackEntry.arguments?.getString(LIST_ARGUMENT_KEY).toAction()
        var myAction by rememberSaveable { mutableStateOf(Action.NO_ACTION) }

        LaunchedEffect(key1 = myAction) {
            if (action != myAction) {
                myAction = action
                sharedViewModel.updateAction(
                    newAction = action
                )
            }
        }

        val databaseAction = sharedViewModel.action

        ListScreen(
            action = databaseAction,
            sharedViewModel = sharedViewModel,
            navigateToTaskScreen = navigationToTaskScreen
        )

    }


}