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
  - [x] 개선: 부트스트랩을 cdnjs(5.3.8)에서 불러오기
  - [x] 개선: 프리텐다드 웹폰트 적용
- [x] 2-15 표준 HTML 구조
  - [x] 개선: 템플릿에 `xmlns:th`·`xmlns:layout` 선언 (IDE 경고 제거)
  - [x] 개선: `layout:title-pattern`으로 페이지별 제목
- [x] 2-16 질문 등록
  - [x] 개선: `@NotEmpty` → `@NotBlank`, 질문·답변 등록 검증 테스트
  - [x] 개선: 폼 클래스를 `record`로 (setter 없는 불변 객체)

### 3장 SBB 서비스 개발

- [x] 3-01 내비게이션 바
- [x] 3-02 페이징
- [x] 3-03 게시물 번호
- [x] 3-04 답변 개수
- [x] 3-05 스프링 시큐리티
  - [x] 개선: CSRF 토큰 자동 삽입과 토큰 없는 POST 403을 테스트로 확인
- [x] 3-06 회원 가입
  - [x] 개선: `UserCreateForm`을 `record`와 `@NotBlank`로, 회원가입 MockMvc 테스트
- [x] 3-07 로그인/로그아웃
  - [x] 개선: 로그아웃을 CSRF 토큰이 있는 POST 폼으로, 로그인·로그아웃 테스트
- [x] 3-08 글쓴이
  - [x] 개선: 글쓴이 저장과 비로그인 등록 차단을 MockMvc 테스트로 확인
- [x] 3-09 수정/삭제
  - [x] 개선: 삭제를 CSRF 토큰이 있는 DELETE 요청으로(hiddenmethod 필터), 수정·삭제 권한 테스트
  - [x] 개선: 답변 삭제를 `Question.deleteAnswer`와 `orphanRemoval`로
- [x] 3-10 추천
  - [x] 개선: 추천을 CSRF 토큰이 있는 POST 요청으로, 추천 테스트
- [x] 3-11 앵커
- [x] 3-12 마크다운
- [x] 3-13 검색
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

### 044 부트스트랩, [개선] 부트스트랩을 cdnjs에서 불러오기
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/044)
- 작업: `static/bootstrap.min.css`(5.3.2) 삭제, 목록·상세 템플릿이 `https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.8/css/bootstrap.min.css`를 링크
- 이유: 강사 14163 39강. 프론트엔드 라이브러리는 내려받아 저장소에 넣지 않고 cdnjs URL로 쓴다. 23만 바이트짜리 압축 파일을 커밋하지 않아도 되고, 버전은 URL만 바꿔 올린다
- 결과: 테스트 15개 통과, 두 화면 모두 cdnjs 링크, cdnjs URL → 200 `text/css`(Bootstrap v5.3.8), `/bootstrap.min.css` → 404(로컬 파일 제거)

### 045 부트스트랩, [개선] 프리텐다드 폰트 적용
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/045)
- 작업: `style.css`에 Pretendard `@font-face`(100~900, jsDelivr woff2)와 `html > body { font-family: Pretendard }` 추가(교재의 textarea·submit 규칙은 유지), 목록·상세 템플릿에 `style.css` 링크
- 이유: 강사 14163 39강의 커스터마이징. 부트스트랩 기본 글꼴은 OS마다 달라 한글 화면이 제각각이다. 한글용 웹폰트를 지정해 어느 환경에서나 같은 모양으로 보이게 한다
- 결과: 브라우저에서 상세 페이지 `body` 글꼴 `Pretendard`, 400·500·700 굵기 로드 확인, 부트스트랩 버튼 색(rgb(13,110,253)) 정상

### 046 표준 HTML 구조, 시작 [교재] layout.html과 질문 목록에 레이아웃 적용
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/046)
- 작업: `templates/layout.html`(doctype·head·body, 부트스트랩·style.css 링크, `<th:block layout:fragment="content">`), `question_list.html`을 `<html layout:decorate="~{layout}">` + `<div layout:fragment="content">`로 변경
- 이유: 교재 2-15. 지금까지 템플릿은 `<html>`·`<head>`·`<body>`가 없는 조각이었다. 공통 뼈대를 `layout.html` 하나에 두고, 각 페이지는 바뀌는 본문만 채운다(thymeleaf-layout-dialect, 026에서 추가)
- 결과: `/question/list` 응답이 `<!doctype html>`로 시작, head에 cdnjs 부트스트랩·`/style.css`, 제목 `Hello, sbb!`, 목록 3칸 그대로. 교재와 차이: 레이아웃의 부트스트랩도 044 개선대로 cdnjs 링크

### 047 표준 HTML 구조, 완료 [교재] 질문 상세에 레이아웃 적용
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/047)
- 작업: `question_detail.html`도 `layout:decorate="~{layout}"` + `layout:fragment="content"`로 바꾸고 개별 CSS 링크 제거
- 이유: 교재 2-15. 상세 페이지도 공통 레이아웃을 쓰면 head(CSS, 제목)를 고칠 때 layout.html 한 곳만 바꾸면 된다
- 결과: `/question/detail/2` 응답이 `<!doctype html>`, `<title>Hello, sbb!</title>`, `/style.css` 링크 1개, 질문·답변 카드 그대로

### 048 표준 HTML 구조, [개선] 템플릿에 th·layout 네임스페이스 선언
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/048)
- 작업: `layout.html`, `question_list.html`, `question_detail.html`의 `<html>`에 `xmlns:th="http://www.thymeleaf.org"`, `xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout"`
- 이유: 강사 14168 44강. 선언이 없으면 IDE가 `layout:decorate`, `th:text`마다 `Namespace 'layout' is not bound` 경고를 띄우고 자동완성도 안 된다. 실행에는 영향이 없고, 렌더링 결과에서는 타임리프가 선언을 지운다
- 결과: 테스트 15개 통과, 목록·상세 200, 응답의 `<html lang="ko">`에 xmlns 속성이 남지 않음

