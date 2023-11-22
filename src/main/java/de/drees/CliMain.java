package de.drees;

import de.drees.cli.ConsoleUtils;
import de.drees.lang.exceptions.CarlangException;
import de.drees.lang.exceptions.CarlangRuntimeException;
import de.drees.lang.interpreter.*;
import de.drees.lang.lexer.Lexer;
import de.drees.lang.lexer.TokenList;
import de.drees.lang.parser.ParseResult;
import de.drees.lang.parser.Parser;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CliMain {
    public static void main(String[] args) throws Exception {
        ConsoleUtils.getInstance().printScreenContents();

        String line = "";
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));

        while(!line.trim().equalsIgnoreCase("exit")) {
            System.out.print("\n >> ");
            line = reader.readLine();

            if(line.startsWith(":tokenize:"))
                handleTokenizerRequest(line.substring(10));
            else if (line.startsWith(":tree:")) {
                handleTreeRequest(line.substring(6));
            } else if (line.startsWith(":type-eval:")) {
                CarlangValue val = handleDefaultInput(line.substring(11));
                System.out.println(val.typeInfoString());
            } else {
                CarlangValue val = handleDefaultInput(line);
                System.out.println(val.toString());
            }
        }
    }

    public static void handleTokenizerRequest(String line) throws Exception {
        //System.out.printf("Tokenizing \"%s\"\n", line);
        Lexer lexer = new Lexer("<stdin>",line);
        TokenList tokens = lexer.tokenize();
        tokens.printTokens();
    }

    public static void handleTreeRequest(String line) throws Exception {
        System.out.println();
        Parser parser = new Parser(new Lexer("<stdin>",line));
        ParseResult result = parser.parse();
        if(result.isError()) {
            printErrorOnLine(line, result.getError());
        } else {
            System.out.println(result.getNode().getNestedRepresentation(0));
        }
    }

    public static CarlangValue handleDefaultInput(String line) throws Exception {
        Parser parser = new Parser(new Lexer("<stdin>",line));
        ParseResult result = parser.parse();
        if(result.isError()) {
            printErrorOnLine(line, result.getError());
        } else {
            CarlangContext programContext = new CarlangContext();
            CarlangInterpreter interpreter = new CarlangInterpreter();
            CarlangRuntimeResult res = interpreter.visitNode(result.getNode(), programContext);

            if(res.isError()) {
                printErrorOnLine(line, res.getError());
                return new CarlangString("Program ended prematurely");
            }

            return res.getValue();
        }
        return new CarlangString("");
    }

    public static void printErrorOnLine(String line, CarlangException error) {
        if(error instanceof CarlangRuntimeException runtimeException) {
            System.out.println(runtimeException.generateTraceBack());
        } else {
            System.out.printf("Parser ran into an error: %s , line %d%n",
                    error.getStartPos().getFileName(),
                    error.getStartPos().getLineNumber()+1);
        }

        System.out.println(line);
        System.out.println(" ".repeat(error.getStartPos().getColumnNumber()) +
                "^".repeat(error.getEndPos().getColumnNumber()));
        System.out.println(error.getMessage());
    }
}