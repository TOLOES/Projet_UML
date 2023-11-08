package model;

import java.util.ArrayList;
import java.util.List;

public class UMLClasse {
    private String name;
    private List<String> attributes;
    private List<String> methods;

    public UMLClasse(String name) {
        this.name = name;
        this.attributes = new ArrayList<>();
        this.methods = new ArrayList<>();
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getAttributes() {
        return attributes;
    }

    public List<String> getMethods() {
        return methods;
    }

    // Methods to manipulate attributes and methods
    public void addAttribute(String attribute) {
        attributes.add(attribute);
    }

    public void removeAttribute(String attribute) {
        attributes.remove(attribute);
    }

    public void addMethod(String method) {
        methods.add(method);
    }

    public void removeMethod(String method) {
        methods.remove(method);
    }

    // Method to edit existing methods or attributes
    public void editMethod(int index, String newMethod) {
        if (index >= 0 && index < methods.size()) {
            methods.set(index, newMethod);
        }
    }

    public void editAttribute(int index, String newAttribute) {
        if (index >= 0 && index < attributes.size()) {
            attributes.set(index, newAttribute);
        }
    }
}