### 049 표준 HTML 구조, [개선] 페이지마다 브라우저 탭 제목 지정
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/049)
- 작업: `layout.html`의 `<title>`을 `layout:title-pattern="$CONTENT_TITLE | $LAYOUT_TITLE"`과 기본 제목 `SBB`로, 목록은 `<head><title>질문 목록</title></head>`, 상세는 질문 제목(`th:text="${question.subject}"`)
- 이유: 강사 14168 44강 레이아웃. 교재 레이아웃은 모든 페이지 제목이 `Hello, sbb!`라 탭·방문 기록에서 페이지를 구분할 수 없다. 페이지는 자기 제목만 적고, 레이아웃이 사이트 이름을 붙인다
- 결과: 목록 탭 제목 `질문 목록 | SBB`, 상세 `스프링부트 모델 질문입니다. | SBB`. 제목이 없는 페이지는 `SBB`만 나온다

### 050 질문 등록, 시작 [교재] 질문 등록 버튼·폼과 QuestionService.create
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/050)
- 작업: 목록 아래 `질문 등록하기` 버튼, `GET /question/create` → `question_form.html`(제목 input, 내용 textarea), `POST /question/create`에서 `@RequestParam`으로 받아 `QuestionService.create` 후 목록으로 리다이렉트
- 이유: 교재 2-16. 같은 URL을 GET(폼 보여 주기)과 POST(폼 처리)로 나눠 매핑한다. 메서드 이름이 같아도 매개변수가 달라 오버로딩된다
- 결과: 폼 등록 → 302 `/question/list`, 목록에 새 질문 행 추가. 빈 제목·내용도 그대로 저장됨(검증은 다음 커밋). 확인 후 개발 DB 원래대로

### 051 질문 등록, [교재] QuestionForm과 @Valid로 입력 검증
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/051)
- 작업: `spring-boot-starter-validation` 추가, `QuestionForm`(`@NotEmpty`, `@Size(max = 200)`), POST는 `@Valid QuestionForm` + `BindingResult`로 오류 시 폼을 다시 보여 줌, 템플릿은 `th:object`·`th:field`와 `#fields.allErrors()` 오류 상자
- 이유: 교재 2-16. 요청 파라미터를 폼 객체 하나로 받고, 애너테이션으로 검증 규칙을 선언한다. `th:field`가 입력값을 다시 채워 주므로 검증에 실패해도 쓴 내용이 남는다
- 결과: 빈 제목·내용 → 200, 폼에 `제목은 필수항목입니다.`, `내용은 필수항목입니다.` 오류 표시. 제목만 쓰면 `value="제목만 입력"` 유지. 정상 입력은 302로 저장. 공백만 넣은 제목은 통과됨(@NotEmpty의 한계). 확인 후 개발 DB 원래대로

### 052 질문 등록, 완료 [교재] form_errors 공통 조각과 답변 입력 검증
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/052)
- 작업: `templates/form_errors.html`(`th:fragment="formErrorsFragment"`)로 오류 상자를 분리해 질문 폼·답변 폼에서 `th:replace`, `AnswerForm`(`@NotEmpty`), 답변 등록도 `@Valid AnswerForm` + `BindingResult`, 상세 GET에 `AnswerForm` 매개변수
- 이유: 교재 2-16. 같은 오류 표시 코드를 템플릿마다 복사하지 않고 조각(fragment)으로 재사용한다. 답변도 빈 내용으로 저장되지 않게 질문과 같은 방식으로 검증한다
- 결과: 빈 답변 POST → 상세 화면 재표시, `내용은 필수항목입니다.` 오류 상자. 빈 질문 POST → 공통 조각으로 같은 오류 상자. 정상 답변 → 302, 답변 2개. 확인 후 개발 DB 원래대로

### 053 질문 등록, [개선] 공백만 입력한 제목·내용도 거부(@NotBlank)
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/053)
- 작업: `QuestionForm`, `AnswerForm`의 `@NotEmpty`를 `@NotBlank`로, 질문 등록·답변 등록 MockMvc 테스트 추가(정상 저장, 공백만 입력 시 거부, 제목 201자 거부)
- 이유: 강사 14168은 폼 검증에 `@NotBlank`를 쓴다. `@NotEmpty`는 길이 0만 막아서 051 검증에서 `"   "` 제목이 그대로 저장되었다. `@NotBlank`는 공백을 뺀 내용이 없으면 거부한다
- 결과: 테스트 19개 통과. `@NotEmpty`로 잠시 되돌리면 공백 입력 테스트가 실패하는 것도 확인. 실제 실행에서 공백만 보낸 질문 → 폼에 두 오류, 목록 1행 그대로

### 054 질문 등록, [개선] QuestionForm·AnswerForm을 record로
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/054)
- 작업: 두 폼을 `@Getter @Setter` 클래스에서 `record`로 바꾸고, 컨트롤러는 `questionForm.subject()`·`answerForm.content()`로 읽음. 폼 GET과 입력값 유지 테스트(t6) 추가
- 이유: 강사 14168 40강. 폼은 요청 데이터를 담아 전달만 하는 객체라 setter가 필요 없다. record는 생성자·접근자·equals/hashCode/toString을 만들어 주고 필드가 final이라 코드가 짧고 바뀌지 않는다. 스프링은 record를 생성자 바인딩으로 만든다
- 결과: 테스트 20개 통과. 실제 실행에서 빈 폼 `value=""`, 검증 실패 시 `value="제목만 입력"` 유지, 정상 등록 302, 빈 답변 오류 표시(확인 후 개발 DB 원래대로)

