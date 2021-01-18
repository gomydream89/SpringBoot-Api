package kakaopay.kakaopaysec.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    private String acctNo;  //계좌번호
    private String acctNm;  //계좌명
    private String brCd;  //관리점코드
}
