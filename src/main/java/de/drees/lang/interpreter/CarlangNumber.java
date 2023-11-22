package de.drees.lang.interpreter;

import de.drees.lang.exceptions.CarlangRuntimeException;
import de.drees.lang.lexer.LexerPosition;
import de.drees.lang.parser.nodes.NumberNode;
import lombok.Getter;

@Getter
public class CarlangNumber implements CarlangValue{
    private LexerPosition startPosition, endPosition;
    private final Number value;
    private CarlangContext context;

    public CarlangNumber(Number value) {
        this.value = value;
    }

    public CarlangNumber setPositions(LexerPosition startPosition, LexerPosition endPosition) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        return this;
    }


    public CarlangRuntimeResult addedTo(CarlangValue other) {
        CarlangRuntimeResult rtResult = new CarlangRuntimeResult();

        if (other instanceof CarlangNumber otherNumber) {

            if (this.value instanceof Integer && otherNumber.value instanceof Integer) {
                int result = this.value.intValue() + otherNumber.value.intValue();
                return rtResult.success(new CarlangNumber(result));
            } else {
                double thisDoubleValue = this.value.doubleValue();
                double otherDoubleValue = otherNumber.value.doubleValue();
                double result = thisDoubleValue + otherDoubleValue;

                if (result == (int) result) {
                    return rtResult.success(new CarlangNumber((int) result).setContext(this.context));
                } else {
                    return rtResult.success(new CarlangNumber(result).setContext(this.context));
                }
            }
        }

        return rtResult.failure(new CarlangRuntimeException(
                LexerPosition.empty(), LexerPosition.empty(),
                "Interpreter failed to handle addition operation on (%s and %s)".formatted(value, other)));
    }

    public CarlangRuntimeResult subtractBy(CarlangValue other) {
        CarlangRuntimeResult rtResult = new CarlangRuntimeResult();

        if (other instanceof CarlangNumber otherNumber) {

            if (this.value instanceof Integer && otherNumber.value instanceof Integer) {
                int result = this.value.intValue() - otherNumber.value.intValue();
                return rtResult.success(new CarlangNumber(result).setContext(this.context));
            } else {
                double thisDoubleValue = this.value.doubleValue();
                double otherDoubleValue = otherNumber.value.doubleValue();
                double result = thisDoubleValue - otherDoubleValue;

                if (result == (int) result) {
                    return rtResult.success(new CarlangNumber((int) result).setContext(this.context));
                } else {
                    return rtResult.success(new CarlangNumber(result).setContext(this.context));
                }
            }
        }

        return rtResult.failure(new CarlangRuntimeException(
                LexerPosition.empty(), LexerPosition.empty(),
                "Interpreter failed to handle subtraction operation on (%s and %s)".formatted(value, other)));
    }

    public CarlangRuntimeResult multiplyBy(CarlangValue other) {
        CarlangRuntimeResult rtResult = new CarlangRuntimeResult();

        if (other instanceof CarlangNumber otherNumber) {

            if (this.value instanceof Integer && otherNumber.value instanceof Integer) {
                int result = this.value.intValue() * otherNumber.value.intValue();
                return rtResult.success(new CarlangNumber(result).setContext(this.context));
            } else {
                double thisDoubleValue = this.value.doubleValue();
                double otherDoubleValue = otherNumber.value.doubleValue();
                double result = thisDoubleValue * otherDoubleValue;

                if (result == (int) result) {
                    return rtResult.success(new CarlangNumber((int) result).setContext(this.context));
                } else {
                    return rtResult.success(new CarlangNumber(result).setContext(this.context));
                }
            }
        }

        return rtResult.failure(new CarlangRuntimeException(
                LexerPosition.empty(), LexerPosition.empty(),
                "Interpreter failed to handle multiplication operation on (%s and %s)".formatted(value, other)));
    }

    public CarlangRuntimeResult divideBy(CarlangValue other) {
        CarlangRuntimeResult rtResult = new CarlangRuntimeResult();

        if(((CarlangNumber) other).value.doubleValue() == 0) {
            return rtResult.failure(new CarlangRuntimeException(
                    LexerPosition.empty(), LexerPosition.empty(),
                    "Interpreter caught devision by 0 Error").setErrorContext(context));
        }

        CarlangNumber otherNumber = (CarlangNumber) other;

        double thisDoubleValue = this.value.doubleValue();
        double otherDoubleValue = otherNumber.value.doubleValue();
        double result = thisDoubleValue / otherDoubleValue;

        if (result == (int) result) {
            return rtResult.success(new CarlangNumber((int) result).setContext(this.context));
        } else {
            return rtResult.success(new CarlangNumber(result).setContext(this.context));
        }

    }

    public CarlangRuntimeResult modulo(CarlangValue other) {
        CarlangRuntimeResult rtResult = new CarlangRuntimeResult();

        if (other instanceof CarlangNumber otherNumber) {

            if (this.value instanceof Integer && otherNumber.value instanceof Integer) {
                int result = this.value.intValue() % otherNumber.value.intValue();
                return rtResult.success(new CarlangNumber(result).setContext(this.context));
            } else {
                double thisDoubleValue = this.value.doubleValue();
                double otherDoubleValue = otherNumber.value.doubleValue();
                double result = thisDoubleValue % otherDoubleValue;

                if (result == (int) result) {
                    return rtResult.success(new CarlangNumber((int) result).setContext(this.context));
                } else {
                    return rtResult.success(new CarlangNumber(result).setContext(this.context));
                }
            }
        }
        return rtResult.failure(new CarlangRuntimeException(
                LexerPosition.empty(), LexerPosition.empty(),
                "Interpreter failed to handle modulo operation on (%s and %s)".formatted(value, other)));
    }

    public CarlangRuntimeResult powerBy(CarlangValue other) {
        CarlangRuntimeResult rtResult = new CarlangRuntimeResult();

        if (other instanceof CarlangNumber otherNumber) {
            double thisDoubleValue = this.value.doubleValue();
            double otherDoubleValue = otherNumber.value.doubleValue();
            double result = Math.pow(thisDoubleValue, otherDoubleValue);

            if (result == (int) result) {
                return rtResult.success(new CarlangNumber((int) result).setContext(this.context));
            } else {
                return rtResult.success(new CarlangNumber(result).setContext(this.context));
            }
        }
        return rtResult.failure(new CarlangRuntimeException(
                LexerPosition.empty(), LexerPosition.empty(),
                "Interpreter failed to handle power operation on (%s and %s)".formatted(value, other)));
    }

    @Override
    public String toString() {
        return this.value.toString();
    }

    public String typeInfoString() {
        String type = "NaN";
        if(value instanceof Integer) {
            type = "INTEGER";
        } else if(value instanceof Double) {
            type = "DOUBLE";
        } else if(value instanceof Float) {
            type = "FLOAT";
        }

        return "(%s: %s)".formatted(this.toString(), type);
    }

    public CarlangNumber setContext(CarlangContext context) {
        this.context = context;
        return this;
    }
}
