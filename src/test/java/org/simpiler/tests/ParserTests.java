package org.simpiler.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.simpiler.handlers.parser.*;
import org.simpiler.handlers.tokenizer.Tokenizer;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ParserTests {

    Tokenizer tokenizer;
    Parser parser;

    @BeforeEach
    public void setup() {
        tokenizer = new Tokenizer();
        parser = new Parser();
    }

    @Test
    public void testParser() {
        Expression resultExp = parser.parse(tokenizer.tokenize("1 + 2"));

        Expression expectedExp = new BinaryOp(
                new Literal<>(1),
                "+",
                new Literal<>(2)
        );

        assertThat(resultExp).usingRecursiveComparison().isEqualTo(expectedExp);
    }

    @Test
    public void testParserMultiple() {
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
    public void testParserMultiple2() {
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
    public void testParserTestMultiplication() {
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
    public void testParserTestMultiplication2() {
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

    @Test
    public void testParserSingleLiteral() {
        Expression resultExp = parser.parse(tokenizer.tokenize("1"));
        Expression expectedExp = new Literal<>(1);
        assertThat(resultExp).usingRecursiveComparison().isEqualTo(expectedExp);
    }

    @Test
    public void testParserParenthesizedExpressionOnRight() {
        Expression resultExp = parser.parse(tokenizer.tokenize("1 * (2 + 3)"));

        Expression left = new Literal<>(1);
        BinaryOp right = new BinaryOp(
                new Literal<>(2),
                "+",
                new Literal<>(3)
        );

        Expression expectedExp = new BinaryOp(
                left,
                "*",
                right
        );

        assertThat(resultExp).usingRecursiveComparison().isEqualTo(expectedExp);
    }

    @Test
    public void testParserParenthesizedExpressionOnLeft() {
        Expression resultExp = parser.parse(tokenizer.tokenize("(2 + 3) * 4"));

        BinaryOp left = new BinaryOp(
                new Literal<>(2),
                "+",
                new Literal<>(3)
        );
        Expression right = new Literal<>(4);

        Expression expectedExp = new BinaryOp(
                left,
                "*",
                right
        );

        assertThat(resultExp).usingRecursiveComparison().isEqualTo(expectedExp);
    }

    @Test
    public void testParserParenthesizedExpressionComplex() {
        Expression resultExp = parser.parse(tokenizer.tokenize("1 * (2 + 3) / 4"));

        BinaryOp left = new BinaryOp(
                new Literal<>(1),
                "*",
                new BinaryOp(
                        new Literal<>(2),
                        "+",
                        new Literal<>(3)
                    )
        );
        Expression right = new Literal<>(4);

        Expression expectedExp = new BinaryOp(
                left,
                "/",
                right
        );

        assertThat(resultExp).usingRecursiveComparison().isEqualTo(expectedExp);
    }

    @Test
    public void testParserBooleanOperators() {
        Expression resultExp = parser.parse(tokenizer.tokenize("1 < 2 + 3"));

        Expression left = new Literal<>(1);
        BinaryOp right = new BinaryOp(
                new Literal<>(2),
                "+",
                new Literal<>(3)
        );

        Expression expectedExp = new BinaryOp(
                left,
                "<",
                right
        );

        assertThat(resultExp).usingRecursiveComparison().isEqualTo(expectedExp);
    }

    // 1 * if ... then 2 else 3

    @Test
    public void testParserConditional() {
        Expression resultExp = parser.parse(tokenizer.tokenize("if 1 then 2"));
        Expression expectedExp = new IfExpression(
                new Literal<>(1),
                new Literal<>(2),
                null
        );
        assertThat(resultExp).usingRecursiveComparison().isEqualTo(expectedExp);
    }

    @Test
    public void testParserConditionalWithElse() {
        Expression resultExp = parser.parse(tokenizer.tokenize("if 1 then 2 else 3"));
        Expression expectedExp = new IfExpression(
                new Literal<>(1),
                new Literal<>(2),
                new Literal<>(3)
        );
        assertThat(resultExp).usingRecursiveComparison().isEqualTo(expectedExp);
    }

    @Test
    public void testParserConditionalComplex() {
        Expression resultExp = parser.parse(tokenizer.tokenize("if 1 + 2 then 2 * 3 else 3 / 4"));

        BinaryOp condition = new BinaryOp(
                new Literal<>(1),
                "+",
                new Literal<>(2));

        BinaryOp thenClause = new BinaryOp(
                new Literal<>(2),
                "*",
                new Literal<>(3));

        BinaryOp elseClause = new BinaryOp(
                new Literal<>(3),
                "/",
                new Literal<>(4));

        Expression expectedExp = new IfExpression(
                condition,
                thenClause,
                elseClause
        );
        assertThat(resultExp).usingRecursiveComparison().isEqualTo(expectedExp);
    }

    @Test
    public void testParserConditionalPartOfExpression() {
        Expression resultExp = parser.parse(tokenizer.tokenize("0 + if 1 then 2 else 3"));
        Expression ifCondition = new IfExpression(
                new Literal<>(1),
                new Literal<>(2),
                new Literal<>(3)
        );

        BinaryOp expectedExp = new BinaryOp(
                new Literal<>(0),
                "+",
                ifCondition
        );

        assertThat(resultExp).usingRecursiveComparison().isEqualTo(expectedExp);
    }
}
