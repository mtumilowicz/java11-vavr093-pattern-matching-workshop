package repo;

import io.vavr.control.Try;

/**
 * Created by mtumilowicz on 2019-05-03.
 */
public class CacheRepository {
    public static Try<String> findById(int id) {
        if (id == 2) {
            return Try.failure(new CacheUserCannotBeFound(id));
        }
        if (id == 3) {
            return Try.failure(new CacheUserCannotBeFound(id));
        }
        if (id == 4) {
            return Try.failure(new CacheUserCannotBeFound(id));
        }
        if (id == 5) {
            return Try.failure(new CacheSynchronization());
        }
        return Try.of(() -> "from cache");
    }
}
