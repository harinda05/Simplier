package org.simpiler.handlers.parser;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Literal<T> extends Expression{
    T value;
}
