package org.simpiler.tests;


import org.junit.Before;
import org.junit.Test;
import org.simpiler.handlers.tokenizer.Token;
import org.simpiler.handlers.tokenizer.TokenType;
import org.simpiler.handlers.tokenizer.Tokenizer;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TokenizerTests {

    Tokenizer tokenizer;

    @Before
    public void setup(){
        tokenizer = new Tokenizer();
    }

    @Test
    public void testTokenizerIdentifier(){
        List<Token> resultList = tokenizer.tokenize("Identifier");
        assertThat(resultList.get(0)).usingRecursiveComparison().isEqualTo(new Token(
                TokenType.IDENTIFIER_LITERAL,
                "Identifier"
        ));
    }

    @Test
    public void testTokenizerIdentifierWithNumber(){
        List<Token> resultList = tokenizer.tokenize("Identifier01");

        List<Token> expectedTokenList = new ArrayList<>();
        expectedTokenList.add(new Token(
                TokenType.IDENTIFIER_LITERAL,
                "Identifier01"
        ));
        assertThat(resultList).usingRecursiveComparison().isEqualTo(expectedTokenList);

    }
    @Test
    public void testTokenizerStringWithWhitespace(){
        List<Token> resultList = tokenizer.tokenize("identifier1 identifier2");

        List<Token> expectedTokenList = new ArrayList<>();
        expectedTokenList.add(new Token(
                TokenType.IDENTIFIER_LITERAL,
                "identifier1"
        ));
        expectedTokenList.add(new Token(
                TokenType.IDENTIFIER_LITERAL,
                "identifier2"
        ));
        assertThat(resultList).usingRecursiveComparison().isEqualTo(expectedTokenList);
    }

    @Test
    public void testTokenizerStringWithMultipleTypesOfTokens(){
        List<Token> resultList = tokenizer.tokenize("A + B");

        List<Token> expectedTokenList = new ArrayList<>();
        expectedTokenList.add(new Token(
                TokenType.IDENTIFIER_LITERAL,
                "A"
        ));
        expectedTokenList.add(new Token(
                TokenType.OPERATOR_LITERAL,
                "+"
        ));
        expectedTokenList.add(new Token(
                TokenType.IDENTIFIER_LITERAL,
                "B"
        ));
        assertThat(resultList).usingRecursiveComparison().isEqualTo(expectedTokenList);
    }

}
