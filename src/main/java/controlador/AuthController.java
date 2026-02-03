package controlador;

import modelo.Usuario;
import java.util.HashMap;
import java.util.Map;

public class AuthController {
    private Map<String, Usuario> usuarios;
    private Usuario usuarioActual;
    
    public AuthController() {
        usuarios = new HashMap<>();
        cargarUsuariosDemo();
    }
    
    private void cargarUsuariosDemo() {
        // CORREGIDO: Usar claves en inglés para que coincidan con la comparación en DashboardView
        usuarios.put("teacher", new Usuario("1", "Prof. Anderson", "profesor@academico.edu", "teacher"));
        usuarios.put("student", new Usuario("2", "Juan Pérez", "estudiante@academico.edu", "student"));
        usuarios.put("coordinator", new Usuario("3", "Coord. García", "coordinador@academico.edu", "coordinator"));
    }
    
    public boolean login(String rol) {
        usuarioActual = usuarios.get(rol);
        return usuarioActual != null;
    }
    
    public void logout() {
        usuarioActual = null;
    }
    
    public Usuario getUsuarioActual() {
        return usuarioActual;
    }
    
    public String getRolActual() {
        return usuarioActual != null ? usuarioActual.getRol() : null;
    }
}