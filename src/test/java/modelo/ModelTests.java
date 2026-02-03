package modelo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

@DisplayName("Tests de modelos del sistema")
public class ModelTests {
    
    @Test
    @DisplayName("Modelo Usuario - Constructor y getters/setters")
    void testUsuarioModel() {
        // Test constructor con parámetros
        Usuario usuario = new Usuario("U001", "Juan Pérez", "juan@email.com", "student");
        
        assertEquals("U001", usuario.getId());
        assertEquals("Juan Pérez", usuario.getNombre());
        assertEquals("juan@email.com", usuario.getEmail());
        assertEquals("student", usuario.getRol());
        assertNull(usuario.getPassword()); // Password no se inicializa en este constructor
        
        // Test constructor vacío
        Usuario usuarioVacio = new Usuario();
        assertNull(usuarioVacio.getId());
        assertNull(usuarioVacio.getNombre());
        assertNull(usuarioVacio.getEmail());
        assertNull(usuarioVacio.getRol());
        assertNull(usuarioVacio.getPassword());
        
        // Test setters
        usuarioVacio.setId("U002");
        usuarioVacio.setNombre("María García");
        usuarioVacio.setEmail("maria@email.com");
        usuarioVacio.setRol("teacher");
        usuarioVacio.setPassword("secret123");
        
        assertEquals("U002", usuarioVacio.getId());
        assertEquals("María García", usuarioVacio.getNombre());
        assertEquals("maria@email.com", usuarioVacio.getEmail());
        assertEquals("teacher", usuarioVacio.getRol());
        assertEquals("secret123", usuarioVacio.getPassword());
        
        // Test actualización de valores
        usuario.setNombre("Juan Actualizado");
        usuario.setEmail("juan.actualizado@email.com");
        usuario.setRol("admin");
        usuario.setPassword("nuevoPass");
        
        assertEquals("Juan Actualizado", usuario.getNombre());
        assertEquals("juan.actualizado@email.com", usuario.getEmail());
        assertEquals("admin", usuario.getRol());
        assertEquals("nuevoPass", usuario.getPassword());
        
        // Verificar que ID no cambió
        assertEquals("U001", usuario.getId());
    }
    
    @Test
    @DisplayName("Modelo Estudiante - Constructor y getters/setters")
    void testEstudianteModel() {
        // Test constructor con parámetros
        Estudiante estudiante = new Estudiante("S001", "2023001", "Carlos López", "carlos@email.com");
        
        assertEquals("S001", estudiante.getId());
        assertEquals("2023001", estudiante.getNumeroEstudiante());
        assertEquals("Carlos López", estudiante.getNombre());
        assertEquals("carlos@email.com", estudiante.getEmail());
        assertEquals("active", estudiante.getStatus()); // Status por defecto
        
        // Test constructor vacío
        Estudiante estudianteVacio = new Estudiante();
        assertNull(estudianteVacio.getId());
        assertNull(estudianteVacio.getNumeroEstudiante());
        assertNull(estudianteVacio.getNombre());
        assertNull(estudianteVacio.getEmail());
        assertNull(estudianteVacio.getStatus());
        
        // Test setters
        estudianteVacio.setId("S002");
        estudianteVacio.setNumeroEstudiante("2023002");
        estudianteVacio.setNombre("Ana Martínez");
        estudianteVacio.setEmail("ana@email.com");
        estudianteVacio.setStatus("inactive");
        
        assertEquals("S002", estudianteVacio.getId());
        assertEquals("2023002", estudianteVacio.getNumeroEstudiante());
        assertEquals("Ana Martínez", estudianteVacio.getNombre());
        assertEquals("ana@email.com", estudianteVacio.getEmail());
        assertEquals("inactive", estudianteVacio.getStatus());
        
        // Test cambiar status
        estudiante.setStatus("inactive");
        assertEquals("inactive", estudiante.getStatus());
        
        estudiante.setStatus("active");
        assertEquals("active", estudiante.getStatus());
        
        // Test actualización de datos personales
        estudiante.setNombre("Carlos Actualizado");
        estudiante.setEmail("carlos.nuevo@email.com");
        estudiante.setNumeroEstudiante("2023999");
        
        assertEquals("Carlos Actualizado", estudiante.getNombre());
        assertEquals("carlos.nuevo@email.com", estudiante.getEmail());
        assertEquals("2023999", estudiante.getNumeroEstudiante());
        
        // Verificar que ID no cambió
        assertEquals("S001", estudiante.getId());
    }
    
