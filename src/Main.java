import model.UMLCanvas;
import view.MainFrame;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UMLCanvas canvas = new UMLCanvas();
            MainFrame mainFrame = new MainFrame("UML Modeller", canvas);
            mainFrame.add(canvas); // Ajouter le canvas à la fenêtre principale
            mainFrame.setVisible(true);
        });
    }
}

