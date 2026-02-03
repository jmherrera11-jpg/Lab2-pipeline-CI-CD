
package controlador;

import modelo.Estudiante;
import util.MongoDBConnection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class EstudianteController {
    private MongoCollection<Document> estudiantesCollection;
    
    public EstudianteController() {
        MongoDatabase database = MongoDBConnection.getInstance().getDatabase();
        this.estudiantesCollection = database.getCollection("estudiantes");
        inicializarDatosDemo();
    }
    
    private void inicializarDatosDemo() {
        if (estudiantesCollection.countDocuments() == 0) {
            List<Document> estudiantesDemo = new ArrayList<>();
            
            estudiantesDemo.add(new Document()
                .append("numeroEstudiante", "2023001")
                .append("nombre", "Juan Pérez")
                .append("email", "juan@email.com")
                .append("status", "active"));
                
            estudiantesDemo.add(new Document()
                .append("numeroEstudiante", "2023002")
                .append("nombre", "María García")
                .append("email", "maria@email.com")
                .append("status", "active"));
                
            estudiantesDemo.add(new Document()
                .append("numeroEstudiante", "2023003")
                .append("nombre", "Carlos López")
                .append("email", "carlos@email.com")
                .append("status", "active"));
                
            estudiantesDemo.add(new Document()
                .append("numeroEstudiante", "2023004")
                .append("nombre", "Ana Martínez")
                .append("email", "ana@email.com")
                .append("status", "active"));
                
            estudiantesCollection.insertMany(estudiantesDemo);
        }
    }
    
    // CREATE
    public boolean crearEstudiante(String numeroEstudiante, String nombre, String email) {
        // Verificar si ya existe el número de estudiante
        Document estudianteExistente = estudiantesCollection.find(
            Filters.eq("numeroEstudiante", numeroEstudiante)
        ).first();
        
        if (estudianteExistente != null) {
            return false;
        }
        
        Document nuevoEstudiante = new Document()
            .append("numeroEstudiante", numeroEstudiante)
            .append("nombre", nombre)
            .append("email", email)
            .append("status", "active");
            
        estudiantesCollection.insertOne(nuevoEstudiante);
        return true;
    }
    
    // READ
    public List<Estudiante> obtenerTodosEstudiantes() {
        List<Estudiante> estudiantes = new ArrayList<>();
        
        for (Document doc : estudiantesCollection.find()) {
            estudiantes.add(documentToEstudiante(doc));
        }
        
        return estudiantes;
    }
    
    public Estudiante obtenerEstudiantePorId(String id) {
        try {
            Document doc = estudiantesCollection.find(
                Filters.eq("_id", new ObjectId(id))
            ).first();
            
            return doc != null ? documentToEstudiante(doc) : null;
        } catch (Exception e) {
            return null;
        }
    }
    
    public Estudiante obtenerEstudiantePorNumero(String numero) {
        Document doc = estudiantesCollection.find(
            Filters.eq("numeroEstudiante", numero)
        ).first();
        
        return doc != null ? documentToEstudiante(doc) : null;
    }
    
    // UPDATE
    public boolean actualizarEstudiante(String id, String numeroEstudiante, String nombre, 
                                       String email, String status) {
        try {
            ObjectId objectId = new ObjectId(id);
            
            // Verificar si el nuevo número ya existe en otro estudiante
            Estudiante estudianteActual = obtenerEstudiantePorId(id);
            if (estudianteActual != null && !estudianteActual.getNumeroEstudiante().equals(numeroEstudiante)) {
                Document estudianteConMismoNumero = estudiantesCollection.find(
                    Filters.and(
                        Filters.eq("numeroEstudiante", numeroEstudiante),
                        Filters.ne("_id", objectId)
                    )
                ).first();
                
                if (estudianteConMismoNumero != null) {
                    return false;
                }
            }
            
            estudiantesCollection.updateOne(
                Filters.eq("_id", objectId),
                Updates.combine(
                    Updates.set("numeroEstudiante", numeroEstudiante),
                    Updates.set("nombre", nombre),
                    Updates.set("email", email),
                    Updates.set("status", status)
                )
            );
            
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    // DELETE
    public boolean eliminarEstudiante(String id) {
        try {
            estudiantesCollection.deleteOne(Filters.eq("_id", new ObjectId(id)));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    // Métodos adicionales
    public List<Estudiante> buscarEstudiantes(String criterio) {
        List<Estudiante> resultados = new ArrayList<>();
        
        // Buscar por múltiples campos usando OR
        for (Document doc : estudiantesCollection.find(
            Filters.or(
                Filters.regex("nombre", ".*" + criterio + ".*", "i"),
                Filters.regex("numeroEstudiante", ".*" + criterio + ".*"),
                Filters.regex("email", ".*" + criterio + ".*", "i")
            )
        )) {
            resultados.add(documentToEstudiante(doc));
        }
        
        return resultados;
    }
    
    public int contarEstudiantesActivos() {
        return (int) estudiantesCollection.countDocuments(Filters.eq("status", "active"));
    }
    
    public int contarEstudiantesInactivos() {
        return (int) estudiantesCollection.countDocuments(Filters.eq("status", "inactive"));
    }
    
    private Estudiante documentToEstudiante(Document doc) {
        Estudiante estudiante = new Estudiante();
        estudiante.setId(doc.getObjectId("_id").toString());
        estudiante.setNumeroEstudiante(doc.getString("numeroEstudiante"));
        estudiante.setNombre(doc.getString("nombre"));
        estudiante.setEmail(doc.getString("email"));
        estudiante.setStatus(doc.getString("status"));
        return estudiante;
    }
}