package org.simpiler.handlers.tokenizer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Token {
    private TokenType type;
    private String text;
}
