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
<img src="https://img.shields.io/badge/apachekafka-231F20?style=for-the-badge&logo=apachekafka&logoColor=white">
<br>

### Frontend
<img src="https://img.shields.io/badge/html5-E34F26?style=for-the-badge&logo=html5&logoColor=white"> <img src="https://img.shields.io/badge/css-1572B6?style=for-the-badge&logo=css3&logoColor=white"> 
<img src="https://img.shields.io/badge/javascript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black">
<img src="https://img.shields.io/badge/vue.js-4FC08D?style=for-the-badge&logo=vue.js&logoColor=white">
<img src="https://img.shields.io/badge/bootstrap-7952B3?style=for-the-badge&logo=bootstrap&logoColor=white">
<br>

### Database
<img src="https://img.shields.io/badge/mariadb-4479A1?style=for-the-badge&logo=mariadb&logoColor=white"> <img src="https://img.shields.io/badge/mongoDB-47A248?style=for-the-badge&logo=MongoDB&logoColor=white">
<img src="https://img.shields.io/badge/elasticsearch-005571?style=for-the-badge&logo=elasticsearch&logoColor=white">
<img src="https://img.shields.io/badge/Amazon%20S3-FF9900?style=for-the-badge&logo=amazons3&logoColor=white">
<img src="https://img.shields.io/badge/redis-FF4438?style=for-the-badge&logo=redis&logoColor=white">
<br>

### 외부 API
<img src="https://img.shields.io/badge/pubmed-326599?style=for-the-badge&logo=pubmed&logoColor=white">


