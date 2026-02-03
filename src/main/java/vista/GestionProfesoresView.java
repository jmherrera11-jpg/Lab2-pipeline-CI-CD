package vista;

import controlador.AuthController;
import controlador.ProfesorController;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class GestionProfesoresView extends JFrame {
    private AuthController authController;
    private ProfesorController profesorController;
    private JTable tablaProfesores;
    private DefaultTableModel modeloTabla;
    
    public GestionProfesoresView(AuthController authController) {
        this.authController = authController;
        this.profesorController = new ProfesorController();
        initUI();
        cargarProfesores();
    }
    
    private void initUI() {
        setTitle("Gestión de Profesores - Coordinador");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
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
        
        JLabel titleLabel = new JLabel("Gestión de Profesores");
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
        
        JLabel searchLabel = new JLabel("Buscar Profesor:");
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
        searchButton.addActionListener(e -> buscarProfesores(searchField.getText()));
        
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
        String[] columnNames = {"ID", "Nombre", "Email", "Rol"};
        modeloTabla = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaProfesores = new JTable(modeloTabla);
        tablaProfesores.setRowHeight(35);
        tablaProfesores.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tablaProfesores.getTableHeader().setBackground(new Color(248, 250, 252));
        tablaProfesores.setFont(new Font("Arial", Font.PLAIN, 13));
        
        JScrollPane scrollPane = new JScrollPane(tablaProfesores);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createActionPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panel.setBackground(new Color(248, 250, 252));
        
        // Botón de agregar
        JButton addButton = new JButton("Agregar Profesor");
        addButton.setFont(new Font("Arial", Font.BOLD, 14));
        addButton.setBackground(new Color(34, 197, 94));
        addButton.setForeground(Color.WHITE);
        addButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addButton.addActionListener(this::agregarProfesor);
        
        // Botón de editar
        JButton editButton = new JButton("Editar Profesor");
        editButton.setFont(new Font("Arial", Font.BOLD, 14));
        editButton.setBackground(new Color(59, 130, 246));
        editButton.setForeground(Color.WHITE);
        editButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        editButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        editButton.addActionListener(this::editarProfesor);
        
        // Botón de eliminar
        JButton deleteButton = new JButton("Eliminar Profesor");
        deleteButton.setFont(new Font("Arial", Font.BOLD, 14));
        deleteButton.setBackground(new Color(239, 68, 68));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        deleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteButton.addActionListener(this::eliminarProfesor);
        
        // Botón de actualizar
        JButton refreshButton = new JButton("Actualizar Lista");
        refreshButton.setFont(new Font("Arial", Font.BOLD, 14));
        refreshButton.setBackground(new Color(168, 85, 247));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshButton.addActionListener(e -> cargarProfesores());
        
        panel.add(addButton);
        panel.add(editButton);
        panel.add(deleteButton);
        panel.add(refreshButton);
        
        return panel;
    }
    
    private void cargarProfesores() {
        modeloTabla.setRowCount(0);
        for (modelo.Usuario profesor : profesorController.obtenerTodosProfesores()) {
            modeloTabla.addRow(new Object[]{
                profesor.getId(),
                profesor.getNombre(),
                profesor.getEmail(),
                profesor.getRol()
            });
        }
    }
    
    private void buscarProfesores(String criterio) {
        modeloTabla.setRowCount(0);
        if (criterio == null || criterio.trim().isEmpty()) {
            cargarProfesores();
            return;
        }
        
        for (modelo.Usuario profesor : profesorController.buscarProfesores(criterio)) {
            modeloTabla.addRow(new Object[]{
                profesor.getId(),
                profesor.getNombre(),
                profesor.getEmail(),
                profesor.getRol()
            });
        }
    }
    
    private void agregarProfesor(ActionEvent e) {
        JDialog dialog = new JDialog(this, "Agregar Nuevo Profesor", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel nameLabel = new JLabel("Nombre Completo:");
        JTextField nameField = new JTextField();
        
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();
        
        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(emailLabel);
        formPanel.add(emailField);
        formPanel.add(new JLabel()); // Espacio vacío
        formPanel.add(new JLabel()); // Espacio vacío
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cancelButton = new JButton("Cancelar");
        JButton saveButton = new JButton("Guardar");
        
        cancelButton.addActionListener(ev -> dialog.dispose());
        saveButton.addActionListener(ev -> {
            if (nameField.getText().trim().isEmpty() || 
                emailField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Todos los campos son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            boolean creado = profesorController.crearProfesor(
                nameField.getText().trim(),
                emailField.getText().trim()
            );
            
            if (creado) {
                JOptionPane.showMessageDialog(dialog, "Profesor creado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarProfesores();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "El email del profesor ya existe", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    private void editarProfesor(ActionEvent e) {
        int selectedRow = tablaProfesores.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un profesor para editar", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String id = (String) modeloTabla.getValueAt(selectedRow, 0);
        modelo.Usuario profesor = profesorController.obtenerProfesorPorId(id);
        
        if (profesor == null) return;
        
        JDialog dialog = new JDialog(this, "Editar Profesor", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel nameLabel = new JLabel("Nombre Completo:");
        JTextField nameField = new JTextField(profesor.getNombre());
        
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField(profesor.getEmail());
        
        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(emailLabel);
        formPanel.add(emailField);
        formPanel.add(new JLabel());
        formPanel.add(new JLabel());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cancelButton = new JButton("Cancelar");
        JButton saveButton = new JButton("Guardar Cambios");
        
        cancelButton.addActionListener(ev -> dialog.dispose());
        saveButton.addActionListener(ev -> {
            if (nameField.getText().trim().isEmpty() || 
                emailField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Todos los campos son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            boolean actualizado = profesorController.actualizarProfesor(
                id,
                nameField.getText().trim(),
                emailField.getText().trim()
            );
            
            if (actualizado) {
                JOptionPane.showMessageDialog(dialog, "Profesor actualizado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarProfesores();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "El email del profesor ya existe", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    private void eliminarProfesor(ActionEvent e) {
        int selectedRow = tablaProfesores.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un profesor para eliminar", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String id = (String) modeloTabla.getValueAt(selectedRow, 0);
        String nombre = (String) modeloTabla.getValueAt(selectedRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de eliminar al profesor: " + nombre + "?",
            "Confirmar Eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            boolean eliminado = profesorController.eliminarProfesor(id);
            if (eliminado) {
                JOptionPane.showMessageDialog(this, "Profesor eliminado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarProfesores();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar el profesor", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}