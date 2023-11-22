package de.drees.lang.interpreter;

import de.drees.lang.exceptions.CarlangException;
import de.drees.lang.exceptions.CarlangRuntimeException;
import lombok.Getter;

@Getter
public class CarlangRuntimeResult {
    CarlangValue value;
    CarlangRuntimeException error;

    public CarlangRuntimeResult() {}


    public CarlangValue register(CarlangRuntimeResult result) {
        if(result.isError()) this.error = result.getError();
        return result.value;
    }

    public CarlangRuntimeResult success(CarlangValue value) {
        this.value = value;
        return this;
    }

    public CarlangRuntimeResult failure(CarlangRuntimeException error) {
        this.error = error;
        return this;
    }

    public boolean isError() {
        return this.error != null;
    }

}
