# SBB 소개 영상 (HyperFrames)

『점프 투 스프링부트』로 만든 SBB와 교재 대비 개선한 점을 소개하는 발표용 롱폼 영상.

- 결과: `output/sbb-intro.mp4` (1920×1080, 30fps, 약 6분 6초, 한국어 나레이션 + 자막)
- 내용의 출처: 저장소 README `## 진행 현황`, 커밋 000~090 본문, `docs/adr/0001-book-code-on-latest-stack.md`
- 제작 방식과 디자인은 `~/claude-test/video1`의 쇼츠 파이프라인(색 토큰, Pretendard, 단어 단위 자막)을 가로 화면으로 옮긴 것

## 구성

도입(작업 방식, 커밋 91개) → 최신 스택 → 만든 것(앱 스크린샷) → 개선 ① 설정·DB → ② JPA → ③ 폼·보안 → ④ 테스트 → 그 밖에 → 정리 → 마무리

## 다시 만들기

```bash
~/claude-test/video1/.venv/bin/python gen_vo.py   # script.json → assets/vo/*.mp3, vo.json (Edge TTS ko-KR-SunHiNeural, +12%)
python3 build.py                                   # → assets/narration.mp3, index.html
npx --yes hyperframes@0.8.214 check
npx --yes hyperframes@0.8.214 render --fps 30 --crf 23 -o output/sbb-intro.mp4
```

스크린샷(`assets/shots/`)은 앱을 8091 포트로 띄우고 데모 데이터를 넣은 뒤 `node shots.mjs <puppeteer-core 경로> <마크다운 질문 id>`로 찍었다.
