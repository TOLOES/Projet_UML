package builder;

public interface CodeBuilder {
    CodeBuilder startClass(String className);
    CodeBuilder addAttribute(String attribute);
    CodeBuilder addMethod(String method);
    CodeBuilder endClass();
    String getCode();
}
