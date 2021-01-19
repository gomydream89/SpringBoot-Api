package kakaopay.kakaopaysec.domain.repository;

import kakaopay.kakaopaysec.dto.AccountAmtOutDto;
import kakaopay.kakaopaysec.dto.AccountOutDto;
import kakaopay.kakaopaysec.dto.BranchOutDto;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.List;

@Repository
public class FinanceRepository {
    @PersistenceUnit
    EntityManagerFactory emf;
    @PersistenceContext
    EntityManager em;
    JpaResultMapper jpaResultMapper = new JpaResultMapper();

    /* 1번 과제 : [거래내역]에서 연도,계좌 별 거래금액 합계 구한 후 [계좌]와 조인하여 계좌명 출력 */
    public AccountAmtOutDto getMaxAmtCusInfo(String year){
        String sql = "SELECT \n" +
                "A.YEAR, B.ACCT_NM, A.ACCT_NO, ROUND(A.SUM_AMT) AS SUM_AMT\n" +
                "FROM\n" +
                        "( \n" +
                                "SELECT\n" +
                                "SUBSTR(TRX_DATE, 0, 4) AS YEAR, " +
                                "ACCT_NO,\n" +
                                "SUM(TRX_AMT) - SUM(TRX_FEE) AS SUM_AMT\n" +
                                "FROM  TRANSACTION\n" +
                                "WHERE SUBSTR(TRX_DATE, 0, 4) = ?1\n" +
                                "AND   CANCEL_YN = 'N'\n" +
                                "GROUP BY SUBSTR(TRX_DATE, 0, 4), ACCT_NO\n" +
                                "ORDER BY SUM_AMT DESC\n" +
                        ") A\n" +
                "JOIN ACCOUNT B\n" +
                "ON A.ACCT_NO = B.ACCT_NO\n" +
                "WHERE ROWNUM <= 1 ";

        Query nativeQuery = em.createNativeQuery(sql);
        nativeQuery.setParameter(1, year);
        AccountAmtOutDto result = jpaResultMapper.uniqueResult(nativeQuery, AccountAmtOutDto.class);
        return result;
    }

    /* 2번 과제 : [계좌]에서 해당 연도의 거래가 없는 계좌를 검색 */
    public List<AccountOutDto> getNonTrxCusList(String year){
        String sql =  "SELECT\n" +
                                "?1 AS YEAR, ACCT_NM, ACCT_NO\n" +
                        "FROM  ACCOUNT\n" +
                        "WHERE ACCT_NO NOT IN\n" +
                                                "(\n" +
                                                "SELECT DISTINCT ACCT_NO\n" +
                                                "FROM   TRANSACTION\n" +
                                                "WHERE  SUBSTR(TRX_DATE,0,4) = ?1\n" +
                                                ")";

        Query nativeQuery = em.createNativeQuery(sql);
        nativeQuery.setParameter(1, year);
        List<AccountOutDto> result = jpaResultMapper.list(nativeQuery, AccountOutDto.class);
        return result;
    }

    /* 3번 과제 : 연도에 해당하는 관리점 별 거래금액 합계를 구하고 합계 금액이 큰 순서로 출력 */
    public List<BranchOutDto> getSumAmtBrList(String year){
        String sql =  "SELECT\n" +
                                "A.BR_NM, A.BR_CD, ROUND(B.SUM_AMT) AS SUM_AMT\n" +
                        "FROM   BRANCH A\n" +
                        "JOIN   ( \n" +
                                "SELECT\n" +
                                        "B.BR_CD, SUM(A.TRX_AMT) AS SUM_AMT\n" +
                                "FROM  TRANSACTION A\n" +
                                "JOIN  ACCOUNT B\n" +
                                "ON    A.ACCT_NO = B.ACCT_NO\n" +
                                "WHERE SUBSTR(A.TRX_DATE, 0, 4) = ?1\n" +
                                "AND   A.CANCEL_YN = 'N'\n" +
                                "GROUP BY B.BR_CD\n" +
                                ") B\n" +
                        "ON    A.BR_CD   = B.BR_CD\n" +
                        "ORDER BY SUM_AMT DESC";

        Query nativeQuery = em.createNativeQuery(sql);
        nativeQuery.setParameter(1, year);
        List<BranchOutDto> result = jpaResultMapper.list(nativeQuery, BranchOutDto.class);
        return result;
    }

    /* 4번 과제 : 입력한 지점의 거래금액 합계를 출력 */
    public BranchOutDto getSumAmtBrInfo(String brCd){
        String sql =  "SELECT\n" +
                                "A.BR_NM, A.BR_CD, ROUND(B.SUM_AMT) AS SUM_AMT\n" +
                        "FROM   BRANCH A\n" +
                        "JOIN   ( \n" +
                                    "SELECT\n" +
                                    "B.BR_CD, SUM(A.TRX_AMT) AS SUM_AMT\n" +
                                    "FROM  TRANSACTION A\n" +
                                    "JOIN  ACCOUNT B\n" +
                                    "ON    A.ACCT_NO   = B.ACCT_NO\n" +
                                    "WHERE B.BR_CD     = ?1\n" +
                                    "AND   A.CANCEL_YN = 'N'\n" +
                                    "GROUP BY B.BR_CD\n" +
                                ") B\n" +
                        "ON    A.BR_CD   = B.BR_CD\n" +
                        "ORDER BY SUM_AMT DESC";

        Query nativeQuery = em.createNativeQuery(sql);
        nativeQuery.setParameter(1, brCd);
        BranchOutDto result;
        try{
            result = jpaResultMapper.uniqueResult(nativeQuery, BranchOutDto.class);
        }catch(NoResultException nre){
            result = new BranchOutDto();
        }
        return result;
    }
}
