package cl.duoc.mastermartiniapp.ui.home

import androidx.compose.foundation.Image
import cl.duoc.mastermartiniapp.R
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource

@Composable
fun HomeScreen() {
    Column() {
    Text(text = "Bienvenido a Martini App")
    Text(text = "Contenido para emprendedores gastronómicos")
        Button(
            onClick = {
                println("Explorando contenidos")
            }
        ) {
            Text("Explorar contenido")
        }
        Image(
            painter = painterResource(id = R.drawable.master_martini_logo),
            contentDescription = "Logo de master martini"
        )
    }

}

