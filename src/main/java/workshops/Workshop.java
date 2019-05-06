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

import static java.util.Objects.nonNull;
import static java.util.function.Predicate.not;

/**
 * Created by mtumilowicz on 2019-05-03.
 */
public class Workshop {

    /**
     * very simple number converter just to show, that every classic
     * switch-case construction could be rewritten using pattern matching
     * to be more concise, clean and easy to read
     */
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

    /**
     * very simple threshold based logic just to show, that every well-known
     * multiple ifs construction could be rewritten using pattern matching
     * to be more concise, clean and easy to read
     */
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

    /**
     * very simple enum based logic just to show, that every well-known
     * switch-case enum construction could be rewritten using pattern matching
     * to be more concise, clean and easy to read
     * 
     * this method simply loads appropriate stats for a given enum in a way:
     * VIP -> full stats (vips are not very common, and full stats are possibly time consuming
     *      so we may want to load it only for special client to show for example highly dedicated 
     *      marketing suggestions)
     * REGULAR -> we load ordinary, highly optimized stats - compromise between time-consumption
     *      and their scope
     * TEMPORARY -> it's just a temporary client (for example - performs buy without permanent account), 
     *      so we don't want to gather all info about him, or even we do not have possibility to do it,
     *      so we perform only fast round
     */
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

    /**
     * well-known X -> Y converter with a non-null guard
     * could be rewritten using pattern-matching in a more concise, 
     * clean and easy to read way
     * 
     * this method simply converts String date -> LocalDate
     * and if date is null - it returns null
     */
    public static LocalDate rawDateMapper(String date) {
        return Objects.isNull(date) // hint: Match(date).of
                ? null // hint: Case($(isNull()), () -> null)
                : LocalDate.parse(date);
    }

    /**
     * well-known X -> Option<Y> converter could be rewritten 
     * using pattern-matching in a more concise, 
     * clean and easy to read way
     *
     * this method simply converts String date -> Option<LocalDate>
     * and if date is null - it returns Option.none()
     */
    public static Option<LocalDate> optionDateMapper(String date) {
        return Option.of(date) // hint: Match(date).option
                .map(LocalDate::parse); // hint: Case($(isNotNull()), LocalDate::parse))
    }

    /**
     * every use of either's fold method could be rewritten using pattern matching
     * in some cases - it is easier to read
     * 
     * the purpose of this example is to show, that we could think about pattern matching
     * as a way of object decomposition
     * switch(either)
     *  case Left - do something
     *  case Right - do something else
     */
    public static Either<String, Person> eitherDecompose(Either<PersonRequest, ValidPersonRequest> either) {
        // Match(either).of
        return Objects.isNull(either)
                ? Either.left("cannot be null")
                // hint: Case($Left($()), PersonService::patch)
                : either.fold(PersonService::patch, PersonService::assemblePerson);
    }

    /**
     * every final consuming of Option could be rewritten using pattern matching
     * in some cases - it is easier to read
     * especially when it comes to performing side-effects
     * 
     * the purpose of this example is to show, that we could think about pattern matching
     * as a way of object decomposition
     * switch(option)
     *  case None - run some action (side-effects)
     *  case Some - run other action (side-effects)
     */
    public static void optionDecompose(int id, Display display) {
        PersonRepository.findById(id) // Match(PersonRepository.findById(id)).of
                // Case($None(), run(() -> display.push("cannot find person with id = " + id)))
                .onEmpty(() -> display.push("cannot find person with id = " + id))
                .forEach(value -> display.push("person: " + value + " processed"));
    }

    /**
     * every final consuming of Try could be rewritten using pattern matching
     * in some cases - it is easier to read
     * especially when it comes to performing side-effects
     *
     * the purpose of this example is to show, that we could think about pattern matching
     * as a way of object decomposition
     * switch(try)
     *  case Success - run some action (side-effects)
     *  case Failure - run other action (side-effects)
     */
    public static void tryDecompose(String number, Display display) {
        Try.of(() -> Integer.parseInt(number)) // Match(...).of
                // Case($Success($()), i -> run(() -> display.push("squared number is: " + i * i)))
                .onSuccess(i -> display.push("squared number is: " + i * i))
                .onFailure(ex -> display.push("cannot square number: " + ex.getLocalizedMessage()));
    }

    /**
     * every ternary operator could be rewritten using pattern matching
     * in some cases - it is easier to read
     * 
     * the purpose of this example is to show, that we could think about pattern matching
     * as a generalization of if-statement or ternary-statement
     */
    public static String ifSyntax(Person person) {
        // Match(person).of
        if (Objects.isNull(person)) {
            return "cannot be null";
        }

        return person.isActive() // Case($(Person::isActive), PersonService::disable)
                ? PersonService.disable(person)
                : PersonService.activate(person);
    }

    /**
     * every logic based on dates could be represented using pattern matching
     * nearly always in a more concise, cleaner and easier to read way
     * 
     */
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
        } catch (IllegalArgumentException ex) {
            return "IllegalArgumentException";
        } catch (RuntimeException ex) {
            return "RuntimeException";
        } catch (IOException ex) {
            return "IOException";
        } catch (Throwable ex) {
            // Match(ex).of
            // Case($(instanceOf(IllegalArgumentException.class)), "IllegalArgumentException")
            return "handle rest";
        }
    }

    public static String noneOfTest(@NonNull Person person) {
        // Match(person).of
        // Case($(noneOf(Person.hasType(PersonType.VIP), Person::hasBigSalary)), "handle special")
        // what if more than two predicates ?
        // what if more than one condition (that cannot be written using ternary op)
        return not(Person.hasType(PersonType.VIP).or(Person::hasBigSalary)).test(person)
                ? "handle special"
                : "handle rest";
    }

    public static String anyOfTest(@NonNull Person person) {
        // Match(person).of
        // Case($(anyOf(Person.hasType(PersonType.VIP), Person::hasBigSalary)), "handle special")
        // what if more than two predicates ?
        // what if more than one condition (that cannot be written using ternary op)
        return Person.hasType(PersonType.VIP).or(Person::hasBigSalary).test(person)
                ? "handle special"
                : "handle rest";
    }
}
