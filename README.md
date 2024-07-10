# 🧳Triply
![Triply](https://github.com/HMisu/Triply_Boot/assets/37448404/d6d70365-7e6b-41e6-94f3-feb228847cea)


## 프로젝트 소개
관광 정보 및 후기 조회 웹 사이트
- Spring Boot + React
## 1. 개발 기간
2024.02~2024.05
## 2. 기능
- 회원가입
- 로그인
  - JWT(Json Web Token)
  - Google, Kakao OAuth2 Login
- 마이페이지
  - 회원 정보 수정
  - 작성한 후기 조회
  - 북마크한 관광 정보 조회
- 관광 정보
  - 목록, 지도 두 형태로 사용자 인근 관광 정보 조회
  - KaKao Maps API
  - 지역, 키워드 옵션으로 관광 정보 조회
  - 중복 조회를 방지한 조회수 증가
- 북마크
  - 관광 정보를 북마크로 등록
- 후기
  - 관광 정보에 대한 휙 작성 및 조회
## 3. 프로젝트 구조
<details>
<summary>구조 보기(React)</summary>

```
Triply
├─.idea
├─node_modules
├─public
│  └─assets
│      ├─assets
│      │  ├─icons
│      │  └─temp
│      ├─icons
│      └─temp
└─src
    ├─apis
    ├─components
    │  ├─community
    │  ├─recruitment
    │  ├─review
    │  ├─travel
    │  ├─ui
    │  │  ├─button
    │  │  ├─layout
    │  │  └─lnput
    │  └─user
    ├─pages
    │  ├─alarm
    │  ├─chat
    │  ├─community
    │  ├─recruitment
    │  ├─review
    │  ├─sample
    │  ├─search
    │  ├─sign
    │  ├─travel
    │  └─user
    ├─scss
    │  ├─components
    │  ├─pages
    │  │  ├─alarm
    │  │  ├─chat
    │  │  ├─community
    │  │  ├─search
    │  │  ├─sign
    │  │  ├─travel
    │  │  └─user
    │  ├─recruitment
    │  ├─review
    │  └─ui
    ├─slices
    ├─store
    └─util
```
</details>
<details>
<summary>구조 보기(Spring Boot)</summary>

```
Triply
├─ .gitignore
├─ build.gradle
├─ gradlew
├─ gradlew.bat
├─ settings.gradle
└─src
    ├─main
       ├─generated
       │  └─com
       │      └─bit
       │          └─nc4_final_project
       │              └─entity
       │                  ├─board
       │                  ├─chat
       │                  ├─community
       │                  └─travel
       ├─java
          └─com
              └─bit
                  └─nc4_final_project
                      ├─api
                      ├─common
                      ├─configuration
                      │  └─chat
                      ├─controller
                      │  ├─chat
                      │  ├─community
                      │  └─travel
                      ├─converter
                      ├─document
                      │  ├─chat
                      │  ├─travel
                      │  └─user
                      ├─dto
                      │  ├─board
                      │  ├─chat
                      │  ├─community
                      │  ├─friend
                      │  ├─recruitment
                      │  ├─report
                      │  ├─review
                      │  ├─search
                      │  ├─travel
                      │  └─user
                      ├─entity
                      │  ├─board
                      │  ├─chat
                      │  ├─community
                      │  └─travel
                      ├─factory
                      │  └─travel
                      │      └─impl
                      ├─jwt
                      ├─repository
                      │  ├─board
                      │  ├─chat
                      │  │  └─impl
                      │  ├─chatroom
                      │  ├─community
                      │  ├─friend
                      │  ├─recruitment
                      │  │  └─impl
                      │  ├─report
                      │  ├─review
                      │  │  └─impl
                      │  ├─travel
                      │  │  └─mongo
                      │  │      └─impl
                      │  └─user
                      │      └─area
                      └─service
                          ├─board
                          │  └─impl
                          ├─chat
                          │  └─impl
                          ├─community
                          │  └─impl
                          ├─friend
                          │  └─impl
                          ├─recruitment
                          │  └─impl
                          ├─report
                          │  └─impl
                          ├─review
                          │  └─impl
                          ├─taravel
                          │  └─impl
                          └─user
                              └─impl
```
</details>


## 4. ERD
![triply](https://github.com/HMisu/Triply_Boot/assets/37448404/80f4e9ad-82cf-48c5-a95b-1c017e43a04c)


## 주요 기능
### [메인 페이지]
- 조회 수 많은 관광 정보와 사용자 인근의 관광 정보를 목록 형태로 조회할 수 있습니다.
- 상단의 슬라이더를 통해 랜덤한 관광 정보의 후기를 볼 수 있습니다.
![home](https://github.com/HMisu/Triply_Boot/assets/37448404/b86c589e-b071-48c8-a961-c12d86e9f67a)

### [관광 정보 조회]
- 지역 메뉴에서 지역과 키워드로 관광 정보를 검색하고, 무작위, 가나다순, 조회순, 북마크순으로 정렬할 수 있습니다.
- 지도에서 관광 정보를 조회 할 수 있으며 마우스로 이동하며 다른 지역의 정보도 확인할 수 있습니다.
- 지도의 마커의 색과, 아이콘으로 관광 정보의 특성을 쉽게 구분 할 수 있습니다.
- 지도의 마커를 클릭 시 관광 정보를 간략하게 조회할 수 있습니다.
![지역](https://github.com/HMisu/Triply_Boot/assets/37448404/d3eacf68-c6a9-49a0-b2ce-f2ae5eedd3f3)
- 관광 정보의 상세 정보와 후기를 확인할 수 있습니다.
- 반려동물 동반이 가능한 관광 정보인 경우 아이콘 위의 마우스를 올릴 시 관련 정보가 출력됩니다.
![관광상세](https://github.com/HMisu/Triply_Boot/assets/37448404/358953a3-3404-4ccc-9791-1edeff91ed87)

### [북마크]
- 관광 정보를 북마크로 등록합니다.
![북마크](https://github.com/HMisu/Triply_Boot/assets/37448404/ec6b05f1-a06c-46fc-9933-52d4f2dd1f3e)

### [관광 정보 후기]
- 관광 정보의 후기를 작성하고 확인할 수 있습니다.
![후기](https://github.com/HMisu/Triply_Boot/assets/37448404/7f468420-c8ba-46b9-8865-73f1feeb364e)
