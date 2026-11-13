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
  - [x] 개선: `Question.addAnswer`와 `cascade = PERSIST`로 질문 쪽에서 답변 생성
  - [x] 개선: SQL 로그에 바인딩 값·조회 값·트랜잭션 시작/종료 출력
- [x] 2-06 도메인별 분류
- [x] 2-07 질문 목록
  - [x] 개선: 타임리프 캐시는 dev 프로필에서만 끄기
  - [x] 개선: 개발 DB가 비어 있으면 `TestInitData` 샘플 데이터 생성
- [x] 2-08 루트 URL
- [x] 2-09 서비스
- [x] 2-10 상세 페이지
  - [x] 개선: 상세 페이지 200과 없는 질문 404를 MockMvc 테스트로 확인
- [x] 2-11 URL 프리픽스
- [x] 2-12 답변 기능
  - [x] 개선: 답변 생성을 `Question.addAnswer`와 `@Transactional`로 (`answerRepository.save` 대신)
- [x] 2-13 웹 페이지 디자인
- [x] 2-14 부트스트랩
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
- [ ] 3-14 도전과제 (작업자가 요청할 때 진행)
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

### 022 리포지터리, [개선] Question.addAnswer와 cascade PERSIST로 답변 생성
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/022)
- 작업: `Question.addAnswer(content)`(답변 생성 + 양쪽 연결 + `answerList.add`), `cascade = {PERSIST, REMOVE}`, `TestInitData`가 `q2.addAnswer(...)` 후 질문만 save, 테스트 "답변 생성 by OneToMany"
- 이유: 강사 14163 21~22강. 교재는 답변을 만들 때 `answer.setQuestion(q)` 후 `AnswerRepository.save`를 따로 한다. 질문 객체의 `answerList`에는 반영되지 않아 메모리 속 객체와 DB가 어긋난다. 질문(애그리거트의 중심)에서 답변을 추가하면 양쪽이 함께 맞춰지고, 저장은 cascade가 처리한다
- 결과: 11개 통과. `PERSIST`를 빼면 TestInitData의 답변이 저장되지 않아 답변 관련 테스트 3개 실패

### 023 리포지터리, [개선] SQL 로그에 바인딩 값과 트랜잭션 출력
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/023)
- 작업: `use_sql_comments: true`, 로그 레벨 `org.hibernate.orm.jdbc.bind`·`extract`·`org.springframework.transaction.interceptor`를 TRACE
- 이유: 강사 14163 6강. 교재 설정(`show_sql`)은 SQL을 `?`로만 보여 준다. 실제로 어떤 값이 들어가고 나왔는지, 트랜잭션이 언제 열리고 닫히는지 봐야 JPA 동작을 이해하기 쉽다
- 결과: 테스트 로그에 `binding parameter (1:VARCHAR) <- [sbb에 대해서 알고 싶습니다.]`, `extracted value (1:INTEGER) -> [1]`, `Getting/Completing transaction for [...SimpleJpaRepository.count]`

### 024 도메인별 분류, 완료 [교재] question·answer 패키지로 나누기
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/024)
- 작업: `Question`·`QuestionRepository` → `com.mysite.sbb.question`, `Answer`·`AnswerRepository` → `com.mysite.sbb.answer`, 사용하는 곳에 import 추가
- 이유: 교재 2-06. 파일이 늘어도 기능(도메인) 단위로 찾을 수 있게 패키지를 나눈다
- 결과: 테스트 11개 통과, 서버 정상 기동, 개발 DB 테이블(QUESTION, ANSWER) 그대로

### 025 질문 목록, 시작 [교재] QuestionController와 /question/list 매핑
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/025)
- 작업: `question` 패키지에 `QuestionController`, `/question/list`에서 `@ResponseBody`로 "question list" 반환
- 이유: 교재 2-07. 질문 목록 화면을 만들기 전에 URL 매핑부터 확인한다
- 결과: `/question/list` → 200, 본문 `question list`

### 026 질문 목록, [교재] 타임리프 의존성과 question_list 템플릿
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/026)
- 작업: `spring-boot-starter-thymeleaf`, `thymeleaf-layout-dialect` 추가, `templates/question_list.html`(`Hello Template`), 컨트롤러가 `@ResponseBody` 없이 `"question_list"` 반환
- 이유: 교재 2-07. 자바 코드에서 HTML 문자열을 만들지 않고 템플릿 파일로 화면을 그린다
- 결과: `/question/list` → 200, 본문 `<h2>Hello Template</h2>`. layout-dialect는 Boot 4 의존성 관리로 4.0.1이 들어옴

