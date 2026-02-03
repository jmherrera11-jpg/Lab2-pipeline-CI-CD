package util;

import org.junit.jupiter.api.Test;
import controlador.CursoController;
import controlador.EstudianteController;
import controlador.ProfesorController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import modelo.Curso;
import modelo.Estudiante;
import modelo.Usuario;
import java.util.List;

@DisplayName("Tests completos de CRUD con MongoDB")
public class MongoDBFullCRUDTest {
    
    @Test
    @DisplayName("CRUD completo de Cursos")
    void testFullCRUDCursos() {
        CursoController controller = new CursoController();
        
        // 1. CREATE - Crear nuevo curso
        String codigoCurso = "TEST001";
        String nombreOriginal = "Curso de Prueba CRUD";
        String horarioOriginal = "Lun/Mie 14:00-15:30";
        String aulaOriginal = "Sala TEST";
        
        boolean creado = controller.crearCurso(
            codigoCurso,
            nombreOriginal,
            horarioOriginal,
            aulaOriginal,
            null
        );
        
        assertTrue(creado, "El curso debería crearse exitosamente");
        
        // 2. READ - Verificar que se creó correctamente
        Curso cursoCreado = controller.obtenerCursoPorCodigo(codigoCurso);
        assertNotNull(cursoCreado, "El curso creado debería existir");
        assertEquals(nombreOriginal, cursoCreado.getNombre());
        assertEquals(horarioOriginal, cursoCreado.getHorario());
        assertEquals(aulaOriginal, cursoCreado.getAula());
        
        // 3. UPDATE - Actualizar el curso
        String nombreActualizado = "Curso Actualizado CRUD";
        String horarioActualizado = "Mar/Jue 10:00-11:30";
        String aulaActualizada = "Sala TEST-ACT";
        
        boolean actualizado = controller.actualizarCurso(
            cursoCreado.getId(),
            codigoCurso,
            nombreActualizado,
            horarioActualizado,
            aulaActualizada,
            null
        );
        
        assertTrue(actualizado, "El curso debería actualizarse exitosamente");
        
        // Verificar actualización
        Curso cursoActualizado = controller.obtenerCursoPorCodigo(codigoCurso);
        assertNotNull(cursoActualizado);
        assertEquals(nombreActualizado, cursoActualizado.getNombre());
        assertEquals(horarioActualizado, cursoActualizado.getHorario());
        assertEquals(aulaActualizada, cursoActualizado.getAula());
        
        // 4. DELETE - Eliminar el curso
        boolean eliminado = controller.eliminarCurso(cursoCreado.getId());
        assertTrue(eliminado, "El curso debería eliminarse exitosamente");
        
        // Verificar eliminación
        Curso cursoEliminado = controller.obtenerCursoPorCodigo(codigoCurso);
        assertNull(cursoEliminado, "El curso eliminado no debería existir");
        
        // 5. LIST - Verificar que no aparece en la lista
        List<Curso> cursos = controller.obtenerTodosCursos();
        boolean encontrado = cursos.stream()
            .anyMatch(c -> c.getCodigo().equals(codigoCurso));
        assertFalse(encontrado, "El curso eliminado no debería aparecer en la lista");
    }
    
    @Test
    @DisplayName("CRUD completo de Estudiantes")
    void testFullCRUDEstudiantes() {
        EstudianteController controller = new EstudianteController();
        
        // 1. CREATE
        String numeroEstudiante = "TEST2024001";
        String nombreOriginal = "Estudiante Test CRUD";
        String emailOriginal = "test.crud@email.com";
        
        boolean creado = controller.crearEstudiante(
            numeroEstudiante,
            nombreOriginal,
            emailOriginal
        );
        
        assertTrue(creado, "El estudiante debería crearse exitosamente");
        
        // 2. READ
        Estudiante estudianteCreado = controller.obtenerEstudiantePorNumero(numeroEstudiante);
        assertNotNull(estudianteCreado);
        assertEquals(nombreOriginal, estudianteCreado.getNombre());
        assertEquals(emailOriginal, estudianteCreado.getEmail());
        assertEquals("active", estudianteCreado.getStatus());
        
        // 3. UPDATE
        String nombreActualizado = "Estudiante Actualizado CRUD";
        String emailActualizado = "actualizado.crud@email.com";
        String statusActualizado = "inactive";
        
        boolean actualizado = controller.actualizarEstudiante(
            estudianteCreado.getId(),
            numeroEstudiante,
            nombreActualizado,
            emailActualizado,
            statusActualizado
        );
        
        assertTrue(actualizado, "El estudiante debería actualizarse exitosamente");
        
        // Verificar actualización
        Estudiante estudianteActualizado = controller.obtenerEstudiantePorNumero(numeroEstudiante);
        assertNotNull(estudianteActualizado);
        assertEquals(nombreActualizado, estudianteActualizado.getNombre());
        assertEquals(emailActualizado, estudianteActualizado.getEmail());
        assertEquals(statusActualizado, estudianteActualizado.getStatus());
        
        // 4. DELETE
        boolean eliminado = controller.eliminarEstudiante(estudianteCreado.getId());
        assertTrue(eliminado, "El estudiante debería eliminarse exitosamente");
        
        // Verificar eliminación
        Estudiante estudianteEliminado = controller.obtenerEstudiantePorNumero(numeroEstudiante);
        assertNull(estudianteEliminado);
    }
    
