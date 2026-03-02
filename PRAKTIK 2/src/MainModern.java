import javax.swing.*;

public class MainModern {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            ModernCarShowroomGUI app = new ModernCarShowroomGUI();
            app.setVisible(true);
        });
    }
}