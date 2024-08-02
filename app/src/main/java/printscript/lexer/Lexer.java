package printscript.lexer;

import printscript.Token;

import java.util.ArrayList;
import java.util.List;

public class Lexer {

    // si hay ; entonces se suma a row en 1 para contar en q fila estamos

    public List<Token> lex(String input) {
        char[] charArray = input.toCharArray();
        int charArrayLength = charArray.length;

        // Token to add to list.
        Token currentToken;

        // Token attributes:
        String identifier;
        String value;
        int row = 1;
        int column = 1;

        List<Token> tokens = new ArrayList<>();

        String currentLine;

        String currentWord = "";
        boolean wordIsString = false;
        char stringInitializer;

        for (int charIndex = 0; charIndex < charArrayLength; charIndex++) {
            char currentChar = charArray[charIndex];

            if (currentChar == ' ') {
                typeWord(wordIsString, currentWord);
            }

            currentWord += currentChar;

//            if (currentWord.equals("\"") || currentWord.equals("'")){
//
//            }




        }

//        for (String line: input.split(";")) {
//            currentLine = new String(line);
//
//            while (!currentLine.isEmpty()) {
//                char currentChar = currentLine.charAt(0);
//
//                if (currentChar == '\n') {
//                    row++;
//                    column = 1; // no tenemos anidacion todavia
//                }
//
//                if (currentChar == '"' || currentChar == '\'' ) {
//                    identifier = "String";
//                    try {
//                        column = currentLine.indexOf("", 1);
//                    }
//                    catch(Exception e) {
//                        tokens.add(new Token(identifier, currentLine, row, column));
//                        break;
//                    }
//                }
//                else {
//                    identifier = "";
//                    column = currentLine.lastIndexOf(" ", 1);
//                }
//                value = currentLine.substring(0, column);
//                tokens.add(new Token(identifier, value, row, column));
//                currentLine = currentLine.substring();
//            }
//        }
    }

    private static String typeWord(boolean wordIsString, String currentWord) {
        String identifier = "NaN";
        if (!wordIsString){
            identifier = switch (currentWord) {
                case "let" -> "variableDeclaration";
                case ";" -> "endLine";
                case ":" -> "dataTypeAssignation";
                case "=" -> "valueAssignation";
                case "string", "number" -> "symbol";
                default -> identifier;
            };
        }
        return identifier;
    }
}
