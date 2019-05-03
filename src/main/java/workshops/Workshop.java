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
import java.util.Objects;

import static io.vavr.API.*;
import static io.vavr.Predicates.*;
import static java.util.Objects.nonNull;
import static java.util.function.Predicate.not;

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
        Preconditions.checkState(nonNull(person.getType()), "value not supported");

        switch (person.getType()) { // hint: Match(person.getType()).of
            case VIP: // hint: Case($(PersonType.VIP), () -> StatsService.getFullStats(person))
                return StatsService.getFullStats(person);
            case REGULAR:
                return StatsService.getStats(person);
            case TEMPORARY:
                return StatsService.getFastStats(person);
            default:
                throw new IllegalStateException("value not supported");
        }
    }

    public static LocalDate rawDateMapper(String date) {
        return Objects.isNull(date) // hint: Match(date).of
                ? null // hint: Case($(isNull()), () -> null)
                : LocalDate.parse(date);
    }

    public static Option<LocalDate> optionDateMapper(String date) {
        return Option.of(date) // hint: Match(date).option
                .map(LocalDate::parse); // hint: Case($(isNotNull()), LocalDate::parse))
    }

    public static Either<String, Person> eitherDecompose(Either<PersonRequest, ValidPersonRequest> either) {
        // Match(either).of
        return Objects.isNull(either)
                ? Either.left("cannot be null")
                // hint: Case($Left($()), PersonService::patch)
                : either.fold(PersonService::patch, PersonService::assemblePerson);
    }

    public static void optionDecompose(int id, Display display) {
        PersonRepository.findById(id) // Match(PersonRepository.findById(id)).of
                // Case($None(), run(() -> display.push("cannot find person with id = " + id)))
                .onEmpty(() -> display.push("cannot find person with id = " + id))
                .forEach(value -> display.push("person: " + value + " processed"));
    }

    public static void tryDecompose(String number, Display display) {
        Try.of(() -> Integer.parseInt(number)) // Match(...).of
                // Case($Success($()), i -> run(() -> display.push("squared number is: " + i * i)))
                .onSuccess(i -> display.push("squared number is: " + i * i))
                .onFailure(ex -> display.push("cannot square number: " + ex.getLocalizedMessage()));
    }

    public static String ifSyntax(Person person) {
        // Match(person).of
        if (Objects.isNull(person)) {
            return "cannot be null";
        }

        return person.isActive() // Case($(Person::isActive), PersonService::disable)
                ? PersonService.disable(person)
                : PersonService.activate(person);
    }

    public static int getTaxRateFor(@NonNull LocalDate date) {
        // Match(date).of
        // Case($LocalDate($(year -> year < 2015), $(), $()), TaxService::taxBeforeAnd2015)
        return date.getYear() <= 2015
                ? TaxService.taxBeforeAnd2015()
                : TaxService.taxAfter2015();
    }

    public static Integer personDecompose(@NonNull Person person) {
        // Match(person).of
        // Case($PersonByCreditAssessSubjects($(), $()), (account, address) -> ...
        Account account = person.getAccount();
        return CreditAssessmentService.serviceMethodAssess(CreditAssessSubjects.builder()
                .balance(account.getBalance())
                .salary(account.getSalary())
                .country(person.getAddress().getCountry())
                .build()
        );
    }

    public static Either<Seq<Throwable>, Seq<Integer>> existsTest(@NonNull Seq<Try<Integer>> list) {
        // Match(list).of
        return list.exists(Try::isFailure)
                // Case($(exists(Try::isFailure)), tries -> ...
                ? Either.left(list.filter(Try::isFailure).map(Try::getCause))
                : Either.right(list.map(Try::get));
    }

    public static Either<Seq<Throwable>, Seq<Integer>> forAllTest(@NonNull Seq<Try<Integer>> list) {
        // Match(list).of
        return list.forAll(Try::isSuccess)
                // Case($(forAll(Try::isSuccess)), tries -> ...
                ? Either.right(list.map(Try::get))
                : Either.left(list.filter(Try::isFailure).map(Try::getCause));
    }

    public static String allOfTest(@NonNull Person person) {
        // Match(person).of
        // Case($(allOf(Person.hasType(PersonType.VIP), Person::isActive)), "vip + active")
        if (Person.hasType(PersonType.VIP).and(Person::isActive).test(person)) {
            return "vip + active";
        }
        if (Person.hasType(PersonType.VIP).and(not(Person::isActive)).test(person)) {
            return "vip + not active";
        }
        if (Person.hasType(PersonType.REGULAR).and(Person::isActive).test(person)) {
            return "regular + active";
        }
        if (Person.hasType(PersonType.REGULAR).and(not(Person::isActive)).test(person)) {
            return "regular + not active";
        }
        if (Person.hasType(PersonType.TEMPORARY).and(Person::isActive).test(person)) {
            return "temporary + active";
        }
        if (Person.hasType(PersonType.TEMPORARY).and(not(Person::isActive)).test(person)) {
            return "temporary + not active";
        }
        throw new IllegalArgumentException("condition not supported");
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
