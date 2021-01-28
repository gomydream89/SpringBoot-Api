## 카카오페이 서버개발 사전과제

> 특정 고객 거래내역 조회 서비스 개발
## 개발환경
- Java       11.0.9
- Gradle
- SpringBoot 2.4.2
- JPA
- H2 Database
- OpenCsv, Lombok, Etc

## 빌드 및 실행 방법
```
$ git clone https://github.com/gomydream89/kakaopay-sec.git
$ cd kakaopay-sec
$ ./gradlew clean build

$ java -jar build/libs/kakaopay-sec-0.0.1-SNAPSHOT.jar 
or
$ ./gradlew bootRun
```

### [API 테스트 페이지 바로가기](<http://localhost:8080/swagger-ui.html>)


## 요구 사항 및 문제해결 전략
- [x] Inmemory Database을 통한 테이블 생성 및 데이터 입력
    - H2 Inmemory DB와 Hibernate를 사용하여 테이블 생성
    - 어플리케이션 시작 시, 로컬의 CSV 파일을 읽어 DB에 로드
  
  
- [x] 연도별 합계 금액이 가장 많은 고객을 추출하는 API 개발
    - [거래내역]에서 해당 연도에 합계금액이 가장 많은 계좌 검색
      - 취소 거래 제외
      - 합계금액은 거래금액에서 수수료를 제외한 금액
    - 해당 계좌로 [계좌정보]에서 계좌명 검색 
  

- [x] 연도별 거래가 없는 고객을 추출하는 API
    - [거래내역]에서 해당 연도에 거래가 없는 계좌 검색
      - 취소 거래 포함
    - 해당 계좌로 [계좌정보]에서 계좌명 검색


- [x] 관리점 별 거래금액 합계를 구하고 합계 금액이 큰 순서로 출력하는 API 개발
  - [거래내역]와 [계좌정보]를 계좌번호로 Join하여 해당 연도에 관리점 별 거래금액 합계 목록 조회
    - 취소 거래 제외
    - 거래금액 합계 내림차순 정렬
  - 해당 관리점코드로 [관리점정보]에서 관리점명 검색
  

- [x] 지점명을 입력하면 해당 지점의 거래금액 합계를 출력하는 API 개발
    - 입력받은 지점명으로 [관리점정보]의 관리점코드 조회
    - 해당 지점이 관리하는 계좌번호의 [거래내역] 조회
      - 취소 거래 제외
    - [거래내역]에서 거래금액의 합계를 산출

## API 명세 및 결과 
### 1. 2018,2019년 각 연도별 합계 금액이 가장 많은 고객을 추출
`GET /apis/inquiry/maxSumAmt`

(1) 연도 입력이 없는 경우, Default 2018,2019년 설정
* Request
```json
-
```  
* Response

```json
[
  {
    "year": "2018",
    "name": "테드",
    "acctNo": "11111114",
    "sumAmt": 28992000
  },
  {
    "year": "2019",
    "name": "에이스",
    "acctNo": "11111112",
    "sumAmt": 40998400
  }
]
```
(2) 연도 입력 시, 해당 연도의 고객정보 및 합계금액 출력 가능
* Request
```json
http://localhost:8080/apis/inquiry/maxSumAmt?years=2018
```  
* Response

```json
[
  {
    "year": "2018",
    "name": "테드",
    "acctNo": "11111114",
    "sumAmt": 28992000
  }
]
```
### 2. 2018년 또는 2019년에 거래가 없는 고객을 추출
`GET /apis/inquiry/noTrx`

(1) 연도 입력이 없는 경우, Default 2018,2019년 설정
* Request
```json
-
```  
* Response

```json
[
  {
    "year": "2018",
    "name": "사라",
    "acctNo": "11111115"
  },
  {
    "year": "2018",
    "name": "에이스",
    "acctNo": "11111121"
  },
  {
    "year": "2019",
    "name": "테드",
    "acctNo": "11111114"
  },
  {
    "year": "2019",
    "name": "에이스",
    "acctNo": "11111121"
  }
]
```
(2) 연도 입력 시, 해당 연도의 거래가 없는 고객 목록 출력
* Request
```json
http://localhost:8080/apis/inquiry/noTrx?years=2019
```  
* Response

```json
[
  {
    "year": "2019",
    "name": "테드",
    "acctNo": "11111114"
  },
  {
    "year": "2019",
    "name": "에이스",
    "acctNo": "11111121"
  }
]
```

### 3. 연도 별 관리점 별 거래금액 합계를 구하고 합계 금액이 큰 순서로 출력
`GET /apis/inquiry/sumAmtAllBr`

거래내역이 존재하는 전체 연도에 대해 출력


* Request
```json
-
```  
* Response

```json
[
  {
    "year": "2018",
    "dataList": [
      {
        "brName": "판교점",
        "brCode": "A",
        "sumAmt": 59010000
      },
      {
        "brName": "강남점",
        "brCode": "C",
        "sumAmt": 20234567
      },
      {
        "brName": "잠실점",
        "brCode": "D",
        "sumAmt": 14000000
      }
    ]
  },
  {
    "year": "2019",
    "dataList": [
      {
        "brName": "판교점",
        "brCode": "A",
        "sumAmt": 112200000
      },
      {
        "brName": "강남점",
        "brCode": "C",
        "sumAmt": 19500000
      },
      {
        "brName": "잠실점",
        "brCode": "D",
        "sumAmt": 6000000
      }
    ]
  },
  {
    "year": "2020",
    "dataList": [
      {
        "brName": "을지로점",
        "brCode": "E",
        "sumAmt": 1000000
      }
    ]
  }
]
```
### 4-1. 관리점 이관 
`POST /apis/manage/mergeBr`

두 관리점을 입력 받아 통폐합을 수행
* Request
```json
{
  "bfBrName": "분당점",
  "toBrName": "판교점"
}
```  
* Response

```json
{
  "200": "분당점 is merged to 판교점"
}
```

### 4-2. 지점명을 입력하면 해당 지점의 거래금액 합계를 출력
`POST /apis/inquiry/sumAmtBr`

(1) 지점명을 입력 받아 해당 지점의 거래금액 합계 정보 출력
* Request
```json
{
  "brName": "판교점"
}
```  
* Response

```json
{
  "brName": "판교점",
  "brCode": "A",
  "sumAmt": 171210000
}
```

(2) 이관 완료 지점인 경우, 에러메시지 출력
* Request
```json
{
  "brName": "분당점"
}
```  
* Response

```json
{
  "404": "br code not found error"
}
```


