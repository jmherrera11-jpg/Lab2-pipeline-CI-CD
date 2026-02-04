package util;

import org.junit.jupiter.api.Test;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import controlador.CursoController;
import controlador.EstudianteController;
import controlador.ProfesorController;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;

import modelo.Curso;
import modelo.Estudiante;
import modelo.Usuario;

import java.util.List;

public class MongoDBTest {
    private CursoController cursoController;
    private EstudianteController estudianteController;
    private ProfesorController profesorController;
    
    @BeforeEach
    void setUp() {
        cursoController = new CursoController();
        estudianteController = new EstudianteController();
        profesorController = new ProfesorController();
    }
    
    @Test
    void testCursoCRUD() {
        // CREATE
        boolean creado = cursoController.crearCurso(
            "CS105", 
            "Nuevo Curso Test", 
            "Lun/Vie 10:00-11:30", 
            "Sala 105", 
            null
        );
        assertTrue(creado);
        
        // READ
        Curso curso = cursoController.obtenerCursoPorCodigo("CS105");
        assertNotNull(curso);
        assertEquals("Nuevo Curso Test", curso.getNombre());
        
        // UPDATE
        boolean actualizado = cursoController.actualizarCurso(
            curso.getId(),
            "CS105",
            "Curso Actualizado",
            "Mar/Jue 14:00-15:30",
            "Sala 205",
            null
        );
        assertTrue(actualizado);
        
        // Verificar actualización
        Curso cursoActualizado = cursoController.obtenerCursoPorCodigo("CS105");
        assertEquals("Curso Actualizado", cursoActualizado.getNombre());
        assertEquals("Sala 205", cursoActualizado.getAula());
        
        // DELETE
        boolean eliminado = cursoController.eliminarCurso(curso.getId());
        assertTrue(eliminado);
        
        // Verificar eliminación
        Curso cursoEliminado = cursoController.obtenerCursoPorCodigo("CS105");
        assertNull(cursoEliminado);
    }
    
    @Test
    void testEstudianteCRUD() {
        // CREATE
        boolean creado = estudianteController.crearEstudiante(
            "2023101",
            "Test Estudiante",
            "test@email.com"
        );
        assertTrue(creado);
        
        // READ
        Estudiante estudiante = estudianteController.obtenerEstudiantePorNumero("2023101");
        assertNotNull(estudiante);
        assertEquals("Test Estudiante", estudiante.getNombre());
        assertEquals("active", estudiante.getStatus());
        
        // UPDATE
        boolean actualizado = estudianteController.actualizarEstudiante(
            estudiante.getId(),
            "2023101",
            "Estudiante Actualizado",
            "actualizado@email.com",
            "inactive"
        );
        assertTrue(actualizado);
        
        // Verificar actualización
        Estudiante estudianteActualizado = estudianteController.obtenerEstudiantePorNumero("2023101");
        assertEquals("Estudiante Actualizado", estudianteActualizado.getNombre());
        assertEquals("inactive", estudianteActualizado.getStatus());
        
        // DELETE
        boolean eliminado = estudianteController.eliminarEstudiante(estudiante.getId());
        assertTrue(eliminado);
        
        // Verificar eliminación
        Estudiante estudianteEliminado = estudianteController.obtenerEstudiantePorNumero("2023101");
        assertNull(estudianteEliminado);
    }
    
    @Test
    void testProfesorCRUD() {
        // CREATE
        boolean creado = profesorController.crearProfesor(
            "Prof. Test",
            "test.prof@academico.edu"
        );
        assertTrue(creado);
        
        // READ
        List<Usuario> profesores = profesorController.buscarProfesores("Test");
        assertFalse(profesores.isEmpty());
        
        Usuario profesor = profesores.get(0);
        assertEquals("Prof. Test", profesor.getNombre());
        
        // UPDATE
        boolean actualizado = profesorController.actualizarProfesor(
            profesor.getId(),
            "Prof. Test Actualizado",
            "test.actualizado@academico.edu"
        );
        assertTrue(actualizado);
        
        // Verificar actualización
        List<Usuario> profesoresActualizados = profesorController.buscarProfesores("Actualizado");
        assertFalse(profesoresActualizados.isEmpty());
        assertEquals("test.actualizado@academico.edu", profesoresActualizados.get(0).getEmail());
        
        // DELETE
        boolean eliminado = profesorController.eliminarProfesor(profesor.getId());
        assertTrue(eliminado);
    }
    
    @Test
    void testBusquedaCursos() {
        List<Curso> cursos = cursoController.buscarCursos("Sistemas");
        assertFalse(cursos.isEmpty());
        
        // Verificar que contiene el curso esperado
        boolean encontrado = cursos.stream()
            .anyMatch(c -> c.getNombre().contains("Sistemas"));
        assertTrue(encontrado);
    }
    