### 055 내비게이션 바, 완료 [교재] navbar 조각과 부트스트랩 JS
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/055)
- 작업: `templates/navbar.html`(`th:fragment="navbarFragment"`, SBB 로고·로그인 링크·햄버거 버튼), `layout.html` body 맨 위에 `<nav th:replace="~{navbar :: navbarFragment}">`, body 끝에 부트스트랩 JS
- 이유: 교재 3-01. 모든 페이지 위에 같은 메뉴를 두려면 레이아웃에 한 번만 넣으면 된다. 좁은 화면에서 햄버거 버튼으로 메뉴를 펼치는 동작(collapse)은 부트스트랩 JS가 처리한다
- 결과: 목록·상세 모두 `<nav class="navbar ...">`와 `SBB`, `로그인` 링크 포함. 교재와 차이: 부트스트랩 JS도 044 개선대로 cdnjs 5.3.8에서 불러옴(로컬 `bootstrap.min.js` 없음)

### 056 페이징, 시작 [교재] 개발 DB에 테스트 데이터 300개 생성
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/056)
- 작업: `TestInitData`에서 dev 프로필이면 `questionService.create`로 `테스트 데이터입니다:[001]`~`[300]` 질문 300개 생성(이미 있으면 건너뜀)
- 이유: 교재 3-02. 페이징이 필요하다는 것을 보려면 목록에 질문이 많아야 한다. 교재는 테스트 메서드로 개발 DB에 300개를 넣는다
- 결과: 테스트 20개 통과(test 프로필은 데이터 그대로), 서버 실행 후 목록에 질문 301행(기존 1 + 300), 다시 실행해도 300개가 더 생기지 않음. 교재와 차이: 테스트는 메모리 DB(test 프로필)를 쓰므로, 개발 DB에 넣으려고 `TestInitData`(dev 프로필)에서 만든다

### 057 페이징, [교재] Page와 PageRequest로 한 페이지에 10개씩 조회
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/057)
- 작업: `QuestionRepository.findAll(Pageable)`, `QuestionService.getList(int page)`는 `PageRequest.of(page, 10, Sort.by(createDate 내림차순))`, 컨트롤러는 `@RequestParam(value = "page", defaultValue = "0") int page`로 받아 모델에 `paging`(Page)을 담음, 템플릿은 `${paging}`을 반복
- 이유: 교재 3-02. 전체를 가져오지 않고 DB에서 필요한 10개만 가져온다. `Page`는 목록과 함께 전체 개수·페이지 수·이전/다음 여부를 담고 있어 페이지 이동 링크를 만들 수 있다
- 결과: `/question/list` 최신 10개(`[300]`~`[291]`), `?page=1`은 `[290]`~`[281]`, `?page=30`은 1개, `?page=31`은 0개. SQL 로그에 `order by create_date desc offset ? rows fetch first ? rows only`와 `count(q1_0.id)` 쿼리

### 058 페이징, 완료 [교재] 이전·다음과 페이지 번호 링크
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/058)
- 작업: `question_list.html` 표 아래에 부트스트랩 `pagination`. 이전/다음은 `paging.hasPrevious`·`hasNext`가 false면 `disabled`, 페이지 번호는 `#numbers.sequence(0, totalPages-1)` 중 현재 페이지 앞뒤 5개만, 현재 페이지는 `active`
- 이유: 교재 3-02. 페이지 번호를 URL에 직접 쓰지 않고 링크로 이동한다. 페이지가 31개라 모두 보여 주면 너무 길어서 현재 페이지 주변만 보여 준다
- 결과: 첫 페이지는 `이전` disabled, 0~5, 0이 active. `?page=10`은 5~15, `?page=30`(마지막)은 25~30과 `다음` disabled. 빈 페이지(`?page=31`)는 페이징 영역 없음

### 059 게시물 번호, 완료 [교재] 전체 개수 기준으로 번호 매기기
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/059)
- 작업: 목록 번호를 `${loop.count}` 대신 `${paging.getTotalElements - (paging.number * paging.size) - loop.index}`로
- 이유: 교재 3-03. `loop.count`는 페이지마다 1부터 다시 시작한다. 최신 글이 가장 큰 번호를 갖도록 "전체 개수 - 앞 페이지들의 개수 - 이 페이지 안의 순서"로 계산한다
- 결과: 첫 페이지 301~292, `?page=1`은 291~282, 마지막 페이지(`?page=30`)는 1

### 060 답변 개수, 완료 [교재] 질문 제목 옆에 답변 개수 표시
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/060)
- 작업: 목록 제목 링크 뒤에 `<span class="text-danger small ms-2">`로 `${#lists.size(question.answerList)}`, 답변이 있을 때만(`th:if > 0`)
- 이유: 교재 3-04. 목록에서 어떤 질문에 답변이 달렸는지 바로 보이게 한다
- 결과: `?page=30`의 "스프링부트 모델 질문입니다." 옆에 빨간 `1`, 답변 없는 첫 페이지는 표시 없음. SQL 로그상 목록의 질문마다 답변 조회 쿼리가 1번씩 나감(첫 페이지 10번)

