package person

import io.vavr.control.Either
import person.request.PersonRequest
import person.request.ValidPersonRequest
import spock.lang.Specification

/**
 * Created by mtumilowicz on 2019-05-03.
 */
class PersonServiceTest extends Specification {
    def "successful patch when salary < 0 - active = false, salary = 0"() {
        given:
        def canBeFixed = PersonRequest.builder()
                .type(PersonType.VIP)
                .balance(3000)
                .country('Poland')
                .city('Warsaw')
                .active(true)
                .salary(-1000)
                .build()
        when:
        def fixed = PersonService.patch(canBeFixed)

        then:
        fixed == Either.right(Person.builder()
                .type(PersonType.VIP)
                .active(false)
                .account(Account.builder()
                        .salary(Salary.of(0))
                        .balance(3000)
                        .build())
                .address(Address.builder()
                        .city('Warsaw')
                        .country('Poland')
                        .build())
                .build())
    }

    def "failure patch - to many errors"() {
        given:
        def cannotBeFixed = PersonRequest.builder()
                .build()
        when:
        def notFixed = PersonService.patch(cannotBeFixed)

        then:
        notFixed == Either.left('cannot be fixed, too many errors')
    }

    def "successful assemble - all business rules are met"() {
        given:
        def allBusinessRulesAreMet = ValidPersonRequest.builder()
                .type(PersonType.VIP)
                .active(true)
                .salary(Salary.of(0))
                .balance(3000)
                .city('Warsaw')
                .country('Poland')
                .build()

        when:
        def correct = PersonService.assemblePerson(allBusinessRulesAreMet)

        then:
        correct == Either.right(Person.builder()
                .type(PersonType.VIP)
                .active(true)
                .account(Account.builder()
                        .salary(Salary.of(0))
                        .balance(3000)
                        .build())
                .address(Address.builder()
                        .city('Warsaw')
                        .country('Poland')
                        .build())
                .build())
    }
    
    def "failure assemble - not all business rules are met: VIP should be active"() {
        given:
        def notAllBusinessRulesAreMet = ValidPersonRequest.builder()
                .type(PersonType.VIP)
                .active(false)
                .salary(Salary.of(0))
                .balance(3000)
                .city('Warsaw')
                .country('Poland')
                .build()

        when:
        def incorrect = PersonService.assemblePerson(notAllBusinessRulesAreMet)

        then:
        incorrect == Either.left('not all business rules are matched')
    }
}
