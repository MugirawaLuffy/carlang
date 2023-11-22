package de.drees.lang.lexer;

public enum Keyword {
    DECLARE ("DECLARE"),

    ;
    public final String label;
    private Keyword(String label) {
        this.label = label;
    }

    static boolean identifierIsKeyword(String identifier) {
        for (Keyword keyword : Keyword.values()) {
            if (keyword.label.equals(identifier)) {
                return true;
            }
        }
        return false;
    }
}
