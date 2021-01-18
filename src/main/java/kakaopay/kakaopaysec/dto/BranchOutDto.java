package kakaopay.kakaopaysec.dto;

import lombok.*;

import java.math.BigDecimal;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BranchOutDto {
    private String brName;
    private String brCode;
    private BigDecimal sumAmt;
}
