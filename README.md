# Scheduler_LOA

로스트 아크 숙제를 위한 주간 콘텐츠 스케줄링 웹사이트입니다.

Backend는 Java Spring Boot와 MariaDB로 개발 중에 있습니다.
Frontend는 React JS로 개발 중에 있습니다.

배포는 AWS EC2 서버에 배포 중입니다.

## 버전 정보

- Backend
: Java openjdk version "17.0.5" 2022-10-18 LTS / mysql Ver 15.1 Distrib 10.10.2-MariaDB

- Frontend
: nodeJS v14.20.0 / npm v6.14.17

- AWS
: EC2 Ubuntu-22.04-amd64 프리티어 / EBS Volumes

## Backend

### 기본 사항

Java Spring Boot와 MariaDB로 개발하고 있습니다.

서버 포트번호는 "8005" 입니다.
DB 포트번호는 "3006" 입니다.

### DB 세팅

MariaDB 계정과 비밀번호는 "root", "root" 입니다.
기본 DB 이름은 "scheduleDB"로 하셔야 합니다.

실행 전에 MariaDB 설치 후 "shceduleDB"를 설치하시기 바랍니다.

### 실행 방법
    
/src/main/java/com/example/loa/LoaApplication을 실행합니다.

## Frontend

### 기본사항

ReactJS로 개발하고 있습니다.
빌드 시, Spring Boot에서 프론트를 우선 빌드해줍니다.

### 실행 방법

이하 설명은 Frontend 개발 및 테스트 시에 별도로 실행하는 방법입니다.

/src/main/frontend 위치에서 다음 코드 실행

```bash
npm start
```

