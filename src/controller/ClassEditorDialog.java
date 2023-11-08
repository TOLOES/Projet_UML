package controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import model.UMLClasse;
import model.UMLCanvas;

public class ClassEditorDialog extends JDialog {

    private final UMLClasse umlClass;
    private final DefaultListModel<String> attributeListModel;
    private final DefaultListModel<String> methodListModel;
    private final JList<String> attributeList;
    private final JList<String> methodList;
    private final UMLCanvas canvas;

    public ClassEditorDialog(JFrame owner, UMLClasse umlClass, UMLCanvas canvas) {
        super(owner, "Edit UML Class", true);
        this.umlClass = umlClass;
        this.canvas = canvas;

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
        addAttributeButton.addActionListener(e -> showAttributePopupMenu(addAttributeButton));
        attributesPanel.add(addAttributeButton, BorderLayout.SOUTH);

        // Methods panel
        JPanel methodsPanel = new JPanel(new BorderLayout());
        methodsPanel.add(new JLabel("Methods:"), BorderLayout.NORTH);
        methodsPanel.add(new JScrollPane(methodList), BorderLayout.CENTER);
        JButton addMethodButton = new JButton("Add Method");
        addMethodButton.addActionListener(e -> showMethodPopupMenu(addMethodButton));
        methodsPanel.add(addMethodButton, BorderLayout.SOUTH);

        // Validate button
        JButton validateButton = new JButton("Validate");
        validateButton.addActionListener(e -> validateChanges());

        // Add panels to dialog
        add(attributesPanel, BorderLayout.WEST);
        add(methodsPanel, BorderLayout.CENTER);
        add(validateButton, BorderLayout.PAGE_END);
    }

    private void showAttributePopupMenu(JComponent component) {
        JPopupMenu popupMenu = createAttributePopupMenu();
        popupMenu.show(component, 0, component.getHeight());
    }

    private void showMethodPopupMenu(JComponent component) {
        JPopupMenu popupMenu = createMethodPopupMenu();
        popupMenu.show(component, 0, component.getHeight());
    }

    private void setupListeners() {
    }

    private JPopupMenu createAttributePopupMenu() {
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem addAttribute = new JMenuItem("Ajouter");
        addAttribute.addActionListener(this::addAttribute);
        popupMenu.add(addAttribute);

        JMenuItem editAttribute = new JMenuItem("Modifier");
        editAttribute.addActionListener(this::editAttribute);
        popupMenu.add(editAttribute);

        JMenuItem removeAttribute = new JMenuItem("Supprimer");
        removeAttribute.addActionListener(this::removeAttribute);
        popupMenu.add(removeAttribute);

        return popupMenu;
    }

    private JPopupMenu createMethodPopupMenu() {
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem addMethod = new JMenuItem("Ajouter");
        addMethod.addActionListener(this::addMethod);
        popupMenu.add(addMethod);

        JMenuItem editMethod = new JMenuItem("Modifier");
        editMethod.addActionListener(this::editMethod);
        popupMenu.add(editMethod);

        JMenuItem removeMethod = new JMenuItem("Supprimer");
        removeMethod.addActionListener(this::removeMethod);
        popupMenu.add(removeMethod);

        return popupMenu;
    }


    private void addAttribute(ActionEvent e) {
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


    private void editAttribute(ActionEvent e) {
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

    private void removeAttribute(ActionEvent e) {
        int selectedIndex = attributeList.getSelectedIndex();
        if (selectedIndex != -1) {
            umlClass.removeAttribute((selectedIndex));
            attributeListModel.remove(selectedIndex);
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un attribut à supprimer.");
        }
    }

    private void addMethod(ActionEvent e) {
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


    private void editMethod(ActionEvent e) {
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

    private void removeMethod(ActionEvent e) {
        int selectedIndex = methodList.getSelectedIndex();
        if (selectedIndex != -1) {
            umlClass.removeMethod((selectedIndex));
            methodListModel.remove(selectedIndex);
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une méthode à supprimer.");
        }
    }

    private void validateChanges() {
        if (canvas != null) {
            canvas.redrawCanvas(umlClass);
        }
        dispose();
    }

}

