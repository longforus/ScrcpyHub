package view

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Snackbar
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.WindowScope
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.inject
import resource.Colors
import resource.Page
import resource.Strings.SETUP
import view.extention.onInitialize
import view.page.DevicePage
import view.page.SettingPage
import viewmodel.DevicePageViewModel
import viewmodel.DevicesPageViewModel
import viewmodel.MainContentViewModel
import viewmodel.SettingPageViewModel


@Composable
fun MainContent(windowScope: WindowScope, mainContentViewModel: MainContentViewModel) {
    onInitialize(mainContentViewModel)
    onDrawWindow(windowScope, mainContentViewModel)
}

@Composable
private fun onDrawWindow(windowScope: WindowScope, viewModel: MainContentViewModel) {
    val selectedPages: Page by viewModel.selectedPages.collectAsState()
    val hasError: Boolean by viewModel.hasError.collectAsState()
    val errorMessage: String? by viewModel.errorMessage.collectAsState()

    MainTheme {
        Box(modifier = Modifier.fillMaxSize().background(Colors.SMOKE_WHITE)) {
            Crossfade(selectedPages, animationSpec = tween(100)) { selectedPageName ->
                when (selectedPageName) {
                    Page.DevicesPage -> {
                        val devicesPageViewModel by inject<DevicesPageViewModel>(clazz = DevicesPageViewModel::class.java)
                        DevicesPage(
                            windowScope = windowScope,
                            devicesPageViewModel = devicesPageViewModel,
                            onNavigateSetting = { viewModel.selectPage(Page.SettingPage) },
                            onNavigateDevice = { viewModel.selectPage(Page.DevicePage(it)) }
                        )
                    }
                    Page.SettingPage -> {
                        val settingPageViewModel by inject<SettingPageViewModel>(clazz = SettingPageViewModel::class.java)
                        SettingPage(
                            windowScope = windowScope,
                            settingPageViewModel = settingPageViewModel,
                            onNavigateDevices = { viewModel.selectPage(Page.DevicesPage) },
                            onSaved = { viewModel.refresh() }
                        )
                    }
                    is Page.DevicePage -> {
                        val devicePageViewModel by inject<DevicePageViewModel>(clazz = DevicePageViewModel::class.java) {
                            parametersOf(selectedPageName.device)
                        }
                        DevicePage(
                            windowScope = windowScope,
                            deviceViewModel = devicePageViewModel,
                            onNavigateDevices = { viewModel.selectPage(Page.DevicesPage) }
                        )
                    }
                }
            }

            if (hasError) {
                if (errorMessage != null) {
                    Snackbar(modifier = Modifier.padding(8.dp).align(Alignment.BottomCenter)) {
                        Box(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
                            Row(modifier = Modifier.wrapContentSize().align(Alignment.Center)) {
                                Text(errorMessage ?: "", fontSize = 16.sp)
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(
                                    SETUP,
                                    fontSize = 16.sp,
                                    color = Colors.NAVY,
                                    modifier = Modifier.clickable { viewModel.selectPage(Page.SettingPage) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

