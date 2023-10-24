package dev.rachamon.rachamoncore.api.utils;

import java.util.Stack;

public class ExpressionEvaluator {
    public static double evaluateExpression(String expression) {
        char[] tokens = expression.toCharArray();

        Stack<Double> values = new Stack<>();
        Stack<Character> operators = new Stack<>();

        for (int i = 0; i < tokens.length; i++) {
            char token = tokens[i];

            if (token >= '0' && token <= '9') {
                StringBuilder sb = new StringBuilder();
                while (i < tokens.length && (tokens[i] >= '0' && tokens[i] <= '9' || tokens[i] == '.')) {
                    sb.append(tokens[i]);
                    i++;
                }
                values.push(Double.parseDouble(sb.toString()));
                i--;
            } else if (token == '(') {
                operators.push(token);
            } else if (token == ')') {
                while (operators.peek() != '(') {
                    values.push(applyOperator(operators.pop(), values.pop(), values.pop()));
                }
                operators.pop(); // Pop the opening parenthesis
            } else if (token == '+' || token == '-' || token == '*' || token == '/') {
                while (!operators.isEmpty() && hasPrecedence(token, operators.peek())) {
                    values.push(applyOperator(operators.pop(), values.pop(), values.pop()));
                }
                operators.push(token);
            }
        }

        while (!operators.isEmpty()) {
            values.push(applyOperator(operators.pop(), values.pop(), values.pop()));
        }

        return values.pop();
    }

    private static boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')') {
            return false;
        }
        return (op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-');
    }

    private static double applyOperator(char operator, double b, double a) {
        switch (operator) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                return a / b;
        }
        return 0;
    }
}
