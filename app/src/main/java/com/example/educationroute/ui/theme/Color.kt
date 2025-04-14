package com.example.educationroute.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// Светлая тема с монохромной бирюзовой палитрой
val LightCustomColorScheme = lightColorScheme(
    // Основная цветовая группа (насыщенный бирюзовый)
    primary = Color(0xFF5099A4),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF445758), // Кнопки и акценты
    onPrimaryContainer = Color(0xFFECF5F6),

    // Вторичная группа (более светлый бирюзовый)
    secondary = Color(0xFF5AADAE),
    onSecondary = Color(0xFF003739),
    secondaryContainer = Color(0xFFA2E9F4),
    onSecondaryContainer = Color(0xFF002022),

    // Третичная группа (нейтральный вариант)
    tertiary = Color(0xFF6B8E8F),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFC1D7D8),
    onTertiaryContainer = Color(0xFF253636),

    // Системные цвета
    onBackground = Color(0xFF1A1C1E),
    onSurface = Color(0xFF1A1C1E),
    onSurfaceVariant = Color(0xFF3F494A),

    // Остальные параметры
    inversePrimary = Color(0xFF82D1DC),
    inverseSurface = Color(0xFF2F3033),
    inverseOnSurface = Color(0xFFF1F2F4),
    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    outline = Color(0xFF6F797A),
    outlineVariant = Color(0xFFBFC8C9),
    scrim = Color(0xFF000000),
    surfaceBright = Color(0xFFFFFFFF),
    surfaceDim = Color(0xFFD8D8DA),
    background = Color(0xFFECF5F6),
    surface = Color(0xFFECF5F6), // Основная поверхность (включая панель навигации)
    surfaceVariant = Color(0xFFDEE7E8),
    surfaceContainerLowest = Color(0xFFFFFFFF), // Для карточек/всплывающих элементов
    surfaceContainer = Color(0xFFECF5F6), // Контейнер поверхности
    surfaceContainerHigh = Color(0xFFE6EFF0) // Для элементов с легким возвышением
)

// Темная тема (производная от светлой)
val DarkCustomColorScheme = darkColorScheme(
    primary = Color(0xFF82D1DC),
    onPrimary = Color(0xFF00363D),
    primaryContainer = Color(0xFF445758), // Сохраняем цвет кнопок
    onPrimaryContainer = Color(0xFFC1E8ED),

    secondary = Color(0xFF5AADAE),
    onSecondary = Color(0xFF003739),
    secondaryContainer = Color(0xFF004F54),
    onSecondaryContainer = Color(0xFFA2E9F4),

    tertiary = Color(0xFFA5CBCE),
    onTertiary = Color(0xFF0F3537),
    tertiaryContainer = Color(0xFF274B4D),
    onTertiaryContainer = Color(0xFFC1E8EB),

    background = Color(0xFF1A1C1E),
    onBackground = Color(0xFFE1E3E5),
    surface = Color(0xFF1E1E1E),
    onSurface = Color(0xFFE1E3E5),
    surfaceVariant = Color(0xFF3F494A),
    onSurfaceVariant = Color(0xFFBFC8C9),

    inversePrimary = Color(0xFF006874),
    inverseSurface = Color(0xFFE1E3E5),
    inverseOnSurface = Color(0xFF2F3033),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    outline = Color(0xFF899394),
    outlineVariant = Color(0xFF3F494A),
    scrim = Color(0xFF000000),
    surfaceBright = Color(0xFF444748),
    surfaceDim = Color(0xFF141414),
    surfaceContainerLowest = Color(0xFF1E1E1E),  // Темно-серый (для карточек)
    surfaceContainer = Color(0xFF121212),       // Почти черный (основной фон)
    surfaceContainerHigh = Color(0xFF1E1E1E)
)