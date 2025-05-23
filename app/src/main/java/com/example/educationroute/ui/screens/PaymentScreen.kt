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
fun PaymentScreen(navController: NavController?, clientId: Int, viewModel: PaymentViewModel = viewModel()) {
    val paidLessons by viewModel.paidLessons
    val selectedLessons by viewModel.selectedLessons
    val discounts by viewModel.discounts
    val error by viewModel.error
    val lessonPrice = 1500

    // Загружаем количество оплаченных занятий при первом запуске
    LaunchedEffect(clientId) {
        viewModel.loadPaidLessons(clientId)
    }

    val percentageDiscounts = mapOf(
        "Многодетная семья" to 15, // 15% скидка
        "Льгота" to 20 // 20% скидка
    )

    val fixedDiscounts = mapOf(
        "Приглашенный друг" to 1000 // 1000₽ фиксированная скидка
    )

    val basePrice = selectedLessons * lessonPrice
    
    // Считаем процентные скидки
    val percentageDiscount = discounts
        .filter { it in percentageDiscounts.keys }
        .sumOf { percentageDiscounts[it] ?: 0 }
    
    // Считаем фиксированные скидки
    val fixedDiscount = discounts
        .filter { it in fixedDiscounts.keys }
        .sumOf { fixedDiscounts[it] ?: 0 }
    
    // Применяем сначала процентные скидки, потом фиксированные
    val priceAfterPercentage = basePrice * (100 - percentageDiscount) / 100
    val totalPrice = priceAfterPercentage - fixedDiscount

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
            // Отображаем процентные скидки
            percentageDiscounts.keys.forEach { discount ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = discount in discounts,
                        onCheckedChange = { viewModel.toggleDiscount(discount) }
                    )
                    Text(text = "$discount (${percentageDiscounts[discount]}%)")
                }
            }
            // Отображаем фиксированные скидки
            fixedDiscounts.keys.forEach { discount ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = discount in discounts,
                        onCheckedChange = { viewModel.toggleDiscount(discount) }
                    )
                    Text(text = "$discount (${fixedDiscounts[discount]}₽)")
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
        }

        if (error != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = error!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.pay(clientId) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Оплатить")
        }

        Spacer(modifier = Modifier.height(8.dp))
        Divider()
        Spacer(modifier = Modifier.height(8.dp))
    }
}