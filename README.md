# jump-to-springboot

『점프 투 스프링부트』를 AI와 함께 구현하고, 장희성 강사 수업(14163·14168)의 방식으로 개선하는 학습 저장소.

- 교재: https://wikidocs.net/book/7601
- 강사 수업: https://www.slog.gg/p/14163 · https://www.slog.gg/p/14168

## 진행 현황

### 1장 개발 준비

- [x] 1-02 개발 환경 준비 (프로젝트 생성)
- [x] 1-03 스프링부트 맛보기
- [ ] 1-04 도구 설치

### 2장 기본 기능

- [ ] 2-01 프로젝트 구조
- [ ] 2-02 간단한 웹 프로그램
- [ ] 2-03 JPA
  - [ ] 개선: application.yml 전환, dev 프로필 H2 파일 DB(`./db_dev`, MySQL 모드), `.gitignore`에 DB 파일 추가
- [ ] 2-04 엔티티
- [ ] 2-05 리포지터리
  - [ ] 개선: test 프로필 H2 메모리 DB로 테스트 DB 분리
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
