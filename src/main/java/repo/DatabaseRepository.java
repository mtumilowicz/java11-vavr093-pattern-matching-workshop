package repo;

import io.vavr.control.Try;

/**
 * Created by mtumilowicz on 2019-05-03.
 */
public class DatabaseRepository {
    public static Try<String> findById(int id) {
        switch (id) {
            case 2:
                return Try.failure(new DatabaseConnectionProblem(id));
            case 3:
                return Try.failure(new DatabaseUserCannotBeFound());
            default:
                return Try.of(() -> "from database");
        }
    }
}