### 061 스프링 시큐리티, 시작 [교재] 시큐리티 의존성과 모든 요청 허용 설정
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/061)
- 작업: `spring-boot-starter-security`, `thymeleaf-extras-springsecurity6` 추가, `SecurityConfig`(`@EnableWebSecurity`, `SecurityFilterChain` 빈)에서 `requestMatchers("/**").permitAll()`. 테스트에 `spring-boot-starter-security-test`를 추가하고 MockMvc POST에 `.with(csrf())`
- 이유: 교재 3-05. 시큐리티를 넣기만 하면 모든 요청에 로그인을 요구하므로, 아직 로그인 기능이 없는 지금은 모든 URL을 허용한다. 시큐리티는 컨트롤러 앞의 필터에서 동작하고, 허용한 URL이라도 POST에는 CSRF 토큰을 검사한다
- 결과: 테스트 20개 통과(csrf 추가 전에는 POST 테스트 7개가 403으로 실패). 서버에서 목록 200, 질문 폼에 `th:action` 덕분에 `<input type="hidden" name="_csrf">`가 자동으로 들어감, 토큰 없이 POST하면 403, 토큰과 함께 POST하면 302. H2 콘솔 로그인(POST)은 403, 응답 헤더 `X-Frame-Options: DENY`(다음 커밋에서 해결). 교재와 차이: Security 7에서 `AntPathRequestMatcher`가 없어져 문자열 `"/**"`로 지정

### 062 스프링 시큐리티, 완료 [교재] H2 콘솔의 CSRF 예외와 같은 출처 프레임 허용
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/062)
- 작업: `SecurityConfig`에 `csrf.ignoringRequestMatchers("/h2-console/**")`, `headers`에 `XFrameOptionsHeaderWriter(SAMEORIGIN)`
- 이유: 교재 3-05. H2 콘솔은 스프링이 만든 폼이 아니라 CSRF 토큰을 보내지 못해 로그인 POST가 403이 된다. 또 콘솔 화면은 frame으로 이루어져 있는데 시큐리티 기본값(`X-Frame-Options: DENY`)은 프레임 안에 페이지를 띄우지 못하게 막는다
- 결과: H2 로그인 POST `/h2-console/login.do` 403 → 200(header·tables·query 프레임), 응답 헤더 `X-Frame-Options: SAMEORIGIN`. SBB 폼은 여전히 토큰 없이 POST하면 403. 교재와 차이: `AntPathRequestMatcher` 대신 문자열 `"/h2-console/**"`

### 063 스프링 시큐리티, [개선] CSRF 보호를 테스트로 확인
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/063)
- 작업: `QuestionControllerTest`에 t7(질문 폼에 `<input type="hidden" name="_csrf">`가 있음), t8(토큰 없이 POST → 403, 컨트롤러 핸들러 없음, 질문 수 그대로)
- 이유: 강사 14168 46강. 시큐리티를 넣기 전에는 다른 사이트의 폼이 우리 서버로 POST를 보내도 처리되었고(CSRF 공격), 시큐리티를 넣으면 토큰 없는 POST는 필터에서 막힌다. 폼은 `th:action`을 써야 토큰이 자동으로 들어간다. 이 두 가지가 깨지면 화면만으로는 알기 어려워 테스트로 남긴다
- 결과: 테스트 22개 통과. CSRF를 잠시 `csrf.disable()`로 바꾸면 t7·t8이 실패하는 것도 확인

### 064 회원 가입, 시작 [교재] SiteUser 엔티티와 UserRepository
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/064)
- 작업: `com.mysite.sbb.user` 패키지에 `SiteUser`(id, `@Column(unique = true)` username·email, password)와 `UserRepository extends JpaRepository<SiteUser, Long>`
- 이유: 교재 3-06. 회원 정보를 저장할 테이블이 필요하다. 사용자 이름과 이메일은 중복되면 안 되므로 DB 유니크 제약을 건다. `User`는 시큐리티의 클래스 이름과 겹쳐서 `SiteUser`로 짓는다
- 결과: 서버 실행 시 `create table site_user (id bigint ..., email, password, username)`와 username·email의 `unique` 제약 추가 SQL 확인. 확인 후 개발 DB 원래대로

### 065 회원 가입, [교재] UserService와 BCrypt 비밀번호 암호화
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/065)
- 작업: `SecurityConfig`에 `PasswordEncoder` 빈(`BCryptPasswordEncoder`), `UserService.create(username, email, password)`는 `passwordEncoder.encode(password)`로 암호화해 저장
- 이유: 교재 3-06. 비밀번호를 그대로 저장하면 DB가 유출될 때 모두 드러난다. BCrypt 해시로 저장하고, 암호화 방식은 빈 하나로 정해 두어 바꿀 때 한 곳만 고친다
- 결과: 테스트 22개 통과, 서버가 `PasswordEncoder` 빈을 주입받아 정상 시작(화면 연결은 다음 커밋)

### 066 회원 가입, 완료 [교재] 회원가입 폼·컨트롤러와 내비게이션 링크
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/066)
- 작업: `UserCreateForm`(username `@Size(3~25)`, password1·password2, email `@Email`, 모두 `@NotEmpty`), `UserController`(`GET/POST /user/signup`), `signup_form.html`, navbar에 `회원가입` 링크
- 이유: 교재 3-06. 질문 등록과 같은 폼 검증에 더해, 두 비밀번호가 다르면 `bindingResult.rejectValue`로 필드 오류를, DB 유니크 제약에 걸리면(`DataIntegrityViolationException`) `bindingResult.reject`로 전역 오류를 직접 넣어 같은 오류 상자에 보여 준다
- 결과: 빈 값·짧은 ID·잘못된 이메일·비밀번호 불일치·중복 가입은 폼에 오류, 정상 가입은 302 `/`, DB에 `$2a$10$...`(BCrypt 60자)로 저장. 공백만 입력한 ID·비밀번호도 가입됨(@NotEmpty의 한계, 053과 같은 문제). 교재와 차이: `signup_form.html`에 048·049처럼 xmlns와 `<title>회원가입</title>`. 확인 후 개발 DB 원래대로

