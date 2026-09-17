# FormulaScript — Phase 1 Prototype

A Mini Spreadsheet Formula Compiler, implemented in Java, covering the
Phase 1 deliverables: lexical analysis, parsing, AST construction, a
symbol table, semantic analysis, Three-Address Code generation, a small
optimizer, and evaluation — plus syntax, semantic, and runtime error
demonstrations.

## Project layout

```
src/formulascript/
  TokenType.java       token kinds
  Token.java            token representation
  Lexer.java             lexical analyzer
  Parser.java            recursive-descent parser -> AST
  Node.java               AST node types (Number, Cell, BinOp)
  Assignment.java        parsed "CELL = expression" statement
  SymbolTable.java       cell -> value storage
  SemanticAnalyzer.java undefined-cell checking
  TACGenerator.java     AST -> Three-Address Code
  Optimizer.java          constant folding + algebraic simplification
  Evaluator.java          AST evaluation, division-by-zero check
  AstPrinter.java        ASCII tree printer for demos
  Exceptions.java        LexicalException, SyntaxErrorException,
                          SemanticException, RuntimeErrorException
  Main.java               runs the Phase 1 demo end to end
```

## Running it

### Command line

```
javac -encoding UTF-8 -d out src/formulascript/*.java
java -cp out formulascript.Main
```

### IntelliJ IDEA

1. Open this folder as a project (or a new project and copy `src/` in).
2. Mark `src` as Sources Root if it isn't automatically.
3. Run `formulascript.Main`.

## What the demo shows

Running `Main` reproduces every example from the Phase 1 proposal:

- `A1 = 10`, `B1 = 20`, `C1 = A1 + B1 * 5` — tokens, AST, TAC before/after
  optimization, and the evaluated result (`C1 = 110`).
- Two standalone optimizer examples: constant folding
  (`10 + 20 -> 30`) and algebraic simplification (`B1 + 0 -> B1`).
- Four error cases:
  - Syntax error on an incomplete assignment (`A1 = 10 +`).
  - Syntax error on a malformed expression (`A1 + * B2`).
  - Semantic error on an undefined cell reference (`C1 = A1 + X5`).
  - Runtime error on division by zero (`C1 = 10 / 0`).

## Extending for Phase 2 / Phase 3

The pieces are already separated so they're easy to grow independently:

- Add `SUM`, `MAX`, `MIN`, `AVG` by extending the lexer with a FUNCTION
  token and the parser's `parseFactor` with a function-call rule.
- Add more optimizations in `Optimizer.java` (e.g. common subexpression
  elimination) without touching the parser or evaluator.
- Swap `Main`'s hardcoded demo lines for a loop that reads statements
  from a file or standard input for a more general test harness.
