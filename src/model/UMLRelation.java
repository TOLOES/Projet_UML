package model;

import java.awt.*;

public class UMLRelation {
    private UMLClasse source;
    private UMLClasse destination;
    private String sourceCardinality;
    private String destinationCardinality;
    private RelationType type;
    
    public enum RelationType {
        AGGREGATION,
        COMPOSITION,
        INHERITANCE,
        ASSOCIATION
    }

    public UMLRelation(UMLClasse source, UMLClasse destination, String sourceCardinality, String destinationCardinality, RelationType type) {
        this.source = source;
        this.destination = destination;
        this.sourceCardinality = sourceCardinality;
        this.destinationCardinality = destinationCardinality;
        this.type = type;
    }

    public UMLClasse getSource() {
        return source;
    }

    public void setSource(UMLClasse source) {
        this.source = source;
    }

    public UMLClasse getDestination() {
        return destination;
    }

    public void setDestination(UMLClasse destination) {
        this.destination = destination;
    }

    public String getSourceCardinality() {
        return sourceCardinality;
    }

    public void setSourceCardinality(String sourceCardinality) {
        this.sourceCardinality = sourceCardinality;
    }

    public String getDestinationCardinality() {
        return destinationCardinality;
    }

    public void setDestinationCardinality(String destinationCardinality) {
        this.destinationCardinality = destinationCardinality;
    }

    public RelationType getType() {
        return type;
    }

}