    @Test
    @DisplayName("CRUD completo de Profesores")
    void testFullCRUDProfesores() {
        ProfesorController controller = new ProfesorController();
        
        // 1. CREATE
        String nombreOriginal = "Profesor Test CRUD";
        String emailOriginal = "profesor.crud@academico.edu";
        
        boolean creado = controller.crearProfesor(nombreOriginal, emailOriginal);
        assertTrue(creado, "El profesor debería crearse exitosamente");
        
        // 2. READ (buscando por nombre)
        List<Usuario> profesores = controller.buscarProfesores("Test CRUD");
        assertFalse(profesores.isEmpty());
        
        Usuario profesorCreado = profesores.stream()
            .filter(p -> p.getEmail().equals(emailOriginal))
            .findFirst()
            .orElse(null);
            
        assertNotNull(profesorCreado);
        assertEquals(nombreOriginal, profesorCreado.getNombre());
        assertEquals("profesor", profesorCreado.getRol());
        
        // 3. UPDATE
        String nombreActualizado = "Profesor Actualizado CRUD";
        String emailActualizado = "actualizado.crud@academico.edu";
        
        boolean actualizado = controller.actualizarProfesor(
            profesorCreado.getId(),
            nombreActualizado,
            emailActualizado
        );
        
        assertTrue(actualizado, "El profesor debería actualizarse exitosamente");
        
        // Verificar actualización buscando por nombre actualizado
        List<Usuario> profesoresActualizados = controller.buscarProfesores("Actualizado CRUD");
        assertFalse(profesoresActualizados.isEmpty());
        
        boolean encontrado = profesoresActualizados.stream()
            .anyMatch(p -> p.getEmail().equals(emailActualizado));
        assertTrue(encontrado, "El profesor actualizado debería encontrarse");
        
        // 4. DELETE
        boolean eliminado = controller.eliminarProfesor(profesorCreado.getId());
        assertTrue(eliminado, "El profesor debería eliminarse exitosamente");
        
        // Verificar eliminación
        List<Usuario> profesoresDespues = controller.buscarProfesores("CRUD");
        assertTrue(profesoresDespues.isEmpty(), "El profesor eliminado no debería encontrarse");
    }
    
    @Test
    @DisplayName("Integración: Curso con profesor asignado")
    void testCursoConProfesor() {
        CursoController cursoController = new CursoController();
        ProfesorController profesorController = new ProfesorController();
        
        // 1. Crear profesor
        String profesorEmail = "profesor.integracion@academico.edu";
        boolean profesorCreado = profesorController.crearProfesor(
            "Prof. Integración", 
            profesorEmail
        );
        assertTrue(profesorCreado);
        
        // Obtener ID del profesor
        List<Usuario> profesores = profesorController.buscarProfesores("Integración");
        assertFalse(profesores.isEmpty());
        Usuario profesor = profesores.get(0);
        assertNotNull(profesor);
        
        // 2. Crear curso asignado al profesor
        boolean cursoCreado = cursoController.crearCurso(
            "INT001",
            "Curso Integración Test",
            "Horario Test",
            "Aula Test",
            profesor.getId()
        );
        assertTrue(cursoCreado);
        
        // 3. Verificar que el curso tiene el profesor asignado
        Curso curso = cursoController.obtenerCursoPorCodigo("INT001");
        assertNotNull(curso);
        assertEquals(profesor.getId(), curso.getProfesorId());
        
        // 4. Verificar información del profesor en el curso
        Usuario profesorAsignado = profesorController.obtenerProfesorPorId(curso.getProfesorId());
        assertNotNull(profesorAsignado);
        assertEquals("Prof. Integración", profesorAsignado.getNombre());
        
        // 5. Limpiar
        cursoController.eliminarCurso(curso.getId());
        profesorController.eliminarProfesor(profesor.getId());
    }
    
