"""Builds assets/narration.mp3 and index.html from vo.json (run gen_vo.py first).

Landscape 1920x1080 version of the video1 shorts pipeline (~/claude-test/video1): same colour tokens,
Pretendard, word-timed captions; a section bar on top instead of the 5-step progress.
Every fact on screen comes from README.md and the commit messages 000..090 of this repository.
"""
import json, re, subprocess, html

LEAD, GAP, TAIL = 0.4, 0.6, 1.5
XF = 0.15  # scene change this long before the next narration
vo = json.load(open("vo.json"))

# --- timing + concatenated narration ---
t = LEAD
for s in vo:
    s["start"] = round(t, 3)
    t += s["dur"] + GAP
TOTAL = round(t - GAP + TAIL, 2)

inputs, filt = [], []
for i, s in enumerate(vo):
    inputs += ["-i", f"assets/vo/{s['id']}.mp3"]
    filt.append(f"[{i}]adelay={int(s['start']*1000)}:all=1[a{i}]")
mix = "".join(f"[a{i}]" for i in range(len(vo)))
filt.append(f"{mix}amix=inputs={len(vo)}:normalize=0,apad,atrim=0:{TOTAL}[out]")
subprocess.run(["ffmpeg", "-y", "-loglevel", "error", *inputs, "-filter_complex", ";".join(filt),
                "-map", "[out]", "-ar", "48000", "-b:a", "192k", "assets/narration.mp3"], check=True)

S = {s["id"]: s for s in vo}

def norm(x):
    return re.sub(r"[^\w]", "", x)

def word_time(sid, token, after=0.0):
    """Absolute time of the first word in scene `sid` starting with `token`, spoken after `after`."""
    s = S[sid]
    for w in s["words"]:
        at = s["start"] + w["t"]
        if at >= after and norm(w["text"]).startswith(norm(token)):
            return round(at, 3)
    raise ValueError(f"{token!r} not found in {sid}")

# --- code highlighting (Java / YAML / HTML snippets) ---
KW = ("public class record private void new return true false null this if").split()
TOK = re.compile(r'"(?:\\.|[^"\\])*"|//.*|#.*|@\w+|\d+\b|\w+|.')
def hl(line):
    out = []
    for m in TOK.finditer(line):
        x = m.group()
        if x[0] == '"':
            out.append(f'<span class="st">{html.escape(x, quote=False)}</span>')
        elif x.startswith(("//", "#")):
            out.append(f'<span class="cm">{html.escape(x)}</span>')
        elif x[0] == "@":
            out.append(f'<span class="an">{x}</span>')
        elif x[0].isdigit():
            out.append(f'<span class="nu">{x}</span>')
        elif x in KW:
            out.append(f'<span class="kw">{x}</span>')
        else:
            out.append(html.escape(x, quote=False))
    return "".join(out)

def code(lines, mark=(), bad=()):
    """`mark`: lines tinted orange (the point), `bad`: lines tinted red (the problem)."""
    for l in lines:
        assert len(l) <= 44, f"code line over 44 chars: {l!r}"
    def cls(i):
        return " mk" if i in mark else " bd" if i in bad else ""
    return '<div class="code">' + "".join(
        f'<div class="ln{cls(i)}">{hl(l) or "&nbsp;"}</div>' for i, l in enumerate(lines)) + "</div>"

def head(step, title, desc=""):
    d = f'<p class="desc in">{desc}</p>' if desc else ""
    return f'<div class="pill in">{step}</div><h2 class="title in">{title}</h2>{d}'

def cmp(book, better, book_note="", better_note=""):
    """Side-by-side cards: 교재 (left) and 개선 (right)."""
    def card(kind, label, body, note):
        n = f'<p class="cn">{note}</p>' if note else ""
        return f'<div class="cc {kind} in"><div class="ct">{label}</div>{body}{n}</div>'
    return ('<div class="cmp">' + card("bk", "교재", book, book_note)
            + '<div class="cmp-arrow in">→</div>' + card("ok", "개선", better, better_note) + "</div>")

def shot(*names):
    """Browser-framed screenshot; extra names are stacked (hidden) for a crossfade, ids `shot-<name>`."""
    imgs = "".join(f'<img id="shot-{n}" class="{"z" if i else ""}" src="assets/shots/{n}.png" />' for i, n in enumerate(names))
    return (f'<div class="shot in"><div class="bar3"><i></i><i></i><i></i>'
            f'<span>localhost:8080</span></div><div class="vp">{imgs}</div></div>')

def zoom_js(at, name, px, py, s):
    """GSAP tween: origin 0 0, translate so the point is centred, clamped so the image still covers the frame."""
    x = min(0.0, max(1 - s, 0.5 - px * s)) * 100
    y = min(0.0, max(1 - s, 0.5 - py * s)) * 100
    return (f'tl.to("#shot-{name}", {{scale: {s}, xPercent: {x:.2f}, yPercent: {y:.2f}, '
            f'duration: 0.9, ease: "power2.inOut"}}, {at});')

