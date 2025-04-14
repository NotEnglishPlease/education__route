package com.example.educationroute.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.educationroute.viewmodel.PaymentViewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PaymentScreen(navController: NavController?, viewModel: PaymentViewModel = viewModel()) {
    val paidLessons by viewModel.paidLessons
    val selectedLessons by viewModel.selectedLessons
    val discounts by viewModel.discounts
    val lessonPrice = 1500

    val discountValues = mapOf(
        "Многодетная семья" to 500,
        "Приглашенный друг" to 300,
        "Льгота" to 700
    )

    val totalDiscount = discounts.sumOf { discountValues[it] ?: 0 }
    val totalPrice = selectedLessons * lessonPrice - totalDiscount

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(text = "Оплаченные занятия: $paidLessons", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Divider()
        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Количество занятий для оплаты", style = MaterialTheme.typography.bodyLarge)
        Column {
            listOf(4, 12, 36).forEach { lessons ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = (selectedLessons == lessons),
                        onClick = { viewModel.setLessons(lessons) }
                    )
                    Text(text = "$lessons занятий")
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Divider()
        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Выбор дополнительных скидок", style = MaterialTheme.typography.bodyLarge)
        Column {
            discountValues.keys.forEach { discount ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = discount in discounts,
                        onCheckedChange = { viewModel.toggleDiscount(discount) }
                    )
                    Text(text = discount)
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Divider()
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "К оплате", style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.width(8.dp))

            Text(text = "$totalPrice ₽", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { viewModel.pay() },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Оплатить")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Divider()
        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { /* TODO: Реализовать получение чека */ }, modifier = Modifier.padding(top = 8.dp)) {
            Text("Получить чек о предыдущей оплате")
        }
    }
}