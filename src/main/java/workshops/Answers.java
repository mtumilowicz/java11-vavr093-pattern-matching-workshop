package workshops;

import com.google.common.collect.Range;

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
                Case($(), () -> {
                    throw new IllegalStateException("value not supported");
                }));
    }

    public static String thresholds(int input) {
        Range<Integer> threshold1 = Range.closed(0, 50);
        Range<Integer> threshold2 = Range.closed(51, 150);
        Range<Integer> threshold3 = Range.atLeast(151);

        return Match(input).of(
                Case($(threshold1::contains), "threshold1"),
                Case($(threshold2::contains), "threshold2"),
                Case($(threshold3::contains), "threshold3"),
                Case($(i -> i < 0), () -> {
                    throw new IllegalArgumentException("only positive numbers!");
                }));
    }
}