    @Test
    @DisplayName("Modelo Curso - Constructor y getters/setters")
    void testCursoModel() {
        // Test constructor con parámetros
        Curso curso = new Curso("C001", "CS101", "Base de Datos", "Lun/Mie 9:00-10:30", "Sala 101");
        
        assertEquals("C001", curso.getId());
        assertEquals("CS101", curso.getCodigo());
        assertEquals("Base de Datos", curso.getNombre());
        assertEquals("Lun/Mie 9:00-10:30", curso.getHorario());
        assertEquals("Sala 101", curso.getAula());
        assertNull(curso.getProfesorId()); // No se asigna en este constructor
        
        // Test constructor vacío
        Curso cursoVacio = new Curso();
        assertNull(cursoVacio.getId());
        assertNull(cursoVacio.getCodigo());
        assertNull(cursoVacio.getNombre());
        assertNull(cursoVacio.getHorario());
        assertNull(cursoVacio.getAula());
        assertNull(cursoVacio.getProfesorId());
        
        // Test setters
        cursoVacio.setId("C002");
        cursoVacio.setCodigo("CS102");
        cursoVacio.setNombre("Cálculo I");
        cursoVacio.setHorario("Mar/Jue 11:00-12:30");
        cursoVacio.setAula("Sala 102");
        cursoVacio.setProfesorId("P001");
        
        assertEquals("C002", cursoVacio.getId());
        assertEquals("CS102", cursoVacio.getCodigo());
        assertEquals("Cálculo I", cursoVacio.getNombre());
        assertEquals("Mar/Jue 11:00-12:30", cursoVacio.getHorario());
        assertEquals("Sala 102", cursoVacio.getAula());
        assertEquals("P001", cursoVacio.getProfesorId());
        
        // Test actualización de curso
        curso.setNombre("Base de Datos Avanzada");
        curso.setHorario("Lun/Mie/Vie 8:00-9:30");
        curso.setAula("Sala 201");
        curso.setProfesorId("P005");
        
        assertEquals("Base de Datos Avanzada", curso.getNombre());
        assertEquals("Lun/Mie/Vie 8:00-9:30", curso.getHorario());
        assertEquals("Sala 201", curso.getAula());
        assertEquals("P005", curso.getProfesorId());
        
        // Verificar que ID y código no cambiaron
        assertEquals("C001", curso.getId());
        assertEquals("CS101", curso.getCodigo());
        
        // Test cambiar código
        curso.setCodigo("CS101-A");
        assertEquals("CS101-A", curso.getCodigo());
    }
    
