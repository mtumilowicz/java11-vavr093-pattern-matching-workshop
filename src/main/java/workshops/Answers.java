package workshops;

import com.google.common.collect.Range;
import credit.CreditAssessmentService;
import io.vavr.CheckedRunnable;
import io.vavr.collection.Seq;
import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Try;
import person.*;
import request.BadRequest;
import tax.TaxService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static io.vavr.API.*;
import static io.vavr.Patterns.*;
import static io.vavr.Predicates.*;
import static java.util.function.Predicate.not;
import static workshops.DecompositionAnswersPatterns.$LocalDate;
import static workshops.DecompositionAnswersPatterns.$PersonByCreditAssessSubjects;

/**
 * Created by mtumilowicz on 2019-05-01.
 */
/*
TO-DO:
1. add personByType pattern
1. find and replace all ex. person3 -> person
1. renaming tests
1. better Try example (maybe with recover - take from Try workshop)
1. Workshop (with switch / case, if)
1. readme
 */
public class Answers {
    public static int numberConverter(String number) {
        return Match(number).of(
                Case($("one"), 1),
                Case($("two"), 2),
                Case($("three"), 3),
                Case($(), () -> {
                    throw new IllegalStateException("value not supported");
                }));
    }

    public static String thresholds(int input) {
        Range<Integer> threshold1 = Range.closed(0, 50);
        Range<Integer> threshold2 = Range.closed(51, 150);
        Range<Integer> threshold3 = Range.atLeast(151);

        return Match(input).of(
                Case($(threshold1::contains), "threshold1"),
                Case($(threshold2::contains), "threshold2"),
                Case($(threshold3::contains), "threshold3"),
                Case($(i -> i < 0), () -> {
                    throw new IllegalArgumentException("only positive numbers!");
                }));
    }

    public static String switchOnEnum(Person person) {
        return Match(person.getType()).of(
                Case($(PersonType.VIP), () -> StatsService.getFullStats(person)),
                Case($(PersonType.REGULAR), () -> StatsService.getStats(person)),
                Case($(PersonType.TEMPORARY), () -> StatsService.getFastStats(person)),
                Case($(), ignore -> {
                    throw new IllegalStateException("value not supported");
                }));
    }

    public static LocalDate rawDateMapper(String date) {
        return Match(date).of(
                Case($(isNull()), () -> null),
                Case($(isNotNull()), LocalDate::parse));
    }

    public static Option<LocalDate> optionDateMapper(String date) {
        return Match(date).option(
                Case($(isNotNull()), LocalDate::parse));
    }

    public static Either<String, Person> eitherDecompose(Either<BadRequest, Person> either) {
        return Match(either).of(
                Case($(isNull()), () -> Either.left("cannot be null")),
                Case($Left($()), PersonService::patch),
                Case($Right($()), PersonService::processPerson)
        );
    }

    public static Option<String> optionDecompose(int id, List<String> logfile) {
        return Match(PersonRepository.findById(id)).of(
                Case($None(), () -> {
                    logfile.add("cannot find for id = " + id);
                    return Option.none();
                }),
                Case($Some($()), value -> Option.some("processed " + id))
        );
    }

    public static Try<Integer> tryDecompose(String number) {
        Try<Integer> _try = Try.of(() -> Integer.parseInt(number));

        return Match(_try).of( // try with exceptions
                Case($Success($()), value -> Try.success(value * value)),
                Case($Failure($()), Try::failure)
        );
    }

    public static String ifSyntax(Person person2) {
        return Match(person2).of(
                Case($(isNull()), () -> "cannot be null"),
                Case($(Person::isActive), PersonService::disable),
                Case($(), PersonService::activate)
        );
    }

    public static int localDateDecompose(LocalDate date) {
        return Match(date).of(
                Case($LocalDate($(year -> year < 2015), $(), $()), TaxService::taxBefore2015),
                Case($LocalDate($(year -> year > 2015), $(), $()), TaxService::taxAfter2015)
        );
    }

    public static Integer decomposePerson3(Person person) {
        return Match(person).of(
                Case($PersonByCreditAssessSubjects($(), $()),
                        (account, address) ->
                                CreditAssessmentService.serviceMethodAssess(CreditAssessSubjects.builder()
                                        .balance(account.getBalance())
                                        .salary(account.getSalary())
                                        .country(address.getCountry())
                                        .build()
                                ))
        );
    }

    public static Either<Seq<Throwable>, Seq<Integer>> existsTest(Seq<Try<Integer>> list) {
        return Match(list).of(
                Case($(exists(Try::isFailure)),
                        tries -> Either.left(tries
                                .filter(Try::isFailure)
                                .map(Try::getCause)
                                .toList())),
                Case($(),
                        tries -> Either.right(tries
                                .filter(Try::isSuccess)
                                .map(Try::get)
                                .toList()))
        );
    }

    public static Either<Seq<Throwable>, Seq<Integer>> forAllTest(Seq<Try<Integer>> list) {
        return Match(list).of(
                Case($(forAll(Try::isSuccess)),
                        tries -> Either.right(tries
                                .filter(Try::isSuccess)
                                .map(Try::get)
                                .toList())),
                Case($(),
                        tries -> Either.left(tries
                                .filter(Try::isFailure)
                                .map(Try::getCause)
                                .toList()))
        );
    }

    public static String allOfTest(Person person) {
        return Match(person).of(
                Case($(isNull()), () -> {
                    throw new IllegalArgumentException("not null");
                }),
                Case($(allOf(Person.hasType(PersonType.VIP), Person::isActive)), "vip + active"),
                Case($(allOf(Person.hasType(PersonType.VIP), not(Person::isActive))), "vip + not active"),
                Case($(allOf(Person.hasType(PersonType.REGULAR), Person::isActive)), "regular + active"),
                Case($(allOf(Person.hasType(PersonType.REGULAR), not(Person::isActive))), "regular + not active"),
                Case($(allOf(Person.hasType(PersonType.TEMPORARY), Person::isActive)), "temporary + active"),
                Case($(allOf(Person.hasType(PersonType.TEMPORARY), not(Person::isActive))), "temporary + not active"),
                Case($(), () -> {
                    throw new IllegalArgumentException("case not supported");
                })
        );
    }

    public static String instanceOfTest(CheckedRunnable runnable) {
        try {
            runnable.run();
            return "no exception";
        } catch (Throwable exx) {
            return Match(exx).of(
                    Case($(instanceOf(IllegalArgumentException.class)), "IllegalArgumentException"),
                    Case($(instanceOf(RuntimeException.class)), "RuntimeException"),
                    Case($(instanceOf(IOException.class)), "IOException"),
                    Case($(), "handle rest")
            );
        }
    }

    public static String noneOfTest(Person person) {
        return Match(person).of(
                Case($(noneOf(Person.hasType(PersonType.VIP), Person::hasBigSalary)), "handle rest"),
                Case($(), "handle special")
        );
    }

    public static String anyOfTest(Person person) {
        return Match(person).of(
                Case($(anyOf(Person.hasType(PersonType.VIP), Person::hasBigSalary)), "handle special"),
                Case($(), "handle rest")
        );
    }
}
