package vista;

import controlador.AuthController;
import controlador.EstudianteController;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class GestionEstudiantesView extends JFrame {
    private AuthController authController;
    private EstudianteController estudianteController;
    private JTable tablaEstudiantes;
    private DefaultTableModel modeloTabla;
    
    public GestionEstudiantesView(AuthController authController) {
        this.authController = authController;
        this.estudianteController = new EstudianteController();
        initUI();
        cargarEstudiantes();
    }
    
    private void initUI() {
        setTitle("Gestión de Estudiantes - Coordinador");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(248, 250, 252));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Header
        JPanel headerPanel = createHeaderPanel();
        
        // Panel de búsqueda
        JPanel searchPanel = createSearchPanel();
        
        // Panel de tabla
        JPanel tablePanel = createTablePanel();
        
        // Panel de botones de acción
        JPanel actionPanel = createActionPanel();
        
        JPanel contentPanel = new JPanel(new BorderLayout(0, 15));
        contentPanel.setBackground(new Color(248, 250, 252));
        contentPanel.add(searchPanel, BorderLayout.NORTH);
        contentPanel.add(tablePanel, BorderLayout.CENTER);
        contentPanel.add(actionPanel, BorderLayout.SOUTH);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(248, 250, 252));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        JLabel titleLabel = new JLabel("Gestión de Estudiantes");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(30, 41, 59));
        
        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        
        // Botón de volver
        JButton backButton = new JButton("Volver al Dashboard");
        backButton.setFont(new Font("Arial", Font.BOLD, 12));
        backButton.setBackground(new Color(100, 116, 139));
        backButton.setForeground(Color.WHITE);
        backButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> {
            dispose();
            new DashboardView(authController).setVisible(true);
        });
        
        // Botón de cerrar sesión
        JButton logoutButton = new JButton("Cerrar Sesión");
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
    
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel searchLabel = new JLabel("Buscar Estudiante:");
        searchLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        JTextField searchField = new JTextField();
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        
        JButton searchButton = new JButton("Buscar");
        searchButton.setFont(new Font("Arial", Font.BOLD, 12));
        searchButton.setBackground(new Color(37, 99, 235));
        searchButton.setForeground(Color.WHITE);
        searchButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        searchButton.addActionListener(e -> buscarEstudiantes(searchField.getText()));
        
        JPanel leftPanel = new JPanel(new BorderLayout(10, 0));
        leftPanel.setOpaque(false);
        leftPanel.add(searchLabel, BorderLayout.WEST);
        leftPanel.add(searchField, BorderLayout.CENTER);
        
        panel.add(leftPanel, BorderLayout.CENTER);
        panel.add(searchButton, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));
        
        // Modelo de tabla
        String[] columnNames = {"ID", "Número", "Nombre", "Email", "Estado"};
        modeloTabla = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaEstudiantes = new JTable(modeloTabla);
        tablaEstudiantes.setRowHeight(35);
        tablaEstudiantes.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tablaEstudiantes.getTableHeader().setBackground(new Color(248, 250, 252));
        tablaEstudiantes.setFont(new Font("Arial", Font.PLAIN, 13));
        
        // Personalizar renderer para estado
        tablaEstudiantes.getColumnModel().getColumn(4).setCellRenderer(new EstadoRenderer());
        
        JScrollPane scrollPane = new JScrollPane(tablaEstudiantes);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createActionPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panel.setBackground(new Color(248, 250, 252));
        
        // Botón de agregar
        JButton addButton = new JButton("Agregar Estudiante");
        addButton.setFont(new Font("Arial", Font.BOLD, 14));
        addButton.setBackground(new Color(34, 197, 94));
        addButton.setForeground(Color.WHITE);
        addButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addButton.addActionListener(this::agregarEstudiante);
        
        // Botón de editar
        JButton editButton = new JButton("Editar Estudiante");
        editButton.setFont(new Font("Arial", Font.BOLD, 14));
        editButton.setBackground(new Color(59, 130, 246));
        editButton.setForeground(Color.WHITE);
        editButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        editButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        editButton.addActionListener(this::editarEstudiante);
        
        // Botón de eliminar
        JButton deleteButton = new JButton("Eliminar Estudiante");
        deleteButton.setFont(new Font("Arial", Font.BOLD, 14));
        deleteButton.setBackground(new Color(239, 68, 68));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        deleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteButton.addActionListener(this::eliminarEstudiante);
        
        // Botón de actualizar
        JButton refreshButton = new JButton("Actualizar Lista");
        refreshButton.setFont(new Font("Arial", Font.BOLD, 14));
        refreshButton.setBackground(new Color(168, 85, 247));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshButton.addActionListener(e -> cargarEstudiantes());
        
        panel.add(addButton);
        panel.add(editButton);
        panel.add(deleteButton);
        panel.add(refreshButton);
        
        return panel;
    }
    
    private void cargarEstudiantes() {
        modeloTabla.setRowCount(0);
        for (modelo.Estudiante estudiante : estudianteController.obtenerTodosEstudiantes()) {
            modeloTabla.addRow(new Object[]{
                estudiante.getId(),
                estudiante.getNumeroEstudiante(),
                estudiante.getNombre(),
                estudiante.getEmail(),
                estudiante.getStatus()
            });
        }
    }
    
    private void buscarEstudiantes(String criterio) {
        modeloTabla.setRowCount(0);
        if (criterio == null || criterio.trim().isEmpty()) {
            cargarEstudiantes();
            return;
        }
        
        for (modelo.Estudiante estudiante : estudianteController.buscarEstudiantes(criterio)) {
            modeloTabla.addRow(new Object[]{
                estudiante.getId(),
                estudiante.getNumeroEstudiante(),
                estudiante.getNombre(),
                estudiante.getEmail(),
                estudiante.getStatus()
            });
        }
    }
    
    private void agregarEstudiante(ActionEvent e) {
        JDialog dialog = new JDialog(this, "Agregar Nuevo Estudiante", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel numLabel = new JLabel("Número de Estudiante:");
        JTextField numField = new JTextField();
        
        JLabel nameLabel = new JLabel("Nombre Completo:");
        JTextField nameField = new JTextField();
        
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();
        
        JLabel statusLabel = new JLabel("Estado:");
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"activo", "inactivo"});
        
        formPanel.add(numLabel);
        formPanel.add(numField);
        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(emailLabel);
        formPanel.add(emailField);
        formPanel.add(statusLabel);
        formPanel.add(statusCombo);
        formPanel.add(new JLabel()); // Espacio vacío
        formPanel.add(new JLabel()); // Espacio vacío
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cancelButton = new JButton("Cancelar");
        JButton saveButton = new JButton("Guardar");
        
        cancelButton.addActionListener(ev -> dialog.dispose());
        saveButton.addActionListener(ev -> {
            if (numField.getText().trim().isEmpty() || 
                nameField.getText().trim().isEmpty() || 
                emailField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Todos los campos son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            boolean creado = estudianteController.crearEstudiante(
                numField.getText().trim(),
                nameField.getText().trim(),
                emailField.getText().trim()
            );
            
            if (creado) {
                JOptionPane.showMessageDialog(dialog, "Estudiante creado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarEstudiantes();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "El número de estudiante ya existe", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    private void editarEstudiante(ActionEvent e) {
        int selectedRow = tablaEstudiantes.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un estudiante para editar", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String id = (String) modeloTabla.getValueAt(selectedRow, 0);
        modelo.Estudiante estudiante = estudianteController.obtenerEstudiantePorId(id);
        
        if (estudiante == null) return;
        
        JDialog dialog = new JDialog(this, "Editar Estudiante", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel numLabel = new JLabel("Número de Estudiante:");
        JTextField numField = new JTextField(estudiante.getNumeroEstudiante());
        
        JLabel nameLabel = new JLabel("Nombre Completo:");
        JTextField nameField = new JTextField(estudiante.getNombre());
        
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField(estudiante.getEmail());
        
        JLabel statusLabel = new JLabel("Estado:");
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"activo", "inactivo"});
        statusCombo.setSelectedItem(estudiante.getStatus());
        
        formPanel.add(numLabel);
        formPanel.add(numField);
        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(emailLabel);
        formPanel.add(emailField);
        formPanel.add(statusLabel);
        formPanel.add(statusCombo);
        formPanel.add(new JLabel());
        formPanel.add(new JLabel());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cancelButton = new JButton("Cancelar");
        JButton saveButton = new JButton("Guardar Cambios");
        
        cancelButton.addActionListener(ev -> dialog.dispose());
        saveButton.addActionListener(ev -> {
            if (numField.getText().trim().isEmpty() || 
                nameField.getText().trim().isEmpty() || 
                emailField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Todos los campos son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            boolean actualizado = estudianteController.actualizarEstudiante(
                id,
                numField.getText().trim(),
                nameField.getText().trim(),
                emailField.getText().trim(),
                (String) statusCombo.getSelectedItem()
            );
            
            if (actualizado) {
                JOptionPane.showMessageDialog(dialog, "Estudiante actualizado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarEstudiantes();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "El número de estudiante ya existe", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    private void eliminarEstudiante(ActionEvent e) {
        int selectedRow = tablaEstudiantes.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un estudiante para eliminar", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String id = (String) modeloTabla.getValueAt(selectedRow, 0);
        String nombre = (String) modeloTabla.getValueAt(selectedRow, 2);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de eliminar al estudiante: " + nombre + "?",
            "Confirmar Eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            boolean eliminado = estudianteController.eliminarEstudiante(id);
            if (eliminado) {
                JOptionPane.showMessageDialog(this, "Estudiante eliminado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarEstudiantes();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar el estudiante", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private class EstadoRenderer extends javax.swing.table.DefaultTableCellRenderer {
        @Override
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            String estado = value.toString();
            setHorizontalAlignment(SwingConstants.CENTER);
            setFont(new Font("Arial", Font.BOLD, 11));
            
            if ("activo".equals(estado)) {
                setBackground(new Color(220, 252, 231));
                setForeground(new Color(21, 128, 61));
                setBorder(BorderFactory.createLineBorder(new Color(187, 247, 208), 1));
            } else {
                setBackground(new Color(254, 226, 226));
                setForeground(new Color(185, 28, 28));
                setBorder(BorderFactory.createLineBorder(new Color(254, 202, 202), 1));
            }
            
            setOpaque(true);
            return this;
        }
    }
}