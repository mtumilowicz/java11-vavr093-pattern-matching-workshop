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

    static void ads3(Person person) {
        switch (person.type) {
            case VIP:
                System.out.println("VIP"); // getfullstatistics
                break;
            case ORDINARY:
                System.out.println("ORDINARY");
                break;
            case TEMPORARY:
                System.out.println("TEMPORARY"); // getfaststatistics
                break;
            default:
                throw new IllegalStateException("value not supported");
        }
    }

    public static void main(String[] args) {
        int input = 2;
        System.out.println(X.ads(input));
    }
}

class Person {
    Type type;
}

enum Type {
    ORDINARY, VIP, TEMPORARY
}