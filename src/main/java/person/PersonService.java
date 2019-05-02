package person;

import io.vavr.control.Either;
import request.BadRequest;

import java.util.Objects;

/**
 * Created by mtumilowicz on 2019-05-02.
 */
public class PersonService {
    
    public static Either<String, Person> patch(BadRequest badRequest) {
        return Objects.equals(badRequest.getMessage(), "can be fixed")
                ? Either.right(Person.builder().build())
                : Either.left("cannot be fixed, too many errors");

    }

    public static Either<String, Person> processPerson(Person person) {
        return person.getType() == PersonType.VIP
                ? Either.right(person)
                : Either.left("cannot be processed, because ...");
    }

    public static String activate(Person person) {
        return "activated";
    }

    public static String disable(Person person) {
        return "deactivated";
    }
}
