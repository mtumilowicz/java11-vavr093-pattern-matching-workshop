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

    public boolean isVip() {
        return type == PersonType.VIP;
    }

    public boolean isRegular() {
        return type == PersonType.REGULAR;
    }

    public boolean isTemporary() {
        return type == PersonType.TEMPORARY;
    }

    public boolean hasBigSalary() {
        return getSalary() > 1000;
    }

    public int getSalary() {
        return account.getSalary();
    }
}
