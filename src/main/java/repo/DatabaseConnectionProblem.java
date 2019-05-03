package repo;

import lombok.Value;

/**
 * Created by mtumilowicz on 2019-05-03.
 */
@Value
public class DatabaseConnectionProblem extends RuntimeException {
    int userId;
}
