package credit;

import person.CreditAssessSubjects;

import java.util.Objects;

import static io.vavr.API.*;
import static io.vavr.API.$;
import static workshops.DecompositionAnswersPatterns.$CreditAssessSubjects;

/**
 * Created by mtumilowicz on 2019-05-02.
 */
public class CreditAssessmentService {
    public static int serviceMethodAssess(CreditAssessSubjects subjects) {
        return Match(subjects).of(
                Case($CreditAssessSubjects($(), $(), $()),
                        (salary, balance, country) -> 5 * serviceMethodAssessBalance(balance) +
                                3 * serviceMethodAssessSalary(salary) +
                                2 * serviceMethodAssessCountry(country))
        );
    }

    private static int serviceMethodAssessBalance(int balance) {
        return balance < 1000
                ? 25
                : 50;
    }

    private static int serviceMethodAssessSalary(int salary) {
        return salary < 3000
                ? 6
                : 10;
    }

    private static int serviceMethodAssessCountry(String country) {
        return Objects.equals(country, "POLAND")
                ? 120
                : 30;
    }
}
