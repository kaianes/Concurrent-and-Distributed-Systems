package uk.ac.bradford.cds.cw2.integration;

import java.util.function.DoubleUnaryOperator;

public final class MathUtils {

    private MathUtils() {
    }

    public static void validateFunction(String functionType, String functionData) {
        buildFunction(functionType, functionData);
    }

    public static double trapezoidal(String functionType, String functionData, double a, double b, int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Invalid number of subintervals: n must be > 0");
        }

        DoubleUnaryOperator f = buildFunction(functionType, functionData);
        double h = (b - a) / n;
        double sum = 0.5 * (f.applyAsDouble(a) + f.applyAsDouble(b));

        for (int i = 1; i < n; i++) {
            sum += f.applyAsDouble(a + (i * h));
        }
        return h * sum;
    }

    public static double midpoint(String functionType, String functionData, double a, double b, int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Invalid number of subintervals: n must be > 0");
        }

        DoubleUnaryOperator f = buildFunction(functionType, functionData);
        double h = (b - a) / n;
        double sum = 0.0;

        for (int i = 0; i < n; i++) {
            sum += f.applyAsDouble(a + ((i + 0.5) * h));
        }
        return h * sum;
    }

    private static DoubleUnaryOperator buildFunction(String functionType, String functionData) {
        if (functionType == null || functionData == null) {
            throw new IllegalArgumentException("Invalid function format");
        }

        String type = functionType.trim().toUpperCase();
        String data = functionData.trim();

        if ("PREDEF".equals(type)) {
            String name = data.toUpperCase();
            return switch (name) {
                case "SIN" -> Math::sin;
                case "COS" -> Math::cos;
                case "EXP" -> Math::exp;
                default -> throw new IllegalArgumentException("Invalid function format");
            };
        }

        if ("POLY".equals(type)) {
            double[] coefficients = parseCoefficients(data);
            return x -> evaluatePolynomial(coefficients, x);
        }

        throw new IllegalArgumentException("Invalid function format");
    }

    private static double[] parseCoefficients(String text) {
        if (text.isBlank()) {
            throw new IllegalArgumentException("Invalid function format");
        }

        String[] parts = text.split(",", -1);
        double[] coefficients = new double[parts.length];

        for (int i = 0; i < parts.length; i++) {
            String value = parts[i].trim();
            if (value.isEmpty()) {
                throw new IllegalArgumentException("Invalid function format");
            }
            try {
                coefficients[i] = Double.parseDouble(value);
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("Invalid function format");
            }
        }

        return coefficients;
    }

    private static double evaluatePolynomial(double[] coefficients, double x) {
        double result = 0.0;
        for (double c : coefficients) {
            result = (result * x) + c;
        }
        return result;
    }
}
