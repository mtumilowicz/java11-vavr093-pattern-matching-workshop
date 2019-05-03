package repo;

import io.vavr.control.Try;

/**
 * Created by mtumilowicz on 2019-05-03.
 */
public class BackupRepository {
    public static Try<String> findById(int id) {
        return Try.of(() -> "from backup");
    }

}
