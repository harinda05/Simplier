package org.simpiler.handlers.parser;

import lombok.AllArgsConstructor;
import org.simpiler.handlers.tokenizer.Token;

@AllArgsConstructor
public class BinaryOp extends Expression{
    Expression left;
    String op;
    Expression right;
}
