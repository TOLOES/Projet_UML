package model;

import controller.ClassEditorDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UMLCanvas extends JPanel implements Serializable   {
    private List<UMLClasse> umlClasses;
    private List<UMLRelation> umlRelations;
    private UMLClasse selectedClass;
    private Point lastMousePosition;
    private boolean showTemporaryMessage = false;
    private String temporaryMessage = "";

    private boolean isCreatingRelation = false;
    private UMLClasse firstSelectedClass;
    private UMLClasse secondSelectedClass;
    private boolean isDeletingRelation = false;

    public UMLCanvas() {
        this.umlClasses = new ArrayList<>();
        this.umlRelations = new ArrayList<>();
        setupMouseListeners();
    }

    public void saveSchema(File file) {
        try (FileOutputStream fileOut = new FileOutputStream(file);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(umlClasses);
            out.writeObject(umlRelations);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void loadSchema(String filePath) {
        try (FileInputStream fileIn = new FileInputStream(filePath);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            umlClasses = (List<UMLClasse>) in.readObject();
            umlRelations = (List<UMLRelation>) in.readObject();
            repaint();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startCreatingRelation() {
        isCreatingRelation = true;
        firstSelectedClass = null;
        secondSelectedClass = null;
        setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        // Réinitialise la sélection si l'utilisateur clique ailleurs
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

    public void startDeletingRelation() {
        isDeletingRelation = true;
        showTemporaryMessage = true;
        temporaryMessage = "Cliquez sur une relation pour la supprimer.";

        setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        repaint();

        Timer timer = new Timer(3000, e -> {
            showTemporaryMessage = false;
            repaint();
        });
        timer.setRepeats(false);
        timer.start();
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
                if (isDeletingRelation) {
                    UMLRelation relation = findRelationAt(e.getPoint());
                    if (relation != null) {
                        umlRelations.remove(relation);
                        repaint();
                        isDeletingRelation = false;
                        setCursor(Cursor.getDefaultCursor());
                    }
                }

                if (e.getClickCount() == 2 && !isDeletingRelation) {
                    UMLRelation clickedRelation = findRelationAt(e.getPoint());
                    if (clickedRelation != null) {
                        editRelation(clickedRelation);
                    }
                }
            }
        };

        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
    }


    private UMLRelation findRelationAt(Point point) {
        final double TOLERANCE = 10.0; // Tolérance pour la proximité du point à la ligne

        for (UMLRelation relation : umlRelations) {
            Point startPoint = getBorderPoint(relation.getSource(), relation.getDestination(), getGraphics());
            Point endPoint = getBorderPoint(relation.getDestination(), relation.getSource(), getGraphics());

            if (isPointNearLine(point, startPoint, endPoint, TOLERANCE)) {
                return relation;
            }
        }
        return null;
    }

    private boolean isPointNearLine(Point p, Point lineStart, Point lineEnd, double tolerance) {
        double normalLength = Math.hypot(lineEnd.x - lineStart.x, lineEnd.y - lineStart.y);
        double distance = Math.abs((p.x - lineStart.x) * (lineEnd.y - lineStart.y) - (p.y - lineStart.y) * (lineEnd.x - lineStart.x)) / normalLength;
        return distance <= tolerance;
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

    private void editRelation(UMLRelation relation) {

        JComboBox<UMLRelation.RelationType> typeComboBox = new JComboBox<>(UMLRelation.RelationType.values());
        JTextField sourceCardinalityField = new JTextField(relation.getSourceCardinality(), 10);
        JTextField destinationCardinalityField = new JTextField(relation.getDestinationCardinality(), 10);

        typeComboBox.setSelectedItem(relation.getType());

        JPanel panel = new JPanel();
        panel.add(new JLabel("Type de relation:"));
        panel.add(typeComboBox);
        panel.add(new JLabel("Cardinalité source:"));
        panel.add(sourceCardinalityField);
        panel.add(new JLabel("Cardinalité destination:"));
        panel.add(destinationCardinalityField);

        // Affiche la boîte de dialogue
        int result = JOptionPane.showConfirmDialog(this, panel, "Modifier la relation", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            relation.setType((UMLRelation.RelationType) typeComboBox.getSelectedItem());
            relation.setSourceCardinality(sourceCardinalityField.getText());
            relation.setDestinationCardinality(destinationCardinalityField.getText());
            repaint();
        }
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
        int headerHeight = lineHeight + 5;

        // Calcule la hauteur totale en fonction du nombre d'attributs et de méthodes
        int attributesHeight = umlClass.getAttributes().size() * lineHeight;
        int methodsHeight = umlClass.getMethods().size() * lineHeight;

        // Ajoute de l'espace supplémentaire pour la séparation entre le nom, les attributs et les méthodes
        int totalHeight = headerHeight + attributesHeight + methodsHeight;
        if (!umlClass.getAttributes().isEmpty()) {
            totalHeight += 5;
        }
        if (!umlClass.getMethods().isEmpty()) {
            totalHeight += 5;
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

    public List<UMLClasse> getUmlClasses() {
        return umlClasses;
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

        if (showTemporaryMessage) {
            drawCenteredTopMessage(g, temporaryMessage);
        }
    }


    private void drawCenteredTopMessage(Graphics g, String message) {
        FontMetrics fm = g.getFontMetrics();
        int messageWidth = fm.stringWidth(message);

        int x = (getWidth() - messageWidth) / 2;
        int y = fm.getHeight();

        g.setColor(Color.BLACK);
        g.drawString(message, x, y);
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

        // Calcule les points de départ et d'arrivée en fonction de la position des classes
        Point startPoint = getBorderPoint(source, destination, g);
        Point endPoint = getBorderPoint(destination, source, g);

        // Dessine la ligne entre les points calculés
        g.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);

        // Dessin des types de relation
        switch (relation.getType()) {
            case AGGREGATION:
                drawDiamond(g, endPoint.x, endPoint.y, false); // Losange vide pour l'agrégation
                break;
            case COMPOSITION:
                drawDiamond(g, endPoint.x, endPoint.y, true); // Losange plein pour la composition
                break;
            case INHERITANCE:
                drawArrow(g, startPoint.x, startPoint.y, endPoint.x, endPoint.y); // Flèche pour l'héritage
                break;
            case ASSOCIATION:
                break;
        }

        // Calcule les points pour les cardinalités
        Point midStartPoint = new Point((startPoint.x * 3 + endPoint.x) / 4, (startPoint.y * 3 + endPoint.y) / 4);
        Point midEndPoint = new Point((endPoint.x * 3 + startPoint.x) / 4, (endPoint.y * 3 + startPoint.y) / 4);

        // Dessine les cardinalités
        drawCardinality(g, midStartPoint.x, midStartPoint.y, relation.getSourceCardinality());
        drawCardinality(g, midEndPoint.x, midEndPoint.y, relation.getDestinationCardinality());
    }

    private Point getBorderPoint(UMLClasse umlClass, UMLClasse otherClass, Graphics g) {
        int x = umlClass.getX();
        int y = umlClass.getY();
        int width = 150;
        int height = calculateClassHeight(umlClass, g);

        int otherCenterX = otherClass.getX() + width / 2;
        int otherCenterY = otherClass.getY() + calculateClassHeight(otherClass, g) / 2;

        // Détermine le point de bord en fonction de la position de l'autre classe
        if (otherCenterY < y) { // classe est au-dessus
            return new Point(x + width / 2, y); // Milieu du bord supérieur
        } else if (otherCenterY > y + height) { //classe est en dessous
            return new Point(x + width / 2, y + height); // Milieu du bord inférieur
        } else if (otherCenterX < x) { //classe est à gauche
            return new Point(x, y + height / 2); // Milieu du bord gauche
        } else { //classe est à droite
            return new Point(x + width, y + height / 2); // Milieu du bord droit
        }
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