def commits(*nums):
    return '<div class="refs in">' + "".join(f"<span>{n}</span>" for n in nums) + "</div>"

# --- sections on the top bar ---
SECTIONS = ["최신 스택", "만든 것", "설정·DB", "JPA", "폼·보안", "테스트"]
PROG = {"stack": 1, "stack2": 1, "built": 2, "built2": 2, "db": 3, "db2": 3, "lazy": 4, "add": 4, "orphan": 4,
        "form": 5, "csrf": 5, "guard": 5, "rollback": 6, "tests": 6}

# test counts written in the 검증: line of each commit (./gradlew test 통과 (N개))
TESTS = [("020", 2), ("021", 10), ("022", 11), ("035", 13), ("040", 15), ("053", 19), ("054", 20), ("063", 22),
         ("067", 26), ("071", 31), ("075", 34), ("080", 43), ("081", 44), ("084", 49), ("090", 53)]

def bars():
    top = max(n for _, n in TESTS)
    cols = "".join(f'<div class="bc"><b>{n}</b><i style="height:{round(n / top * 300)}px"></i><small>{c}</small></div>'
                   for c, n in TESTS)
    return f'<div class="bars in">{cols}</div>'

FEATURES = ["질문 목록·상세", "답변", "질문 등록·입력 검증", "페이징", "회원 가입·로그인", "글쓴이",
            "수정·삭제", "추천", "마크다운", "검색"]

OUTRO = ["프로필로 개발 DB · 테스트 DB 분리", "LAZY · 질문 쪽에서 답변 추가/삭제", "@NotBlank · record 폼",
         "상태 변경은 CSRF 토큰이 있는 폼", "테스트 53개로 동작 고정"]
OUTRO_SAID = ["프로필로", "엔티티는", "폼은", "상태를", "오십삼"]

