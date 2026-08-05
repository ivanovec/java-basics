package simple;

import java.util.Scanner;

public class Calculator {

    public int add(int a, int b) {
        return a + b;
    }

    public int subtract(int a, int b) {
        return a - b;
    }

    public int multiply(int a, int b) {
        return a * b;
    }

    public double divide(int a, int b) {
        if (b == 0) {
            throw new ArithmeticException("Division by zero");
        }
        return (double) a / b;
    }

    public double calculate(int a, int b, char operation) {
        switch (operation) {
            case '+':
                return add(a, b);
            case '-':
                return subtract(a, b);
            case '*':
                return multiply(a, b);
            case '/':
                return divide(a, b);
            default:
                throw new IllegalArgumentException("Unknown operation: " + operation);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Calculator calculator = new Calculator();

        try {
            System.out.print("Enter the first integer: ");
            int firstNumber = scanner.nextInt();

            System.out.print("Enter an operation (+, -, *, /): ");
            char operation = scanner.next().charAt(0);

            System.out.print("Enter the second integer: ");
            int secondNumber = scanner.nextInt();

            double result = calculator.calculate(firstNumber, secondNumber, operation);
            System.out.println("Result: " + result);
        } catch (ArithmeticException | IllegalArgumentException exception) {
            System.out.println("Error: " + exception.getMessage());
        } catch (java.util.InputMismatchException exception) {
            System.out.println("Error: please enter integers only");
        }
    }
}
