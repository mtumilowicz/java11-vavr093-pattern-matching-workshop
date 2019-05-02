package person;

import io.vavr.control.Either;
import person.request.PersonRequest;
import person.request.ValidPersonRequest;

import java.util.function.Predicate;

import static io.vavr.API.*;
import static io.vavr.Predicates.allOf;

/**
 * Created by mtumilowicz on 2019-05-02.
 */
public class PersonService {

    public static Either<String, Person> patch(PersonRequest personRequest) {
        Predicate<PersonRequest> negativeSalary = request -> request.getSalary() < 0;
        return Match(personRequest).of(
                Case($(negativeSalary),
                        request -> Either.right(
                                Person.builder()
                                        .type(request.getType())
                                        .active(false)
                                        .address(Address.builder()
                                                .city(request.getCity())
                                                .country(request.getCountry())
                                                .build())
                                        .account(Account.builder()
                                                .balance(request.getBalance())
                                                .build())
                                        .build()
                        )),
                Case($(), () -> Either.left("cannot be fixed, too many errors"))
        );
    }

    public static Either<String, Person> assemblePerson(ValidPersonRequest request) {
        return Match(request).of(
                Case($(allOf(PersonService::businessRule1, PersonService::businessRule2)),
                        validRequest ->
                                Either.right(Person.builder()
                                        .type(validRequest.getType())
                                        .active(validRequest.isActive())
                                        .address(Address.builder()
                                                .city(validRequest.getCity())
                                                .country(validRequest.getCountry())
                                                .build())
                                        .account(Account.builder()
                                                .salary(validRequest.getSalary())
                                                .balance(validRequest.getBalance())
                                                .build())
                                        .build())),
                Case($(), () -> Either.left("not all business rules are matched"))
        );
    }

    public static String activate(Person person) {
        return "activated";
    }

    public static String disable(Person person) {
        return "deactivated";
    }

    private static boolean businessRule1(ValidPersonRequest request) {
        return true;
    }

    private static boolean businessRule2(ValidPersonRequest request) {
        return true;
    }
}