scenes = {
 "hook": '''<div class="hook">
   <div class="pill in">점프 투 스프링부트로 만든 SBB</div>
   <h1 class="big in">교재 <em>그대로</em>,<br/>그리고 <em>개선</em></h1>
   <div class="sub in">질문·답변 게시판 SBB를 만들고 무엇을 왜 바꿨는지</div>
 </div>''',
 "flow": head("작업 방식", "섹션마다 <em>교재 구현</em> → <em>개선</em>",
              "교재 1-03 ~ 3-13 · 강사 수업 14163 · 14168 · 작은 작업 하나 = 커밋 하나 = 태그 하나")
    + '<div class="stats">'
      '<div class="st0 in"><b>91</b><span>커밋 (000~090)</span></div>'
      '<div class="st1 in"><b>64</b><span>[교재] 교재 그대로 구현</span></div>'
      '<div class="st2 in"><b>24</b><span>[개선] 강사 방식으로 변경</span></div>'
      '<div class="st3 in"><b>3</b><span>[설정] 저장소 규칙</span></div></div>'
    + '<div class="note in"><code>088 검색, [교재] 검색창과 검색어를 유지하는 페이징</code> · '
      '<code>090 검색, [개선] 검색을 테스트로 확인</code></div>',
 "stack": head("최신 스택 · ADR 0001", "교재보다 새 버전 위에서", "개선 커밋에 <b>버전 차이로 생긴 수정</b>이 섞이지 않도록")
    + '<div class="ver"><div class="vc bk in"><div class="ct">교재</div>'
      '<p>Spring Boot <b>3.x</b></p><p>Java <b>19~20</b></p><p>Gradle <b>Groovy</b></p></div>'
      '<div class="cmp-arrow in">→</div>'
      '<div class="vc ok in"><div class="ct">이 프로젝트 (강사 환경)</div>'
      '<p>Spring Boot <b>4.1.2</b></p><p>Java <b>25</b></p><p>Gradle <b>Kotlin DSL</b></p></div></div>',
 "stack2": head("최신 스택 · 교재와 차이", "그대로 안 되는 곳은 최소한만", "커밋 본문의 <b>교재와 차이:</b>에 기록")
    + cmp(code(["// build.gradle", "implementation 'org.springframework.boot:", "    spring-boot-starter-web'", "",
                "requestMatchers(new AntPathRequestMatcher(", '    "/**"))'], bad=(2, 4)),
          code(["// build.gradle.kts", "implementation(\"org.springframework.boot:", "    spring-boot-starter-webmvc\")", "",
                "// Security 7: AntPathRequestMatcher 삭제", 'requestMatchers("/**")'], mark=(2, 5)),
          "Spring Boot 3 · Spring Security 6", "Spring Boot 4.1.2 · Spring Security 7")
    + commits("001", "061", "062", "070"),
 "built": '<div class="split"><div class="lcol">' + head("만든 것 · 교재 1-03 ~ 3-13", "SBB 질문·답변 게시판")
    + '<div class="feat in">' + "".join(f"<span>{f}</span>" for f in FEATURES) + "</div>"
    + '<div class="note in">한 페이지 <b>10개</b> · 페이지 번호 · 검색창</div></div>'
    + shot("list") + "</div>",
 "built2": '<div class="split"><div class="lcol">' + head("만든 것", "로그인·글쓴이·추천<br/>마크다운·검색")
    + '<div class="note in">내가 쓴 글에만 <b>수정·삭제</b><br/>추천 수 <b>배지</b> · 내용은 <b>마크다운</b></div>'
    + '<div class="note in">제목·내용·글쓴이·답변 검색<br/><code>/question/list?kw=LAZY</code></div></div>'
    + shot("detail", "search") + "</div>",
 "db": head("개선 ① 설정·DB", "개발 DB와 테스트 DB 분리", "테스트가 개발 DB를 건드려 <b>삭제 테스트가 2번째 실행에서 실패</b> (017)")
    + cmp(code(["# application.properties (하나)", "spring.datasource.url=", "    jdbc:h2:~/local", "",
                "# 테스트도 같은 개발 DB를 사용"], bad=(2, 4)),
          code(["# application.yml", "spring.profiles.active: dev", "# application-dev.yml",
                "url: jdbc:h2:./db_dev;MODE=MySQL", "# application-test.yml",
                "url: jdbc:h2:mem:db_test;MODE=MySQL"], mark=(3, 5)),
          "", "테스트가 끝나면 메모리 DB도 사라짐 → 언제 몇 번 돌려도 같은 결과")
    + commits("010", "020"),
 "db2": head("개선 ① 설정·DB", "환경별로 나눈 설정 몇 가지")
    + '<div class="cards3">'
      '<div class="card in"><b>타임리프 캐시</b><span><code>cache: false</code>는 dev 프로필에서만</span><small>028</small></div>'
      '<div class="card in"><b>샘플 데이터</b><span>개발 DB가 비어 있으면 <code>TestInitData</code>가 질문 2개 생성</span><small>029</small></div>'
      '<div class="card in"><b>SQL 로그</b><span>바인딩 값 · 조회 값 · 트랜잭션 시작/완료 출력</span><small>023</small></div></div>'
    + '<div class="term in"><i>테스트 로그</i>'
      '<div>binding parameter (1:VARCHAR) &lt;- [sbb에 대해서 알고 싶습니다.]</div>'
      '<div class="cmd">Getting transaction for [...SimpleJpaRepository.count]</div></div>',
 "lazy": head("개선 ② JPA 엔티티", "@ManyToOne에는 LAZY", "기본값 EAGER → 답변만 조회해도 <b>질문까지 항상 조회</b>")
    + cmp(code(["// Answer.java", "@ManyToOne", "private Question question;", "", "// Question.java",
                "private List<Answer> answerList;"], bad=(1, 5)),
          code(["// Answer.java", "@ManyToOne(fetch = FetchType.LAZY)", "private Question question;", "",
                "// Question.java", "private List<Answer> answerList", "        = new ArrayList<>();"], mark=(1, 5, 6)),
          "새로 만든 Question의 answerList는 null", "질문은 실제로 쓸 때만 조회 · NPE 방지")
    + commits("013"),
 "add": head("개선 ② JPA 엔티티", "답변은 질문 쪽에서 만든다", "교재 방식은 꺼내 둔 <b>question.answerList에 새 답변이 없음</b>")
    + cmp(code(["// AnswerService.create", "Answer answer = new Answer();", "answer.setContent(content);",
                "answer.setQuestion(question);", "this.answerRepository.save(answer);"], bad=(3, 4)),
          code(["// AnswerService.create", "@Transactional", "question.addAnswer(content);", "",
                "// Question.java", "@OneToMany(mappedBy = \"question\",", "  cascade = {PERSIST, REMOVE})"], mark=(2, 6)),
          "연관관계의 한쪽만 설정", "양쪽이 함께 맞춰지고, 커밋 때 cascade로 insert")
    + commits("022", "040"),
 "orphan": head("개선 ② JPA 엔티티", "삭제도 질문 쪽에서", "목록에서 빼면 <b>orphanRemoval</b>이 커밋 때 delete")
    + cmp(code(["// AnswerService.delete", "this.answerRepository.delete(answer);", "",
                "// DB에선 지워졌지만", "// question.answerList엔 남아 있음"], bad=(1, 4)),
          code(["// AnswerService.delete", "@Transactional", "answer.getQuestion().deleteAnswer(answer);", "",
                "@OneToMany(..., orphanRemoval = true)", "// deleteAnswer = answerList.remove(answer)"], mark=(2, 4)),
          "", "엔티티의 목록과 DB가 같은 상태로 유지")
    + commits("081"),
 "form": head("개선 ③ 폼·보안", "@NotBlank와 record 폼", "@NotEmpty는 길이 0만 막아 <b>공백만 넣은 제목</b>이 저장됨")
    + cmp(code(["@Getter @Setter", "public class QuestionForm {", "  @NotEmpty(message = \"제목은 ...\")",
                "  @Size(max = 200)", "  private String subject;", "  @NotEmpty(message = \"내용은 ...\")",
                "  private String content;", "}"], bad=(2, 5)),
          code(["public record QuestionForm(", "  @NotBlank(message = \"제목은 ...\")", "  @Size(max = 200)",
                "  String subject,", "  @NotBlank(message = \"내용은 ...\")", "  String content", ") {}"], mark=(0, 1, 4)),
          "\"   \" → 목록에 빈 링크", "\"   \" 거부 · setter 없는 불변 객체")
    + commits("053", "054", "067"),
 "csrf": head("개선 ③ 폼·보안", "상태를 바꾸는 요청은 토큰이 있는 폼으로",
              "시큐리티는 <b>GET에 CSRF 토큰을 검사하지 않음</b> → 다른 사이트의 &lt;img src=\"/question/delete/1\"&gt;")
    + cmp(code(["<a href=\"javascript:void(0);\"", "   th:data-uri=\"@{|/question/delete/..|}\"",
                "   class=\"delete\">삭제</a>", "// 자바스크립트가 location.href로 GET",
                "@GetMapping(\"/delete/{id}\")"], bad=(3, 4)),
          code(["<form th:action=\"@{|/question/delete/..|}\"", "      method=\"post\">",
                "  <input type=\"hidden\" name=\"_method\"", "         value=\"DELETE\">",
                "  <button>삭제</button></form>", "@DeleteMapping(\"/delete/{id}\")"], mark=(0, 2, 3, 5)),
          "로그아웃 · 삭제 · 추천 모두 GET", "th:action이 _csrf를 넣음 · hiddenmethod 필터")
    + commits("071", "080", "084"),
 "guard": head("개선 ③ 폼·보안", "삭제 요청은 이렇게 걸러진다", "080 검증: bootRun 후 직접 보낸 요청")
    + '<div class="flow4">'
      '<div class="fx in"><b>GET</b><span>/question/delete/303</span><em class="ng">405</em></div>'
      '<div class="fx in"><b>POST · 토큰 없음</b><span>_method=DELETE</span><em class="ng">403</em></div>'
      '<div class="fx in"><b>다른 사용자 user2</b><span>토큰과 함께</span><em class="ng">400</em></div>'
      '<div class="fx in"><b>글쓴이 user1</b><span>토큰과 함께</span><em class="ok">302 삭제</em></div></div>',
 "rollback": head("개선 ④ 테스트", "모든 단계의 테스트를 남기고 롤백", "교재: 테스트를 <b>지우고 다시 쓰며</b> 진행 → 지난 단계 테스트가 없음")
    + cmp(code(["@SpringBootTest", "class SbbApplicationTests {", "  @Test", "  void testJpa() {",
                "    // 단계마다 내용을 바꿔 씀", "  }", "}"], bad=(4,)),
          code(["@ActiveProfiles(\"test\")", "@SpringBootTest", "@Transactional  // 메서드마다 롤백",
                "class QuestionRepositoryTest {", "  @Test @DisplayName(\"findAll\")", "  void t1() { ... }",
                "  // t2 ~ t11 모두 남김", "}"], mark=(0, 2, 6)),
          "", "실행 순서와 상관없이 매번 같은 TestInitData에서 시작")
    + commits("020", "021"),
 "tests": head("개선 ④ 테스트", "테스트 2개 → 53개", "MockMvc로 화면 요청까지 · <b>코드를 잠시 되돌려 실패하는지</b> 확인")
    + bars()
    + '<div class="cards2"><div class="card in"><b>csrf.disable()</b><span>CSRF 테스트 2개 실패 (063)</span></div>'
      '<div class="card in"><b>distinct 제거</b><span>검색 count 테스트 실패 (090)</span></div></div>',
 "etc": head("그 밖에", "작은 개선들")
    + '<div class="cards4">'
      '<div class="card in"><b>부트스트랩</b><span>static 파일 대신 cdnjs 5.3.8</span><small>044</small></div>'
      '<div class="card in"><b>프리텐다드</b><span>OS와 상관없이 같은 한글 글꼴</span><small>045</small></div>'
      '<div class="card in"><b>탭 제목</b><span>질문 목록 | SBB</span><small>049</small></div>'
      '<div class="card in"><b>xmlns 선언</b><span>th · layout 편집기 경고 제거</span><small>048</small></div></div>',
 "outro": head("정리", "교재 대비 바꾼 것 다섯 가지") + '<div class="list in">'
    + "".join(f'<div class="row" id="ex{i}"><span class="no">{i}</span><b>{html.escape(c)}</b></div>' for i, c in enumerate(OUTRO, 1))
    + "</div>",
 "end": '''<div class="hook end">
   <h1 class="big in">감사합니다</h1>
   <div class="repo in">github.com/wiwi-git/jump-to-springboot</div>
   <div class="sub in">커밋 000~090 · 태그로 그 시점 코드를 열 수 있어요</div>
 </div>''',
}
assert set(scenes) == set(S), set(scenes) ^ set(S)


