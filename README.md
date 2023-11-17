![SandamsoBanner](https://github.com/palzo/sandamso/assets/88123219/c12dfc91-8500-4330-87f3-8631286077f2)
 
# 🌄 Sandamso

## 🌈 Team Introduce
- ### [Team Notion](https://www.notion.so/d95019267b314a0ba78ccf3eab1ee834)

- ### [Team GitHub](https://github.com/palzo/sandamso)

| 이름   | 역할 | MBTI        | BLOG                                               | GitHub                                                   | 
| ------ | ---- | ----------  | -------------------------------------------------- | -------------------------------------------------------- |
| 박용석 | 팀장 | ISFJ         | https://velog.io/@ys4897                           |      https://github.com/yspark2                          |
| 임주리 | 팀원 | ENFJ         | https://juperi.tistory.com/             |  https://github.com/Lim-Juri/Android_Project                         |
| 이다민 | 팀원 | ESTJ         | https://velog.io/@kkominl                            |    https://github.com/kkomin                            |
| 권민석 | 팀원 | ISFP         | https://coding-martinkwon.tistory.com/                     |  https://github.com/MartinKwon94                          |


##  Project Introduce 🎩
### 기본 화면 구성

<details>
<summary>와이어 프레임</summary>
  
![wireframe1](https://github.com/palzo/sandamso/assets/88123219/f1876416-e100-43cb-adaa-91eccb2cdfbb)
![wireFrame2](https://github.com/palzo/sandamso/assets/88123219/07c4f189-1bc7-47f5-ae85-f540c8699352)

회의를 통하여 구체적인 설계에 들어가기 전에 `대략적인 틀`을 구성하였습니다.
중간에 한 번씩 디자이너 튜터님의 피드백을 받아가며 완성을 하게 되었습니다.

</details>

<details>
<summary>Splash Page</summary>

![SplashPage](https://github.com/palzo/sandamso/assets/88123219/bf4a8413-8f0e-438c-ac38-600393fa2cc5)

앱이 실행될 때 시작화면으로 앱 설치 후 최초 실행시에는 `Tutorial Page`를 제공하며, 이후로 
`SignIn Page`를 제공합니다.

</details>
    
<details>
<summary>Tutorial Page</summary>

![TutorialPage](https://github.com/palzo/sandamso/assets/88123219/06b9b03c-1e87-424f-98f9-eb1fd479693c)

Tutorial Page는 사용자에게 `앱의 사용방법을 간단하게 소개`하는 화면입니다.

</details>

<details>
<summary>SignIn Page</summary>

![SignInPage](https://github.com/palzo/sandamso/assets/88123219/668e0603-7547-4e02-ba43-9e2ed629672a)

로그인 페이지는 `로그인 기능`, `회원가입 기능`, `비밀번호 변경 기능`, `자동 로그인 기능`, `이메일 저장 기능`을 제공합니다.

로그인 기능은 회원가입을 해야만 이용할 수 있으며, 회원가입 후 사용자의 이메일 인증을 통해 firebase의 authentication에 등록이 되어야 사용가능합니다. 

비밀번호 변경 기능은 자신의 이메일을 입력 후 이메일에 전송된 메일에서 변경할 수 있습니다.

자동 로그인 기능은 자동 로그인을 체크 후 로그인을 하면 다음부터 로그인 화면 없이 이용이 가능합니다.

이메일 저장 기능은 이메일 저장을 체크 후 로그인을 하면 다음부터 이메일이 저장이 되어있어 비밀번호만 입력해서 로그인이 가능합니다.

</details>

<details>
<summary>SignUp Page</summary>

![SignUpPage](https://github.com/palzo/sandamso/assets/88123219/36107bae-98ec-4f73-a5a4-b0e76c027f4e)

SignUpPage는 회원가입을 할 수 있는 페이지입니다. 

모든 EditText에는 TextWhatchar를 사용하여 해당 입력란에 알맞은 `유효성 검사`를 실시합니다.

닉네임이 활용이 되는 부분이 많기 때문에 `중복확인`을 할 수 있는 버튼으로 확인하여 가입이 가능하게 만들었습니다.

</details>

<details>
    
<summary>Community Page</summary>

![CommunityPage](https://github.com/palzo/sandamso/assets/88123219/dbee5362-be6b-4cd0-bd37-6d742057c266)

Community Page는 모든 사용자들의 게시물을 확인할 수 있는 화면입니다.

FloatingActionButton으로 `게시물을 등록을 할 수 있는 기능`이 있는 Add Page로 이동할 수 있으며, 

좌측상단에 위치한 토글버튼은 `마감일이 지난 글을 리스트에서 지워주는 기능`을 하며,

우측 상단의 스피너를 통해서 `최신순, 인기순, 마감일순, 내글순`으로 리스트를 정렬하여 게시물을 확인할 수 있는 기능이 있습니다.

</details>

<details>
<summary>Add Page</summary>

![AddPage](https://github.com/palzo/sandamso/assets/88123219/5dea2a32-1b5e-4239-8ec9-75b5e94651ad)

Add Page는 `게시물을 등록할 수 있는 기능`을 하는 페이지입니다.

제목을 입력하고, 등산할 산을 입력하면 존재하는 산일 경우에 변경 버튼으로 바뀌고 적용이 됩니다.

게시할 이미지를 올리고, 등산할 날짜를 popup창을 통해 클릭해준 후, 내용까지 입력을 해주면 게시가 가능하게 됩니다.

Detail Page에서 `게시물을 수정할 경우`나 Info Page에서 `모임 만들기 버튼`을 통해서도 Add Page로 들어올 수 있습니다.

</details>

<details>
<summary>Detail Page</summary>

![DetailPage1](https://github.com/palzo/sandamso/assets/88123219/d6c011d4-81de-43dc-a578-22ad65ce41b7)
![DetailPage2](https://github.com/palzo/sandamso/assets/88123219/b578ee3d-a647-4fab-bfee-eaf2f03fa935)
![DetailPage3](https://github.com/palzo/sandamso/assets/88123219/a423da03-26ec-4dc7-8c2e-f267f6f63d3c)

Detail Page는 Community Page에서 게시물을 클릭하면 이동할 수 있는 페이지입니다.

게시물을 올린사람이라면 `수정하기 기능`으로 게시물을 수정할 수 있으며,  `삭제하기 기능`으로 게시물을 삭제할 수 있습니다.

게시물을 올리지않은 사람이라면 `참여하기 기능`을 통해서 채팅방에 입장할 수 있는 기능을 구현하였습니다.


</details>

<details>
<summary>ChattingList Page</summary>

![ChattingListPage](https://github.com/palzo/sandamso/assets/88123219/48b10ee7-f0e9-4366-bae4-5934bd919535)

ChattingList Page는 `자신이 참여하고 있는 채팅방`을 보여주는 리스트 기능을 제공하는 화면입니다.

`읽지않은 메세지가 있을 경우` 해당하는 채팅방에 빨간색 동그라미가 표시되며 `마지막 메세지`를 채팅방 리스트에서 확인도 할 수 있습니다.

해당하는 방의 `등산 d-day가 표시`되며, 채팅방에 참여한 인원의 숫자를 제공합니다.

채팅방을 누르면 해당하는 ChattingRoom Page로 이동이 가능합니다.


</details>

<details>
<summary>ChattingRoom Page</summary>

![ChattingRoomPage](https://github.com/palzo/sandamso/assets/88123219/835a3ab4-fdff-4622-b087-3de82e82360c)

ChattingRoom Page는 `참여한 인원들과 소통을 할 수 있는 채팅 기능`을 구현하였습니다.

`메세지를 보낼 때 채팅을 보낸 시간과 닉네임이 함께 채팅방에 전송`이 되도록 구현하였습니다. 

`ViewType`을 채팅방에 적용하여 본인이 작성한 글은 우측, 다른 사람이 보낸 글은 좌측에 보이도록 만들었습니다.

우측 상단에 아이콘을 클릭하면 `채팅방에 참여한 인원수와 닉네임`을 확인할 수 있는 기능을 추가했습니다.


</details>

<details>
<summary>SearchMountain Page</summary>

![SearchMountainPage](https://github.com/palzo/sandamso/assets/88123219/91ac237b-738a-4066-ab62-84122a508ee7)

SearchMountain Page는 `산에 관련된 다양한 정보를 제공하는 기능`을 제공합니다.

산 이름으로 검색을 누르면 `직접 원하는 산을 입력`하여 검색 후 Info Page로 넘어가서 산에 대한 자세한 정보를 확인해 볼 수 있습니다.

지역명으로 검색 버튼을 누르면 2개의 스피너를 통해 왼쪽스피너로 지역을 고르면 `그 지역에 해당하는 산을 API`를 통해 불러오게 됩니다.

오른쪽의 스피너는 왼쪽에서 고른 지역을 `더욱 자세한 지역`을 고를 수 있도록 만들었으며, 마찬가지로 그 지역에 해당하는 산 리스트를 화면에 보여줍니다.

</details>

<details>
<summary>Info Page</summary>

![InfoPage](https://github.com/palzo/sandamso/assets/88123219/6528dfae-f4fe-495a-a280-3717ca66e5da)

Info Page는 SearchMountain Page에서 선택한 `산의 위치, 산의 높이, 산 소개하는 글`등을 상세하게 보여주는 화면입니다.

등산하고자 하는 산을 보고나서 바로 게시글을 사용할 사람을 위해 `모임만들기 버튼을 통해서 바로 게시글 입력란으로 이동`이 가능하도록 구현하였습니다.

산의 위치를 모르시는 사용자들을 위해서 `naverMap을 통해 대략적인 산의 위치를 마커`로 표시해주는 기능을 추가했습니다.

등산 어플이기 때문에 등산 당일에 해당하는 산의 `지역 날씨 정보도 API`를 통해서 추가 구현했습니다. 

</details>

<details>
<summary>My Page</summary>

     Nickname Change [sub page]

![MyPage](https://github.com/palzo/sandamso/assets/88123219/af076384-c693-4860-91a7-718de8dddf1d)

My Page에서는 `닉네임을 변경할 수 있는 기능, 비밀번호 변경 기능, 사용설명서 보러가기, 로그아웃 기능, 회원탈퇴 기능`이 있습니다.

사용설명서 보러가기는 최초 앱 실행시에만 볼 수 있던 Tutorial Page를 다시 볼 수 있도록 해주는 기능입니다.

회원탈퇴를 하는 경우에는 탈퇴하는 회원이 작성한 게시글이 모두 삭제되며, 참여했던 채팅방에서도 나가지도록 구현하였습니다.



</details>


## 📗 Platforms & Languages 📒
<img src="https://img.shields.io/badge/android-3DDC84?style=flat-square&logo=android&logoColor=white"/>  <img src="https://img.shields.io/badge/kotlin-7F52FF?style=flat-square&logo=kotlin&logoColor=white"/>

## 📕 Tools 📘
<img src="https://img.shields.io/badge/figma-F24E1E?style=flat-square&logo=figma&logoColor=white"/>  <img src="https://img.shields.io/badge/git-F05032?style=flat-square&logo=git&logoColor=white"/>  <img src="https://img.shields.io/badge/github-181717?style=flat-square&logo=github&logoColor=white"/>  <img src="https://img.shields.io/badge/notion-000000?style=flat-square&logo=notion&logoColor=white"/>
