package com.absenceapp.teacher.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.PersonOff
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material.icons.outlined.RestartAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.absenceapp.teacher.domain.AppLanguage
import com.absenceapp.teacher.ui.components.BarcodeScannerScreen
import com.absenceapp.teacher.ui.components.CollapsibleCard
import com.absenceapp.teacher.ui.components.ManechTile
import com.absenceapp.teacher.ui.components.ManechTitleBar
import com.absenceapp.teacher.ui.theme.ManechAccent
import com.absenceapp.teacher.ui.theme.ManechSurface
import com.absenceapp.teacher.ui.theme.ManechTitle
import com.absenceapp.teacher.ui.theme.manechSwitchColors
import com.absenceapp.teacher.ui.theme.manechTextFieldColors
import com.absenceapp.teacher.ui.viewmodel.AppUiState
import com.absenceapp.teacher.ui.viewmodel.SeanceOption
import com.absenceapp.teacher.ui.viewmodel.TeacherViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class AppStrings(
    val teacherLogin: String,
    val laravelBaseUrl: String,
    val email: String,
    val password: String,
    val mqttHost: String,
    val mqttPort: String,
    val mqttUsername: String,
    val mqttPassword: String,
    val login: String,
    val session: String,
    val mqtt: String,
    val lastMqtt: String,
    val noMessages: String,
    val seance: String,
    val selectSeance: String,
    val loadRoster: String,
    val openLast: String,
    val logout: String,
    val hide: String,
    val show: String,
    val reset: String,
    val submitAttendance: String,
    val pendingSync: String,
    val present: String,
    val absent: String,
    val teacher: String,
    val language: String,
    val welcome: String,
    val salle: String,
    val absentCount: String,
    val settings: String,
    val home: String,
    val apiBaseUrl: String,
    val save: String,
    val currentBaseUrl: String,
    val settingsHint: String,
    val openMenu: String,
    val markAllAbsent: String,
    val scanCode: String,
    val scanInstruction: String,
    val cameraPermissionRequired: String,
    val grantPermission: String,
    val studentMarkedPresent: String,
    val codeNotRecognized: String,
    val close: String,
) {
    companion object {
        fun forLanguage(language: AppLanguage): AppStrings = when (language) {
            AppLanguage.EN -> AppStrings(
                teacherLogin = "Teacher Login",
                laravelBaseUrl = "API Base URL",
                email = "Email",
                password = "Password",
                mqttHost = "MQTT Host",
                mqttPort = "MQTT Port",
                mqttUsername = "MQTT Username",
                mqttPassword = "MQTT Password",
                login = "Login",
                session = "Session",
                mqtt = "MQTT",
                lastMqtt = "Last MQTT",
                noMessages = "No messages yet",
                seance = "Seance",
                selectSeance = "Select a seance",
                loadRoster = "Load Roster",
                openLast = "Open Last",
                logout = "Logout",
                hide = "Hide",
                show = "Show",
                reset = "Reset",
                submitAttendance = "Submit Attendance",
                pendingSync = "Pending sync",
                present = "Present",
                absent = "Absent",
                teacher = "Teacher",
                language = "Language",
                welcome = "Welcome",
                salle = "Salle",
                absentCount = "Absent",
                settings = "Settings",
                home = "Home",
                apiBaseUrl = "API Base URL",
                save = "Save",
                currentBaseUrl = "Current URL",
                settingsHint = "Update the API address here when the server port changes.",
                openMenu = "Open menu",
                markAllAbsent = "Mark all absent",
                scanCode = "Scan code",
                scanInstruction = "Point the camera at a student's code",
                cameraPermissionRequired = "Camera permission is required to scan codes.",
                grantPermission = "Grant permission",
                studentMarkedPresent = "%s marked present",
                codeNotRecognized = "Code not recognized",
                close = "Close",
            )
            AppLanguage.AR -> AppStrings(
                teacherLogin = "تسجيل دخول المعلم",
                laravelBaseUrl = "رابط API",
                email = "البريد الإلكتروني",
                password = "كلمة المرور",
                mqttHost = "مضيف MQTT",
                mqttPort = "منفذ MQTT",
                mqttUsername = "اسم مستخدم MQTT",
                mqttPassword = "كلمة مرور MQTT",
                login = "تسجيل الدخول",
                session = "الجلسة",
                mqtt = "MQTT",
                lastMqtt = "آخر MQTT",
                noMessages = "لا توجد رسائل بعد",
                seance = "الحصة",
                selectSeance = "اختر حصة",
                loadRoster = "تحميل القائمة",
                openLast = "آخر حفظ",
                logout = "تسجيل الخروج",
                hide = "إخفاء",
                show = "إظهار",
                reset = "إعادة تعيين",
                submitAttendance = "إرسال الحضور",
                pendingSync = "بإنتظار المزامنة",
                present = "حاضر",
                absent = "غائب",
                teacher = "المعلم",
                language = "اللغة",
                welcome = "مرحبًا",
                salle = "القاعة",
                absentCount = "الغائبون",
                settings = "الإعدادات",
                home = "الرئيسية",
                apiBaseUrl = "رابط API",
                save = "حفظ",
                currentBaseUrl = "الرابط الحالي",
                settingsHint = "يمكنك تحديث عنوان API هنا عندما يتغير المنفذ.",
                openMenu = "فتح القائمة",
                markAllAbsent = "تعليم الكل غائب",
                scanCode = "مسح الرمز",
                scanInstruction = "وجّه الكاميرا نحو رمز الطالب",
                cameraPermissionRequired = "إذن الكاميرا مطلوب لمسح الرموز.",
                grantPermission = "منح الإذن",
                studentMarkedPresent = "تم تسجيل حضور %s",
                codeNotRecognized = "الرمز غير معروف",
                close = "إغلاق",
            )
        }
    }
}

