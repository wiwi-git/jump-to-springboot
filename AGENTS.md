## 작업 규칙

이 저장소는 『점프 투 스프링부트』를 따라 SBB를 구현하는 학습 프로젝트다. 최상위 목표는 **작업자의 이해**이며, 개발 속도가 아니다. 용어는 `GLOSSARY.md`를 따른다.

### 범위

- 교재 섹션 1-03부터 3-13 검색까지. 3-14 도전과제는 작업자가 요청할 때만 진행한다(031에서 변경). 진행 상황은 `README.md`의 `## 진행 현황`을 본다.

### 진행 방식: 생성 단계 → 이해 단계

작업 하나마다 승인받는 방식은 너무 느려서(002에서 변경), 두 단계로 나눈다.

**생성 단계** (AI가 진행)
- 3-13까지의 커밋을 교재 순서대로 한 번에 만든다. 작은 작업 하나 = 커밋 하나 원칙은 그대로다.
- 커밋마다 빌드·테스트를 돌리고, 화면이 바뀌면 실행해서 확인한다. 결과는 커밋 본문 `검증:`과 README에 적는다.
- 검증이 실패하거나 교재와 Spring Boot 4가 충돌해 판단이 필요하면 멈추고 작업자에게 묻는다.

**이해 단계** (작업자와 함께)
1. `git checkout NNN`으로 그 시점 코드를 연다.
2. AI가 그 커밋의 diff와 교재·강의 개념을 설명한다. 필요하면 앱을 실행해 브라우저로 보여준다.
3. 확인 질문 1~2개 → 작업자 답변 → 빈틈 짚기 → 다음 태그.
4. 끝나면 `main`으로 돌아온다.
- 확인 질문은 교재·강의 내용(Spring, JPA, 타임리프, 시큐리티 등)에만 한다. 저장소 설정 같은 메타 작업은 묻지 않는다.
- 이해 중 발견한 수정 사항은 push된 커밋·태그를 고치지 않고 `main`에 새 커밋으로 반영한다.

### 교재 구현과 개선

- 교재 섹션마다 먼저 **교재 구현** 커밋(교재 그대로)을 만든다.
- 섹션을 완료한 직후, 강사 수업(14163·14168)에 관련 방식이 있으면 **개선** 커밋을 따로 만든다.
- 교재에 없는 코드(테스트 추가 포함)는 교재 구현 커밋에 섞지 않는다.
- 참고 자료: 교재 https://wikidocs.net/book/7601 (코드 https://github.com/pahkey/sbb3), 강사 수업 https://www.slog.gg/p/14163 · https://www.slog.gg/p/14168 (코드 https://github.com/jhs512/p-14163-2 · https://github.com/jhs512/p-14168-2)

### 기술 환경

- Spring Boot 4.1.2, Java 25, Gradle Kotlin DSL. 패키지는 교재대로 `com.mysite.sbb`. 근거는 `docs/adr/0001-book-code-on-latest-stack.md`.
- 저장소 루트가 곧 Gradle 프로젝트 루트다.
- 의존성은 교재 섹션에서 필요해질 때 추가한다.

### 커밋

- 제목: `NNN 기능명, [시작|완료] [교재|개선] 세부 내용`. NNN은 커밋마다 1씩 오르는 3자리 일련번호. (`000`만 예외로 `[설정]`을 쓴다.)
- 본문: 첫 줄에 교재 섹션 번호, 이어서 `작업 내용:` / `작업 이유:` / `검증:`.
- 커밋마다 README `## 작업내역`에 같은 항목을 추가하고, 태그 `NNN`을 달아 push한다.

## Agent skills

### Issue tracker

Issues live as local markdown files under `.scratch/<feature>/` in this repo. See `docs/agents/issue-tracker.md`.

### Triage labels

Uses the five default triage labels (`needs-triage`, `needs-info`, `ready-for-agent`, `ready-for-human`, `wontfix`), recorded as a `Status:` line in each issue file. See `docs/agents/triage-labels.md`.

### Domain docs

Single-context: one `GLOSSARY.md` and `docs/adr/` at the repo root. See `docs/agents/domain.md`.
