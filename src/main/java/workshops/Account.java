package workshops;

import lombok.Builder;
import lombok.Value;

/**
 * Created by mtumilowicz on 2019-05-01.
 */
@Value
@Builder
public class Account {
    int balance;
    int salary;
}