private enum class HomeSection {
    HOME,
    SEANCE,
    SETTINGS,
    SCANNER,
}

@Composable
fun TeacherAppNavHost(viewModel: TeacherViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    if (uiState.isLoggedIn) {
        HomeScreen(uiState, viewModel)
    } else {
        LoginScreen(uiState, viewModel)
    }
}

@Composable
fun LoginScreen(uiState: AppUiState, viewModel: TeacherViewModel) {
    val strings = AppStrings.forLanguage(uiState.language)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedButton(
                onClick = {
                    viewModel.setLanguage(if (uiState.language == AppLanguage.EN) AppLanguage.AR else AppLanguage.EN)
                },
                colors = ButtonDefaults.outlinedButtonColors(contentColor = ManechTitle),
                border = BorderStroke(1.dp, ManechTitle),
            ) {
                Text(if (uiState.language == AppLanguage.EN) "AR" else "EN")
            }
        }
        Text(
            strings.teacherLogin,
            style = MaterialTheme.typography.headlineMedium,
            color = ManechAccent,
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = uiState.baseUrl,
            onValueChange = { viewModel.updateBaseUrl(it) },
            label = { Text(strings.laravelBaseUrl) },
            colors = manechTextFieldColors(),
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = uiState.email,
            onValueChange = { viewModel.updateLoginForm(it, uiState.password) },
            label = { Text(strings.email) },
            colors = manechTextFieldColors(),
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = uiState.password,
            onValueChange = { viewModel.updateLoginForm(uiState.email, it) },
            label = { Text(strings.password) },
            colors = manechTextFieldColors(),
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = uiState.mqttConfig.host,
            onValueChange = { viewModel.updateMqttConfig(uiState.mqttConfig.copy(host = it)) },
            label = { Text(strings.mqttHost) },
            colors = manechTextFieldColors(),
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = uiState.mqttConfig.port.toString(),
            onValueChange = { value ->
                val port = value.toIntOrNull() ?: 1883
                viewModel.updateMqttConfig(uiState.mqttConfig.copy(port = port))
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text(strings.mqttPort) },
            colors = manechTextFieldColors(),
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = uiState.mqttConfig.username,
            onValueChange = { viewModel.updateMqttConfig(uiState.mqttConfig.copy(username = it)) },
            label = { Text(strings.mqttUsername) },
            colors = manechTextFieldColors(),
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = uiState.mqttConfig.password,
            onValueChange = { viewModel.updateMqttConfig(uiState.mqttConfig.copy(password = it)) },
            label = { Text(strings.mqttPassword) },
            colors = manechTextFieldColors(),
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(12.dp))
        if (uiState.errorMessage != null) {
            Text(uiState.errorMessage, color = MaterialTheme.colorScheme.error)
        }
        Button(
            onClick = {
                viewModel.login(
                    onSuccess = {},
                    onError = { },
                )
            },
            enabled = !uiState.isLoading,
            colors = ButtonDefaults.buttonColors(containerColor = ManechAccent, contentColor = ManechSurface),
            modifier = Modifier.fillMaxWidth(),
        ) {
            if (uiState.isLoading) CircularProgressIndicator() else Text(strings.login)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeancePicker(
    selectedSeanceId: String,
    options: List<SeanceOption>,
    strings: AppStrings,
    onSelect: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedOption = options.firstOrNull { it.id == selectedSeanceId }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        OutlinedTextField(
            value = selectedOption?.label ?: selectedSeanceId.ifBlank { strings.selectSeance },
            onValueChange = {},
            readOnly = true,
            label = { Text(strings.seance) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = manechTextFieldColors(),
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.label) },
                    onClick = {
                        onSelect(option.id)
                        expanded = false
                    },
                )
            }
        }
    }
}

