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
        String currentWord = "";
        int row = 1;
        int column = 1;

        List<Token> tokens = new ArrayList<>();

        int spaceCounter = 0;
        Character stringInitializer = null; //Guarda la comilla inicial

        for (int charIndex = 0; charIndex < charArrayLength; charIndex++) {
            char currentChar = charArray[charIndex];

            // String lol = "sa"
            // Verifica si el caracter actual es el inicio de un string y busca todos el resto (falta number)
            if (currentChar == '"' || currentChar == '\'') {
                identifier = "literalValue";
                stringInitializer = currentChar;
                currentWord += currentChar;
                for (int charIndexS = charIndex + 1; charIndexS < charArrayLength; charIndexS++) {
                    char currentCharS = charArray[charIndexS];
                    currentWord += currentCharS; //agrega hasta coincidir el char con el strInit

                    if (currentCharS == stringInitializer) {
                        // Si el caracter actual es igual que la comilla inicial, se termino el string
                        tokens.add(new Token(identifier, currentWord, row, column)); // toDo resolver tema del column
                        currentWord = "";
                        charIndex = charIndexS;
                        break;
                    }
                }
            }

            // verifica si el caracter actual es un numero
            else if (Character.isDigit(currentChar) && currentWord.isEmpty()) { //si las variables pueden tener numeros basta con chequear que este vacio el currentWord
                identifier = "literalValue";
                currentWord += currentChar;

                for (int charIndexN = charIndex + 1; charIndexN < charArrayLength; charIndexN++) {
                    char currentCharN = charArray[charIndexN];
                    // apendea el numero o el . al currentWord y si no es lo agrega como toen
                    if (Character.isDigit(currentCharN) || currentCharN == '.') {
                        currentWord += currentCharN;
                    } else {
                        tokens.add(new Token(identifier, currentWord, row, column)); // toDo resolver tema del column
                        currentWord = "";
                        charIndex = charIndexN - 1; //esto es xq al no encontrarse con un numero a diferencia del string con el "", se pierde ese char si no se le deja el -1
                        break;
                    }
                }
            }

            // Verifica si el caracter es un keyWord / nombre de la variable
//            else if (currentChar == ' ') {
//                switch (currentWord) {
//                    case "let" -> identifier = "variableDeclaration";
//                    case ";" -> identifier = "endLine";
//                    case ":" -> identifier = "dataTypeAssignation";
//                    case "=" -> identifier = "valueAssignation";
//                    case "string", "number" -> identifier = "symbol";
//                    default -> identifier = "nameVariable";
//                }
//
//                // si hay mas de 1 espcacio y esta vacio el CurrentWord no agrega nadaaa
//                if (!currentWord.isEmpty()) {
//                    tokens.add(new Token(identifier, currentWord, row, column));
//                    currentWord = "";
//                }
        //    }
            else if (currentWord.equals("let")) {
                tokens.add(new Token("variableDeclaration", currentWord, row, column - currentWord.length()));
                currentWord = "";
            }

            // verificar si es un + - * /
            else if ((currentChar == '+' || currentChar == '-' || currentChar == '*' || currentChar == '/') ) {
                column++;
                // Si la currentWord no esta vacia es porque es una nameVariable
                // (si fuera un string-number ya hubiese sido agregado)
                if (!currentWord.isEmpty()) {
                    identifier = "nameVariable";
                    tokens.add(new Token(identifier, currentWord, row, column - currentWord.length() + spaceCounter));
                    currentWord = "";
                }
                identifier = "operator";
                currentWord += currentChar;
                tokens.add(new Token(identifier, currentWord, row, column ));
                currentWord = "";
            }

            else if (currentChar == ':') {
                column++;
                // Si la currentWord no esta vacia es porque es una nameVariable
                // (si fuera un string-number ya hubiese sido agregado)
                if (!currentWord.isEmpty()) {
                    identifier = "nameVariable";
                    tokens.add(new Token(identifier, currentWord, row, column - currentWord.length() - spaceCounter));
                }

                tokens.add(new Token("dataTypeAssignation", ":", row, column));
                currentWord = "";
            }

            else if (currentChar == '=') {
                // Si la currentWord no esta vacia es porque es una nameVariable
                // (si fuera un string-number ya hubiese sido agregado)
                if (!currentWord.isEmpty()) {
                    identifier = "symbol";
                    tokens.add(new Token(identifier, currentWord, row, column - currentWord.length()));
                }
                column++;

                tokens.add(new Token("valueAssignation", "=", row, column ));
                currentWord = "";
            }

            else if (currentChar == ';') {
                column++;
                // Si la currentWord no esta vacia es porque es una nameVariable
                // (si fuera un string-number ya hubiese sido agregado)
                if (!currentWord.isEmpty()) {
                    identifier = "nameVariable";
                    tokens.add(new Token(identifier, currentWord, row, column - currentWord.length() + spaceCounter));
                }

                tokens.add(new Token("endLine", ";", row, column ));
                currentWord = "";
                spaceCounter = 0;
            }

            else if (currentChar == '\n') {
                row++;
                column = 1;
            }

            else if (currentChar == '\t') {
                column += 4;
            }

            else if (currentChar == ' ') {
                column++;
                spaceCounter++;
            }

            // agrega el caracter actual a la palabra actual si no es nada (seguir rellenando)
            else {
                currentWord += currentChar;
                column++;
            }
        }
        // Verifica si la palabra actual no esta vacia y la agrega a la lista de tokens SI no se cerro un string ocn comillas
        if (!currentWord.isEmpty()) {
            tokens.add(new Token("literalValue", currentWord, row, column));
        }

        return tokens;
    }


    public static void main(String[] args) {
        Lexer lexer = new Lexer();
        List<Token> tokens = lexer.lex("let a : number = 12;\nlet b: number = 4;\nlet c: number = a / b;");
        for (Token token : tokens) {
            System.out.println(token);
        }

    }
}
// TODO (solo para resaltar) la columna es ANTES del char