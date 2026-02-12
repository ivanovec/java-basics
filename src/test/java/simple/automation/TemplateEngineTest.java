package simple.automation;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TemplateEngineTest {

    @Test
    void renderReplacesProvidedVariablesOnly() {
        TemplateEngine engine = new TemplateEngine("Hello {{ name }}! {{ missing }}");
        String result = engine.render(Map.of("name", "Ivan"));
        assertEquals("Hello Ivan! {{ missing }}", result);
    }

    @Test
    void renderWithDefaultsReplacesAllVariables() {
        TemplateEngine engine = new TemplateEngine("{{a}}-{{ b }}-{{c}}");
        String result = engine.renderWithDefaults(Map.of("b", 2), "X");
        assertEquals("X-2-X", result);
    }

    @Test
    void hasVariablesDetectsPresence() {
        assertTrue(new TemplateEngine("{{value}}").hasVariables());
        assertFalse(new TemplateEngine("plain text").hasVariables());
    }

    @Test
    void setTemplateAllowsReuse() {
        TemplateEngine engine = new TemplateEngine("{{x}}");
        engine.setTemplate("Value: {{x}}");
        assertEquals("Value: 5", engine.render(Map.of("x", 5)));
    }

    @Test
    void quickRenderUsesEngineInternals() {
        String result = TemplateEngine.quickRender("Hi, {{name}}", Map.of("name", "Olga"));
        assertEquals("Hi, Olga", result);
    }
}
