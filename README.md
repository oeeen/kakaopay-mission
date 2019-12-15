# 사전과제 3 - 주택 금융 서비스 API 개발

**모든 API Test 는 Controller Test 를 통해 수행할 수 있습니다.**

## 빌드 및 실행 방법

git과 java가 설치되어 있어야 합니다.

터미널에서 다음 명령을 실행합니다.

```shell script
#!/bin/bash
$ git clone https://github.com/oeeen/kakaopay-mission.git
$ cd kakaopay-mission
$ ./gradlew clean build
$ java -jar build/libs/kakaopay-0.0.1-SNAPSHOT.jar
```

https://localhost:8080 에 접속하여 csv 파일을 업로드하여야 데이터베이스에 해당 데이터가 들어갑니다.

1. https://localhost:8080
2. csv 파일 업로드 - 현재 프로젝트에서는 인증 없이 가능하도록 구현
3. 회원가입/로그인 요청
   - `POST /api/signup`
   - `POST /api/signin`
   - SignIn 결과로 나오는 Token 값을 가지고 Authorization Bearer Token에 넣은 후에 이후 요청 가능 (인증 확인)
4. 각 api 요청 (인증 없이 불가능)
   - `GET /institutes`
   - `GET /years`
   - `GET /maxfund`
   - `GET /average`

## 개발 프레임워크

