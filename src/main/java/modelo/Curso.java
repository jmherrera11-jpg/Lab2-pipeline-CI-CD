package modelo;

public class Curso {
    private String id;
    private String codigo;
    private String nombre;
    private String horario;
    private String aula;
    private String profesorId;
    
    public Curso() {}
    
    public Curso(String id, String codigo, String nombre, String horario, String aula) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.horario = horario;
        this.aula = aula;
    }
    
    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getHorario() { return horario; }
    public void setHorario(String horario) { this.horario = horario; }
    
    public String getAula() { return aula; }
    public void setAula(String aula) { this.aula = aula; }
    
    public String getProfesorId() { return profesorId; }
    public void setProfesorId(String profesorId) { this.profesorId = profesorId; }
}