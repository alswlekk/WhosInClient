package org.whosin.client.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.whosin.client.presentation.auth.LoginScreen
import org.whosin.client.presentation.home.HomeScreen
import org.whosin.client.presentation.mypage.ModifyMyInfoScreen
import org.whosin.client.presentation.mypage.MyPageScreen

@Composable
fun WhosInNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Route.AuthGraph
    ) {
        /* 인증,인가 화면들을 위한 별도의 graph */
        navigation<Route.AuthGraph>(
            startDestination = Route.Login,
        ) {
            composable<Route.Login> {
                LoginScreen(
                    modifier = modifier,
                    onNavigateToHome = {
                        navController.navigate(Route.Home)
                    }
                )
            }
        }

        /* 홈 화면 */
        composable<Route.Home> {
            HomeScreen(
                modifier = modifier,
                onNavigateBack = { navController.navigateUp() },
                onNavigateToMyPage = {
                    navController.navigate(Route.MyPage)
                }
            )
        }

        /* 마이페이지 화면 */
        composable<Route.MyPage> {
            MyPageScreen(
                modifier = modifier,
                onNavigateBack = { navController.navigateUp() },
                onNavigateToAddClub = {

                },
                onNavigateToModifyMyInfo = {
                    navController.navigate(Route.UpdateMyInfo)
                }
            )
        }

        composable<Route.UpdateMyInfo> {
            ModifyMyInfoScreen(
                modifier = modifier,
                onNavigateBack = { navController.navigateUp() },
                onNavigateToMyPage = {
                    navController.navigate(Route.MyPage) {
                        popUpTo(Route.UpdateMyInfo) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
