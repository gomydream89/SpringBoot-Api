package kakaopay.kakaopaysec.domain.repository;

import kakaopay.kakaopaysec.domain.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    @Transactional
    @Modifying
    @Query(value =
            "UPDATE ACCOUNT \n" +
                    "SET BR_CD = ?2 \n" +
                    "WHERE BR_CD = ?1 "
            , nativeQuery = true)
    void modifyBrCd(String bfBrCd, String toBrCd);
}
