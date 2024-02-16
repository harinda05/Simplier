package org.simpiler.handlers.parser;

import org.simpiler.handlers.tokenizer.Token;
import org.simpiler.handlers.tokenizer.TokenType;

import java.util.Arrays;
import java.util.List;


public class Parser {

    int pos = 0;
    List<Token> tokenList;

    public Expression parse(List<Token> tokenList){
        this.tokenList = tokenList;
        return parseExpression(this.tokenList, pos);
    }

    private Token peek(){
        if(pos < tokenList.size()){
            return tokenList.get(pos);
        } else {
            return new Token(TokenType.END, "");
        }
    }

    private Token consume(){
        Token token = peek();
        pos += 1;
        return token;
    }

    private Literal parseLiteral(){
        Token token = peek();
        if(token.getType() == TokenType.INTEGER_LITERAL){
            token = consume();
            return new Literal<>(Integer.valueOf(token.getText()));
        } else {
            throw new RuntimeException("Expected literal found - " + token.getText() + " at - " + pos);
        }
    }

    private Expression parseTerm(){
        String[] term_operators = new String[] {"*", "/"};
        Expression left = parseLiteral();
        while(Arrays.asList(term_operators).contains(peek().getText())){
            String op_token = consume().getText();
            Expression right = parseLiteral();
            left = new BinaryOp(left, op_token, right);
        }
        return left;
    }

    private Expression parseExpression(List<Token> tokenList, int pos){
        String[] exp_operators = new String[] {"+", "-"};
        Expression left = parseTerm();
        while(Arrays.asList(exp_operators).contains(peek().getText())){
            String op_token = consume().getText();
            Expression right = parseTerm();
            left = new BinaryOp(left, op_token, right);
        }
        return left;
    }
}

