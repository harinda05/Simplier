package org.simpiler.tests;

import org.junit.Before;
import org.junit.Test;
import org.simpiler.handlers.parser.BinaryOp;
import org.simpiler.handlers.parser.Expression;
import org.simpiler.handlers.parser.Literal;
import org.simpiler.handlers.parser.Parser;
import org.simpiler.handlers.tokenizer.Tokenizer;

import static org.assertj.core.api.Assertions.assertThat;

public class ParserTests {

    Tokenizer tokenizer;
    Parser parser;

    @Before
    public void setup(){
        tokenizer = new Tokenizer();
        parser = new Parser();
    }

    @Test
    public void testParser(){
        Expression resultExp = parser.parse(tokenizer.tokenize("1 + 2"));

        Expression expectedExp = new BinaryOp(
                new Literal<>(1),
                "+",
                new Literal<>(2)
        );

        assertThat(resultExp).usingRecursiveComparison().isEqualTo(expectedExp);
    }

}