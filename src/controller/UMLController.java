package controller;

import model.UMLCanvas;
import model.UMLClasse;
import builder.JavaCodeBuilder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class UMLController {

    public void generateJavaCode(UMLCanvas canvas, File file) {
        List<UMLClasse> classes = canvas.getUmlClasses();
        StringBuilder code = new StringBuilder();

        for (UMLClasse umlClass : classes) {
            JavaCodeBuilder builder = new JavaCodeBuilder();
            builder.startClass(umlClass.getName());

            for (String attribute : umlClass.getAttributes()) {
                builder.addAttribute(attribute);
            }

            for (String method : umlClass.getMethods()) {
                builder.addMethod(method);
            }

            builder.endClass();
            code.append(builder.getCode()).append("\n\n");
        }

        // Écrit dans le fichier
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(code.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