    @Test
    @DisplayName("Modelo Asistencia - Constructor y getters/setters")
    void testAsistenciaModel() {
        Date fechaActual = new Date();
        
        // Test constructor con parámetros
        Asistencia asistencia = new Asistencia("C001", "S001", fechaActual, "present");
        
        assertEquals("C001", asistencia.getCursoId());
        assertEquals("S001", asistencia.getEstudianteId());
        assertEquals(fechaActual, asistencia.getFecha());
        assertEquals("present", asistencia.getEstado());
        assertNull(asistencia.getObservaciones()); // No se inicializa
        assertNull(asistencia.getId()); // ID no se asigna en constructor
        
        // Test constructor vacío
        Asistencia asistenciaVacia = new Asistencia();
        assertNull(asistenciaVacia.getId());
        assertNull(asistenciaVacia.getCursoId());
        assertNull(asistenciaVacia.getEstudianteId());
        assertNull(asistenciaVacia.getFecha());
        assertNull(asistenciaVacia.getEstado());
        assertNull(asistenciaVacia.getObservaciones());
        
        // Test setters
        Date nuevaFecha = new Date(System.currentTimeMillis() + 100000);
        asistenciaVacia.setId("A001");
        asistenciaVacia.setCursoId("C002");
        asistenciaVacia.setEstudianteId("S002");
        asistenciaVacia.setFecha(nuevaFecha);
        asistenciaVacia.setEstado("absent");
        asistenciaVacia.setObservaciones("Justificado con certificado médico");
        
        assertEquals("A001", asistenciaVacia.getId());
        assertEquals("C002", asistenciaVacia.getCursoId());
        assertEquals("S002", asistenciaVacia.getEstudianteId());
        assertEquals(nuevaFecha, asistenciaVacia.getFecha());
        assertEquals("absent", asistenciaVacia.getEstado());
        assertEquals("Justificado con certificado médico", asistenciaVacia.getObservaciones());
        
        // Test cambios en asistencia
        Date otraFecha = new Date(System.currentTimeMillis() + 200000);
        asistencia.setEstado("late");
        asistencia.setObservaciones("Llegó 15 minutos tarde");
        asistencia.setFecha(otraFecha);
        
        assertEquals("late", asistencia.getEstado());
        assertEquals("Llegó 15 minutos tarde", asistencia.getObservaciones());
        assertEquals(otraFecha, asistencia.getFecha());
        
        // Test todos los estados posibles
        asistencia.setEstado("present");
        assertEquals("present", asistencia.getEstado());
        
        asistencia.setEstado("late");
        assertEquals("late", asistencia.getEstado());
        
        asistencia.setEstado("absent");
        assertEquals("absent", asistencia.getEstado());
        
        // Verificar que cursoId y estudianteId no cambiaron
        assertEquals("C001", asistencia.getCursoId());
        assertEquals("S001", asistencia.getEstudianteId());
    }
    
    @Test
    @DisplayName("Modelos - Igualdad basada en IDs")
    void testModelEquality() {
        // Usuarios con mismo ID deberían ser iguales (en lógica de negocio)
        Usuario usuario1 = new Usuario("U001", "Juan", "juan@email.com", "student");
        Usuario usuario2 = new Usuario("U001", "Juan Pérez", "juan.perez@email.com", "teacher");
        
        // En estos modelos no sobrescribimos equals(), pero verificamos lógica de negocio
        assertEquals(usuario1.getId(), usuario2.getId());
        assertNotEquals(usuario1.getNombre(), usuario2.getNombre());
        assertNotEquals(usuario1.getEmail(), usuario2.getEmail());
        assertNotEquals(usuario1.getRol(), usuario2.getRol());
        
        // Estudiantes
        Estudiante estudiante1 = new Estudiante("S001", "2023001", "Carlos", "carlos@email.com");
        Estudiante estudiante2 = new Estudiante("S001", "2023999", "Carlos López", "carlos.lopez@email.com");
        
        assertEquals(estudiante1.getId(), estudiante2.getId());
        assertNotEquals(estudiante1.getNumeroEstudiante(), estudiante2.getNumeroEstudiante());
        
        // Cursos
        Curso curso1 = new Curso("C001", "CS101", "Curso 1", "Horario 1", "Sala 1");
        Curso curso2 = new Curso("C001", "CS102", "Curso 2", "Horario 2", "Sala 2");
        
        assertEquals(curso1.getId(), curso2.getId());
        assertNotEquals(curso1.getCodigo(), curso2.getCodigo());
    }
    
