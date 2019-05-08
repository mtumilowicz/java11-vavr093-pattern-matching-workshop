# java11-vavr093-pattern-matching-workshop

https://www.baeldung.com/vavr-pattern-matching
https://www.vavr.io/vavr-docs/#_pattern_matching
https://blog.pragmatists.com/functional-programming-in-java-with-vavr-9861e704301c
https://nikitapavlenko.wordpress.com/2017/02/22/how-to-use-javaslang-pattern-matching/
http://blog.vavr.io/pattern-matching-starter/
http://blog.vavr.io/pattern-matching-essentials/
https://static.javadoc.io/io.vavr/vavr/0.9.3/io/vavr/Predicates.html

# project description

# theory in a nutshell
* easy example to set intuition
    ```
    String s = Match(i).of(
        Case($(1), "one"),
        Case($(2), "two"),
        Case($(), "?")
    );
    ```
* saves us from writing stacks of if-then-else branches
* switch-case on steroids
* is not natively supported in Java - we need third party libraries
* more human readable way
* reduces the amount of code while focusing on the relevant parts
* patterns overview
    * `$()` - wildcard pattern
        * saves us from a `MatchError` which is thrown if no case matches
    * `$(value)` - equals pattern
    * `$(predicate)` - conditional pattern
    
# conclusions in a nutshell
* as we cannot perform exhaustiveness checks (feature not supporter on the language level), 
there is possibility to return an `Option` result which prevents as from `MatchError`:
    ```
    Match(obj).option(
         Case($(predicate1()), do something));
         Case($(predicate2()), do something else));
    ```
    and in the case when there is no much for `predicate1()` as well as `predicate2()`
    we got `Option.none()` instead of `MatchError`