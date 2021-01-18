package kakaopay.kakaopaysec.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AccountAmtOutDto {
    private String year;
    private String name;
    private String acctNo;
    private BigDecimal sumAmt;
}
