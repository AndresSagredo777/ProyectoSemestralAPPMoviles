package cl.duoc.mastermartiniapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import cl.duoc.mastermartiniapp.navigation.NavegacionApp
import cl.duoc.mastermartiniapp.ui.theme.MasterMartiniApp_Grupo9Theme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            MasterMartiniApp_Grupo9Theme {
                NavegacionApp()
            }
        }
    }
}