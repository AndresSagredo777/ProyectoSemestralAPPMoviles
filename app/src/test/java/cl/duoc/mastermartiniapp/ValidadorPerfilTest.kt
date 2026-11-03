package cl.duoc.mastermartiniapp

import cl.duoc.mastermartiniapp.viewmodel.ValidadorPerfil
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

/** Pruebas unitarias de las reglas de validación del perfil (no necesitan emulador). */
class ValidadorPerfilTest {

    @Test
    fun nombreVacio_daError() {
        assertNotNull(ValidadorPerfil.validarNombre("   "))
    }

    @Test
    fun nombreMuyCorto_daError() {
        assertNotNull(ValidadorPerfil.validarNombre("Ed"))
    }

    @Test
    fun nombreConNumeros_daError() {
        assertNotNull(ValidadorPerfil.validarNombre("Eduardo 123"))
    }

    @Test
    fun nombreConTildesYEspacios_esValido() {
        assertNull(ValidadorPerfil.validarNombre("José Ñúñez"))
    }

    @Test
    fun celularConEspacios_esValido() {
        assertNull(ValidadorPerfil.validarTelefono("9 1234 5678"))
    }

    @Test
    fun celularConCodigoPais_esValido() {
        assertNull(ValidadorPerfil.validarTelefono("+56 9 1234 5678"))
    }

    @Test
    fun celularIncompleto_daError() {
        assertNotNull(ValidadorPerfil.validarTelefono("9 1234 567"))
    }

    @Test
    fun telefonoFijo_daError() {
        assertNotNull(ValidadorPerfil.validarTelefono("2 2345 6789"))
    }

    @Test
    fun normalizarTelefono_quitaEspaciosYGuiones() {
        assertEquals("912345678", ValidadorPerfil.normalizarTelefono("9 1234-5678"))
    }
}
