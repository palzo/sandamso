//package com.example.sansaninfo.MountainInfoData
//
//import com.tickaroo.tikxml.annotation.PropertyElement
//import com.tickaroo.tikxml.annotation.Xml
//import org.simpleframework.xml.Element
//import org.simpleframework.xml.Root
//
//@Xml(name="response")
//data class Response(
//    @Element(name = "header")
//    val header: Header,
//    @Element(name = "body")
//    val body: Body,
//)
//
//@Xml(name = "header")
//data class Header(
//    @PropertyElement
//    var resultCode: Int,
//    @PropertyElement
//    var resultMsg: String
//)
//
//@Xml(name = "body")
//data class Body(
//    @Element
//    val items: Items,
//    @PropertyElement
//    val numOfRows: Int,
//    @PropertyElement
//    val pageNo: Int,
//    @PropertyElement
//    val totalCount: Int,
//)
//
//@Xml
//data class Items(
//    @Element(name = "item")
//    val item: List<Item>
//)
//
//@Xml
//data class Item(
//
//    @PropertyElement(name = "mntninfodtlinfocont")  // 상세정보 1
//    var mntInfo: String?,
//    @PropertyElement(name = "pbtrninfodscrt")  // 2
//    var pbtrninfodscrt: String?,
//    @PropertyElement(name = "crcmrsghtnginfodscrt") // 3
//    var crcmrsghtnginfodscrt: String?,
//    @PropertyElement(name = "crcmrsghtnginfoetcdscrt") // 4
//    var crcmrsghtnginfoetcdscrt: String?,
//    @PropertyElement(name = "crcmrsghtngetcimageseq") // 5
//    var crcmrsghtngetcimageseq: String?,
//    @PropertyElement(name = "hkngpntdscrt") // 6
//    var hkngpntdscrt: String?,
//    @PropertyElement(name = "hndfmsmtnmapimageseq") // 7
//    var hndfmsmtnmapimageseq: String?,
//    @PropertyElement(name = "mntnattchimageseq") // 8
//    var mntnattchimageseq: String?,
//    @PropertyElement(name = "mntninfomapdnldfilenm") // 9
//    var mntninfomapdnldfilenm: String?,
//    @PropertyElement(name = "mntninfotrnspinfoimageseq") // 10
//    var mntninfotrnspinfoimageseq: String?,
//    @PropertyElement(name = "mntnsbttlinfo")  // 산정보부제 11
//    var mntSubInfo: String?,
//    @PropertyElement(name = "ptmntrcmmncoursdscrt") // 12
//    var ptmntrcmmncoursdscrt: String?,
//    @PropertyElement(name = "rcmmncoursimageseq") // 13
//    var rcmmncoursimageseq: String?,
//    @PropertyElement(name = "mntnid")  // 산 코드 14
//    var mntId: String?,
//    @PropertyElement(name = "mntnnm")  // 산 이름 15
//    var mntName: String?,
//    @PropertyElement(name = "hndfmsmtnslctnrson") // 100대명산 선정이유 16
//    var hndfmsmtnslctnrson: String?,
//    @PropertyElement(name = "mntninfopoflc") // 상세 주소 17
//    var mntninfopoflc: String?,
//    @PropertyElement(name = "mntninfohght") // 산 높이 18
//    var mntHeight: String?,
//
////    @field:Element(name = "mntninfomangrtlno")
////    val mntninfomangrtlno: String,
//
//    @PropertyElement(name = "mntninfomngmemnbdnm") // 19
//    var mntninfomngmemnbdnm: String?,
//    @PropertyElement(name = "mntninfodscrt")  // 산정보개관(개관) 20
//    var mntninfodscrt: String?,
//
//
//
//
//){
//    constructor() : this(
//        null,
//        null,
//        null,
//        null,
//        null,
//        null,
//        null,
//        null,
//        null,
//        null,
//        null,
//        null,
//        null,
//        null,
//        null,
//        null,
//        null,
//        null,
//        null,
//        null,
//    )
//}