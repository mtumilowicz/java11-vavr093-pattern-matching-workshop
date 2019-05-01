package xxx;

import lombok.AllArgsConstructor;
import lombok.ToString;

import java.util.function.Predicate;

import static io.vavr.API.*;
import static io.vavr.Predicates.anyOf;
import static io.vavr.Predicates.noneOf;

/**
 * Created by mtumilowicz on 2019-04-27.
 */
public class X {
//    static String ads(int input) {
//        return Match(input).of(
//                Case($(1), "one"),
//                Case($(2), "two"),
//                Case($(3), "three"),
//                Case($(), "?"));
//    }

//    static String ads2(int input) {
//        Range<Integer> threshold1 = Range.closed(0, 50);
//        Range<Integer> threshold2 = Range.closed(51, 150);
//        Range<Integer> threshold3 = Range.atLeast(151);
//
//        return Match(input).of(
//                Case($(threshold1::contains), "one"),
//                Case($(threshold2::contains), "two"),
//                Case($(threshold3::contains), "three"),
//                Case($(), "?"));
//    }

//    static void ads3(Person person) {
//        switch (person.type) {
//            case VIP:
//                System.out.println("VIP"); // getfullstatistics
//                break;
//            case ORDINARY:
//                System.out.println("ORDINARY");
//                break;
//            case TEMPORARY:
//                System.out.println("TEMPORARY"); // getfaststatistics
//                break;
//            default:
//                throw new IllegalStateException("value not supported");
//        }
//    }

//    static void ads33(Person person) {
//        Match(person.type).of(
//                Case($(Type.VIP), type -> run(() -> System.out.println("VIP"))),
//                Case($(Type.ORDINARY), type -> run(() -> System.out.println("ORDINARY"))),
//                Case($(Type.TEMPORARY), type -> run(() -> System.out.println("TEMPORARY"))),
//                Case($(), ignore -> {
//                    throw new IllegalStateException("value not supported");
//                }));
//    }

//    static LocalDate dateMapper(String date) {
//        return Match(date).of(
//                Case($(isNull()), () -> null),
//                Case($(isNotNull()), it -> LocalDate.parse(it)));
//    }
//
//    static Option<LocalDate> dateMapper2(String date) {
//        return Match(date).of(
//                Case($(isNull()), Option.none()),
//                Case($(isNotNull()), it -> Option.some(LocalDate.parse(it))));
//    }
//
//    static Option<LocalDate> dateMapper3(String date) {
//        return Match(date).option( // Because we can’t perform exhaustiveness checks like the Scala compiler, we provide the possibility to return an optional result
//                Case($(isNotNull()), it -> LocalDate.parse(it)));
//    }

//    static void eitherDecompose(Either<BadRequest, Person> either) {
//        Match(either).of(
//                Case($Left($()), badRequest -> patch(badRequest)),
//                Case($Right($()), person -> processPerson(person))
//        );
//
//        Try<Integer> _try = Try.success(1);
//        Match(_try).of(
//                Case($Success($()), value -> value),
//                Case($Failure($()), x -> x)
//        );
//    }
//
//
//    static void optionDecompose(Option<Integer> option) {
//        ArrayList<String> logfile = new ArrayList<>();
//        option.onEmpty(() -> logfile.add("empty"))
//                .map(value -> IntMath.pow(value, 2))
//                .getOrElse(0);
//
//        Integer i = Match(option).of(
//                Case($None(), () -> {
//                    logfile.add("empty");
//                    return 0;
//                }),
//                Case($Some($()), value -> IntMath.pow(value, 2))
//        );
//    }

//    static void ifSyntax() {
//        Person2 person = Match(new Person2()).of(
//                Case($(p -> p.active), p -> X.doIfTrue2(p)),
//                Case($(p -> p.active), p -> X.doIfNot2(p))
//        );
//    }
//
//    static Person2 doIfTrue2(Person2 person2) {
//        return new Person2();
//    }
//
//    static Person2 doIfNot2(Person2 person2) {
//        return new Person2();
//    }

//    static String localDateDecompose(LocalDate date) {
//        return Match(date).of(
//                Case($LocalDate($(year -> year < 2015), $(), $()), () -> "old"),
//                Case($LocalDate($(year -> year > 2015), $(), $()), (y, m, d) -> "new")
//        );
//    }

//    static void person3Destructor2() {
//        Person3 p3 = new Person3(new Account(1, 5), new Address("a", "b"));
//
//        Match(p3).of(
//                Case($Person3($(), $()), (account, address) -> serviceMethodAssess(new CreditAssessSubjects(account.balance, account.salary, address.country)))
//        );
//    }
//
//    static int serviceMethodAssess(CreditAssessSubjects subjects) {
//        return Match(subjects).of(
//                Case($CreditAssessSubjects($(), $(), $()),
//                        (salary, balance, country) -> 5 * serviceMethodAssessBalance(balance) +
//                                3 * serviceMethodAssessSalary(salary) +
//                                2 * serviceMethodAssessCountry(country))
//        );
//    }
//
//    static int serviceMethodAssessBalance(int balance) {
//        return 5;
//    }
//
//    static int serviceMethodAssessSalary(int salary) {
//        return 5;
//    }
//
//    static int serviceMethodAssessCountry(String country) {
//        return 5;
//    }


//    static Either<String, Person> patch(BadRequest badRequest) {
//        return Either.right(null);
//    }
//
//    static Either<String, Person> processPerson(Person person) {
//        return Either.right(null);
//    }

//    static void isInTest() {
//        Person person = new Person();
//
//        Match(person).of(
//                Case($Person($(isIn(Type.VIP, Type.ORDINARY))), ""),
//                Case($Person($(is(Type.TEMPORARY))), "")
//        );
//    }

//    static void existsTest() {
//        List<Try<Integer>> list = List.of(Try.success(1),
//                Try.success(2),
//                Try.success(3),
//                Try.failure(new IllegalArgumentException("a")),
//                Try.failure(new IllegalStateException("b")));
//        Either<List<Throwable>, List<Integer>> a = Match(list).of(
//                Case($(exists(Try::isFailure)),
//                        tries -> Either.left(tries
//                                .filter(Try::isFailure)
//                                .map(Try::getCause)
//                                .toList())),
//                Case($(),
//                        tries -> Either.right(tries
//                                .filter(Try::isSuccess)
//                                .map(Try::get)
//                                .toList()))
//        );
//    }

//    static void forAllTest() {
//        List<Try<Integer>> list = List.of(Try.success(1),
//                Try.success(2),
//                Try.success(3),
//                Try.failure(new IllegalArgumentException("a")),
//                Try.failure(new IllegalStateException("b")));
//        Either<List<Throwable>, List<Integer>> a = Match(list).of(
//                Case($(forAll(Try::isSuccess)),
//                        tries -> Either.right(tries
//                                .filter(Try::isSuccess)
//                                .map(Try::get)
//                                .toList())),
//                Case($(),
//                        tries -> Either.left(tries
//                                .filter(Try::isFailure)
//                                .map(Try::getCause)
//                                .toList()))
//        );
//    }

//    static void allOfTest() {
//        Person4 p4 = new Person4(Type.VIP, true, 1);
//
//        Predicate<Person4> isActive = p -> p.active;
//        Predicate<Person4> isVIP = p -> p.type == Type.VIP;
//        Predicate<Person4> isOrdinary = p -> p.type == Type.ORDINARY;
//        Predicate<Person4> isTemporary = p -> p.type == Type.TEMPORARY;
//        
//        Match(p4).of(
//                Case($(allOf(isVIP, isActive)), "vip + active"),
//                Case($(allOf(isVIP, isActive.negate())), "vip + not active"),
//                Case($(allOf(isOrdinary, isActive)), "ordinary + active"),
//                Case($(allOf(isOrdinary, isActive.negate())), "ordinary + not active"),
//                Case($(allOf(isTemporary, isActive)), "temporary + active"),
//                Case($(allOf(isTemporary, isActive.negate())), "temporary + not active")
//        );
//    }

//    static String instanceOfTest(CheckedRunnable runnable) {
//        try {
//            runnable.run();
//            return "no exception";
//        } catch (Throwable exx) {
//            return Match(exx).of(
//                    Case($(instanceOf(IllegalArgumentException.class)), "IllegalArgumentException"),
//                    Case($(instanceOf(RuntimeException.class)), "RuntimeException"),
//                    Case($(instanceOf(IOException.class)), "IOException"),
//                    Case($(), "handle rest")
//            );
//        }
//    }