- SpringBoot 2.2.2
- Spring Data JPA
- H2
- JJWT [https://github.com/jwtk/jjwt](https://github.com/jwtk/jjwt)

## 문제 해결 전략

### 데이터 파일에서 각 레코드를 데이터베이스에 저장하는 API 개발

- opencsv 사용하여 csv 파일 파싱 후 entity에 저장
- `POST /amount` 요청으로 들어온 csv 파일을 opencsv 이용하여 파싱 후 Institute, Fund 엔티티로 매핑

### Request

```request
POST /amount HTTP/1.1
Content-Type: application/x-www-form-urlencoded
```

- Request 에 File 전송(csv)을 해야합니다.

### Response

```response
{
  "AffectedRows": 1386
}
```

전체 저장한 목록을 응답할 수도 있었지만, 저장한 총 row수만 응답하도록 구현

### 주택 금융 공급 금융기관(은행) 목록을 출력하는 API 를 개발

- `GET /institutes` 요청 시 은행 목록 출력
- JPA의 findAll() 메서드 사용
- Institute 엔티티 중 필요한 필드만 갖는 InstituteResponseDto 생성 후 반환

### Request

```request
GET /institute HTTP/1.1
Authorization: Bearer Token
```

### Response

```response
[
    {
        "name": "주택도시기금",
        "code": "bnk001"
    },
    {
        "name": "국민은행",
        "code": "bnk002"
    },
    {
        "name": "우리은행",
        "code": "bnk003"
    },
    {
        "name": "신한은행",
        "code": "bnk004"
    },
    {
        "name": "한국시티은행",
        "code": "bnk005"
    },
    {
        "name": "하나은행",
        "code": "bnk006"
    },
    {
        "name": "농협은행/수협은행",
        "code": "bnk007"
    },
    {
        "name": "외환은행",
        "code": "bnk008"
    },
    {
        "name": "기타은행",
        "code": "bnk999"
    }
]
```

### 년도별 각 금융기관의 지원금액 합계를 출력하는 API 를 개발

- `GET /years` 요청 시 년도별 각 금융기관 지원금액 합계 출력

```java
@Query("SELECT f.institute, SUM(f.amount) FROM Fund f WHERE f.year=?1 GROUP BY f.institute")
List<Object> findSumByYearGroupByInstitute(Year year);

@Query("SELECT DISTINCT(f.year) FROM Fund f")
List<Year> findDistinctYear();
```

- `findDistinctYear()`로 Year Fund 내의 Year 조회 (2005~2017)
- `findSumByYearGroupByInstitute(Year year)` 로 Institute 로 group by 후 년도 별로 따로 조회 할 수 있도록 구현

먼저 Fund 내의 Year을 조회 한 후 (2005~2017) 해당 년도별 합계를 조회

### Request

```request
GET /years HTTP/1.1
Authorization: Bearer Token
```

### Response

```response
[
    {
        "year": "2005",
        "totalAmount": 48016,
        "detailAmount": {
            "하나은행": 3122,
            "농협은행/수협은행": 1486,
            "우리은행": 2303,
            "국민은행": 13231,
            "신한은행": 1815,
            "주택도시기금": 22247,
            "외환은행": 1732,
            "한국시티은행": 704,
            "기타은행": 1376
        }
    },
    ...
    {
        "year": "2017",
        "totalAmount": 295126,
        "detailAmount": {
            "하나은행": 35629,
            "농협은행/수협은행": 26969,
            "우리은행": 38846,
            "국민은행": 31480,
            "신한은행": 40729,
            "주택도시기금": 85409,
            "외환은행": 0,
            "한국시티은행": 7,
            "기타은행": 36057
        }
    }
]
```

### 각 년도 별 각 기관의 전체 지원 금액 중에서 가장 큰 금액의 기관명을 출력하는 API 개발

- `GET /maxfund` 요청 시 가장 큰 금액의 년도, 기관명을 출력
- 년도별 각 금융기관의 지원금액 합계 출력하기 위한 jpql 재사용하여 구현

위 요구사항에서 구현한 `findSumByYearGroupByInstitute(Year year)` 사용하여 지원 금액의 합계를 구한 후 그 중 가장 큰 금액을 가진 기관명을 출력 하도록 구현

### Request

```request
GET /maxfund HTTP/1.1
Authorization: Bearer Token
```

### Response

```response
{
    "year": "2014",
    "instituteName": "주택도시기금"
}
```

### 전체 년도(2005 ~ 2016)에서 외환은행의 지원금액 평균 중에서 가장 작은 금액과 큰 금액을 출력하는 API 개발

- `GET /average` 요청 시 외환은행의 평균 지원 금액을 계산하여 최소값, 최대값을 출력
- `findSumByYearGroupByInstitute(Year year)` 사용하여 년 단위 합계를 구한 후 최소, 최대값 계산
- 구해진 최소, 최대값을 12로 나누어 평균 최소, 최대값 계산 후 출력

### Request

```request
GET /average HTTP/1.1
Authorization: Bearer Token
```

### Response

```response
{
    "minimum": {
        "year": "2017",
        "amount": 0
    },
    "maximum": {
        "year": "2015",
        "amount": 1702
    }
}
```

### 특정 은행의 특정 달에 대해서 2018년도 해당 달에 금융지원 금액을 예측하는 API 개발

- `implementation 'org.apache.commons:commons-math3:3.6.1'` 라이브러리의 선형회귀로 예측
- JPA의 `List<Fund> findAllByInstituteOrderByYearAscMonthAsc(Institute institute);`로 특정 은행의 모든 지원 금액을 조회(Year, Month로 오름차순)
- 조회한 `List<Fund>`에서 시간 순(x축)으로 지원금액(Amount, y축)을 그래프를 그립니다.(`simpleRegression.addData(x, y)`)
- 예측을 원하는 2018년도의 Month에 맞는 값을 넣어줍니다. (`(int)simpleRegression.predict(fundsOfInstitute.size() + predictRequestDto.getMonth().getValue() + 1);`)
- 나온 결과 값을 Dto로 포장 후 Controller로 넘겨줍니다.

### Request

```request
GET /predict HTTP/1.1
Authorization: Bearer Token
```

- Request의 parameter는 instituteName="국민은행", month=2 와 같은 식으로 넘깁니다. 그 결과는 아래와 같습니다.

### Response

```response
{
    "instituteName": "국민은행",
    "year": "2018",
    "month": "FEBRUARY",
    "amount": {
        "amount": 4849
    }
}
```

### JWT 이용 인증/인가

- API 인증을 위해 JWT(Json Web Token)를 이용해서 Token 기반 API 인증 기능을 개발하고 각 API 호출 시에 HTTP Header 에 발급받은 토큰을 가지고 호출
  - jjwt 라이브러리 사용
- signup 계정생성 API: 입력으로 ID, PW 받아 내부 DB 에 계정 저장하고 토큰 생성하여 출력
  - 회원 가입시 존재하는 아이디 인지 확인 후 DB에 저장, ID 이용하여 토큰 생성하여 return
- 단, 패스워드는 인코딩하여 저장한다.
  - BCrypt 사용하여 PasswordEncrytors 클래스 구현
- 단, 토큰은 특정 secret 으로 서명하여 생성한다.
  - jwt 서명 secret은 `kakaopay_secret` 로 고정
- signin 로그인 API: 입력으로 생성된 계정 (ID, PW)으로 로그인 요청하면 토큰을 발급한다.
  - 로그인 시 response header에 새로운 토큰 발급
- refresh 토큰 재발급 API: 기존에 발급받은 토큰을 Authorization 헤더에 `Bearer Token`으로 입력 요청을 하면 토큰을 재발급한다.
  - Interceptor에서 Header 중 Authorization 의 값에 Bearer가 포함 되어 있으면 토큰을 재발급 하도록 구현
  - 기존 토큰 이용하여 ID를 다시 구한 후 해당 ID를 이용해 토큰 재발급

JwpService의 `public String generateToken(String userId)` 메서드에서 jjwt 라이브러리 사용하여 JWT 생성

`public String refreshToken(String headerValue)` 에서는 response의 header로부터 Authorization의 value를 받아와서 Bearer를 제외한 후 실제 Token 값을 이용해 userId 생성 후 해당 아이디 기반으로 다시 토큰을 재발급 합니다.

### Entity

- Entity
  - Fund
    - Long id (PK)
    - Year year
    - Month month
    - Institute institute
      - ManyToOne mapping
    - Amount amount
      - Integer 값 wrapping 한 Vo Class
      - 데이터 중 "," 들어간 경우 "," 제거하는 로직
  - Institute
    - Long id (PK)
    - String name
      - 이름을 가지고 InstituteCode 에서 일치하는 code 를 반환 받아 내부의 code 도 생성
    - String code
      - InstituteCode 에 임의로 설정된 code 값을 매칭 시켜 사용
