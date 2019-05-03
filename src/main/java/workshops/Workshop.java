package workshops;

import com.google.common.base.Preconditions;
import com.google.common.collect.Range;
import credit.CreditAssessmentService;
import io.vavr.CheckedRunnable;
import io.vavr.collection.Seq;
import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.NonNull;
import out.Display;
import person.*;
import person.request.PersonRequest;
import person.request.ValidPersonRequest;
import tax.TaxService;

import java.io.IOException;
import java.time.LocalDate;

import static io.vavr.API.*;
import static io.vavr.API.$;
import static io.vavr.Patterns.*;
import static io.vavr.Patterns.$Failure;
import static io.vavr.Predicates.*;
import static io.vavr.Predicates.anyOf;
import static java.util.Objects.nonNull;
import static java.util.function.Predicate.not;
import static workshops.DecompositionAnswersPatterns.$LocalDate;
import static workshops.DecompositionAnswersPatterns.$PersonByCreditAssessSubjects;

/**
 * Created by mtumilowicz on 2019-05-03.
 */
public class Workshop {

    public static int numberConverter(String number) {
        Preconditions.checkState(nonNull(number), "value not supported");

        switch (number) { // hint: Match(number).of
            case "one": // hint: Case($("one"), 1)
                return 1;
            case "two":
                return 2;
            case "three":
                return 3;
            default:
                throw new IllegalStateException("value not supported");
        }
    }

    public static String thresholds(int input) {
        Preconditions.checkArgument(input >= 0, "only positive numbers!");
        
        Range<Integer> threshold1 = Range.closed(0, 50);
        Range<Integer> threshold2 = Range.closed(51, 150);
        Range<Integer> threshold3 = Range.atLeast(151);

        // hint: Match(input).of
        if (threshold1.contains(input)) { // hint: Case($(threshold1::contains), "threshold1")
            return "threshold1";
        }
        if (threshold2.contains(input)) {
            return "threshold2";
        }
        if (threshold3.contains(input)) {
            return "threshold3";
        }

        return "threshold3";
    }

    public static String switchOnEnum(@NonNull Person person) {
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

    public static Either<String, Person> eitherDecompose(Either<PersonRequest, ValidPersonRequest> either) {
        return Match(either).of(
                Case($(isNull()), () -> Either.left("cannot be null")),
                Case($Left($()), PersonService::patch),
                Case($Right($()), PersonService::assemblePerson)
        );
    }

    public static void optionDecompose(int id, Display display) {
        Match(PersonRepository.findById(id)).of(
                Case($None(), run(() -> display.push("cannot find person with id = " + id))),
                Case($Some($()), value -> run(() -> display.push("person: " + value + " processed")))
        );
    }

    public static void tryDecompose(String number, Display display) {
        Try<Integer> _try = Try.of(() -> Integer.parseInt(number));
        Match(_try).of(
                Case($Success($()),
                        i -> run(() -> display.push("squared number is: " + i * i))),
                Case($Failure($()),
                        ex -> run(() -> display.push("cannot square number: " + ex.getLocalizedMessage())))
        );
    }

    public static String ifSyntax(Person person) {
        return Match(person).of(
                Case($(isNull()), "cannot be null"),
                Case($(Person::isActive), PersonService::disable),
                Case($(), PersonService::activate)
        );
    }

    public static int getTaxRateFor(@NonNull LocalDate date) {
        return Match(date).of(
                Case($LocalDate($(year -> year < 2015), $(), $()), TaxService::taxBefore2015),
                Case($LocalDate($(year -> year > 2015), $(), $()), TaxService::taxAfter2015)
        );
    }

    public static Integer personDecompose(@NonNull Person person) {
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

    public static Either<Seq<Throwable>, Seq<Integer>> existsTest(@NonNull Seq<Try<Integer>> list) {
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

    public static Either<Seq<Throwable>, Seq<Integer>> forAllTest(@NonNull Seq<Try<Integer>> list) {
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

    public static String allOfTest(@NonNull Person person) {
        return Match(person).of(
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

    public static String instanceOfTest(@NonNull CheckedRunnable runnable) {
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

    public static String noneOfTest(@NonNull Person person) {
        return Match(person).of(
                Case($(noneOf(Person.hasType(PersonType.VIP), Person::hasBigSalary)), "handle rest"),
                Case($(), "handle special")
        );
    }

    public static String anyOfTest(@NonNull Person person) {
        return Match(person).of(
                Case($(anyOf(Person.hasType(PersonType.VIP), Person::hasBigSalary)), "handle special"),
                Case($(), "handle rest")
        );
    }
}
