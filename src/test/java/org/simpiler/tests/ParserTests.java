package org.simpiler.tests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.simpiler.handlers.parser.BinaryOp;
import org.simpiler.handlers.parser.Expression;
import org.simpiler.handlers.parser.Literal;
import org.simpiler.handlers.parser.Parser;
import org.simpiler.handlers.tokenizer.Tokenizer;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ParserTests {

    Tokenizer tokenizer;
    Parser parser;

    @BeforeEach
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

    @Test
    public void testParserMultiple(){
        Expression resultExp = parser.parse(tokenizer.tokenize("1 + 2 + 3"));

        BinaryOp left = new BinaryOp(new Literal<>(1),
                "+",
                new Literal<>(2));

        Expression expectedExp = new BinaryOp(
                left,
                "+",
                new Literal<>(3)
        );

        assertThat(resultExp).usingRecursiveComparison().isEqualTo(expectedExp);
    }

    @Test
    public void testParserMultiple2(){
        Expression resultExp = parser.parse(tokenizer.tokenize("1 + 2 - 5"));

        BinaryOp left = new BinaryOp(new Literal<>(1),
                "+",
                new Literal<>(2));

        Expression expectedExp = new BinaryOp(
                left,
                "-",
                new Literal<>(5)
        );

        assertThat(resultExp).usingRecursiveComparison().isEqualTo(expectedExp);
    }

    // 1 + (2 * 5)
    @Test
    public void testParserTestMultiplication(){
        Expression resultExp = parser.parse(tokenizer.tokenize("1 + 2 * 5"));

        BinaryOp right = new BinaryOp(new Literal<>(2),
                "*",
                new Literal<>(5));

        Expression expectedExp = new BinaryOp(
                new Literal<>(1),
                "+",
                right
        );

        assertThat(resultExp).usingRecursiveComparison().isEqualTo(expectedExp);
    }

    @Test
    public void testParserTestMultiplication2(){
        Expression resultExp = parser.parse(tokenizer.tokenize("1 * 5 + 2 * 5"));

        BinaryOp left = new BinaryOp(new Literal<>(1),
                "*",
                new Literal<>(5));

        BinaryOp right = new BinaryOp(new Literal<>(2),
                "*",
                new Literal<>(5));

        Expression expectedExp = new BinaryOp(
                left,
                "+",
                right
        );
        assertThat(resultExp).usingRecursiveComparison().isEqualTo(expectedExp);
    }

}
