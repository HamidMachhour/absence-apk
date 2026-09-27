package com.absenceapp.teacher.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.absenceapp.teacher.TeacherApp
import com.absenceapp.teacher.ui.theme.AbsenceTheme
import com.absenceapp.teacher.ui.viewmodel.TeacherViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = application as TeacherApp
        val viewModel = TeacherViewModel(app.appContainer)

        setContent {
            AbsenceTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    TeacherAppNavHost(viewModel = viewModel)
                }
            }
        }
    }
}
