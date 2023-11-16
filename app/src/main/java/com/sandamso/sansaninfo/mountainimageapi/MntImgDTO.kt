package com.sandamso.sansaninfo.mountainimageapi

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml


@Xml(name = "response")
data class ImgResponse(
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

@Xml(name = "items")
data class Items(
    @Element(name="item")
    val item: List<Item>
)

@Xml
data class Item(
    @PropertyElement(name = "imgfilename")
    var imgURL: String = "",
    @PropertyElement(name = "imgname")
    var imgName: String = "",
    @PropertyElement(name = "imgno")
    var imgNo: String = ""
)