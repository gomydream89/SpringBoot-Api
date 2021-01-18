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
public class Branch {
    @Id
    private String brCd;  //관리점코드
    private String brNm;  //관리점명

}
