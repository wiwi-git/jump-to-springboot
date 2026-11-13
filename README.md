# jump-to-springboot

『점프 투 스프링부트』를 AI와 함께 구현하고, 장희성 강사 수업(14163·14168)의 방식으로 개선하는 학습 저장소.

- 교재: https://wikidocs.net/book/7601
- 강사 수업: https://www.slog.gg/p/14163 · https://www.slog.gg/p/14168

## 진행 현황

### 1장 개발 준비

- [x] 1-02 개발 환경 준비 (프로젝트 생성)
- [x] 1-03 스프링부트 맛보기
- [x] 1-04 도구 설치

### 2장 기본 기능

- [x] 2-01 프로젝트 구조
- [x] 2-02 간단한 웹 프로그램
- [x] 2-03 JPA
  - [x] 개선: application.yml 전환, dev 프로필 H2 파일 DB(`./db_dev`, MySQL 모드), `.gitignore`에 DB 파일 추가
- [x] 2-04 엔티티
  - [x] 개선: `@ManyToOne(fetch = LAZY)`, `answerList`를 빈 리스트로 초기화
- [x] 2-05 리포지터리
  - [x] 개선: test 프로필 H2 메모리 DB로 테스트 DB 분리
  - [x] 개선: `QuestionRepositoryTest`로 단계별 테스트를 모두 남기고 `@Transactional` 롤백으로 서로 독립
- [ ] 2-06 도메인별 분류
- [ ] 2-07 질문 목록
- [ ] 2-08 루트 URL
- [ ] 2-09 서비스
- [ ] 2-10 상세 페이지
- [ ] 2-11 URL 프리픽스
- [ ] 2-12 답변 기능
- [ ] 2-13 웹 페이지 디자인
- [ ] 2-14 부트스트랩
- [ ] 2-15 표준 HTML 구조
- [ ] 2-16 질문 등록

### 3장 SBB 서비스 개발

- [ ] 3-01 내비게이션 바
- [ ] 3-02 페이징
- [ ] 3-03 게시물 번호
- [ ] 3-04 답변 개수
- [ ] 3-05 스프링 시큐리티
- [ ] 3-06 회원 가입
- [ ] 3-07 로그인/로그아웃
- [ ] 3-08 글쓴이
- [ ] 3-09 수정/삭제
- [ ] 3-10 추천
- [ ] 3-11 앵커
- [ ] 3-12 마크다운
- [ ] 3-13 검색
- [ ] 3-14 도전과제
  - [ ] 답변 페이징과 정렬
  - [ ] 댓글
  - [ ] 카테고리
  - [ ] 비밀번호 찾기와 변경
  - [ ] 프로필
  - [ ] 최근 답변과 최근 댓글
  - [ ] 조회 수
  - [ ] 소셜 로그인
  - [ ] 마크다운 에디터

## 작업내역

### 000 저장소 준비, [설정] 작업 규칙·용어집·진행 현황 작성
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/000)
- 작업: AI 작업 규칙, 용어집, 버전 결정 기록, 진행 현황표 작성
- 이유: 어느 세션의 AI든 같은 규칙과 같은 용어로 진행하도록
- 결과: GitHub 저장소 생성, 태그 000 링크 접속 확인

### 001 스프링부트 맛보기, 시작 [교재] 프로젝트 생성
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/001)
- 작업: start.spring.io로 프로젝트 생성 (Spring Boot 4.1.2, Java 25, Gradle Kotlin DSL, `com.mysite.sbb`, Spring Web)
- 이유: 교재 1-02의 sbb 프로젝트를 강사 환경 버전으로 준비 (ADR 0001)
- 결과: 기본 테스트 통과, 서버 실행 후 `localhost:8080`에서 404 Whitelabel 페이지 확인
- 교재와 차이: Boot 4에서 `spring-boot-starter-web` → `spring-boot-starter-webmvc`로 이름 변경

### 002 작업 방식 변경, [설정] 생성 단계와 이해 단계 분리
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/002)
- 작업: AGENTS.md 작업 루프를 생성 단계(AI가 3-14까지 커밋 생성)와 이해 단계(태그 체크아웃 후 질문·답변)로 변경
- 이유: 작업마다 승인하는 방식이 너무 느려서
- 결과: 다음 세션의 AI도 바뀐 방식을 따르도록 규칙 문서 갱신

