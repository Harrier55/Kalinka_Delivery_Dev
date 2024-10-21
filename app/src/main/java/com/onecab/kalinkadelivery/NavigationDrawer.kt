package com.onecab.kalinkadelivery

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.onecab.core.theme.KalinkaDeliveryTheme
import com.onecab.domain.repository.RegisterRepository
import com.onecab.features.navigation_route.Screens
import com.onecab.navigation_api.NavigationService
import kotlinx.coroutines.launch

/*
    У этого NavigationDrawer есть логика. Он должен показывать разные пункты меню
    для авторизованного и не авторизованного пользователя.

    Есть функция выхода из профиля
 */

@Composable
fun NavigationDrawer(
    navHostController: NavHostController,
    drawerState: DrawerState,
    navigationService: NavigationService,
    registerRepository: RegisterRepository,
) {
    val selectedItem = remember { mutableStateOf(listMenuItems[0]) }
    val coroutineScope = rememberCoroutineScope()
    val isRegistered by registerRepository.userIsRegistered.collectAsState()
    val showExitDialog = remember { mutableStateOf(false) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        scrimColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
        gesturesEnabled = true,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.requiredWidth(250.dp),
                drawerShape = RoundedCornerShape(0.dp),
                drawerContainerColor = MaterialTheme.colorScheme.background
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 30.dp),
                    verticalArrangement = Arrangement.spacedBy(43.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    listMenuItems.forEach { item ->
                        val itemIsSelected = selectedItem.value == item

                        if (isRegistered == item.isRegister) {
                            ItemMenu(
                                item = item,
                                itemIsSelected = itemIsSelected,
                                onClickItem = {
                                    coroutineScope.launch {
                                        selectedItem.value = item
                                        navigationService.closeNavigationDrawer()
                                        if (item.route.isNotEmpty()) {
                                            navigationService.navigateWithoutArgument(item.route)
                                        } else {
                                            showExitDialog.value = true
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        },
    ) {
        MyNavHost(navHostController = navHostController)

        // При выборе пункта меню "Выйти из профиля" - показываем диалог, а дальше по логике
        ShowExitDialog(
            showDialog = showExitDialog.value,
            clickYes = {
                // удаляем данные пользователя
                registerRepository.unRegisterUser()
                // закрываем диалог
                showExitDialog.value = false
                navigationService.navigateWithoutArgument(route = Screens.AuthorizationServerScreen.route)
            },
            onDismiss = { showExitDialog.value = false }
        )
    }
}

@Composable
fun ShowExitDialog(
    showDialog: Boolean,
    clickYes: () -> Unit,
    onDismiss: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { onDismiss.invoke() },
            confirmButton = {
                Button(
                    onClick = { clickYes.invoke() }
                ) {
                    Text(text = "Да")
                }
            },
            dismissButton = {
                Button(
                    onClick = { onDismiss.invoke() }
                ) {
                    Text(text = "Oтмена")
                }
            },
            title = { Text(text = "Выход их профиля") },
            text = {
                Text(
                    text = "Вы действительно хотите" +
                            " выйти из профиля и удалить все регистрационные данные ?"
                )
            },
            containerColor = Color.White
        )
    }
}

@Preview
@Composable
private fun Preview() {
    KalinkaDeliveryTheme {
        ShowExitDialog(showDialog = true, onDismiss = {}, clickYes = {})
    }
}

@Composable
private fun ItemMenu(
    item: MenuItem,
    itemIsSelected: Boolean,
    onClickItem: () -> Unit
) {
    Row(
        modifier = Modifier
            .clickable {
                onClickItem.invoke()
            }
            .width(230.dp)
            .height(40.dp)
            .shadow(if (itemIsSelected) 2.dp else 0.dp)
            .background(
                color = Color.White,
                shape = MaterialTheme.shapes.extraSmall
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(5.dp))
        Icon(
            painter = painterResource(id = item.image),
            contentDescription = "",
            modifier = Modifier.size(27.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = item.title,
            style = if (itemIsSelected)
                MaterialTheme.typography.bodySmall
            else
                MaterialTheme.typography.labelLarge
        )
    }
}

private val listMenuItems = listOf(
    MenuItem(
        route = Screens.OrdersListScreen.route,
        title = "Главная",
        image = com.onecab.core.R.drawable.icon_menu,
        isRegister = true
    ),
    MenuItem(
        route = "${Screens.CompletedOrdersScreen.route}/${"empty"}",
        title = "Завершенные заказы",
        image = com.onecab.core.R.drawable.icon_order,
        isRegister = true
    ),
    MenuItem(
        route = Screens.ReportScreen.route,
        title = "Сводка",
        image = com.onecab.core.R.drawable.icon_report,
        isRegister = true
    ),
    MenuItem(
        route = Screens.SettingScreen.route,
        title = "Настройки",
        image = com.onecab.core.R.drawable.icon_setting,
        isRegister = true
    ),
    MenuItem(
        route = "",
        title = "Выйти из профиля",
        image = com.onecab.core.R.drawable.exit_to_app_24px,
        isRegister = true
    ),
    MenuItem(
        route = Screens.AuthorizationScreen.route,
        title = "Авторизация",
        image = com.onecab.core.R.drawable.icon_report,
        isRegister = false
    ),
    MenuItem(
        route = Screens.SettingAuthScreen.route,
        title = "Настройки",
        image = com.onecab.core.R.drawable.icon_setting,
        isRegister = false
    ),
)

@Stable
private data class MenuItem(
    val route: String,
    val title: String,
    val image: Int,
    val isRegister: Boolean
)

