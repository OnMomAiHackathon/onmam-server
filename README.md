# onmom-server

서버 URL : http://15.165.54.182:8080/



### ERD
![0903erd_snake](https://github.com/user-attachments/assets/0b146978-de94-446a-8033-e6367adb8f4a)

### Service Architecture
![architecture](https://github.com/user-attachments/assets/881f80c2-e663-45a8-a6b7-7cbd9484f5e0)


### 폴더구조

```plaintext
├─ .github
│  └─ workflows            # GitHub Actions 관련 워크플로우 설정
├─ .gradle                 # Gradle 빌드 도구 관련 파일
├─ .idea                   # IntelliJ IDEA 프로젝트 설정 파일
├─ build                   # 빌드 결과물이 저장되는 디렉터리
├─ gradle                  # Gradle 래퍼 관련 설정
└─ src
    ├─ main
    │  ├─ java
    │  │  ├─ config         # 설정 관련 클래스 (e.g., AI, Web, WebSocket)
    │  │  ├─ controller     # 컨트롤러 계층, HTTP 요청 처리
    │  │  ├─ dto            # 데이터 전송 객체 (DTO)
    │  │  │  ├─ ai
    │  │  │  ├─ auth
    │  │  │  │  └─ login
    │  │  │  ├─ diary
    │  │  │  │  └─ question
    │  │  │  ├─ group
    │  │  │  │  ├─ expel
    │  │  │  │  └─ invite
    │  │  │  ├─ main
    │  │  │  │  └─ groupImage
    │  │  │  ├─ medication
    │  │  │  └─ user
    │  │  │      ├─ get
    │  │  │      └─ join
    │  │  ├─ entity         # 엔티티 클래스 (JPA 관련)
    │  │  │  ├─ diary
    │  │  │  ├─ group
    │  │  │  ├─ medication
    │  │  │  └─ user
    │  │  ├─ exception      # 예외 처리 관련 클래스
    │  │  │  ├─ auth
    │  │  │  │  └─ login
    │  │  │  └─ user
    │  │  │      ├─ get
    │  │  │      └─ join
    │  │  ├─ filter         # 필터 클래스 (e.g., 인증/인가 필터)
    │  │  ├─ mapper         # 매퍼 클래스 (DTO <-> 엔티티 변환)
    │  │  │  ├─ main
    │  │  │  └─ util
    │  │  ├─ repository     # JPA 리포지토리 인터페이스
    │  │  │  ├─ diary
    │  │  │  ├─ group
    │  │  │  ├─ medication
    │  │  │  └─ user
    │  │  ├─ service        # 비즈니스 로직 구현
    │  │  │  └─ ai
    │  │  │      ├─ chatgpt
    │  │  │      └─ dalle
    │  │  ├─ store          # 데이터 저장소 관련 클래스
    │  │  │  └─ onmom
    │  │  │      └─ onmomserver
    │  └─ resources
    │      └─ erd           # 엔티티 관계도(ERD) 파일
    └─ test                 # 테스트 코드가 위치한 디렉터리
