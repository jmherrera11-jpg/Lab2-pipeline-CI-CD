package vista;

import org.junit.jupiter.api.Test;
import controlador.CursoController;
import controlador.EstudianteController;
import controlador.ProfesorController;
import controlador.ReporteController;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import modelo.Curso;
import modelo.Estudiante;
import modelo.Usuario;
import java.util.List;

@DisplayName("Tests para funcionalidades CRUD del Dashboard del Coordinador")
public class CoordinatorCRUDViewsTest {
    
    @Test
    @DisplayName("Dashboard del Coordinador - Gestión completa de cursos")
    void testDashboardCursosManagement() {
        CursoController cursoController = new CursoController();
        ProfesorController profesorController = new ProfesorController();
        
        // Simular operaciones que haría el coordinador desde el dashboard
        // 1. Verificar que hay cursos iniciales
        List<Curso> cursosIniciales = cursoController.obtenerTodosCursos();
        assertTrue(cursosIniciales.size() >= 3, "Debería haber cursos iniciales");
        
        // 2. Crear nuevo curso desde el dashboard
        String nuevoCodigo = "DASH001";
        boolean cursoCreado = cursoController.crearCurso(
            nuevoCodigo,
            "Curso Dashboard Test",
            "Lun/Vie 08:00-09:30",
            "Sala Dashboard",
            null
        );
        assertTrue(cursoCreado, "El coordinador debería poder crear cursos");
        
        // 3. Verificar que aparece en la lista
        List<Curso> cursosDespues = cursoController.obtenerTodosCursos();
        boolean encontrado = cursosDespues.stream()
            .anyMatch(c -> c.getCodigo().equals(nuevoCodigo));
        assertTrue(encontrado, "El nuevo curso debería aparecer en la lista");
        
        // 4. Buscar el curso creado
        List<Curso> resultadosBusqueda = cursoController.buscarCursos("Dashboard");
        assertFalse(resultadosBusqueda.isEmpty());
        
        // 5. Asignar profesor al curso
        Curso curso = cursoController.obtenerCursoPorCodigo(nuevoCodigo);
        assertNotNull(curso);
        
        // Obtener un profesor existente
        List<Usuario> profesores = profesorController.obtenerTodosProfesores();
        assertFalse(profesores.isEmpty());
        
        String profesorId = profesores.get(0).getId();
        boolean asignado = cursoController.actualizarCurso(
            curso.getId(),
            curso.getCodigo(),
            curso.getNombre(),
            curso.getHorario(),
            curso.getAula(),
            profesorId
        );
        assertTrue(asignado, "Debería poder asignar profesor al curso");
        
        // 6. Eliminar el curso (acción del coordinador)
        boolean eliminado = cursoController.eliminarCurso(curso.getId());
        assertTrue(eliminado, "El coordinador debería poder eliminar cursos");
        
        // 7. Verificar que se eliminó
        Curso cursoEliminado = cursoController.obtenerCursoPorCodigo(nuevoCodigo);
        assertNull(cursoEliminado);
    }
    
