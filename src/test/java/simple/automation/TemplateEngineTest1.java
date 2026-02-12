package simple.automation;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TemplateEngineTest1 {

    @Test
    void renderReplacesProvidedVariablesOnly() {
        TemplateEngine1 engine = new TemplateEngine1("Hello {{ name }}! {{ missing }}");
        String result = engine.render(Map.of("name", "Ivan"));
        assertEquals("Hello Ivan! {{ missing }}", result);
    }

    @Test
    void renderWithDefaultsReplacesAllVariables() {
        TemplateEngine1 engine = new TemplateEngine1("{{a}}-{{ b }}-{{c}}");
        String result = engine.renderWithDefaults(Map.of("b", 2), "X");
        assertEquals("X-2-X", result);
    }

    @Test
    void hasVariablesDetectsPresence() {
        assertTrue(new TemplateEngine1("{{value}}").hasVariables());
        assertFalse(new TemplateEngine1("plain text").hasVariables());
    }

    @Test
    void setTemplateAllowsReuse() {
        TemplateEngine1 engine = new TemplateEngine1("{{x}}");
        engine.setTemplate("Value: {{x}}");
        assertEquals("Value: 5", engine.render(Map.of("x", 5)));
    }

    @Test
    void quickRenderUsesEngineInternals() {
        String result = TemplateEngine1.quickRender("Hi, {{name}}", Map.of("name", "Olga"));
        assertEquals("Hi, Olga", result);
    }
}
