package kakaopay.kakaopaysec.service;

import kakaopay.kakaopaysec.domain.entity.Account;
import kakaopay.kakaopaysec.dto.AccountAmtOutDto;
import kakaopay.kakaopaysec.dto.AccountOutDto;
import kakaopay.kakaopaysec.dto.BranchOutDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
//@RunWith(SpringRunner.class)
public class FinanceServiceTest {
    @Autowired
    private FinanceService financeService;

    @Autowired
    private DataService dataService;

    @BeforeEach
    void initDataBase() throws IOException {
        //Test 실행 시마다, DataBase 초기화
        dataService.saveFileToDatabase("data/데이터_관리점정보.csv");
        dataService.saveFileToDatabase("data/데이터_계좌정보.csv");
        dataService.saveFileToDatabase("data/데이터_거래내역.csv");
    }

    @Test
    @DisplayName("1번 과제 : 연도 별 합계 금액이 가장 많은 고객을 추출하는 API")
    void getMaxAmtCusInfo() {
        AccountAmtOutDto result = new AccountAmtOutDto();

        // 2018년 합계금액이 가장 많은 계좌는 11111114 -> 소유주는 테드
        result = financeService.getMaxAmtCusInfo("2018");
        Assertions.assertThat(result.getName()).isEqualTo("테드");

        // 2019년 합계금액이 가장 많은 계좌는 11111112 -> 소유주 : 테드
        result = financeService.getMaxAmtCusInfo("2019");
        Assertions.assertThat(result.getName()).isEqualTo("에이스");
    }

    @Test
    @DisplayName("2번 과제 : 연도에 거래가 없는 고객을 추출하는 API")
    void getNonTrxCusList() {
        List<AccountOutDto> resultList = new ArrayList<AccountOutDto>();

        // 2018년 거래가 없는 고객은 2명 (취소거래 포함)
        // 1) 계좌번호 : 11111115 -> 소유주 : 사라
        // 2) 계좌번호 : 11111121 -> 소유주 : 에이스
        resultList = financeService.getNonTrxCusList("2018");
        Assertions.assertThat(resultList.size()).isEqualTo(2);
        Assertions.assertThat(resultList.get(0).getName()).isEqualTo("사라");
        Assertions.assertThat(resultList.get(1).getName()).isEqualTo("에이스");

        // 2019년 거래가 없는 고객은 2명 (취소거래 포함)
        // 1) 계좌번호 : 11111114 -> 소유주 : 테드
        // 2) 계좌번호 : 11111121 -> 소유주 : 에이스
        resultList = financeService.getNonTrxCusList("2019");
        Assertions.assertThat(resultList.size()).isEqualTo(2);
        Assertions.assertThat(resultList.get(0).getName()).isEqualTo("테드");
        Assertions.assertThat(resultList.get(1).getName()).isEqualTo("에이스");

        // 2020년 유일한 거래내역이 있는 계좌는 11111121
        // 거래가 없는 고객수는 총 10명
        resultList = financeService.getNonTrxCusList("2020");
        Assertions.assertThat(resultList.size()).isEqualTo(10);
        for(AccountOutDto cusInfo : resultList){
            Assertions.assertThat(cusInfo.getAcctNo()).isNotEqualTo("11111121");
        }
    }

    @Test
    @DisplayName("3번 과제 : 연도별 관리점별 거래금액을 합한 후 합계금액이 큰 순서로 출력하는 API")
    void getSumAmtBrList() {
        List<BranchOutDto> resultList = new ArrayList<BranchOutDto>();

        // 2018년 거래내역(취소여부 N)이 존재하는 관리점 개수 : 4개
        // 거래금액의 합계가 가장 큰 관리점은 분당점
        // 관리점코드 : 'B' | 거래금액 합계 : 38500000
        resultList = financeService.getSumAmtBrList("2018");
        Assertions.assertThat(resultList.size()).isEqualTo(4);
        Assertions.assertThat(resultList.get(0).getBrName()).isEqualTo("분당점");
        Assertions.assertThat(resultList.get(0).getSumAmt()).isEqualTo(new BigDecimal("38500000"));

        // 2019년 거래내역(취소여부 N)이 존재하는 관리점 개수 : 4개
        // 거래금액의 합계가 가장 큰 관리점은 판교점
        // 관리점코드 : 'A' | 거래금액 합계 : 66800000
        resultList = financeService.getSumAmtBrList("2019");
        Assertions.assertThat(resultList.size()).isEqualTo(4);
        Assertions.assertThat(resultList.get(0).getBrName()).isEqualTo("판교점");
        Assertions.assertThat(resultList.get(0).getSumAmt()).isEqualTo(new BigDecimal("66800000"));

        // 2020년 거래내역(취소여부 N)이 있는 유일한 관리점은 을지로점
        // 관리점코드 : 'E' | 거래금액 합계 : 4000000
        resultList = financeService.getSumAmtBrList("2020");
        Assertions.assertThat(resultList.size()).isEqualTo(1);
        Assertions.assertThat(resultList.get(0).getBrName()).isEqualTo("을지로점");
        Assertions.assertThat(resultList.get(0).getSumAmt()).isEqualTo(new BigDecimal("1000000"));
    }

    @Test
    @DisplayName("4번 과제 : 입력한 관리점의 거래금액을 합계하여 출력하는 API")
    void getSumAmtBrInfo() {
        BranchOutDto result = new BranchOutDto();
        List<Account> accountList = new ArrayList<Account>();

        // 판교점(A)의 거래금액 합계 : 87310000
        result = financeService.getSumAmtBrInfo("A");
        Assertions.assertThat(result.getSumAmt()).isEqualTo(new BigDecimal("87310000"));

        // 분당점(B)의 거래금액 합계 : 83900000
        result = financeService.getSumAmtBrInfo("B");
        Assertions.assertThat(result.getSumAmt()).isEqualTo(new BigDecimal("83900000"));

        // 판교점(A)이 가지고 있는 계좌정보 개수 : 3개
        accountList = financeService.findByBrCd("A");
        Assertions.assertThat(accountList.size()).isEqualTo(3);

        // 분당점(B)이 가지고 있는 계좌정보 개수 : 3개
        accountList = financeService.findByBrCd("B");
        Assertions.assertThat(accountList.size()).isEqualTo(3);

        // 판교점(A)을 분당점(B)으로 통폐합
        financeService.mergeByBrCode("A", "B");

        // 통폐합 이후 판교점(A)이 가지고 있는 계좌정보 개수 : 0개
        accountList = financeService.findByBrCd("A");
        Assertions.assertThat(accountList.size()).isEqualTo(0);

        // 통폐합 이후 분당점(B)이 가지고 있는 계좌정보 개수 : 0개
        accountList = financeService.findByBrCd("B");
        Assertions.assertThat(accountList.size()).isEqualTo(6);

        // 분당점(B)의 거래금액 합계 : 171210000
        result = financeService.getSumAmtBrInfo("B");
        Assertions.assertThat(result.getSumAmt()).isEqualTo(new BigDecimal("171210000"));

    }

}


