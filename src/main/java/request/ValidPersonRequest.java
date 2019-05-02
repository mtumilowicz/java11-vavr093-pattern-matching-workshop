package request;

import lombok.Builder;
import lombok.Value;
import person.Account;
import person.Address;
import person.PersonType;

/**
 * Created by mtumilowicz on 2019-05-02.
 */
@Value
@Builder
public class ValidPersonRequest {
    PersonType type;
    boolean active;
    Account account;
    Address address;
}
