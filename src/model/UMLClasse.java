package model;

import java.util.ArrayList;
import java.util.List;

public class UMLClasse {
    private  int x;
    private  int y;
    private String name;
    private List<String> attributes;
    private List<String> methods;

    public UMLClasse(String name, int x, int y) {
        this.name = name;
        this.attributes = new ArrayList<>();
        this.methods = new ArrayList<>();
        this.x = x;
        this.y = y;
    }

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

    public void addAttribute(String attribute) {
        attributes.add(attribute);
    }
    public int getX() { return x; }
    public int getY() { return y; }
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }

    public void removeAttribute(int index) {
        if (index >= 0 && index < attributes.size()) {
            attributes.remove(index);
        }
    }

    public void addMethod(String method) {
        methods.add(method);
    }

    public void removeMethod(int index) {
        if (index >= 0 && index < methods.size()) {
            methods.remove(index);
        }
    }

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

