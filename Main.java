import java.util.List;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Cargar personajes desde la API
        List<Character> characters = ApiClient.fetchCharacters();
        if (characters.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No se pudieron cargar los personajes.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        // Mostrar ventana principal
        javax.swing.SwingUtilities.invokeLater(() -> {
            MainWindow mw = new MainWindow(characters);
            mw.setVisible(true);
        });
    }
}