### 067 회원 가입, [개선] UserCreateForm을 record와 @NotBlank로, 회원가입 테스트
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/067)
- 작업: `UserCreateForm`을 `record`로 바꾸고 `@NotEmpty` → `@NotBlank`, 컨트롤러는 `userCreateForm.password1()` 등 접근자로 읽음. `UserControllerTest` 추가(정상 가입·암호화, 공백 입력, 비밀번호 불일치, 중복 ID)
- 이유: 053·054와 같은 근거(강사 14168 13~16강 `@NotBlank`, 40강 record 폼). 066 확인에서 공백 `"   "` ID·비밀번호로 가입이 되었다. 폼은 요청 값을 담아 전달만 하므로 setter가 필요 없다
- 결과: 테스트 26개 통과. 실제 실행에서 공백 입력은 필수항목 오류, 비밀번호 불일치 때 ID·이메일 값 유지, 정상 가입 302. 확인 후 개발 DB 원래대로

### 068 로그인/로그아웃, 시작 [교재] 로그인 화면과 formLogin 설정
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/068)
- 작업: `SecurityConfig`에 `.formLogin(loginPage("/user/login").defaultSuccessUrl("/"))`, `UserController`에 `GET /user/login` → `login_form.html`(username·password 입력, `${param.error}`면 오류 문구), navbar 로그인 링크를 `/user/login`으로
- 이유: 교재 3-07. 로그인 화면은 우리가 만들지만, `POST /user/login` 처리(사용자 조회·비밀번호 비교·세션 저장)는 컨트롤러가 아니라 시큐리티 필터가 한다. 실패하면 시큐리티가 `?error`를 붙여 로그인 화면으로 돌려보낸다
- 결과: 로그인 화면 `<title>로그인 | SBB</title>`, 틀린 비밀번호 → 302 `/user/login?error` → `사용자ID 또는 비밀번호를 확인해 주세요.` 아직 시큐리티가 우리 회원 테이블을 모르므로, 가입한 user1/1234도 로그인 실패(다음 커밋에서 해결). 확인 후 개발 DB 원래대로

### 069 로그인/로그아웃, [교재] UserSecurityService로 회원 테이블 기반 인증
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/069)
- 작업: `UserRole` enum(`ROLE_ADMIN`, `ROLE_USER`), `UserRepository.findByusername`, `UserSecurityService implements UserDetailsService`(username으로 `SiteUser`를 찾아 시큐리티 `User`로 반환, `admin`이면 ADMIN 권한), `SecurityConfig`에 `AuthenticationManager` 빈
- 이유: 교재 3-07. 시큐리티는 로그인할 때 `UserDetailsService.loadUserByUsername`으로 사용자(아이디, 암호화된 비밀번호, 권한)를 받아 `PasswordEncoder`로 입력 비밀번호와 비교한다. 이 빈을 만들면 자동 생성 사용자 대신 우리 회원 테이블로 로그인한다
- 결과: 가입한 user1/1234 → 302 `/`(로그인 성공, 세션 ID 바뀜), 틀린 비밀번호·없는 사용자 → `?error`. 서버 로그에 `Using generated security password`가 더 이상 나오지 않음. 확인 후 개발 DB 원래대로

### 070 로그인/로그아웃, 완료 [교재] 로그아웃과 로그인 상태에 따른 내비게이션 링크
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/070)
- 작업: `SecurityConfig`에 `.logout(logoutRequestMatcher("/user/logout"), logoutSuccessUrl("/"), invalidateHttpSession(true))`, navbar는 `sec:authorize="isAnonymous()"`면 로그인, `isAuthenticated()`면 로그아웃 링크
- 이유: 교재 3-07. 로그아웃도 시큐리티 필터가 처리한다(세션 무효화 후 메인으로). 타임리프 시큐리티 확장(`thymeleaf-extras-springsecurity6`, 061에서 추가)의 `sec:authorize`로 로그인 여부에 따라 태그를 보이거나 숨긴다
- 결과: 비로그인 navbar `로그인`·`회원가입`, 로그인 후 `로그아웃`·`회원가입`, `GET /user/logout` → 302 `/` 후 다시 `로그인`. 교재와 차이: Security 7에서 `AntPathRequestMatcher`가 삭제되어 `PathPatternRequestMatcher.withDefaults().matcher("/user/logout")` 사용(교재처럼 GET 링크로 로그아웃됨). 확인 후 개발 DB 원래대로

### 071 로그인/로그아웃, [개선] 로그아웃을 CSRF 토큰이 있는 POST로, 로그인·로그아웃 테스트
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/071)
- 작업: `SecurityConfig` 로그아웃을 `logoutUrl("/user/logout")`(CSRF가 켜져 있으면 POST만)로, navbar 로그아웃 링크를 `th:action` POST 폼의 버튼으로. `UserLoginTest` 추가(로그인 성공·실패, navbar의 로그아웃 폼, POST 로그아웃, GET 로그아웃 무시)
- 이유: 강사 14168 7강(GET은 조회용, 상태를 바꾸는 요청은 POST)과 46강(시큐리티는 POST에만 CSRF 토큰을 검사). GET 링크 로그아웃은 다른 사이트가 `<img src=".../user/logout">`만 넣어도 사용자를 로그아웃시킬 수 있다. POST + `th:action`이면 토큰이 자동으로 붙어 우리 화면에서 누른 요청만 처리된다
- 결과: 테스트 31개 통과(070 설정으로 되돌리면 GET 로그아웃 테스트 실패 확인). 실제 실행에서 GET `/user/logout` 404(로그인 유지), 토큰 없는 POST 403, 토큰 있는 POST 302 `/` 후 비로그인. 확인 후 개발 DB 원래대로