# The script spells technical words the way TTS should say them; captions show them as on screen.
CAPTION_TERMS = {
    "에스비비": "SBB", "아흔한 개": "91개", "예순네 개": "64개", "스물네 개": "24개", "세 개": "3개",
    "두 개": "2개", "오십삼 개": "53개",
    "스프링부트 삼 점 엑스": "Spring Boot 3.x", "자바 십구에서 이십": "Java 19~20", "그루비": "Groovy",
    "스프링부트 사 점 일 점 이": "Spring Boot 4.1.2", "자바 이십오": "Java 25", "코틀린 디에스엘": "Kotlin DSL",
    "스타터 웹 엠브이씨": "spring-boot-starter-webmvc", "스타터 웹": "spring-boot-starter-web",
    "스프링 시큐리티 칠": "Spring Security 7", "스프링 시큐리티": "Spring Security",
    "앤트 패스 리퀘스트 매처": "AntPathRequestMatcher", "일 장부터 삼 장": "1장부터 3장",
    "디비": "DB", "에스큐엘": "SQL", "제이피에이": "JPA", "매니 투 원": "@ManyToOne", "레이지": "LAZY",
    "애드 앤서": "addAnswer", "캐스케이드 퍼시스트": "cascade PERSIST", "딜리트 앤서": "deleteAnswer",
    "오펀 리무벌": "orphanRemoval", "낫 엠티": "@NotEmpty", "낫 블랭크": "@NotBlank", "길이가 영인": "길이가 0인",
    "레코드": "record", "세터": "setter", "겟": "GET", "씨에스알에프": "CSRF", "이미지 태그": "<img> 태그",
    "히든 메소드 필터": "hiddenmethod 필터", "딜리트 요청": "DELETE 요청",
    "사백오": "405", "사백삼": "403", "사백": "400", "트랜잭셔널": "@Transactional", "목 엠브이씨": "MockMvc",
    "디스팅트": "DISTINCT", "씨디엔제이에스": "cdnjs",
}
TERM_RE = re.compile("|".join(map(re.escape, sorted(CAPTION_TERMS, key=len, reverse=True))))

