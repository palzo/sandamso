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

앱이 실행될 때 시작화면으로 `lottie animation`을 추가했습니다.

</details>
    
<details>
<summary>Tutorial Page</summary>

![TutorialPage](https://github.com/palzo/sandamso/assets/88123219/06b9b03c-1e87-424f-98f9-eb1fd479693c)

사용자에게 YouTube의 현재 인기 및 새로운 콘텐츠를 중점적으로 보여주는 핵심 화면입니다.

TabLayout + ViePager2 사용하여 구현하였습니다.

아래 목록을 스크롤이 가능한 RecyclerView 형태로 나열하여 출력합니다.

`Most Popular Videos 목록` 보여주고 수평으로 스크롤이 되도록 구현했습니다.

`Category Videos 목록` 보여주고 수평으로 스크롤이 되도록 구현했습니다.

`Category Channels 목록` 보여주고 수평으로 스크롤이 되도록 구현했습니다.

</details>

<details>
<summary>SignIn Page</summary>

     FindPwPage[sub page]

![SignInPage](https://github.com/palzo/sandamso/assets/88123219/668e0603-7547-4e02-ba43-9e2ed629672a)

Home Page에서 각 아이템 선택시 선택된 비디오의 `상세 정보 제공`합니다.

`좋아요` 버튼 클릭 시 My Page에 비디오 정보를 `저장`합니다. 

Detail page 시작과 종료시 특별한 Effect로 `화면 전환 애니메이션`을 적용했습니다.

댓글 버튼 누를 시 `영상의 댓글`을 불러와 보여주기를 구현했습니다.

homefragment에서 불러온 영상 `재생`하기가 가능합니다.

링크 `공유하기` 기능을 추가했습니다.

</details>

<details>
<summary>SignUp Page</summary>

![SignUpPage](https://github.com/palzo/sandamso/assets/88123219/36107bae-98ec-4f73-a5a4-b0e76c027f4e)

사용자가 원하는 비디오를 쉽게 `검색`하고 `결과`를 빠르게 확인할 수 있는 기능을 제공합니다.

상단에는 검색을 위한 `Search EditText`를 배치하고, 그 아래에 검색 결과를 출력할 `RecyclerView`를 배치했습니다.

격자 구조의 형태로 결과를 배치했습니다.

각 아이템에는 영상 정보(`제목, 영상 길이, 조회 수 등`)를 함께 보여줍니다.

</details>

<details>
    
<summary>Community Page</summary>

![CommunityPage](https://github.com/palzo/sandamso/assets/88123219/dbee5362-be6b-4cd0-bd37-6d742057c266)

사용자의 개인 정보 및 사용자가 `좋아요`를 누른 비디오 목록을 보여주는 기능 제공합니다.

사용자의 프로필 사진, 이름 등의 `개인 정보를 상단에 표시`합니다.

`좋아요`를 누른 비디오 목록은 `RecyclerView`를 사용해 아래쪽에 목록 형태로 출력합니다.

저장과 삭제에는 `Room database`를 적용하였습니다.

`롱 클릭시 삭제 기능` 추가했습니다.

</details>

<details>
<summary>Add Page</summary>

![AddPage](https://github.com/palzo/sandamso/assets/88123219/5dea2a32-1b5e-4239-8ec9-75b5e94651ad)

동영상의 길이 `60초 이내인 비디오 목록`을 `여러 채널`에서 가져와 보여주는 기능 제공합니다.

쇼츠, 댓글의 끝에서 스크롤 시 다음 페이지를 가져와 보여주는 기능을 제공 합니다.(`infinite scroll`)

`좋아요` 버튼 클릭 시 `My Video` 에 저장합니다.

`댓글` 버튼 누를 시 영상의 댓글을 불러와 보여줍니다.

`공유` 버튼 누를 시 , 영상 제목과 링크를 공유합니다.

`progress bar`를 추가하여 현재 영상의 `진행률을 실시간`으로 보여줍니다.


</details>

<details>
<summary>Detail Page</summary>

![DetailPage1](https://github.com/palzo/sandamso/assets/88123219/d6c011d4-81de-43dc-a578-22ad65ce41b7)
![DetailPage2](https://github.com/palzo/sandamso/assets/88123219/b578ee3d-a647-4fab-bfee-eaf2f03fa935)
![DetailPage3](https://github.com/palzo/sandamso/assets/88123219/a423da03-26ec-4dc7-8c2e-f267f6f63d3c)

동영상의 길이 `60초 이내인 비디오 목록`을 `여러 채널`에서 가져와 보여주는 기능 제공합니다.

쇼츠, 댓글의 끝에서 스크롤 시 다음 페이지를 가져와 보여주는 기능을 제공 합니다.(`infinite scroll`)

`좋아요` 버튼 클릭 시 `My Video` 에 저장합니다.

`댓글` 버튼 누를 시 영상의 댓글을 불러와 보여줍니다.

`공유` 버튼 누를 시 , 영상 제목과 링크를 공유합니다.

`progress bar`를 추가하여 현재 영상의 `진행률을 실시간`으로 보여줍니다.


</details>

<details>
<summary>ChattingList Page</summary>

![ChattingListPage](https://github.com/palzo/sandamso/assets/88123219/48b10ee7-f0e9-4366-bae4-5934bd919535)

동영상의 길이 `60초 이내인 비디오 목록`을 `여러 채널`에서 가져와 보여주는 기능 제공합니다.

쇼츠, 댓글의 끝에서 스크롤 시 다음 페이지를 가져와 보여주는 기능을 제공 합니다.(`infinite scroll`)

`좋아요` 버튼 클릭 시 `My Video` 에 저장합니다.

`댓글` 버튼 누를 시 영상의 댓글을 불러와 보여줍니다.

`공유` 버튼 누를 시 , 영상 제목과 링크를 공유합니다.

`progress bar`를 추가하여 현재 영상의 `진행률을 실시간`으로 보여줍니다.


</details>

<details>
<summary>ChattingRoom Page</summary>

    User Page [sub page]

![ChattingRoomPage](https://github.com/palzo/sandamso/assets/88123219/835a3ab4-fdff-4622-b087-3de82e82360c)

동영상의 길이 `60초 이내인 비디오 목록`을 `여러 채널`에서 가져와 보여주는 기능 제공합니다.

쇼츠, 댓글의 끝에서 스크롤 시 다음 페이지를 가져와 보여주는 기능을 제공 합니다.(`infinite scroll`)

`좋아요` 버튼 클릭 시 `My Video` 에 저장합니다.

`댓글` 버튼 누를 시 영상의 댓글을 불러와 보여줍니다.

`공유` 버튼 누를 시 , 영상 제목과 링크를 공유합니다.

`progress bar`를 추가하여 현재 영상의 `진행률을 실시간`으로 보여줍니다.


</details>

<details>
<summary>Info Page</summary>

![InfoPage](https://github.com/palzo/sandamso/assets/88123219/6528dfae-f4fe-495a-a280-3717ca66e5da)

동영상의 길이 `60초 이내인 비디오 목록`을 `여러 채널`에서 가져와 보여주는 기능 제공합니다.

쇼츠, 댓글의 끝에서 스크롤 시 다음 페이지를 가져와 보여주는 기능을 제공 합니다.(`infinite scroll`)

`좋아요` 버튼 클릭 시 `My Video` 에 저장합니다.

`댓글` 버튼 누를 시 영상의 댓글을 불러와 보여줍니다.

`공유` 버튼 누를 시 , 영상 제목과 링크를 공유합니다.

`progress bar`를 추가하여 현재 영상의 `진행률을 실시간`으로 보여줍니다.


</details>

<details>
<summary>My Page</summary>

     Nickname Change [sub page]

![MyPage](https://github.com/palzo/sandamso/assets/88123219/af076384-c693-4860-91a7-718de8dddf1d)

동영상의 길이 `60초 이내인 비디오 목록`을 `여러 채널`에서 가져와 보여주는 기능 제공합니다.

쇼츠, 댓글의 끝에서 스크롤 시 다음 페이지를 가져와 보여주는 기능을 제공 합니다.(`infinite scroll`)

`좋아요` 버튼 클릭 시 `My Video` 에 저장합니다.

`댓글` 버튼 누를 시 영상의 댓글을 불러와 보여줍니다.

`공유` 버튼 누를 시 , 영상 제목과 링크를 공유합니다.

`progress bar`를 추가하여 현재 영상의 `진행률을 실시간`으로 보여줍니다.


</details>


## 📗 Platforms & Languages 📒
<img src="https://img.shields.io/badge/android-3DDC84?style=flat-square&logo=android&logoColor=white"/>  <img src="https://img.shields.io/badge/kotlin-7F52FF?style=flat-square&logo=kotlin&logoColor=white"/>

## 📕 Tools 📘
<img src="https://img.shields.io/badge/figma-F24E1E?style=flat-square&logo=figma&logoColor=white"/>  <img src="https://img.shields.io/badge/git-F05032?style=flat-square&logo=git&logoColor=white"/>  <img src="https://img.shields.io/badge/github-181717?style=flat-square&logo=github&logoColor=white"/>  <img src="https://img.shields.io/badge/notion-000000?style=flat-square&logo=notion&logoColor=white"/>
