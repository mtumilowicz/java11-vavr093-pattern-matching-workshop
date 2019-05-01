import io.vavr.CheckedRunnable
import io.vavr.collection.List
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

    def "decomposePerson3"() {
        given:
        Person3 p1 = new Person3(new Account(20_000, 800), new Address("POLAND", "WARSAW"))
        Person3 p2 = new Person3(new Account(1000, 2000), new Address("USA", "NEW YORK"))
        Person3 p3 = new Person3(new Account(15_000, 950), new Address("POLAND", "KRAKOW"))

        expect:
        Answers.decomposePerson3(p1) == 215
        Answers.decomposePerson3(p2) == 328
        Answers.decomposePerson3(p3) == 215
    }

    def "existsTest"() {
        given:
        def ex1 = new IllegalArgumentException("a")
        def ex2 = new IllegalStateException("b")
        and:
        def withFailures = List.of(Try.success(1),
                Try.success(2),
                Try.success(3),
                Try.failure(ex1),
                Try.failure(ex2))
        def withoutFailures = List.of(Try.success(1),
                Try.success(2),
                Try.success(3),
                Try.success(4),
                Try.success(5))

        when:
        def test1 = Answers.existsTest(withFailures)
        def test2 = Answers.existsTest(withoutFailures)

        then:
        test1.isLeft()
        test1.getLeft().size() == 2
        test1.getLeft().containsAll([ex1, ex2])
        test2.isRight()
        test2.get().size() == 5
        test2.get().containsAll([1, 2, 3, 4, 5])
    }

    def "forAllTest"() {
        given:
        def ex1 = new IllegalArgumentException("a")
        def ex2 = new IllegalStateException("b")
        and:
        def withFailures = List.of(Try.success(1),
                Try.success(2),
                Try.success(3),
                Try.failure(ex1),
                Try.failure(ex2))
        def withoutFailures = List.of(Try.success(1),
                Try.success(2),
                Try.success(3),
                Try.success(4),
                Try.success(5))

        when:
        def test1 = Answers.forAllTest(withFailures)
        def test2 = Answers.forAllTest(withoutFailures)

        then:
        test1.isLeft()
        test1.getLeft().size() == 2
        test1.getLeft().containsAll([ex1, ex2])
        test2.isRight()
        test2.get().size() == 5
        test2.get().containsAll([1, 2, 3, 4, 5])
    }

    def "allOfTest"() {
        given:
        def vipActive = new Person4(Person.PersonType.VIP, true, 1)
        def vipNotActive = new Person4(Person.PersonType.VIP, false, 1)
        def ordinaryActive = new Person4(Person.PersonType.ORDINARY, true, 1)
        def ordinaryNotActive = new Person4(Person.PersonType.ORDINARY, false, 1)
        def temporaryActive = new Person4(Person.PersonType.TEMPORARY, true, 1)
        def temporaryNotActive = new Person4(Person.PersonType.TEMPORARY, false, 1)

        expect:
        Answers.allOfTest(vipActive) == 'vip + active'
        Answers.allOfTest(vipNotActive) == 'vip + not active'
        Answers.allOfTest(ordinaryActive) == 'ordinary + active'
        Answers.allOfTest(ordinaryNotActive) == 'ordinary + not active'
        Answers.allOfTest(temporaryActive) == 'temporary + active'
        Answers.allOfTest(temporaryNotActive) == 'temporary + not active'
    }

    def "allOfTest, guard"() {
        when:
        Answers.allOfTest(null)

        then:
        thrown(IllegalArgumentException)
    }
    
    def "instanceOfTest"() {
        given:
        CheckedRunnable noException = {}
        CheckedRunnable illegalArgumentException = {throw new IllegalArgumentException()}
        CheckedRunnable runtimeException = {throw new RuntimeException()}
        CheckedRunnable iOException = {throw new IOException()}
        CheckedRunnable classNotFoundException = {throw new ClassNotFoundException()}
        
        expect:
        Answers.instanceOfTest(noException) == 'no exception'
        Answers.instanceOfTest(illegalArgumentException) == 'IllegalArgumentException'
        Answers.instanceOfTest(runtimeException) == 'RuntimeException'
        Answers.instanceOfTest(iOException) == 'IOException'
        Answers.instanceOfTest(classNotFoundException) == 'handle rest'
    }
    
    def "noneOfTest"() {
        given:
        def vip = new Person4(Person.PersonType.VIP, true, 1)
        def bigSalary = new Person4(Person.PersonType.ORDINARY, true, 3000)
        def regular = new Person4(Person.PersonType.ORDINARY, true, 300)
        
        expect:
        Answers.noneOfTest(vip) == 'handle special'
        Answers.noneOfTest(bigSalary) == 'handle special'
        Answers.noneOfTest(regular) == 'handle rest'
    }

    def "anyOfTest"() {
        given:
        def vip = new Person4(Person.PersonType.VIP, true, 1)
        def bigSalary = new Person4(Person.PersonType.ORDINARY, true, 3000)
        def regular = new Person4(Person.PersonType.ORDINARY, true, 300)

        expect:
        Answers.anyOfTest(vip) == 'handle special'
        Answers.anyOfTest(bigSalary) == 'handle special'
        Answers.anyOfTest(regular) == 'handle rest'
    }
}
