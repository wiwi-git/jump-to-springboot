# 교재 코드를 Spring Boot 4.1.2 / Java 25 / Kotlin DSL 위에서 구현한다

교재는 Spring Boot 3.x, Java 19~20, Gradle Groovy DSL을 쓰지만, 우리는 교재 구현 단계부터 강사 환경(Spring Boot 4.1.2, Java 25, Kotlin DSL)을 쓴다. 개선 커밋은 강사 환경을 기준으로 만들기 때문에, 처음부터 버전을 맞춰 두어야 개선 커밋에 버전 차이로 생긴 수정이 섞이지 않는다. 이 Mac의 기본 JDK가 25라는 점도 고려했다.

## 결과

- 교재 코드가 Spring Boot 4에서 동작하지 않는 곳이 있으면, 차이를 설명하고 동작하는 데 필요한 최소한만 고친다. 그 내용은 교재 구현 커밋 본문에 적는다.
- 교재의 Groovy 빌드 코드는 Kotlin DSL로 옮겨 적고, 문법 차이를 설명한다.
