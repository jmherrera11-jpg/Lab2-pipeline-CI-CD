package controlador;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import modelo.Estudiante;
import java.util.List;

public class EstudianteControllerTest {
    private EstudianteController controller;
    
    @BeforeEach
    void setUp() {
        controller = new EstudianteController();
    }
    
    @Test
    void obtenerTodosEstudiantes_debe_retornar_lista_no_vacia() {
        List<Estudiante> estudiantes = controller.obtenerTodosEstudiantes();
        assertFalse(estudiantes.isEmpty());
        assertTrue(estudiantes.size() >= 4); // Al menos 4 estudiantes demo
    }
    
    @Test
    void crearEstudiante_debe_funcionar_correctamente() {
        boolean creado = controller.crearEstudiante(
            "TEST2024001",
            "Estudiante Test",
            "test.estudiante@email.com"
        );
        assertTrue(creado);
        
        // Verificar que se creó
        Estudiante estudiante = controller.obtenerEstudiantePorNumero("TEST2024001");
        assertNotNull(estudiante);
        assertEquals("Estudiante Test", estudiante.getNombre());
        assertEquals("active", estudiante.getStatus());
        
        // Limpiar
        controller.eliminarEstudiante(estudiante.getId());
    }
    
    @Test
    void crearEstudiante_duplicado_debe_fallar() {
        String numero = "TEST2024002";
        
        boolean primerCreacion = controller.crearEstudiante(numero, "Estudiante 1", "est1@email.com");
        assertTrue(primerCreacion);
        
        boolean segundaCreacion = controller.crearEstudiante(numero, "Estudiante 2", "est2@email.com");
        assertFalse(segundaCreacion);
        
        // Limpiar
        Estudiante estudiante = controller.obtenerEstudiantePorNumero(numero);
        if (estudiante != null) {
            controller.eliminarEstudiante(estudiante.getId());
        }
    }
    
    @Test
    void actualizarEstudiante_debe_modificar_correctamente() {
        // Crear estudiante
        controller.crearEstudiante("TEST2024003", "Original", "original@email.com");
        Estudiante estudiante = controller.obtenerEstudiantePorNumero("TEST2024003");
        assertNotNull(estudiante);
        
        // Actualizar
        boolean actualizado = controller.actualizarEstudiante(
            estudiante.getId(),
            "TEST2024003",
            "Actualizado",
            "actualizado@email.com",
            "inactive"
        );
        assertTrue(actualizado);
        
        // Verificar
        Estudiante estudianteActualizado = controller.obtenerEstudiantePorNumero("TEST2024003");
        assertEquals("Actualizado", estudianteActualizado.getNombre());
        assertEquals("actualizado@email.com", estudianteActualizado.getEmail());
        assertEquals("inactive", estudianteActualizado.getStatus());
        
        // Limpiar
        controller.eliminarEstudiante(estudiante.getId());
    }
    
    @Test
    void eliminarEstudiante_debe_funcionar() {
        controller.crearEstudiante("TEST2024004", "Para Eliminar", "eliminar@email.com");
        Estudiante estudiante = controller.obtenerEstudiantePorNumero("TEST2024004");
        assertNotNull(estudiante);
        
        boolean eliminado = controller.eliminarEstudiante(estudiante.getId());
        assertTrue(eliminado);
        
        Estudiante estudianteEliminado = controller.obtenerEstudiantePorNumero("TEST2024004");
        assertNull(estudianteEliminado);
    }
    
    @Test
    void buscarEstudiantes_debe_encontrar_por_nombre() {
        List<Estudiante> resultados = controller.buscarEstudiantes("Juan");
        assertFalse(resultados.isEmpty());
    }
    
    @Test
    void buscarEstudiantes_debe_encontrar_por_email() {
        List<Estudiante> resultados = controller.buscarEstudiantes("@email.com");
        assertFalse(resultados.isEmpty());
    }
    
    @Test
    void contarEstudiantesActivos_debe_retornar_numero_valido() {
        int activos = controller.contarEstudiantesActivos();
        assertTrue(activos >= 0);
    }
    
    @Test
    void contarEstudiantesInactivos_debe_retornar_numero_valido() {
        int inactivos = controller.contarEstudiantesInactivos();
        assertTrue(inactivos >= 0);
    }
    
    @Test
    void obtenerEstudiantePorId_con_id_invalido_debe_retornar_null() {
        Estudiante resultado = controller.obtenerEstudiantePorId("ID_INVALIDO_12345");
        assertNull(resultado);
    }
    
    @Test
    void obtenerEstudiantePorNumero_con_numero_invalido_debe_retornar_null() {
        Estudiante resultado = controller.obtenerEstudiantePorNumero("NUM_INVALIDO_12345");
        assertNull(resultado);
    }
    
    @Test
    void actualizarEstudiante_con_id_invalido_debe_fallar() {
        boolean resultado = controller.actualizarEstudiante(
            "ID_INVALIDO",
            "NUM001",
            "Nombre",
            "email@test.com",
            "active"
        );
        assertFalse(resultado);
    }
    
    @Test
    void eliminarEstudiante_con_id_invalido_debe_fallar() {
        boolean resultado = controller.eliminarEstudiante("ID_INVALIDO_12345");
        assertFalse(resultado);
    }
    
    @Test
    void buscarEstudiantes_con_termino_vacio_debe_retornar_todos() {
        List<Estudiante> resultados = controller.buscarEstudiantes("");
        assertNotNull(resultados);
    }
    
    @Test
    void actualizarEstudiante_con_numero_duplicado_debe_fallar() {
        // Crear dos estudiantes
        controller.crearEstudiante("TEST2024005", "Estudiante A", "a@email.com");
        controller.crearEstudiante("TEST2024006", "Estudiante B", "b@email.com");
        
        Estudiante estudianteA = controller.obtenerEstudiantePorNumero("TEST2024005");
        Estudiante estudianteB = controller.obtenerEstudiantePorNumero("TEST2024006");
        
        assertNotNull(estudianteA);
        assertNotNull(estudianteB);
        
        // Intentar cambiar número de B al mismo que A
        boolean resultado = controller.actualizarEstudiante(
            estudianteB.getId(),
            "TEST2024005", // Mismo número que A
            "Estudiante B Modificado",
            "b@email.com",
            "active"
        );
        
        assertFalse(resultado);
        
        // Limpiar
        controller.eliminarEstudiante(estudianteA.getId());
        controller.eliminarEstudiante(estudianteB.getId());
    }
    
    @AfterEach
    void tearDown() {
        // Limpiar estudiantes de prueba
        List<Estudiante> estudiantes = controller.obtenerTodosEstudiantes();
        for (Estudiante estudiante : estudiantes) {
            if (estudiante.getNumeroEstudiante().startsWith("TEST")) {
                controller.eliminarEstudiante(estudiante.getId());
            }
        }
    }
}