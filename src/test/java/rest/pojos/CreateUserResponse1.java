package rest.pojos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import utils.DateDeserializer1;

import java.time.LocalDateTime;

@Data
public class CreateUserResponse1 extends UserRequest1 {
    private int id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'hh:mm:ss.SSSZ")
    @JsonDeserialize(using = DateDeserializer1.class)
    private LocalDateTime createdAt;
}
