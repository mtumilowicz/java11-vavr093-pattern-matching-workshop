package workshops;

import lombok.AllArgsConstructor;
import lombok.Value;

/**
 * Created by mtumilowicz on 2019-05-01.
 */
@AllArgsConstructor
@Value
public class Account {
    int balance;
    int salary;
}