### 072 글쓴이, 시작 [교재] 질문·답변에 author 연결과 UserService.getUser
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/072)
- 작업: `Question`·`Answer`에 `@ManyToOne(fetch = LAZY) SiteUser author`, `UserService.getUser(username)`(없으면 `DataNotFoundException`)
- 이유: 교재 3-08. 글쓴이를 저장하려면 질문·답변이 회원을 참조해야 한다(여러 글 → 한 회원이라 `@ManyToOne`). 로그인 정보에는 username만 있으므로 그 이름으로 회원 엔티티를 꺼내는 메서드가 필요하다. 교재와 차이: 013 개선을 따라 LAZY
- 결과: 테스트 31개 통과. 실행 시 `question`·`answer` 테이블에 `author_id` 컬럼과 `site_user` 외래키가 추가됨. 확인 후 개발 DB 원래대로

### 073 글쓴이, [교재] 로그인한 사용자만 질문·답변 등록하고 글쓴이 저장
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/073)
- 작업: `@EnableMethodSecurity(prePostEnabled = true)`, 질문 등록(GET·POST)·답변 등록에 `@PreAuthorize("isAuthenticated()")`, `Principal.getName()`으로 회원을 찾아 `create(..., siteUser)`로 글쓴이 저장. 기존 MockMvc 테스트는 user1을 가입시키고 `with(user("user1"))`로 요청
- 이유: 교재 3-08. 비로그인이면 `Principal`이 null이라 글쓴이를 정할 수 없으므로, 메서드 보안으로 로그인한 사용자만 실행하게 한다(비로그인은 로그인 화면으로). 교재와 차이: 040 개선대로 `Question.addAnswer(content, author)`로 답변 생성, record 폼 접근자 사용
- 결과: 테스트 31개 통과. 비로그인 질문 등록 화면·답변 등록 → 302 `/user/login`, 로그인 후 질문·답변 등록 시 SQL에 `author_id = 1` 바인딩. 확인 후 개발 DB 원래대로

### 074 글쓴이, 완료 [교재] 질문 목록·상세에 글쓴이 표시, 비로그인 답변 입력창 비활성화
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/074)
- 작업: 목록에 글쓴이 열(가운데 정렬, 제목은 50% 폭·왼쪽 정렬), 상세의 질문·답변에 글쓴이, 답변 입력창은 `sec:authorize`로 비로그인이면 `disabled`
- 이유: 교재 3-08. 예전 글은 글쓴이가 없으므로(null) `th:if`로 있을 때만 이름을 출력한다. 비로그인 사용자가 답변을 다 쓰고 나서 로그인 화면으로 튕기지 않도록 입력창을 막는다
- 결과: 테스트 31개 통과. 목록에 `글쓴이` 열과 `user1`, 비로그인 상세는 `<textarea disabled>`, 로그인 상세는 입력 가능. 확인 후 개발 DB 원래대로

### 075 글쓴이, [개선] 글쓴이 저장과 로그인 필요를 테스트로 확인
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/075)
- 작업: 질문·답변 등록 시 글쓴이가 로그인 사용자(user1)로 저장되는지, 비로그인 등록 화면·등록 요청이 `/user/login`으로 가고 저장되지 않는지 MockMvc 테스트 추가
- 이유: 다음 섹션의 수정·삭제 권한이 글쓴이에 기대므로 동작을 테스트로 고정한다(14163 35강). 14168 46강처럼 시큐리티는 단계별로 막는다: 토큰 없는 POST는 CSRF 필터가 403, 토큰은 있지만 비로그인이면 `@PreAuthorize`가 로그인 화면으로
- 결과: 테스트 34개 통과. `@EnableMethodSecurity`를 빼면 비로그인 테스트 2개가 실패함을 확인

### 076 수정/삭제, 시작 [교재] 글쓴이만 질문 수정
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/076)
- 작업: `Question.modifyDate`, `QuestionService.modify`, `GET·POST /question/modify/{id}`(글쓴이가 아니면 400 "수정권한이 없습니다."), `question_form`은 action 없이 현재 URL로 전송하고 CSRF hidden input 직접 추가, 상세에 수정일시와 글쓴이에게만 보이는 수정 버튼
- 이유: 교재 3-09. 버튼을 숨기는 것만으로는 URL 직접 접근을 막지 못하므로 서버에서 글쓴이와 로그인 사용자를 비교한다. 등록·수정이 같은 폼을 쓰도록 action을 비운다. 교재와 차이: 폼이 record라 setter 대신 `new QuestionForm(subject, content)`를 모델에 넣음
- 결과: 테스트 34개 통과. 글쓴이 user1만 수정 버튼이 보이고 수정 폼에 기존 값이 채워짐, 수정 후 `modified at` 표시. user2의 수정 화면·수정 요청은 400. 확인 후 개발 DB 원래대로

### 077 수정/삭제, [교재] 글쓴이만 질문 삭제, 확인 창 자바스크립트
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/077)
- 작업: `QuestionService.delete`, `GET /question/delete/{id}`(글쓴이가 아니면 400), layout에 `layout:fragment="script"` 자리, 상세에 글쓴이에게만 보이는 삭제 버튼과 `confirm` 후 `data-uri`로 이동하는 스크립트
- 이유: 교재 3-09. 되돌릴 수 없는 삭제는 확인 창을 거친다. URL을 `href`가 아닌 `data-uri`에 두어 자바스크립트가 확인한 경우에만 이동한다. 스크립트는 부트스트랩 JS 뒤에 오도록 layout 끝에 자리를 만든다
- 결과: 테스트 34개 통과. 남의 질문 삭제 400, 내 질문 삭제 302 `/` 후 상세 404, SQL은 답변 → 질문 순으로 delete(cascade REMOVE). 확인 후 개발 DB 원래대로

