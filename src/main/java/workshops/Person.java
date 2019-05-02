package workshops;

import lombok.Builder;
import lombok.Value;

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
    
    public int getSalary() {
        return account.getSalary();
    }
}