### 003 스프링부트 맛보기, 완료 [교재] HelloController로 /hello 응답
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/003)
- 작업: `HelloController`에 `@GetMapping("/hello")` + `@ResponseBody`로 "Hello World" 문자열 반환
- 이유: 교재 1-03에서 URL 요청이 컨트롤러 메서드로 연결되는 가장 작은 예를 만들어 본다
- 결과: 테스트 통과, 서버 실행 후 `localhost:8080/hello` → 200, 본문 `Hello World`

### 004 도구 설치, 시작 [교재] devtools로 코드 변경 시 서버 자동 재시작
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/004)
- 작업: `developmentOnly("org.springframework.boot:spring-boot-devtools")` 추가, `/hello` 응답을 "Hello Spring Boot Board"로 변경
- 이유: 교재 1-04. 코드를 고칠 때마다 서버를 직접 껐다 켜지 않도록
- 결과: 서버 실행 중 응답 문자열을 고치고 컴파일만 하자 자동 재시작되어 `/hello`가 `Hello World` → `Hello Spring Boot Board`로 바뀜

### 005 도구 설치, [교재] 롬복으로 Getter/Setter 자동 생성
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/005)
- 작업: 롬복 의존성(`compileOnly` + `annotationProcessor`) 추가, `HelloLombok`에 `@Getter` `@Setter`
- 이유: 교재 1-04. 반복되는 getter/setter를 직접 쓰지 않고 컴파일 시점에 만들게 한다
- 결과: `HelloLombok.main` 실행 시 `헬로`, `5` 출력, `javap`로 getHello/setHello 등 메서드 생성 확인

### 006 도구 설치, 완료 [교재] @RequiredArgsConstructor로 final 필드 생성자 만들기
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/006)
- 작업: `HelloLombok` 필드를 `final`로 바꾸고 `@Setter` 대신 `@RequiredArgsConstructor`
- 이유: 교재 1-04. final 필드를 받는 생성자를 롬복이 만들게 한다 (뒤에서 의존성 주입에 이 방식을 쓴다)
- 결과: `new HelloLombok("헬로", 5)` 실행 시 `헬로`, `5` 출력, `javap`로 `HelloLombok(String, int)` 생성자 확인, setter와 기본 생성자는 사라짐

### 007 간단한 웹 프로그램, 완료 [교재] MainController로 /sbb 응답
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/007)
- 작업: `MainController`에 `@GetMapping("/sbb")` + `@ResponseBody`로 환영 문자열 반환. 코드 변경이 없는 2-01(프로젝트 구조)도 진행 현황에 체크
- 이유: 교재 2-02. 요청 → 컨트롤러 → 응답 흐름을 직접 만들어 본다
- 결과: `@ResponseBody` 없이 `void`로 두면 `/sbb` → 500, 문자열을 반환하게 고친 뒤 `/sbb` → 200, `안녕하세요 sbb에 오신 것을 환영합니다.`

### 008 JPA, 시작 [교재] H2 의존성과 데이터소스, H2 콘솔 설정
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/008)
- 작업: `com.h2database:h2`, `spring-boot-h2console` 의존성 추가, `application.properties`에 H2 콘솔과 `jdbc:h2:~/local` 데이터소스 설정
- 이유: 교재 2-03. 설치 없이 쓸 수 있는 파일 DB(H2)를 준비하고 웹 콘솔로 접속해 본다
- 결과: `~/local.mv.db` 생성(교재대로 `touch`), `/h2-console` 200, 콘솔 로그인 성공
- 교재와 차이: Boot 4는 H2 콘솔 자동 설정이 별도 모듈이라 `spring-boot-h2console`을 추가해야 `/h2-console`이 열린다

### 009 JPA, 완료 [교재] JPA 의존성과 설정
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/009)
- 작업: `spring-boot-starter-data-jpa` 추가, `application.properties`에 dialect와 `ddl-auto=update`
- 이유: 교재 2-03. SQL 대신 자바 객체로 DB를 다루는 JPA(구현체 Hibernate)를 연결한다
- 결과: 서버 시작 로그에서 Hibernate ORM 7.4, HikariPool `jdbc:h2:~/local` 연결, EntityManagerFactory 초기화 확인
- 교재와 차이: 없음. Hibernate 7은 H2Dialect를 직접 지정할 필요가 없다는 경고(HHH90000025)를 내지만 교재대로 둠

