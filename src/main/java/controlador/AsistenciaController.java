package controlador;

import modelo.Asistencia;
import modelo.Estudiante;
import util.MongoDBConnection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.*;

public class AsistenciaController {
    private MongoCollection<Document> asistenciasCollection;
    private MongoCollection<Document> estudiantesCollection;
    private EstudianteController estudianteController;
    
    public AsistenciaController() {
        MongoDatabase database = MongoDBConnection.getInstance().getDatabase();
        this.asistenciasCollection = database.getCollection("asistencias");
        this.estudiantesCollection = database.getCollection("estudiantes");
        this.estudianteController = new EstudianteController();
        
        inicializarDatosDemo();
    }
    
    private void inicializarDatosDemo() {
        if (estudiantesCollection.countDocuments() == 0) {
            estudianteController.crearEstudiante("2023001", "Juan Pérez", "juan@email.com");
            estudianteController.crearEstudiante("2023002", "María García", "maria@email.com");
            estudianteController.crearEstudiante("2023003", "Carlos López", "carlos@email.com");
            
            Document estudiante4 = new Document()
                .append("numeroEstudiante", "2023004")
                .append("nombre", "Ana Martínez")
                .append("email", "ana@email.com")
                .append("status", "inactive");
            estudiantesCollection.insertOne(estudiante4);
        }
    }
    
    public List<Estudiante> obtenerTodosEstudiantes() {
        return estudianteController.obtenerTodosEstudiantes();
    }
    
    public void registrarAsistencia(String cursoId, Map<String, String> asistencias) {
        List<Document> registros = new ArrayList<>();
        Date fecha = new Date();
        
        for (Map.Entry<String, String> entry : asistencias.entrySet()) {
            Document asistencia = new Document()
                .append("cursoId", cursoId)
                .append("estudianteId", entry.getKey())
                .append("fecha", fecha)
                .append("estado", entry.getValue());
                
            registros.add(asistencia);
        }
        
        if (!registros.isEmpty()) {
            asistenciasCollection.insertMany(registros);
        }
    }
    
    public Map<String, String> obtenerAsistenciaPorCurso(String cursoId) {
        Map<String, String> resultado = new HashMap<>();
        
        for (Document doc : asistenciasCollection.find(Filters.eq("cursoId", cursoId))) {
            resultado.put(doc.getString("estudianteId"), doc.getString("estado"));
        }
        
        return resultado;
    }
    
    public double calcularPorcentajeAsistencia(String estudianteId) {
        long total = asistenciasCollection.countDocuments(
            Filters.eq("estudianteId", estudianteId)
        );
        
        if (total == 0) {
            return 0.0;
        }
        
        long presentes = asistenciasCollection.countDocuments(
            Filters.and(
                Filters.eq("estudianteId", estudianteId),
                Filters.eq("estado", "present")
            )
        );
        
        return (presentes * 100.0) / total;
    }
 // Agregar este método a AsistenciaController.java
    public void limpiarAsistenciasEstudiante(String estudianteId) {
        // Opcional: Implementar limpieza si es necesario para tests
    }

    public void limpiarAsistenciasPorCurso(String cursoId) {
        asistenciasCollection.deleteMany(Filters.eq("cursoId", cursoId));
    }
}