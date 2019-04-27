package xxx;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.Tuple3;
import io.vavr.match.annotation.Patterns;
import io.vavr.match.annotation.Unapply;

import java.time.LocalDate;

/**
 * Created by mtumilowicz on 2019-04-27.
 */
@Patterns 
class Demo {
    @Unapply
    static Tuple3<Integer, Integer, Integer> LocalDate(LocalDate date) {
        return Tuple.of(
                date.getYear(), date.getMonthValue(), date.getDayOfMonth());
    }

    @Unapply
    static Tuple2<Account, Address> Person3(Person3 person) {
        return Tuple.of(person.account, person.address);
    }

    @Unapply
    static Tuple3<Integer, Integer, String> CreditAssessSubjects(CreditAssessSubjects subjects) {
        return Tuple.of(subjects.balance, subjects.salary, subjects.country);
    }
}
