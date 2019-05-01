package workshops;

import lombok.Value;

/**
 * Created by mtumilowicz on 2019-05-01.
 */
@Value(staticConstructor = "of")
public class PersonStats {
    PersonStatsType type;
    
    public enum PersonStatsType {
        FULL, NORMAL, FAST
    }
}