### 010 JPA, [개선] application.yml 전환과 dev 프로필 H2 파일 DB
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/010)
- 작업: `application.properties` → `application.yml`(공통) + `application-dev.yml`(개발 DB `jdbc:h2:./db_dev;MODE=MySQL`, H2 콘솔), `spring.profiles.active: dev`, `.gitignore`에 `*.mv.db` `*.trace.db`
- 이유: 강사 14163 5강 방식. 홈 디렉터리(`~/local`)가 아니라 프로젝트 안에 개발 DB를 두고, 환경(dev/test)별 설정을 파일로 나눌 준비. 계층 구조라 읽기 쉬운 YAML 사용
- 결과: 서버 로그 `The following 1 profile is active: "dev"`, 프로젝트 루트에 `db_dev.mv.db` 생성, git에는 안 잡힘, H2 콘솔 `jdbc:h2:./db_dev;MODE=MySQL` 로그인 성공

### 011 엔티티, 시작 [교재] Question 엔티티
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/011)
- 작업: `@Entity Question` (id 자동 증가, subject 200자, content TEXT, createDate)
- 이유: 교재 2-04. 질문 테이블을 SQL 대신 자바 클래스로 정의한다
- 결과: 서버 실행 후 개발 DB에 QUESTION 테이블 생성 (ID INTEGER identity, SUBJECT VARCHAR(200), CONTENT, CREATE_DATE TIMESTAMP)

### 012 엔티티, 완료 [교재] Answer 엔티티와 질문-답변 연관관계
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/012)
- 작업: `@Entity Answer` (`@ManyToOne Question question`), `Question.answerList`에 `@OneToMany(mappedBy = "question", cascade = REMOVE)`
- 이유: 교재 2-04. 답변 N개가 질문 1개에 속하는 관계를 객체 참조로 표현한다
- 결과: ANSWER 테이블에 QUESTION_ID 컬럼과 외래키 생성, QUESTION 테이블에는 답변용 컬럼이 생기지 않음(mappedBy 쪽은 컬럼 없음)

### 013 엔티티, [개선] @ManyToOne LAZY와 answerList 빈 리스트 초기화
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/013)
- 작업: `Answer.question`을 `@ManyToOne(fetch = FetchType.LAZY)`로, `Question.answerList`를 `new ArrayList<>()`로 초기화
- 이유: 강사 14163 25강·21강. `@ManyToOne` 기본값 EAGER는 답변을 읽을 때마다 질문까지 함께 조회한다. 필요할 때만 읽도록 LAZY가 기본 선택. 리스트를 초기화하면 새 질문에서 `getAnswerList()`가 null이 아니다
- 결과: 테스트 통과, 테이블 구조 변화 없음(조회 시점만 바뀜). LAZY 동작은 2-05 답변 조회에서 SQL 로그로 확인

### 014 리포지터리, 시작 [교재] 리포지터리와 질문 저장 테스트
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/014)
- 작업: `QuestionRepository`, `AnswerRepository`(`JpaRepository` 상속), SQL 로그 설정(`show_sql`, `format_sql`), `SbbApplicationTests.testJpa`에서 질문 2개 `save`
- 이유: 교재 2-05. 리포지터리 인터페이스만 만들면 스프링 데이터 JPA가 구현체를 만들어 준다. 테스트로 DB에 데이터를 넣어 본다
- 결과: 테스트 실행 1회 → 로그에 insert SQL 2번, 개발 DB `question`에 id 1·2 저장. 테스트가 개발 DB에 직접 데이터를 넣는다(다시 실행하면 같은 질문이 또 쌓인다)
- 교재와 차이: 교재가 추가하는 `junit-jupiter`, `junit-platform-launcher`는 Boot 4 프로젝트에 이미 있음(`spring-boot-starter-webmvc-test`에 JUnit 포함)

### 015 리포지터리, [교재] findAll·findById와 메서드 이름 기반 조회
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/015)
- 작업: `QuestionRepository`에 `findBySubject`, `findBySubjectAndContent`, `findBySubjectLike` 선언, 조회 테스트 5개
- 이유: 교재 2-05. 메서드 이름만 규칙대로 지으면 JPA가 where 조건 SQL을 만들어 준다
- 결과: 테스트 5개 통과, SQL 로그에 `where q1_0.subject=?`, `subject=? and content=?`, `subject like ?` 확인. `findAll` 크기 2는 014에서 1번만 저장했기 때문에 맞는 값

