/*package controller;

import javax.swing.*;
import model.UMLCanvas;
import model.UMLClasse;
public class ClassCreationDialog extends JDialog {

    private final JTextField classNameField;
    private final JButton createButton;
    private final UMLCanvas canvas;

    public ClassCreationDialog(JFrame owner, UMLCanvas canvas) {
        super(owner, "Nouvelle Classe", true);
        this.canvas = canvas;
        classNameField = new JTextField(20);
        createButton = new JButton("Créer");

        createButton.addActionListener(e -> createClass());

        setupLayout();
        pack();
        setLocationRelativeTo(owner);
    }

    private void setupLayout() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Nom de la classe :"));
        panel.add(classNameField);
        panel.add(createButton);
        add(panel);
    }

    private void createClass() {
        String className = classNameField.getText().trim();
        if (!className.isEmpty()) {
            int x = 50;
            int y = 50;
            UMLClasse newClass = new UMLClasse(className, x, y);
            canvas.addUMLClass(newClass);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Le nom de la classe ne peut pas être vide.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

}
*/