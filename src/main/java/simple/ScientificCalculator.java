package simple;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.BiFunction;
import java.util.function.DoubleUnaryOperator;

/**
 * A self-contained scientific calculator.
 *
 * <p>Features:
 * <ul>
 *   <li>Tokenizer for numbers, operators, parentheses, functions, constants and variables.</li>
 *   <li>Recursive-descent / shunting-yard expression parser honouring precedence and associativity.</li>
 *   <li>Rich set of unary functions (trig, hyperbolic, log, roots, rounding, etc.).</li>
 *   <li>Binary operators including power, modulo and integer division.</li>
 *   <li>Named constants (pi, e, tau, phi) and user-assignable variables.</li>
 *   <li>Degree / radian mode switch for trigonometric functions.</li>
 *   <li>A tiny REPL so the class can be run directly.</li>
 * </ul>
 *
 * <p>The class has no external dependencies and is intentionally verbose so that
 * it works well as a demonstration / teaching artifact.
 */
public final class ScientificCalculator {

    /** Angle mode for trigonometric functions. */
    public enum AngleMode {
        RADIANS,
        DEGREES
    }

    // ------------------------------------------------------------------
    // State
    // ------------------------------------------------------------------

    private final Map<String, Double> variables = new HashMap<>();
    private final Map<String, Double> constants = new LinkedHashMap<>();
    private final Map<String, DoubleUnaryOperator> functions = new LinkedHashMap<>();
    private final Map<String, BiFunction<Double, Double, Double>> binaryFunctions = new LinkedHashMap<>();

    private AngleMode angleMode = AngleMode.RADIANS;
    private double lastResult = 0.0;

    public ScientificCalculator() {
        registerConstants();
        registerFunctions();
    }

    // ------------------------------------------------------------------
    // Registration
    // ------------------------------------------------------------------

    private void registerConstants() {
        constants.put("pi", Math.PI);
        constants.put("e", Math.E);
        constants.put("tau", Math.PI * 2.0);
        constants.put("phi", (1.0 + Math.sqrt(5.0)) / 2.0);
    }

    private void registerFunctions() {
        functions.put("sin", x -> Math.sin(toRadians(x)));
        functions.put("cos", x -> Math.cos(toRadians(x)));
        functions.put("tan", x -> Math.tan(toRadians(x)));
        functions.put("asin", x -> fromRadians(Math.asin(x)));
        functions.put("acos", x -> fromRadians(Math.acos(x)));
        functions.put("atan", x -> fromRadians(Math.atan(x)));
        functions.put("sinh", Math::sinh);
        functions.put("cosh", Math::cosh);
        functions.put("tanh", Math::tanh);
        functions.put("ln", Math::log);
        functions.put("log", Math::log10);
        functions.put("log2", x -> Math.log(x) / Math.log(2.0));
        functions.put("sqrt", Math::sqrt);
        functions.put("cbrt", Math::cbrt);
        functions.put("exp", Math::exp);
        functions.put("abs", Math::abs);
        functions.put("floor", Math::floor);
        functions.put("ceil", Math::ceil);
        functions.put("round", x -> (double) Math.round(x));
        functions.put("sign", Math::signum);
        functions.put("fact", ScientificCalculator::factorial);

        binaryFunctions.put("pow", Math::pow);
        binaryFunctions.put("atan2", (y, x) -> fromRadians(Math.atan2(y, x)));
        binaryFunctions.put("max", Math::max);
        binaryFunctions.put("min", Math::min);
        binaryFunctions.put("hypot", Math::hypot);
        binaryFunctions.put("gcd", (a, b) -> (double) gcd((long) Math.floor(a), (long) Math.floor(b)));
    }

    // ------------------------------------------------------------------
    // Public API
    // ------------------------------------------------------------------

    public void setAngleMode(AngleMode mode) {
        this.angleMode = Objects.requireNonNull(mode);
    }

