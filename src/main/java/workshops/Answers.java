package workshops;

import static io.vavr.API.*;

/**
 * Created by mtumilowicz on 2019-05-01.
 */
public class Answers {
    public static int numberConverter(String number) {
        return Match(number).of(
                Case($("one"), 1),
                Case($("two"), 2),
                Case($("three"), 3),
                Case($(), () -> {throw new IllegalStateException("value not supported");}));
    }
}
