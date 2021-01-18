package kakaopay.kakaopaysec.service;

import kakaopay.kakaopaysec.domain.entity.Branch;
import kakaopay.kakaopaysec.dto.AccountAmtOutDto;
import kakaopay.kakaopaysec.dto.AccountOutDto;
import kakaopay.kakaopaysec.dto.BranchOutDto;

import java.util.List;

public interface FinanceService {
    AccountAmtOutDto getMaxAmtCusInfo(String year);
    List<AccountOutDto> getNonTrxCusList(String year);
    List<BranchOutDto> getSumAmtBrList(String year);
    List<String> getAllYears();

    Branch findByBrName(String brNm);
    void modifyByBrCode(String bfBrCd, String toBrCd);
    BranchOutDto getSumAmtBrInfo(String brCd);

}
