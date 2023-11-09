package model;

import controller.ClassEditorDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class UMLCanvas extends JPanel {
    private final List<UMLClasse> umlClasses;
    private UMLClasse selectedClass;
    private Point lastMousePosition;

    public UMLCanvas() {
        this.umlClasses = new ArrayList<>();
        setupMouseListeners();
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

    private void setupMouseListeners() {
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    UMLClasse clickedClass = findClassAt(e.getPoint());
                    if (clickedClass != null) {
                        JFrame frameAncestor = (JFrame) SwingUtilities.getWindowAncestor(UMLCanvas.this);
                        ClassEditorDialog editorDialog = new ClassEditorDialog(frameAncestor, clickedClass, UMLCanvas.this);
                        editorDialog.setVisible(true);
                    }
                } else {
                    selectedClass = findClassAt(e.getPoint());
                    lastMousePosition = e.getPoint();
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (selectedClass != null && lastMousePosition != null) {
                    int dx = e.getX() - lastMousePosition.x;
                    int dy = e.getY() - lastMousePosition.y;
                    selectedClass.setX(selectedClass.getX() + dx);
                    selectedClass.setY(selectedClass.getY() + dy);
                    lastMousePosition = e.getPoint();
                    repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                selectedClass = null;
                lastMousePosition = null;
            }
        };

        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
    }

    private int calculateClassHeight(UMLClasse umlClass) {
        FontMetrics metrics = getFontMetrics(getFont());
        int lineHeight = metrics.getHeight();
        // Hauteur initiale pour le nom de la classe et la marge
        int height = lineHeight + 5;
        // Ajouter la hauteur pour chaque attribut et méthode
        height += (umlClass.getAttributes().size() + umlClass.getMethods().size()) * lineHeight + 10;
        return height;
    }

    private UMLClasse findClassAt(Point point) {
        for (UMLClasse umlClass : umlClasses) {
            int classHeight = calculateClassHeight(umlClass);
            Rectangle bounds = new Rectangle(umlClass.getX(), umlClass.getY(), 150, classHeight);
            if (bounds.contains(point)) {
                return umlClass;
            }
        }
        return null;
    }

    public UMLClasse findUMLClasseByName(String name) {
        for (UMLClasse umlClass : umlClasses) {
            if (umlClass.getName().equalsIgnoreCase(name)) {
                return umlClass;
            }
        }
        return null;
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
        int lineHeight = metrics.getHeight();

        // Calcule la largeur maximale nécessaire pour le texte
        for (String attribute : umlClass.getAttributes()) {
            maxWidth = Math.max(maxWidth, metrics.stringWidth(attribute) + 10);
        }
        for (String method : umlClass.getMethods()) {
            maxWidth = Math.max(maxWidth, metrics.stringWidth(method) + 10);
        }

        int x = umlClass.getX();
        int y = umlClass.getY();

        // Dessine le nom de la classe
        g.drawString(umlClass.getName(), x + 5, y + metrics.getAscent());

        // Dessine une ligne horizontale sous le nom de la classe
        int separatorY = y + lineHeight;
        g.drawLine(x, separatorY, x + maxWidth, separatorY);

        int classHeight = calculateClassHeight(umlClass)+5;


        int attributeY = separatorY + 5;
        for (String attribute : umlClass.getAttributes()) {
            g.drawString(attribute, x + 5, attributeY + metrics.getAscent());
            attributeY += lineHeight;
            classHeight += lineHeight;
        }

        // Dessine une ligne horizontale sous les attributs
        int methodY = attributeY;
        if (!umlClass.getAttributes().isEmpty()) {
            methodY += 5;
            g.drawLine(x, methodY, x + maxWidth, methodY);
            classHeight += 5;
        }

        methodY += 5;
        for (String method : umlClass.getMethods()) {
            g.drawString(method, x + 5, methodY + metrics.getAscent());
            methodY += lineHeight;
            classHeight += lineHeight;
        }

        g.drawRect(umlClass.getX(), umlClass.getY(), maxWidth, classHeight);;
    }

}