def caption_text(spoken):
    return TERM_RE.sub(lambda m: CAPTION_TERMS[m.group()], spoken)

def inside_term(x, i):
    """True if a break at index `i` would split one of the CAPTION_TERMS."""
    return any(m.start() < i < m.end() for m in TERM_RE.finditer(x))

def split_caption(text, limit=38):
    """Comma-first split; long pieces halved at a space (never inside a term); short fragments merged."""
    pieces = [x.strip() + "," for x in text.split(",")]
    pieces[-1] = pieces[-1][:-1]
    out = []
    for x in pieces:
        while len(x) > limit and " " in x:
            mid = len(x) // 2
            cut = min((i for i, c in enumerate(x) if c == " "), key=lambda i: abs(i - mid) + 1000 * inside_term(x, i))
            out.append(x[:cut]); x = x[cut + 1:]
        out.append(x)
    merged = []
    for x in out:
        if merged and (len(x) < 12 or len(merged[-1]) < 12) and len(merged[-1]) + len(x) <= limit + 6:
            merged[-1] += " " + x
        else:
            merged.append(x)
    return merged

def word_marks(s):
    """(char offset in norm(script), time) per TTS word."""
    ntext, cur, marks = norm(s["text"]), 0, []
    for w in s["words"]:
        for tok in filter(None, map(norm, re.split(r"[\s.,?!]+", w["text"]))):
            pos = ntext.find(tok, cur, cur + len(tok) + 3)
            if pos >= 0:
                marks.append((pos, w["t"])); cur = pos + len(tok)
                break
    if cur < len(ntext) - 2:
        raise ValueError(f"{s['id']}: TTS words cover only {cur}/{len(ntext)} chars of the script")
    return marks

def caption_chunks(s):
    """Caption pieces timed to the TTS word boundaries."""
    marks, pos, pieces = word_marks(s), 0, []
    for sentence in re.split(r"(?<=[.?!])\s+", s["text"].strip()):
        for part in split_caption(sentence):
            pieces.append((part, next(t for p, t in marks if p >= pos)))
            pos += len(norm(part))
    out = []
    for k, (txt, a) in enumerate(pieces):
        b = pieces[k + 1][1] if k + 1 < len(pieces) else s["dur"]
        a = 0.0 if k == 0 else a
        out.append((s["start"] + a, b - a, txt))
    return out

clips, caps, js = [], [], []
for s in vo:
    st = s["start"]
    vis_start = 0 if s is vo[0] else st - XF
    vis_end = TOTAL if s is vo[-1] else vo[vo.index(s) + 1]["start"] - XF
    vis_dur = round(vis_end - vis_start, 3)
    clips.append(f'<section id="sc-{s["id"]}" class="clip scene" data-start="{vis_start:.3f}" data-duration="{vis_dur}" data-track-index="1">{scenes[s["id"]]}</section>')
    js.append(f'enter("#sc-{s["id"]}", {st}, {s["dur"]});')
    rk = PROG.get(s["id"], 0)
    js.append(f'tl.set(".tabs span", {{color: "#8391ad", backgroundColor: "rgba(0,0,0,0)"}}, {vis_start});')
    if rk:
        js.append(f'tl.set(".tabs span:nth-child({rk})", {{color: "#0b1220", backgroundColor: "#f29111"}}, {vis_start});')
    chunks = caption_chunks(s)
    assert sum(len(TERM_RE.findall(t)) for _, _, t in chunks) == len(TERM_RE.findall(s["text"])), f"{s['id']}: a term is split across captions"
    for j, (a, d, txt) in enumerate(chunks):
        caps.append(f'<div id="cap-{s["id"]}-{j}" class="clip cap" data-start="{a:.3f}" data-duration="{d:.3f}" data-track-index="2"><span>{html.escape(caption_text(txt))}</span></div>')