### 027 질문 목록, 완료 [교재] 리포지터리로 조회한 질문 목록을 템플릿에 표시
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/027)
- 작업: `QuestionController`에 `@RequiredArgsConstructor`로 `QuestionRepository` 생성자 주입, `findAll()` 결과를 `Model`에 `questionList`로 담음, 템플릿에서 `th:each`로 제목·작성일시 표 출력
- 이유: 교재 2-07. 컨트롤러는 데이터를 모델에 담고, 화면 모양은 템플릿이 정한다
- 결과: `/question/list` → 200, 표에 개발 DB의 질문 1건(`스프링부트 모델 질문입니다.`, 2-05 테스트가 남긴 데이터) 표시

### 028 질문 목록, [개선] 타임리프 캐시는 dev 프로필에서만 끄기
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/028)
- 작업: `application-dev.yml`에 `spring.thymeleaf.cache: false`
- 이유: 강사 14163 28~29강. 템플릿 캐시가 켜져 있으면 템플릿을 고쳐도 서버를 다시 켜야 반영된다. 개발 중에는 끄고, 운영(그 밖의 프로필)에서는 기본값(켜짐)으로 성능을 지킨다. devtools도 개발 실행 때 캐시를 꺼 주지만, 설정 파일에 명시해 두면 devtools 없이도 같은 동작이고 의도가 보인다
- 결과: 서버 실행 중 빌드된 템플릿의 `<th>제목</th>`을 고치자 재시작 없이(Started 로그 1회) 다음 요청에 `<th>제목(수정)</th>` 반영

### 029 질문 목록, [개선] 개발 DB가 비어 있을 때도 샘플 데이터 생성
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/029)
- 작업: `TestInitData`의 `@Profile("test")` 제거 → dev에서도 앱 시작 시 질문이 하나도 없으면 샘플 질문 2개·답변 1개 생성
- 이유: 강사 14163 27강. 교재는 개발 DB 데이터를 2-05 테스트로 넣었는데, 020에서 테스트를 테스트 DB로 분리했으니 개발 DB를 채울 방법이 없다. 새로 clone하면 질문 목록이 빈 표다
- 결과: 개발 DB 파일을 치우고 실행 → 목록에 `sbb가 무엇인가요?`, `스프링부트 모델 질문입니다.` 2행. 데이터가 있는 기존 개발 DB에서는 아무것도 추가하지 않음(1행 그대로)

### 030 루트 URL, 완료 [교재] / 접속 시 질문 목록으로 리다이렉트
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/030)
- 작업: `MainController`에 `@GetMapping("/")` → `"redirect:/question/list"`
- 이유: 교재 2-08. 사이트 첫 주소(`/`)로 들어오면 질문 목록을 보여 준다
- 결과: `/` → 302, `Location: /question/list`, 따라가면 200 질문 목록 표

### 031 범위 변경, [설정] 생성 단계를 3-13까지로 축소
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/031)
- 작업: AGENTS.md 범위와 생성 단계를 3-13 검색까지로 변경, 진행 현황의 3-14에 "요청할 때 진행" 표시
- 이유: 3-14 도전과제는 작업자가 따로 요청할 때 진행하기로 함
- 결과: 규칙 문서와 진행 현황에 바뀐 범위 반영

### 032 서비스, 완료 [교재] QuestionService로 질문 목록 조회 옮기기
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/032)
- 작업: `question` 패키지에 `@Service QuestionService`(`getList()`), `QuestionController`는 리포지터리 대신 서비스를 주입받아 호출
- 이유: 교재 2-09. 컨트롤러가 리포지터리를 직접 쓰지 않고 서비스를 거치게 해서, 여러 컨트롤러가 같은 데이터 처리를 재사용하고 엔티티 다루는 일을 서비스에 모은다
- 결과: 테스트 통과(11개), `/question/list` → 200, 화면은 그대로(개발 DB 질문 1행)

