package modelo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

@DisplayName("Tests de lógica de negocio en modelos")
public class ModelBusinessLogicTests {
    
    @Test
    @DisplayName("Validación de estados válidos para Asistencia")
    void testAsistenciaValidStates() {
        Asistencia asistencia = new Asistencia();
        
        // Estados válidos según el sistema
        String[] estadosValidos = {"present", "absent", "late"};
        
        for (String estado : estadosValidos) {
            asistencia.setEstado(estado);
            assertEquals(estado, asistencia.getEstado(), 
                "El estado '" + estado + "' debería ser aceptado");
        }
        
        // Estado no definido (técnicamente posible, pero no válido en negocio)
        asistencia.setEstado("justificado");
        assertEquals("justificado", asistencia.getEstado());
        
        asistencia.setEstado(null);
        assertNull(asistencia.getEstado());
        
        asistencia.setEstado("");
        assertEquals("", asistencia.getEstado());
    }
    
    @Test
    @DisplayName("Validación de estados válidos para Estudiante")
    void testEstudianteValidStatus() {
        Estudiante estudiante = new Estudiante();
        
        // Estados esperados en el sistema
        estudiante.setStatus("active");
        assertEquals("active", estudiante.getStatus());
        
        estudiante.setStatus("inactive");
        assertEquals("inactive", estudiante.getStatus());
        
        // Status por defecto en constructor
        Estudiante nuevoEstudiante = new Estudiante("S001", "2023001", "Nombre", "email@test.com");
        assertEquals("active", nuevoEstudiante.getStatus(), 
            "El status por defecto debería ser 'active'");
        
        // Status no estándar (técnicamente posible)
        estudiante.setStatus("graduated");
        assertEquals("graduated", estudiante.getStatus());
        
        estudiante.setStatus("suspended");
        assertEquals("suspended", estudiante.getStatus());
    }
    
    @Test
    @DisplayName("Validación de roles de Usuario")
    void testUsuarioValidRoles() {
        Usuario usuario = new Usuario();
        
        // Roles definidos en el sistema
        String[] rolesValidos = {"teacher", "student", "coordinator"};
        
        for (String rol : rolesValidos) {
            usuario.setRol(rol);
            assertEquals(rol, usuario.getRol(), 
                "El rol '" + rol + "' debería ser aceptado");
        }
        
        // Rol no estándar
        usuario.setRol("admin");
        assertEquals("admin", usuario.getRol());
        
        usuario.setRol("assistant");
        assertEquals("assistant", usuario.getRol());
    }
    
    @Test
    @DisplayName("Validación de formatos de email")
    void testEmailFormatInModels() {
        // Usuario con diferentes formatos de email
        Usuario usuario = new Usuario();
        
        String[] emailsValidos = {
            "usuario@dominio.com",
            "usuario.nombre@dominio.co.uk",
            "usuario+tag@dominio.com",
            "usuario@sub.dominio.com",
            "u@d.com" // Email muy corto pero válido
        };
        
        for (String email : emailsValidos) {
            usuario.setEmail(email);
            assertEquals(email, usuario.getEmail());
        }
        
        // Estudiante con emails
        Estudiante estudiante = new Estudiante();
        
        estudiante.setEmail("estudiante2023@universidad.edu");
        assertEquals("estudiante2023@universidad.edu", estudiante.getEmail());
        
        // Email con caracteres especiales (técnicamente válido según RFC)
        estudiante.setEmail("estudiante+test@universidad.edu");
        assertEquals("estudiante+test@universidad.edu", estudiante.getEmail());
    }
    
    @Test
    @DisplayName("Validación de códigos de curso")
    void testCursoCodeFormat() {
        Curso curso = new Curso();
        
        // Formatos típicos de códigos de curso
        String[] codigosValidos = {
            "CS101",
            "MATH202",
            "PHYS-101",
            "BIO 101",
            "101", // Solo números
            "ABC", // Solo letras
            "CS101-A", // Con sufijo
            "CS101.1" // Con punto
        };
        
        for (String codigo : codigosValidos) {
            curso.setCodigo(codigo);
            assertEquals(codigo, curso.getCodigo());
        }
        
        // Código largo
        String codigoLargo = "INTRO-TO-COMPUTER-SCIENCE-101-A";
        curso.setCodigo(codigoLargo);
        assertEquals(codigoLargo, curso.getCodigo());
    }
    
    @Test
    @DisplayName("Validación de formatos de horario")
    void testCursoScheduleFormat() {
        Curso curso = new Curso();
        
        // Formatos típicos de horarios
        String[] horariosValidos = {
            "Lun/Mie 9:00-10:30",
            "Mar/Jue 11:00-12:30",
            "Lun/Mie/Vie 14:00-15:30",
            "Lun 8:00-10:00, Mie 8:00-10:00",
            "Flexible",
            "Por acuerdo",
            "Online"
        };
        
        for (String horario : horariosValidos) {
            curso.setHorario(horario);
            assertEquals(horario, curso.getHorario());
        }
        
        // Horario con detalles
        String horarioDetallado = "Lun: 9:00-10:30 (Teoría), Mie: 9:00-10:30 (Laboratorio)";
        curso.setHorario(horarioDetallado);
        assertEquals(horarioDetallado, curso.getHorario());
    }
    
    @Test
    @DisplayName("Validación de números de estudiante")
    void testStudentNumberFormat() {
        Estudiante estudiante = new Estudiante();
        
        // Formatos típicos
        String[] numerosValidos = {
            "2023001",
            "2023-001",
            "2023/001",
            "EST2023001",
            "S2023001",
            "001",
            "A2023001" // Con letra
        };
        
        for (String numero : numerosValidos) {
            estudiante.setNumeroEstudiante(numero);
            assertEquals(numero, estudiante.getNumeroEstudiante());
        }
        
        // Número con sufijo
        estudiante.setNumeroEstudiante("2023001-B");
        assertEquals("2023001-B", estudiante.getNumeroEstudiante());
    }
    
