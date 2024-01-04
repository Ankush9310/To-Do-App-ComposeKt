package com.ac10.to_doappcomposekt.navigation

import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.ac10.to_doappcomposekt.util.Action
import com.ac10.to_doappcomposekt.util.Constants.LIST_SCREEN

class Screens(navHostController: NavHostController) {

    val list: (Int) -> Unit = { taskId ->
        navHostController.navigate(
            route = "task/$taskId"
        )
    }

    val task: (Action) -> Unit = { action ->
        navHostController.navigate(
            route = "list/${action}"
        ) {
            popUpTo(LIST_SCREEN) {
                inclusive = true
            }
        }
    }

}