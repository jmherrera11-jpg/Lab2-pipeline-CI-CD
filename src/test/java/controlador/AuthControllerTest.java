package controlador;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AuthControllerTest {
    
    @Test
    void login_debe_funcionar_con_rol_teacher() {
        AuthController controller = new AuthController();
        boolean result = controller.login("teacher");
        assertTrue(result);
        assertEquals("teacher", controller.getRolActual());
    }
    
    @Test
    void login_debe_funcionar_con_rol_student() {
        AuthController controller = new AuthController();
        boolean result = controller.login("student");
        assertTrue(result);
        assertEquals("student", controller.getRolActual());
    }
    
    @Test
    void login_debe_funcionar_con_rol_coordinator() {
        AuthController controller = new AuthController();
        boolean result = controller.login("coordinator");
        assertTrue(result);
        assertEquals("coordinator", controller.getRolActual());
    }
    
    @Test
    void login_debe_fallar_con_rol_invalido() {
        AuthController controller = new AuthController();
        boolean result = controller.login("invalid");
        assertFalse(result);
        assertNull(controller.getRolActual());
    }
    
    @Test
    void logout_debe_limpiar_usuario_actual() {
        AuthController controller = new AuthController();
        controller.login("teacher");
        assertNotNull(controller.getUsuarioActual());
        assertEquals("teacher", controller.getRolActual());
        
        controller.logout();
        assertNull(controller.getUsuarioActual());
        assertNull(controller.getRolActual());
    }
    
    @Test
    void getUsuarioActual_debe_retornar_nulo_sin_login() {
        AuthController controller = new AuthController();
        assertNull(controller.getUsuarioActual());
        assertNull(controller.getRolActual());
        
        controller.login("teacher");
        assertNotNull(controller.getUsuarioActual());
        
        controller.logout();
        assertNull(controller.getUsuarioActual());
    }
    
    @Test
    void getRolActual_debe_retornar_rol_correcto() {
        AuthController controller = new AuthController();
        
        controller.login("teacher");
        assertEquals("teacher", controller.getRolActual());
        
        controller.logout();
        controller.login("student");
        assertEquals("student", controller.getRolActual());
        
        controller.logout();
        controller.login("coordinator");
        assertEquals("coordinator", controller.getRolActual());
    }
    @Test
    void getUsuarioActual_debe_retornar_nulo_despues_de_logout() {
        AuthController controller = new AuthController();
        
        controller.login("teacher");
        assertNotNull(controller.getUsuarioActual());
        
        controller.logout();
        assertNull(controller.getUsuarioActual());
    }

    @Test
    void getRolActual_debe_retornar_null_sin_login() {
        AuthController controller = new AuthController();
        assertNull(controller.getRolActual());
    }
}