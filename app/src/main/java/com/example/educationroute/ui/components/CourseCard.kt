package com.example.educationroute.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.educationroute.data.Course

@Composable
fun CourseCard(
    course: Course,
    onEnrollClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = course.subject,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "${course.weekDay}, ${course.time}",
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = "Преподаватель: ${course.teacher}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onEnrollClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Записаться")
            }
        }
    }
}