    public AngleMode getAngleMode() {
        return angleMode;
    }

    public void callFunction() {
        getAngleMode();
    }

    public void callFunction2() {
        getAngleMode();
    }

    public double getLastResult() {
        return lastResult;
    }

    public void setVariable(String name, double value) {
        variables.put(name, value);
    }

    /**
     * Clears all user-defined variables.
     * Constants and functions are not affected.
     */
    public void clearVariables() {
        variables.clear();
    }

    /**
     * Evaluates an infix expression and returns its numeric value.
     * Supports assignments of the form {@code name = expression}.
     */
    public double evaluate(String expression) {
        Objects.requireNonNull(expression, "expression");
        String trimmed = expression.trim();
        if (trimmed.isEmpty()) {
            throw new CalculatorException("Empty expression");
        }

        String assignTarget = null;
        int eq = indexOfTopLevelAssignment(trimmed);
        if (eq >= 0) {
            assignTarget = trimmed.substring(0, eq).trim();
            if (!isValidIdentifier(assignTarget)) {
                throw new CalculatorException("Invalid assignment target: " + assignTarget);
            }
            trimmed = trimmed.substring(eq + 1).trim();
        }

        List<Token> tokens = tokenize(trimmed);
        List<Token> rpn = toReversePolish(tokens);
        double result = evaluateRpn(rpn);

        if (assignTarget != null) {
            variables.put(assignTarget, result);
        }
        lastResult = result;
        return result;
    }

    // ------------------------------------------------------------------
    // Tokenizer
    // ------------------------------------------------------------------

    private enum TokenType {
        NUMBER,
        IDENTIFIER,
        OPERATOR,
        LEFT_PAREN,
        RIGHT_PAREN,
        COMMA
    }

    private static final class Token {
        final TokenType type;
        final String text;
        final double value;

        Token(TokenType type, String text) {
            this(type, text, Double.NaN);
        }

        Token(TokenType type, String text, double value) {
            this.type = type;
            this.text = text;
            this.value = value;
        }

        @Override
        public String toString() {
            return type + "(" + text + ")";
        }
    }

    private List<Token> tokenize(String input) {
        List<Token> tokens = new ArrayList<>();
        int i = 0;
        int n = input.length();

        while (i < n) {
            char c = input.charAt(i);

            if (Character.isWhitespace(c)) {
                i++;
                continue;
            }

            if (Character.isDigit(c) || c == '.') {
                int start = i;
                boolean seenDot = false;
                boolean seenExp = false;
                while (i < n) {
                    char d = input.charAt(i);
                    if (Character.isDigit(d)) {
                        i++;
                    } else if (d == '.' && !seenDot && !seenExp) {
                        seenDot = true;
                        i++;
                    } else if ((d == 'e' || d == 'E') && !seenExp && i > start) {
                        seenExp = true;
                        i++;
                        if (i < n && (input.charAt(i) == '+' || input.charAt(i) == '-')) {
                            i++;
                        }
                    } else {
                        break;
                    }
                }
                String num = input.substring(start, i);
                try {
                    tokens.add(new Token(TokenType.NUMBER, num, Double.parseDouble(num)));
                } catch (NumberFormatException ex) {
                    throw new CalculatorException("Malformed number: " + num);
                }
                continue;
            }

            if (Character.isLetter(c) || c == '_') {
                int start = i;
                while (i < n && (Character.isLetterOrDigit(input.charAt(i)) || input.charAt(i) == '_')) {
                    i++;
                }
                tokens.add(new Token(TokenType.IDENTIFIER, input.substring(start, i)));
                continue;
            }

            switch (c) {
                case '(':
                    tokens.add(new Token(TokenType.LEFT_PAREN, "("));
                    break;
                case ')':
                    tokens.add(new Token(TokenType.RIGHT_PAREN, ")"));
                    break;
                case ',':
                    tokens.add(new Token(TokenType.COMMA, ","));
                    break;
                case '+':
                case '-':
                case '*':
                case '/':
                case '%':
                case '^':
                case '!':
                    tokens.add(new Token(TokenType.OPERATOR, String.valueOf(c)));
                    break;
                default:
                    throw new CalculatorException("Unexpected character '" + c + "' at position " + i);
            }
            i++;
        }
        return tokens;
    }

