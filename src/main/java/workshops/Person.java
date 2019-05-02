package workshops;

import lombok.Value;

/**
 * Created by mtumilowicz on 2019-05-01.
 */
@Value(staticConstructor = "ofType")
public class Person {
    PersonType type;
}
