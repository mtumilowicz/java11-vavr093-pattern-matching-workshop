import com.google.common.collect.Range;
import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Try;

import java.time.LocalDate;

import static io.vavr.API.*;
import static io.vavr.Patterns.*;
import static io.vavr.Predicates.isNotNull;
import static io.vavr.Predicates.isNull;

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
    }
}

class Person {
    Type type;
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