package vista;

import controlador.AuthController;
import controlador.CursoController;
import controlador.ProfesorController;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class GestionCursosView extends JFrame {
    private AuthController authController;
    private CursoController cursoController;
    private ProfesorController profesorController;
    private JTable tablaCursos;
    private DefaultTableModel modeloTabla;
    
    public GestionCursosView(AuthController authController) {
        this.authController = authController;
        this.cursoController = new CursoController();
        this.profesorController = new ProfesorController();
        initUI();
        cargarCursos();
    }
    
    private void initUI() {
        setTitle("Gestión de Cursos - Coordinador");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1100, 700);
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
        
        JLabel titleLabel = new JLabel("Gestión de Cursos");
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
        
        JLabel searchLabel = new JLabel("Buscar Curso:");
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
        searchButton.addActionListener(e -> buscarCursos(searchField.getText()));
        
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
        String[] columnNames = {"Código", "Nombre", "Horario", "Aula", "Profesor"};
        modeloTabla = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaCursos = new JTable(modeloTabla);
        tablaCursos.setRowHeight(35);
        tablaCursos.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tablaCursos.getTableHeader().setBackground(new Color(248, 250, 252));
        tablaCursos.setFont(new Font("Arial", Font.PLAIN, 13));
        
        JScrollPane scrollPane = new JScrollPane(tablaCursos);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createActionPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panel.setBackground(new Color(248, 250, 252));
        
        // Botón de agregar
        JButton addButton = new JButton("Agregar Curso");
        addButton.setFont(new Font("Arial", Font.BOLD, 14));
        addButton.setBackground(new Color(34, 197, 94));
        addButton.setForeground(Color.WHITE);
        addButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addButton.addActionListener(this::agregarCurso);
        
        // Botón de editar
        JButton editButton = new JButton("Editar Curso");
        editButton.setFont(new Font("Arial", Font.BOLD, 14));
        editButton.setBackground(new Color(59, 130, 246));
        editButton.setForeground(Color.WHITE);
        editButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        editButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        editButton.addActionListener(this::editarCurso);
        
        // Botón de eliminar
        JButton deleteButton = new JButton("Eliminar Curso");
        deleteButton.setFont(new Font("Arial", Font.BOLD, 14));
        deleteButton.setBackground(new Color(239, 68, 68));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        deleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteButton.addActionListener(this::eliminarCurso);
        
        // Botón de actualizar
        JButton refreshButton = new JButton("Actualizar Lista");
        refreshButton.setFont(new Font("Arial", Font.BOLD, 14));
        refreshButton.setBackground(new Color(168, 85, 247));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshButton.addActionListener(e -> cargarCursos());
        
        panel.add(addButton);
        panel.add(editButton);
        panel.add(deleteButton);
        panel.add(refreshButton);
        
        return panel;
    }
    
    private void cargarCursos() {
        modeloTabla.setRowCount(0);
        for (modelo.Curso curso : cursoController.obtenerTodosCursos()) {
            String profesorNombre = "Sin asignar";
            if (curso.getProfesorId() != null) {
                modelo.Usuario profesor = profesorController.obtenerProfesorPorId(curso.getProfesorId());
                if (profesor != null) {
                    profesorNombre = profesor.getNombre();
                }
            }
            
            modeloTabla.addRow(new Object[]{
                curso.getCodigo(),
                curso.getNombre(),
                curso.getHorario(),
                curso.getAula(),
                profesorNombre
            });
        }
    }
    
    private void buscarCursos(String criterio) {
        modeloTabla.setRowCount(0);
        if (criterio == null || criterio.trim().isEmpty()) {
            cargarCursos();
            return;
        }
        
        for (modelo.Curso curso : cursoController.buscarCursos(criterio)) {
            String profesorNombre = "Sin asignar";
            if (curso.getProfesorId() != null) {
                modelo.Usuario profesor = profesorController.obtenerProfesorPorId(curso.getProfesorId());
                if (profesor != null) {
                    profesorNombre = profesor.getNombre();
                }
            }
            
            modeloTabla.addRow(new Object[]{
                curso.getCodigo(),
                curso.getNombre(),
                curso.getHorario(),
                curso.getAula(),
                profesorNombre
            });
        }
    }
    
    private void agregarCurso(ActionEvent e) {
        JDialog dialog = new JDialog(this, "Agregar Nuevo Curso", true);
        dialog.setSize(400, 450);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel codeLabel = new JLabel("Código del Curso:");
        JTextField codeField = new JTextField();
        
        JLabel nameLabel = new JLabel("Nombre del Curso:");
        JTextField nameField = new JTextField();
        
        JLabel scheduleLabel = new JLabel("Horario:");
        JTextField scheduleField = new JTextField();
        
        JLabel roomLabel = new JLabel("Aula:");
        JTextField roomField = new JTextField();
        
        JLabel profesorLabel = new JLabel("Profesor:");
        JComboBox<String> profesorCombo = new JComboBox<>();
        profesorCombo.addItem("Sin asignar");
        for (modelo.Usuario profesor : profesorController.obtenerTodosProfesores()) {
            profesorCombo.addItem(profesor.getNombre() + " (" + profesor.getEmail() + ")");
        }
        
        formPanel.add(codeLabel);
        formPanel.add(codeField);
        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(scheduleLabel);
        formPanel.add(scheduleField);
        formPanel.add(roomLabel);
        formPanel.add(roomField);
        formPanel.add(profesorLabel);
        formPanel.add(profesorCombo);
        formPanel.add(new JLabel());
        formPanel.add(new JLabel());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cancelButton = new JButton("Cancelar");
        JButton saveButton = new JButton("Guardar");
        
        cancelButton.addActionListener(ev -> dialog.dispose());
        saveButton.addActionListener(ev -> {
            if (codeField.getText().trim().isEmpty() || 
                nameField.getText().trim().isEmpty() || 
                scheduleField.getText().trim().isEmpty() || 
                roomField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Todos los campos son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String profesorId = null;
            if (profesorCombo.getSelectedIndex() > 0) {
                // Obtener ID del profesor seleccionado
                String seleccion = (String) profesorCombo.getSelectedItem();
                for (modelo.Usuario profesor : profesorController.obtenerTodosProfesores()) {
                    if (seleccion.contains(profesor.getEmail())) {
                        profesorId = profesor.getId();
                        break;
                    }
                }
            }
            
            boolean creado = cursoController.crearCurso(
                codeField.getText().trim(),
                nameField.getText().trim(),
                scheduleField.getText().trim(),
                roomField.getText().trim(),
                profesorId
            );
            
            if (creado) {
                JOptionPane.showMessageDialog(dialog, "Curso creado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarCursos();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "El código del curso ya existe", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    private void editarCurso(ActionEvent e) {
        int selectedRow = tablaCursos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un curso para editar", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String codigo = (String) modeloTabla.getValueAt(selectedRow, 0);
        modelo.Curso curso = cursoController.obtenerCursoPorCodigo(codigo);
        
        if (curso == null) return;
        
        JDialog dialog = new JDialog(this, "Editar Curso", true);
        dialog.setSize(400, 450);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel codeLabel = new JLabel("Código del Curso:");
        JTextField codeField = new JTextField(curso.getCodigo());
        
        JLabel nameLabel = new JLabel("Nombre del Curso:");
        JTextField nameField = new JTextField(curso.getNombre());
        
        JLabel scheduleLabel = new JLabel("Horario:");
        JTextField scheduleField = new JTextField(curso.getHorario());
        
        JLabel roomLabel = new JLabel("Aula:");
        JTextField roomField = new JTextField(curso.getAula());
        
        JLabel profesorLabel = new JLabel("Profesor:");
        JComboBox<String> profesorCombo = new JComboBox<>();
        profesorCombo.addItem("Sin asignar");
        
        String profesorSeleccionado = "Sin asignar";
        for (modelo.Usuario profesor : profesorController.obtenerTodosProfesores()) {
            String item = profesor.getNombre() + " (" + profesor.getEmail() + ")";
            profesorCombo.addItem(item);
            if (profesor.getId().equals(curso.getProfesorId())) {
                profesorSeleccionado = item;
            }
        }
        profesorCombo.setSelectedItem(profesorSeleccionado);
        
        formPanel.add(codeLabel);
        formPanel.add(codeField);
        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(scheduleLabel);
        formPanel.add(scheduleField);
        formPanel.add(roomLabel);
        formPanel.add(roomField);
        formPanel.add(profesorLabel);
        formPanel.add(profesorCombo);
        formPanel.add(new JLabel());
        formPanel.add(new JLabel());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cancelButton = new JButton("Cancelar");
        JButton saveButton = new JButton("Guardar Cambios");
        
        cancelButton.addActionListener(ev -> dialog.dispose());
        saveButton.addActionListener(ev -> {
            if (codeField.getText().trim().isEmpty() || 
                nameField.getText().trim().isEmpty() || 
                scheduleField.getText().trim().isEmpty() || 
                roomField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Todos los campos son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String profesorId = null;
            if (profesorCombo.getSelectedIndex() > 0) {
                String seleccion = (String) profesorCombo.getSelectedItem();
                for (modelo.Usuario profesor : profesorController.obtenerTodosProfesores()) {
                    if (seleccion.contains(profesor.getEmail())) {
                        profesorId = profesor.getId();
                        break;
                    }
                }
            }
            
            boolean actualizado = cursoController.actualizarCurso(
                curso.getId(),
                codeField.getText().trim(),
                nameField.getText().trim(),
                scheduleField.getText().trim(),
                roomField.getText().trim(),
                profesorId
            );
            
            if (actualizado) {
                JOptionPane.showMessageDialog(dialog, "Curso actualizado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarCursos();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "El código del curso ya existe", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    private void eliminarCurso(ActionEvent e) {
        int selectedRow = tablaCursos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un curso para eliminar", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String codigo = (String) modeloTabla.getValueAt(selectedRow, 0);
        String nombre = (String) modeloTabla.getValueAt(selectedRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de eliminar el curso: " + nombre + " (" + codigo + ")?",
            "Confirmar Eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            modelo.Curso curso = cursoController.obtenerCursoPorCodigo(codigo);
            if (curso != null) {
                boolean eliminado = cursoController.eliminarCurso(curso.getId());
                if (eliminado) {
                    JOptionPane.showMessageDialog(this, "Curso eliminado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    cargarCursos();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al eliminar el curso", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}