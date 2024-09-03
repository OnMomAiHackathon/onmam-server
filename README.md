# onmom-server

서버 URL : [http://15.165.54.182:8080/](http://15.165.54.182:8080/)

## 프로젝트 소개

### 프로젝트 개요

독거노인과 그 가족 간의 정서적 연결을 돕기 위해 AI 기반 그림일기 서비스를 제공하는 웹 애플리케이션입니다. 백엔드에서는 가족 그룹 관리, AI와의 상호작용을 통한 일기 생성, 그리고 가족 간의 정보 공유 기능을 중심으로 구축되었습니다.

## 🛠 기술 스택

1. **BE**
   - **Spring Data JPA**의 **영속성 컨텍스트**를 활용하여 데이터의 일관성을 유지하며, 로직 수행 중 예외발생시 **트랜잭션 롤백**을 관리하여 안정적인 데이터 처리를 보장했습니다.

2. **DB**
   - 관계형 데이터 모델링과 높은 읽기 성능, 저렴한 비용을 고려하여 **AWS RDS MySQL t3.micro**를 사용했습니다.

3. **Infra**
   - 추후 서버 증설을 고려해 **AWS EC2**를 사용한 서버 인프라를 구축하고, **AWS S3**를 활용한 파일을 저장했습니다.

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=Spring%20Boot&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-6DB33F?style=for-the-badge&logo=Spring%20&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=MySQL&logoColor=white)
![AWS S3 SDK](https://img.shields.io/badge/AWS%20S3%20SDK-FF9900?style=for-the-badge&logo=Amazon%20AWS&logoColor=white)
![AWS EC2](https://img.shields.io/badge/AWS%20EC2-FF9900?style=for-the-badge&logo=Amazon%20AWS&logoColor=white)
![AWS CodeDeploy](https://img.shields.io/badge/AWS%20CodeDeploy-FF9900?style=for-the-badge&logo=Amazon%20AWS&logoColor=white)
![AWS Secrets Manager](https://img.shields.io/badge/AWS%20Secrets%20Manager-FF9900?style=for-the-badge&logo=Amazon%20AWS&logoColor=white)

### 주요 기능 및 API

**주요 기능**

- **그룹 생성 및 관리**: 가족 구성원 간의 그룹을 통해 가족 간 사진 공유, AI 그림일기 공유, 복약 스케줄을 공유할 수 있습니다.
- **부모님 복약 스케줄 관리 **: 자식들이 부모님의 복약 정보와 검진 일정을 등록하고 관리할 수 있도록 지원하여, 부모님의 건강을 체계적으로 관리할 수 있습니다.
- **AI 기반 그림일기 생성**: 어르신들이 지정된 서비스 질문을 통해 그림일기를 생성할 수 있도록 했습니다. 어르신의 음성을 whisper를 통해 번역 및 chatGPT를 통해 그림일기로 요약하고, 이를 바탕으로 dalle를 이용한 그림일기를 생성합니다.
- **가족 간 일기 공유**: 생성된 일기와 그림을 가족들이 쉽게 조회하고, 공유할 수 있도록 지원하여, SNS 사용 없이도 가족 간 소통이 가능하도록 했습니다.


**전체 API Swagger 페이지에서 보기 :** : [Link](http://15.165.54.182:8080/swagger-ui/index.html)
![api명세](https://github.com/user-attachments/assets/5cf6ddad-fded-4260-9da1-55eb4a88978b)


**특이사항**

- **생성형 AI의 활용**: OpenAI와 같은 최신 AI 기술을 통합하여 사용자 경험을 향상시켰습니다.

    - **STT (Speech-to-Text)**: OpenAI의 Whisper를 사용하여 어르신의 음성을 텍스트로 전사합니다.
    - **AI 이미지 생성**: DALL·E를 활용해 어르신의 이야기에서 생성된 텍스트를 기반으로 그림을 자동 생성합니다.
    - **대화 요약**: ChatGPT 3.5를 사용하여 어르신과의 대화를 요약하고, 그 내용을 바탕으로 일기를 작성합니다.

## ERD

![ERD](https://github.com/user-attachments/assets/0b146978-de94-446a-8033-e6367adb8f4a)

## 아키텍처

아키텍처 설계시 중요시한 부분은 ‘신속한 배포를 통해 주어진 시간 동안 목표한 API를 모두 제공하는 것’이었습니다. 따라서 소규모 프로젝트에 맞게 아래와 같이 아키텍처를 설계하였습니다.

- **시스템 아키텍처**
    - 모놀리식 아키텍처를 채택하여 프로젝트 복잡도를 줄이고자 했습니다.
- **배포 아키텍처**
    - **배포 전략**: In-place 전략을 통해 배포 시 다운타임을 허용했습니다. 배포 속도를 최적화 및 간소화된 프로세스로 시간/비용 효율적인 배포를 구현했습니다.
    - **CI/CD**: GitHub Actions를 활용하여 CI/CD 파이프라인을 구축했습니다. 이를 통해 코드 변경 시 자동으로 빌드, 테스트, 배포가 이루어지도록 하여 배포 프로세스를 자동화하고, 신속한 배포를 지향했습니다.

![아키텍처](https://github.com/user-attachments/assets/881f80c2-e663-45a8-a6b7-7cbd9484f5e0)

## 패키지 구조

**생산성이 높은 프로젝트 수행을 위해 아래와 같은 패키지 구조로 설계했습니다.**

- 소규모 프로젝트 특성상 도메인이 많지 않기 떄문에 ‘패키지 하위에 도메인이 포함된 구조’로 설계하여 프로젝트 단순성을 높였습니다.
- 구체적으로, 기능별 패키지(controller, service, repository, entity, dto)를 최상위에 배치하고, 그 하위에 각 도메인 관련 클래스 혹은 패키지(ai, auth, diary 등)를 구분했습니다.

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
    └─ test                 
```
