package com.example.sansaninfo.InfoPage

data class RegionList(val region : String, val regionX : Int, val regionY : Int)

// 날씨의 nx, ny 데이터 리스트
/*class RegionLocation {
    // 서울특별시
    val seoulList = listOf<RegionList>(
        RegionList("강서구", 57, 127),
        RegionList("관악구", 59, 125),
        RegionList("서초구", 60, 125),
        RegionList("강남구", 61, 125),
        RegionList("도봉구", 61, 129),
        RegionList("중량구", 62, 128),
        RegionList("은평구", 59, 127),
        RegionList("서대문구", 59, 127),
        RegionList("종로구", 60, 127),
        RegionList("강북구", 61, 128),
        RegionList("노원구", 61, 129),
        RegionList("광진구", 62, 126),
    )

    // 대전광역시
    val daejeonList = listOf<RegionList>(
        RegionList("대덕구", 68, 101),
        RegionList("서구", 67, 100),
        RegionList("중구", 68, 100),
        RegionList("동구", 68, 100),
        RegionList("유성구", 66, 101),
    )

    // 대구광역시
    val daeguList = listOf<RegionList>(
        RegionList("달성군", 87, 89),
        RegionList("달서구", 88, 90),
        RegionList("수성구", 89, 90),
        RegionList("동구", 90, 91),
    )

    // 부산광역시
    val busanList = listOf<RegionList>(
        RegionList("기장군", 100, 79),
        RegionList("북구", 97, 76),
        RegionList("금정구", 98, 77),
        RegionList("강서구", 95, 74),
        RegionList("연제구", 98, 76),
        RegionList("부산진구", 97, 75),
        RegionList("사하구", 96, 74),
        RegionList("서구", 97, 74),
        RegionList("동래구", 98, 76),
    )

    // 인천광역시
    val incheonList = listOf<RegionList>(
        RegionList("서구", 55, 125),
        RegionList("남동구", 56, 124),
        RegionList("강화군", 51, 130),
        RegionList("옹진군", 54, 124),
        RegionList("중구", 54, 125),
        RegionList("부평구", 55, 125),
    )

    // 광주광역시
    val gwangjuList = listOf<RegionList>(
        RegionList("동구", 60, 74),
        RegionList("서구", 59, 74),
        RegionList("광산구", 57, 74),
    )

    // 울산광역시
    val ulsanList = listOf<RegionList>(
        RegionList("울주군", 102, 80),
        RegionList("동구", 104, 83),
        RegionList("중구", 102, 84),
    )

    // 경기도
    val gyeonggiList = listOf<RegionList>(
        RegionList("가평군", 69, 133),
        RegionList("포천시", 64, 134),
        RegionList("김포시", 55, 128),
        RegionList("양평군", 69, 125),
        RegionList("양주시", 61, 131),
        RegionList("하남시", 64, 126),
        RegionList("파주시", 56, 131),
        RegionList("연천군", 61, 138),
        RegionList("여주시", 71, 121),
        RegionList("안양시", 59, 123),
        RegionList("용인시", 64, 119),
        RegionList("광주군", 65, 123),
        RegionList("이천시", 68, 121),
        RegionList("안성시", 65, 115),
        RegionList("평택시", 62, 114),
        RegionList("의정부시", 61, 130),
        RegionList("의왕시", 50, 122),
        RegionList("광주시", 65, 123),
        RegionList("남양주시", 64, 128),
        RegionList("시흥시", 57, 123),
        RegionList("여주시", 71, 121),
        RegionList("성남시", 63, 124),
    )

    // 강원도
    val gangwonList = listOf<RegionList>(
        RegionList("춘천시", 73, 134),
        RegionList("인제군", 80, 138),
        RegionList("정선군", 73, 123),
        RegionList("홍천군", 75, 130),
        RegionList("철원군", 65, 139),
        RegionList("삼척시", 98, 125),
        RegionList("원주시", 76, 122),
        RegionList("평창군", 84, 123),
        RegionList("영월군", 86, 119),
    )

    // 경상남도
    val gyeongnamList = listOf<RegionList>(
        RegionList("거제시", 90, 69),
        RegionList("밀양시", 92, 83),
        RegionList("김해시", 95, 77),
        RegionList("진주시", 81, 75),
        RegionList("진해구", 91, 75),
        RegionList("양산시", 90, 79),
        RegionList("마산시", 89, 76),
        RegionList("창원시", 91, 75),
        RegionList("통영시", 87, 68),
        RegionList("사천시", 80, 71),
        RegionList("합천군", 81, 84),
        RegionList("산청군", 76, 80),
        RegionList("거창군", 77, 86),
        RegionList("고성군", 85, 71),
        RegionList("함양군", 74, 82),
        RegionList("창녕군", 87, 83),
        RegionList("남해군", 77, 68),
        RegionList("의령군", 83, 78),
        RegionList("함안군", 86, 77),
        RegionList("하동군", 74, 73),
    )
    // 경상북도
    val gyeongbukList = listOf<RegionList>(
        RegionList("문경시", 81, 106),
        RegionList("안동시", 91, 106),
        RegionList("상주시", 91, 102),
        RegionList("경산시", 91, 90),
        RegionList("경주시", 100, 91),
        RegionList("영주시", 89, 111),
        RegionList("구미시", 84, 96),
        RegionList("포항시", 102, 95),
        RegionList("김천시", 80, 96),
        RegionList("양산시 ", 102, 103),
        RegionList("칠곡군", 85, 93),
        RegionList("청도군", 91, 86),
        RegionList("봉화군", 90, 113),
        RegionList("군위군", 88, 99),
        RegionList("영덕군", 102, 103),
        RegionList("예천군", 86, 107),
        RegionList("청송군", 96, 103),
        RegionList("고령군", 83, 87),
        RegionList("울릉군", 127, 127),
        RegionList("울진군", 102, 115),
        RegionList("영양군", 91, 108),
    )

    // 충청남도
    val chungnamList = listOf<RegionList>(
        RegionList("공주시", 62, 102),
        RegionList("서산시", 51, 110),
        RegionList("천안시", 63, 110),
        RegionList("논산시", 62, 97),
        RegionList("아산시", 60, 110),
        RegionList("보령시", 54, 100),
        RegionList("당진시", 54, 112),
        RegionList("계룡시", 65, 99),
        RegionList("부여군", 59, 99),
        RegionList("홍성군", 55, 106),
        RegionList("예산군", 58, 107),
        RegionList("청양군", 57, 103),
        RegionList("금산군", 69, 95),
        RegionList("태안군", 48, 109),
        RegionList("서천군", 55, 94),
    )

    // 충청북도
    val chungbukList = listOf<RegionList>(
        RegionList("제천시", 81, 118),
        RegionList("충주시", 76, 114),
        RegionList("청주시", 69, 107),
        RegionList("괴산군", 74, 111),
        RegionList("영동군", 74, 97),
        RegionList("청원군", 69, 107),
        RegionList("보은군", 73, 103),
        RegionList("단양군", 82, 115),
        RegionList("옥천군", 71, 99),
        RegionList("진천군", 68, 111),
        RegionList("음성군", 72, 113),
        RegionList("증평군", 71, 110),
    )

    // 전라남도
    val jeonnamList = listOf<RegionList>(
        RegionList("나주시", 56, 71),
        RegionList("순천시", 70, 70),
        RegionList("광양시", 73, 70),
        RegionList("여수시", 73, 66),
        RegionList("목포시", 50, 67),
        RegionList("장흥군", 59, 64),
        RegionList("해남군", 54, 61),
        RegionList("무안군", 52, 71),
        RegionList("화순군", 61, 72),
        RegionList("구례군", 69, 75),
        RegionList("함평군", 52, 72),
        RegionList("장성군", 57, 77),
        RegionList("곡성군", 66, 77),
        RegionList("담양군", 61, 78),
        RegionList("영광군", 52, 77),
        RegionList("영암군", 56, 66),
        RegionList("진도군", 48, 59),
        RegionList("곡성군", 66, 77),
        RegionList("강진군", 57, 63),
        RegionList("신안군", 50, 66),
        RegionList("고흥군", 66, 62),
        RegionList("보성군", 62, 66),
        RegionList("완도군", 57, 56),
        RegionList("여천군", 73, 66),
    )

    // 전라북도
    val jeonbukList = listOf<RegionList>(
        RegionList("남원시", 68, 80),
        RegionList("군산시", 56, 92),
        RegionList("김제시", 59, 88),
        RegionList("정읍시", 58, 83),
        RegionList("익산시", 60, 91),
        RegionList("전주시", 63, 89),
        RegionList("순창군", 63, 79),
        RegionList("고창군", 56, 80),
        RegionList("임실군", 66, 84),
        RegionList("부안군", 56, 87),
        RegionList("장수군", 52, 85),
        RegionList("진안군", 70, 88),
        RegionList("무주군", 63, 93),
        RegionList("완주군", 63, 89),

    )

    // 제주특별자치도
    val jejuList = listOf<RegionList>(
        RegionList("서귀포시", 53, 33),
        RegionList("제주시", 53, 38),
        RegionList("남제주군", 53, 33),
    )
}*/

