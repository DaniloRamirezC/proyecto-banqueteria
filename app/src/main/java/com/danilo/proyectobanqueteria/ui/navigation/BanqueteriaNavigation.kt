package com.danilo.proyectobanqueteria.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.danilo.proyectobanqueteria.ui.screen.BanqueteriaScreen
import com.danilo.proyectobanqueteria.ui.screen.CatalogoScreen
import com.danilo.proyectobanqueteria.ui.screen.LoginScreen
import com.danilo.proyectobanqueteria.ui.screen.RegisterScreen

object Routes{
    const val BANQUETER = "Banqueteria"
    const val REGISTER_USER = "Registro usuario"
    const val LOGIN = "Login"
    const val CATALOG = "Catálogo"
    const val REGISTER_EVENT = "Registro evento"
}

@Composable
fun BanqueteriaNavigation(){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Routes.BANQUETER,
        modifier = Modifier.fillMaxSize(),
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(400)
            )+ fadeIn(animationSpec = tween(400))
        },

        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(400)
            )+ fadeOut(animationSpec = tween(400))
        },

        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(400)
            )+ fadeIn(animationSpec = tween(400))
        },

        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(400)
            )+fadeOut(animationSpec = tween(400))
        }
    ){
        composable(Routes.BANQUETER){
            BanqueteriaScreen(
                onNavigateToRegister = {
                    navController.navigate(Routes.REGISTER_USER){
                        popUpTo(Routes.REGISTER_USER) {inclusive = true}
                    }
                }
            )
        }
        composable(Routes.REGISTER_USER){
            RegisterScreen(
                onNavigateToLogin = {
                    navController.navigate(Routes.LOGIN)
                }
            )

        }
        composable(Routes.LOGIN) {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate(Routes.REGISTER_USER)
                }
            )

        }
        composable(Routes.CATALOG){
            CatalogoScreen(
                onNavigateToCotizar = {
                    navController.navigate(Routes.REGISTER_EVENT)
                }
            )
        }
        composable(Routes.REGISTER_EVENT){

        }
    }
}