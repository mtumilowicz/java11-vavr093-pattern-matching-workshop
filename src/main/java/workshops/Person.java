package workshops;

import lombok.Builder;
import lombok.Value;

import java.util.function.Predicate;

import static io.vavr.API.*;

/**
 * Created by mtumilowicz on 2019-05-01.
 */
@Value
@Builder
public class Person {
    PersonType type;
    boolean active;
    Account account;
    Address address;
    
    public static Predicate<Person> hasType(PersonType type) {
        return p -> Match(p.type).of(
                Case($(type), true),
                Case($(), false)
        );
    }

    public boolean hasBigSalary() {
        return getSalary() > 1000;
    }

    public int getSalary() {
        return account.getSalary();
    }
}