    @Test
    @DisplayName("Búsquedas avanzadas con MongoDB")
    void testBusquedasAvanzadas() {
        EstudianteController estudianteController = new EstudianteController();
        CursoController cursoController = new CursoController();
        
        // 1. Búsqueda por nombre (debe encontrar estudiantes demo)
        List<Estudiante> estudiantes = estudianteController.buscarEstudiantes("Juan");
        assertFalse(estudiantes.isEmpty());
        
        boolean encontrado = estudiantes.stream()
            .anyMatch(e -> e.getNombre().contains("Juan"));
        assertTrue(encontrado);
        
        // 2. Búsqueda por email (debe encontrar estudiantes demo)
        List<Estudiante> estudiantesEmail = estudianteController.buscarEstudiantes("juan@email.com");
        assertFalse(estudiantesEmail.isEmpty());
        
        // 3. Búsqueda en cursos por nombre
        List<Curso> cursos = cursoController.buscarCursos("Sistemas");
        assertFalse(cursos.isEmpty());
        
        boolean salaEncontrada = cursos.stream()
            .anyMatch(c -> c.getNombre().contains("Sistemas"));
        assertTrue(salaEncontrada);
        
        // 4. Búsqueda de cursos por código
        List<Curso> cursosCodigo = cursoController.buscarCursos("CS101");
        assertFalse(cursosCodigo.isEmpty());
    }
    
    @Test
    @DisplayName("Validaciones de unicidad")
    void testValidacionesUnicidad() {
        CursoController cursoController = new CursoController();
        EstudianteController estudianteController = new EstudianteController();
        ProfesorController profesorController = new ProfesorController();
        
        // 1. Código de curso único
        String codigoUnico = "UNICO001";
        boolean curso1 = cursoController.crearCurso(codigoUnico, "Curso 1", "H1", "A1", null);
        assertTrue(curso1);
        
        boolean curso2 = cursoController.crearCurso(codigoUnico, "Curso 2", "H2", "A2", null);
        assertFalse(curso2, "No debería permitir código de curso duplicado");
        
        // Limpiar
        Curso curso = cursoController.obtenerCursoPorCodigo(codigoUnico);
        if (curso != null) {
            cursoController.eliminarCurso(curso.getId());
        }
        
        // 2. Número de estudiante único
        String numeroUnico = "2024UNICO";
        boolean estudiante1 = estudianteController.crearEstudiante(numeroUnico, "Estudiante 1", "e1@email.com");
        assertTrue(estudiante1);
        
        boolean estudiante2 = estudianteController.crearEstudiante(
            numeroUnico, "Estudiante 2", "e2@email.com"
        );
        assertFalse(estudiante2, "No debería permitir número de estudiante duplicado");
        
        // Limpiar
        Estudiante estudiante = estudianteController.obtenerEstudiantePorNumero(numeroUnico);
        if (estudiante != null) {
            estudianteController.eliminarEstudiante(estudiante.getId());
        }
        
        // 3. Email de profesor único
        String emailUnico = "prof.unico@academico.edu";
        boolean profesor1 = profesorController.crearProfesor("Profesor 1", emailUnico);
        assertTrue(profesor1);
        
        boolean profesor2 = profesorController.crearProfesor("Profesor 2", emailUnico);
        assertFalse(profesor2, "No debería permitir email de profesor duplicado");
        
        // Limpiar
        List<Usuario> profesores = profesorController.buscarProfesores("unico");
        if (!profesores.isEmpty()) {
            for (Usuario p : profesores) {
                profesorController.eliminarProfesor(p.getId());
            }
        }
    }
    
    @AfterEach
    void limpiarDatosTest() {
        // Limpiar cursos de prueba
        CursoController cursoController = new CursoController();
        List<Curso> cursos = cursoController.obtenerTodosCursos();
        for (Curso curso : cursos) {
            if (curso.getCodigo().startsWith("TEST") || 
                curso.getCodigo().startsWith("INT") ||
                curso.getCodigo().startsWith("UNICO")) {
                cursoController.eliminarCurso(curso.getId());
            }
        }
        
        // Limpiar estudiantes de prueba
        EstudianteController estudianteController = new EstudianteController();
        List<Estudiante> estudiantes = estudianteController.obtenerTodosEstudiantes();
        for (Estudiante estudiante : estudiantes) {
            if (estudiante.getNumeroEstudiante().startsWith("TEST") ||
                estudiante.getNumeroEstudiante().startsWith("2024UNICO")) {
                estudianteController.eliminarEstudiante(estudiante.getId());
            }
        }
        
        // Limpiar profesores de prueba
        ProfesorController profesorController = new ProfesorController();
        List<Usuario> profesores = profesorController.obtenerTodosProfesores();
        for (Usuario profesor : profesores) {
            if (profesor.getEmail().contains("crud") ||
                profesor.getEmail().contains("integracion") ||
                profesor.getEmail().contains("unico")) {
                profesorController.eliminarProfesor(profesor.getId());
            }
        }
    }
}