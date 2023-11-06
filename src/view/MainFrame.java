package view;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class MainFrame extends JFrame {

    public MainFrame(String title) {
        super(title); // Appel du constructeur de la superclasse JFrame avec le titre de la fenêtre

        // Configuration de la fermeture de l'application
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Définition de la taille de la fenêtre
        setSize(800, 600);

        // Positionnement de la fenêtre au centre de l'écran
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        // S'assurer que la tâche de création de l'interface utilisateur est effectuée dans le thread de distribution des événements (EDT)
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Création de la fenêtre principale
                MainFrame frame = new MainFrame("UML Modeller");

                // Rendre la fenêtre visible
                frame.setVisible(true);
            }
        });
    }
}
