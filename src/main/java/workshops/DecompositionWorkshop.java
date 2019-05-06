package workshops;

import io.vavr.Tuple2;
import io.vavr.Tuple3;
import io.vavr.match.annotation.Unapply;
import person.*;

import java.time.LocalDate;

/**
 * Created by mtumilowicz on 2019-05-01.
 */
//@Patterns
class DecompositionWorkshop {
    @Unapply
    static Tuple2<Account, Address> PersonByCreditAssessSubjects(Person person) {
        return null; // Tuple.of(..., ...)
    }

    @Unapply
    static Tuple3<Integer, Salary, String> CreditAssessSubjects(CreditAssessSubjects subjects) {
        return null; // Tuple.of(..., ..., ...)
    }

    @Unapply
    static Tuple3<Integer, Integer, Integer> LocalDate(LocalDate date) {
        return null; // Tuple.of(..., ..., ...)
    }
}