package com.example.sansaninfo.SearchPage

object MountainMapping {
    private val mountainMap = hashMapOf(
        // key 값  4bpUeSQaXnUDSalDsumQ5dkxA+bJXWN4dhwsYexJp6wAJnadjR+UoIVo1Dhac/spEq1HRVngbbHuY8QLzUwVBg==
        // 이미지 파일 경로  https://www.forest.go.kr/images/data/down/mountain/
        // 북한산 20000317_3.jpg
        "가덕산" to "418200101",
        "가라산" to "483100101",
        "가령산" to "0000",
        "가리산" to "416500201",
        "가야산" to "478400101",
        "가리왕산" to "427700101",
        "가산" to "478500101",
        "가야산" to "478400101",
        "가은산" to "431504101",
        "가지산" to "468000101",
        "가칠산" to "0000",
        "가학산" to "468200201",
        "가현산" to "282600101",
        "낙가산" to "",
        "덕가산" to "",
        "방가산" to "",
        "학가산" to "",
        "구나무산" to "",
        "나각산" to "",
        "나래산" to "",
        "다락산" to "",
        "다봉산" to "",
        "바라산" to "",
        "오산" to "",
        "적라산" to "",
        "한라산" to "",
        "거마산" to "",
        "검마산" to "",
        "달마산" to "",
        "마구산" to "",
        "마금산" to "",
        "마니산" to "",
        "마대산" to "",
        "마래산" to "",
        "마명산" to "",
        "마복산" to "",
        "마산" to "",
        "마옥산" to "",
        "마이산" to "",
        "마적산" to "",
        "마정산" to "",
        "마천산" to "",
        "망마산" to "",
        "백마산" to "",
        "옥마산" to "",
        "용마산" to "",
        "천마산" to "",
        "철마산" to "",
        "치마산" to "",
        "바라산" to "",
        "바랑산" to "",
        "바위산" to "",




    )

    fun getMountainCode(mountainName: String?): String? {
        return mountainMap[mountainName]
    }
}