    @Test
    @DisplayName("Datos temporales en modelos")
    void testTemporalDataInModels() {
        // Test con fechas en Asistencia
        Asistencia asistencia = new Asistencia();
        
        Date ahora = new Date();
        asistencia.setFecha(ahora);
        assertEquals(ahora, asistencia.getFecha());
        
        // Verificar que es la misma referencia (no copia)
        assertSame(ahora, asistencia.getFecha());
        
        // Fecha en el pasado
        Date pasado = new Date(System.currentTimeMillis() - 86400000); // Ayer
        asistencia.setFecha(pasado);
        assertEquals(pasado, asistencia.getFecha());
        
        // Fecha en el futuro (posible para programación de asistencias)
        Date futuro = new Date(System.currentTimeMillis() + 86400000); // Mañana
        asistencia.setFecha(futuro);
        assertEquals(futuro, asistencia.getFecha());
        
        // Fecha null
        asistencia.setFecha(null);
        assertNull(asistencia.getFecha());
    }
    
    @Test
    @DisplayName("Manejo de observaciones y datos adicionales")
    void testObservationsAndAdditionalData() {
        Asistencia asistencia = new Asistencia();
        
        // Observaciones normales
        asistencia.setObservaciones("Llegó tarde por tráfico");
        assertEquals("Llegó tarde por tráfico", asistencia.getObservaciones());
        
        // Observaciones con saltos de línea
        String observacionMultilinea = "Justificación:\n" +
                                      "- Certificado médico adjunto\n" +
                                      "- Comunicado con coordinador\n" +
                                      "- Recuperación programada";
        asistencia.setObservaciones(observacionMultilinea);
        assertEquals(observacionMultilinea, asistencia.getObservaciones());
        
        // Observaciones vacías o null
        asistencia.setObservaciones("");
        assertEquals("", asistencia.getObservaciones());
        
        asistencia.setObservaciones(null);
        assertNull(asistencia.getObservaciones());
        
        // Observaciones con caracteres especiales para base de datos
        String observacionEspecial = "Observación con comillas 'simples' y \"dobles\" y \\backslashes\\";
        asistencia.setObservaciones(observacionEspecial);
        assertEquals(observacionEspecial, asistencia.getObservaciones());
    }
    
    @Test
    @DisplayName("Consistencia de datos entre modelos relacionados")
    void testDataConsistencyAcrossModels() {
        // Crear conjunto de datos consistente
        String profesorId = "PROF-001";
        String cursoId = "CURSO-101";
        String estudianteId = "EST-2023001";
        Date fechaAsistencia = new Date();
        
        // Crear modelos
        Usuario profesor = new Usuario(profesorId, "Profesor", "prof@edu.com", "teacher");
        Curso curso = new Curso(cursoId, "CS101", "Curso", "Horario", "Aula");
        curso.setProfesorId(profesorId);
        
        Estudiante estudiante = new Estudiante(estudianteId, "2023001", "Estudiante", "est@edu.com");
        Asistencia asistencia = new Asistencia(cursoId, estudianteId, fechaAsistencia, "present");
        
        // Verificar consistencia de IDs
        assertEquals(profesor.getId(), curso.getProfesorId());
        assertEquals(curso.getId(), asistencia.getCursoId());
        assertEquals(estudiante.getId(), asistencia.getEstudianteId());
        
        // Verificar que los IDs no están vacíos
        assertFalse(profesor.getId().isEmpty());
        assertFalse(curso.getId().isEmpty());
        assertFalse(estudiante.getId().isEmpty());
        assertFalse(asistencia.getCursoId().isEmpty());
        assertFalse(asistencia.getEstudianteId().isEmpty());
        
        // Verificar tipos de relación
        assertTrue(curso.getProfesorId().startsWith("PROF-"));
        assertTrue(asistencia.getCursoId().startsWith("CURSO-"));
        assertTrue(asistencia.getEstudianteId().startsWith("EST-"));
    }
    
    @Test
    @DisplayName("Resistencia a datos malformados")
    void testResilienceToMalformedData() {
        // Usuario con datos extremos
        Usuario usuario = new Usuario();
        usuario.setNombre(null);
        usuario.setEmail(null);
        usuario.setRol(null);
        usuario.setPassword(null);
        
        assertNull(usuario.getNombre());
        assertNull(usuario.getEmail());
        assertNull(usuario.getRol());
        assertNull(usuario.getPassword());
        
        // Estudiante con datos extremos
        Estudiante estudiante = new Estudiante();
        estudiante.setNombre("   "); // Solo espacios
        estudiante.setEmail("   ");
        estudiante.setStatus("   ");
        
        assertEquals("   ", estudiante.getNombre());
        assertEquals("   ", estudiante.getEmail());
        assertEquals("   ", estudiante.getStatus());
        
        // Curso con datos extremos
        Curso curso = new Curso();
        curso.setCodigo("\t\n\r"); // Caracteres de control
        curso.setNombre("\u0000\u0001\u0002"); // Caracteres no imprimibles
        
        assertEquals("\t\n\r", curso.getCodigo());
        assertEquals("\u0000\u0001\u0002", curso.getNombre());
        
        // Asistencia con datos extremos
        Asistencia asistencia = new Asistencia();
        asistencia.setEstado("PRESENTE"); // Mayúsculas (diferente de los valores esperados)
        asistencia.setObservaciones("\u0000"); // Null character
        
        assertEquals("PRESENTE", asistencia.getEstado());
        assertEquals("\u0000", asistencia.getObservaciones());
    }
}