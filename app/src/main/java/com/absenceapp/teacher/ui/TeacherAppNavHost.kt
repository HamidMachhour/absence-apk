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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
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
import com.absenceapp.teacher.domain.AppLanguage
import com.absenceapp.teacher.ui.viewmodel.AppUiState
import com.absenceapp.teacher.ui.viewmodel.SeanceOption
import com.absenceapp.teacher.ui.viewmodel.TeacherViewModel
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
) {
    companion object {
        fun forLanguage(language: AppLanguage): AppStrings = when (language) {
            AppLanguage.EN -> AppStrings(
                teacherLogin = "Teacher Login",
                laravelBaseUrl = "Laravel Base URL",
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
                apiBaseUrl = "Laravel Base URL",
                save = "Save",
                currentBaseUrl = "Current URL",
                settingsHint = "Update the Laravel address here when the server port changes.",
            )
            AppLanguage.AR -> AppStrings(
                teacherLogin = "تسجيل دخول المعلم",
                laravelBaseUrl = "رابط Laravel",
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
                apiBaseUrl = "رابط Laravel",
                save = "حفظ",
                currentBaseUrl = "الرابط الحالي",
                settingsHint = "يمكنك تحديث عنوان Laravel هنا عندما يتغير المنفذ.",
            )
        }
    }
}

private enum class HomeSection {
    HOME,
    SETTINGS,
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
            Button(
                onClick = {
                    viewModel.setLanguage(if (uiState.language == AppLanguage.EN) AppLanguage.AR else AppLanguage.EN)
                },
            ) {
                Text(if (uiState.language == AppLanguage.EN) "AR" else "EN")
            }
        }
        Text(strings.teacherLogin, style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = uiState.baseUrl,
            onValueChange = { viewModel.updateBaseUrl(it) },
            label = { Text(strings.laravelBaseUrl) },
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = uiState.email,
            onValueChange = { viewModel.updateLoginForm(it, uiState.password) },
            label = { Text(strings.email) },
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = uiState.password,
            onValueChange = { viewModel.updateLoginForm(uiState.email, it) },
            label = { Text(strings.password) },
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = uiState.mqttConfig.host,
            onValueChange = { viewModel.updateMqttConfig(uiState.mqttConfig.copy(host = it)) },
            label = { Text(strings.mqttHost) },
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
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = uiState.mqttConfig.username,
            onValueChange = { viewModel.updateMqttConfig(uiState.mqttConfig.copy(username = it)) },
            label = { Text(strings.mqttUsername) },
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = uiState.mqttConfig.password,
            onValueChange = { viewModel.updateMqttConfig(uiState.mqttConfig.copy(password = it)) },
            label = { Text(strings.mqttPassword) },
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
    var headerExpanded by remember { mutableStateOf(true) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                        Icon(Icons.Default.Menu, contentDescription = "Open menu")
                    }
                    Button(
                        onClick = {
                            viewModel.setLanguage(if (uiState.language == AppLanguage.EN) AppLanguage.AR else AppLanguage.EN)
                        },
                    ) {
                        Text(if (uiState.language == AppLanguage.EN) "AR" else "EN")
                    }
                    Text(
                        uiState.session?.teacherName ?: strings.teacher,
                        style = MaterialTheme.typography.titleLarge,
                    )
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
            } else {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { headerExpanded = !headerExpanded },
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(strings.session)
                            Text(if (headerExpanded) strings.hide else strings.show)
                        }

                        if (headerExpanded) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("${strings.mqtt}: ${uiState.mqttStatus}")
                            Text("${strings.lastMqtt}: ${uiState.lastMqttMessage.ifBlank { strings.noMessages }}")
                            Spacer(modifier = Modifier.height(12.dp))
                            SeancePicker(
                                selectedSeanceId = uiState.seanceId,
                                options = uiState.availableSeanceOptions,
                                strings = strings,
                                onSelect = { viewModel.selectSeance(it) },
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { viewModel.loadRoster() }) { Text(strings.loadRoster) }
                    Button(onClick = { viewModel.openLastRoster() }) { Text(strings.openLast) }
                }
                Spacer(modifier = Modifier.height(12.dp))
                if (uiState.rosterError != null) {
                    Text(uiState.rosterError, color = MaterialTheme.colorScheme.error)
                }
                if (uiState.roster != null) {
                    Column(modifier = Modifier.weight(1f)) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { rosterExpanded = !rosterExpanded },
                        ) {
                            Column(Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Column {
                                        Text(
                                            uiState.roster.classeName,
                                            style = MaterialTheme.typography.titleMedium,
                                        )
                                        Text(uiState.roster.subject)
                                    }
                                    Text(if (rosterExpanded) strings.hide else strings.show)
                                }

                                if (rosterExpanded) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("${strings.salle}: ${uiState.roster.salle}")
                                    Text("${strings.absentCount}: ${uiState.roster.students.count { uiState.attendanceDraft[it.id] == true }}")
                                }
                            }
                        }

                        if (rosterExpanded) {
                            Spacer(modifier = Modifier.height(12.dp))
                            if (uiState.pendingSync) {
                                Text(strings.pendingSync)
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Button(onClick = { viewModel.resetAttendance() }) { Text(strings.reset) }
                                Button(onClick = { viewModel.submitAttendance() }) { Text(strings.submitAttendance) }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                            ) {
                                items(uiState.roster.students) { student ->
                                    val absent = uiState.attendanceDraft[student.id] == true
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                    ) {
                                        Column {
                                            Text("${student.num}. ${student.name}")
                                        }
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(if (absent) strings.absent else strings.present)
                                            Switch(
                                                checked = absent,
                                                onCheckedChange = { viewModel.toggleAbsent(student.id) },
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
}

}

@Composable
private fun SettingsScreen(
    uiState: AppUiState,
    strings: AppStrings,
    viewModel: TeacherViewModel,
) {
    Column {
        Text(strings.settings, style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))
        Text(strings.settingsHint)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = uiState.baseUrl,
            onValueChange = { viewModel.updateBaseUrl(it) },
            label = { Text(strings.apiBaseUrl) },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text("${strings.currentBaseUrl}: ${uiState.baseUrl}")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { viewModel.saveBaseUrl() }) {
            Text(strings.save)
        }
        uiState.settingsMessage?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(it, color = MaterialTheme.colorScheme.primary)
        }
    }
}
