package workshops;

import lombok.Value;

@Value(staticConstructor = "of")
public
class BadRequest {
    Request request;
    String message;
}

