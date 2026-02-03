package modelo;

public class Estudiante {
    private String id;
    private String numeroEstudiante;
    private String nombre;
    private String email;
    private String status; // "active", "inactive"
    
    public Estudiante() {}
    
    public Estudiante(String id, String numeroEstudiante, String nombre, String email) {
        this.id = id;
        this.numeroEstudiante = numeroEstudiante;
        this.nombre = nombre;
        this.email = email;
        this.status = "active";
    }
    
    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getNumeroEstudiante() { return numeroEstudiante; }
    public void setNumeroEstudiante(String numeroEstudiante) { this.numeroEstudiante = numeroEstudiante; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}