    // ------------------------------------------------------------------
    // Shunting-yard: infix -> RPN
    // ------------------------------------------------------------------

    private static final class OperatorInfo {
        final int precedence;
        final boolean rightAssociative;
        final boolean unary;

        OperatorInfo(int precedence, boolean rightAssociative, boolean unary) {
            this.precedence = precedence;
            this.rightAssociative = rightAssociative;
            this.unary = unary;
        }
    }

    private OperatorInfo operatorInfo(String op, boolean unaryContext) {
        switch (op) {
            case "u-":
            case "u+":
                return new OperatorInfo(5, true, true);
            case "^":
                return new OperatorInfo(4, true, false);
            case "!":
                return new OperatorInfo(6, false, true);
            case "*":
            case "/":
            case "%":
                return new OperatorInfo(3, false, false);
            case "+":
            case "-":
                return new OperatorInfo(2, false, false);
            default:
                throw new CalculatorException("Unknown operator: " + op);
        }
    }

    private List<Token> toReversePolish(List<Token> tokens) {
        List<Token> output = new ArrayList<>();
        Deque<Token> stack = new ArrayDeque<>();
        Token prev = null;

        for (Token token : tokens) {
            switch (token.type) {
                case NUMBER:
                    output.add(token);
                    break;

                case IDENTIFIER:
                    // Function call if immediately followed by '(' — handled lazily below.
                    if (isFunctionName(token.text)) {
                        stack.push(token);
                    } else {
                        output.add(token);
                    }
                    break;

                case COMMA:
                    while (!stack.isEmpty() && stack.peek().type != TokenType.LEFT_PAREN) {
                        output.add(stack.pop());
                    }
                    if (stack.isEmpty()) {
                        throw new CalculatorException("Misplaced comma or missing parenthesis");
                    }
                    break;

                case OPERATOR: {
                    String opText = token.text;
                    boolean unaryContext = prev == null
                            || prev.type == TokenType.OPERATOR
                            || prev.type == TokenType.LEFT_PAREN
                            || prev.type == TokenType.COMMA;

                    if ((opText.equals("-") || opText.equals("+")) && unaryContext) {
                        opText = "u" + opText;
                    }

                    Token opToken = new Token(TokenType.OPERATOR, opText);
                    OperatorInfo info = operatorInfo(opText, unaryContext);

                    if (opText.equals("!")) {
                        // Postfix unary: apply immediately.
                        output.add(opToken);
                        break;
                    }

                    while (!stack.isEmpty() && stack.peek().type == TokenType.OPERATOR) {
                        OperatorInfo top = operatorInfo(stack.peek().text, false);
                        boolean pop = top.precedence > info.precedence
                                || (top.precedence == info.precedence && !info.rightAssociative);
                        if (pop) {
                            output.add(stack.pop());
                        } else {
                            break;
                        }
                    }
                    stack.push(opToken);
                    break;
                }

                case LEFT_PAREN:
                    stack.push(token);
                    break;

                case RIGHT_PAREN:
                    while (!stack.isEmpty() && stack.peek().type != TokenType.LEFT_PAREN) {
                        output.add(stack.pop());
                    }
                    if (stack.isEmpty()) {
                        throw new CalculatorException("Mismatched parentheses");
                    }
                    stack.pop(); // discard '('
                    if (!stack.isEmpty() && stack.peek().type == TokenType.IDENTIFIER
                            && isFunctionName(stack.peek().text)) {
                        output.add(stack.pop());
                    }
                    break;

                default:
                    throw new CalculatorException("Unexpected token: " + token);
            }
            prev = token;
        }

        while (!stack.isEmpty()) {
            Token top = stack.pop();
            if (top.type == TokenType.LEFT_PAREN || top.type == TokenType.RIGHT_PAREN) {
                throw new CalculatorException("Mismatched parentheses");
            }
            output.add(top);
        }
        return output;
    }

