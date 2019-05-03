package repo;

import lombok.Value;

/**
 * Created by mtumilowicz on 2019-05-03.
 */
@Value
public class CacheUserCannotBeFound extends RuntimeException {
    int userId;
}