class RegionLocation {
    val regionList = listOf<RegionList>(
        // 서울
        RegionList("서울특별시 강서구", 57, 127),
        RegionList("서울특별시 관악구", 59, 125),
        RegionList("서울특별시 서초구", 60, 125),
        RegionList("서울특별시 강남구", 61, 125),
        RegionList("서울특별시 도봉구", 61, 129),
        RegionList("서울특별시 중량구", 62, 128),
        RegionList("서울특별시 은평구", 59, 127),
        RegionList("서울특별시 서대문구", 59, 127),
        RegionList("서울특별시 종로구", 60, 127),
        RegionList("서울특별시 강북구", 61, 128),
        RegionList("서울특별시 노원구", 61, 129),
        RegionList("서울특별시 광진구", 62, 126),

        // 대전
        RegionList("대전광역시 대덕구", 68, 101),
        RegionList("대전광역시 서구", 67, 100),
        RegionList("대전광역시 중구", 68, 100),
        RegionList("대전광역시 동구", 68, 100),
        RegionList("대전광역시 유성구", 66, 101),

        // 대구
        RegionList("대구광역시 달성군", 87, 89),
        RegionList("대구광역시 달서구", 88, 90),
        RegionList("대구광역시 수성구", 89, 90),
        RegionList("대구광역시 동구", 90, 91),

        // 부산
        RegionList("부산광역시 기장군", 100, 79),
        RegionList("부산광역시 북구", 97, 76),
        RegionList("부산광역시 금정구", 98, 77),
        RegionList("부산광역시 강서구", 95, 74),
        RegionList("부산광역시 연제구", 98, 76),
        RegionList("부산광역시 부산진구", 97, 75),
        RegionList("부산광역시 사하구", 96, 74),
        RegionList("부산광역시 서구", 97, 74),
        RegionList("부산광역시 동래구", 98, 76),

        // 인천
        RegionList("인천광역시 서구", 55, 125),
        RegionList("인천광역시 남동구", 56, 124),
        RegionList("인천광역시 강화군", 51, 130),
        RegionList("인천광역시 옹진군", 54, 124),
        RegionList("인천광역시 중구", 54, 125),
        RegionList("인천광역시 부평구", 55, 125),

        // 광주
        RegionList("광주광역시 동구", 60, 74),
        RegionList("광주광역시 서구", 59, 74),
        RegionList("광주광역시 광산구", 57, 74),

        // 울산
        RegionList("울산광역시 울주군", 102, 80),
        RegionList("울산광역시 동구", 104, 83),
        RegionList("울산광역시 중구", 102, 84),

        // 경기도
        RegionList("경기도 가평군", 69, 133),
        RegionList("경기도 포천시", 64, 134),
        RegionList("경기도 김포시", 55, 128),
        RegionList("경기도 양평군", 69, 125),
        RegionList("경기도 양주시", 61, 131),
        RegionList("경기도 하남시", 64, 126),
        RegionList("경기도 파주시", 56, 131),
        RegionList("경기도 연천군", 61, 138),
        RegionList("경기도 여주시", 71, 121),
        RegionList("경기도 안양시", 59, 123),
        RegionList("경기도 용인시", 64, 119),
        RegionList("경기도 광주군", 65, 123),
        RegionList("경기도 이천시", 68, 121),
        RegionList("경기도 안성시", 65, 115),
        RegionList("경기도 평택시", 62, 114),
        RegionList("경기도 의정부시", 61, 130),
        RegionList("경기도 의왕시", 50, 122),
        RegionList("경기도 광주시", 65, 123),
        RegionList("경기도 남양주시", 64, 128),
        RegionList("경기도 시흥시", 57, 123),
        RegionList("경기도 여주시", 71, 121),
        RegionList("경기도 성남시", 63, 124),

        // 강원도
        RegionList("강원도 춘천시", 73, 134),
        RegionList("강원도 인제군", 80, 138),
        RegionList("강원도 정선군", 73, 123),
        RegionList("강원도 홍천군", 75, 130),
        RegionList("강원도 철원군", 65, 139),
        RegionList("강원도 삼척시", 98, 125),
        RegionList("강원도 원주시", 76, 122),
        RegionList("강원도 평창군", 84, 123),
        RegionList("강원도 영월군", 86, 119),

        // 경상남도
        RegionList("경상남도 거제시", 90, 69),
        RegionList("경상남도 밀양시", 92, 83),
        RegionList("경상남도 김해시", 95, 77),
        RegionList("경상남도 진주시", 81, 75),
        RegionList("경상남도 진해구", 91, 75),
        RegionList("경상남도 양산시", 90, 79),
        RegionList("경상남도 마산시", 89, 76),
        RegionList("경상남도 창원시", 91, 75),
        RegionList("경상남도 통영시", 87, 68),
        RegionList("경상남도 사천시", 80, 71),
        RegionList("경상남도 합천군", 81, 84),
        RegionList("경상남도 산청군", 76, 80),
        RegionList("경상남도 거창군", 77, 86),
        RegionList("경상남도 고성군", 85, 71),
        RegionList("경상남도 함양군", 74, 82),
        RegionList("경상남도 창녕군", 87, 83),
        RegionList("경상남도 남해군", 77, 68),
        RegionList("경상남도 의령군", 83, 78),
        RegionList("경상남도 함안군", 86, 77),
        RegionList("경상남도 하동군", 74, 73),

        // 경상북도
        RegionList("경상북도 문경시", 81, 106),
        RegionList("경상북도 안동시", 91, 106),
        RegionList("경상북도 상주시", 91, 102),
        RegionList("경상북도 경산시", 91, 90),
        RegionList("경상북도 경주시", 100, 91),
        RegionList("경상북도 영주시", 89, 111),
        RegionList("경상북도 구미시", 84, 96),
        RegionList("경상북도 포항시", 102, 95),
        RegionList("경상북도 김천시", 80, 96),
        RegionList("경상북도 양산시 ", 102, 103),
        RegionList("경상북도 칠곡군", 85, 93),
        RegionList("경상북도 청도군", 91, 86),
        RegionList("경상북도 봉화군", 90, 113),
        RegionList("경상북도 군위군", 88, 99),
        RegionList("경상북도 영덕군", 102, 103),
        RegionList("경상북도 예천군", 86, 107),
        RegionList("경상북도 청송군", 96, 103),
        RegionList("경상북도 고령군", 83, 87),
        RegionList("경상북도 울릉군", 127, 127),
        RegionList("경상북도 울진군", 102, 115),
        RegionList("경상북도 영양군", 91, 108),

        // 충청남도
        RegionList("충청남도 공주시", 62, 102),
        RegionList("충청남도 서산시", 51, 110),
        RegionList("충청남도 천안시", 63, 110),
        RegionList("충청남도 논산시", 62, 97),
        RegionList("충청남도 아산시", 60, 110),
        RegionList("충청남도 보령시", 54, 100),
        RegionList("충청남도 당진시", 54, 112),
        RegionList("충청남도 계룡시", 65, 99),
        RegionList("충청남도 부여군", 59, 99),
        RegionList("충청남도 홍성군", 55, 106),
        RegionList("충청남도 예산군", 58, 107),
        RegionList("충청남도 청양군", 57, 103),
        RegionList("충청남도 금산군", 69, 95),
        RegionList("충청남도 태안군", 48, 109),
        RegionList("충청남도 서천군", 55, 94),

        // 충청북도
        RegionList("충청북도 제천시", 81, 118),
        RegionList("충청북도 충주시", 76, 114),
        RegionList("충청북도 청주시", 69, 107),
        RegionList("충청북도 괴산군", 74, 111),
        RegionList("충청북도 영동군", 74, 97),
        RegionList("충청북도 청원군", 69, 107),
        RegionList("충청북도 보은군", 73, 103),
        RegionList("충청북도 단양군", 82, 115),
        RegionList("충청북도 옥천군", 71, 99),
        RegionList("충청북도 진천군", 68, 111),
        RegionList("충청북도 음성군", 72, 113),
        RegionList("충청북도 증평군", 71, 110),

        // 전라남도
        RegionList("전라남도 공주시", 62, 102),
        RegionList("전라남도 서산시", 51, 110),
        RegionList("전라남도 천안시", 63, 110),
        RegionList("전라남도 논산시", 62, 97),
        RegionList("전라남도 아산시", 60, 110),
        RegionList("전라남도 보령시", 54, 100),
        RegionList("전라남도 당진시", 54, 112),
        RegionList("전라남도 계룡시", 65, 99),
        RegionList("전라남도 부여군", 59, 99),
        RegionList("전라남도 홍성군", 55, 106),
        RegionList("전라남도 예산군", 58, 107),
        RegionList("전라남도 청양군", 57, 103),
        RegionList("전라남도 금산군", 69, 95),
        RegionList("전라남도 태안군", 48, 109),
        RegionList("전라남도 서천군", 55, 94),

        // 전라북도
        RegionList("전라북도 남원시", 68, 80),
        RegionList("전라북도 군산시", 56, 92),
        RegionList("전라북도 김제시", 59, 88),
        RegionList("전라북도 정읍시", 58, 83),
        RegionList("전라북도 익산시", 60, 91),
        RegionList("전라북도 전주시", 63, 89),
        RegionList("전라북도 순창군", 63, 79),
        RegionList("전라북도 고창군", 56, 80),
        RegionList("전라북도 임실군", 66, 84),
        RegionList("전라북도 부안군", 56, 87),
        RegionList("전라북도 장수군", 52, 85),
        RegionList("전라북도 진안군", 70, 88),
        RegionList("전라북도 무주군", 63, 93),
        RegionList("전라북도 완주군", 63, 89),

        // 제주도
        RegionList("제주도 서귀포시", 53, 33),
        RegionList("제주도 제주시", 53, 38),
        RegionList("제주도 남제주군", 53, 33),
    )
}
