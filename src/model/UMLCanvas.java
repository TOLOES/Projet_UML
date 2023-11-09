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

        int x = 10;
        int y = 10;

        g.drawRect(x, y, 150, 20);
        g.drawString(umlClass.getName(), x + 5, y + 15);

        y += 25;
        for (String attribute : umlClass.getAttributes()) {
            g.drawString(attribute, x + 5, y);
            y += 15;
        }

        y += 25;
        for (String method : umlClass.getMethods()) {
            g.drawString(method, x + 5, y);
            y += 15;
        }
    }
}




