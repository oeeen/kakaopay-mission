# 사전과제 3 - 주택 금융 서비스 API 개발

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
2. csv 파일 업로드
3. 각 api 요청
    - `GET /institutes`
    - `GET /years`
    - `GET /maxfund`
    - `GET /average`

모든 API Test 는 Controller Test 를 통해 수행할 수 있습니다.

## 개발 프레임워크

- SpringBoot 2.2.2
- Spring Data JPA
- H2

## 문제 해결 전략

### 요구 사항

- 데이터 파일에서 각 레코드를 데이터베이스에 저장하는 API 개발
  - 메인 페이지에서 csv 파일을 받아 parsing 하여 Fund Entity 로 저장
- 주택 금융 공급 금융기관(은행) 목록을 출력하는 API 를 개발하세요.
  - `GET /institutes` 요청 시 은행 목록 출력
- 년도별 각 금융기관의 지원금액 합계를 출력하는 API 를 개발하세요.
  - `GET /years` 요청 시 년도별 각 금융기관 지원금액 합계 출력
  - jpql 로 Fund 내에 있는 Year 전체 조회
  - jpql 로 Institute 로 group by 후 년도 별로 따로 조회 할 수 있도록 구현
- 각 년도 별 각 기관의 전체 지원 금액 중에서 가장 큰 금액의 기관명을 출력하는 API 개발
  - 예를들어, 2005 년 ~ 2017 년 중에 2010 년 국민은행의 전체 지원금액(1 월 ~ 12 월 지원 합계)이 가장 높았다면 { “year": “2010” , "bank": “국민은행”}을 결과로 출력합니다.
    - `GET /maxfund` 요청 시 가장 큰 금액의 년도, 기관명을 출력
    - 년도별 각 금융기관의 지원금액 합계 출력하기 위한 jpql 재사용하여 구현
- 전체 년도(2005 ~ 2016)에서 외환은행의 지원금액 평균 중에서 가장 작은 금액과 큰 금액을 출력하는 API 개발
  - 예를들어, 2005 년 ~ 2016 년 외환은행의 평균 지원금액 (매년 12 달의 지원금액 평균값)을 계산하여 가장 작은 값과 큰 값을 출력합니다. 소수점 이하는 반올림해서 계산하세요.
    - `GET /average` 요청 시 외환은행의 평균 지원 금액을 계산하여 최소값, 최대값을 출력합니다.
    - 지원 금액 합계에서 사용한 jpql 메서드를 재활용 하여 년 단위 합계를 구한 후 최소, 최대값 계산
    - 구해진 최소, 최대값을 12로 나누어 평균 최소, 최대값 계산 후 출력

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

### 선택 문제(옵션)

- 특정 은행의 특정 달에 대해서 2018년도 해당 달에 금융지원 금액을 예측하는 API 개발
  - 단, 예측 알고리즘을 무엇을 써야하는지에 대한 제약은 없지만, 가장 근사치에 가까울 수록 높은 점수 부여

## 추가 제약 사항

- API 인증을 위해 JWT(Json Web Token)를 이용해서 Token 기반 API 인증 기능을 개발하고 각 API 호출 시에 HTTP Header 에 발급받은 토큰을 가지고 호출
- signup 계정생성 API: 입력으로 ID, PW 받아 내부 DB 에 계정 저장하고 토큰 생성하여 출력
- 단, 패스워드는 인코딩하여 저장한다.
- 단, 토큰은 특정 secret 으로 서명하여 생성한다.
- signin 로그인 API: 입력으로 생성된 계정 (ID, PW)으로 로그인 요청하면 토큰을 발급한다.
- refresh 토큰 재발급 API: 기존에 발급받은 토큰을 Authorization 헤더에 `Bearer Token`으로 입력 요청을 하면 토큰을 재발급한다.
