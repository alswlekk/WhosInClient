package org.whosin.client.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import org.whosin.client.presentation.auth.clubcode.ClubCodeInputScreen
import org.whosin.client.presentation.auth.login.EmailVerificationScreen
import org.whosin.client.presentation.auth.login.LoginScreen
import org.whosin.client.presentation.auth.login.NicknameInputScreen
import org.whosin.client.presentation.auth.login.FindPasswordScreen
import org.whosin.client.presentation.auth.login.PasswordInputScreen
import org.whosin.client.presentation.auth.login.SignupScreen
import org.whosin.client.presentation.auth.login.SplashScreen
import org.whosin.client.presentation.home.HomeScreen
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
            startDestination = Route.Splash,
        ) {
            composable<Route.Splash> {
                SplashScreen(
                    modifier = modifier,
                    onNavigateToLogin = {
                        navController.navigate(Route.Login) {
                            popUpTo(Route.Splash) { inclusive = true }
                        }
                    }
                )
            }
            
            composable<Route.Login> {
                LoginScreen(
                    modifier = modifier,
                    onNavigateToHome = {
                        navController.navigate(Route.Home)
                    },
                    onNavigateToFindPassword = {
                        navController.navigate(Route.FindPassword)
                    },
                    onNavigateToSignup = {
                        navController.navigate(Route.Signup)
                    }
                )
            }
            
            composable<Route.FindPassword> {
                FindPasswordScreen(
                    modifier = modifier,
                    onNavigateBack = { navController.navigateUp() },
                    onPasswordResetComplete = {
                        navController.navigate(Route.Login) {
                            popUpTo(Route.FindPassword) { inclusive = true }
                        }
                    }
                )
            }
            
            composable<Route.Signup> {
                SignupScreen(
                    modifier = modifier,
                    onNavigateBack = { navController.navigateUp() },
                    onNavigateToEmailVerification = { email ->
                        navController.navigate(Route.EmailVerification)
                    }
                )
            }
            
            composable<Route.EmailVerification> { backStackEntry ->
                
                EmailVerificationScreen(
                    modifier = modifier,
                    onNavigateBack = { navController.navigateUp() },
                    onVerificationComplete = {
                        navController.navigate(Route.PasswordInput)
                    }
                )
            }
            
            composable<Route.PasswordInput> {
                PasswordInputScreen(
                    modifier = modifier,
                    onNavigateBack = { navController.navigateUp() },
                    onPasswordComplete = { password, confirmPassword ->
                        navController.navigate(Route.NicknameInput)
                    }
                )
            }
            
            composable<Route.NicknameInput> {
                NicknameInputScreen(
                    modifier = modifier,
                    onNavigateBack = { navController.navigateUp() },
                    onNavigateToClubCode = {
                        navController.navigate(Route.ClubCodeInput(returnToMyPage = false))
                    }
                )
            }
            
            composable<Route.ClubCodeInput> { backStackEntry ->
                val route = backStackEntry.toRoute<Route.ClubCodeInput>()
                ClubCodeInputScreen(
                    modifier = modifier,
                    onNavigateBack = { navController.navigateUp() },
                    onNavigateToHome = {
                        if (route.returnToMyPage) {
                            // MyPage에서 온 경우 단순히 뒤로가기
                            navController.navigateUp()
                        } else {
                            // 회원가입 플로우에서 온 경우 Home으로 이동
                            navController.navigate(Route.Home) {
                                popUpTo(Route.AuthGraph) { inclusive = true }
                            }
                        }
                    }
                )
            }
        }

        /* 홈 화면 */
        composable<Route.Home> {
            HomeScreen(
                modifier = modifier,
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
                    navController.navigate(Route.ClubCodeInput(returnToMyPage = true))
                }
            )
        }
    }
}