### 033 상세 페이지, 시작 [교재] 질문 목록 제목에 상세 페이지 링크
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/033)
- 작업: `question_list.html`의 제목을 `<a th:href="@{|/question/detail/${question.id}|}">`로 감쌈
- 이유: 교재 2-10. 목록에서 제목을 누르면 그 질문의 상세 페이지로 가게 한다. `@{...}`는 URL을 만들고, `|...|`는 문자열 안에 `${}` 값을 끼워 넣는다
- 결과: 목록에 `<a href="/question/detail/2">` 링크 생성. 아직 매핑이 없어 `/question/detail/2` → 404

### 034 상세 페이지, 완료 [교재] 질문 상세 매핑과 없는 질문은 404
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/034)
- 작업: `/question/detail/{id}` 매핑(`@PathVariable`), `QuestionService.getQuestion(id)`, 없는 id면 `DataNotFoundException`(`@ResponseStatus(NOT_FOUND)`), `question_detail.html`에 제목·내용 출력
- 이유: 교재 2-10. URL 경로의 값을 `@PathVariable`로 받아 질문을 조회한다. 질문이 없을 때 `Optional.get()`의 500(`No value present`) 대신 404와 `question not found`라는 뜻이 분명한 예외를 낸다
- 결과: `/question/detail/2` → 200, 제목·내용 표시. `/question/detail/999` → 404, `message: entity not found`, 예외 `DataNotFoundException: question not found`

### 035 상세 페이지, [개선] 상세 페이지와 없는 질문 404를 MockMvc 테스트로 고정
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/035)
- 작업: `QuestionControllerTest`(`@SpringBootTest` + `@AutoConfigureMockMvc`, test 프로필): 있는 질문 → 200·`question_detail`·제목 포함, 없는 질문 → 404·`DataNotFoundException("question not found")`
- 이유: 강사 14163 35강. 강사는 `.get()`(500, `No value present`)과 커스텀 예외(404, `question not found`)를 나란히 보여 주며 고객과 개발자 모두에게 친절한 쪽을 고른다. 그 차이가 나중에 리팩터링으로 사라지지 않도록, 서버를 띄워 curl로 보던 확인을 테스트로 남긴다
- 결과: 테스트 13개 통과(기존 11 + 2). 브라우저(`Accept: text/html`)로 `/question/detail/999` → 404 Whitelabel 페이지에 `entity not found`, `DataNotFoundException: question not found`

### 036 URL 프리픽스, 완료 [교재] QuestionController에 @RequestMapping("/question")
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/036)
- 작업: `QuestionController` 클래스에 `@RequestMapping("/question")`, 메서드 매핑은 `/list`, `/detail/{id}`로 줄임
- 이유: 교재 2-11. 이 컨트롤러의 URL은 모두 `/question`으로 시작하므로 공통 부분을 클래스에 한 번만 적는다
- 결과: 테스트 13개 통과, `/` 302, `/question/list` 200, `/question/detail/2` 200, `/question/detail/999` 404 (URL은 그대로)

### 037 답변 기능, 시작 [교재] 상세 페이지에 답변 등록 폼
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/037)
- 작업: `question_detail.html`에 `<form th:action="@{|/answer/create/${question.id}|}" method="post">`, `textarea name="content"`, 답변등록 버튼
- 이유: 교재 2-12. 답변 내용을 `POST /answer/create/{질문id}`로 보낼 입력 폼을 먼저 만든다. 폼 필드의 `name`이 서버에서 받을 파라미터 이름이 된다
- 결과: 상세 페이지에 `action="/answer/create/2"` 폼 표시. 아직 처리 컨트롤러가 없어 POST → 404

### 038 답변 기능, [교재] AnswerController와 AnswerService로 답변 저장
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/038)
- 작업: `AnswerController`(`@RequestMapping("/answer")`, `@PostMapping("/create/{id}")`, `@RequestParam("content")`), `AnswerService.create(question, content)`가 `Answer`를 만들어 `answerRepository.save`, 저장 후 상세 페이지로 리다이렉트
- 이유: 교재 2-12. 폼이 보낸 `content` 파라미터를 `@RequestParam`으로 받고, 답변 저장은 서비스에 맡긴다. 저장 뒤 리다이렉트하면 새로고침해도 POST가 다시 전송되지 않는다
- 결과: `POST /answer/create/2` → 302 `/question/detail/2`, 개발 DB `answer`에 2번 질문의 답변이 한 줄 추가됨(확인 후 개발 DB 원래대로 되돌림). 없는 질문 999 → 404

