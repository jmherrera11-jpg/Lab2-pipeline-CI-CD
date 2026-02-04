package controlador;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Map;
import java.util.List;

public class ReporteControllerTest {
    private ReporteController controller;
    
    @BeforeEach
    void setUp() {
        controller = new ReporteController();
    }
    
    @Test
    void generarReporteGeneral_debe_retornar_estructura_completa() {
        Map<String, Object> reporte = controller.generarReporteGeneral();
        
        assertNotNull(reporte);
        assertTrue(reporte.containsKey("totalEstudiantes"));
        assertTrue(reporte.containsKey("estudiantesActivos"));
        assertTrue(reporte.containsKey("estudiantesInactivos"));
        assertTrue(reporte.containsKey("promedioAsistencia"));
        assertTrue(reporte.containsKey("estudiantesRiesgo"));
        assertTrue(reporte.containsKey("totalCursos"));
    }
    
    @Test
    void generarReporteEstudiantes_debe_retornar_lista() {
        List<Map<String, Object>> reporte = controller.generarReporteEstudiantes();
        assertNotNull(reporte);
    }
    
    @Test
    void generarReporteCurso_con_id_invalido_debe_retornar_null() {
        Map<String, Object> reporte = controller.generarReporteCurso("ID_INEXISTENTE_12345");
        assertNull(reporte);
    }
}