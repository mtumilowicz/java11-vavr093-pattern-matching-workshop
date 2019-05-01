import io.vavr.control.Either
import io.vavr.control.Option
import io.vavr.control.Try
import spock.lang.Specification
import workshops.*

import java.time.LocalDate
import java.time.format.DateTimeParseException

/**
 * Created by mtumilowicz on 2019-04-10.
 */
class Tests extends Specification {
    def "number converter"() {
        expect:
        Answers.numberConverter("one") == 1
        Answers.numberConverter("two") == 2
        Answers.numberConverter("three") == 3
    }

    def "number converter, guard"() {
        when:
        Answers.numberConverter(null)

        then:
        thrown(IllegalStateException)

        when:
        Answers.numberConverter("four")

        then:
        thrown(IllegalStateException)
    }

    def "thresholds"() {
        expect:
        Answers.thresholds(0) == "threshold1"
        Answers.thresholds(25) == "threshold1"
        Answers.thresholds(50) == "threshold1"
        and:
        Answers.thresholds(51) == "threshold2"
        Answers.thresholds(100) == "threshold2"
        Answers.thresholds(150) == "threshold2"
        and:
        Answers.thresholds(200) == "threshold3"
        Answers.thresholds(250) == "threshold3"
        Answers.thresholds(1_000_000) == "threshold3"
        Answers.numberConverter("two") == 2
        Answers.numberConverter("three") == 3
    }

    def "thresholds guard"() {
        when:
        Answers.thresholds(-1)

        then:
        thrown(IllegalArgumentException)
    }

    def "switchOnEnum"() {
        expect:
        Answers.switchOnEnum(Person.ofType(Person.PersonType.VIP)) == PersonStats.of(PersonStats.PersonStatsType.FULL)
        Answers.switchOnEnum(Person.ofType(Person.PersonType.ORDINARY)) == PersonStats.of(PersonStats.PersonStatsType.NORMAL)
        Answers.switchOnEnum(Person.ofType(Person.PersonType.TEMPORARY)) == PersonStats.of(PersonStats.PersonStatsType.FAST)
    }

    def "switchOnEnum, guard"() {
        when:
        Answers.switchOnEnum(Person.ofType(null))

        then:
        thrown(IllegalStateException)
    }

    def "rawDateMapper"() {
        expect:
        Answers.rawDateMapper('2014-10-10') == LocalDate.of(2014, 10, 10)
        !Answers.rawDateMapper(null)
    }

    def "rawDateMapper, exceptional"() {
        when:
        Answers.rawDateMapper('wrong')

        then:
        thrown(DateTimeParseException)
    }

    def "optionDateMapper"() {
        expect:
        Answers.optionDateMapper('2014-10-10') == Option.some(LocalDate.of(2014, 10, 10))
        Answers.optionDateMapper(null) == Option.none()
    }

    def "optionDateMapper, exceptional"() {
        when:
        Answers.optionDateMapper('wrong')

        then:
        thrown(DateTimeParseException)
    }

    def "eitherDecompose"() {
        expect:
        Answers.eitherDecompose(null) == Either.left("cannot be null")
        Answers.eitherDecompose(Either.left(BadRequest.of(new Request(), "can be fixed"))) ==
                Either.right(Person.ofType(null))
        Answers.eitherDecompose(Either.left(BadRequest.of(new Request(), "cannot be fixed"))) ==
                Either.left('cannot be fixed, too many errors')
    }

    def "optionDecompose, cannot find in database"() {
        given:
        def logfile = []
        def notExistsId = 2

        expect:
        Answers.optionDecompose(notExistsId, logfile) == Option.none()
        logfile == ["cannot find for id = ${notExistsId}"]
    }

    def "optionDecompose, found in database"() {
        given:
        def logfile = []
        def existsId = 1

        expect:
        Answers.optionDecompose(existsId, logfile) == Option.some('processed ' + existsId)
        logfile == []
    }

    def "tryDecompose"() {
        when:
        def successTry = Answers.tryDecompose("2")
        def failTry = Answers.tryDecompose("wrong")

        then:
        successTry == Try.success(4)
        failTry.failure
        failTry.getCause().class == NumberFormatException
    }

    def "ifSyntax"() {
        expect:
        Answers.ifSyntax(null) == "cannot be null"
        Answers.ifSyntax(Person2.of(false)) == "activated"
        Answers.ifSyntax(Person2.of(true)) == "deactivated"
    }

    def "localDateDecompose"() {
        expect:
        Answers.localDateDecompose(LocalDate.of(2010, 10, 10)) == 15
        Answers.localDateDecompose(LocalDate.of(2019, 10, 10)) == 25
    }
}
