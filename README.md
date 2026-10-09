# FormulaScript - Phase 2

A mini spreadsheet formula compiler in Java: lexer, recursive-descent parser,
AST, symbol table, semantic analysis, Three-Address Code, optimizer, evaluator.

## Build

    javac -encoding UTF-8 -d out src/formulascript/*.java

## Run

    java -cp out formulascript.Main                      built-in demo
    java -cp out formulascript.Main examples/sample.txt  run formulas from a file
    java -cp out formulascript.Main --repl               type formulas interactively
    java -cp out formulascript.Main --test               run the test suite (13 cases)

In IntelliJ, put the argument (for example --test or examples/sample.txt) under
Run > Edit Configurations > Program arguments. Lines starting with # in an
input file are comments.

## Changes since Phase 1

- New: Interpreter.java (one pipeline entry point), TestRunner.java (13 tests)
- New: examples/sample.txt, examples/errors.txt
- Parser.java: unary minus, and "end of formula" in error messages
- Optimizer.java: a constant division by zero is no longer folded away
- Main.java: file, REPL, and test modes added; the demo is still the default
