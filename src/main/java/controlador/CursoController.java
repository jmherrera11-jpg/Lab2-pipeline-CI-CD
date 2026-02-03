	package controlador;

import modelo.Curso;
import util.MongoDBConnection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class CursoController {
    private MongoCollection<Document> cursosCollection;
    
    public CursoController() {
        MongoDatabase database = MongoDBConnection.getInstance().getDatabase();
        this.cursosCollection = database.getCollection("cursos");
        inicializarDatosDemo();
    }
    
    private void inicializarDatosDemo() {
        if (cursosCollection.countDocuments() == 0) {
            // Insertar datos demo si la colección está vacía
            List<Document> cursosDemo = new ArrayList<>();
            
            cursosDemo.add(new Document()
                .append("codigo", "CS101")
                .append("nombre", "Sistemas de Base de Datos")
                .append("horario", "Lun/Mie 9:00-10:30")
                .append("aula", "Sala 101")
                .append("profesorId", null));
                
            cursosDemo.add(new Document()
                .append("codigo", "CS102")
                .append("nombre", "Cálculo I")
                .append("horario", "Mar/Jue 11:00-12:30")
                .append("aula", "Sala 102")
                .append("profesorId", null));
                
            cursosDemo.add(new Document()
                .append("codigo", "CS103")
                .append("nombre", "Introducción a CS")
                .append("horario", "Lun/Vie 14:00-15:30")
                .append("aula", "Sala 103")
                .append("profesorId", null));
                
            cursosCollection.insertMany(cursosDemo);
        }
    }
    
    // CREATE
    public boolean crearCurso(String codigo, String nombre, String horario, String aula, String profesorId) {
        // Verificar si ya existe el código
        Document cursoExistente = cursosCollection.find(Filters.eq("codigo", codigo)).first();
        if (cursoExistente != null) {
            return false;
        }
        
        Document nuevoCurso = new Document()
            .append("codigo", codigo)
            .append("nombre", nombre)
            .append("horario", horario)
            .append("aula", aula)
            .append("profesorId", profesorId);
            
        cursosCollection.insertOne(nuevoCurso);
        return true;
    }
    
    // READ
    public List<Curso> obtenerTodosCursos() {
        List<Curso> cursos = new ArrayList<>();
        
        for (Document doc : cursosCollection.find()) {
            cursos.add(documentToCurso(doc));
        }
        
        return cursos;
    }
    
    public Curso obtenerCursoPorId(String id) {
        try {
            Document doc = cursosCollection.find(Filters.eq("_id", new ObjectId(id))).first();
            return doc != null ? documentToCurso(doc) : null;
        } catch (Exception e) {
            return null;
        }
    }
    
    public Curso obtenerCursoPorCodigo(String codigo) {
        Document doc = cursosCollection.find(Filters.eq("codigo", codigo)).first();
        return doc != null ? documentToCurso(doc) : null;
    }
    
    // UPDATE
    public boolean actualizarCurso(String id, String codigo, String nombre, String horario, String aula, String profesorId) {
        try {
            ObjectId objectId = new ObjectId(id);
            
            // Verificar si el nuevo código ya existe en otro curso
            if (!obtenerCursoPorId(id).getCodigo().equals(codigo)) {
                Document cursoConMismoCodigo = cursosCollection.find(
                    Filters.and(
                        Filters.eq("codigo", codigo),
                        Filters.ne("_id", objectId)
                    )
                ).first();
                
                if (cursoConMismoCodigo != null) {
                    return false;
                }
            }
            
            cursosCollection.updateOne(
                Filters.eq("_id", objectId),
                Updates.combine(
                    Updates.set("codigo", codigo),
                    Updates.set("nombre", nombre),
                    Updates.set("horario", horario),
                    Updates.set("aula", aula),
                    Updates.set("profesorId", profesorId)
                )
            );
            
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    // DELETE
    public boolean eliminarCurso(String id) {
        try {
            cursosCollection.deleteOne(Filters.eq("_id", new ObjectId(id)));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    // Métodos adicionales
    public List<Curso> buscarCursos(String criterio) {
        List<Curso> resultados = new ArrayList<>();
        String criterioLower = criterio.toLowerCase();
        
        for (Document doc : cursosCollection.find()) {
            Curso curso = documentToCurso(doc);
            if (curso.getNombre().toLowerCase().contains(criterioLower) ||
                curso.getCodigo().toLowerCase().contains(criterioLower) ||
                curso.getHorario().toLowerCase().contains(criterioLower)) {
                resultados.add(curso);
            }
        }
        
        return resultados;
    }
    
    public int contarCursos() {
        return (int) cursosCollection.countDocuments();
    }
    
    private Curso documentToCurso(Document doc) {
        Curso curso = new Curso();
        curso.setId(doc.getObjectId("_id").toString());
        curso.setCodigo(doc.getString("codigo"));
        curso.setNombre(doc.getString("nombre"));
        curso.setHorario(doc.getString("horario"));
        curso.setAula(doc.getString("aula"));
        curso.setProfesorId(doc.getString("profesorId"));
        return curso;
    }
}