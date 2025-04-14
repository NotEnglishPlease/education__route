package com.example.educationroute.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.educationroute.data.EnrolledCourse

@Composable
fun EnrolledCourseCard(
    course: EnrolledCourse,
    onDetailsClick: () -> Unit,
    onUnenrollClick: () -> Unit,
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
                    text = "Возраст: ${course.ageGroup}",
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = "Преподаватель: ${course.teacher}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onDetailsClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Сведения")
                }

                Button(
                    onClick = onUnenrollClick,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Отписаться")
                }
            }
        }
    }
}