@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.BDDFinal

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.BDDFinal.ui.theme.BDDFinalTheme
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.BDDFinal.data.User
import com.example.BDDFinal.viewmodel.UserViewModel
import at.favre.lib.crypto.bcrypt.BCrypt
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import com.example.BDDFinal.viewmodel.PlanningViewModel
import java.util.Calendar

fun hashPassword(plainPassword: String): String {
    return BCrypt.withDefaults().hashToString(12, plainPassword.toCharArray())
}



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BDDFinalTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "home") {
                    composable("home") { HomeScreen(navController) }
                    composable("register") { RegisterScreen(navController) }
                    composable("login") { LoginScreen(navController) }
                    composable(
                        route = "planning/{userId}",
                        arguments = listOf(navArgument("userId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val userId = backStackEntry.arguments?.getInt("userId") ?: 0
                        PlanningScreen(navController, userId)
                    }
                    composable(
                        route = "planningSummary/{userId}",
                        arguments = listOf(navArgument("userId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val userId = backStackEntry.arguments?.getInt("userId") ?: 0
                        PlanningSummaryScreen(navController, userId)
                    }
                }
            }
        }
    }
}



@Composable
fun HomeScreen(navController: androidx.navigation.NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Accueil",
                            modifier = Modifier.align(Alignment.Center),
                            textAlign = TextAlign.Center
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { navController.navigate("register") },
                    shape = RoundedCornerShape(2.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(56.dp)
                ) {
                    Text("Nouvelle Inscription")
                }
                Button(
                    onClick = { navController.navigate("login") },
                    shape = RoundedCornerShape(2.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(56.dp)
                ) {
                    Text("Connexion")
                }
            }
        }
    }
}

@Composable
fun RegisterScreen(
    navController: NavController,
    userViewModel: UserViewModel = viewModel()
) {
    val context = LocalContext.current

    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var birthdate by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var interests by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf("") }

    val calendar = java.util.Calendar.getInstance()
    val datePickerDialog = android.app.DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            birthdate = "${dayOfMonth}/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Scaffold(
        topBar = { TopAppBar(title = { Text("Inscription") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (errorMsg.isNotEmpty()) {
                Text(text = errorMsg, color = MaterialTheme.colorScheme.error)
            }
            OutlinedTextField(
                value = login,
                onValueChange = { login = it },
                label = { Text("Login") },
                placeholder = { Text("Ex : user1") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Mot de passe") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(autoCorrect = false),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Prénom") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = surname,
                onValueChange = { surname = it },
                label = { Text("Nom") },
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = { datePickerDialog.show() },
                shape = RoundedCornerShape(4.dp), // Ajuste l'arrondi selon tes préférences
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp) // Hauteur similaire à un OutlinedTextField
            ) {
                Text(
                    text = if (birthdate.isNotEmpty()) "Date de naissance : $birthdate"
                    else "Sélectionner la date de naissance"
                )
            }



            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Téléphone") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )
            InterestsSelection(interests = interests, onInterestsChange = { interests = it })

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (!login.matches("^[A-Za-z][A-Za-z0-9]{0,9}$".toRegex())) {
                        errorMsg = "Login invalide : doit commencer par une lettre et contenir max 10 caractères"
                        return@Button
                    }
                    if (password.length < 6) {
                        errorMsg = "Mot de passe trop court (min 6 caractères)"
                        return@Button
                    }
                    if (birthdate.isEmpty()) {
                        errorMsg = "Veuillez sélectionner une date de naissance"
                        return@Button
                    }
                    val newUser = User(
                        login = login,
                        password = hashPassword(password), // Utilise ta fonction de hachage
                        name = name,
                        surname = surname,
                        birthdate = birthdate,
                        phone = phone,
                        email = email,
                        interests = interests
                    )
                    userViewModel.registerUser(newUser) { success, id ->
                        if (!success) {
                            errorMsg = "Login déjà utilisé, choisissez-en un autre."
                        } else {
                            Toast.makeText(context, "Inscription réussie", Toast.LENGTH_SHORT).show()
                            navController.navigate("login")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("S'inscrire")
            }
        }
    }
}


@Composable
fun LoginScreen(
    navController: NavController,
    userViewModel: UserViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val context = LocalContext.current

    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf("") }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Connexion") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (errorMsg.isNotEmpty()) {
                Text(text = errorMsg, color = MaterialTheme.colorScheme.error)
            }
            OutlinedTextField(
                value = login,
                onValueChange = { login = it },
                label = { Text("Login") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Mot de passe") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    userViewModel.login(login, password) { success, user ->
                        if (success && user != null) {
                            Toast.makeText(context, "Connexion réussie", Toast.LENGTH_SHORT).show()
                            navController.navigate("planning/${user.id}")
                        } else {
                            errorMsg = "Login ou mot de passe incorrect"
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Se connecter")
            }
        }
    }
}



