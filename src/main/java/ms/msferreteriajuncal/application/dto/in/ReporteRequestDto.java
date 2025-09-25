package ms.msferreteriajuncal.application.dto.in;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ReporteRequestDto {
    private LocalDate desde;
    private LocalDate hasta;
    private Integer top;

}
