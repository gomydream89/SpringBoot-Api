package kakaopay.kakaopaysec.domain;

import kakaopay.kakaopaysec.service.DataService;
import kakaopay.kakaopaysec.service.impl.FinanceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class RepositoryTest {
    @Autowired
    private DataService dataService;
    @Autowired
    private FinanceServiceImpl financeServiceImpl;

    @BeforeEach
    void initDatabase() throws IOException {
        dataService.saveFileToDatabase("data/데이터_관리점정보.csv");
        dataService.saveFileToDatabase("data/데이터_계좌정보.csv");
        dataService.saveFileToDatabase("data/데이터_거래내역.csv");
    }

    @Test
    public void test(){
        //given initDatabase()

    }





}
