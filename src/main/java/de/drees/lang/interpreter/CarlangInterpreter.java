package de.drees.lang.interpreter;

import de.drees.lang.exceptions.CarlangRuntimeException;
import de.drees.lang.lexer.TokenType;
import de.drees.lang.parser.nodes.*;

public class CarlangInterpreter {

    public CarlangRuntimeResult visitNode(ISyntaxNode node, CarlangContext context) throws Exception {
        if (node instanceof NumberNode) {
            return visitNumberNode((NumberNode) node, context);
        } else if (node instanceof BinaryOperationNode) {
            return visitBinaryOperationNode((BinaryOperationNode) node, context);
        } else if (node instanceof UnaryOperationNode) {
            return visitUnaryOperationNode((UnaryOperationNode) node, context);
        } else if (node instanceof VariableAssignmentNode) {
            return visitVariableAssignmentNode((VariableAssignmentNode) node, context);
        } else if (node instanceof VariableAccessNode) {
            return visitVariableAccessNode((VariableAccessNode) node, context);
        } else {
            throw new Exception("Unsupported Node, Interpreter ran into an error with node '%s'"
                    .formatted(node.getStringRepresentation()));
        }
    }
    private CarlangRuntimeResult visitNumberNode(NumberNode numberNode, CarlangContext context) {
        return new CarlangRuntimeResult().success(
                new CarlangNumber((Number) numberNode.getToken().getPayload())
                        .setContext(context)
                        .setPositions(numberNode.getStartPosition(), numberNode.getEndPosition())


        );
    }

    private CarlangRuntimeResult visitBinaryOperationNode(BinaryOperationNode binaryOperationNode, CarlangContext context) throws Exception {
        CarlangRuntimeResult result = new CarlangRuntimeResult();

        //FOR NOW: ASSUME ITS JUST NUMBERS!!!
        //CarlangValue left = visitNode(binaryOperationNode.getLeftNode());
        //CarlangValue right = visitNode(binaryOperationNode.getRightNode());

        CarlangNumber left = (CarlangNumber) result.register(visitNode(binaryOperationNode.getLeftNode(), context));
        if(result.isError()) return result;
        CarlangNumber right = (CarlangNumber) result.register(visitNode(binaryOperationNode.getRightNode(), context));
        if(result.isError()) return result;

        CarlangRuntimeResult operationResult = switch(binaryOperationNode.getOperator().getType()) {
            case PLUS -> left.addedTo(right);
            case MINUS -> left.subtractBy(right);
            case MULTIPLY -> left.multiplyBy(right);
            case DIVIDE -> left.divideBy(right);
            case MODULO -> left.modulo(right);
            case POWER -> left.powerBy(right);

            default -> new CarlangRuntimeResult().failure(new CarlangRuntimeException(
                            binaryOperationNode.getStartPosition(), binaryOperationNode.getEndPosition(),
                            "Interpreter failed to handle operation %s"
                                    .formatted(binaryOperationNode.getOperator().getType().label)
                    ));
        };

        CarlangValue value = result.register(operationResult);
        if(result.isError()) {
            result.getError().setStartPos(binaryOperationNode.getStartPosition());
            result.getError().setEndPos(binaryOperationNode.getEndPosition());
            return result;
        }
        return result.success(value
                .setPositions(binaryOperationNode.getStartPosition(), binaryOperationNode.getEndPosition()));
    }

    private CarlangRuntimeResult visitUnaryOperationNode(UnaryOperationNode unaryOperationNode, CarlangContext context) throws Exception {
        CarlangRuntimeResult result = new CarlangRuntimeResult();
        CarlangNumber left = (CarlangNumber) result.register(visitNode(unaryOperationNode.getNode(), context));

        if (unaryOperationNode.getOperator().getType() != TokenType.MINUS) {
            return result.failure(new CarlangRuntimeException(
                            unaryOperationNode.getStartPosition(), unaryOperationNode.getEndPosition(),
                            "Interpreter failed to handle unary operation %s"
                                    .formatted(unaryOperationNode.getOperator().getType().label)
                    ));
        }

        CarlangValue value = result.register(left.multiplyBy(new CarlangNumber(-1)));

        if(result.isError()) {
            result.getError().setStartPos(unaryOperationNode.getStartPosition());
            result.getError().setEndPos(unaryOperationNode.getEndPosition());
            return result;
        }

        return result.success(value.setPositions(unaryOperationNode.getStartPosition(), unaryOperationNode.getEndPosition()));
    }

    private CarlangRuntimeResult visitVariableAssignmentNode(VariableAssignmentNode variableAssignmentNode, CarlangContext context) throws Exception {
        CarlangRuntimeResult result = new CarlangRuntimeResult();
        String varName = variableAssignmentNode.getVariableNameString();
        CarlangValue expressionResult = result.register(visitNode(variableAssignmentNode.getValueNode(), context));

        if(result.isError()) return result;

        context.symbolTable.setSymbol(varName, expressionResult);
        return result.success(expressionResult);
    }

    private CarlangRuntimeResult visitVariableAccessNode(VariableAccessNode variableAccessNode, CarlangContext context) {
        CarlangRuntimeResult result = new CarlangRuntimeResult();
        String varName = variableAccessNode.getVariableNameString();
        CarlangValue value = context.getSymbolTable().getSymbolValue(varName);

        if (value == null) {
            return result.failure(new CarlangRuntimeException(
                    variableAccessNode.getStartPosition(), variableAccessNode.getEndPosition(),
                    "Tried to access variable %s, but it has not been defined"
                            .formatted(varName)
            ));
        }

        value.setPositions(variableAccessNode.getStartPosition(), variableAccessNode.getEndPosition());
        return result.success(value);
    }
}
