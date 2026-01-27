# Book and Talk

원하는 책에 대한 독서 모임을 만들고 참여하는 독서 모임 서비스입니다.

개발 기간: 2024.10 ~ 2024.11(이후 지속 개선 중)

## 기술 스택

| 분류 | 기술 |
|------|------|
| Framework | Spring Boot, Java |
| Database | PostgreSQL, PostGIS |
| ORM | Spring Data JPA, Hibernate Spatial |
| Authentication | Spring Security |
| Real-time | WebSocket, STOMP |
| Storage | AWS S3 |

## 주요 기능

### 독서 모임
사용자가 선택한 책을 기반으로 모임을 생성할 수 있습니다. 이후 해당 책을 통해 모임 리스트를 조회할 수 있습니다.
책은 ISBN 기반으로 구분합니다.

### 모임 참가 신청
사용자는 관심 있는 모임에 참가 신청을 할 수 있습니다. 모임을 생성한 호스트는 신청 목록을 확인하고 승인 또는 거절할 수 있으며, 신청자는 승인 전까지 신청을 취소할 수 있습니다.

### 위치 기반 검색
사용자가 선택한 위치와 반경(추가 예정)에 맞추어 선택한 위치 근처에 있는 모임 리스트를 조회할 수 있습니다. PostGIS의 ST_DWithin 함수를 통해 구현하였고, GIST 인덱스를 적용하여 응답 속도를 향상했습니다. 사용자가 선택한 위치로부터 거리순으로 모임을 정렬할 수 있습니다.

### 실시간 채팅
모임이 생성될 경우 단체 채팅방이 자동으로 생성됩니다. 참가 신청이 승인될 경우 신청한 사용자는 채팅방에 자동으로 참가합니다. WebSocket, STOMP을 활용했습니다.

## 프로젝트 구조

```
src/main/java/com/talk/book/
├── config/           # 설정 (S3, PostGIS, WebSocket)
├── controller/       # REST API 컨트롤러
├── domain/           # JPA 엔티티
├── dto/              # Data Transfer Objects
├── enumerate/        # Enum 정의
├── repository/       # Spring Data JPA Repository
├── service/          # 비즈니스 로직
└── security/         # Spring Security 설정
```