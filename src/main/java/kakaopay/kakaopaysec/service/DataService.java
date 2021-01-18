package kakaopay.kakaopaysec.service;

import com.opencsv.CSVReader;
import kakaopay.kakaopaysec.domain.entity.Account;
import kakaopay.kakaopaysec.domain.entity.Branch;
import kakaopay.kakaopaysec.domain.entity.Transaction;
import kakaopay.kakaopaysec.domain.entity.TransactionId;
import kakaopay.kakaopaysec.domain.repository.AccountRepository;
import kakaopay.kakaopaysec.domain.repository.BranchRepository;
import kakaopay.kakaopaysec.domain.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class DataService implements CommandLineRunner {
    private final BranchRepository branchRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Override
    public void run(String... args) throws Exception {
        saveFileToDatabase("data/데이터_관리점정보.csv");
        saveFileToDatabase("data/데이터_계좌정보.csv");
        saveFileToDatabase("data/데이터_거래내역.csv");
    }

    public void saveBranchInfo(String brCode, String brName) {
        Branch branch = new Branch(brCode, brName);
        branchRepository.save(branch);
    }

    public void saveAccountInfo(String acctNo, String acctNm, String brCode) {
        Account account = new Account(acctNo, acctNm, brCode);
        accountRepository.save(account);
    }

    public void saveTransactionInfo(TransactionId id, BigDecimal trxAmt, BigDecimal trxFee, String cancelYn) {
        Transaction transaction = new Transaction(id, trxAmt, trxFee, cancelYn);
        transactionRepository.save(transaction);
    }

    public void saveFileToDatabase(String filePath) throws IOException {
        // JAR 실행을 위한 인풋스트림 처리 및 인코딩 지정(UTF-8)
        ClassPathResource classPathResource = new ClassPathResource(filePath);
        Reader reader = new BufferedReader(new InputStreamReader(classPathResource.getInputStream(),"UTF-8"));

        CSVReader csvReader = new CSVReader(reader);

        String fileName = classPathResource.getFilename();
        String[] line;
        csvReader.readNext();

        // 파일명에 따라 각각의 Repository 호출하여 데이터 저장
        while ((line = csvReader.readNext()) != null) {
            switch (fileName) {
                case "데이터_관리점정보.csv":
                    saveBranchInfo(line[0], line[1]);
                    break;

                case "데이터_계좌정보.csv":
                    saveAccountInfo(line[0], line[1], line[2]);
                    break;

                case "데이터_거래내역.csv":
                    // 거래내역은 PK를 생성한 후 저장
                    TransactionId newId = TransactionId.builder()
                                            .trxDate(line[0])
                                            .acctNo(line[1])
                                            .trxNo(Integer.parseInt(line[2]))
                                            .build();

                    saveTransactionInfo(newId, new BigDecimal(line[3]),new BigDecimal(line[4]), line[5]);
                    break;
            }
        }
    }
}