### 078 수정/삭제, [교재] 글쓴이만 답변 수정
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/078)
- 작업: `Answer.modifyDate`, `AnswerService.getAnswer`·`modify`, `GET·POST /answer/modify/{id}`(글쓴이가 아니면 400), `answer_form.html`, 상세에 답변 수정일시와 글쓴이에게만 보이는 답변 수정 버튼
- 이유: 교재 3-09. 질문 수정과 같은 흐름(서버에서 글쓴이 확인 → 폼 → 검증 → 저장)을 답변에도 적용하고, 저장 후에는 답변이 달린 질문 상세로 돌아간다. 교재와 차이: record 폼이라 `new AnswerForm(content)`를 모델에 넣음, 새 템플릿에 xmlns와 title
- 결과: 테스트 34개 통과. 글쓴이만 수정 버튼·수정 화면, 남이 요청하면 400, 공백은 필수항목 오류, 수정 후 `modified at`. 확인 후 개발 DB 원래대로

### 079 수정/삭제, 완료 [교재] 글쓴이만 답변 삭제
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/079)
- 작업: `AnswerService.delete`, `GET /answer/delete/{id}`(글쓴이가 아니면 400), 상세에 글쓴이에게만 보이는 답변 삭제 버튼(077의 확인 창 스크립트 재사용)
- 이유: 교재 3-09. 질문 삭제와 같은 흐름. 같은 `class="delete"`를 주어 확인 창 스크립트를 그대로 쓴다
- 결과: 테스트 34개 통과. 남의 답변 삭제 400, 내 답변 삭제 후 질문 상세에서 답변 수 2 → 1. 확인 후 개발 DB 원래대로

### 080 수정/삭제, [개선] 삭제를 CSRF 토큰이 있는 DELETE 요청으로, 수정·삭제 권한 테스트
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/080)
- 작업: hiddenmethod 필터를 켜고 질문·답변 삭제를 `@DeleteMapping`으로, 상세의 삭제 버튼을 `th:action` POST 폼(`_method=DELETE`, `onsubmit` 확인 창)으로. `QuestionModifyDeleteTest`·`AnswerModifyDeleteTest` 추가(글쓴이만 수정·삭제, 남은 400, GET 삭제는 405)
- 이유: 강사 14168 7강(GET은 조회용), 46강(시큐리티는 GET에 CSRF 토큰을 검사하지 않음). 교재의 GET 삭제는 다른 사이트의 `<img src=".../question/delete/1">`로도 실행된다. 41강·44강처럼 `_method` 파라미터로 폼에서 DELETE를 보내고 `@DeleteMapping`으로 받는다. 수정은 이미 CSRF 토큰이 있는 POST이고 등록과 같은 폼을 쓰므로 교재대로 둔다
- 결과: 테스트 43개 통과. 실제 실행에서 GET 삭제 405, 토큰 없는 DELETE 403, 남의 글 400, 글쓴이 DELETE 302 후 삭제 확인. 확인 후 개발 DB 원래대로

### 081 수정/삭제, [개선] 답변 삭제를 Question.deleteAnswer와 orphanRemoval로
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/081)
- 작업: `answerList`에 `orphanRemoval = true`, `Question.deleteAnswer(answer)`(목록에서 제거), `AnswerService.delete`는 `@Transactional`로 `answer.getQuestion().deleteAnswer(answer)`. 같은 트랜잭션에서 삭제 후 `answerList`에서도 빠지는지 테스트 추가
- 이유: 040 개선으로 답변을 질문 쪽에서 만들듯 지울 때도 질문 쪽에서 지운다. 교재의 `answerRepository.delete`는 메모리의 `answerList`에 지운 답변이 남는다(테스트로 실패 확인). 강사 14168 37강: `orphanRemoval`은 부모 목록에서 빠진 자식을 커밋 때 delete한다
- 결과: 테스트 44개 통과. 실제 실행에서 답변 삭제 후 답변 수 2 → 1, SQL은 answerList 조회 후 커밋 때 `delete from answer`. 확인 후 개발 DB 원래대로

### 082 추천, 시작 [교재] 질문 추천
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/082)
- 작업: `Question`에 `@ManyToMany Set<SiteUser> voter`, `QuestionService.vote`, `GET /question/vote/{id}`(로그인 필요), 상세에 추천 버튼·추천 수 배지와 확인 후 이동하는 스크립트
- 이유: 교재 3-10. 질문과 추천인은 다대다라 `question_voter` 연결 테이블이 생긴다. `Set`이라 같은 사람이 여러 번 추천해도 한 번만 센다. 교재와 차이: 013 개선처럼 `voter`를 `new HashSet<>()`으로 초기화
- 결과: 테스트 44개 통과. 비로그인 추천은 로그인 화면으로, user1 두 번 + user2 한 번 추천 → 추천 수 2. 확인 후 개발 DB 원래대로

### 083 추천, 완료 [교재] 답변 추천
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/083)
- 작업: `Answer`에 `@ManyToMany Set<SiteUser> voter`, `AnswerService.vote`, `GET /answer/vote/{id}`(로그인 필요), 상세의 답변마다 추천 버튼과 추천 수 배지
- 이유: 교재 3-10. 답변 추천도 질문 추천과 같은 다대다 구조(`answer_voter` 연결 테이블). 같은 `recommend` 클래스로 082의 확인 스크립트를 재사용한다. 교재와 차이: `voter` 빈 Set 초기화
- 결과: 테스트 44개 통과. 비로그인 추천은 로그인 화면으로, user1 두 번 + user2 한 번 → 답변 추천 수 2. 확인 후 개발 DB 원래대로

