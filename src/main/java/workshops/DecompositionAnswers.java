package workshops;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.Tuple3;
import io.vavr.match.annotation.Patterns;
import io.vavr.match.annotation.Unapply;
import person.Account;
import person.Address;
import person.CreditAssessSubjects;
import person.Person;

import java.time.LocalDate;

/**
 * Created by mtumilowicz on 2019-05-01.
 */
@Patterns
class DecompositionAnswers {
    @Unapply
    static Tuple2<Account, Address> PersonByCreditAssessSubjects(Person person) {
        return Tuple.of(person.getAccount(), person.getAddress());
    }

    @Unapply
    static Tuple3<Integer, Integer, String> CreditAssessSubjects(CreditAssessSubjects subjects) {
        return Tuple.of(subjects.getBalance(), subjects.getSalary(), subjects.getCountry());
    }

    @Unapply
    static Tuple3<Integer, Integer, Integer> LocalDate(LocalDate date) {
        return Tuple.of(
                date.getYear(), date.getMonthValue(), date.getDayOfMonth());
    }
}