    @Test
    @DisplayName("Modelos - Valores límite y casos especiales")
    void testModelEdgeCases() {
        // Usuario con valores límite
        Usuario usuario = new Usuario();
        usuario.setId(""); // String vacío
        usuario.setNombre(""); // String vacío
        usuario.setEmail(""); // String vacío
        usuario.setRol(""); // String vacío
        usuario.setPassword(""); // String vacío
        
        assertEquals("", usuario.getId());
        assertEquals("", usuario.getNombre());
        assertEquals("", usuario.getEmail());
        assertEquals("", usuario.getRol());
        assertEquals("", usuario.getPassword());
        
        // Estudiante con valores límite
        Estudiante estudiante = new Estudiante();
        estudiante.setStatus(""); // Status vacío (no válido en negocio pero técnicamente posible)
        assertEquals("", estudiante.getStatus());
        
        // Status con espacios
        estudiante.setStatus("  active  ");
        assertEquals("  active  ", estudiante.getStatus());
        
        // Curso con valores largos
        Curso curso = new Curso();
        String nombreLargo = "Curso con nombre extremadamente largo que podría exceder límites de UI pero no de modelo";
        curso.setNombre(nombreLargo);
        assertEquals(nombreLargo, curso.getNombre());
        
        // Asistencia con observaciones largas
        Asistencia asistencia = new Asistencia();
        String observacionesLargas = "Observación muy detallada sobre la asistencia del estudiante. " +
            "El estudiante presentó justificación médica válida. " +
            "Se recomienda seguimiento con el departamento de bienestar estudiantil.";
        asistencia.setObservaciones(observacionesLargas);
        assertEquals(observacionesLargas, asistencia.getObservaciones());
    }
    
    @Test
    @DisplayName("Modelos - Cadena de caracteres especiales")
    void testModelSpecialCharacters() {
        // Usuario con caracteres especiales
        Usuario usuario = new Usuario();
        usuario.setNombre("María José O'Connor-García");
        usuario.setEmail("maría.josé@español.edu");
        usuario.setRol("coördinator"); // Carácter especial en rol
        
        assertEquals("María José O'Connor-García", usuario.getNombre());
        assertEquals("maría.josé@español.edu", usuario.getEmail());
        assertEquals("coördinator", usuario.getRol());
        
        // Estudiante con caracteres especiales
        Estudiante estudiante = new Estudiante();
        estudiante.setNombre("Jürgen Müller");
        estudiante.setEmail("jürgen.müller@email.de");
        
        assertEquals("Jürgen Müller", estudiante.getNombre());
        assertEquals("jürgen.müller@email.de", estudiante.getEmail());
        
        // Curso con caracteres especiales
        Curso curso = new Curso();
        curso.setNombre("Cálculo Diferéncial & Integral");
        curso.setAula("Sala 101-A (Piso 3)");
        
        assertEquals("Cálculo Diferéncial & Integral", curso.getNombre());
        assertEquals("Sala 101-A (Piso 3)", curso.getAula());
        
        // Asistencia con observaciones especiales
        Asistencia asistencia = new Asistencia();
        asistencia.setObservaciones("Asistencia con emojis 👍✅ y símbolos ©®™");
        
        assertEquals("Asistencia con emojis 👍✅ y símbolos ©®™", asistencia.getObservaciones());
    }
    
    @Test
    @DisplayName("Modelos - Serialización a String (toString)")
    void testModelToString() {
        // Usuario toString
        Usuario usuario = new Usuario("U001", "Juan Pérez", "juan@email.com", "student");
        String usuarioStr = usuario.toString();
        assertNotNull(usuarioStr);
        assertTrue(usuarioStr.contains("Usuario") || usuarioStr.contains("U001") || 
                  usuarioStr.contains("Juan") || usuarioStr.contains("student"));
        
        // Estudiante toString
        Estudiante estudiante = new Estudiante("S001", "2023001", "Carlos López", "carlos@email.com");
        String estudianteStr = estudiante.toString();
        assertNotNull(estudianteStr);
        
        // Curso toString
        Curso curso = new Curso("C001", "CS101", "Base de Datos", "Lun/Mie 9:00", "Sala 101");
        String cursoStr = curso.toString();
        assertNotNull(cursoStr);
        
        // Asistencia toString
        Asistencia asistencia = new Asistencia("C001", "S001", new Date(), "present");
        String asistenciaStr = asistencia.toString();
        assertNotNull(asistenciaStr);
        
        // Verificar que toString no lanza excepciones con nulls
        Usuario usuarioNull = new Usuario();
        assertDoesNotThrow(usuarioNull::toString);
        
        Estudiante estudianteNull = new Estudiante();
        assertDoesNotThrow(estudianteNull::toString);
        
        Curso cursoNull = new Curso();
        assertDoesNotThrow(cursoNull::toString);
        
        Asistencia asistenciaNull = new Asistencia();
        assertDoesNotThrow(asistenciaNull::toString);
    }
    