### CI/CD
<img src="https://img.shields.io/badge/GithubActions-2088FF.svg?style=for-the-badge&logo=GithubActions&logoColor=white"> <img src="https://img.shields.io/badge/aws-232F3E.svg?style=for-the-badge&logo=amazonwebservices&logoColor=white">
<img src="https://img.shields.io/badge/amazoneks-FF9900?style=for-the-badge&logo=amazoneks&logoColor=white">
<img src="https://img.shields.io/badge/docker-2496ED?style=for-the-badge&logo=docker&logoColor=white">
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
5. [백엔드 설계 및 구축](#5-백엔드-설계-및-구축)
6. [프론트엔드 설계 및 구축](#6-프론트엔드-설계-및-구축)
7. [시스템 통합](#7-시스템-통합)
8. [회고](#8-회고)

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

### chat.messages Collection
```mongodb-json
// chatMessageSchema.js

const mongoose = require('mongoose');

// 첨부파일(Attachment) 서브문서 스키마 정의
const attachmentSchema = new mongoose.Schema({
  originName: { type: String, required: false }, // 원본 파일 이름
  url: { type: String, required: false }        // S3 URL
});

// ChatMessage 스키마 정의
const chatMessageSchema = new mongoose.Schema({
  chatroomSeq: { type: Number, required: true },       // 채팅방 식별자
  senderUserSeq: { type: Number, required: true },     // 발신자 사용자 식별자
  type: { type: String, required: true },              // 메시지 타입 (예: text, file, image)
  message: { type: String, required: false },          // 메시지 내용
  createdAt: { type: Date, default: Date.now },        // 메시지 생성 시간 (기본값 현재 시간)
  isDeleted: { type: Boolean, default: false },        // 메시지 삭제 여부
  attachment: { type: attachmentSchema, required: false } // 첨부파일 서브문서
});

// 복합 인덱스 설정 (chatroomSeq, createdAt, isDeleted 기준으로 쿼리 최적화)
chatMessageSchema.index(
  { chatroomSeq: 1, createdAt: 1, isDeleted: 1 },
  { name: "chatroom_createdAt_isDeleted_idx" }
);

// 모델 내보내기
module.exports = mongoose.model('ChatMessage', chatMessageSchema);

```

</div>
</details>

### 4-6. 화면 설계서
[화면 설계서 (Figma)](https://www.figma.com/design/SiXw9eqSH2yS3GFtUxoINf/MediHub?node-id=0-1&node-type=canvas&t=yScI0V8n8O79M0vo-0)

### 4-7. 시스템 아키텍처
![systemArchitecture](https://github.com/user-attachments/assets/7bc3d280-2276-4c57-b946-811271441017)

<br>

## 5. 백엔드 설계 및 구축
### 5-1. 프로그램 사양서
|구성요소| 사양|
|:----------------------------------------------------------------------------------------------:|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|CloudFront(Frontend)| Vue.js, Vite|
|EKS| t3.large(2 vCPU, 8GB RAM), Nginx, Spring Boot|
|RDS| db.t3.medium(2 vCPU, 4GB RAM), MariaDB, MongoDB, redis|
|S3| 5GB 표준 스토리지, 총 2개의 버킷(프론트엔드 프로젝트, 업로드 파일)|

### 5-2. 단위 테스트 결과서
[단위 테스트 결과서 (Sheet)](https://docs.google.com/spreadsheets/d/1sWHGbgL9R79AbUzCAmxKIulggjMqbi1lwv-RsJdtgVA/edit?gid=922908943#gid=922908943)

<br>

## 6. 프론트엔드 설계 및 구축
### 6-1. 기능 수행 테스트 결과
#### 6-1-1. User (사용자)
<details>
<summary>내 정보 수정</summary>
<div markdown="1">

![내정보수정](/images/User/1.내정보수정.gif)

</div>
</details>

<details>
<summary>팔로우</summary>
<div markdown="1">

![팔로우](/images/User/2.팔로우.gif)

</div>
</details>

<details>
<summary>나의 Case Sharing</summary>
<div markdown="1">

![MyCaseSharing](/images/User/3.나의Case_Sharing.gif)

</div>
</details>

<details>
<summary>나의 Medical Life</summary>
<div markdown="1">

![MyMedicalLife](/images/User/04.나의메디컬라이프.gif)

</div>
</details>

<details>
<summary>나의 익명게시판</summary>
<div markdown="1">

![MyAnonymousBoard](/images/User/05.나의익명게시판.gif)
}

</div>
</details>

<details>
<summary>나의 CP</summary>
<div markdown="1">

![MyCP](/images/User/6.나의CP.gif)

</div>
</details>

<details>
<summary>나의 논문</summary>
<div markdown="1">

![MyJournal](/images/User/7.나의논문.gif)

</div>
</details>

<br>

#### 6-1-2. Admin (관리자)

<details>
<summary>로그인</summary>
<div markdown="1">

![Login](/images/Admin/1.로그인.gif)

</div>
</details>

<details>
<summary>회원 조회 및 회원 등록</summary>
<div markdown="1">

![회원조회및등록](/images/Admin/2.회원조회및회원등록.gif)

</div>
</details>

<details>
<summary>부서 등록</summary>
<div markdown="1">

![부서등록](/images/Admin/3.부서등록.gif)

</div>
</details>

<details>
<summary>파트 관리</summary>
<div markdown="1">

![파트관리](/images/Admin/4.파트관리.gif)

</div>
</details>

<details>
<summary>직급관리</summary>
<div markdown="1">

![직급관리](/images/Admin/5.직급관리.gif)

</div>
</details>

<details>
<summary>회원 정보 수정</summary>
<div markdown="1">

![회원정보수정](/images/Admin/6.관리자회원정보수정.gif)

</div>
</details>

<details>
<summary>비밀번호 초기화</summary>
<div markdown="1">

![비밀번호초기화](/images/Admin/7.비밀번호초기화.gif)

</div>
</details>

<br>

#### 6-1-3. Case Sharing (케이스 공유)
<details>
<summary>케이스 공유 작성 및 조회</summary>
<div markdown="1">

![케이스공유작성및조회](/images/CaseSharing/1.케이스공유_작성_및_조회.gif)

</div>
</details>

<details>
<summary>케이스 공유 새 버전 작성</summary>
<div markdown="1">

![케이스공유새버전작성](/images/CaseSharing/2.케이스공유_새_버전_작성.gif)

</div>
</details>

<details>
<summary>케이스 공유 댓글 작성</summary>
<div markdown="1">

![케이스공유댓글작성](/images/CaseSharing/3_케이스공유_댓글작성.gif)

</div>
</details>

<details>
<summary>케이스 공유 댓글 수정</summary>
<div markdown="1">

![케이스공유댓글수정](/images/CaseSharing/4_케이스공유_댓글수정.gif)

</div>
</details>

<details>
<summary>케이스 공유 알림 댓글 조회</summary>
<div markdown="1">

![케이스공유알림](/images/CaseSharing/5.케이스공유_알림_댓글조회.gif)

</div>
</details>

<br>

#### 6-1-4. CP (표준 진료 지침)
<details>
<summary>CP 목록 조회</summary>
<div markdown="1">

![CPList](/images/CP/CP/[CP]목록_조회.gif)

</div>
</details>

<details>
<summary>CP 조회 (검색 카테고리 사용)</summary>
<div markdown="1">

![CPListByCategory](/images/CP/CP/[CP]조회(검색_카테고리_사용).gif)

</div>
</details>

<details>
<summary>CP 상세 조회</summary>
<div markdown="1">

![CPDetail](/images/CP/CP/[CP]상세_조회.gif)

</div>
</details>

<details>
<summary>CP 북마크 목록</summary>
<div markdown="1">

![CPBookmark](/images/CP/CP/[CP]북마크(목록).gif)

</div>
</details>

<br>

<details>
<summary>CP 의견 마커 표시</summary>
<div markdown="1">

![CPOpinion](/images/CP/CP%20의견/[CP]의견_마커_표시.gif)

</div>
</details>

<details>
<summary>CP 의견 생성</summary>
<div markdown="1">

![CP의견생성](/images/CP/CP%20의견/[CP]의견_생성.gif)

</div>
</details>

<details>
<summary>CP 의견 목록 조회</summary>
<div markdown="1">

![CP 의견 목록 조회](/images/CP/CP%20의견/[CP]의견_목록_조회.gif)

</div>
</details>
<details>
<summary>CP 의견 상세 조회</summary>
<div markdown="1">

![CP 의견 상세 조회](/images/CP/CP%20의견/[CP]의견_상세_조회.gif)

</div>
</details>
<details>
<summary>CP 의견 수정</summary>
<div markdown="1">

![CP의견수정](/images/CP/CP%20의견/[CP]의견_수정.gif)

</div>
</details>

<details>
<summary>CP 의견 삭제</summary>
<div markdown="1">

![CP의견삭제](/images/CP/CP%20의견/[CP]의견_삭제.gif)

</div>
</details>

<details>
<summary>CP 의견 투표 및 결과 확인</summary>
<div markdown="1">

![CP 의견 투표 및 결과 확인](/images/CP/CP%20의견/[CP]의견_투표_및_결과_확인.gif)

</div>
</details>

<details>
<summary>북마크 (CP 상세기보 내부)</summary>
<div markdown="1">

![CP북마크](/images/CP/CP%20의견/[CP]북마크(CP세상보기).gif)

</div>
</details>

<details>
<summary>CP 다운로드</summary>
<div markdown="1">

![CP 다운로드](/images/CP/CP%20의견/[CP]다운로드.gif)

</div>
</details>

<br>

<details>
<summary>CP 검색 카테고리 생성</summary>
<div markdown="1">

![](/images/CP/CP%20검색%20카테고리/[CP_검색_카테고리]생성.gif)

</div>
</details>

<details>
<summary>CP 검색 카테고리 수정</summary>
<div markdown="1">

![](/images/CP/CP%20검색%20카테고리/[CP_검색_카테고리]수정.gif)

</div>
</details>

<details>
<summary>CP 검색 카테고리 삭제</summary>
<div markdown="1">

![](/images/CP/CP%20검색%20카테고리/[CP_검색_카테고리]삭제.gif)

</div>
</details>

<details>
<summary>CP 검색 카테고리 데이터 생성</summary>
<div markdown="1">

![](/images/CP/CP%20검색%20카테고리/[CP_검색_카테고리_데이터]생성.gif)

</div>
</details>

<details>
<summary>CP 검색 카테고리 데이터 수정</summary>
<div markdown="1">

![](/images/CP/CP%20검색%20카테고리/[CP_검색_카테고리_데이터]수정.gif)

</div>
</details>

<details>
<summary>CP 검색 카테고리 데이터 삭제</summary>
<div markdown="1">

![](/images/CP/CP%20검색%20카테고리/[CP_검색_카테고리_데이터]삭제.gif)

</div>
</details>

<br>

#### 6-1-5. 논문
<details>
<summary>논문</summary>
<div markdown="1">

![Journal](/images/Journal/논문검색.gif)

</div>
</details>

<br>

#### 6-1-6. Elasticsearch (엘라스틱서치)
<details>
<summary>추천 검색어</summary>
<div markdown="1">

![추천검색어](/images/Elasticsearch/1.추천검색어.gif)

</div>
</details>

<details>
<summary>통합 검색</summary>
<div markdown="1">

![통합검색](/images/Elasticsearch/2.통합검색.gif)

</div>
</details>

<details>
<summary>Case Sharing 검색</summary>
<div markdown="1">

![케이스공유](/images/Elasticsearch/3.케이스공유.gif)

</div>
</details>

<details>
<summary>Medical Life 검색</summary>
<div markdown="1">

![메디컬라이프](/images/Elasticsearch/4.메디컬라이프.gif)

</div>
</details>

<details>
<summary>Anonymous Board 검색</summary>
<div markdown="1">

![익명게시판](/images/Elasticsearch/5.익명게시판.gif)

</div>
</details>


<br>

#### 6-1-7. Chatbot (챗봇)
<details>
<summary>챗봇</summary>
<div markdown="1">

![Chatbot](/images/Chatbot/Chatbot.gif)

</div>
</details>

<br>

#### 6-1-8. MediTalk (채팅)
<details>
<summary>조직도 조회 및 검색</summary>
<div markdown="1">

![Organization](/images/Chat/조직도조회및검색.gif)

</div>
</details>

<details>
<summary>1:1 채팅방 생성</summary>
<div markdown="1">

![](/images/Chat/.gif)

</div>
</details>

<details>
<summary>단체 채팅방 생성</summary>
<div markdown="1">

![Chatroom](/images/Chat/다대다채팅방생성.gif)

</div>
</details>

<details>
<summary>채팅방 목록 조회 및 검색</summary>
<div markdown="1">

![ChatroomList](/images/Chat/채팅방목록조회및검색.gif)

</div>
</details>

<details>
<summary>채팅방 이름 수정, 대화상대 추가</summary>
<div markdown="1">

![OneToOneChatroom](/images/Chat/1대1채팅방생성.gif)

</div>
</details>

<details>
<summary>메시지 및 첨부파일 전송</summary>
<div markdown="1">

![ChatMessage](/images/Chat/메시지및첨부파일전송.gif)

</div>
</details>

<details>
<summary>메시지 삭제</summary>
<div markdown="1">

![DeleteMessage](/images/Chat/메시지삭제.gif)

</div>
</details>

<details>
<summary>채팅방 나가기</summary>
<div markdown="1">

![LeaveChatroom](/images/Chat/채팅방나가기.gif)

</div>
</details>

<details>
<summary>파일함 조회 및 검색</summary>
<div markdown="1">

![FileBox](/images/Chat/파일함조회검색.gif)

</div>
</details>

<br>

#### 6-1-9. MedicalLife (부서별 게시판)
<details>
<summary>Medical Life</summary>
<div markdown="1">

![MedicalLife](/images/MedicalLife/1.메디컬라이프.gif)

</div>
</details>

<details>
<summary>Medical Life 댓글</summary>
<div markdown="1">

![MedicalLifeComment](/images/MedicalLife/2.메디컬라이프댓글.gif)

</div>
</details>

<br>

#### 6-1-10. Anonymous Board (익명 자유 게시판)
<details>
<summary>전체 조회</summary>
<div markdown="1">

![전체조회](/images/AnonymousBoard/1.전체조회.gif)

</div>
</details>

<details>
<summary>상세조회-1</summary>
<div markdown="1">

![상세조회1](/images/AnonymousBoard/2.상세조회(1).gif)

</div>
</details>

<details>
<summary>상세조회-2</summary>
<div markdown="1">

![상세조회2](/images/AnonymousBoard/2.상세조회(2).gif)

</div>
</details>

<details>
<summary>게시글 작성</summary>
<div markdown="1">

![게시글작성](/images/AnonymousBoard/3.게시글작성.gif)

</div>
</details>

<details>
<summary>게시글 수정</summary>
<div markdown="1">

![게시글수정](/images/AnonymousBoard/4.게시글수정.gif)

</div>
</details>

<details>
<summary>게시글 삭제</summary>
<div markdown="1">

![게시글삭제](/images/AnonymousBoard/5.게시글삭제.gif)

</div>
</details>

<details>
<summary>댓글 작성</summary>
<div markdown="1">

![댓글작성](/images/AnonymousBoard/6.댓글작성.gif)

</div>
</details>

<details>
<summary>댓글 수정</summary>
<div markdown="1">

![댓글수정](/images/AnonymousBoard/7.댓글수정.gif)

</div>
</details>

<details>
<summary>댓글 삭제</summary>
<div markdown="1">

![댓글삭제](/images/AnonymousBoard/8.댓글삭제.gif)

</div>
</details>

<details>
<summary>검색</summary>
<div markdown="1">

![검색](/images/AnonymousBoard/9.검색.gif)

</div>
</details>

<details>
<summary>좋아요, 북마크 -1</summary>
<div markdown="1">

![좋아요북마크1](/images/AnonymousBoard/10.좋아요,북마크(1).gif)

</div>
</details>

<details>
<summary>좋아요, 북마크 -2</summary>
<div markdown="1">

![좋아요북마크2](/images/AnonymousBoard/10.좋아요,북마크(2).gif)

</div>
</details>

<details>
<summary>Hot Contents</summary>
<div markdown="1">

![HotContents](/images/AnonymousBoard/11.HOTCONTENTS.gif)

</div>
</details>

<br>

## 7. 시스템 통합
### 7-1. CI/CD 계획서
![CICDBackend](https://github.com/user-attachments/assets/192e9a40-ebfb-4615-a74f-5fbc7d40bc67)
![CICDFront](https://github.com/user-attachments/assets/085e24ca-f904-42ad-96d7-647d73d8f9b7)

### 7-2. CI/CD 결과서
#### 7-2-1. Backend CI/CD 결과서
![BackendCICD](https://github.com/user-attachments/assets/595f046f-1dec-4635-90e9-0374293c8cc9)

#### 7-2-2. Frontend CI/CD 결과서
![FrontendCI](https://github.com/user-attachments/assets/692d9175-3259-4f90-9420-cdbffdf8f3fd)
![FrontendCD](https://github.com/user-attachments/assets/fd4f13ce-9f0c-418d-b6bb-41e3072143ad)

### 7-3. 통합 테스트 결과서
[통합 테스트 결과서](https://docs.google.com/spreadsheets/d/1sWHGbgL9R79AbUzCAmxKIulggjMqbi1lwv-RsJdtgVA/edit?gid=1001120134#gid=1001120134)

<br>

## 8. 회고
| 팀원  | 회고 내용                                                                                                                                                                                                               |
|:---:|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 연견창 | 이번 마지막 프로젝트를 통해 백엔드와 프론트엔드를 아우르는 다양한 기술을 배우고, 실습하면서, 팀워크와 문제 해결 능력을 크게 향상시켰다. 더 나은 결과물을 만들기 위해 노력하면서, 더 능숙하고 효율적인 코드를 작성할 수 있도록 계속 발전하고자 한다.|
| 윤지영 | 채팅이라는 기능을 처음 해봤는데, 너무 어려웠지만 끝까지 해낼 수 있어서 실력 향상에 도움이 되었던 것 같다. 생각 보다 시간이 오래 걸렸지만 다음에 다시 한다면 지금보다 발전할 수 있을 것 같다. 어려운 부분은 팀원들에게 도움을 청하고, 팀원들과 공통 코드를 나눌 수 있어서 협업의 중요성도 알 수 있었다.
| 박상현 | 새로운 기능을 해볼 수 있어서 좋았고, 어려웠지만 팀원들의 도움으로 끝까지 프로젝트를 마무리 할 수 있었다.|
| 임광택 |MediHub 프로젝트를 진행하며 처음으로 설문조사와 인터뷰를 통해 요구사항을 분석하는 과정을 경험했습니다. 이번 경험을 통해 여러 가지 중요한 통찰을 얻었고, 특히 문제 관계자와의 직접적인 소통이 얼마나 중요한지를 깨달았습니다. 단순히 문서화된 요구사항뿐만 아니라, 현장에서의 실질적인 문제와 기대를 이해할 수 있었습니다. 이 과정에서 관계자들이 언급한 문제들은 제가 처음에 고려하지 않았던 부분이었고, 예상치 못한 문제를 발견하는 데 큰 역할을 했습니다. 앞으로도 이번 경험을 바탕으로 지속적으로 관계자와의 소통을 강화하고, 그들의 피드백을 적극적으로 반영하여 프로젝트의 방향성을 조정할 것입니다. 또한, 다양한 분석 도구와 기법을 활용하여 요구사항을 보다 체계적으로 정리하고, 변화하는 요구에 유연하게 대응할 수 있도록 하겠습니다. 이러한 경험은 제 개발 역량을 더욱 향상시키고, 팀의 성공적인 프로젝트 진행에 기여할 수 있는 밑거름이 될 것입니다.|
| 임채륜 | Docker-Compose로 개발환경을 맞춰서 팀과의 협업 속도가 증진되었고, 쿠버네티스에서로의 마이그레이션이 빠르고 편해 좋은 성과를 얻을 수 있었다.|
| 임서연 | 도전의 연속이었던 프로젝트였던 만큼, 결과에 대한 보람도 크게 느꼈고 개발에 대한 자신감도 생겼다. 2개월간 함께해준 팀원분들에게 감사하다.|
