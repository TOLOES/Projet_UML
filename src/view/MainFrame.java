package view;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.Serializable;

import controller.UMLController;
import model.UMLCanvas;
import model.UMLClasse;
import controller.ClassEditorDialog;

public class MainFrame extends JFrame implements Serializable {
    private UMLController umlController;

    public MainFrame(String title, UMLCanvas canvas) {
        super(title);
        umlController = new UMLController();
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

        JMenuItem saveItem = new JMenuItem("Enregistrer");
        saveItem.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Enregistrer le schéma");

            fileChooser.setSelectedFile(new File("schema.uml"));

            int userSelection = fileChooser.showSaveDialog(this);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                canvas.saveSchema(fileToSave);
            }
        });

        JMenuItem loadItem = new JMenuItem("Charger");
        loadItem.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Ouvrir le schéma");

            int userSelection = fileChooser.showOpenDialog(this);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToLoad = fileChooser.getSelectedFile();
                canvas.loadSchema(fileToLoad.getAbsolutePath());
            }
        });

        JMenu editMenu = new JMenu("Édition");

        JMenuItem deleteRelationItem = new JMenuItem("Supprimer une relation");
        deleteRelationItem.addActionListener(e -> canvas.startDeletingRelation());

        JMenuItem createRelationItem = new JMenuItem("Ajouter une relation");
        createRelationItem.addActionListener(e -> canvas.startCreatingRelation());

        JMenuItem generateCodeButton = new JMenuItem("Générer le code");
        generateCodeButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Enregistrer le code Java");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setFileFilter(new FileNameExtensionFilter("Java Files", "java"));

            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                if (!fileToSave.getAbsolutePath().endsWith(".java")) {
                    fileToSave = new File(fileToSave.getAbsolutePath() + ".java");
                }
                umlController.generateJavaCode(canvas, fileToSave);
            }
        });

        fileMenu.add(saveItem);
        fileMenu.add(loadItem);
        fileMenu.add(createClassItem);
        fileMenu.add(editClassItem);
        fileMenu.add(deleteClassItem);
        fileMenu.add(generateCodeButton);
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