    @Test
    @DisplayName("Modelos - Inmutabilidad parcial")
    void testModelPartialImmutability() {
        // Crear objetos
        Usuario usuario = new Usuario("U001", "Original", "original@email.com", "student");
        Estudiante estudiante = new Estudiante("S001", "2023001", "Original", "original@email.com");
        Curso curso = new Curso("C001", "CS001", "Original", "Horario", "Sala");
        Date fechaOriginal = new Date();
        Asistencia asistencia = new Asistencia("C001", "S001", fechaOriginal, "present");
        
        // Guardar valores originales
        String usuarioIdOriginal = usuario.getId();
        String estudianteIdOriginal = estudiante.getId();
        String cursoIdOriginal = curso.getId();
        Date asistenciaFechaOriginal = asistencia.getFecha();
        
        // Modificar
        usuario.setId("U002");
        estudiante.setId("S002");
        curso.setId("C002");
        
        Date nuevaFecha = new Date(System.currentTimeMillis() + 100000);
        asistencia.setFecha(nuevaFecha);
        
        // Verificar que cambiaron
        assertNotEquals(usuarioIdOriginal, usuario.getId());
        assertNotEquals(estudianteIdOriginal, estudiante.getId());
        assertNotEquals(cursoIdOriginal, curso.getId());
        assertNotEquals(asistenciaFechaOriginal, asistencia.getFecha());
        
        // Verificar que son objetos diferentes (no el mismo reference)
        assertNotSame(usuarioIdOriginal, usuario.getId());
        assertNotSame(asistenciaFechaOriginal, asistencia.getFecha());
    }
    
    @Test
    @DisplayName("Modelos - Integración entre modelos")
    void testModelIntegration() {
        // Crear profesor
        Usuario profesor = new Usuario("P001", "Prof. Anderson", "anderson@academico.edu", "teacher");
        
        // Crear curso asignado al profesor
        Curso curso = new Curso("C001", "CS101", "Base de Datos", "Lun/Mie 9:00", "Sala 101");
        curso.setProfesorId(profesor.getId());
        
        // Crear estudiante
        Estudiante estudiante = new Estudiante("S001", "2023001", "Juan Pérez", "juan@email.com");
        
        // Crear asistencia que relaciona curso y estudiante
        Asistencia asistencia = new Asistencia(
            curso.getId(), 
            estudiante.getId(), 
            new Date(), 
            "present"
        );
        asistencia.setObservaciones("Asistencia registrada por " + profesor.getNombre());
        
        // Verificar relaciones
        assertEquals(profesor.getId(), curso.getProfesorId());
        assertEquals(curso.getId(), asistencia.getCursoId());
        assertEquals(estudiante.getId(), asistencia.getEstudianteId());
        assertTrue(asistencia.getObservaciones().contains(profesor.getNombre()));
        
        // Verificar tipos de datos
        assertInstanceOf(String.class, profesor.getId());
        assertInstanceOf(String.class, curso.getId());
        assertInstanceOf(String.class, estudiante.getId());
        assertInstanceOf(String.class, asistencia.getEstado());
        assertInstanceOf(Date.class, asistencia.getFecha());
        
        // Verificar valores no nulos después de integración
        assertNotNull(profesor.getId());
        assertNotNull(curso.getId());
        assertNotNull(estudiante.getId());
        assertNotNull(asistencia.getCursoId());
        assertNotNull(asistencia.getEstudianteId());
        assertNotNull(asistencia.getFecha());
        assertNotNull(asistencia.getEstado());
    }
}
