package de.drees.lang.interpreter;

import lombok.Getter;

import java.util.HashMap;

@Getter
public class SymbolTable {
    HashMap<String, CarlangValue> symbols;
    SymbolTable parentScope;

    public SymbolTable() {
        symbols = new HashMap<>();
        parentScope = null;
    }

    public CarlangValue getSymbolValue(String name) {
        CarlangValue value = symbols.get(name);
        if (value == null && parentScope != null) {
            return parentScope.getSymbolValue(name);
        }

        return value;
    }

    public void setSymbol(String name, CarlangValue value) {
        this.symbols.put(name, value);
    }

    public void removeSymbol(String name) {
        this.symbols.remove(name);
    }
}
