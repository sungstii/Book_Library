<div align=center> <img src="https://capsule-render.vercel.app/api?type=waving&color=auto&height=150&section=header&text=Navigation%20LibraryBooks&fontSize=50" /> </div>


<br>



## 🔥 프로젝트 소개

- 도서관리 API 개인 미니 프로젝트입니다.
- 사용자/관리자의 편의를 제공하기 위한 서비스를 제공합니다.

<br>

## ⚙ 주요 기능

### 1. 회원가입 및 로그인
* 비밀번호 암호화하여 저장
* JWT 토큰을 이용하여 인증/인가
* 사용자/관리자를 구분하여 역할에 맞는 권한 부여
  * 사용자 = 도서/도서관 찾기 및 대여 가능
  * 관리자 = 사용자 기능 + 도서/도서관의 내용을 등록 및 삭제 가능

### 2. 도서관 기능
* Page를 사용하여 모든 도서관 조회
* 도서관 상세조회
  * 도서관 이름, 위치, 책의 상태 조회
* 도서관 삭제시, 논리적 삭제(boolean으로 관리)
 
### 3. 책 기능
* Page를 사용하여 모든 책 조회
* 책 상세조회  
  * 책 제목, 작가, 출판사, 총책 개수 조회
* 책 삭제시, 논리적 삭제(boolean으로 관리)

### 4. 책 대여 및 반납
* 조건별 확인로직
  * 1인당 최대 5권 책 대여가능
  * 대여 기간은 14일이며, 대여 기간을 넘길 시 초과한 일수만큼 페널티 부여(대여 불가능 상태로 변경)
  * 도서관에 등록된 책이 모두 대여 중일 경우 대여 불가능 상태로 변경
  * 사용자의 대여 히스토리는 영구저장

### 5. 기한 체크 스케줄러  
* 매일 12시 사용자의 패널티 기간을 체크하여 상태값 업데이트
<br>

## 🔑 DB의 부하를 줄이기(TODO)
* Redis를 사용하여 자주 이용하는 자주 이용하는 책, 도서관 List Page Data를 캐시화하여 DB서버 부하분산
  * Redis를 사용하지 않았을 경우, 내용을 Get 하면 쿼리문이 많이 날아가 30sec의 시간이 걸림
  * Redis를 사용할 경우, 한번 등록된 Get 내용은 저장되어 있으므로 시간 단축
<br>

## 🛠 BackEnd
<img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"><img src="https://img.shields.io/badge/Spring Data JPA-6DB33F?style=for-the-badge&logo=springdata&logoColor=white"><img src="https://img.shields.io/badge/Spring Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white"><img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white"><img src="https://img.shields.io/badge/JWT-181717?style=for-the-badge&logo=jwt&logoColor=white"><img src="https://img.shields.io/badge/redis-DC382D?style=for-the-badge&logo=redis&logoColor=white"><img src="https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white"><img src="https://img.shields.io/badge/Jasypt-364161?style=for-the-badge&logo=jasypt&logoColor=white"><img src="https://img.shields.io/badge/Map Struct-F1A54F?style=for-the-badge&logo=mapstruct&logoColor=white">

<br>

## 📌 ERD

ERD![erd](https://github.com/sungstii/book_library/assets/116154554/14cd5514-d775-4d34-b9ec-86ee57a7202a)
