import spock.lang.Specification
import workshops.Answers

/**
 * Created by mtumilowicz on 2019-04-10.
 */
class Tests extends Specification {
    def "simple switch case - number converter"() {
        expect:
        Answers.numberConverter("one") == 1
        Answers.numberConverter("two") == 2
        Answers.numberConverter("three") == 3
    }

    def "simple switch case - number converter, guard"() {
        when:
        Answers.numberConverter(null)
        
        then:
        thrown(IllegalStateException)
        
        when:
        Answers.numberConverter("four")
        
        then:
        thrown(IllegalStateException)
    }
}
