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
    var mntInfo: String= "",
    @field:Element(name = "pbtrninfodscrt", required = false)  // 2
    var pbtrninfodscrt: String= "",
    @field:Element(name = "crcmrsghtnginfodscrt", required = false) // 3
    var crcmrsghtnginfodscrt: String= "",
    @field:Element(name = "crcmrsghtnginfoetcdscrt", required = false) // 4
    var crcmrsghtnginfoetcdscrt: String= "",
    @field:Element(name = "crcmrsghtngetcimageseq", required = false) // 5
    var crcmrsghtngetcimageseq: String= "",
    @field:Element(name = "hkngpntdscrt", required = false) // 6
    var hkngpntdscrt: String= "",
    @field:Element(name = "hndfmsmtnmapimageseq", required = false) // 7
    var hndfmsmtnmapimageseq: String= "",
    @field:Element(name = "mntnattchimageseq", required = false) // 8
    var mntnattchimageseq: String= "",
    @field:Element(name = "mntninfomapdnldfilenm", required = false) // 9
    var mntninfomapdnldfilenm: String= "",
    @field:Element(name = "mntninfotrnspinfoimageseq", required = false) // 10
    var mntninfotrnspinfoimageseq: String= "",
    @field:Element(name = "mntnsbttlinfo", required = false)  // 산정보부제 11
    var mntSubInfo: String= "",
    @field:Element(name = "ptmntrcmmncoursdscrt", required = false) // 12
    var ptmntrcmmncoursdscrt: String= "",
    @field:Element(name = "rcmmncoursimageseq", required = false) // 13
    var rcmmncoursimageseq: String= "",
    @field:Element(name = "mntnid", required = false)  // 산 코드 14
    var mntId: String= "",
    @field:Element(name = "mntnnm", required = false)  // 산 이름 15
    var mntName: String= "",
    @field:Element(name = "hndfmsmtnslctnrson", required = false) // 100대명산 선정이유 16
    var hndfmsmtnslctnrson: String= "",
    @field:Element(name = "mntninfopoflc", required = false) // 상세 주소 17
    var mntninfopoflc: String= "",
    @field:Element(name = "mntninfohght", required = false) // 산 높이 18
    var mntHeight: String= "",
    @field:Element(name = "mntninfomngmemnbdnm", required = false) // 19
    var mntninfomngmemnbdnm: String= "",
    @field:Element(name = "mntninfodscrt", required = false)  // 산정보개관(개관) 20
    var mntninfodscrt: String= "",

    @field:Element(name = "mntninfomangrtlno")
    val mntninfomangrtlno: String = "",

)







