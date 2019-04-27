import spock.lang.Specification 
/**
 * Created by mtumilowicz on 2019-04-10.
 */
class Answers extends Specification {
    
    def "first test"() {
        given:
        int input = 2
        String output = X.ads(input)
        
        expect:
        output == "two"
    }

    def "snd test"() {
        given:
        int input = 60
        String output = X.ads2(input)
        
        expect:
        output == "two"
    }
}