    @Test
    void testBusquedaEstudiantes() {
        List<Estudiante> estudiantes = estudianteController.buscarEstudiantes("Juan");
        assertFalse(estudiantes.isEmpty());
        
        boolean encontrado = estudiantes.stream()
            .anyMatch(e -> e.getNombre().contains("Juan"));
        assertTrue(encontrado);
    }
    
    @Test
    void testContadorEstudiantes() {
        int activos = estudianteController.contarEstudiantesActivos();
        int inactivos = estudianteController.contarEstudiantesInactivos();
        
        assertTrue(activos >= 0);
        assertTrue(inactivos >= 0);
    }
    
    @Test
    void testCursoDuplicado() {
        // Intentar crear curso con código duplicado
        boolean primerCreacion = cursoController.crearCurso(
            "CS106",
            "Curso Test",
            "Horario Test",
            "Aula Test",
            null
        );
        assertTrue(primerCreacion);
        
        // Intentar crear otro curso con el mismo código
        boolean segundaCreacion = cursoController.crearCurso(
            "CS106",
            "Otro Curso",
            "Otro Horario",
            "Otra Aula",
            null
        );
        assertFalse(segundaCreacion);
        
        // Limpiar
        Curso curso = cursoController.obtenerCursoPorCodigo("CS106");
        if (curso != null) {
            cursoController.eliminarCurso(curso.getId());
        }
    }
    
    @Test
    void testEstudianteDuplicado() {
        // Intentar crear estudiante con número duplicado
        boolean primerCreacion = estudianteController.crearEstudiante(
            "2023102",
            "Estudiante Test",
            "test1@email.com"
        );
        assertTrue(primerCreacion);
        
        // Intentar crear otro estudiante con el mismo número
        boolean segundaCreacion = estudianteController.crearEstudiante(
            "2023102",
            "Otro Estudiante",
            "test2@email.com"
        );
        assertFalse(segundaCreacion);
        
        // Limpiar
        Estudiante estudiante = estudianteController.obtenerEstudiantePorNumero("2023102");
        if (estudiante != null) {
            estudianteController.eliminarEstudiante(estudiante.getId());
        }
    }
    
 // En MongoDBTest.java y MongoDBFullCRUDTest.java
    @AfterEach
    void tearDown() {
        // Asegurar limpieza de datos de prueba
        MongoDatabase database = MongoDBConnection.getInstance().getDatabase();
        
        // Limpiar colecciones de prueba específicas
        database.getCollection("cursos").deleteMany(Filters.or(
            Filters.regex("codigo", "^TEST", "i"),
            Filters.regex("codigo", "^CS10[4-9]")
        ));
        
        database.getCollection("estudiantes").deleteMany(Filters.or(
            Filters.regex("numeroEstudiante", "^TEST", "i"),
            Filters.regex("numeroEstudiante", "^20231[0-9]{2}")
        ));
        
        database.getCollection("profesores").deleteMany(Filters.or(
            Filters.regex("email", "test\\.", "i"),
            Filters.regex("email", "crud@", "i")
        ));
    }
    
    @Test
    void getInstance_debe_retornar_singleton() {
        MongoDBConnection instance1 = MongoDBConnection.getInstance();
        MongoDBConnection instance2 = MongoDBConnection.getInstance();
        
        assertNotNull(instance1);
        assertNotNull(instance2);
        assertSame(instance1, instance2, "Debería ser la misma instancia (singleton)");
    }
    
    @Test
    void getDatabase_debe_retornar_database_valido() {
        MongoDBConnection connection = MongoDBConnection.getInstance();
        MongoDatabase database = connection.getDatabase();
        
        assertNotNull(database);
        assertEquals("attendance_system", database.getName());
    }
    
    @Test
    void close_no_debe_lanzar_excepcion() {
        MongoDBConnection connection = MongoDBConnection.getInstance();
        
        // close() debería funcionar sin excepciones
        assertDoesNotThrow(() -> connection.close());
        
        // Reestablecer instancia para otros tests
        try {
            var field = MongoDBConnection.class.getDeclaredField("instance");
            field.setAccessible(true);
            field.set(null, null);
        } catch (Exception e) {
            // Ignorar
        }
    }
    
 // Verificar que MongoDB está corriendo antes de los tests
    @BeforeAll
    static void checkMongoDB() {
        try {
            MongoDBConnection.getInstance();
            System.out.println("MongoDB conectado para tests");
        } catch (Exception e) {
            System.err.println("MongoDB no disponible para tests: " + e.getMessage());
            // Opcional: saltar tests si MongoDB no está disponible
        }
    }
}