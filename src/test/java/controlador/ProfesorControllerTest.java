package controlador;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class ProfesorControllerTest {
    private ProfesorController controller;
    
    @BeforeEach
    void setUp() {
        controller = new ProfesorController();
    }
    
    @Test
    void obtenerTodosProfesores_debe_retornar_lista_no_vacia() {
        List<modelo.Usuario> profesores = controller.obtenerTodosProfesores();
        assertFalse(profesores.isEmpty());
    }
    
    @Test
    void crearProfesor_debe_funcionar() {
        boolean creado = controller.crearProfesor(
            "Prof. Test", 
            "test.profesor@academico.edu"
        );
        assertTrue(creado);
        
        // Limpiar
        List<modelo.Usuario> profesores = controller.buscarProfesores("Test");
        if (!profesores.isEmpty()) {
            for (modelo.Usuario p : profesores) {
                if (p.getEmail().equals("test.profesor@academico.edu")) {
                    controller.eliminarProfesor(p.getId());
                }
            }
        }
    }
    
    @Test
    void crearProfesor_duplicado_debe_fallar() {
        // Crear primer profesor
        boolean primerCreacion = controller.crearProfesor(
            "Prof. Duplicado", 
            "duplicado@academico.edu"
        );
        assertTrue(primerCreacion);
        
        // Intentar crear otro con mismo email
        boolean segundaCreacion = controller.crearProfesor(
            "Prof. Otro", 
            "duplicado@academico.edu"
        );
        assertFalse(segundaCreacion);
        
        // Limpiar
        List<modelo.Usuario> profesores = controller.buscarProfesores("Duplicado");
        if (!profesores.isEmpty()) {
            for (modelo.Usuario p : profesores) {
                controller.eliminarProfesor(p.getId());
            }
        }
    }
    
    @Test
    void obtenerProfesorPorId_con_id_invalido_debe_retornar_null() {
        modelo.Usuario resultado = controller.obtenerProfesorPorId("ID_INEXISTENTE_12345");
        assertNull(resultado);
    }
    
    @Test
    void buscarProfesores_debe_encontrar_resultados() {
        List<modelo.Usuario> resultados = controller.buscarProfesores("Anderson");
        assertFalse(resultados.isEmpty());
    }
    
    @Test
    void eliminarProfesor_con_id_invalido_debe_fallar() {
        boolean resultado = controller.eliminarProfesor("ID_INEXISTENTE_12345");
        assertFalse(resultado);
    }
}