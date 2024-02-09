package org.simpiler.handlers.tokenizer;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
public class Token {
    private TokenType type;
    private String text;
}
