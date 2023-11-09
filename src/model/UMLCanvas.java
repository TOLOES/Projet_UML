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

    public void redrawCanvas(UMLClasse umlClass) {
        for (int i = 0; i < umlClasses.size(); i++) {
            UMLClasse existingClass = umlClasses.get(i);
            if (existingClass.getName().equals(umlClass.getName())) {
                umlClasses.set(i, umlClass);
                break;
            }
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (UMLClasse umlClass : umlClasses) {
            drawUMLClass(g, umlClass);
        }
    }

    private void drawUMLClass(Graphics g, UMLClasse umlClass) {
        FontMetrics metrics = g.getFontMetrics();
        int maxWidth = 150;
        for (String attribute : umlClass.getAttributes()) {
            maxWidth = Math.max(maxWidth, metrics.stringWidth(attribute) + 10);
        }
        for (String method : umlClass.getMethods()) {
            maxWidth = Math.max(maxWidth, metrics.stringWidth(method) + 10);
        }

        int x = 10;
        int startY = 10;
        int lineHeight = metrics.getHeight();
        int classHeight = lineHeight;

        // Calcule la hauteur totale avant de dessiner le rectangle
        classHeight += (umlClass.getAttributes().size() + umlClass.getMethods().size()) * lineHeight + 10; // Ajouter de l'espace supplémentaire

        // Dessine le rectangle pour la classe
        g.drawRect(x, startY, maxWidth, classHeight);

        // Dessine le nom de la classe
        int nameY = startY + metrics.getAscent();
        g.drawString(umlClass.getName(), x + 5, nameY);

        // Dessine une ligne horizontale sous le nom de la classe
        int separatorY = startY + lineHeight;
        g.drawLine(x, separatorY, x + maxWidth, separatorY);

        // Décalage pour les attributs
        int attributeY = separatorY + 5;
        for (String attribute : umlClass.getAttributes()) {
            g.drawString(attribute, x + 5, attributeY + metrics.getAscent());
            attributeY += lineHeight;
        }

           // Dessine  une ligne horizontale sous les attributs
        int methodY = attributeY + 5;
        for (String method : umlClass.getMethods()) {
            g.drawString(method, x + 5, methodY + metrics.getAscent());
            methodY += lineHeight;
        }
    }
}
