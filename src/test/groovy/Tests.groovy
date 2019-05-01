import spock.lang.Specification
import workshops.Answers
import workshops.Person
import workshops.PersonStats

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
}
