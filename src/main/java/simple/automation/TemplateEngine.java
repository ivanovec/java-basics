package simple.automation;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateEngine {
    private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\{\\{([^}]+)}}");
    private String template;

    public TemplateEngine(String template) {
        this.template = template;
    }

    public String render(Map<String, Object> variables) {
        String result = template;
        Matcher matcher = VARIABLE_PATTERN.matcher(template);

        while (matcher.find()) {
            String variableName = matcher.group(1).trim();
            Object value = variables.get(variableName);

            if (value != null) {
                result = result.replace("{{" + matcher.group(1) + "}}", value.toString());
            }
        }

        return result;
    }

    public TemplateEngine setTemplate(String template) {
        this.template = template;
        return this;
    }

    public String renderWithDefaults(Map<String, Object> variables, String defaultValue) {
        String result = template;
        Matcher matcher = VARIABLE_PATTERN.matcher(template);

        while (matcher.find()) {
            String variableName = matcher.group(1).trim();
            Object value = variables.getOrDefault(variableName, defaultValue);
            result = result.replace("{{" + matcher.group(1) + "}}", value.toString());
        }

        return result;
    }

    public boolean hasVariables() {
        return VARIABLE_PATTERN.matcher(template).find();
    }

    public static String quickRender(String template, Map<String, Object> variables) {
        return new TemplateEngine(template).render(variables);
    }
}
