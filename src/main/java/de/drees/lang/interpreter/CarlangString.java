package de.drees.lang.interpreter;
import de.drees.lang.lexer.LexerPosition;
import lombok.Getter;


@Getter
public class CarlangString implements CarlangValue {
    private LexerPosition startPosition, endPosition;
    String str;
    public CarlangString(String string) {
        this.str = string;
    }

    public CarlangValue setPositions(LexerPosition startPosition, LexerPosition endPosition) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        return this;
    }

    @Override
    public String toString() {
        return str;
    }

    @Override
    public String typeInfoString() {
        return "(%s, %s)".formatted(str, "STRING");
    }
}
