package com.slyscrat.impress.service.scheduled;

public abstract class AbstractApi {
    protected boolean containsHanScript(String s) {
        return s.codePoints().anyMatch(
                codepoint ->
                        Character.UnicodeScript.of(codepoint) == Character.UnicodeScript.HAN);
    }
}