    static String noneOfTest(Person4 person) {
        Predicate<Person4> isVIP = p -> p.type == Type.VIP;
        Predicate<Person4> hasBigSalary = p -> p.salary > 1000;

        return Match(person).of(
                Case($(noneOf(isVIP, hasBigSalary)), "handle special"),
                Case($(), "handle rest")
        );
    }

    static void anyOfTest() {
        Person4 p4 = new Person4(Type.VIP, true, 1);

        Predicate<Person4> isVIP = p -> p.type == Type.VIP;
        Predicate<Person4> hasBigSalary = p -> p.salary > 1000;

        Match(p4).of(
                Case($(anyOf(isVIP, hasBigSalary)), "handle special"),
                Case($(), "handle rest")
        );
    }

    public static void main(String[] args) {
//        System.out.println(LocalDate.parse("2014-10-12"));
//        System.out.println(dateMapper("2014-10-12"));
//        System.out.println(dateMapper(null));
//        listDecomposition();
//        localDateDecompose();
//        person3Destructor();
    }
}

class Person2 {
    boolean active;
}

@AllArgsConstructor
@ToString
class Person3 {
    Account account;
    Address address;
}

@AllArgsConstructor
@ToString
class Person4 {
    Type type;
    boolean active;
    int salary;
}

@AllArgsConstructor
@ToString
class Account {
    int balance;
    int salary;
}

@AllArgsConstructor
@ToString
class Address {
    String city;
    String country;
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

@AllArgsConstructor
class CreditAssessSubjects {
    int balance;
    int salary;
    String country;
}