    // ------------------------------------------------------------------
    // RPN evaluator
    // ------------------------------------------------------------------

    private double evaluateRpn(List<Token> rpn) {
        Deque<Double> stack = new ArrayDeque<>();

        for (Token token : rpn) {
            switch (token.type) {
                case NUMBER:
                    stack.push(token.value);
                    break;

                case IDENTIFIER:
                    if (isFunctionName(token.text)) {
                        applyFunction(token.text, stack);
                    } else {
                        stack.push(resolveValue(token.text));
                    }
                    break;

                case OPERATOR:
                    applyOperator(token.text, stack);
                    break;

                default:
                    throw new CalculatorException("Unexpected token in RPN: " + token);
            }
        }

        if (stack.size() != 1) {
            throw new CalculatorException("Invalid expression (stack size " + stack.size() + ")");
        }
        return stack.pop();
    }

    private void applyFunction(String name, Deque<Double> stack) {
        if (functions.containsKey(name)) {
            if (stack.isEmpty()) {
                throw new CalculatorException("Function " + name + " expects 1 argument");
            }
            double arg = stack.pop();
            stack.push(functions.get(name).applyAsDouble(arg));
        } else if (binaryFunctions.containsKey(name)) {
            if (stack.size() < 2) {
                throw new CalculatorException("Function " + name + " expects 2 arguments");
            }
            double b = stack.pop();
            double a = stack.pop();
            stack.push(binaryFunctions.get(name).apply(a, b));
        } else {
            throw new CalculatorException("Unknown function: " + name);
        }
    }

    private void applyOperator(String op, Deque<Double> stack) {
        switch (op) {
            case "u-":
                requireArgs(stack, 1, op);
                stack.push(-stack.pop());
                return;
            case "u+":
                requireArgs(stack, 1, op);
                // no-op, value stays
                return;
            case "!":
                requireArgs(stack, 1, op);
                stack.push(factorial(stack.pop()));
                return;
            default:
                break;
        }

        requireArgs(stack, 2, op);
        double b = stack.pop();
        double a = stack.pop();
        switch (op) {
            case "+":
                stack.push(a + b);
                break;
            case "-":
                stack.push(a - b);
                break;
            case "*":
                stack.push(a * b);
                break;
            case "/":
                if (b == 0.0) {
                    throw new CalculatorException("Division by zero");
                }
                stack.push(a / b);
                break;
            case "%":
                if (b == 0.0) {
                    throw new CalculatorException("Modulo by zero");
                }
                stack.push(a % b);
                break;
            case "^":
                stack.push(Math.pow(a, b));
                break;
            default:
                throw new CalculatorException("Unknown operator: " + op);
        }
    }

    private void requireArgs(Deque<Double> stack, int count, String op) {
        if (stack.size() < count) {
            throw new CalculatorException("Operator '" + op + "' requires " + count + " operand(s)");
        }
    }

    private double resolveValue(String name) {
        if (variables.containsKey(name)) {
            return variables.get(name);
        }
        if (constants.containsKey(name)) {
            return constants.get(name);
        }
        if ("ans".equals(name)) {
            return lastResult;
        }
        throw new CalculatorException("Unknown identifier: " + name);
    }

    // ------------------------------------------------------------------
    // Helpers
    // ------------------------------------------------------------------

    private boolean isFunctionName(String name) {
        return functions.containsKey(name) || binaryFunctions.containsKey(name);
    }

    private double toRadians(double value) {
        return angleMode == AngleMode.DEGREES ? Math.toRadians(value) : value;
    }

