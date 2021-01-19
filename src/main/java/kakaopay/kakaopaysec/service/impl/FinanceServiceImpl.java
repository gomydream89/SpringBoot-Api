package kakaopay.kakaopaysec.service.impl;

import kakaopay.kakaopaysec.domain.entity.Account;
import kakaopay.kakaopaysec.domain.entity.Branch;
import kakaopay.kakaopaysec.domain.repository.AccountRepository;
import kakaopay.kakaopaysec.domain.repository.BranchRepository;
import kakaopay.kakaopaysec.domain.repository.FinanceRepository;
import kakaopay.kakaopaysec.domain.repository.TransactionRepository;
import kakaopay.kakaopaysec.dto.AccountAmtOutDto;
import kakaopay.kakaopaysec.dto.AccountOutDto;
import kakaopay.kakaopaysec.dto.BranchOutDto;
import kakaopay.kakaopaysec.service.FinanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FinanceServiceImpl implements FinanceService {

    private final TransactionRepository transactionRepository;
    private final BranchRepository branchRepository;
    private final AccountRepository accountRepository;
    private final FinanceRepository financeRepository;

    @Override
    public List<String> getAllYears(){
        // [거래내역]의 연도 목록 조회
        List<String> allYears = transactionRepository.getAllYearsList();
        return allYears;
    }

    @Override
    public AccountAmtOutDto getMaxAmtCusInfo(String year) {
        // 연도별 합계 금액이 가장 많은 고객정보 조회
        AccountAmtOutDto result = financeRepository.getMaxAmtCusInfo(year);
        return result;
    }

    @Override
    public List<AccountOutDto> getNonTrxCusList(String year) {
        // 연도별 거래가 없는 고객목록 조회
        List<AccountOutDto> resultList = financeRepository.getNonTrxCusList(year);
        return resultList;
    }

    @Override
    public List<BranchOutDto> getSumAmtBrList(String year) {
        // 해당 연도에 관리점 별 거래금액의 합계 목록 조회(합계금액이 큰 순서로 출력)
        List<BranchOutDto> resultList = financeRepository.getSumAmtBrList(year);
        return resultList;
    }

    @Override
    public Branch findByBrName(String brNm) {
        // 관리점 명으로 관리점 코드 조회
        Branch branch = branchRepository.findByBrNm(brNm);
        return branch;
    }

    @Override
    @Transactional
    public void mergeByBrCode(String bfBrCd, String toBrCd){
        // 계좌정보의 관리점을 수정
        accountRepository.modifyBrCd(bfBrCd, toBrCd);
    }

    @Override
    public BranchOutDto getSumAmtBrInfo(String brCd) {
        // 지점의 거래금액 합계 조회
        BranchOutDto result = financeRepository.getSumAmtBrInfo(brCd);
        return result;
    }

    @Override
    public List<Account> findByBrCd(String brCd) {
        // 관리점 코드로 계좌정보 목록 조회
        List<Account> accountList = accountRepository.findByBrCd(brCd);
        return accountList;
    }
}
