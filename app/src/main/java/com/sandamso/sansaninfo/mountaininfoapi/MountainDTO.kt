package com.sandamso.sansaninfo.mountaininfoapi

import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml
import com.tickaroo.tikxml.annotation.Element

@Xml(name = "response")
data class XmlResponse(
    @Element(name = "header")
    var header: Header = Header(),

    @Element(name = "body")
    var body: Body
)
@Xml(name = "header")
data class Header(
    @PropertyElement(name = "resultCode")
    var resultCode: String = "",

    @PropertyElement(name = "resultMsg")
    var resultMsg: String = ""
)
@Xml(name = "body")
data class Body(
    @Element(name="items")
    val items: Items,

    @PropertyElement(name = "numOfRows")
    var numOfRows: Int = 0,

    @PropertyElement(name = "pageNo")
    var pageNo: Int = 0,

    @PropertyElement(name = "totalCount")
    var totalCount: Int = 0
)
@Xml(name= "items")
data class Items(
    @Element(name="item")
    val item: List<Item>
)
@Xml
data class Item(

    @PropertyElement(name = "mntninfodtlinfocont")  // 상세정보
    var mntInfo: String= "",
    @PropertyElement(name = "mntnsbttlinfo")  // 산정보부제
    var mntnsbttlinfo: String= "",
    @PropertyElement(name = "mntnid")  // 산 코드
    var mntId: String= "",
    @PropertyElement(name = "mntnnm")  // 산 이름
    var mntName: String= "",
    @PropertyElement(name = "hndfmsmtnslctnrson") // 100대명산 선정 이유
    var mntSubInfo: String= "",
    @PropertyElement(name = "mntninfopoflc") // 상세 주소
    var mntAddress: String= "",
    @PropertyElement(name = "mntninfohght") // 산 높이
    var mntHeight: String= "",
    @PropertyElement(name = "mntninfodscrt")  // 산정보개관(개관)
    var mntLastInfo: String= "",

)







