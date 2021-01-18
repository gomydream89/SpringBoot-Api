package kakaopay.kakaopaysec.domain.entity;

import lombok.*;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class TransactionId implements Serializable {
    // 복합키
    private String  trxDate;    //거래일자
    private String  acctNo;     //계좌번호
    private int     trxNo;      //거래번호
}
