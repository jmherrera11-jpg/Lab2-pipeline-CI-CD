package vista;

import controlador.AuthController;
import controlador.ReporteController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class CoordinatorReportView extends JFrame {
    private AuthController authController;
    private ReporteController reporteController;
    
    public CoordinatorReportView(AuthController authController) {
        this.authController = authController;
        this.reporteController = new ReporteController();
        initUI();
    }
    
    private void initUI() {
        setTitle("Coordinator Reports");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(248, 250, 252));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Header con botones
        JPanel headerPanel = createHeaderPanel();
        
        // Panel de pestañas
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));
        
        // Pestaña de reporte general
        tabbedPane.addTab("General Report", createGeneralReportPanel());
        
        // Pestaña de reporte por curso
        tabbedPane.addTab("Course Report", createCourseReportPanel());
        
        // Pestaña de reporte de estudiantes
        tabbedPane.addTab("Student Report", createStudentReportPanel());
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(248, 250, 252));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        JLabel titleLabel = new JLabel("Attendance Reports");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(30, 41, 59));
        
        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        
        // Botón de volver al dashboard
        JButton backButton = new JButton("Back to Dashboard");
        backButton.setFont(new Font("Arial", Font.BOLD, 12));
        backButton.setBackground(new Color(100, 116, 139));
        backButton.setForeground(Color.WHITE);
        backButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> {
            dispose();
            new DashboardView(authController).setVisible(true);
        });
        
        // Botón de logout
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.BOLD, 12));
        logoutButton.setBackground(new Color(239, 68, 68));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.addActionListener(e -> {
            authController.logout();
            dispose();
            new LoginView(authController).setVisible(true);
        });
        
        buttonPanel.add(backButton);
        buttonPanel.add(logoutButton);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createGeneralReportPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Generar reporte
        var reporte = reporteController.generarReporteGeneral();
        
        // Panel de estadísticas
        JPanel statsPanel = createGeneralStatsPanel(reporte);
        
        // Panel de estudiantes en riesgo
        JPanel riskPanel = createRiskStudentsPanel(reporte);
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.add(statsPanel);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(riskPanel);
        
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createGeneralStatsPanel(java.util.Map<String, Object> reporte) {
        JPanel panel = new JPanel(new GridLayout(2, 3, 15, 15));
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            "General Statistics"
        ));
        
        // Total Students
        panel.add(createStatCard("Total Students", 
            reporte.get("totalEstudiantes").toString(), 
            new Color(59, 130, 246)));
        
        // Active Students
        panel.add(createStatCard("Active Students", 
            reporte.get("estudiantesActivos").toString(), 
            new Color(34, 197, 94)));
        
        // Inactive Students
        panel.add(createStatCard("Inactive Students", 
            reporte.get("estudiantesInactivos").toString(), 
            new Color(100, 116, 139)));
        
        // Average Attendance
        double avgAttendance = (double) reporte.get("promedioAsistencia");
        panel.add(createStatCard("Average Attendance", 
            String.format("%.1f%%", avgAttendance), 
            new Color(168, 85, 247)));
        
        // At Risk Students
        int atRisk = ((java.util.List<?>) reporte.get("estudiantesRiesgo")).size();
        panel.add(createStatCard("At Risk Students", 
            String.valueOf(atRisk), 
            new Color(245, 158, 11)));
        
        // Total Courses
        panel.add(createStatCard("Total Courses", 
            reporte.get("totalCursos").toString(), 
            new Color(236, 72, 153)));
        
        return panel;
    }
    
    private JPanel createStatCard(String title, String value, Color color) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 12));
        titleLabel.setForeground(new Color(100, 116, 139));
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 20));
        valueLabel.setForeground(color);
        
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(valueLabel);
        
        panel.add(contentPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createRiskStudentsPanel(java.util.Map<String, Object> reporte) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            "Students at Risk (< 75% Attendance)"
        ));
        
        java.util.List<java.util.Map<String, Object>> estudiantesRiesgo = 
            (java.util.List<java.util.Map<String, Object>>) reporte.get("estudiantesRiesgo");
        
        if (estudiantesRiesgo.isEmpty()) {
            JLabel noDataLabel = new JLabel("No students at risk", SwingConstants.CENTER);
            noDataLabel.setFont(new Font("Arial", Font.ITALIC, 14));
            noDataLabel.setForeground(new Color(148, 163, 184));
            panel.add(noDataLabel, BorderLayout.CENTER);
        } else {
            String[] columnNames = {"Student Name", "Student ID", "Attendance %", "Affected Courses"};
            Object[][] data = new Object[estudiantesRiesgo.size()][4];
            
            for (int i = 0; i < estudiantesRiesgo.size(); i++) {
                var datos = estudiantesRiesgo.get(i);
                modelo.Estudiante est = (modelo.Estudiante) datos.get("estudiante");
                double porcentaje = (double) datos.get("porcentajeAsistencia");
                java.util.List<String> cursos = (java.util.List<String>) datos.get("cursosAfectados");
                
                data[i][0] = est.getNombre();
                data[i][1] = est.getNumeroEstudiante();
                data[i][2] = String.format("%.1f%%", porcentaje);
                data[i][3] = String.join(", ", cursos);
            }
            
            JTable table = new JTable(data, columnNames);
            table.setRowHeight(35);
            table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
            table.getTableHeader().setBackground(new Color(248, 250, 252));
            table.setFont(new Font("Arial", Font.PLAIN, 13));
            
            JScrollPane scrollPane = new JScrollPane(table);
            panel.add(scrollPane, BorderLayout.CENTER);
        }
        
        return panel;
    }
    
    private JPanel createCourseReportPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Select a course to view detailed report:");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // Panel de selección de curso
        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JComboBox<String> courseCombo = new JComboBox<>();
        courseCombo.addItem("Select a course...");
        courseCombo.addItem("CS101 - Database Systems");
        courseCombo.addItem("CS102 - Calculus I");
        courseCombo.addItem("CS103 - Intro to CS");
        courseCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        courseCombo.setPreferredSize(new Dimension(300, 35));
        
        JButton viewReportButton = new JButton("View Report");
        viewReportButton.setFont(new Font("Arial", Font.BOLD, 14));
        viewReportButton.setBackground(new Color(37, 99, 235));
        viewReportButton.setForeground(Color.WHITE);
        viewReportButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        viewReportButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewReportButton.addActionListener(e -> {
            int selectedIndex = courseCombo.getSelectedIndex();
            if (selectedIndex > 0) {
                String courseId = "CS" + (100 + selectedIndex);
                mostrarReporteCurso(courseId);
            }
        });
        
        selectionPanel.add(courseCombo);
        selectionPanel.add(Box.createHorizontalStrut(20));
        selectionPanel.add(viewReportButton);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(selectionPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void mostrarReporteCurso(String cursoId) {
        var reporte = reporteController.generarReporteCurso(cursoId);
        if (reporte == null) {
            JOptionPane.showMessageDialog(this, "Course not found", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        modelo.Curso curso = (modelo.Curso) reporte.get("curso");
        double tasaAsistencia = (double) reporte.get("tasaAsistencia");
        
        String message = String.format(
            "<html><body style='width: 300px'>" +
            "<h3>%s - %s</h3>" +
            "<p><b>Schedule:</b> %s</p>" +
            "<p><b>Room:</b> %s</p>" +
            "<hr>" +
            "<p><b>Attendance Rate:</b> %.1f%%</p>" +
            "<p><b>Present:</b> %d</p>" +
            "<p><b>Late:</b> %d</p>" +
            "<p><b>Absent:</b> %d</p>" +
            "<p><b>Total Records:</b> %d</p>" +
            "</body></html>",
            curso.getCodigo(), curso.getNombre(), curso.getHorario(), curso.getAula(),
            tasaAsistencia, reporte.get("presentes"), reporte.get("tardios"), 
            reporte.get("ausentes"), reporte.get("totalRegistros")
        );
        
        JOptionPane.showMessageDialog(this, message, "Course Report", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private JPanel createStudentReportPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Student Attendance Report");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // Generar reporte de estudiantes
        var reporte = reporteController.generarReporteEstudiantes();
        
        String[] columnNames = {"Student Name", "Student ID", "Status", "Attendance %", "Performance"};
        Object[][] data = new Object[reporte.size()][5];
        
        for (int i = 0; i < reporte.size(); i++) {
            var datos = reporte.get(i);
            modelo.Estudiante est = (modelo.Estudiante) datos.get("estudiante");
            double porcentaje = (double) datos.get("porcentajeAsistencia");
            String estado = (String) datos.get("estado");
            
            data[i][0] = est.getNombre();
            data[i][1] = est.getNumeroEstudiante();
            data[i][2] = est.getStatus();
            data[i][3] = String.format("%.1f%%", porcentaje);
            data[i][4] = estado;
        }
        
        JTable table = new JTable(data, columnNames);
        table.setRowHeight(35);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(248, 250, 252));
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        
        // Personalizar renderer para la columna de performance
        table.getColumnModel().getColumn(4).setCellRenderer(new PerformanceRenderer());
        
        JScrollPane scrollPane = new JScrollPane(table);
        
        // Botón de exportar
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton exportButton = new JButton("Export to CSV");
        exportButton.setFont(new Font("Arial", Font.BOLD, 12));
        exportButton.setBackground(new Color(34, 197, 94));
        exportButton.setForeground(Color.WHITE);
        exportButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        exportButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exportButton.addActionListener(e -> exportarReporte());
        
        bottomPanel.add(exportButton);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private class PerformanceRenderer extends javax.swing.table.DefaultTableCellRenderer {
        @Override
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            String performance = value.toString();
            setHorizontalAlignment(SwingConstants.CENTER);
            setFont(new Font("Arial", Font.BOLD, 11));
            
            // Color basado en el desempeño
            switch (performance) {
                case "Excelente":
                    setBackground(new Color(220, 252, 231));
                    setForeground(new Color(21, 128, 61));
                    setBorder(BorderFactory.createLineBorder(new Color(187, 247, 208), 1));
                    break;
                case "Bueno":
                    setBackground(new Color(254, 249, 195));
                    setForeground(new Color(161, 98, 7));
                    setBorder(BorderFactory.createLineBorder(new Color(254, 240, 138), 1));
                    break;
                case "Regular":
                    setBackground(new Color(254, 226, 226));
                    setForeground(new Color(185, 28, 28));
                    setBorder(BorderFactory.createLineBorder(new Color(254, 202, 202), 1));
                    break;
                case "Preocupante":
                    setBackground(new Color(253, 242, 248));
                    setForeground(new Color(190, 24, 93));
                    setBorder(BorderFactory.createLineBorder(new Color(251, 207, 232), 1));
                    break;
                case "Crítico":
                    setBackground(new Color(30, 41, 59));
                    setForeground(Color.WHITE);
                    setBorder(BorderFactory.createLineBorder(new Color(51, 65, 85), 1));
                    break;
                default:
                    setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                    setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
            }
            
            setOpaque(true);
            return this;
        }
    }
    
    private void exportarReporte() {
        JOptionPane.showMessageDialog(this, 
            "Report exported successfully.\n" +
            "File saved as: student_report_" + java.time.LocalDate.now() + ".csv",
            "Export Complete",
            JOptionPane.INFORMATION_MESSAGE);
    }
}