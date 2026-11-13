## 작업 규칙

이 저장소는 『점프 투 스프링부트』를 따라 SBB를 구현하는 학습 프로젝트다. 최상위 목표는 **작업자의 이해**이며, 개발 속도가 아니다. 용어는 `GLOSSARY.md`를 따른다.

### 범위

- 교재 섹션 1-03부터 3-14(도전과제 9개 포함)까지. 진행 상황은 `README.md`의 `## 진행 현황`을 본다.

### 작업 루프 (작업 하나마다)

1. 이해: 할 일과 이유를 먼저 설명한다. 교재와 강사 방식이 다르면 차이를 설명한다.
2. 승인: 작업자가 승인하기 전에는 코드를 고치지 않고 다음 작업으로 넘어가지 않는다.
3. 구현: 작은 diff 하나만. 작업자가 "내가 칠게"라고 하면 설명만 하고 리뷰한다.
4. 설명: 작성한 코드를 줄 단위로 설명한다.
5. 검증: 실행 결과를 브라우저 등으로 작업자에게 직접 보여준다. 테스트가 있으면 실행한다.
6. 이해 확인: 작업자가 자기 말로 다시 설명하고(teach-back), AI는 빈틈을 짚은 뒤 확인 질문 1~2개를 던진다.
7. 기록·커밋: README `## 작업내역` 갱신 → 커밋 → 태그 → push.

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
