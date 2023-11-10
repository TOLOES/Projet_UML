package model;

import controller.ClassEditorDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

public class UMLCanvas extends JPanel {
    private final List<UMLClasse> umlClasses;
    private List<UMLRelation> umlRelations;
    private UMLClasse selectedClass;
    private Point lastMousePosition;

    private boolean isCreatingRelation = false;
    private UMLClasse firstSelectedClass;
    private UMLClasse secondSelectedClass;

    public UMLCanvas() {
        this.umlClasses = new ArrayList<>();
        this.umlRelations = new ArrayList<>();
        setupMouseListeners();
    }

    public void startCreatingRelation() {
        isCreatingRelation = true;
        firstSelectedClass = null;
        secondSelectedClass = null;
        // Peut-être changer le curseur ou ajouter un label temporaire pour indiquer à l'utilisateur de sélectionner une classe
        setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        // Réinitialiser la sélection si l'utilisateur clique ailleurs
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isCreatingRelation && findClassAt(e.getPoint()) == null) {
                    resetRelationCreation();
                }
            }
        });
        repaint();
    }



    public void addUMLClass(UMLClasse umlClass) {
        umlClasses.add(umlClass);
        repaint();
    }

    public void removeUMLClass(UMLClasse umlClass) {
        umlClasses.remove(umlClass);
        repaint();
    }

    public void addUMLRelation(UMLRelation relation) {
        umlRelations.add(relation);
        repaint();
    }

    public void removeUMLRelation(UMLRelation relation) {
        umlRelations.remove(relation);
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

            public void mouseClicked(MouseEvent e) {
                if (isCreatingRelation) {
                    UMLClasse selectedClass = findClassAt(e.getPoint());
                    if (selectedClass != null) {
                        if (firstSelectedClass == null) {
                            firstSelectedClass = selectedClass;
                        } else if (secondSelectedClass == null && selectedClass != firstSelectedClass) {
                            secondSelectedClass = selectedClass;
                            askForRelationDetails();
                        }
                    }
                }
            }
        };

        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
    }

    private void askForRelationDetails() {
        // Demande le type de relation
        UMLRelation.RelationType[] types = UMLRelation.RelationType.values();
        UMLRelation.RelationType selectedType = (UMLRelation.RelationType) JOptionPane.showInputDialog(
                this,
                "Sélectionnez le type de relation :",
                "Type de relation",
                JOptionPane.QUESTION_MESSAGE,
                null,
                types,
                types[0]
        );

        if (selectedType == null) {
            resetRelationCreation();
            return;
        }

        // Demande les cardinalités
        String sourceCardinality = JOptionPane.showInputDialog(this, "Cardinalité pour " + firstSelectedClass.getName() + " :");
        String destinationCardinality = JOptionPane.showInputDialog(this, "Cardinalité pour " + secondSelectedClass.getName() + " :");

        if (sourceCardinality == null || destinationCardinality == null) {
            resetRelationCreation();
            return;
        }

        UMLRelation newRelation = new UMLRelation(firstSelectedClass, secondSelectedClass, sourceCardinality, destinationCardinality, selectedType);
        umlRelations.add(newRelation);
        repaint();


        resetRelationCreation();
    }

    private void resetRelationCreation() {
        isCreatingRelation = false;
        firstSelectedClass = null;
        secondSelectedClass = null;
        setCursor(Cursor.getDefaultCursor());
        repaint();
    }

    private int calculateClassHeight(UMLClasse umlClass, Graphics g) {
        FontMetrics metrics = g.getFontMetrics();
        int lineHeight = metrics.getHeight();
        int headerHeight = lineHeight + 5; // Hauteur pour le nom de la classe + un peu d'espace

        // Calculez la hauteur totale en fonction du nombre d'attributs et de méthodes
        int attributesHeight = umlClass.getAttributes().size() * lineHeight;
        int methodsHeight = umlClass.getMethods().size() * lineHeight;

        // Ajoutez de l'espace supplémentaire pour la séparation entre le nom, les attributs et les méthodes
        int totalHeight = headerHeight + attributesHeight + methodsHeight;
        if (!umlClass.getAttributes().isEmpty()) {
            totalHeight += 5; // Espace supplémentaire si la classe a des attributs
        }
        if (!umlClass.getMethods().isEmpty()) {
            totalHeight += 5; // Espace supplémentaire si la classe a des méthodes
        }

        return totalHeight;
    }

    private UMLClasse findClassAt(Point point) {
        for (UMLClasse umlClass : umlClasses) {
            int classHeight = calculateClassHeight(umlClass, getGraphics());
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
        for (UMLRelation relation : umlRelations) {
            drawUMLRelation(g, relation);
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

        int classHeight = calculateClassHeight(umlClass, getGraphics())+5;


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

    private void drawUMLRelation(Graphics g, UMLRelation relation) {
        UMLClasse source = relation.getSource();
        UMLClasse destination = relation.getDestination();

        int sourceX = source.getX() + 150 / 2;
        int sourceY = source.getY() + calculateClassHeight(source, g) / 2;
        int destX = destination.getX() + 150 / 2;
        int destY = destination.getY() + calculateClassHeight(destination, g) / 2;

        // Dessiner la ligne entre les classes
        g.drawLine(sourceX, sourceY, destX, destY);


        switch (relation.getType()) {
            case AGGREGATION:
                drawDiamond(g, destX, destY, false);
                break;
            case COMPOSITION:
                drawDiamond(g, destX, destY, true);
                break;
            case INHERITANCE:
                drawArrow(g, sourceX, sourceY, destX, destY);
                break;
            case ASSOCIATION:
                break;
        }

        // Dessine les cardinalités
        drawCardinality(g, sourceX, sourceY, relation.getSourceCardinality());
        drawCardinality(g, destX, destY, relation.getDestinationCardinality());
    }

    private void drawArrow(Graphics g, int x1, int y1, int x2, int y2) {
        g.drawLine(x1, y1, x2, y2);

        // Logique pour dessiner la tête de la flèche
        int arrowSize = 10;
        int dx = x2 - x1, dy = y2 - y1;
        double D = Math.sqrt(dx * dx + dy * dy);
        double xm = D - arrowSize, xn = xm, ym = arrowSize, yn = -arrowSize, x;
        double sin = dy / D, cos = dx / D;

        x = xm * cos - ym * sin + x1;
        ym = xm * sin + ym * cos + y1;
        xm = x;

        x = xn * cos - yn * sin + x1;
        yn = xn * sin + yn * cos + y1;
        xn = x;

        int[] xpoints = {x2, (int) xm, (int) xn};
        int[] ypoints = {y2, (int) ym, (int) yn};

        g.fillPolygon(xpoints, ypoints, 3);
    }

    private void drawDiamond(Graphics g, int x, int y, boolean filled) {
        int diamondSize = 10;
        int[] xpoints = {x, x - diamondSize, x, x + diamondSize};
        int[] ypoints = {y - diamondSize, y, y + diamondSize, y};

        if (filled) {
            g.fillPolygon(xpoints, ypoints, 4);
        } else {
            g.drawPolygon(xpoints, ypoints, 4);
        }
    }

    private void drawCardinality(Graphics g, int x, int y, String cardinality) {
        g.drawString(cardinality, x, y);
    }
}
