package sem.commons;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DateDTO {

    private int year;
    private int month;
    private int day;

    public DateDTO(
            @JsonProperty("year") int year,
            @JsonProperty("month") int month,
            @JsonProperty("day") int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

}
