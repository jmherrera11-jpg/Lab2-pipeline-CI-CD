package vista;

import controlador.AuthController;
import vista.LoginView;
import javax.swing.*;

public class App {
    public static void main(String[] args) {
        // Usar SwingUtilities para thread safety
        SwingUtilities.invokeLater(() -> {
            try {
                // Establecer Look and Feel moderno
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                
                // Crear controlador de autenticación
                AuthController authController = new AuthController();
                
                // Mostrar ventana de login
                LoginView loginView = new LoginView(authController);
                loginView.setVisible(true);
                
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, 
                    "Error al iniciar la aplicación: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