# screenshots: zoom into the part the narration is talking about (points are fractions of the screenshot)
ZOOMS = [
    ("built", "아래에", "list", 0.50, 0.71, 2.2),       # 페이지 번호
    ("built", "오른쪽", "list", 0.72, 0.11, 2.2),       # 검색창
    ("built2", "수정과", "detail", 0.16, 0.45, 2.4),    # 추천 배지 · 수정 · 삭제 버튼
    ("built2", "마크다운으로", "detail", 0.22, 0.25, 2.0),  # 마크다운으로 렌더링된 내용
]
for sid, token, name, px, py, s_ in ZOOMS:
    js.append(zoom_js(word_time(sid, token, S[sid]["start"]), name, px, py, s_))
# search: cross-fade the detail page to the search result, zoomed on the search box and the hit
at = word_time("built2", "마지막으로", S["built2"]["start"])
js.append(f'tl.to("#shot-search", {{opacity: 1, duration: 0.6}}, {at});')
js.append(zoom_js(at, "search", 0.50, 0.16, 1.35))

# outro: light up each item as the narration names it
after = S["outro"]["start"]
for i, token in enumerate(OUTRO_SAID, 1):
    at = word_time("outro", token, after)
    after = at
    if i > 1:
        js.append(f'tl.to("#ex{i - 1}", {{backgroundColor: "#131c2e", scale: 1, duration: 0.2}}, {at});')
    js.append(f'tl.to("#ex{i}", {{backgroundColor: "#3a2a12", scale: 1.02, duration: 0.2}}, {at});')
js.append(f'tl.to("#ex{len(OUTRO)}", {{backgroundColor: "#131c2e", scale: 1, duration: 0.3}}, {S["outro"]["start"] + S["outro"]["dur"]});')

