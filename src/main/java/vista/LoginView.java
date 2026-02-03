package vista;

import controlador.AuthController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginView extends JFrame {
    private AuthController authController;
    
    public LoginView(AuthController authController) {
        this.authController = authController;
        initUI();
    }
    
    private void initUI() {
        setTitle("Portal Académico - Inicio de Sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(null);
        
        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(248, 250, 252));
        
        // Header
        JPanel headerPanel = createHeaderPanel();
        
        // Body con botones de roles
        JPanel bodyPanel = createBodyPanel();
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(bodyPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(37, 99, 235));
        headerPanel.setPreferredSize(new Dimension(500, 180));
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        
        // Icono
        JPanel iconPanel = new JPanel();
        iconPanel.setOpaque(false);
        iconPanel.setMaximumSize(new Dimension(80, 80));
        iconPanel.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 50), 2));
        iconPanel.setLayout(new GridBagLayout());
        
        JLabel iconLabel = new JLabel("A");
        iconLabel.setFont(new Font("Arial", Font.BOLD, 32));
        iconLabel.setForeground(Color.WHITE);
        iconPanel.add(iconLabel);
        
        // Título
        JLabel titleLabel = new JLabel("Portal Académico");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Subtítulo
        JLabel subtitleLabel = new JLabel("Sistema de Control de Asistencia");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(191, 219, 254));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        headerPanel.add(Box.createVerticalStrut(30));
        headerPanel.add(iconPanel);
        headerPanel.add(Box.createVerticalStrut(10));
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(subtitleLabel);
        
        return headerPanel;
    }
    
    private JPanel createBodyPanel() {
        JPanel bodyPanel = new JPanel();
        bodyPanel.setBackground(Color.WHITE);
        bodyPanel.setLayout(new BoxLayout(bodyPanel, BoxLayout.Y_AXIS));
        bodyPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        JLabel selectLabel = new JLabel("Seleccione su rol para iniciar sesión");
        selectLabel.setFont(new Font("Arial", Font.BOLD, 16));
        selectLabel.setForeground(new Color(71, 85, 105));
        selectLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        bodyPanel.add(selectLabel);
        bodyPanel.add(Box.createVerticalStrut(30));
        
        // Botones de roles
        JButton profesorBtn = createRoleButton("Acceso Profesor", 
            "Registrar asistencia y ver cursos", new Color(59, 130, 246), "profesor");
        
        JButton estudianteBtn = createRoleButton("Acceso Estudiante", 
            "Consultar estado de asistencia", new Color(34, 197, 94), "estudiante");
        
        JButton coordinadorBtn = createRoleButton("Acceso Coordinador", 
            "Gestionar reportes y sistema", new Color(168, 85, 247), "coordinador");
        
        bodyPanel.add(profesorBtn);
        bodyPanel.add(Box.createVerticalStrut(15));
        bodyPanel.add(estudianteBtn);
        bodyPanel.add(Box.createVerticalStrut(15));
        bodyPanel.add(coordinadorBtn);
        bodyPanel.add(Box.createVerticalStrut(40));
        
        // Footer
        JLabel footerLabel = new JLabel("Sistema Protegido • Solo Personal Autorizado");
        footerLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        footerLabel.setForeground(new Color(148, 163, 184));
        footerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        bodyPanel.add(footerLabel);
        
        return bodyPanel;
    }
    
    private JButton createRoleButton(String title, String description, Color color, String role) {
        JButton button = new JButton();
        button.setLayout(new BorderLayout());
        button.setBackground(Color.WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(400, 80));
        
        // Panel de contenido
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        
        // Panel de icono
        JPanel iconPanel = new JPanel();
        iconPanel.setPreferredSize(new Dimension(50, 50));
        iconPanel.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 30));
        iconPanel.setBorder(BorderFactory.createLineBorder(
            new Color(color.getRed(), color.getGreen(), color.getBlue(), 50), 1));
        iconPanel.setLayout(new GridBagLayout());
        
        JLabel iconLabel = new JLabel(role.substring(0, 1).toUpperCase());
        iconLabel.setFont(new Font("Arial", Font.BOLD, 16));
        iconLabel.setForeground(color);
        iconPanel.add(iconLabel);
        
        // Panel de texto
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(new Color(30, 41, 59));
        
        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        descLabel.setForeground(new Color(100, 116, 139));
        
        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalStrut(2));
        textPanel.add(descLabel);
        
        // Flecha
        JLabel arrowLabel = new JLabel(">");
        arrowLabel.setFont(new Font("Arial", Font.BOLD, 16));
        arrowLabel.setForeground(new Color(203, 213, 225));
        
        contentPanel.add(iconPanel, BorderLayout.WEST);
        contentPanel.add(textPanel, BorderLayout.CENTER);
        contentPanel.add(arrowLabel, BorderLayout.EAST);
        
        button.add(contentPanel);
        button.addActionListener(e -> handleLogin(role));
        
        return button;
    }
    
    private void handleLogin(String role) {
        // CORREGIR: Traducir los roles del español al inglés
        String rolAuth;
        switch(role) {
            case "profesor":
                rolAuth = "teacher";
                break;
            case "estudiante":
                rolAuth = "student";  // ← Aquí está el problema
                break;
            case "coordinador":
                rolAuth = "coordinator";
                break;
            default:
                rolAuth = role;
        }
        
        if (authController.login(rolAuth)) {
            dispose();
            new DashboardView(authController).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, 
                "Error de autenticación. Rol '" + role + "' no encontrado.", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}