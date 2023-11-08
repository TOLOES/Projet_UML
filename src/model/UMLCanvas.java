package model;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class UMLCanvas extends JPanel {
    private final List<UMLClasse> umlClasses;


    public UMLCanvas() {
        this.umlClasses = new ArrayList<>();
    }

    public void addUMLClass(UMLClasse umlClass) {
        umlClasses.add(umlClass);
        repaint();
    }

    public void removeUMLClass(UMLClasse umlClass) {
        umlClasses.remove(umlClass);
        repaint();
    }

    // Dans UMLCanvas.java
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Dessiner les classes ici
        int x = 10; // Décalage initial horizontal
        for (UMLClasse umlClass : umlClasses) {
            int y = 30; // Décalage initial vertical
            int classHeight = 20 + (umlClass.getAttributes().size() + umlClass.getMethods().size()) * 15;
            g.drawRect(x, y, 200, classHeight); // Ajustez la largeur si nécessaire
            g.drawString(umlClass.getName(), x + 5, y + 15);
            y += 20;

            for (String attribute : umlClass.getAttributes()) {
                g.drawString(attribute, x + 5, y);
                y += 15;
            }

            y += 5; // Un petit espace entre les attributs et les méthodes

            for (String method : umlClass.getMethods()) {
                g.drawString(method, x + 5, y);
                y += 15;
            }

            x += 210; // Espacement pour la prochaine classe, ajustez selon la largeur
        }
    }

}