### 039 답변 기능, 완료 [교재] 상세 페이지에 답변 개수와 목록 표시
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/039)
- 작업: `question_detail.html`에 `${#lists.size(question.answerList)}개의 답변이 있습니다.`와 `th:each`로 답변 내용 `<li>` 목록
- 이유: 교재 2-12. 질문 엔티티의 `answerList`로 그 질문에 달린 답변을 바로 꺼낸다(답변 리포지터리를 따로 부르지 않음). `#lists.size`는 타임리프 유틸리티
- 결과: 답변 등록 → 리다이렉트된 상세 페이지에 `2개의 답변이 있습니다.`, 기존 답변과 새 답변 `<li>` 2개(확인 후 개발 DB 원래대로)

### 040 답변 기능, [개선] 답변 생성을 Question.addAnswer로
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/040)
- 작업: `AnswerService.create`를 `@Transactional` + `question.addAnswer(content)`로 변경(`answerRepository.save` 호출 없음), `AnswerControllerTest`(등록 → 302·답변 1건 증가, 없는 질문 → 404) 추가
- 이유: 강사 14163 37강. 강사는 `answerRepository.save` 대신 `@OneToMany` 필드로 답변을 만든다. 2-05 개선(016)에서 만든 `addAnswer`를 실제 기능에서도 쓰면 질문과 답변 목록이 메모리에서도 함께 맞춰지고, 저장은 트랜잭션 종료 때 cascade PERSIST가 한다. URL은 교재대로 `/answer/create/{id}` 유지(HTML 폼은 GET·POST만 보낼 수 있어서, 강사가 보여 준 RESTful 예시 `POST /questions/5/answers`는 3장 이후 교재 흐름과 맞지 않음)
- 결과: 테스트 15개 통과. 실제 실행에서 `AnswerService.create` 트랜잭션 완료 시점에 insert SQL, 상세 페이지 답변 2개(확인 후 개발 DB 원래대로)

### 041 웹 페이지 디자인, 완료 [교재] static/style.css를 상세 페이지에 적용
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/041)
- 작업: `static/style.css`(textarea 너비 100%, 제출 버튼 위 여백), `question_detail.html` 첫 줄에 `<link th:href="@{/style.css}">`
- 이유: 교재 2-13. `src/main/resources/static`의 파일은 컨트롤러 없이 `/파일명`으로 바로 제공된다. 화면 모양(CSS)을 템플릿과 분리한다
- 결과: 상세 페이지에 `<link href="/style.css">`, `/style.css` → 200 `text/css`

### 042 부트스트랩, 시작 [교재] bootstrap.min.css 추가와 질문 목록 꾸미기
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/042)
- 작업: `static/bootstrap.min.css`(Bootstrap 5.3.2, 교재 저장소 파일), `question_list.html`을 `container`·`table`·`table-dark`로 꾸미고 번호 칸(`loop.count`), 작성일시는 `#temporals.format(..., "yyyy-MM-dd HH:mm")`
- 이유: 교재 2-14. 부트스트랩의 클래스만 붙여 CSS를 직접 쓰지 않고 화면을 꾸민다. 반복 상태 변수(`loop`)로 번호를 매기고, 날짜는 읽기 좋은 형식으로 바꾼다
- 결과: `/bootstrap.min.css` → 200 `text/css`(232,948바이트), 목록에 번호 `1`, 작성일시 `2026-11-13 12:32`

### 043 부트스트랩, 완료 [교재] 질문 상세를 카드 레이아웃으로 꾸미기
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/043)
- 작업: `question_detail.html`을 부트스트랩으로 변경: 질문·답변을 `card`로, 작성일시 배지(`#temporals.format`), 내용은 `white-space: pre-line`, 폼은 `form-control`·`btn btn-primary`. `style.css` 링크는 빠짐
- 이유: 교재 2-14. 상세 화면도 목록과 같은 부트스트랩 스타일로 맞춘다. `pre-line`은 내용의 줄바꿈을 화면에 그대로 보여 준다
- 결과: 상세 페이지에 `card my-3` 2개(질문, 답변 1개), 날짜 `2026-11-13 12:32`, `btn btn-primary` 답변등록 버튼
