import com.google.common.collect.Range;

import static io.vavr.API.*;

/**
 * Created by mtumilowicz on 2019-04-27.
 */
public class X {
    static String ads(int input) {
        return Match(input).of(
                Case($(1), "one"),
                Case($(2), "two"),
                Case($(3), "three"),
                Case($(), "?"));
    }    
    
    static String ads2(int input) {
        Range<Integer> threshold1 = Range.closed(0, 50);
        Range<Integer> threshold2 = Range.closed(51, 150);
        Range<Integer> threshold3 = Range.atLeast(151);
                
        return Match(input).of(
                Case($(threshold1::contains), "one"),
                Case($(threshold2::contains), "two"),
                Case($(threshold3::contains), "three"),
                Case($(), "?"));
    }

    public static void main(String[] args) {
        int input = 2;
        System.out.println(X.ads(input));
    }
}
