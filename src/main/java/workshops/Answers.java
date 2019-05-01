package workshops;

import com.google.common.collect.Range;
import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Try;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static io.vavr.API.*;
import static io.vavr.Patterns.*;
import static io.vavr.Predicates.isNotNull;
import static io.vavr.Predicates.isNull;
import static workshops.DecompositionAnswersPatterns.*;

/**
 * Created by mtumilowicz on 2019-05-01.
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

    public static PersonStats switchOnEnum(Person person) {
        return Match(person.getType()).of(
                Case($(Person.PersonType.VIP), () -> getFullStats(person)),
                Case($(Person.PersonType.ORDINARY), () -> getStats(person)),
                Case($(Person.PersonType.TEMPORARY), () -> getFastStats(person)),
                Case($(), ignore -> {
                    throw new IllegalStateException("value not supported");
                }));
    }

    private static PersonStats getFullStats(Person person) {
        return PersonStats.of(PersonStats.PersonStatsType.FULL);
    }


    private static PersonStats getStats(Person person) {
        return PersonStats.of(PersonStats.PersonStatsType.NORMAL);
    }

    private static PersonStats getFastStats(Person person) {
        return PersonStats.of(PersonStats.PersonStatsType.FAST);
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
                Case($Left($()), Answers::patch),
                Case($Right($()), Answers::processPerson)
        );
    }

    private static Either<String, Person> patch(BadRequest badRequest) {
        return Objects.equals(badRequest.getMessage(), "can be fixed")
                ? Either.right(Person.ofType(null))
                : Either.left("cannot be fixed, too many errors");

    }

    private static Either<String, Person> processPerson(Person person) {
        return person.getType() == Person.PersonType.VIP
                ? Either.right(person)
                : Either.left("cannot be processed, because ...");
    }

    public static Option<String> optionDecompose(int id, List<String> logfile) {
        return Match(findById(id)).of(
                Case($None(), () -> {
                    logfile.add("cannot find for id = " + id);
                    return Option.none();
                }),
                Case($Some($()), value -> Option.some("processed " + id))
        );
    }

    private static Option<String> findById(int id) {
        return id == 1
                ? Option.some("found in database")
                : Option.none();
    }

    public static Try<Integer> tryDecompose(String number) {
        Try<Integer> _try = Try.of(() -> Integer.parseInt(number));

        return Match(_try).of( // try with exceptions
                Case($Success($()), value -> Try.success(value * value)),
                Case($Failure($()), Try::failure)
        );
    }

    public static String ifSyntax(Person2 person2) {
        return Match(person2).of(
                Case($(isNull()), () -> "cannot be null"),
                Case($(Person2::isActive), Answers::serviceDisable),
                Case($(), Answers::serviceActivate)
        );
    }

    private static String serviceActivate(Person2 person2) {
        return "activated";
    }

    private static String serviceDisable(Person2 person2) {
        return "deactivated";
    }

    public static int localDateDecompose(LocalDate date) {
        return Match(date).of(
                Case($LocalDate($(year -> year < 2015), $(), $()), Answers::taxBefore2015),
                Case($LocalDate($(year -> year > 2015), $(), $()), Answers::taxAfter2015)
        );
    }
    
    private static int taxBefore2015() {
        return 15;
    }

    private static int taxAfter2015() {
        return 25;
    }

    public static Integer decomposePerson3(Person3 person) {
        return Match(person).of(
                Case($Person3($(), $()), 
                        (account, address) -> serviceMethodAssess(new CreditAssessSubjects(
                                account.getBalance(), 
                                account.getSalary(), 
                                address.getCountry())))
        );
    }

    private static int serviceMethodAssess(CreditAssessSubjects subjects) {
        return Match(subjects).of(
                Case($CreditAssessSubjects($(), $(), $()),
                        (salary, balance, country) -> 5 * serviceMethodAssessBalance(balance) +
                                3 * serviceMethodAssessSalary(salary) +
                                2 * serviceMethodAssessCountry(country))
        );
    }

    private static int serviceMethodAssessBalance(int balance) {
        return balance < 1000 
                ? 25
                : 50;
    }

    private static int serviceMethodAssessSalary(int salary) {
        return salary < 3000
                ? 6
                : 10;
    }

    private static int serviceMethodAssessCountry(String country) {
        return Objects.equals(country, "POLAND")
                ? 120
                : 30;
    }
    
}