@Composable
fun HomeScreen(uiState: AppUiState, viewModel: TeacherViewModel) {
    val strings = AppStrings.forLanguage(uiState.language)
    var currentSection by remember { mutableStateOf(HomeSection.HOME) }
    var rosterExpanded by remember { mutableStateOf(true) }
    var scanFeedback by remember { mutableStateOf<String?>(null) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    LaunchedEffect(scanFeedback) {
        if (scanFeedback != null) {
            delay(1500)
            scanFeedback = null
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    strings.teacher,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleLarge,
                )
                HorizontalDivider()
                NavigationDrawerItem(
                    label = { Text(strings.home) },
                    selected = currentSection == HomeSection.HOME,
                    onClick = {
                        currentSection = HomeSection.HOME
                        scope.launch { drawerState.close() }
                    },
                )
                NavigationDrawerItem(
                    label = { Text(strings.seance) },
                    selected = currentSection == HomeSection.SEANCE,
                    onClick = {
                        currentSection = HomeSection.SEANCE
                        scope.launch { drawerState.close() }
                    },
                )
                NavigationDrawerItem(
                    label = { Text(strings.settings) },
                    selected = currentSection == HomeSection.SETTINGS,
                    onClick = {
                        currentSection = HomeSection.SETTINGS
                        scope.launch { drawerState.close() }
                    },
                )
                NavigationDrawerItem(
                    label = { Text(strings.language) },
                    selected = false,
                    onClick = {
                        viewModel.setLanguage(if (uiState.language == AppLanguage.EN) AppLanguage.AR else AppLanguage.EN)
                        scope.launch { drawerState.close() }
                    },
                )
                NavigationDrawerItem(
                    label = { Text(strings.logout) },
                    selected = false,
                    onClick = {
                        viewModel.logout()
                        scope.launch { drawerState.close() }
                    },
                )
            }
        },
    ) {
        Scaffold(
            topBar = {
                Column {
                    ManechTitleBar(title = uiState.session?.teacherName ?: strings.teacher)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(ManechSurface)
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Outlined.Menu, contentDescription = strings.openMenu, tint = ManechAccent)
                        }
                    }
                    if (currentSection == HomeSection.HOME) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(ManechSurface)
                                .horizontalScroll(rememberScrollState())
                                .padding(horizontal = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            IconButton(onClick = { viewModel.loadRoster() }) {
                                Icon(Icons.Outlined.Download, contentDescription = strings.loadRoster, tint = ManechAccent)
                            }
                            IconButton(onClick = { viewModel.openLastRoster() }) {
                                Icon(Icons.Outlined.History, contentDescription = strings.openLast, tint = ManechAccent)
                            }
                            if (uiState.roster != null) {
                                IconButton(onClick = { viewModel.markAllAbsent() }) {
                                    Icon(Icons.Outlined.PersonOff, contentDescription = strings.markAllAbsent, tint = ManechAccent)
                                }
                                IconButton(onClick = { viewModel.resetAttendance() }) {
                                    Icon(Icons.Outlined.RestartAlt, contentDescription = strings.reset, tint = ManechAccent)
                                }
                                IconButton(onClick = { viewModel.submitAttendance() }) {
                                    Icon(Icons.AutoMirrored.Outlined.Send, contentDescription = strings.submitAttendance, tint = ManechAccent)
                                }
                                IconButton(onClick = { currentSection = HomeSection.SCANNER }) {
                                    Icon(Icons.Outlined.QrCodeScanner, contentDescription = strings.scanCode, tint = ManechAccent)
                                }
                            }
                        }
                    }
                }
            },
        ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
        ) {
            if (currentSection == HomeSection.SETTINGS) {
                SettingsScreen(uiState, strings, viewModel)
            } else if (currentSection == HomeSection.SEANCE) {
                SeanceSelectionScreen(
                    uiState = uiState,
                    strings = strings,
                    viewModel = viewModel,
                    onSeanceSelected = { currentSection = HomeSection.HOME },
                )
            } else if (currentSection == HomeSection.SCANNER) {
                BarcodeScannerScreen(
                    instructionText = strings.scanInstruction,
                    permissionRationale = strings.cameraPermissionRequired,
                    grantPermissionLabel = strings.grantPermission,
                    closeLabel = strings.close,
                    feedbackText = scanFeedback,
                    onCodeScanned = { code ->
                        val matchedName = viewModel.markPresentByScannedCode(code)
                        scanFeedback = if (matchedName != null) {
                            strings.studentMarkedPresent.format(matchedName)
                        } else {
                            strings.codeNotRecognized
                        }
                    },
                    onClose = { currentSection = HomeSection.HOME },
                )
            } else {
                val selectedSeanceLabel = uiState.availableSeanceOptions
                    .firstOrNull { it.id == uiState.seanceId }
                    ?.label
                    ?: uiState.seanceId.ifBlank { strings.selectSeance }
                Text(
                    "${strings.seance}: $selectedSeanceLabel",
                    color = ManechAccent,
                    fontSize = 14.sp,
                )

                Spacer(modifier = Modifier.height(8.dp))
                if (uiState.rosterError != null) {
                    Text(uiState.rosterError, color = MaterialTheme.colorScheme.error)
                }
                if (uiState.roster != null) {
                    Column(modifier = Modifier.weight(1f)) {
                        CollapsibleCard(
                            expanded = rosterExpanded,
                            onToggleExpanded = { rosterExpanded = !rosterExpanded },
                            expandContentDescription = strings.show,
                            collapseContentDescription = strings.hide,
                            header = {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(uiState.roster.classeName, color = ManechSurface, fontSize = 16.sp)
                                    Text(uiState.roster.subject, color = ManechSurface, fontSize = 14.sp)
                                }
                            },
                        ) {
                            Text("${strings.salle}: ${uiState.roster.salle}", color = ManechTitle, fontSize = 14.sp)
                            Text(
                                "${strings.absentCount}: ${uiState.roster.students.count { uiState.attendanceDraft[it.id] == true }}",
                                color = ManechTitle,
                                fontSize = 14.sp,
                            )

                            Spacer(modifier = Modifier.height(12.dp))
                            if (uiState.pendingSync) {
                                Text(strings.pendingSync, color = ManechTitle, fontSize = 14.sp)
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                            ) {
                                items(uiState.roster.students) { student ->
                                    val absent = uiState.attendanceDraft[student.id] == true
                                    ManechTile(
                                        primaryText = "${student.num}. ${student.name}",
                                        secondaryText = if (absent) strings.absent else strings.present,
                                        trailing = {
                                            Switch(
                                                checked = absent,
                                                onCheckedChange = { viewModel.toggleAbsent(student.id) },
                                                colors = manechSwitchColors(),
                                            )
                                        },
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

}

@Composable
private fun SeanceSelectionScreen(
    uiState: AppUiState,
    strings: AppStrings,
    viewModel: TeacherViewModel,
    onSeanceSelected: () -> Unit,
) {
    CollapsibleCard(
        expanded = true,
        onToggleExpanded = {},
        expandContentDescription = strings.show,
        collapseContentDescription = strings.hide,
        collapsible = false,
        header = { Text(strings.seance, color = ManechSurface, fontSize = 16.sp) },
    ) {
        Text("${strings.mqtt}: ${uiState.mqttStatus}", color = ManechTitle, fontSize = 14.sp)
        Text(
            "${strings.lastMqtt}: ${uiState.lastMqttMessage.ifBlank { strings.noMessages }}",
            color = ManechTitle,
            fontSize = 14.sp,
        )
        Spacer(modifier = Modifier.height(12.dp))
        SeancePicker(
            selectedSeanceId = uiState.seanceId,
            options = uiState.availableSeanceOptions,
            strings = strings,
            onSelect = {
                viewModel.selectSeance(it)
                onSeanceSelected()
            },
        )
    }
}

@Composable
private fun SettingsScreen(
    uiState: AppUiState,
    strings: AppStrings,
    viewModel: TeacherViewModel,
) {
    CollapsibleCard(
        expanded = true,
        onToggleExpanded = {},
        expandContentDescription = strings.show,
        collapseContentDescription = strings.hide,
        collapsible = false,
        header = { Text(strings.settings, color = ManechSurface, fontSize = 16.sp) },
    ) {
        Text(strings.settingsHint, color = ManechTitle, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = uiState.baseUrl,
            onValueChange = { viewModel.updateBaseUrl(it) },
            label = { Text(strings.apiBaseUrl) },
            colors = manechTextFieldColors(),
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text("${strings.currentBaseUrl}: ${uiState.baseUrl}", color = ManechTitle, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { viewModel.saveBaseUrl() },
            colors = ButtonDefaults.buttonColors(containerColor = ManechAccent, contentColor = ManechSurface),
        ) {
            Text(strings.save)
        }
        uiState.settingsMessage?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(it, color = MaterialTheme.colorScheme.primary)
        }
    }
}
