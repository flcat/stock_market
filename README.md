# 주식 거래 프로젝트

주식 거래 프로젝트는 해외 주식의 실시간 주가 정보와 거래 기능을 제공하는 웹 애플리케이션입니다. Python 스크립트를 사용하여 주식 데이터를 가져오고, Spring Boot를 이용하여 데이터를 보여줍니다.

## 기능

- 실시간 주가 업데이트
- 주식 검색 및 필터링
- 주식 데이터 페이지네이션
- 해외 주식 주문 기능
- 계좌 잔고 및 거래 내역 확인
- Slack 통합을 통한 알림 수신

## 기술 스택

- Java 17
- Spring Boot 2.7.x
- Python 3.8+
- PostgreSQL
- Thymeleaf
- Slack API

## 시스템 요구사항

- Java Development Kit (JDK) 17 이상
- Python 3.8 이상
- PostgreSQL 데이터베이스
- 필요한 Python 라이브러리: `pandas`, `yfinance`, `requests`

## 설치 및 설정

1. 저장소 클론: `git clone https://github.com/flcat/stock_market.git`
2. 프로젝트 디렉터리로 이동: `cd stock_market`
3. Python 의존성 설치: `pip install -r requirements.txt`
4. Spring Boot 애플리케이션 빌드: `./mvnw clean package`
5. PostgreSQL 데이터베이스 설정 및 `application.properties`에 구성 업데이트
6. `application.properties`에 Slack API 자격 증명 구성
7. Spring Boot 애플리케이션 실행: `java -jar target/stock_market-0.0.1-SNAPSHOT.jar`
8. `http://localhost:8080`에서 애플리케이션 접속

## API 문서

주식 거래 프로젝트는 다음과 같은 API 엔드포인트를 제공합니다:

- `GET /api/price/{ticker}`: 주어진 티커 심볼에 대한 실시간 주가 정보 가져오기
- `POST /api/order/{id}`: 해외 주식 주문 요청

자세한 API 문서, 요청/응답 형식 및 인증 세부 정보는 [API 문서](https://apiportal.koreainvestment.com/about)를 참조하세요.

## 프로젝트 구조

![image](https://github.com/flcat/stock_market/assets/16348278/34d10176-4dfb-46b1-bbce-e7f847b500cb)

- `config/`: 외부 서비스에 대한 구성 클래스
- `controller/`: HTTP 요청 처리를 위한 Spring MVC 컨트롤러
- `dto/`: 요청/응답 페이로드를 위한 데이터 전송 객체
- `entity/`: 데이터베이스 매핑을 위한 JPA 엔티티
- `exception/`: 커스텀 예외 클래스
- `repository/`: 데이터베이스 액세스를 위한 Spring Data 리포지토리
- `service/`: 비즈니스 로직 서비스
- `util/`: 유틸리티 클래스
- `vo/`: 외부 API 응답을 위한 값 객체
- `static/`: 정적 리소스(CSS, JavaScript)
- `templates/`: 뷰 렌더링을 위한 Thymeleaf 템플릿
- `stock_scripts/`: 주식 데이터 가져오기를 위한 Python 스크립트
    - `nasdaq100_data_crawling.py`: 나스닥 100 지수 주식 데이터를 크롤링하는 Python 스크립트
- `python_workspace/stock_list/`: 주식 데이터 CSV 파일이 저장되는 디렉터리

## 기여

기여는 언제나 환영합니다! 프로젝트에 기여하려면 다음 단계를 따라주세요!

1. 저장소 포크
2. 새 브랜치 생성: `git checkout -b feature/your-feature-name`
3. 변경 사항 커밋: `git commit -m 'Add some feature'`
4. 브랜치 푸시: `git push origin feature/your-feature-name`
5. 풀 리퀘스트 열기

코드가 [Java 코드 규약](https://google.github.io/styleguide/javaguide.html)과 [Python 스타일 가이드](https://google.github.io/styleguide/pyguide.html)를 준수하는지 확인해주세요!

## 라이센스

이 프로젝트는 [MIT 라이센스](LICENSE)에 따라 라이센스가 부여됩니다.

## 추가 리소스

- [Spring Boot 문서](https://spring.io/projects/spring-boot)
- [Python 문서](https://docs.python.org/3/)
- [Pandas 문서](https://pandas.pydata.org/docs/)
- [yfinance 문서](https://pypi.org/project/yfinance/)

## 문제 해결 및 FAQ

### Q: Slack 통합을 어떻게 구성하나요?
A: `application.properties`에서 `slack.token`과 `slack.channel` 속성에 Slack API 토큰과 채널 ID를 각각 업데이트합니다.

### Q: 새로운 주식 데이터 소스를 어떻게 추가하나요?
A: `stock_scripts/` 디렉터리에 새 Python 스크립트를 만들고, `MarketPriceService`를 업데이트하여 새 스크립트를 실행하도록 합니다.

## 스크린샷

<!--![주가 페이지](screenshots/stock-price.png)
![주문 페이지](screenshots/order.png)
(프로젝트의 UI와 기능을 보여주는 관련 스크린샷 또는 GIF 추가)
--!>
