package org.simpiler.handlers.parser;

import org.simpiler.handlers.tokenizer.Token;
import org.simpiler.handlers.tokenizer.TokenType;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


public class Parser {

    int pos = 0;
    List<Token> tokenList;

    public Expression parse(List<Token> tokenList){
        this.tokenList = tokenList;
        return parseExpression();
    }

    private Token peek(){
        if(pos < tokenList.size()){
            return tokenList.get(pos);
        } else {
            return new Token(TokenType.END, "");
        }
    }

    private Token consume(Optional<String> expected){
        Token token = peek();
        if(expected.isPresent() && !token.getText().equals(expected.get()))
            throw new RuntimeException("Expected: " + expected + "got: " + token.getText());
        pos += 1;
        return token;
    }

    private Literal parseLiteral(){
        Token token = peek();
        if(token.getType() == TokenType.INTEGER_LITERAL){
            token = consume(Optional.empty());
            return new Literal<>(Integer.valueOf(token.getText()));
        } else {
            throw new RuntimeException("Expected literal found - " + token.getText() + " at - " + pos);
        }
    }

    private Expression parseExpression(){
        String[] exp_operators = new String[] {"<"};
        Expression left = parsePolynomial();
        while(Arrays.asList(exp_operators).contains(peek().getText())){
            String op_token = consume(Optional.empty()).getText();
            Expression right = parsePolynomial();
            left = new BinaryOp(left, op_token, right);
        }
        return left;
    }

    private Expression parsePolynomial(){
        String[] exp_operators = new String[] {"+", "-"};
        Expression left = parseTerm();
        while(Arrays.asList(exp_operators).contains(peek().getText())){
            String op_token = consume(Optional.empty()).getText();
            Expression right = parseTerm();
            left = new BinaryOp(left, op_token, right);
        }
        return left;
    }

    private Expression parseTerm(){
        String[] term_operators = new String[] {"*", "/"};
        Expression left = parseFactor();
        while(Arrays.asList(term_operators).contains(peek().getText())){
            String op_token = consume(Optional.empty()).getText();
            Expression right = parseFactor();
            left = new BinaryOp(left, op_token, right);
        }
        return left;
    }

    private Expression parseFactor(){
        if(peek().getText().equals(String.valueOf('('))){
            return parseParenthesizedExpression();
        } else if(peek().getText().equals("if")){
            return parseIfStatements();
        } else if (peek().getType() == TokenType.INTEGER_LITERAL) {
            return parseLiteral();
        } else {
            throw new RuntimeException("Unexpected " + peek().getText());
        }
    }

    private Expression parseParenthesizedExpression(){
        consume(Optional.of(String.valueOf('(')));
        Expression expressionInParenthesize = parseExpression();
        consume(Optional.of(String.valueOf(')')));
        return expressionInParenthesize;
    }

    private Expression parseIfStatements() {
        consume(Optional.of("if"));
        Expression condition = parseExpression();
        consume(Optional.of("then"));
        Expression thenClause = parseExpression();
        Expression elseClause = null;

        if(peek().getText().equals("else")){
            consume(Optional.of("else"));
            elseClause = parseExpression();
        }
        return new IfExpression(condition, thenClause, elseClause);
    }

}

