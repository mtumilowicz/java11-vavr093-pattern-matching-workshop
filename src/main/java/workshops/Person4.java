package workshops;

import lombok.AllArgsConstructor;
import lombok.Value;

/**
 * Created by mtumilowicz on 2019-05-01.
 */
@AllArgsConstructor
@Value
public class Person4 {
    Person.PersonType type;
    boolean active;
    int salary;
}
