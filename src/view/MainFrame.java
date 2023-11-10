package view;

import javax.swing.*;

import model.UMLCanvas;
import model.UMLClasse;
import controller.ClassEditorDialog;

public class MainFrame extends JFrame {

    public MainFrame(String title, UMLCanvas canvas) {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        initMenuBar(canvas);
    }

    private void initMenuBar(UMLCanvas canvas) {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Fichier");

        JMenuItem createClassItem = new JMenuItem("Créer une classe");
        createClassItem.addActionListener(e -> createClass(canvas));

        JMenuItem deleteClassItem = new JMenuItem("Supprimer une classe");
        deleteClassItem.addActionListener(e -> deleteClass(canvas));

        JMenuItem createRelationItem = new JMenuItem("Ajouter une relation");
        createRelationItem.addActionListener(e -> canvas.startCreatingRelation());

        fileMenu.add(createClassItem);
        fileMenu.add(deleteClassItem);
        fileMenu.add(createRelationItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }

    private void createClass(UMLCanvas canvas) {
        String className = JOptionPane.showInputDialog(this, "Nom de la classe:");
        if (className != null && !className.trim().isEmpty()) {
            int x = 50;
            int y = 50;
            UMLClasse umlClass = new UMLClasse(className.trim(), x, y);
            canvas.addUMLClass(umlClass);
            ClassEditorDialog editorDialog = new ClassEditorDialog(this, umlClass, canvas);
            editorDialog.setVisible(true);
        }
    }

    private void deleteClass(UMLCanvas canvas) {
        String className = JOptionPane.showInputDialog(this, "Nom de la classe à supprimer:");
        if (className != null && !className.trim().isEmpty()) {
            UMLClasse umlClass = canvas.findUMLClasseByName(className.trim());
            if (umlClass != null) {
                canvas.removeUMLClass(umlClass);
            } else {
                JOptionPane.showMessageDialog(this, "Aucune classe trouvée avec le nom : " + className, "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

