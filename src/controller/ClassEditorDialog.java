package controller;

import javax.swing.*;
import java.awt.*;
import model.UMLClasse;

public class ClassEditorDialog extends JDialog {

    private final UMLClasse umlClass;
    private final DefaultListModel<String> attributeListModel;
    private final DefaultListModel<String> methodListModel;
    private final JList<String> attributeList;
    private final JList<String> methodList;

    public ClassEditorDialog(JFrame owner, UMLClasse umlClass) {
        super(owner, "Edit UML Class", true);
        this.umlClass = umlClass;

        attributeListModel = new DefaultListModel<>();
        methodListModel = new DefaultListModel<>();
        umlClass.getAttributes().forEach(attributeListModel::addElement);
        umlClass.getMethods().forEach(methodListModel::addElement);

        attributeList = new JList<>(attributeListModel);
        methodList = new JList<>(methodListModel);

        setupLayout();
        setupListeners();
        pack();
        setLocationRelativeTo(owner);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Attributes panel
        JPanel attributesPanel = new JPanel(new BorderLayout());
        attributesPanel.add(new JLabel("Attributes:"), BorderLayout.NORTH);
        attributesPanel.add(new JScrollPane(attributeList), BorderLayout.CENTER);
        JButton addAttributeButton = new JButton("Add Attribute");
        attributesPanel.add(addAttributeButton, BorderLayout.SOUTH);

        // Methods panel
        JPanel methodsPanel = new JPanel(new BorderLayout());
        methodsPanel.add(new JLabel("Methods:"), BorderLayout.NORTH);
        methodsPanel.add(new JScrollPane(methodList), BorderLayout.CENTER);
        JButton addMethodButton = new JButton("Add Method");
        methodsPanel.add(addMethodButton, BorderLayout.SOUTH);

        // Add panels to dialog
        add(attributesPanel, BorderLayout.WEST);
        add(methodsPanel, BorderLayout.EAST);


    }

    private void setupListeners() {
        // Boutons pour les attributs
        JButton addAttributeButton = new JButton("Ajouter");
        JButton editAttributeButton = new JButton("Modifier");
        JButton removeAttributeButton = new JButton("Supprimer");

        // Boutons pour les méthodes
        JButton addMethodButton = new JButton("Ajouter");
        JButton editMethodButton = new JButton("Modifier");
        JButton removeMethodButton = new JButton("Supprimer");

        // Panel pour les boutons d'attributs
        JPanel attributeButtonPanel = new JPanel();
        attributeButtonPanel.add(addAttributeButton);
        attributeButtonPanel.add(editAttributeButton);
        attributeButtonPanel.add(removeAttributeButton);

        // Panel pour les boutons de méthodes
        JPanel methodButtonPanel = new JPanel();
        methodButtonPanel.add(addMethodButton);
        methodButtonPanel.add(editMethodButton);
        methodButtonPanel.add(removeMethodButton);

        // Ajouter les action listeners pour les attributs
        addAttributeButton.addActionListener(e -> addAttribute());
        editAttributeButton.addActionListener(e -> editAttribute());
        removeAttributeButton.addActionListener(e -> removeAttribute());

        // Ajouter les action listeners pour les méthodes
        addMethodButton.addActionListener(e -> addMethod());
        editMethodButton.addActionListener(e -> editMethod());
        removeMethodButton.addActionListener(e -> removeMethod());

        // Panel principal pour les boutons
        JPanel mainButtonPanel = new JPanel(new BorderLayout());
        mainButtonPanel.add(attributeButtonPanel, BorderLayout.NORTH);
        mainButtonPanel.add(methodButtonPanel, BorderLayout.SOUTH);

        // Ajouter le panel principal à la fenêtre
        getContentPane().add(mainButtonPanel, BorderLayout.SOUTH);
    }


    private void addAttribute() {
        JTextField nameField = new JTextField(10);
        JTextField typeField = new JTextField(10);

        JPanel myPanel = new JPanel();
        myPanel.add(new JLabel("Nom:"));
        myPanel.add(nameField);
        myPanel.add(Box.createHorizontalStrut(15)); // un espace
        myPanel.add(new JLabel("Type:"));
        myPanel.add(typeField);

        int result = JOptionPane.showConfirmDialog(this, myPanel,
                "Entrez le nom et le type de l'attribut", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String attributeName = nameField.getText().trim();
            String attributeType = typeField.getText().trim();
            if (!attributeName.isEmpty() && !attributeType.isEmpty()) {
                String fullAttribute = attributeType + " " + attributeName;
                umlClass.addAttribute(fullAttribute);
                attributeListModel.addElement(fullAttribute);
            }
        }
    }


    private void editAttribute() {
        int selectedIndex = attributeList.getSelectedIndex();
        if (selectedIndex != -1) {
            String currentAttribute = attributeListModel.getElementAt(selectedIndex);
            String newAttribute = JOptionPane.showInputDialog(this, "Modifier l'attribut:", currentAttribute);
            if (newAttribute != null && !newAttribute.trim().isEmpty()) {
                umlClass.editAttribute(selectedIndex, newAttribute.trim());
                attributeListModel.set(selectedIndex, newAttribute.trim());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un attribut à modifier.");
        }
    }

    private void removeAttribute() {
        int selectedIndex = attributeList.getSelectedIndex();
        if (selectedIndex != -1) {
            umlClass.removeAttribute(String.valueOf(selectedIndex));
            attributeListModel.remove(selectedIndex);
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un attribut à supprimer.");
        }
    }

    private void addMethod() {
        JTextField nameField = new JTextField(10);
        JTextField returnTypeField = new JTextField(10);

        JPanel myPanel = new JPanel();
        myPanel.add(new JLabel("Nom:"));
        myPanel.add(nameField);
        myPanel.add(Box.createHorizontalStrut(15)); // un espace
        myPanel.add(new JLabel("Type de retour:"));
        myPanel.add(returnTypeField);

        int result = JOptionPane.showConfirmDialog(this, myPanel,
                "Entrez le nom et le type de retour de la méthode", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String methodName = nameField.getText().trim();
            String returnType = returnTypeField.getText().trim();
            if (!methodName.isEmpty() && !returnType.isEmpty()) {
                String fullMethod = returnType + " " + methodName + "()";
                umlClass.addMethod(fullMethod);
                methodListModel.addElement(fullMethod);
            }
        }
    }


    private void editMethod() {
        int selectedIndex = methodList.getSelectedIndex();
        if (selectedIndex != -1) {
            String currentMethod = methodListModel.getElementAt(selectedIndex);
            String newMethod = JOptionPane.showInputDialog(this, "Modifier la méthode:", currentMethod);
            if (newMethod != null && !newMethod.trim().isEmpty()) {
                umlClass.editMethod(selectedIndex, newMethod.trim());
                methodListModel.set(selectedIndex, newMethod.trim());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une méthode à modifier.");
        }
    }

    private void removeMethod() {
        int selectedIndex = methodList.getSelectedIndex();
        if (selectedIndex != -1) {
            umlClass.removeMethod(String.valueOf(selectedIndex));
            methodListModel.remove(selectedIndex);
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une méthode à supprimer.");
        }
    }

}

