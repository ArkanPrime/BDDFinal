package com.example.BDDFinal.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

// Couleurs du thème général
val PrimaryViolet = Color(0xFF6A1B9A)      // Violet profond pour le thème général
val OnPrimaryViolet = Color.White          // Texte sur les éléments primaires
val SecondaryColor = Color(0xFF8E24AA)       // Exemple pour secondary
val OnSecondaryColor = Color.White
val BackgroundColor = Color.White          // Fond blanc pour l'application
val OnBackgroundColor = Color.Black
val SurfaceColor = Color.White
val OnSurfaceColor = Color.Black

private val CustomLightColorScheme = lightColorScheme(
    primary = PrimaryViolet,
    onPrimary = OnPrimaryViolet,
    secondary = SecondaryColor,
    onSecondary = OnSecondaryColor,
    background = BackgroundColor,
    onBackground = OnBackgroundColor,
    surface = SurfaceColor,
    onSurface = OnSurfaceColor,
    error = Color(0xFFD32F2F),
    onError = Color.White
)

// Couleurs spécifiques aux boutons
val ButtonBackground = Color(0xFFFFF3E5)  // Fond pastel (couleur pour les boutons)
val ButtonContent = PrimaryViolet         // Texte en violet

// Définition de formes personnalisées
val CustomShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),   // Par exemple, utilisé pour les boutons
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(0.dp)
)

// Typographie par défaut (personnalisable)
val CustomTypography = Typography()

@Composable
fun BDDFinalTheme(
    darkTheme: Boolean = false, // Forçage du mode clair
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = CustomLightColorScheme,
        typography = CustomTypography,
        shapes = CustomShapes,
        content = content
    )
}
