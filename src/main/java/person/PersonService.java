package person;

import io.vavr.control.Either;
import request.PersonRequest;
import request.ValidPersonRequest;

import static io.vavr.API.*;

/**
 * Created by mtumilowicz on 2019-05-02.
 */
public class PersonService {

    public static Either<String, Person> patch(PersonRequest personRequest) {
        return Match(personRequest).of(
                Case($(request -> request.getSalary() < 0),
                        request -> Either.right(
                                Person.builder()
                                        .type(personRequest.getType())
                                        .active(false)
                                        .address(Address.builder()
                                                .city(personRequest.getCity())
                                                .country(personRequest.getCountry())
                                                .build())
                                        .account(Account.builder()
                                                .balance(personRequest.getBalance())
                                                .build())
                                        .build()
                        )),
                Case($(), () -> Either.left("cannot be fixed, too many errors"))
        );
    }

    public static Either<String, Person> assemblePerson(ValidPersonRequest request) {
        return Either.right(Person.builder()
                .type(request.getType())
                .active(request.isActive())
                .address(request.getAddress())
                .account(request.getAccount())
                .build());
    }

    public static String activate(Person person) {
        return "activated";
    }

    public static String disable(Person person) {
        return "deactivated";
    }
}