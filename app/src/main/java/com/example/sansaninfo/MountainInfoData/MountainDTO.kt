package com.example.sansaninfo.MountainInfoData

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "response", strict = false)
data class Response(
    @field:Element(name = "header", required = false)
    var header: Header = Header(),

    @field:Element(name = "body", required = false)
    var body: Body = Body()
)

@Root(name = "header", strict = false)
data class Header(
    @field:Element(name = "resultCode", required = false)
    var resultCode: String = "",

    @field:Element(name = "resultMsg", required = false)
    var resultMsg: String = ""
)

@Root(name = "body", strict = false)
data class Body(
    @field:ElementList(name = "items", inline = true, required = false)
    var items: List<Item> = mutableListOf(),

    @field:Element(name = "numOfRows", required = false)
    var numOfRows: Int = 0,

    @field:Element(name = "pageNo", required = false)
    var pageNo: Int = 0,

    @field:Element(name = "totalCount", required = false)
    var totalCount: Int = 0
)

@Root(name = "item", strict = false)
data class Item(

    @field:Element(name = "mntninfodtlinfocont", required = false)  // 상세정보 1
    val mntInfo: String,
    @field:Element(name = "pbtrninfodscrt", required = false)  // 2
    val pbtrninfodscrt: String,
    @field:Element(name = "crcmrsghtnginfodscrt", required = false) // 3
    val crcmrsghtnginfodscrt: String,
    @field:Element(name = "crcmrsghtnginfoetcdscrt", required = false) // 4
    val crcmrsghtnginfoetcdscrt: String,
    @field:Element(name = "crcmrsghtngetcimageseq", required = false) // 5
    val crcmrsghtngetcimageseq: String,
    @field:Element(name = "hkngpntdscrt", required = false) // 6
    var hkngpntdscrt: String,
    @field:Element(name = "hndfmsmtnmapimageseq", required = false) // 7
    val hndfmsmtnmapimageseq: String,
    @field:Element(name = "mntnattchimageseq", required = false) // 8
    val mntnattchimageseq: String,
    @field:Element(name = "mntninfomapdnldfilenm", required = false) // 9
    var mntninfomapdnldfilenm: String,
    @field:Element(name = "mntninfotrnspinfoimageseq", required = false) // 10
    val mntninfotrnspinfoimageseq: String,
    @field:Element(name = "mntnsbttlinfo", required = false)  // 산정보부제 11
    val mntSubInfo: String,
    @field:Element(name = "ptmntrcmmncoursdscrt", required = false) // 12
    var ptmntrcmmncoursdscrt: String,
    @field:Element(name = "rcmmncoursimageseq", required = false) // 13
    val rcmmncoursimageseq: String,
    @field:Element(name = "mntnid", required = false)  // 산 코드 14
    val mntId: String,
    @field:Element(name = "mntnnm", required = false)  // 산 이름 15
    val mntName: String,
    @field:Element(name = "hndfmsmtnslctnrson", required = false) // 100대명산 선정이유 16
    val hndfmsmtnslctnrson: String,
    @field:Element(name = "mntninfopoflc", required = false) // 상세 주소 17
    val mntninfopoflc: String,
    @field:Element(name = "mntninfohght", required = false) // 산 높이 18
    val mntHeight: String,
    @field:Element(name = "mntninfomngmemnbdnm", required = false) // 19
    val mntninfomngmemnbdnm: String,
    @field:Element(name = "mntninfodscrt", required = false)  // 산정보개관(개관) 20
    val mntninfodscrt: String,

//    @field:Element(name = "mntninfomangrtlno")
//    val mntninfomangrtlno: String,



)