tabs = "".join(f"<span>{x}</span>" for x in SECTIONS)
page = f'''<!doctype html>
<html lang="ko" data-resolution="landscape">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=1920, height=1080" />
<script src="https://cdn.jsdelivr.net/npm/gsap@3.14.2/dist/gsap.min.js"></script>
<style>
:root {{
  --ground: #0b1220; --panel: #131c2e; --ink: #f4f6fb; --mute: #9aa6bd;
  --blue: #3fa7d6; --orange: #f29111; --red: #ff5d5d; --green: #7ee08a; --violet: #c3a6ff;
  --font: "Pretendard", sans-serif;
  --mono: "Menlo", "SF Mono", monospace;
}}
@font-face {{ font-family: "Pretendard"; src: url("assets/fonts/PretendardVariable.woff2") format("woff2"); font-weight: 45 920; }}
* {{ margin: 0; padding: 0; box-sizing: border-box; }}
html, body {{ width: 1920px; height: 1080px; overflow: hidden; background: var(--ground); }}
#root {{ position: relative; width: 100%; height: 100%; font-variant-ligatures: none; font-family: var(--font); color: var(--ink); overflow: hidden;
  background: radial-gradient(1400px 900px at 90% -20%, #16365a 0%, transparent 60%),
              radial-gradient(1100px 800px at -10% 120%, #3a2410 0%, transparent 55%), var(--ground); }}
.clip {{ position: absolute; inset: 0; }}
.brand {{ position: absolute; top: 34px; left: 90px; right: 90px; display: flex; justify-content: space-between; align-items: center;
  font-size: 26px; font-weight: 700; color: var(--mute); }}
.brand b {{ color: var(--blue); }} .brand b em {{ color: var(--orange); font-style: normal; }}
.tabs {{ display: flex; gap: 8px; }}
.tabs span {{ padding: 6px 18px; border-radius: 999px; font-size: 22px; font-weight: 700; color: #8391ad; }}
.scene {{ padding: 120px 90px 0; display: flex; flex-direction: column; gap: 26px; }}
.pill {{ align-self: flex-start; padding: 8px 22px; border-radius: 999px; background: #1d2a44; color: var(--blue); font-size: 24px; font-weight: 700; }}
.title {{ font-size: 62px; font-weight: 800; letter-spacing: -0.03em; line-height: 1.15; }}
.title em {{ font-style: normal; color: var(--orange); }}
.desc {{ font-size: 32px; color: var(--mute); font-weight: 600; margin-top: -6px; }} .desc b {{ color: var(--orange); }}
.hook {{ display: flex; flex-direction: column; gap: 40px; padding-top: 150px; }}
.big {{ font-size: 128px; line-height: 1.1; font-weight: 800; letter-spacing: -0.04em; }}
.big em {{ font-style: normal; color: var(--orange); }}
.sub {{ font-size: 40px; font-weight: 700; color: var(--mute); }}
.end {{ align-items: center; text-align: center; padding-top: 230px; }}
.repo {{ font-family: var(--mono); font-size: 46px; color: var(--blue); padding: 18px 36px; background: var(--panel); border-radius: 22px; }}
.code {{ background: #0a0f1a; border: 2px solid #26324a; border-radius: 20px; padding: 20px 24px; font-family: var(--mono);
  font-size: 27px; line-height: 1.6; color: #d9e1f2; white-space: pre; font-variant-ligatures: none; font-feature-settings: "liga" 0, "calt" 0; }}
.code .ln {{ margin: 0 -10px; padding: 0 10px; border-radius: 8px; }}
.code .ln.mk {{ background: rgba(242,145,17,.16); }}
.code .ln.bd {{ background: rgba(255,93,93,.14); }}
.kw {{ color: var(--blue); font-weight: 700; }} .st {{ color: var(--green); }} .nu {{ color: var(--orange); }} .cm {{ color: #6f7d98; }} .an {{ color: var(--violet); }}
.cmp {{ display: grid; grid-template-columns: 1fr 60px 1fr; gap: 16px; align-items: stretch; }}
.cmp-arrow {{ display: flex; align-items: center; justify-content: center; font-size: 56px; color: var(--orange); font-weight: 800; }}
.cc, .vc {{ border-radius: 24px; padding: 18px 22px 20px; display: flex; flex-direction: column; gap: 12px; background: var(--panel); border: 3px solid; }}
.cc.bk, .vc.bk {{ border-color: #3a4560; }} .cc.ok, .vc.ok {{ border-color: var(--orange); }}
.ct {{ font-size: 26px; font-weight: 800; }} .bk .ct {{ color: var(--mute); }} .ok .ct {{ color: var(--orange); }}
.cn {{ font-size: 27px; font-weight: 700; color: var(--mute); }} .ok .cn {{ color: var(--green); }} .bk .cn {{ color: #ff9b9b; }}
.refs {{ display: flex; gap: 10px; align-items: center; }}
.refs::before {{ content: "커밋"; font-size: 22px; font-weight: 700; color: var(--mute); margin-right: 4px; }}
.refs span {{ font-family: var(--mono); font-size: 22px; padding: 4px 14px; border-radius: 10px; background: #1d2a44; color: var(--blue); }}
.ver {{ display: grid; grid-template-columns: 1fr 80px 1fr; gap: 16px; margin-top: 20px; }}
.vc {{ padding: 30px 40px; gap: 18px; }} .vc p {{ font-size: 40px; font-weight: 600; color: var(--mute); }}
.vc p b {{ color: var(--ink); }} .vc.ok p b {{ color: var(--orange); }}
.stats {{ display: grid; grid-template-columns: 1.1fr 1fr 1fr 1fr; gap: 20px; margin-top: 10px; }}
.stats div {{ background: var(--panel); border-radius: 24px; padding: 26px 30px; display: flex; flex-direction: column; gap: 6px; }}
.stats b {{ font-size: 110px; font-weight: 900; line-height: 1; }}
.stats span {{ font-size: 28px; font-weight: 700; color: var(--mute); }}
.st0 b {{ color: var(--ink); }} .st1 b {{ color: var(--blue); }} .st2 b {{ color: var(--orange); }} .st3 b {{ color: var(--mute); }}
.note {{ padding: 18px 28px; border-radius: 20px; font-size: 28px; font-weight: 700; line-height: 1.5; background: rgba(63,167,214,.12); border-left: 8px solid var(--blue); }}
.note b {{ color: var(--orange); }}
code {{ font-family: var(--mono); color: var(--orange); font-size: .92em; }}
.split {{ display: grid; grid-template-columns: 640px 1fr; gap: 50px; height: 100%; }}
.lcol {{ display: flex; flex-direction: column; gap: 22px; }}
.feat {{ display: flex; flex-wrap: wrap; gap: 12px; }}
.feat span {{ padding: 10px 20px; background: var(--panel); border-radius: 14px; font-size: 27px; font-weight: 700; }}
.shot {{ align-self: start; background: #fff; border-radius: 18px; overflow: hidden; box-shadow: 0 30px 70px rgba(0,0,0,.5); border: 2px solid #26324a; }}
.shot .bar3 {{ height: 40px; background: #1d2536; display: flex; align-items: center; gap: 8px; padding: 0 16px; }}
.shot .bar3 i {{ width: 13px; height: 13px; border-radius: 50%; background: #3a4560; }}
.shot .bar3 span {{ margin-left: 14px; font-family: var(--mono); font-size: 18px; color: var(--mute); }}
.shot .vp {{ position: relative; overflow: hidden; }}
.shot img {{ display: block; width: 100%; transform-origin: 0 0; }}
.shot img.z {{ position: absolute; top: 0; left: 0; opacity: 0; }}
.split > .shot {{ max-height: 800px; }}
.cap2 {{ font-size: 26px; font-weight: 700; color: var(--mute); margin-top: -8px; }}
.cards2, .cards3, .cards4 {{ display: grid; gap: 20px; }}
.cards2 {{ grid-template-columns: 1fr 1fr; }} .cards3 {{ grid-template-columns: repeat(3, 1fr); }} .cards4 {{ grid-template-columns: repeat(2, 1fr); }}
.card {{ background: var(--panel); border-radius: 22px; padding: 24px 28px; display: flex; flex-direction: column; gap: 10px; }}
.card b {{ font-size: 36px; color: var(--blue); font-weight: 800; }}
.card span {{ font-size: 28px; font-weight: 600; line-height: 1.45; }}
.card small {{ font-family: var(--mono); font-size: 22px; color: var(--mute); }}
.cards4 .card {{ padding: 30px 34px; }} .cards4 .card b {{ font-size: 42px; }}
.term {{ background: #05080f; border: 2px solid #26324a; border-radius: 20px; padding: 16px 26px 20px; font-family: var(--mono);
  font-size: 24px; line-height: 1.6; color: var(--ink); white-space: pre; }}
.term i {{ display: block; font-style: normal; font-family: var(--font); font-size: 22px; font-weight: 700; color: var(--mute); margin-bottom: 4px; }}
.term .cmd {{ color: var(--blue); }}
.flow4 {{ display: flex; flex-direction: column; gap: 16px; margin-top: 10px; }}
.fx {{ display: grid; grid-template-columns: 420px 1fr 240px; align-items: center; background: var(--panel); border-radius: 20px; padding: 22px 34px; }}
.fx b {{ font-size: 36px; font-weight: 800; }} .fx span {{ font-family: var(--mono); font-size: 28px; color: var(--mute); }}
.fx em {{ font-style: normal; font-family: var(--mono); font-size: 44px; font-weight: 800; text-align: right; }}
.fx em.ng {{ color: var(--red); }} .fx em.ok {{ color: var(--green); }}
.bars {{ display: flex; align-items: flex-end; gap: 14px; height: 390px; padding: 0 10px; }}
.bc {{ flex: 1; display: flex; flex-direction: column; align-items: center; gap: 6px; }}
.bc i {{ display: block; width: 100%; background: var(--orange); border-radius: 8px 8px 0 0; }}
.bc b {{ font-size: 26px; font-weight: 800; }} .bc small {{ font-family: var(--mono); font-size: 20px; color: var(--mute); }}
.list {{ display: flex; flex-direction: column; gap: 14px; margin-top: 6px; }}
.row {{ display: flex; align-items: center; gap: 30px; background: var(--panel); border-radius: 20px; padding: 16px 34px; font-size: 38px; transform-origin: left center; }}
.row .no {{ font-size: 40px; font-weight: 900; color: var(--orange); width: 36px; }}
.cap {{ top: auto; bottom: 44px; height: auto; display: flex; justify-content: center; padding: 0 200px; }}
.cap span {{ background: rgba(0,0,0,.8); color: #fff; font-size: 36px; font-weight: 700; line-height: 1.4; padding: 10px 26px;
  border-radius: 14px; text-align: center; word-break: keep-all; }}
.bar {{ position: absolute; left: 0; bottom: 0; height: 8px; width: 1920px; background: var(--orange); transform-origin: left center; }}
</style>
</head>
<body>
<div id="root" data-composition-id="main" data-start="0" data-duration="{TOTAL}" data-width="1920" data-height="1080">
  <div class="brand"><b>점프 투 <em>스프링부트</em> · SBB</b><div class="tabs">{tabs}</div></div>
  {chr(10).join(clips)}
  {chr(10).join(caps)}
  <div class="bar"></div>
  <audio id="vo" src="assets/narration.mp3" data-start="0" data-duration="{TOTAL}" data-track-index="0"></audio>
</div>
<script>
const tl = gsap.timeline({{ paused: true }});
function enter(sel, t, dur) {{
  const items = document.querySelectorAll(sel + " .in");
  const step = Math.min(0.9, (dur * 0.45) / Math.max(1, items.length));
  items.forEach((el, i) => {{
    const at = Math.max(0, t - 0.1) + i * step;
    tl.fromTo(el, {{ opacity: 0, y: 40 }}, {{ opacity: 1, y: 0, duration: 0.5, ease: "power3.out" }}, at);
    el.querySelectorAll(".ln").forEach((ln, j) => {{
      tl.fromTo(ln, {{ opacity: 0, x: -24 }}, {{ opacity: 1, x: 0, duration: 0.3, ease: "power2.out" }}, at + 0.15 + j * 0.12);
    }});
  }});
}}
{chr(10).join(js)}
tl.fromTo(".bar", {{ scaleX: 0 }}, {{ scaleX: 1, duration: {TOTAL}, ease: "none" }}, 0);
window.__timelines = window.__timelines || {{}};
window.__timelines["main"] = tl;
tl.seek(0);
</script>
</body>
</html>
'''
open("index.html", "w").write(page)
print("total", TOTAL, "s;", len(caps), "captions")
