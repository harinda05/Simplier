package org.simpiler.handlers.tokenizer;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {
    public List<Token> tokenize(String src) {

        Pattern whitespace_re = Pattern.compile("\\s+");
        Pattern integer_re = Pattern.compile("[0-9]+");
        Pattern identifier_re = Pattern.compile("[a-zA-Z_][a-zA-Z0-9_]*");
        Pattern operator_re = Pattern.compile("[+-]");
        Pattern paren_re = Pattern.compile("[(){}]");

        int position = 0;
        final int START_POSITION = 0;

        List<Token> result = new ArrayList<>();

        while (position < src.length()) {

            // matches whitespaces and skips
            Matcher matcher = whitespace_re.matcher(src.substring(position));
            matcher.region(START_POSITION, src.substring(position).length());

            if (matcher.lookingAt()) {
                position += matcher.start() + matcher.group().length();
                continue;
            }

            matcher = integer_re.matcher(src.substring(position));
            matcher.region(START_POSITION, src.substring(position).length());

            // matches integers and add to result list
            if (matcher.lookingAt()) {
                result.add(new Token(
                        TokenType.INTEGER_LITERAL,
                        matcher.group()));
                position += matcher.end();
                continue;
            }

            matcher = identifier_re.matcher(src.substring(position));
            matcher.region(START_POSITION, src.substring(position).length());

            if (matcher.lookingAt()) {
                result.add(new Token(
                        TokenType.IDENTIFIER_LITERAL,
                        matcher.group()));
                position += matcher.end();
                continue;
            }

            matcher = operator_re.matcher(src.substring(position));
            matcher.region(START_POSITION, src.substring(position).length());

            if (matcher.lookingAt()) {
                result.add(new Token(
                        TokenType.OPERATOR_LITERAL,
                        matcher.group()));
                position += matcher.end();
                continue;
            }

            matcher = paren_re.matcher(src.substring(position));
            matcher.region(START_POSITION, src.substring(position).length());

            if (matcher.lookingAt()) {
                result.add(new Token(
                        TokenType.PARANTHESIS_LITERAL,
                        matcher.group()));
                position += matcher.end();
                continue;

            }
        }

        return result;
    }
}
