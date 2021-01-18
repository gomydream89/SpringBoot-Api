package kakaopay.kakaopaysec.controller;

import io.swagger.annotations.ApiOperation;
import kakaopay.kakaopaysec.domain.entity.Branch;
import kakaopay.kakaopaysec.dto.*;
import kakaopay.kakaopaysec.service.FinanceService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
public class FinanceCortroller {
    private static Logger logger = LoggerFactory.getLogger(FinanceCortroller.class);
    private static final HttpHeaders httpHeaders = new HttpHeaders();
    private final FinanceService financeService;

    // 문제에서 제시된 연도 : Default(2018,2019)
    private final List<String> defaultYearList = new ArrayList<String>(
            Arrays.asList("2018","2019"));


    @GetMapping("/apis/inquiry/maxSumAmt")
    @ApiOperation(value="1번 과제에 해당하는 API", notes="연도 별 합계 금액이 가장 많은 고객을 추출하는 API")
    public ResponseEntity getMaxAmtCusList(@RequestParam(value="years", required=false) List<String> years) {
        // 1번 과제 : 각 연도별 합계 금액이 많은 고객 추출 API
        try{
            List<AccountAmtOutDto> customerList = new ArrayList<AccountAmtOutDto>();

            // Param 없는 경우 Default(2018,2019) 설정
            if( years == null)
                years = defaultYearList;

            for (String year : years){
                AccountAmtOutDto customerInfo = financeService.getMaxAmtCusInfo(year);
                if(!Objects.isNull(customerInfo.getYear()))
                    customerList.add(customerInfo);
            }
            
            if(customerList.size() > 0)
                return new ResponseEntity<>(customerList, httpHeaders, HttpStatus.OK);
            else
                return new ResponseEntity<>(Collections.singletonMap("404", "data not found error"), httpHeaders, HttpStatus.NOT_FOUND);
        }catch(Exception e){
            logger.error(e.toString());
            return new ResponseEntity<>(Collections.singletonMap("error","INTERNAL SERVER ERROR"),httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/apis/inquiry/noTrx")
    @ApiOperation(value="2번 과제에 해당하는 API", notes="연도에 거래가 없는 고객을 추출하는 API")
    public ResponseEntity getNonTrxCusList(@RequestParam(value="years", required=false) List<String> years) {
        // 2번 과제 : 해당연도에 거래가 없는 고객 목록을 추출하는 API
        try{
            List<AccountOutDto> customerList = new ArrayList<AccountOutDto>();

            // Param 없는 경우 Default(2018,2019) 설정
            if( years == null)
                years = defaultYearList;

            for (String year : years){
                List<AccountOutDto> list = financeService.getNonTrxCusList(year);
                if(list.size() > 0)
                    customerList.addAll(list);
            }

            if(customerList.size() > 0)
                return new ResponseEntity<>(customerList, httpHeaders, HttpStatus.OK);
            else
                return new ResponseEntity<>(Collections.singletonMap("404", "data not found error"), httpHeaders, HttpStatus.NOT_FOUND);
        }catch(Exception e){
            logger.error(e.toString());
            return new ResponseEntity<>(Collections.singletonMap("error","INTERNAL SERVER ERROR"),httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/apis/inquiry/sumAmtAllBr")
    @ApiOperation(value="3번 과제에 해당하는 API", notes="연도별 관리점별 거래금액을 합한 후 합계금액이 큰 순서로 출력하는 API")
    public ResponseEntity getSumAmtBrList() {
        //3번 과제 : 연도별 관리점별 거래금액을 합한 후 합계금액이 큰 순서로 출력하는 API
        try{
            List<Map<String, Object>> branchList = new ArrayList<Map<String, Object>>();
            Map<String, Object> branch;

            // [거래내역]에서 정상인 거래들의 연도 목록을 조회
            List<String> years = financeService.getAllYears();
            for (String year : years) {
                branch = new LinkedHashMap<String, Object>();

                //해당 연도에 관리점 별 거래금액 합계 목록 조회
                List<BranchOutDto> list = financeService.getSumAmtBrList(year);

                if(list.size() >0){
                    branch.put("year"       , year);
                    branch.put("dataList"   , list);
                    branchList.add(branch);
                }
            }

            if(branchList.size() > 0)
                return new ResponseEntity<>(branchList, httpHeaders, HttpStatus.OK);
            else
                return new ResponseEntity<>(Collections.singletonMap("404", "data not found error"), httpHeaders, HttpStatus.NOT_FOUND);
        }catch(Exception e){
            logger.error(e.toString());
            return new ResponseEntity<>(Collections.singletonMap("error","INTERNAL SERVER ERROR"),httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/apis/manage/mergeBr")
    @ApiOperation(value="4번 과제 전, 관리점 통폐합을 수행하는 API", notes="두 관리점 명을 입력 받아, 통폐합을 수행하는 API\n* bfBrName : 통합대상지점 | toBrName : 이관대상지점")
    public ResponseEntity mergeBranch(@RequestBody BranchMergeInDto branchInDto) {
        // 4번 과제를 위한 관리점 통폐합 API : bfBrName=분당점 -> toBrName=판교점
        try{
            if(!Objects.isNull(branchInDto.getBfBrName())
                && !Objects.isNull(branchInDto.getToBrName())){

                Branch bfBrInfo = financeService.findByBrName(branchInDto.getBfBrName());
                Branch toBrInfo = financeService.findByBrName(branchInDto.getToBrName());

                String  bfBrCd;
                String  toBrCd;

                if(Objects.isNull(bfBrInfo))
                    return new ResponseEntity<>(Collections.singletonMap("404", "enter the exact name of the as-is branch"), httpHeaders, HttpStatus.NOT_FOUND);
                else
                    bfBrCd = bfBrInfo.getBrCd();    //이전 관리점코드

                if(Objects.isNull(toBrInfo))
                    return new ResponseEntity<>(Collections.singletonMap("404", "enter the exact name of the to-be branch"), httpHeaders, HttpStatus.NOT_FOUND);
                else
                    toBrCd = toBrInfo.getBrCd();    //이관 관리점코드

                financeService.modifyByBrCode(bfBrCd, toBrCd);
                return new ResponseEntity<>(Collections.singletonMap("200", branchInDto.getBfBrName() + " is merged to " + branchInDto.getToBrName()), httpHeaders, HttpStatus.OK);
            }else
                return new ResponseEntity<>(Collections.singletonMap("404", "enter the name of each branch"), httpHeaders, HttpStatus.NOT_FOUND);
        }catch(Exception e){
            logger.error(e.toString());
            return new ResponseEntity<>(Collections.singletonMap("error","INTERNAL SERVER ERROR"),httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/apis/inquiry/sumAmtBr")
    @ApiOperation(value="4번 과제에 해당하는 API", notes="입력한 관리점의 거래금액을 합계하여 출력하는 API")
    public ResponseEntity updateData(@RequestBody BranchInDto branchInDto) {
        //4번 과제 : 입력한 해당 관리점의 거래금액 합계를 출력하는 API
        try{
            if(!Objects.isNull(branchInDto.getBrName())){
                String  brCd = financeService.findByBrName(branchInDto.getBrName()).getBrCd();
                BranchOutDto brInfo = financeService.getSumAmtBrInfo(brCd);

                if(!Objects.isNull(brInfo.getBrName()))
                    return new ResponseEntity<>(brInfo, httpHeaders, HttpStatus.OK);
                else
                    return new ResponseEntity<>(Collections.singletonMap("404", "br code not found error"), httpHeaders, HttpStatus.NOT_FOUND);
            }else
                return new ResponseEntity<>(Collections.singletonMap("404", "enter the name of branch"), httpHeaders, HttpStatus.NOT_FOUND);
        }catch(Exception e){
            logger.error(e.toString());
            return new ResponseEntity<>(Collections.singletonMap("error","INTERNAL SERVER ERROR"),httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
