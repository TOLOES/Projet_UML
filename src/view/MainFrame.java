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

        fileMenu.add(createClassItem);
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
}

