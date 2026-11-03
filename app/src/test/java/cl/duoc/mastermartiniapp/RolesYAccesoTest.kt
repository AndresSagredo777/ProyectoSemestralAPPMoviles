package cl.duoc.mastermartiniapp

import cl.duoc.mastermartiniapp.model.Cuenta
import cl.duoc.mastermartiniapp.model.Rol
import cl.duoc.mastermartiniapp.repository.HashClave
import cl.duoc.mastermartiniapp.viewmodel.AdminViewModel
import cl.duoc.mastermartiniapp.viewmodel.ValidadorAcceso
import cl.duoc.mastermartiniapp.viewmodel.ValidadorReceta
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/** Pruebas unitarias de acceso, roles y recetas (no necesitan emulador). */
class RolesYAccesoTest {

    // ---------- Correo ----------

    @Test
    fun correoVacio_daError() = assertNotNull(ValidadorAcceso.validarCorreo("  "))

    @Test
    fun correoSinArroba_daError() = assertNotNull(ValidadorAcceso.validarCorreo("chefmastermartini.cl"))

    @Test
    fun correoSinDominio_daError() = assertNotNull(ValidadorAcceso.validarCorreo("chef@martini"))

    @Test
    fun correoCorrecto_esValido() = assertNull(ValidadorAcceso.validarCorreo("chef@mastermartini.cl"))

    // ---------- Contraseña ----------

    @Test
    fun claveCorta_daError() = assertNotNull(ValidadorAcceso.validarClaveNueva("ab1"))

    @Test
    fun claveSinNumeros_daError() = assertNotNull(ValidadorAcceso.validarClaveNueva("soloLetras"))

    @Test
    fun claveConEspacios_daError() = assertNotNull(ValidadorAcceso.validarClaveNueva("clave 123"))

    @Test
    fun claveSegura_esValida() = assertNull(ValidadorAcceso.validarClaveNueva("Martini2026"))

    @Test
    fun confirmacionDistinta_daError() =
        assertNotNull(ValidadorAcceso.validarConfirmacion("Martini2026", "Martini2025"))

    // ---------- Hash ----------

    @Test
    fun hash_esSiempreElMismoParaLaMismaClave() =
        assertEquals(HashClave.sha256("Chef123"), HashClave.sha256("Chef123"))

    @Test
    fun hash_noGuardaLaClaveEnTexto() {
        val hash = HashClave.sha256("Chef123")
        assertNotEquals("Chef123", hash)
        assertEquals(64, hash.length)
    }

    // ---------- Roles ----------

    @Test
    fun permisos_porRol() {
        assertFalse(Rol.USUARIO.puedePublicarRecetas)
        assertTrue(Rol.CHEF.puedePublicarRecetas)
        assertFalse(Rol.CHEF.puedeGestionarCuentas)
        assertTrue(Rol.ADMIN.puedeGestionarCuentas)
    }

    private val admin = Cuenta("Admin", "admin@x.cl", "h", Rol.ADMIN)
    private val chef = Cuenta("Chef", "chef@x.cl", "h", Rol.CHEF)

    @Test
    fun admin_noPuedeCambiarSuPropioRol() =
        assertNotNull(AdminViewModel.validarCambioDeRol(admin, Rol.USUARIO, admin.correo, listOf(admin, chef)))

    @Test
    fun admin_puedeAscenderAUnChef() =
        assertNull(AdminViewModel.validarCambioDeRol(chef, Rol.ADMIN, admin.correo, listOf(admin, chef)))

    @Test
    fun noSePuedeEliminarAlUltimoAdmin() {
        val otroAdmin = Cuenta("Otro", "otro@x.cl", "h", Rol.ADMIN)
        // Con un solo administrador no se puede eliminar.
        assertNotNull(AdminViewModel.validarEliminacion(admin, chef.correo, listOf(admin, chef)))
        // Con dos administradores sí.
        assertNull(AdminViewModel.validarEliminacion(otroAdmin, admin.correo, listOf(admin, otroAdmin)))
    }

    // ---------- Recetas ----------

    @Test
    fun receta_conUnSoloIngrediente_daError() =
        assertNotNull(ValidadorReceta.validarIngredientes("Harina\n\n"))

    @Test
    fun receta_separaIngredientesPorLinea() =
        assertEquals(listOf("Harina", "Azúcar"), ValidadorReceta.separarIngredientes(" Harina \n\nAzúcar"))

    @Test
    fun receta_categoriaInventada_daError() =
        assertNotNull(ValidadorReceta.validarCategoria("Sushi"))
}
