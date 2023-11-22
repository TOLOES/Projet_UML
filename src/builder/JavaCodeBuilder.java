package builder;

public class JavaCodeBuilder implements CodeBuilder {
    private StringBuilder code = new StringBuilder();

    @Override
    public CodeBuilder startClass(String className) {
        code.append("public class ").append(className).append(" {\n");
        return this;
    }

    @Override
    public CodeBuilder addAttribute(String attribute) {
        code.append("\tprivate ").append(attribute).append(";\n");
        return this;
    }

    @Override
    public CodeBuilder addMethod(String method) {
        code.append("\t").append(method).append(" {\n");
        code.append("\t\t// TODO: Implementer la méthode\n");
        code.append("\t}\n");
        return this;
    }

    @Override
    public CodeBuilder endClass() {
        code.append("}\n");
        return this;
    }

    @Override
    public String getCode() {
        return code.toString();
    }
}

