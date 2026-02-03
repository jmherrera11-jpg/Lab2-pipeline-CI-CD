package vista;

import controlador.AsistenciaController;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class AttendanceRegisterView extends JFrame {
    private modelo.Curso curso;
    private AsistenciaController asistenciaController;
    private Map<String, JRadioButton[]> studentButtons;
    private controlador.AuthController authController;
    
    public AttendanceRegisterView(modelo.Curso curso, controlador.AuthController authController) {
        this.curso = curso;
        this.authController = authController;
        this.asistenciaController = new AsistenciaController();
        this.studentButtons = new HashMap<>();
        initUI();
    }
    
    private void initUI() {
        setTitle("Attendance Register - " + curso.getNombre());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(248, 250, 252));
        
        // Header con botón de back y logout
        JPanel headerPanel = createHeaderPanel();
        
        // Course Info Card
        JPanel courseInfoPanel = createCourseInfoPanel();
        
        // Info Alert
        JPanel infoAlertPanel = createInfoAlertPanel();
        
        // Students Table
        JPanel studentsTablePanel = createStudentsTablePanel();
        
        // Layout principal
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(248, 250, 252));
        
        contentPanel.add(courseInfoPanel);
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(infoAlertPanel);
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(studentsTablePanel);
        
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(248, 250, 252));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        // Botón de back
        JButton backButton = new JButton("< Back to Dashboard");
        backButton.setFont(new Font("Arial", Font.PLAIN, 13));
        backButton.setForeground(new Color(100, 116, 139));
        backButton.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        backButton.setContentAreaFilled(false);
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
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);
        rightPanel.add(logoutButton);
        
        headerPanel.add(backButton, BorderLayout.WEST);
        headerPanel.add(rightPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createCourseInfoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // Left panel with course info
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);
        
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        titlePanel.setOpaque(false);
        
        JLabel courseNameLabel = new JLabel(curso.getNombre());
        courseNameLabel.setFont(new Font("Arial", Font.BOLD, 22));
        courseNameLabel.setForeground(new Color(30, 41, 59));
        
        JLabel codeLabel = new JLabel(curso.getCodigo());
        codeLabel.setFont(new Font("Monospaced", Font.BOLD, 12));
        codeLabel.setForeground(new Color(71, 85, 105));
        codeLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        codeLabel.setBackground(new Color(248, 250, 252));
        codeLabel.setOpaque(true);
        
        titlePanel.add(courseNameLabel);
        titlePanel.add(codeLabel);
        
        JPanel detailsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        detailsPanel.setOpaque(false);
        
        addDetail(detailsPanel, "Schedule: " + curso.getHorario());
        addDetail(detailsPanel, "Room: " + curso.getAula());
        addDetail(detailsPanel, "Date: " + java.time.LocalDate.now().toString());
        
        leftPanel.add(titlePanel);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(detailsPanel);
        
        // Right panel with save button
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);
        
        JButton saveButton = new JButton("Save Attendance");
        saveButton.setFont(new Font("Arial", Font.BOLD, 14));
        saveButton.setBackground(new Color(37, 99, 235));
        saveButton.setForeground(Color.WHITE);
        saveButton.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveButton.addActionListener(e -> guardarAsistencia());
        
        rightPanel.add(saveButton);
        
        panel.add(leftPanel, BorderLayout.CENTER);
        panel.add(rightPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel createInfoAlertPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(new Color(239, 246, 255));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(191, 219, 254), 1),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        JLabel iconLabel = new JLabel("i");
        iconLabel.setFont(new Font("Arial", Font.BOLD, 16));
        iconLabel.setForeground(new Color(37, 99, 235));
        
        JLabel infoLabel = new JLabel(
            "<html>Attendance recording is open until <b>10:45 AM</b>. Changes after this time will require administrator approval. " +
            "Please ensure all students are marked correctly before saving.</html>"
        );
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        infoLabel.setForeground(new Color(30, 64, 175));
        
        panel.add(iconLabel);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(infoLabel);
        
        return panel;
    }
    
    private JPanel createStudentsTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240), 1));
        
        // Create table model
        String[] columnNames = {"#", "Student Name", "Student ID", "Status", "Remarks"};
        Object[][] data = new Object[asistenciaController.obtenerTodosEstudiantes().size()][5];
        
        for (int i = 0; i < asistenciaController.obtenerTodosEstudiantes().size(); i++) {
            modelo.Estudiante est = asistenciaController.obtenerTodosEstudiantes().get(i);
            data[i][0] = i + 1;
            data[i][1] = est.getNombre() + (est.getStatus().equals("inactive") ? " (Inactive)" : "");
            data[i][2] = est.getNumeroEstudiante();
            data[i][3] = est.getId(); // Store ID for renderer
            data[i][4] = ""; // Remarks
        }
        
        JTable table = new JTable(data, columnNames) {
            public boolean isCellEditable(int row, int column) {
                return column == 3 || column == 4;
            }
            
            public Class<?> getColumnClass(int column) {
                return column == 3 ? Object.class : String.class;
            }
        };
        
        table.setRowHeight(50);
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);
        table.getColumnModel().getColumn(3).setPreferredWidth(250);
        table.getColumnModel().getColumn(4).setPreferredWidth(250);
        
        // Set custom renderer for status column
        table.getColumnModel().getColumn(3).setCellRenderer(new StatusCellRenderer());
        table.getColumnModel().getColumn(3).setCellEditor(new StatusCellEditor());
        
        // Set editor for remarks column
        table.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(new JTextField()));
        
        JScrollPane scrollPane = new JScrollPane(table);
        
        // Footer with statistics
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(new Color(248, 250, 252));
        footerPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(226, 232, 240)));
        footerPanel.setPreferredSize(new Dimension(100, 50));
        
        JLabel countLabel = new JLabel("Showing " + asistenciaController.obtenerTodosEstudiantes().size() + " students");
        countLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        countLabel.setForeground(new Color(100, 116, 139));
        countLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        statsPanel.setOpaque(false);
        
        statsPanel.add(createStatLabel("Present: 0", new Color(34, 197, 94)));
        statsPanel.add(createStatLabel("Late: 0", new Color(245, 158, 11)));
        statsPanel.add(createStatLabel("Absent: 0", new Color(239, 68, 68)));
        
        footerPanel.add(countLabel, BorderLayout.WEST);
        footerPanel.add(statsPanel, BorderLayout.EAST);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(footerPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JLabel createStatLabel(String text, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setForeground(color);
        return label;
    }
    
    private void addDetail(JPanel panel, String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 13));
        label.setForeground(new Color(100, 116, 139));
        panel.add(label);
    }
    
    private void guardarAsistencia() {
        Map<String, String> asistencias = new HashMap<>();
        int presentCount = 0, lateCount = 0, absentCount = 0;
        
        for (Map.Entry<String, JRadioButton[]> entry : studentButtons.entrySet()) {
            String studentId = entry.getKey();
            JRadioButton[] buttons = entry.getValue();
            
            String status = "present";
            if (buttons[1].isSelected()) {
                status = "late";
                lateCount++;
            } else if (buttons[2].isSelected()) {
                status = "absent";
                absentCount++;
            } else {
                presentCount++;
            }
            
            asistencias.put(studentId, status);
        }
        
        asistenciaController.registrarAsistencia(curso.getId(), asistencias);
        
        JOptionPane.showMessageDialog(this, 
            "Attendance saved successfully for " + asistencias.size() + " students.\n" +
            "Present: " + presentCount + " | Late: " + lateCount + " | Absent: " + absentCount,
            "Success",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Custom cell renderer for status column
    private class StatusCellRenderer implements javax.swing.table.TableCellRenderer {
        private JPanel panel;
        private JRadioButton presentBtn, lateBtn, absentBtn;
        private ButtonGroup group;
        
        public StatusCellRenderer() {
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            panel.setOpaque(true);
            
            presentBtn = new JRadioButton("Present");
            lateBtn = new JRadioButton("Late");
            absentBtn = new JRadioButton("Absent");
            
            group = new ButtonGroup();
            group.add(presentBtn);
            group.add(lateBtn);
            group.add(absentBtn);
            
            styleRadioButton(presentBtn, new Color(34, 197, 94));
            styleRadioButton(lateBtn, new Color(245, 158, 11));
            styleRadioButton(absentBtn, new Color(239, 68, 68));
            
            panel.add(presentBtn);
            panel.add(lateBtn);
            panel.add(absentBtn);
        }
        
        private void styleRadioButton(JRadioButton button, Color color) {
            button.setBackground(Color.WHITE);
            button.setFont(new Font("Arial", Font.PLAIN, 11));
            button.setFocusPainted(false);
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            button.addChangeListener(e -> {
                if (button.isSelected()) {
                    button.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 30));
                    button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(color, 1),
                        BorderFactory.createEmptyBorder(3, 10, 3, 10)
                    ));
                    button.setForeground(color);
                } else {
                    button.setBackground(Color.WHITE);
                    button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                        BorderFactory.createEmptyBorder(3, 10, 3, 10)
                    ));
                    button.setForeground(new Color(100, 116, 139));
                }
            });
        }
        
        @Override
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            String studentId = value.toString();
            
            // Store reference for editor
            JRadioButton[] buttons = new JRadioButton[]{presentBtn, lateBtn, absentBtn};
            studentButtons.put(studentId, buttons);
            
            // Set selection based on default (all present)
            presentBtn.setSelected(true);
            lateBtn.setSelected(false);
            absentBtn.setSelected(false);
            
            // Trigger styling
            presentBtn.setSelected(true);
            
            if (isSelected) {
                panel.setBackground(new Color(239, 246, 255));
            } else {
                panel.setBackground(Color.WHITE);
            }
            
            return panel;
        }
    }
    
    // Custom cell editor for status column
    private class StatusCellEditor extends javax.swing.AbstractCellEditor 
            implements javax.swing.table.TableCellEditor {
        
        private JPanel panel;
        private JRadioButton presentBtn, lateBtn, absentBtn;
        private ButtonGroup group;
        private String currentStatus = "present";
        
        public StatusCellEditor() {
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            panel.setOpaque(true);
            
            presentBtn = new JRadioButton("Present");
            lateBtn = new JRadioButton("Late");
            absentBtn = new JRadioButton("Absent");
            
            group = new ButtonGroup();
            group.add(presentBtn);
            group.add(lateBtn);
            group.add(absentBtn);
            
            styleRadioButton(presentBtn, new Color(34, 197, 94));
            styleRadioButton(lateBtn, new Color(245, 158, 11));
            styleRadioButton(absentBtn, new Color(239, 68, 68));
            
            // Add action listeners
            presentBtn.addActionListener(e -> {
                currentStatus = "present";
                fireEditingStopped();
            });
            
            lateBtn.addActionListener(e -> {
                currentStatus = "late";
                fireEditingStopped();
            });
            
            absentBtn.addActionListener(e -> {
                currentStatus = "absent";
                fireEditingStopped();
            });
            
            panel.add(presentBtn);
            panel.add(lateBtn);
            panel.add(absentBtn);
        }
        
        private void styleRadioButton(JRadioButton button, Color color) {
            button.setBackground(Color.WHITE);
            button.setFont(new Font("Arial", Font.PLAIN, 11));
            button.setFocusPainted(false);
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            button.addChangeListener(e -> {
                if (button.isSelected()) {
                    button.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 30));
                    button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(color, 1),
                        BorderFactory.createEmptyBorder(3, 10, 3, 10)
                    ));
                    button.setForeground(color);
                } else {
                    button.setBackground(Color.WHITE);
                    button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                        BorderFactory.createEmptyBorder(3, 10, 3, 10)
                    ));
                    button.setForeground(new Color(100, 116, 139));
                }
            });
        }
        
        @Override
        public java.awt.Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            
            String studentId = value.toString();
            
            // Store reference
            JRadioButton[] buttons = new JRadioButton[]{presentBtn, lateBtn, absentBtn};
            studentButtons.put(studentId, buttons);
            
            // Set initial selection
            presentBtn.setSelected(true);
            lateBtn.setSelected(false);
            absentBtn.setSelected(false);
            currentStatus = "present";
            
            // Trigger styling
            presentBtn.setSelected(true);
            
            if (isSelected) {
                panel.setBackground(new Color(239, 246, 255));
            } else {
                panel.setBackground(Color.WHITE);
            }
            
            return panel;
        }
        
        @Override
        public Object getCellEditorValue() {
            return currentStatus;
        }
    }
}