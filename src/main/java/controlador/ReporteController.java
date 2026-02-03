package controlador;

import modelo.Asistencia;
import modelo.Curso;
import modelo.Estudiante;
import util.MongoDBConnection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.*;

public class ReporteController {
    private AsistenciaController asistenciaController;
    private CursoController cursoController;
    private MongoCollection<Document> asistenciasCollection;
    
    public ReporteController() {
        this.asistenciaController = new AsistenciaController();
        this.cursoController = new CursoController();
        
        MongoDatabase database = MongoDBConnection.getInstance().getDatabase();
        this.asistenciasCollection = database.getCollection("asistencias");
    }
    
    public Map<String, Object> generarReporteGeneral() {
        Map<String, Object> reporte = new HashMap<>();
        
        // Estadísticas generales
        List<Estudiante> estudiantes = asistenciaController.obtenerTodosEstudiantes();
        int totalEstudiantes = estudiantes.size();
        
        int estudiantesActivos = (int) estudiantes.stream()
            .filter(e -> "active".equals(e.getStatus()))
            .count();
        int estudiantesInactivos = totalEstudiantes - estudiantesActivos;
        
        // Calcular promedio de asistencia desde MongoDB
        double promedioAsistencia = calcularPromedioAsistenciaGeneral();
        
        // Identificar estudiantes en riesgo
        List<Map<String, Object>> estudiantesRiesgo = identificarEstudiantesRiesgo();
        
        reporte.put("totalEstudiantes", totalEstudiantes);
        reporte.put("estudiantesActivos", estudiantesActivos);
        reporte.put("estudiantesInactivos", estudiantesInactivos);
        reporte.put("promedioAsistencia", promedioAsistencia);
        reporte.put("estudiantesRiesgo", estudiantesRiesgo);
        reporte.put("totalCursos", cursoController.contarCursos());
        
        return reporte;
    }
    
    public Map<String, Object> generarReporteCurso(String cursoId) {
        Map<String, Object> reporte = new HashMap<>();
        
        Curso curso = cursoController.obtenerCursoPorId(cursoId);
        if (curso == null) {
            return null;
        }
        
        // Obtener estadísticas desde MongoDB usando agregaciones
        Map<String, String> asistencias = asistenciaController.obtenerAsistenciaPorCurso(cursoId);
        
        // Calcular estadísticas del curso
        long totalRegistros = asistencias.size();
        long presentes = asistencias.values().stream().filter(v -> "present".equals(v)).count();
        long tardios = asistencias.values().stream().filter(v -> "late".equals(v)).count();
        long ausentes = asistencias.values().stream().filter(v -> "absent".equals(v)).count();
        
        double tasaAsistencia = totalRegistros > 0 ? (presentes * 100.0) / totalRegistros : 0.0;
        
        reporte.put("curso", curso);
        reporte.put("totalRegistros", totalRegistros);
        reporte.put("presentes", presentes);
        reporte.put("tardios", tardios);
        reporte.put("ausentes", ausentes);
        reporte.put("tasaAsistencia", tasaAsistencia);
        reporte.put("fechaGeneracion", new Date());
        
        return reporte;
    }
    
    public List<Map<String, Object>> generarReporteEstudiantes() {
        List<Map<String, Object>> reporte = new ArrayList<>();
        List<Estudiante> estudiantes = asistenciaController.obtenerTodosEstudiantes();
        
        for (Estudiante estudiante : estudiantes) {
            Map<String, Object> datos = new HashMap<>();
            double porcentaje = asistenciaController.calcularPorcentajeAsistencia(estudiante.getId());
            
            datos.put("estudiante", estudiante);
            datos.put("porcentajeAsistencia", porcentaje);
            datos.put("estado", determinarEstadoAsistencia(porcentaje));
            
            reporte.add(datos);
        }
        
        // Ordenar por porcentaje de asistencia
        reporte.sort(Comparator.comparingDouble(m -> (Double) m.get("porcentajeAsistencia")));
        
        return reporte;
    }
    
    private double calcularPromedioAsistenciaGeneral() {
        List<Estudiante> estudiantes = asistenciaController.obtenerTodosEstudiantes();
        if (estudiantes.isEmpty()) return 0.0;
        
        double suma = 0.0;
        for (Estudiante estudiante : estudiantes) {
            suma += asistenciaController.calcularPorcentajeAsistencia(estudiante.getId());
        }
        
        return suma / estudiantes.size();
    }
    
    private List<Map<String, Object>> identificarEstudiantesRiesgo() {
        List<Map<String, Object>> estudiantesRiesgo = new ArrayList<>();
        List<Estudiante> estudiantes = asistenciaController.obtenerTodosEstudiantes();
        
        for (Estudiante estudiante : estudiantes) {
            double porcentaje = asistenciaController.calcularPorcentajeAsistencia(estudiante.getId());
            
            if (porcentaje < 75.0 && "active".equals(estudiante.getStatus())) {
                Map<String, Object> datos = new HashMap<>();
                datos.put("estudiante", estudiante);
                datos.put("porcentajeAsistencia", porcentaje);
                datos.put("cursosAfectados", obtenerCursosAfectados(estudiante.getId()));
                
                estudiantesRiesgo.add(datos);
            }
        }
        
        return estudiantesRiesgo;
    }
    
    private List<String> obtenerCursosAfectados(String estudianteId) {
        List<String> cursos = new ArrayList<>();
        
        // Consultar cursos únicos donde el estudiante tiene asistencia
        Set<String> cursoIds = new HashSet<>();
        for (Document doc : asistenciasCollection.find(
            Filters.eq("estudianteId", estudianteId)
        )) {
            cursoIds.add(doc.getString("cursoId"));
        }
        
        // Obtener códigos de cursos
        for (String cursoId : cursoIds) {
            Curso curso = cursoController.obtenerCursoPorId(cursoId);
            if (curso != null) {
                cursos.add(curso.getCodigo());
            }
        }
        
        return cursos;
    }
    
    private String determinarEstadoAsistencia(double porcentaje) {
        if (porcentaje >= 90.0) return "Excelente";
        if (porcentaje >= 80.0) return "Bueno";
        if (porcentaje >= 70.0) return "Regular";
        if (porcentaje >= 60.0) return "Preocupante";
        return "Crítico";
    }
}