public class TestQuickFixes {
    public void testMethod() {
        // Проблема: использование println вместо logging
        System.out.println("Test message");
        
        // Проблема: неиспользуемая переменная
        int unusedVariable = 42;
        
        // Проблема: может быть null
        String str = null;
        str.length();
    }
}
