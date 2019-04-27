package xxx;

import com.google.common.collect.Range;
import com.google.common.math.IntMath;
import io.vavr.collection.List;
import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Try;

import java.time.LocalDate;
import java.util.ArrayList;

import static io.vavr.API.*;
import static io.vavr.Patterns.*;
import static io.vavr.Predicates.isNotNull;
import static io.vavr.Predicates.isNull;
import static xxx.DemoPatterns.*;

/**
 * Created by mtumilowicz on 2019-04-27.
 */
public class X {
    static String ads(int input) {
        return Match(input).of(
                Case($(1), "one"),
                Case($(2), "two"),
                Case($(3), "three"),
                Case($(), "?"));
    }

    static String ads2(int input) {
        Range<Integer> threshold1 = Range.closed(0, 50);
        Range<Integer> threshold2 = Range.closed(51, 150);
        Range<Integer> threshold3 = Range.atLeast(151);

        return Match(input).of(
                Case($(threshold1::contains), "one"),
                Case($(threshold2::contains), "two"),
                Case($(threshold3::contains), "three"),
                Case($(), "?"));
    }

    static void ads3(Person person) {
        switch (person.type) {
            case VIP:
                System.out.println("VIP"); // getfullstatistics
                break;
            case ORDINARY:
                System.out.println("ORDINARY");
                break;
            case TEMPORARY:
                System.out.println("TEMPORARY"); // getfaststatistics
                break;
            default:
                throw new IllegalStateException("value not supported");
        }
    }

    static void ads33(Person person) {
        Match(person.type).of(
                Case($(Type.VIP), type -> run(() -> System.out.println("VIP"))),
                Case($(Type.ORDINARY), type -> run(() -> System.out.println("ORDINARY"))),
                Case($(Type.TEMPORARY), type -> run(() -> System.out.println("TEMPORARY"))),
                Case($(), ignore -> {
                    throw new IllegalStateException("value not supported");
                }));
    }

    static LocalDate dateMapper(String date) {
        return Match(date).of(
                Case($(isNull()), () -> null),
                Case($(isNotNull()), it -> LocalDate.parse(it)));
    }

    static Option<LocalDate> dateMapper2(String date) {
        return Match(date).of(
                Case($(isNull()), Option.none()),
                Case($(isNotNull()), it -> Option.some(LocalDate.parse(it))));
    }

    static Option<LocalDate> dateMapper3(String date) {
        return Match(date).option( // Because we can’t perform exhaustiveness checks like the Scala compiler, we provide the possibility to return an optional result
                Case($(isNotNull()), it -> LocalDate.parse(it)));
    }

    static void eitherDecompose(Either<BadRequest, Person> either) {
        Match(either).of(
                Case($Left($()), badRequest -> patch(badRequest)),
                Case($Right($()), person -> processPerson(person))
        );

        Try<Integer> _try = Try.success(1);
        Match(_try).of(
                Case($Success($()), value -> value),
                Case($Failure($()), x -> x)
        );
    }


    static void optionDecompose(Option<Integer> option) {
        ArrayList<String> logfile = new ArrayList<>();
        option.onEmpty(() -> logfile.add("empty"))
                .map(value -> IntMath.pow(value, 2))
                .getOrElse(0);

        Integer i = Match(option).of(
                Case($None(), () -> {
                    logfile.add("empty");
                    return 0;
                }),
                Case($Some($()), value -> IntMath.pow(value, 2))
        );
    }

    static void ifSyntax() {
        boolean a = true;

        Match(a).of(
                Case($(true), () -> run(X::doIfTrue)),
                Case($(false), () -> run(X::doIfNot))
        );

        Person2 person = Match(new Person2()).of(
                Case($(p -> p.active), p -> X.doIfTrue2(p)),
                Case($(p -> p.active), p -> X.doIfNot2(p))
        );
    }

    static void doIfTrue() {

    }

    static void doIfNot() {

    }

    static Person2 doIfTrue2(Person2 person2) {
        return new Person2();
    }

    static Person2 doIfNot2(Person2 person2) {
        return new Person2();
    }

    static void listDecomposition() {
        List x = List.of(1, 2, 3, 4, 5);
        
        Match(x).of(
                Case($Cons($(), $()), (a, tail) -> run(() -> System.out.println(a + " " + tail)))
        );
    }
    
    static void localDateDecompose() {
        LocalDate date = LocalDate.of(2017, 2, 13);

        String result = Match(date).of(
                Case($LocalDate($(2016), $(3), $(13)),
                        () -> "2016-02-13"),
                Case($LocalDate($(2016), $(), $()),
                        (y, m, d) -> "month " + m + " in 2016"),
                Case($LocalDate($(), $(), $()),
                        (y, m, d) -> "month " + m + " in " + y),
                Case($(),
                        () -> "(catch all)")
        );

        System.out.println(result);
    }

    static Either<String, Person> patch(BadRequest badRequest) {
        return Either.right(new Person());
    }

    static Either<String, Person> processPerson(Person person) {
        return Either.right(new Person());
    }

    public static void main(String[] args) {
        System.out.println(LocalDate.parse("2014-10-12"));
        System.out.println(dateMapper("2014-10-12"));
        System.out.println(dateMapper(null));
        listDecomposition();
        localDateDecompose();
    }
}

class Person {
    Type type;
}

class Person2 {
    boolean active;
}

enum Type {
    ORDINARY, VIP, TEMPORARY
}

class BadRequest {
    Request request;
    String message;
}

class Request {

}

/*
TO-DO predicates

allOf
anyOf
exists
forAll
instanceOf
is
isIn
noneOf

http://blog.vavr.io/pattern-matching-starter/ - objects decomposition
    LocalDate - $(x -> x < 2015) $(x -> x >= 2015)
    Person(Account, Address), deconstructor: (balance, country, salary) -> assess(creditAssessSubjects(balance, country, salary))
    
    List.of(assess(balance), assess(country), assess(salary)).sum()
 */