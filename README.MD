# ConstEval

This is a small Kotlin IR compiler plugin which attempts to evaluate calls to certain functions starting with `eval` at
compile-time.

## Features

The plugin tries only to evaluate calls to functions which:

- Start with `eval`
- Take and return only the types `Int`, `Boolean` and `String` ("constant types")
- Are top-level
- Have no extension or context receivers

Note that the arguments of a root `eval` call must be possible to evaluate without any context from their enclosing
scope. This restriction does not apply to nested `eval` calls.

In its evaluation it can handle:

- Built-in methods of the constant types
- `if` expressions
- `when` expressions
- `while` loops
- `val`/`var`s and corresponding assignments
- Default parameter values, including those which reference other preceding parameters
- Calls to other `eval` functions, including directly or indirectly recursive ones

## Tests

Test cases can be found [here](https://github.com/LlamaLad7/JetBrainsProject-ConstEval/tree/main/src/testData/box).
Each test case has a small program and a corresponding IR dump to verify that the evaluation is working correctly.
Running the gradle `test` task will generate the JUnit tests and run them, unless nothing has changed since last time.

There is currently no gradle plugin to use this in a real project, but one would of course be simple to make. If you
want to test custom things you can add new test cases.

## Design Decisions

The interpreter's design is fairly simple, it is just a simple IR visitor which recursively visits the function body
and produces an `EvalResult` each time, representing one of the "constant types" or `Unit`.

I decided to implement "jumping" control-flow (i.e. `return`, `break`, `continue`) using special `Throwable`s which do
not create stack-traces.

From a cleanliness perspective, I opted for this design to avoid repeated manual propagation of these signals throughout
the visitors.

From a performance perspective, the overhead of the exceptions is fairly low with stack-traces disabled. In essence the
JVM just has to do the same unwinding that we would do manually, so the performance characteristics should be similar,
but profiling is needed to confirm this.

## Considerations for the future

- It would probably be wise to have some kind of complexity heuristic, so that we do not waste time evaluating deep
  call chains at compile-time.
- We are not very stack-safe since the visitor makes lots of recursive calls for each IR element, so we may
  StackOverflow on particularly complex and deep logic. Perhaps it would be better to make some kind of linear IR
  bytecode which we then interpret, or perhaps we should avoid such deep evaluations altogether.
- Additionally, we should probably have some kind of timeout for each evaluation to avoid slow compile-times.