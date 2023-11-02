package com.example.sansaninfo.SearchPage

class GooData {
    companion object {
        // 구, 군 뺴고

        // 서울
        val seoulList: ArrayList<String> = arrayListOf(
            "강서구", "관악구", "서초구", "강남구", "도봉구", "중랑구",
            "은평구", "서대문구", "종로구", "강북구", "노원구", "광진구"
        )
        // 대전
        val daejeonList: ArrayList<String> = arrayListOf(
            "대덕구", "서구", "중구", "동구", "유성구"
        )

        // 대구
        val daeguList: ArrayList<String> = arrayListOf(
            "달성군", "달서구", "수성구", "동구"
        )

        // 부산
        val busanList: ArrayList<String> = arrayListOf(
            "기장군", "북구", "금정구", "강서구", "연제구", "부산진구", "사하구", "서구", "동래구"
        )

        // 인천
        val incheonList: ArrayList<String> = arrayListOf(
            "서구", "남동구", "강화군", "옹진군", "중구", "부평구"
        )

        // 광주
        val gwangjuList: ArrayList<String> = arrayListOf(
            "동구", "서구", "광산구"
        )

        // 울산
        val ulsanList: ArrayList<String> = arrayListOf(
            "울주군", "동구", "중구"
        )

        // 경기
        val gyeonggiList: ArrayList<String> = arrayListOf(
            "가평군", "포천시", "김포시", "양평군", "양주시", "하남시", "파주시", "연천군", "여주군", "안양시",
            "용인시", "광주군", "이천시", "안성시", "평택시", "의정부시", "의왕시", "광주시", "남양주시",
            "시흥시", "여주시", "성남시"
        )

        // 강원
        val gangwonList: ArrayList<String> = arrayListOf(
            "춘천시", "인제군", "정선군", "홍천군", "철원군", "삼척시", "원주시", "평창군", "영월군", "강릉시",
            "고성군", "태백시", "횡성군", "화천군", "동해시"
        )

        // 경남
        val gyeongnamList: ArrayList<String> = arrayListOf(
            "거제시", "밀양시", "김해시", "진주시", "진해구", "양산시", "마산시", "창원시", "통영시",
            "사천시", "합천군", "산청군", "거창군", "고성군", "함양군", "창녕군", "남해군", "의령군", "구례군",
            "함안군", "하동군",
        )

        // 경북
        val gyeongbukList: ArrayList<String> = arrayListOf(
            "문경시", "안동시", "상주시", "경산시", "경주시", "영주시", "구미시", "포항시", "김천시",
            "양산시", "칠곡군", "청도군", "봉화군", "군위군", "영덕군", "예천군", "청송군", "고령군",
            "울릉군", "울진군", "영양군"
        )

        // 충남
        val chungnamList: ArrayList<String> = arrayListOf(
            "공주시", "서산시", "천안시", "논산시", "아산시", "보령시", "당진시", "계룡시",
            "부여군", "홍성군", "예산군", "청양군", "금산군", "태안군", "서천군", "영동군"
        )

        // 충북
        val chungbukList: ArrayList<String> = arrayListOf(
            "제천시", "충주시", "청주시", "괴산군", "영동군", "청원군", "보은군", "단양군",
            "옥천군", "진천군", "음성군", "증평군"
        )

        // 전남
        val jeonnamList: ArrayList<String> = arrayListOf(
            "나주시", "순천시", "광양시", "여수시", "목포시",
            "장흥군", "해남군", "무안군", "화순군", "구례군", "함평군", "장성군", "곡성군",
            "담양군", "영광군", "영암군", "진도군", "강진군", "신안군", "고흥군", "보성군", "완도군", "여천군"
        )

        // 전북
        val jeonbukList: ArrayList<String> = arrayListOf(
            "남원시", "군산시", "김제시", "정읍시", "익산시", "전주시",
            "순창군", "고창군", "임실군", "부안군", "장수군", "진안군", "무주군", "완주군"
        )

        // 제주
        val jejuList: ArrayList<String> = arrayListOf(
            "서귀포시", "제주시", "남제주군"
        )

        fun getRegionList(city: String): ArrayList<String> {
            return when (city) {
                "서울특별시" -> seoulList
                "대전광역시" -> daejeonList
                "대구광역시" -> daeguList
                "부산광역시" -> busanList
                "인천광역시" -> incheonList
                "광주광역시" -> gwangjuList
                "울산광역시" -> ulsanList
                "경기도" -> gyeonggiList
                "강원도" -> gangwonList
                "경상남도" -> gyeongnamList
                "경상북도" -> gyeongbukList
                "충청남도" -> chungnamList
                "충청북도" -> chungbukList
                "전라남도" -> jeonnamList
                "전라북도" -> jeonbukList
                "제주도" -> jejuList
                else -> arrayListOf() // 기본값은 빈 리스트 또는 다른 처리
            }
        }
    }
}