### 016 리포지터리, [교재] 질문 수정 테스트
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/016)
- 작업: `findById(1)`로 가져온 질문의 제목을 `setSubject("수정된 제목")` 후 `save`
- 이유: 교재 2-05. 이미 id가 있는 엔티티를 `save`하면 insert가 아니라 update가 실행된다
- 결과: 로그에 `update question set ... where id=?`, 개발 DB id 1 제목이 `수정된 제목`으로 바뀜. 앞 커밋의 조회 테스트는 이제 개발 DB 상태와 맞지 않아 교재처럼 교체

### 017 리포지터리, [교재] 질문 삭제 테스트
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/017)
- 작업: `count()`가 2인지 확인 → `findById(1)` 질문 `delete` → `count()`가 1인지 확인
- 이유: 교재 2-05. 리포지터리의 `delete`와 `count` 사용
- 결과: 1회 실행 통과, 로그에 `delete from question where id=?`, 개발 DB에 id 2만 남음. 이 테스트는 한 번 더 돌리면 `count()`가 1이라 실패한다(개발 DB 공유의 문제)

### 018 리포지터리, [교재] 답변 저장 테스트
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/018)
- 작업: `findById(2)` 질문을 `answer.setQuestion(q)`로 연결해 `AnswerRepository.save`
- 이유: 교재 2-05. 답변은 어떤 질문의 답변인지 `Question` 객체로 연결한다
- 결과: 1회 실행 통과, 개발 DB `answer`에 id 1, `QUESTION_ID` 2 저장

### 019 리포지터리, 완료 [교재] 답변 조회와 질문에 연결된 답변 목록, @Transactional
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/019)
- 작업: `testAnswerFind`(답변 1의 질문 id가 2), `testJpa`(질문 2의 `getAnswerList()` 1개) + `@Transactional`
- 이유: 교재 2-05. `answerList`는 필요할 때 읽는 LAZY 컬렉션이라, DB 세션(트랜잭션)이 열려 있어야 꺼낼 수 있다
- 결과: `@Transactional` 없이 실행 → `LazyInitializationException ... answerList (no session)` 실패, 붙인 뒤 2개 통과. 답변 조회 SQL은 answer만 읽고 question은 조인하지 않음(013의 LAZY 확인). 모든 기대값은 개발 DB에 쌓인 데이터(014~018 실행 결과)에 의존한다

### 020 리포지터리, [개선] test 프로필 H2 메모리 DB와 TestInitData로 테스트 DB 분리
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/020)
- 작업: `application-test.yml`(`jdbc:h2:mem:db_test;MODE=MySQL`), 테스트 클래스에 `@ActiveProfiles("test")`, test 프로필에서만 샘플 데이터(질문 2개, 2번 질문의 답변 1개)를 넣는 `TestInitData`
- 이유: 강사 14163 5강·14강. 교재 방식은 테스트가 개발 DB를 함께 써서 테스트가 개발 데이터를 바꾸고, 결과가 개발 DB 상태에 따라 달라진다(017 삭제 테스트는 두 번째 실행에서 실패). 테스트 DB는 테스트할 때만 만들어지고 끝나면 사라진다
- 결과: 테스트 로그 `profile is active: "test"`, `url=jdbc:h2:mem:db_test`, 2번 연속 실행 모두 통과. 실행 전후 개발 DB는 질문 1건·답변 1건 그대로

### 021 리포지터리, [개선] QuestionRepositoryTest와 @Transactional 롤백으로 독립적인 테스트
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/021)
- 작업: `SbbApplicationTests` → `QuestionRepositoryTest`로 이름 변경, 교재 2-05의 단계(조회 5개, 수정, 삭제, 답변 생성·조회·목록) 10개를 `@DisplayName` 붙은 메서드로 모두 남김, 클래스에 `@Transactional`, AssertJ `assertThat`
- 이유: 강사 14163 15~25강. 교재는 testJpa 하나를 지우고 고쳐 가며 써서 지난 단계가 남지 않는다. 테스트 메서드마다 트랜잭션을 열고 끝나면 롤백하면 수정·삭제·생성 테스트가 다른 테스트에 영향을 주지 않는다
- 결과: 10개 통과(2번 실행). "답변 생성"이 먼저 실행돼도 "답변 목록 조회"는 1개 → 롤백 확인. 클래스 `@Transactional`을 빼면 LAZY 목록 조회가 `LazyInitializationException`으로 실패
