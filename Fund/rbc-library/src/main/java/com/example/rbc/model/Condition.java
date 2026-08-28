package com.example.rbc.model;

import java.math.BigDecimal;
import java.util.Objects;

import com.example.rbc.exception.InvalidRuleException;

/**
 * A comparison made between a named request attribute and an expected value.
 */
public final class Condition {
    private final String attribute;
    private final ComparisonOperator operator;
    private final Object expectedValue;

    public Condition(String attribute, ComparisonOperator operator, Object expectedValue) {
        if (attribute == null || attribute.isBlank())
            throw new InvalidRuleException("condition attribute must not be blank");
        this.attribute = attribute;
        this.operator = Objects.requireNonNull(operator, "operator must not be null");
        this.expectedValue = Objects.requireNonNull(expectedValue, "expected value must not be null");
    }

    public String getAttribute() {
        return attribute;
    }

    public ComparisonOperator getOperator() {
        return operator;
    }

    public Object getExpectedValue() {
        return expectedValue;
    }

    public boolean matches(Object actualValue) {
        if (actualValue == null)
            return false;
        if (operator == ComparisonOperator.EQUALS)
            return valuesEqual(actualValue, expectedValue);
        if (operator == ComparisonOperator.NOT_EQUALS)
            return !valuesEqual(actualValue, expectedValue);
        int comparison = compare(actualValue, expectedValue);
        return switch (operator) {
            case LESS_THAN -> comparison < 0;
            case LESS_THAN_OR_EQUAL -> comparison <= 0;
            case GREATER_THAN -> comparison > 0;
            case GREATER_THAN_OR_EQUAL -> comparison >= 0;
            default -> throw new IllegalStateException("Unhandled operator: " + operator);
        };
    }

    private static boolean valuesEqual(Object actual, Object expected) {
        if (actual instanceof Number && expected instanceof Number)
            return toBigDecimal(actual).compareTo(toBigDecimal(expected)) == 0;
        return actual.equals(expected);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static int compare(Object actual, Object expected) {
        if (actual instanceof Number && expected instanceof Number)
            return toBigDecimal(actual).compareTo(toBigDecimal(expected));
        if (actual instanceof Comparable comparable && actual.getClass().isInstance(expected))
            return comparable.compareTo(expected);
        throw new InvalidRuleException(
                "Cannot compare " + actual.getClass().getName() + " with " + expected.getClass().getName());
    }

    private static BigDecimal toBigDecimal(Object value) {
        if (value instanceof BigDecimal decimal)
            return decimal;
        if (value instanceof Byte || value instanceof Short || value instanceof Integer || value instanceof Long)
            return BigDecimal.valueOf(((Number) value).longValue());
        if (value instanceof Number number)
            return new BigDecimal(number.toString());
        throw new IllegalArgumentException("Not a number: " + value);
    }
}