### 084 추천, [개선] 추천을 CSRF 토큰이 있는 POST 요청으로, 추천 테스트
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/084)
- 작업: 질문·답변 추천을 `@PostMapping`으로, 상세의 추천 버튼을 `th:action` POST 폼(`onsubmit` 확인 창)으로. `VoteTest` 추가(중복 추천 한 번만, 질문·답변 추천 분리, GET 405, 토큰 없음 403, 비로그인은 로그인 화면)
- 이유: 080 삭제와 같은 근거(14168 7강 GET은 조회용, 46강 GET은 CSRF 검사 밖). 교재의 GET 추천은 다른 사이트의 `<img>` 태그로도 눌린다
- 결과: 테스트 49개 통과. 실제 실행에서 GET 추천 405, 토큰 없는 POST 403, 토큰 있는 POST 302 후 추천 수 1. 확인 후 개발 DB 원래대로

### 085 앵커, 완료 [교재] 답변 등록·수정·추천 후 그 답변 위치로 이동
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/085)
- 작업: 답변 카드마다 `<a th:id="|answer_${answer.id}|">`, `AnswerService.create`가 `Answer`를 돌려주고, 등록·수정·추천 후 `/question/detail/{id}#answer_{답변id}`로 리다이렉트
- 이유: 교재 3-11. URL의 `#answer_2`는 브라우저가 그 id를 가진 요소로 스크롤하게 한다. 교재와 차이: 040 개선으로 답변이 cascade PERSIST(flush 때 insert)로 저장되어 바로는 id가 없으므로 `create`에서 `flush()`해 id를 받는다(안 하면 테스트에서 `#answer_null`)
- 결과: 테스트 49개 통과. 실제 실행에서 등록 → `#answer_2`, 수정 → `#answer_2`, 추천 → `#answer_1`, 상세에 `<a id="answer_N">` 앵커. 확인 후 개발 DB 원래대로

### 086 마크다운, 완료 [교재] commonmark로 질문·답변 내용을 마크다운 렌더링
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/086)
- 작업: `commonmark:0.21.0` 의존성, `CommonUtil.markdown`(@Component), 상세에서 질문·답변 내용을 `@commonUtil.markdown(...)`으로 변환해 `th:utext`로 출력
- 이유: 교재 3-12. 마크다운으로 쓴 글을 HTML로 보여 준다. 변환 결과가 HTML이라 이스케이프하지 않는 `th:utext`를 쓴다. 교재와 차이: 타임리프 3.1.5는 `th:utext` 안의 식을 제한 모드로 평가해 교재 식이 예외(상세 500)가 나므로, `th:with`에서 변환해 변수에 담고 `th:utext`는 변수만 출력
- 결과: 테스트 49개 통과. `# 제목` → `<h1>`, `* 항목` → `<ul><li>`, `**굵게**` → `<strong>`. 본문에 쓴 HTML 태그도 그대로 출력됨(교재 기본 동작). 확인 후 개발 DB 원래대로

### 087 검색, 시작 [교재] JPA Specification으로 제목·내용·글쓴이·답변 검색
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/087)
- 작업: `QuestionService.search(kw)`(Specification: 글쓴이·답변·답변 글쓴이를 left join, distinct, 다섯 항목 like를 or), `getList(page, kw)`, 리포지터리 `findAll(Specification, Pageable)`, 목록 URL에 `kw` 파라미터
- 이유: 교재 3-13. where 조건을 Criteria API로 조립한다. 답변을 join하면 질문이 답변 수만큼 늘어나 distinct가 필요하다. Spring Data JPA 4에서도 교재 코드가 그대로 동작
- 결과: 테스트 49개 통과. 제목(사과)·답변 내용(바나나)·질문 작성자(user1)·답변 작성자(user2)로 각각 1건, 없는 말 0건, `kw`와 `page`가 함께 동작. 확인 후 개발 DB 원래대로

### 088 검색, [교재] 검색창과 검색어를 유지하는 페이징
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/088)
- 작업: 목록 위에 질문 등록 버튼과 검색창, 페이지 링크를 `data-page`로 바꾸고 `kw`·`page`를 hidden으로 가진 GET 폼(`searchForm`)을 자바스크립트로 전송
- 이유: 교재 3-13. `?page=2` 링크만으로는 검색어가 빠져 전체 목록으로 돌아간다. 검색어와 페이지를 한 폼으로 보내 검색 결과 안에서 페이지를 넘긴다. 새 검색은 0페이지부터
- 결과: 테스트 49개 통과. 브라우저에서 `[02` 검색 → `?kw=%5B02&page=0` 10건, `[1` 검색 결과의 3페이지 클릭 → `?kw=%5B1&page=3`, 검색어 유지. 확인 후 개발 DB 원래대로

### 089 검색, 완료 [교재] 같은 검색을 @Query(JPQL)로
- [링크](https://github.com/wiwi-git/jump-to-springboot/tree/089)
- 작업: `QuestionRepository.findAllByKeyword`(@Query JPQL: 엔티티 조인 + `like %:kw%` 다섯 조건 or, distinct), `getList`가 이것을 사용. Specification `search`는 교재처럼 남겨 둠
- 이유: 교재 3-13. 같은 검색을 JPQL로 쓰면 쿼리가 한눈에 보인다. 조건이 고정이면 @Query, 상황에 따라 조립하면 Specification. `Pageable`로 정렬·페이징·count는 스프링 데이터가 붙인다
- 결과: 테스트 49개 통과. SQL 로그에 JPQL과 변환된 SQL, 검색 결과는 087(Specification)과 같음. 확인 후 개발 DB 원래대로
