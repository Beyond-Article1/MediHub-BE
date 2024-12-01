# MediHub-BE


## 👩🏻‍💻 제1조1항 🧑🏻‍💻

| [![](https://avatars.githubusercontent.com/u/173455095?v=4)](https://github.com/SANGHYUN0519) | [![](https://avatars.githubusercontent.com/u/158060587?v=4)](https://github.com/ygc1994) | [![](https://avatars.githubusercontent.com/u/173458380?v=4)](https://github.com/JIYOUNG-22) | [![](https://avatars.githubusercontent.com/u/103546300?v=4)](https://github.com/Pangtaek) | [![](https://avatars.githubusercontent.com/u/94957124?v=4)](https://github.com/person76) | [<img src="https://github.com/user-attachments/assets/4d62fcb6-1511-4e22-86da-1704a26180d9" width="450" />](https://github.com/PBEM22) |
|--------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------|------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------|
| <p align="center">박상현</p> | <p align="center">연건창</p> | <p align="center">윤지영</p> | <p align="center">임광택</p> | <p align="center">임서연</p> | <p align="center">임채륜</p> |

<br>

## 🛠️ 기술 스택
### Backend
<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"> <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
<img src="https://img.shields.io/badge/spring data JPA-6DB33F?style=for-the-badge&logo=hibernate&logoColor=white">
<img src="https://img.shields.io/badge/spring%20security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white">
<img src="https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white">
<br>
<img src="https://img.shields.io/badge/socket.io-010101?style=for-the-badge&logo=socketdotio&logoColor=white">
<img src="https://img.shields.io/badge/pubmed-326599?style=for-the-badge&logo=pubmed&logoColor=white">
<br>

### Frontend
<img src="https://img.shields.io/badge/html5-E34F26?style=for-the-badge&logo=html5&logoColor=white"> <img src="https://img.shields.io/badge/css-1572B6?style=for-the-badge&logo=css3&logoColor=white"> 
<img src="https://img.shields.io/badge/javascript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black">
<img src="https://img.shields.io/badge/vue.js-4FC08D?style=for-the-badge&logo=vue.js&logoColor=white">
<br>

### Database
<img src="https://img.shields.io/badge/mariadb-4479A1?style=for-the-badge&logo=mariadb&logoColor=white"> <img src="https://img.shields.io/badge/mongoDB-47A248?style=for-the-badge&logo=MongoDB&logoColor=white">
<img src="https://img.shields.io/badge/elasticsearch-005571?style=for-the-badge&logo=elasticsearch&logoColor=white">
<img src="https://img.shields.io/badge/Amazon%20S3-FF9900?style=for-the-badge&logo=amazons3&logoColor=white">
<br>

### CI/CD
<img src="https://img.shields.io/badge/GithubActions-2088FF.svg?style=for-the-badge&logo=GithubActions&logoColor=white"> <img src="https://img.shields.io/badge/aws-232F3E.svg?style=for-the-badge&logo=amazonwebservices&logoColor=white">
<br>

### Tool
<img src="https://img.shields.io/badge/swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=white"> <img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">
<img src="https://img.shields.io/badge/figma-F24E1E?style=for-the-badge&logo=figma&logoColor=white">
<img src="https://img.shields.io/badge/slack-4A154B?style=for-the-badge&logo=slack&logoColor=white">
<img src="https://img.shields.io/badge/discord-5865F2?style=for-the-badge&logo=discord&logoColor=white">

<br>

## 📜 목차
1. [프로젝트 개요](#1-프로젝트-개요)
2. [주요 기능](#2-주요-기능)
3. [기대 효과](#3-기대-효과)
4. [프로젝트 설계 문서](#4-프로젝트-설계-문서)

<hr>
<br>

## 1. 프로젝트 개요
### MediHub

> MediHub는 병원 내 의료진과 의료 관계자들이 효율적으로 협업하고 지식 공유 및 축적을 통해 의료 서비스의 품질을 향상시키기 위한 지식 관리 시스템(KMS)입니다.
>
> 병원의 의료 수준은 개개인의 지식과 다수 의료진의 협업으로 결정되므로 지식의 효율적인 관리와 빠른 업데이트가 필수적입니다.
> 그러므로 병원 내에서 지식은 암묵적 지식에서 명시적 지식으로 변환되어야 합니다.
> 이를 통해 의료진과 직원들이 각자 보유한 업무 경험, 진료 지침(CP), 의료 논문 등을 조직적으로 공유하고 축적할 수 있습니다.
>
>이를 위해 MediHub는 병원 내 정보의 일원화를 추진하고 실시간 정보 공유와 협업을 촉진하는 다양한 기능을 제공합니다.

<br>

## 2. 주요 기능
의료진 및 병원 직원들이 의료 케이스, CP 등 분산된 데이터를 검색 엔진을 통해 한 곳에서 통합적•효율적으로 조회할 수 있도록 지원합니다.
<br>
또한 자신의 의견을 자유롭게 개진할 수 있는 게시판과 실시간 채팅 기능은 직원 간 협업과 커뮤니케이션을 지원하여 사용자 편의성을 극대화합니다.

### 1. 지식 관리
- <b>CP(Clinical Pathway):</b> 병원 내 치료 매뉴얼 문서(CP)를 병원 내 관계자들이 쉽게 접근하고 수정을 건의할 수 있는 기능 제공
- <b>Case Sharing(의료 케이스 공유):</b> 희귀병 치료 등 다양한 의료 사례 공유 및 논의를 통한 의료 사고 예방 및 치료 성과 개선
- <b>ChatGPT + PubMed(논문):</b> 최신 논문과 연구 결과를 효율적으로 검색할 수 있는 기능을 통해 의료진이 필요한 정보를 빠르게 찾을 수 있도록 지원
- <b>Medical Life(부서 게시판):</b> 부서별 게시판을 통해 각 부서의 전문성을 바탕으로 정보 공유 및 효율적인 업무 처리 지원. 다양한 직군 간의 정보 교류를 통해 지식의 지속적인 축적과 확장 촉진

### 2. 정보 검색 및 접근
- <b>Elasticsearch:</b> 대량의 의료 데이터를 신속하고 효율적으로 검색하고 분석할 수 있도록 지원
- <b>Newsapi:</b> 최신 의료 뉴스를 검색하고 통합하여 관계자들이 정보에 쉽게 접근할 수 있도록 지원

### 3. 협업 및 커뮤니케이션
- <b>익명 게시판(Anonymous Board)</b> 병원 내 모든 관계자가 개인적인 의견이나 질문을 자유롭게 개진하고 정보를 공유할 수 있는 환경 제공
- <b>MediTalk(메신저):</b> 응급 상황이나 신속한 의사 결정이 필요한 경우 직원 간 실시간으로 소통할 수 있는 메신저 기능 제공
- <b>알림:</b> 팔로우 한 의료진이 새로운 CP나 Case Sharing 등을 작성했을 때 실시간으로 알림 메시지를 전송하여 최신 정보를 신속하게 공유하고 협업을 촉진

### 4. 사용자 지원 및 자동화
- <b>ChatBot:</b> 질문과 답변을 실시간으로 처리할 수 있어 정보 검색의 편의성 향상

<br>

## 3. 기대 효과
- 의료 서비스 품질 향상: 업무 효율성•생산성•대응 능력 향상 등을 통한 성과 도출 및 의료 환경의 발전에 긍정적인 효과 기대
- 환자의 만족도 제고: 최신 연구 결과와 치료 방법을 신속하게 공유함으로써 수준 높은 의료 서비스 제공하여 환자의 만족도 제고
- 지식의 지속적인 축적과 확장: 병원 내 관계자들의 지식 공유성과 활용성을 강화하여 문제 해결 능력의 제고와 의사 결정 수준의 증진
- 병원 내 협업 문화 확산


<br>

## 4. 프로젝트 설계 문서
### 4-1. 프로젝트 기획서
[프로젝트 기획서](https://docs.google.com/document/d/1-ND5Wz1T14ndjffN6jAfWhBTUUeX5B-A93sHcd4FhfM/edit?tab=t.0)

### 4-2. WBS
[WBS](https://docs.google.com/spreadsheets/d/1sWHGbgL9R79AbUzCAmxKIulggjMqbi1lwv-RsJdtgVA/edit?gid=1194459417#gid=1194459417)

### 4-3. DDD 설계
[DDD](https://miro.com/app/board/uXjVLFIor4Y=/)

### 4-4. 요구사항 명세서
[요구사항명세서](https://docs.google.com/spreadsheets/d/1sWHGbgL9R79AbUzCAmxKIulggjMqbi1lwv-RsJdtgVA/edit?gid=205274512#gid=205274512)

### 4-5. Database 설계
[ERD](https://www.erdcloud.com/d/EmGTuF2SJmXwbLNiv)
<br>
<details>
<summary>MongoDB Schema</summary>
<div markdown="1">

### chat.users Collection
```mongodb-json
{
  "UserSeq": "123",
  "Name": "홍길동",
  "Groups": ["group1", "group2"],  // 사용자가 참여한 그룹들
  "GroupCount": 2,                 // 참여 채팅방 개수
  "Status": "online",              // 사용자 상태 (online, offline, away 등)
  "ProfileImage": "https://example.com/profile.jpg",  // 프로필 사진 URL
  "LastActiveAt": "2024-11-29T15:34:21Z",               // 마지막 활동 시간
  "GlobalMute": false              // 모든 채팅방에서 알림 음소거 여부
}
```

### chat.groups Collection
```mongodb-json
{
  "GroupSeq": "group1",
  "Name": "정형외과 공지방",
  "TotalMessages": 150,  // 총 메시지 수
  "LastMessageAt": "2024-11-29T15:34:21Z",  // 마지막 메시지 전송 시간
  "LastModified": "2024-11-29T15:00:00Z",   // 채팅방 마지막 수정 시간 (예: 이름 변경, 참여자 추가 등)
  "CreatedAt" : "2024-11-29T15:00:00Z",     // 채팅방 생성 시간
  "DeletedAt" : "",                         // 채팅방 삭제 시간
  "Users": [
    {
      "userSeq": "123",
      "RoomName" : "공지공지방",               // 개인별 채팅방 이름 설정
      "TotalUnread": 5,                     // 읽지 않은 메시지 수
      "JoinedAt": "2024-11-01T10:00:00Z"    // 채팅방에 참여한 시간
    },
    {
      "userId": "456",
      "RoomName" : "읽지마",
      "TotalUnread": 2,
      "JoinedAt": "2024-11-10T14:00:00Z"
    }
  ]
}
```

### chat.messages Collection
```mongodb-json
{
  "MessageSeq": "msg1",
  "GroupSeq": "group1",
  "MessageContent": "회의자료를 첨부합니다.",
  "SendUser": {
    "userSeq": "123",
    "Name": "홍길동",
    "ProfileImage": "https://example.com/profile.jpg",  // 프로필 사진 URL
  },
  "CreatedAt": "2024-11-29T15:34:21Z",  // 메시지 전송 시간
  "Type": "file",                       // 메시지 타입 (text, image, file 등)
  "IsDeleted": false,                   // 메시지 삭제 여부
  "Attachments": [                      // 첨부파일 정보
    {
      "FileType": "image",  // 파일 타입 (image, document, video 등)
      "FileUrl": "https://example.com/path/to/image.jpg",  // 파일의 URL
      "FileName": "image.jpg",  // 파일 이름
      "FileSize": 123456  // 파일 크기 (바이트 단위)
    },
    {
      "FileType": "document",
      "FileUrl": "https://example.com/path/to/document.pdf",
      "FileName": "meeting_minutes.pdf",
      "FileSize": 234567
    }
  ]
}
```

### chat.files Collection
```mongodb-json
{
  "FileSeq": "12345",  // 자동 생성된 고유 ID (ObjectId)
  "UserSeq": "user123",  // 파일을 받은 사용자 ID
  "GroupSeq": "group1",  // 파일이 속한 채팅방 ID
  "MessageSeq": "msg123",  // 해당 파일이 포함된 메시지 ID
  "FileType": "image",  // 파일 유형 (image, document, video 등)
  "FileUrl": "https://example.com/path/to/image.jpg",  // 파일의 URL
  "FileName": "image.jpg",  // 파일 이름
  "FileSize": 1048576,  // 파일 크기 (바이트 단위)
  "UploadedAt": "2024-11-29T15:34:21Z",  // 파일이 첨부된 시간
  "ReceivedAt": "2024-11-29T15:35:21Z",  // 사용자가 파일을 받은 시간
  "ReadBy": ["123", "456"]  // 파일을 다운로드한/열어본 사용자 목록
}
```


</div>
</details>

### 4-6. 화면 설계서
[화면 설계서 (Figma)](https://www.figma.com/design/SiXw9eqSH2yS3GFtUxoINf/MediHub?node-id=0-1&node-type=canvas&t=yScI0V8n8O79M0vo-0)

### 4-7. 시스템 아키텍처
![systemArchitecture](https://github.com/user-attachments/assets/7bc3d280-2276-4c57-b946-811271441017)