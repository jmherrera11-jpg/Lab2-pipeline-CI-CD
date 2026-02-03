package controlador;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class CursoControllerTest {
    private CursoController controller;
    
    @BeforeEach
    void setUp() {
        controller = new CursoController();
    }
    
    @Test
    void obtenerTodosCursos_debe_retornar_lista_no_vacia() {
        var cursos = controller.obtenerTodosCursos();
        assertFalse(cursos.isEmpty());
        assertTrue(cursos.size() >= 3); // Debe tener al menos los 3 cursos demo
    }
    
    @Test
    void obtenerCursoPorCodigo_debe_retornar_curso_correcto() {
        var curso = controller.obtenerCursoPorCodigo("CS101");
        assertNotNull(curso);
        assertEquals("Sistemas de Base de Datos", curso.getNombre());
        assertEquals("Sala 101", curso.getAula());
    }
    
    @Test
    void obtenerCursoPorCodigo_debe_retornar_null_si_no_existe() {
        var curso = controller.obtenerCursoPorCodigo("INVALID");
        assertNull(curso);
    }
    
    @Test
    void crearCurso_debe_incrementar_lista() {
        int initialSize = controller.obtenerTodosCursos().size();
        
        boolean creado = controller.crearCurso(
            "CS104", 
            "Nuevo Curso Test", 
            "Lun/Mie 10:00-11:30", 
            "Sala 104", 
            null
        );
        
        assertTrue(creado);
        assertEquals(initialSize + 1, controller.obtenerTodosCursos().size());
        
        // Verificar que el curso se creó
        var curso = controller.obtenerCursoPorCodigo("CS104");
        assertNotNull(curso);
    }
    
    @Test
    void crearCurso_duplicado_debe_fallar() {
        // Crear primer curso
        boolean primerCreacion = controller.crearCurso(
            "CS105",
            "Curso Test 1",
            "Horario 1",
            "Aula 1",
            null
        );
        assertTrue(primerCreacion);
        
        // Intentar crear curso con mismo código
        boolean segundaCreacion = controller.crearCurso(
            "CS105",
            "Curso Test 2",
            "Horario 2",
            "Aula 2",
            null
        );
        assertFalse(segundaCreacion);
    }
    
    @Test
    void actualizarCurso_debe_modificar_correctamente() {
        // Primero crear un curso para actualizar
        controller.crearCurso("CS106", "Curso Original", "Horario", "Aula", null);
        var curso = controller.obtenerCursoPorCodigo("CS106");
        assertNotNull(curso);
        
        // Actualizar curso
        boolean actualizado = controller.actualizarCurso(
            curso.getId(),
            "CS106",
            "Curso Actualizado",
            "Nuevo Horario",
            "Nueva Aula",
            "P001"
        );
        
        assertTrue(actualizado);
        
        // Verificar cambios
        var cursoActualizado = controller.obtenerCursoPorCodigo("CS106");
        assertNotNull(cursoActualizado);
        assertEquals("Curso Actualizado", cursoActualizado.getNombre());
        assertEquals("Nuevo Horario", cursoActualizado.getHorario());
        assertEquals("Nueva Aula", cursoActualizado.getAula());
        assertEquals("P001", cursoActualizado.getProfesorId());
    }
    
    @Test
    void eliminarCurso_debe_remover_de_lista() {
        // Crear curso para eliminar
        controller.crearCurso("CS107", "Curso a Eliminar", "Horario", "Aula", null);
        var curso = controller.obtenerCursoPorCodigo("CS107");
        assertNotNull(curso);
        
        int sizeBefore = controller.obtenerTodosCursos().size();
        
        // Eliminar curso
        boolean eliminado = controller.eliminarCurso(curso.getId());
        assertTrue(eliminado);
        
        // Verificar que ya no existe
        var cursoEliminado = controller.obtenerCursoPorCodigo("CS107");
        assertNull(cursoEliminado);
        
        int sizeAfter = controller.obtenerTodosCursos().size();
        assertEquals(sizeBefore - 1, sizeAfter);
    }
    
    @Test
    void buscarCursos_debe_encontrar_por_nombre() {
        var resultados = controller.buscarCursos("Sistemas");
        assertFalse(resultados.isEmpty());
        
        boolean encontrado = resultados.stream()
            .anyMatch(c -> c.getNombre().contains("Sistemas"));
        assertTrue(encontrado);
    }
    
    @Test
    void buscarCursos_debe_encontrar_por_codigo() {
        var resultados = controller.buscarCursos("CS101");
        assertFalse(resultados.isEmpty());
        
        boolean encontrado = resultados.stream()
            .anyMatch(c -> c.getCodigo().equals("CS101"));
        assertTrue(encontrado);
    }
    
    @Test
    void buscarCursos_debe_encontrar_por_horario() {
        var resultados = controller.buscarCursos("Lun/Mie");
        assertFalse(resultados.isEmpty());
        
        boolean encontrado = resultados.stream()
            .anyMatch(c -> c.getHorario().contains("Lun/Mie"));
        assertTrue(encontrado);
    }
    
    @AfterEach
    void tearDown() {
        // Limpiar solo los cursos de prueba que creamos en los tests
        limpiarCursosDePrueba();
    }
    
    private void limpiarCursosDePrueba() {
        List<String> codigosPrueba = List.of("CS104", "CS105", "CS106", "CS107");
        
        for (String codigo : codigosPrueba) {
            var curso = controller.obtenerCursoPorCodigo(codigo);
            if (curso != null) {
                controller.eliminarCurso(curso.getId());
            }
        }
    }
}