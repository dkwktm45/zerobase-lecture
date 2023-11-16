# 📌Plan Learn
나만의 강의 계획 서비스(진행중)

제약 사항 : 학습은 유튜브 영상을 토대로 학습 플랫폼 구축

개요 : 온라인 플랫폼의 단점을 파기하고자 해당 서비스를 만들게 됐습니다.

해당 서비스 사용자는 자기 주도적으로 학습을 계획하고 이를 수행하는 데 높은 성취도와 효과를 이어갈 수 있습니다.

사용 스텍
- Vue Js , Java 11 , Gradle , MySQL, JPA , Redis , Security , Naver Cloud Platfrom , Git Action

<br>

## 📌기능 정의 사항
**[공통]**
- 사용자는 email과 password를 통해서 로그인이 가능하다. 
- 사용자는 Kakao, Naver, Google를 통해서 로그인이 가능하다.
  - 사항으로는 social_type, social_id가 들어갈 예정이다.  
- 사용자는 회원가입을 통해서 회원등록을 할 수 있다.
- 회원은 강의를 학습한 시간을 기준으로 회원의 티어가 부여된다.
  - Bronze , Silver, Gold, Platinum
  - 시간은 분 단위로 측정
  - 이러한 데이터는 Redis에 저장한다.
  - email을 기준으로 데이터를 저장한다.
  - 이러한 티어정보 및 시간은 Redis에 저장한다.
- 토큰에 대한 정보 또한 Redis에 저장을한다.
  - 토큰객체에는 만료시간이 있다.(2주)
  - 토큰에는 email, accessToken(key), refreshToken 데이터가 있으다.

**[회원 - 플래너]**
- 회원은 강좌 , 개인 학습 , 리마인드를 통해 원하는 날짜에 계획을 새울 수 있다.
  - 어떤 학습 종류인지는 planner_type에 명시한다.
- 회원이 만약 계획표 날짜에 대한 강의 , 개인 학습, 리마인드를 수행 못했을 시 못 한 부분은 다음 날짜로 미루어진다.
규정 사항<br>
>2023/10/01     강의 1<br>
2023/10/03     강의 2<br>
2023/10/04     강의 3<br>

만약 내 계획표에 해당 강의가 짜여저 있다고 강의 1을 수강하지 못 했다면 다음날 계획한 강의가 없기 때문에 강의 1만 미루게 되고<br>
>2023/10/01     강의 1<br>
2023/10/02     강의 2<br>
2023/10/03     강의 3<br>

위와 같이 강의 1을 듣지 못했다면 다음날 강의가 아래와 같이 변경됩니다.<br>
>2023/10/02     강의 1<br>
2023/10/03     강의 2<br>
2023/10/04     강의 3<br>
- 회원은 수강을 시작한 강의 및 개인 학습 리스트 , 리마인드 에서 Filter가 가능하다.
  - 다만 강좌를 다 수강한 강좌는 보여주지 않으며, 이는 개인 학습 / 리마인드도 마찬가지이다.

**[회원 - 강좌]**
- 회원은 여러 강좌를 수강목록에 추가할 수 있다.
- 회원은 강좌에 해당하는 강의를 들었을 때 강의를 들었다는 요청을 할 수 있다.
  - 완료 했다는 요청은 json(lecture_complete) 데이터로 저장이 된다.
- 회원은 수강 목록에서 강좌를 수강 취소할 수 있다.
  - 수강 취소 시 만약 학습리스트에 해당 강좌 및 강의가 있을시 학습리스트에서도 삭제가 된다.
- 강의를 수강하기 시작한 인원은 리뷰(댓글) 달 수 있다.
  - 리뷰는 한번 달면 삭제는 불가능하다.
- 강좌를 수강 중인 학생이 많을 수록 해당 강좌의 인지도를 알 수 있다.
  - 인지도는 단순히 수강 중, 수강 완료 학생 수를 기준으로 한다.
  - 도중에 수강이 취소된다면 인지도는 낮아진다.

**[회원 - 리뷰]**
- 회원은 리뷰는 수정이 불가능하지만 언제 생성되고, 수정 및 누가 적었는지는 볼 수 있다.
- 회원은 리뷰를 작성할 수 있다.
  - 이 때 따로 회원 연관관계는 맺지 않는다.

**[회원 - 회고]**
- 회원은 주차별 회고 사항을 볼 수 있다.
  - 주차별 회고 사항은 "yyyy-mm-dd-yyyy-mm-dd" 같은 데이터가 들어간다.
- 회원은 완료 버튼을 통해서 회고 사항을 완료 상태로 수정할 수 있다.
- 회원은 날짜가 지나지 않는 주차를 작성 할 수 있다.(주차에 대한 중복은 가능하다.)
- 회고 작성시 크게 , 제목 / 내용이 있다.
- 조회는 크게 단건 조회 다수 건 조회가 가능하다.

**[회원 - 리마인드]**
- 회원은 어떤 특정 강의를 리마인드 목록에 추가 및 삭제가 가능하다.
  - 리마인드는 특정 어떤 주제(개인 학습, 강의, 회고) 구분하여 넣을 수 있다.
- 회원은 리마인드를 완료 했다면 완료 했다는 요청을 보낼 수 있다.
- 조회는 크게 단건 조회 다수 건 조회가 가능하다.

**[회원 - 개인학습]**
- 회원은 개인학습이 필요한 부분을 추가, 수정 , 삭제가 가능하다.
- 회원은 개인학습을 완료 했다면 완료 했다는 요청을 보낼 수 있다.
- 조회는 크게 단건 조회 다수 건 조회가 가능하다.

**[관리자]**
- 관리자는 효율성을 위해서 auth_type으로 구분한다.

**[관리자 - 강좌]**
- 관리자는 강좌를 추가 , 수정 , 삭제가 가능하다.
  - 강좌 테이블에 member_id는 관리자가 강좌를 만들 시 연관관계가 설정이 된다.
- 추가 시 크게 강의 소개, 강의, 강의 시간을 넣을 수 있다.
  
**[관리자 - 게시판(공지사항)]**
- 관리자는 게시글을 추가 , 수정 삭제가 가능하다.

<br>

## 📌ERD(해당 사항은 변경이 될 수 있습니다.)
![Plan Learn Project](https://github.com/dkwktm45/zerobase-lecture/assets/48014869/c786d197-7e22-4037-a052-3ed8e19237f1)




