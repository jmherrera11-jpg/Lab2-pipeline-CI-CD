package controlador;

import org.junit.jupiter.api.Test;

import modelo.Estudiante;

import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.HashMap;
import java.util.Map;

public class AsistenciaControllerTest {
    private AsistenciaController controller;
    
    @BeforeEach
    void setUp() {
        controller = new AsistenciaController();
    }
    
    @Test
    void obtenerTodosEstudiantes_debe_retornar_lista_no_vacia() {
        var estudiantes = controller.obtenerTodosEstudiantes();
        assertFalse(estudiantes.isEmpty());
        assertTrue(estudiantes.size() >= 4); // Debe tener al menos 4 estudiantes demo
    }
    
    @Test
    void registrarAsistencia_debe_guardar_correctamente() {
        Map<String, String> asistencias = new HashMap<>();
        
        // Obtener IDs de estudiantes reales
        var estudiantes = controller.obtenerTodosEstudiantes();
        assertFalse(estudiantes.isEmpty());
        
        String estudianteId1 = estudiantes.get(0).getId();
        String estudianteId2 = estudiantes.get(1).getId();
        String estudianteId3 = estudiantes.get(2).getId();
        
        asistencias.put(estudianteId1, "present");
        asistencias.put(estudianteId2, "late");
        asistencias.put(estudianteId3, "absent");
        
        controller.registrarAsistencia("CS101", asistencias);
        
        var resultado = controller.obtenerAsistenciaPorCurso("CS101");
        assertNotNull(resultado);
        // Verificar que contiene las claves
        assertTrue(resultado.containsKey(estudianteId1) || 
                   resultado.containsKey(estudianteId2) || 
                   resultado.containsKey(estudianteId3));
    }
    
    @Test
    void calcularPorcentajeAsistencia_debe_ser_cero_sin_registros() {
        var estudiantes = controller.obtenerTodosEstudiantes();
        assertFalse(estudiantes.isEmpty());
        
        // Crear un nuevo estudiante para asegurar que no tiene asistencias
        EstudianteController estudianteController = new EstudianteController();
        estudianteController.crearEstudiante("TEST001", "Test Sin Asistencia", "test@email.com");
        Estudiante nuevoEstudiante = estudianteController.obtenerEstudiantePorNumero("TEST001");
        assertNotNull(nuevoEstudiante);
        
        double porcentaje = controller.calcularPorcentajeAsistencia(nuevoEstudiante.getId());
        assertEquals(0.0, porcentaje, 0.001);
        
        // Limpiar
        estudianteController.eliminarEstudiante(nuevoEstudiante.getId());
    }
    
    @Test
    void calcularPorcentajeAsistencia_debe_calcular_correctamente() {
        // Primero crear un estudiante específico para este test
        EstudianteController estudianteController = new EstudianteController();
        estudianteController.crearEstudiante("TEST002", "Test Calculo", "test2@email.com");
        Estudiante estudianteTest = estudianteController.obtenerEstudiantePorNumero("TEST002");
        assertNotNull(estudianteTest);
        
        String estudianteId = estudianteTest.getId();
        
        // Registrar asistencias solo para este estudiante
        Map<String, String> asistencias1 = new HashMap<>();
        asistencias1.put(estudianteId, "present");
        controller.registrarAsistencia("CS101", asistencias1);
        
        Map<String, String> asistencias2 = new HashMap<>();
        asistencias2.put(estudianteId, "present");
        controller.registrarAsistencia("CS101", asistencias2);
        
        Map<String, String> asistencias3 = new HashMap<>();
        asistencias3.put(estudianteId, "absent");
        controller.registrarAsistencia("CS101", asistencias3);
        
        double porcentaje = controller.calcularPorcentajeAsistencia(estudianteId);
        
        // 2 de 3 = 66.666...%
        // Verificar con un rango aceptable
        assertTrue(porcentaje >= 66.0 && porcentaje <= 67.0, 
            "Porcentaje debe ser aproximadamente 66.66%, pero fue: " + porcentaje);
        
        // Limpiar
        estudianteController.eliminarEstudiante(estudianteId);
    }
    
    @Test
    void obtenerAsistenciaPorCurso_debe_retornar_vacio_para_curso_inexistente() {
        var resultado = controller.obtenerAsistenciaPorCurso("CURSO_INEXISTENTE_12345");
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }
}