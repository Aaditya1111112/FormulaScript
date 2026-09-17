package formulascript;

class LexicalException extends RuntimeException {
    LexicalException(String message) { super(message); }
}

class SyntaxErrorException extends RuntimeException {
    SyntaxErrorException(String message) { super(message); }
}

class SemanticException extends RuntimeException {
    SemanticException(String message) { super(message); }
}

class RuntimeErrorException extends RuntimeException {
    RuntimeErrorException(String message) { super(message); }
}
