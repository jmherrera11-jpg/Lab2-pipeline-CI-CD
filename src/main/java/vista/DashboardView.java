package vista;

import controlador.AuthController;
import controlador.CursoController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class DashboardView extends JFrame {
    private AuthController authController;
    private CursoController cursoController;
    
    public DashboardView(AuthController authController) {
        this.authController = authController;
        this.cursoController = new CursoController();
        initUI();
    }
    
    private void initUI() {
        setTitle("Academic Dashboard - " + authController.getRolActual().toUpperCase());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        
        String rol = authController.getRolActual();
        
        switch (rol) {
            case "teacher":
                mostrarDashboardTeacher();
                break;
            case "student":
                mostrarDashboardStudent();
                break;
            case "coordinator":
                mostrarDashboardCoordinator();
                break;
            default:
                JOptionPane.showMessageDialog(this, "Rol no reconocido: " + rol);
                break;
        }
    }
    
    private void mostrarDashboardTeacher() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(248, 250, 252));
        
        // Header con logout
        JPanel headerPanel = createHeaderPanel("Teacher Dashboard", 
            "Welcome back, Prof. Anderson. Select a course to manage attendance.");
        
        // Courses Grid
        JPanel coursesPanel = new JPanel(new GridLayout(0, 3, 20, 20));
        coursesPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        coursesPanel.setBackground(new Color(248, 250, 252));
        
        cursoController.obtenerTodosCursos().forEach(curso -> {
            JPanel courseCard = createCourseCard(curso);
            coursesPanel.add(courseCard);
        });
        
        JScrollPane scrollPane = new JScrollPane(coursesPanel);
        scrollPane.setBorder(null);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel createHeaderPanel(String title, String subtitle) {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        headerPanel.setPreferredSize(new Dimension(1200, 100));
        
        // Panel izquierdo con título
        JPanel leftPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        leftPanel.setOpaque(false);
        
        JLabel welcomeLabel = new JLabel(title);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(new Color(30, 41, 59));
        
        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(100, 116, 139));
        
        leftPanel.add(welcomeLabel);
        leftPanel.add(subtitleLabel);
        
        // Panel derecho con fecha y logout
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setOpaque(false);
        
        JLabel dateLabel = new JLabel(java.time.LocalDate.now().toString());
        dateLabel.setFont(new Font("Arial", Font.BOLD, 14));
        dateLabel.setForeground(new Color(37, 99, 235));
        dateLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(191, 219, 254), 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        dateLabel.setBackground(new Color(239, 246, 255));
        dateLabel.setOpaque(true);
        
        // Botón de logout
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.BOLD, 14));
        logoutButton.setBackground(new Color(239, 68, 68));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.addActionListener(this::handleLogout);
        
        rightPanel.add(dateLabel);
        rightPanel.add(logoutButton);
        
        headerPanel.add(leftPanel, BorderLayout.WEST);
        headerPanel.add(rightPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createCourseCard(modelo.Curso curso) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Top bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        
        JLabel codeLabel = new JLabel(curso.getCodigo());
        codeLabel.setFont(new Font("Monospaced", Font.BOLD, 11));
        codeLabel.setForeground(new Color(71, 85, 105));
        codeLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(241, 245, 249), 1),
            BorderFactory.createEmptyBorder(3, 8, 3, 8)
        ));
        codeLabel.setBackground(new Color(248, 250, 252));
        codeLabel.setOpaque(true);
        
        JLabel arrowLabel = new JLabel(">");
        arrowLabel.setFont(new Font("Arial", Font.BOLD, 16));
        arrowLabel.setForeground(new Color(203, 213, 225));
        
        topBar.add(codeLabel, BorderLayout.WEST);
        topBar.add(arrowLabel, BorderLayout.EAST);
        
        // Course name
        JLabel nameLabel = new JLabel(curso.getNombre());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nameLabel.setForeground(new Color(30, 41, 59));
        
        // Details
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setOpaque(false);
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        addDetail(detailsPanel, "Schedule: " + curso.getHorario());
        addDetail(detailsPanel, "Room: " + curso.getAula());
        addDetail(detailsPanel, "Students: 32 Enrolled");
        
        // Footer
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(new Color(248, 250, 252));
        footerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(241, 245, 249)),
            BorderFactory.createEmptyBorder(10, 0, 0, 0)
        ));
        
        JLabel nextLabel = new JLabel("Next Session");
        nextLabel.setFont(new Font("Arial", Font.BOLD, 10));
        nextLabel.setForeground(new Color(100, 116, 139));
        
        JLabel timeLabel = new JLabel("Today, 09:00 AM");
        timeLabel.setFont(new Font("Arial", Font.BOLD, 12));
        timeLabel.setForeground(new Color(30, 41, 59));
        
        footerPanel.add(nextLabel, BorderLayout.WEST);
        footerPanel.add(timeLabel, BorderLayout.EAST);
        
        card.add(topBar, BorderLayout.NORTH);
        card.add(nameLabel, BorderLayout.CENTER);
        card.add(detailsPanel, BorderLayout.SOUTH);
        card.add(footerPanel, BorderLayout.AFTER_LAST_LINE);
        
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                new AttendanceRegisterView(curso, authController).setVisible(true);
            }
        });
        
        return card;
    }
    
    private void addDetail(JPanel panel, String text) {
        JPanel detailPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        detailPanel.setOpaque(false);
        
        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        textLabel.setForeground(new Color(71, 85, 105));
        
        detailPanel.add(textLabel);
        panel.add(detailPanel);
    }
    
    private void mostrarDashboardStudent() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(248, 250, 252));
        
        // Header con logout
        JPanel headerPanel = createHeaderPanel("Student Dashboard", 
            "Overview of your academic attendance performance.");
        
        // Panel de estadísticas
        JPanel statsPanel = createStudentStatsPanel();
        
        // Panel de actividad reciente
        JPanel activityPanel = createStudentActivityPanel();
        
        JPanel contentPanel = new JPanel(new BorderLayout(0, 20));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 30, 30));
        contentPanel.setBackground(new Color(248, 250, 252));
        contentPanel.add(statsPanel, BorderLayout.NORTH);
        contentPanel.add(activityPanel, BorderLayout.CENTER);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel createStudentStatsPanel() {
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 15, 15));
        statsPanel.setBackground(Color.WHITE);
        statsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // Present stat
        JPanel presentPanel = createStatCard("Present", "85%", new Color(34, 197, 94));
        
        // Late stat
        JPanel latePanel = createStatCard("Late", "10%", new Color(245, 158, 11));
        
        // Absent stat
        JPanel absentPanel = createStatCard("Absent", "5%", new Color(239, 68, 68));
        
        statsPanel.add(presentPanel);
        statsPanel.add(latePanel);
        statsPanel.add(absentPanel);
        
        return statsPanel;
    }
    
    private JPanel createStatCard(String title, String value, Color color) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 20));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(color.getRed(), color.getGreen(), color.getBlue(), 50), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JPanel leftPanel = new JPanel();
        leftPanel.setOpaque(false);
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(color);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        valueLabel.setForeground(darkenColor(color, 0.3f));
        
        leftPanel.add(titleLabel);
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(valueLabel);
        
        panel.add(leftPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private Color darkenColor(Color color, float factor) {
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        return Color.getHSBColor(hsb[0], hsb[1], hsb[2] * factor);
    }
    
    private JPanel createStudentActivityPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JLabel titleLabel = new JLabel("Recent Activity");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(30, 41, 59));
        
        // Tabla de actividad
        String[] columnNames = {"Date", "Course", "Status"};
        Object[][] data = {
            {"2024-03-10", "Database Systems", "present"},
            {"2024-03-09", "Calculus I", "late"},
            {"2024-03-08", "Intro to CS", "present"},
            {"2024-03-05", "Database Systems", "absent"}
        };
        
        JTable table = new JTable(data, columnNames);
        table.setRowHeight(40);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(248, 250, 252));
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240), 1));
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void mostrarDashboardCoordinator() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(248, 250, 252));
        
        // Header con logout
        JPanel headerPanel = createHeaderPanel("Academic Coordinator Dashboard", 
            "System overview and attendance reporting.");
        
        // Panel de estadísticas
        JPanel statsPanel = createCoordinatorStatsPanel();
        
        // Panel de cursos
        JPanel coursesPanel = createCoordinatorCoursesPanel();
        
        // Panel de botones de acción
        JPanel actionPanel = createCoordinatorActionPanel();
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 30, 30));
        contentPanel.setBackground(new Color(248, 250, 252));
        
        contentPanel.add(statsPanel);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(coursesPanel);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(actionPanel);
        
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(mainPanel);
    }

    private JPanel createCoordinatorActionPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 15, 15)); // Cambiado a 2x2
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // Botón de generar reportes
        JButton reportsButton = createActionButton("View Reports", 
            "Generate and view attendance reports", new Color(59, 130, 246));
        reportsButton.addActionListener(e -> {
            new CoordinatorReportView(authController).setVisible(true);
        });
        
        // Botón de gestionar cursos
        JButton manageCoursesButton = createActionButton("Manage Courses", 
            "Add, edit or remove courses", new Color(168, 85, 247));
        manageCoursesButton.addActionListener(e -> {
            new GestionCursosView(authController).setVisible(true);
        });
        
        // Botón de gestionar estudiantes
        JButton manageStudentsButton = createActionButton("Manage Students", 
            "Add, edit or remove students", new Color(34, 197, 94));
        manageStudentsButton.addActionListener(e -> {
            new GestionEstudiantesView(authController).setVisible(true);
        });
        
        // NUEVO BOTÓN: Gestionar profesores
        JButton manageTeachersButton = createActionButton("Manage Teachers", 
            "Add, edit or remove teachers", new Color(245, 158, 11)); // Color naranja
        manageTeachersButton.addActionListener(e -> {
            new GestionProfesoresView(authController).setVisible(true);
        });
        
        panel.add(reportsButton);
        panel.add(manageCoursesButton);
        panel.add(manageStudentsButton);
        panel.add(manageTeachersButton);
        
        return panel;
    }

    private JButton createActionButton(String title, String description, Color color) {
        JButton button = new JButton();
        button.setLayout(new BorderLayout());
        button.setBackground(Color.WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        
        // Icon panel
        JPanel iconPanel = new JPanel(new GridBagLayout());
        iconPanel.setPreferredSize(new Dimension(40, 40));
        iconPanel.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 20));
        iconPanel.setBorder(BorderFactory.createLineBorder(
            new Color(color.getRed(), color.getGreen(), color.getBlue(), 50), 1));
        
        JLabel iconLabel = new JLabel(title.substring(0, 1));
        iconLabel.setFont(new Font("Arial", Font.BOLD, 16));
        iconLabel.setForeground(color);
        iconPanel.add(iconLabel);
        
        // Text panel
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        textPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(new Color(30, 41, 59));
        
        JLabel descLabel = new JLabel("<html><div style='width: 200px'>" + description + "</div></html>");
        descLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        descLabel.setForeground(new Color(100, 116, 139));
        
        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalStrut(5));
        textPanel.add(descLabel);
        
        // Arrow
        JLabel arrowLabel = new JLabel(">");
        arrowLabel.setFont(new Font("Arial", Font.BOLD, 20));
        arrowLabel.setForeground(new Color(203, 213, 225));
        arrowLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        
        contentPanel.add(iconPanel);
        contentPanel.add(textPanel);
        
        button.add(contentPanel, BorderLayout.CENTER);
        button.add(arrowLabel, BorderLayout.EAST);
        
        return button;
    }
    
    private JPanel createCoordinatorStatsPanel() {
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 15, 15));
        
        // Total Students
        JPanel studentsPanel = createMetricCard("Total Students", "142", 
            new Color(59, 130, 246), "Students");
        
        // Average Attendance
        JPanel attendancePanel = createMetricCard("Avg Attendance", "88.4%", 
            new Color(168, 85, 247), "Overall");
        
        // At Risk
        JPanel riskPanel = createMetricCard("At Risk", "3", 
            new Color(245, 158, 11), "Students");
        
        statsPanel.add(studentsPanel);
        statsPanel.add(attendancePanel);
        statsPanel.add(riskPanel);
        
        return statsPanel;
    }
    
    private JPanel createMetricCard(String title, String value, Color color, String unit) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JPanel contentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        contentPanel.setOpaque(false);
        
        // Icon panel
        JPanel iconPanel = new JPanel(new GridBagLayout());
        iconPanel.setPreferredSize(new Dimension(50, 50));
        iconPanel.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 20));
        iconPanel.setBorder(BorderFactory.createLineBorder(
            new Color(color.getRed(), color.getGreen(), color.getBlue(), 50), 1));
        
        JLabel iconLabel = new JLabel(title.substring(0, 1));
        iconLabel.setFont(new Font("Arial", Font.BOLD, 18));
        iconLabel.setForeground(color);
        iconPanel.add(iconLabel);
        
        // Text panel
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(new Color(100, 116, 139));
        
        JPanel valuePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        valuePanel.setOpaque(false);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        valueLabel.setForeground(new Color(30, 41, 59));
        
        JLabel unitLabel = new JLabel(" " + unit);
        unitLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        unitLabel.setForeground(new Color(148, 163, 184));
        
        valuePanel.add(valueLabel);
        valuePanel.add(unitLabel);
        
        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalStrut(5));
        textPanel.add(valuePanel);
        
        contentPanel.add(iconPanel);
        contentPanel.add(textPanel);
        
        panel.add(contentPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createCoordinatorCoursesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JLabel titleLabel = new JLabel("Course Overview");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(30, 41, 59));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        // Tabla de cursos
        String[] columnNames = {"Course Code", "Course Name", "Schedule", "Attendance Rate"};
        Object[][] data = {
            {"CS101", "Database Systems", "Mon/Wed 9:00-10:30", "92%"},
            {"CS102", "Calculus I", "Tue/Thu 11:00-12:30", "88%"},
            {"CS103", "Intro to CS", "Mon/Fri 14:00-15:30", "95%"}
        };
        
        JTable table = new JTable(data, columnNames);
        table.setRowHeight(50);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(248, 250, 252));
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        
        // Personalizar columna de attendance rate
        table.getColumnModel().getColumn(3).setCellRenderer(new AttendanceRateRenderer());
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240), 1));
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private class AttendanceRateRenderer extends JPanel implements javax.swing.table.TableCellRenderer {
        private JLabel percentageLabel;
        private JProgressBar progressBar;
        
        public AttendanceRateRenderer() {
            setLayout(new BorderLayout(10, 0));
            setOpaque(true);
            
            percentageLabel = new JLabel();
            percentageLabel.setFont(new Font("Arial", Font.BOLD, 12));
            
            progressBar = new JProgressBar(0, 100);
            progressBar.setPreferredSize(new Dimension(100, 8));
            progressBar.setBorderPainted(false);
            progressBar.setStringPainted(false);
            
            add(percentageLabel, BorderLayout.WEST);
            add(progressBar, BorderLayout.CENTER);
        }
        
        @Override
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            String percentage = value.toString().replace("%", "");
            int percentValue = Integer.parseInt(percentage);
            
            percentageLabel.setText(value.toString());
            progressBar.setValue(percentValue);
            
            // Color basado en el porcentaje
            if (percentValue >= 90) {
                progressBar.setForeground(new Color(34, 197, 94));
                percentageLabel.setForeground(new Color(34, 197, 94));
            } else if (percentValue >= 80) {
                progressBar.setForeground(new Color(245, 158, 11));
                percentageLabel.setForeground(new Color(245, 158, 11));
            } else {
                progressBar.setForeground(new Color(239, 68, 68));
                percentageLabel.setForeground(new Color(239, 68, 68));
            }
            
            // Fondo basado en selección
            if (isSelected) {
                setBackground(new Color(239, 246, 255));
            } else {
                setBackground(Color.WHITE);
            }
            
            return this;
        }
    }
    
    private void handleLogout(ActionEvent e) {
        authController.logout();
        dispose();
        new LoginView(authController).setVisible(true);
    }
}