    private double fromRadians(double value) {
        return angleMode == AngleMode.DEGREES ? Math.toDegrees(value) : value;
    }

    private static double factorial(double x) {
        if (x < 0 || Math.floor(x) != x) {
            throw new CalculatorException("Factorial is only defined for non-negative integers");
        }
        double result = 1.0;
        for (int k = 2; k <= (int) x; k++) {
            result *= k;
        }
        return result;
    }

    private static long gcd(long a, long b) {
        a = Math.abs(a);
        b = Math.abs(b);
        while (b != 0) {
            long t = b;
            b = a % b;
            a = t;
        }
        return a;
    }

    private static boolean isValidIdentifier(String s) {
        if (s.isEmpty() || !(Character.isLetter(s.charAt(0)) || s.charAt(0) == '_')) {
            return false;
        }
        for (int i = 1; i < s.length(); i++) {
            if (!(Character.isLetterOrDigit(s.charAt(i)) || s.charAt(i) == '_')) {
                return false;
            }
        }
        return true;
    }

    private static int indexOfTopLevelAssignment(String expr) {
        int depth = 0;
        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);
            if (c == '(') {
                depth++;
            } else if (c == ')') {
                depth--;
            } else if (c == '=' && depth == 0) {
                // Ignore ==, <=, >= (none supported, but be safe).
                boolean prevCompare = i > 0 && "!<>=".indexOf(expr.charAt(i - 1)) >= 0;
                boolean nextEquals = i + 1 < expr.length() && expr.charAt(i + 1) == '=';
                if (!prevCompare && !nextEquals) {
                    return i;
                }
            }
        }
        return -1;
    }

    /** Exception thrown for any parsing or evaluation error. */
    public static final class CalculatorException extends RuntimeException {
        public CalculatorException(String message) {
            super(message);
        }
    }

    // ------------------------------------------------------------------
    // REPL
    // ------------------------------------------------------------------

    public static void main(String[] args) {
        ScientificCalculator calc = new ScientificCalculator();
        System.out.println("Scientific Calculator. Type 'help' for commands, 'quit' to exit.");

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print("> ");
                if (!scanner.hasNextLine()) {
                    break;
                }
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }
                if (line.equalsIgnoreCase("quit") || line.equalsIgnoreCase("exit")) {
                    break;
                }
                if (line.equalsIgnoreCase("help")) {
                    printHelp(calc);
                    continue;
                }
                if (line.equalsIgnoreCase("deg")) {
                    calc.setAngleMode(AngleMode.DEGREES);
                    System.out.println("Angle mode: DEGREES");
                    continue;
                }
                if (line.equalsIgnoreCase("rad")) {
                    calc.setAngleMode(AngleMode.RADIANS);
                    System.out.println("Angle mode: RADIANS");
                    continue;
                }
                try {
                    double result = calc.evaluate(line);
                    System.out.println(format(result));
                } catch (CalculatorException ex) {
                    System.out.println("Error: " + ex.getMessage());
                }
            }
        }
        System.out.println("Bye.");
    }

    private static void printHelp(ScientificCalculator calc) {
        System.out.println("Operators: + - * / % ^ ! (factorial)");
        System.out.println("Functions: " + String.join(", ", calc.functions.keySet()));
        System.out.println("Binary:    " + String.join(", ", calc.binaryFunctions.keySet()));
        System.out.println("Constants: " + String.join(", ", calc.constants.keySet()));
        System.out.println("Commands:  deg, rad, help, quit");
        System.out.println("Assign:    x = 2 + 3   then use 'x' or 'ans'");
    }

    private static String format(double value) {
        if (value == Math.floor(value) && !Double.isInfinite(value) && Math.abs(value) < 1e15) {
            return String.format(Locale.ROOT, "%d", (long) value);
        }
        return String.format(Locale.ROOT, "%.10g", value);
    }
}