    @Test
    @DisplayName("Dashboard del Coordinador - Gestión de estudiantes")
    void testDashboardEstudiantesManagement() {
        EstudianteController estudianteController = new EstudianteController();
        
        // Simular operaciones del dashboard
        // 1. Verificar que hay estudiantes iniciales
        List<Estudiante> estudiantesIniciales = estudianteController.obtenerTodosEstudiantes();
        assertFalse(estudiantesIniciales.isEmpty());
        
        // 2. Crear nuevo estudiante
        String nuevoNumero = "DASH2024001";
        boolean estudianteCreado = estudianteController.crearEstudiante(
            nuevoNumero,
            "Estudiante Dashboard Test",
            "dashboard.test@email.com"
        );
        assertTrue(estudianteCreado);
        
        // 3. Buscar estudiante
        List<Estudiante> resultados = estudianteController.buscarEstudiantes("Dashboard");
        assertFalse(resultados.isEmpty());
        
        Estudiante estudiante = resultados.get(0);
        assertEquals("active", estudiante.getStatus());
        
        // 4. Cambiar estado a inactive (acción del coordinador)
        boolean actualizado = estudianteController.actualizarEstudiante(
            estudiante.getId(),
            estudiante.getNumeroEstudiante(),
            estudiante.getNombre(),
            estudiante.getEmail(),
            "inactive"
        );
        assertTrue(actualizado);
        
        // 5. Verificar cambio
        Estudiante estudianteActualizado = estudianteController.obtenerEstudiantePorNumero(nuevoNumero);
        assertNotNull(estudianteActualizado);
        assertEquals("inactive", estudianteActualizado.getStatus());
        
        // 6. Eliminar estudiante
        boolean eliminado = estudianteController.eliminarEstudiante(estudiante.getId());
        assertTrue(eliminado);
        
        // 7. Verificar eliminación
        Estudiante estudianteEliminado = estudianteController.obtenerEstudiantePorNumero(nuevoNumero);
        assertNull(estudianteEliminado);
    }
    
    @Test
    @DisplayName("Dashboard del Coordinador - Reportes integrados")
    void testDashboardReports() {
        ReporteController reporteController = new ReporteController();
        
        // 1. Generar reporte general
        var reporteGeneral = reporteController.generarReporteGeneral();
        assertNotNull(reporteGeneral);
        
        // Verificar que el reporte tiene las claves esperadas
        assertTrue(reporteGeneral.containsKey("totalEstudiantes"));
        assertTrue(reporteGeneral.containsKey("estudiantesActivos"));
        assertTrue(reporteGeneral.containsKey("estudiantesInactivos"));
        assertTrue(reporteGeneral.containsKey("promedioAsistencia"));
        
        // 2. Generar reporte de estudiantes
        var reporteEstudiantes = reporteController.generarReporteEstudiantes();
        assertNotNull(reporteEstudiantes);
        
        // 3. Generar reporte de curso específico (si hay cursos)
        CursoController cursoController = new CursoController();
        List<Curso> cursos = cursoController.obtenerTodosCursos();
        if (!cursos.isEmpty()) {
            Curso primerCurso = cursos.get(0);
            var reporteCurso = reporteController.generarReporteCurso(primerCurso.getId());
            
            assertNotNull(reporteCurso);
            assertTrue(reporteCurso.containsKey("curso"));
        }
    }
    
    @Test
    @DisplayName("Dashboard del Coordinador - Funcionalidades de búsqueda")
    void testDashboardSearchFunctionality() {
        CursoController cursoController = new CursoController();
        EstudianteController estudianteController = new EstudianteController();
        ProfesorController profesorController = new ProfesorController();
        
        // 1. Búsqueda en cursos (simulando búsqueda desde dashboard)
        List<Curso> cursosPorNombre = cursoController.buscarCursos("Sistemas");
        assertFalse(cursosPorNombre.isEmpty());
        
        List<Curso> cursosPorCodigo = cursoController.buscarCursos("CS101");
        assertFalse(cursosPorCodigo.isEmpty());
        
        // 2. Búsqueda en estudiantes
        List<Estudiante> estudiantesPorNombre = estudianteController.buscarEstudiantes("Juan");
        assertFalse(estudiantesPorNombre.isEmpty());
        
        // 3. Búsqueda en profesores
        List<Usuario> profesores = profesorController.obtenerTodosProfesores();
        assertFalse(profesores.isEmpty());
        
        // Buscar por parte del nombre
        if (!profesores.isEmpty()) {
            String nombreBusqueda = profesores.get(0).getNombre().split(" ")[0]; // Primer nombre
            List<Usuario> profesoresPorNombre = profesorController.buscarProfesores(nombreBusqueda);
            assertFalse(profesoresPorNombre.isEmpty());
        }
    }
}