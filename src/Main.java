import model.UMLCanvas;
import view.MainFrame;

import javax.swing.*;
import java.io.Serializable;

public class Main implements Serializable{

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UMLCanvas canvas = new UMLCanvas();
            MainFrame mainFrame = new MainFrame("UML Modeller", canvas);
            mainFrame.add(canvas);
            mainFrame.setVisible(true);
        });
    }
}


