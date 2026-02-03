package modelo;

import java.util.Date;

public class Asistencia {
    private String id;
    private String cursoId;
    private String estudianteId;
    private Date fecha;
    private String estado; // "present", "absent", "late"
    private String observaciones;
    
    public Asistencia() {}
    
    public Asistencia(String cursoId, String estudianteId, Date fecha, String estado) {
        this.cursoId = cursoId;
        this.estudianteId = estudianteId;
        this.fecha = fecha;
        this.estado = estado;
    }
    
    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getCursoId() { return cursoId; }
    public void setCursoId(String cursoId) { this.cursoId = cursoId; }
    
    public String getEstudianteId() { return estudianteId; }
    public void setEstudianteId(String estudianteId) { this.estudianteId = estudianteId; }
    
    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}