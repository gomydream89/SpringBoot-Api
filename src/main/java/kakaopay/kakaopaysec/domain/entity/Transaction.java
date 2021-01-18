package kakaopay.kakaopaysec.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @EmbeddedId
    private TransactionId transactionId;    //거래정보의 PK(거래일자, 계좌번호, 거래번호)

    private BigDecimal  trxAmt;     //거래금액
    private BigDecimal  trxFee;     //거래수수료
    private String      cancelYn;   //취소여부

}