@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun PlanningScreen(
    navController: NavController,
    userId: Int,
    planningViewModel: PlanningViewModel = viewModel()
) {
    LaunchedEffect(userId) {
        planningViewModel.loadPlanning(userId)
    }

    var slot1 by remember { mutableStateOf("") }
    var slot2 by remember { mutableStateOf("") }
    var slot3 by remember { mutableStateOf("") }
    var slot4 by remember { mutableStateOf("") }

    LaunchedEffect(planningViewModel.planning.value) {
        planningViewModel.planning.value?.let { plan ->
            slot1 = plan.slot1
            slot2 = plan.slot2
            slot3 = plan.slot3
            slot4 = plan.slot4
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Planning") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = "Saisis ton planning pour l'utilisateur $userId")
            OutlinedTextField(
                value = slot1,
                onValueChange = { slot1 = it },
                label = { Text("08h-10h") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = slot2,
                onValueChange = { slot2 = it },
                label = { Text("10h-12h") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = slot3,
                onValueChange = { slot3 = it },
                label = { Text("14h-16h") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = slot4,
                onValueChange = { slot4 = it },
                label = { Text("16h-18h") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    planningViewModel.savePlanning(userId, slot1, slot2, slot3, slot4)
                    navController.navigate("planningSummary/$userId")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Valider Planning")
            }
        }
    }
}
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun PlanningSummaryScreen(
    navController: NavController,
    userId: Int,
    planningViewModel: PlanningViewModel = viewModel()
) {
    LaunchedEffect(userId) {
        planningViewModel.loadPlanning(userId)
    }

    val plan = planningViewModel.planning.collectAsState().value

    var slot1 by remember { mutableStateOf(plan?.slot1 ?: "") }
    var slot2 by remember { mutableStateOf(plan?.slot2 ?: "") }
    var slot3 by remember { mutableStateOf(plan?.slot3 ?: "") }
    var slot4 by remember { mutableStateOf(plan?.slot4 ?: "") }

    LaunchedEffect(plan) {
        plan?.let {
            slot1 = it.slot1
            slot2 = it.slot2
            slot3 = it.slot3
            slot4 = it.slot4
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Synthèse du Planning",
                            modifier = Modifier.align(Alignment.Center),
                            textAlign = TextAlign.Center
                        )
                    }
                },
                modifier = Modifier.height(56.dp)
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.9f),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                EditablePlanningSlot(
                    label = "08h-10h",
                    initialValue = slot1,
                    onValueChange = { newVal ->
                        slot1 = newVal
                        planningViewModel.savePlanning(userId, slot1, slot2, slot3, slot4)
                    }
                )
                EditablePlanningSlot(
                    label = "10h-12h",
                    initialValue = slot2,
                    onValueChange = { newVal ->
                        slot2 = newVal
                        planningViewModel.savePlanning(userId, slot1, slot2, slot3, slot4)
                    }
                )
                EditablePlanningSlot(
                    label = "14h-16h",
                    initialValue = slot3,
                    onValueChange = { newVal ->
                        slot3 = newVal
                        planningViewModel.savePlanning(userId, slot1, slot2, slot3, slot4)
                    }
                )
                EditablePlanningSlot(
                    label = "16h-18h",
                    initialValue = slot4,
                    onValueChange = { newVal ->
                        slot4 = newVal
                        planningViewModel.savePlanning(userId, slot1, slot2, slot3, slot4)
                    }
                )
            }
        }
    }
}


@Composable
fun EditablePlanningSlot(
    label: String,
    initialValue: String,
    onValueChange: (String) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var textValue by remember { mutableStateOf(initialValue) }

    Button(
        onClick = { showDialog = true },
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        Text(text = "$label : ${if (initialValue.isNotEmpty()) initialValue else "vide"}")
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Modifier $label") },
            text = {
                OutlinedTextField(
                    value = textValue,
                    onValueChange = { textValue = it },
                    label = { Text(label) },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onValueChange(textValue)
                        showDialog = false
                    }
                ) {
                    Text("Confirmer")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog = false }
                ) {
                    Text("Annuler")
                }
            }
        )
    }
}


@Composable
fun InterestsSelection(
    interests: String,
    onInterestsChange: (String) -> Unit
) {
    var sportChecked by remember { mutableStateOf(false) }
    var musiqueChecked by remember { mutableStateOf(false) }
    var lectureChecked by remember { mutableStateOf(false) }

    LaunchedEffect(sportChecked, musiqueChecked, lectureChecked) {
        val selected = mutableListOf<String>()
        if (sportChecked) selected.add("Sport")
        if (musiqueChecked) selected.add("Musique")
        if (lectureChecked) selected.add("Lecture")
        onInterestsChange(selected.joinToString(", "))
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Checkbox(
                checked = sportChecked,
                onCheckedChange = { sportChecked = it }
            )
            Text(text = "Sport")
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Checkbox(
                checked = musiqueChecked,
                onCheckedChange = { musiqueChecked = it }
            )
            Text(text = "Musique")
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Checkbox(
                checked = lectureChecked,
                onCheckedChange = { lectureChecked = it }
            )
            Text(text = "Lecture")
        }
    }
}
