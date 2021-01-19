package kakaopay.kakaopaysec.domain.repository;

import kakaopay.kakaopaysec.domain.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    //[거래내역]의 연도 목록 검색
    @Query(value =
            "SELECT\n" +
                    "DISTINCT SUBSTR(TRX_DATE, 0, 4) AS YEAR\n" +
                    "FROM  TRANSACTION\n" +
                    "WHERE CANCEL_YN = 'N'\n" +
                    "ORDER BY SUBSTR(TRX_DATE, 0, 4)"
            , nativeQuery = true)
    List<String> getAllYearsList();
}
