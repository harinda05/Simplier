package org.simpiler.handlers.parser;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class IfExpression extends Expression{
    Expression condition;
    Expression thenClause;
    Expression elseClause;
}
