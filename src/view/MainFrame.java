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

        JMenuItem editClassItem = new JMenuItem("Modifier une classe");
        editClassItem.addActionListener(e -> editClass(canvas));

        JMenuItem deleteClassItem = new JMenuItem("Supprimer une classe");
        deleteClassItem.addActionListener(e -> deleteClass(canvas));

        JMenu editMenu = new JMenu("Édition");

        JMenuItem deleteRelationItem = new JMenuItem("Supprimer une relation");
        deleteRelationItem.addActionListener(e -> canvas.startDeletingRelation());

        JMenuItem createRelationItem = new JMenuItem("Ajouter une relation");
        createRelationItem.addActionListener(e -> canvas.startCreatingRelation());

        fileMenu.add(createClassItem);
        fileMenu.add(editClassItem);
        fileMenu.add(deleteClassItem);
        editMenu.add(createRelationItem);
        editMenu.add(deleteRelationItem);
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
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

    private void editClass(UMLCanvas canvas) {
        String className = JOptionPane.showInputDialog(this, "Nom de la classe à modifier:");
        if (className != null && !className.trim().isEmpty()) {
            UMLClasse umlClass = canvas.findUMLClasseByName(className.trim());
            if (umlClass != null) {
                String newName = JOptionPane.showInputDialog(this, "Entrez le nouveau nom pour la classe " + className + ":");
                if (newName != null && !newName.trim().isEmpty()) {
                    umlClass.setName(newName.trim());
                    canvas.repaint();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Aucune classe trouvée avec le nom : " + className, "Erreur", JOptionPane.ERROR_MESSAGE);
            }
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

