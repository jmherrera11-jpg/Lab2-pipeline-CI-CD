package controlador;

import modelo.Usuario;
import util.MongoDBConnection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class ProfesorController {
    private MongoCollection<Document> profesoresCollection;
    
    public ProfesorController() {
        MongoDatabase database = MongoDBConnection.getInstance().getDatabase();
        this.profesoresCollection = database.getCollection("profesores");
        inicializarDatosDemo();
    }
    
    private void inicializarDatosDemo() {
        if (profesoresCollection.countDocuments() == 0) {
            List<Document> profesoresDemo = new ArrayList<>();
            
            profesoresDemo.add(new Document()
                .append("nombre", "Prof. Anderson")
                .append("email", "anderson@academico.edu")
                .append("rol", "profesor"));
                
            profesoresDemo.add(new Document()
                .append("nombre", "Prof. Martínez")
                .append("email", "martinez@academico.edu")
                .append("rol", "profesor"));
                
            profesoresDemo.add(new Document()
                .append("nombre", "Prof. Rodríguez")
                .append("email", "rodriguez@academico.edu")
                .append("rol", "profesor"));
                
            profesoresCollection.insertMany(profesoresDemo);
        }
    }
    
    // CREATE
    public boolean crearProfesor(String nombre, String email) {
        // Verificar si ya existe el email
        Document profesorExistente = profesoresCollection.find(
            Filters.eq("email", email)
        ).first();
        
        if (profesorExistente != null) {
            return false;
        }
        
        Document nuevoProfesor = new Document()
            .append("nombre", nombre)
            .append("email", email)
            .append("rol", "profesor");
            
        profesoresCollection.insertOne(nuevoProfesor);
        return true;
    }
    
    // READ
    public List<Usuario> obtenerTodosProfesores() {
        List<Usuario> profesores = new ArrayList<>();
        
        for (Document doc : profesoresCollection.find()) {
            profesores.add(documentToUsuario(doc));
        }
        
        return profesores;
    }
    
    public Usuario obtenerProfesorPorId(String id) {
        try {
            Document doc = profesoresCollection.find(
                Filters.eq("_id", new ObjectId(id))
            ).first();
            
            return doc != null ? documentToUsuario(doc) : null;
        } catch (Exception e) {
            return null;
        }
    }
    
    // UPDATE
    public boolean actualizarProfesor(String id, String nombre, String email) {
        try {
            ObjectId objectId = new ObjectId(id);
            
            // Verificar si el nuevo email ya existe en otro profesor
            Usuario profesorActual = obtenerProfesorPorId(id);
            if (profesorActual != null && !profesorActual.getEmail().equals(email)) {
                Document profesorConMismoEmail = profesoresCollection.find(
                    Filters.and(
                        Filters.eq("email", email),
                        Filters.ne("_id", objectId)
                    )
                ).first();
                
                if (profesorConMismoEmail != null) {
                    return false;
                }
            }
            
            profesoresCollection.updateOne(
                Filters.eq("_id", objectId),
                Updates.combine(
                    Updates.set("nombre", nombre),
                    Updates.set("email", email)
                )
            );
            
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    // DELETE
    public boolean eliminarProfesor(String id) {
        try {
            profesoresCollection.deleteOne(Filters.eq("_id", new ObjectId(id)));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    // Métodos adicionales
    public List<Usuario> buscarProfesores(String criterio) {
        List<Usuario> resultados = new ArrayList<>();
        
        for (Document doc : profesoresCollection.find(
            Filters.or(
                Filters.regex("nombre", ".*" + criterio + ".*", "i"),
                Filters.regex("email", ".*" + criterio + ".*", "i")
            )
        )) {
            resultados.add(documentToUsuario(doc));
        }
        
        return resultados;
    }
    
    private Usuario documentToUsuario(Document doc) {
        Usuario usuario = new Usuario();
        usuario.setId(doc.getObjectId("_id").toString());
        usuario.setNombre(doc.getString("nombre"));
        usuario.setEmail(doc.getString("email"));
        usuario.setRol(doc.getString("rol"));
        return usuario;
    }
}