package com.example.sansaninfo.MountainInfoData

import com.google.gson.annotations.SerializedName
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "response", strict = false)
data class ApiResponse(
    @field:Element(name = "body")
    val body: ApiBody? = null,
    @field:Element(name = "header")
    val header: ApiHeader? = null,
)
data class ApiHeader(
    @field:Element(name = "resultCode")
    val resultCode: Int,
    @field:Element(name = "resultMsg")
    val resultMsg: String
)
data class ApiBody(
    @field:Element(name = "items")
    val items: ApiItems? = null
)
data class ApiItems(
    @field:ElementList(entry = "item", inline = true)
    var item: List<ApiItem>? = null
)
data class ApiItem(

    @field:Element(name = "crcmrsghtngetcimageseq")
    val crcmrsghtngetcimageseq: String? = null,
    @field:Element(name = "crcmrsghtnginfodscrt")
    val crcmrsghtnginfodscrt: String? = null,
    @field:Element(name = "crcmrsghtnginfoetcdscrt")
    val crcmrsghtnginfoetcdscrt: String? = null,
    @field:Element(name = "hkngpntdscrt")
    var hkngpntdscrt: String? = null,
    @field:Element(name = "hndfmsmtnmapimageseq")
    val hndfmsmtnmapimageseq: String? = null,
    @field:Element(name = "hndfmsmtnslctnrson") // 100대명산 선정이유
    val hndfmsmtnslctnrson: String? = null,
    @field:Element(name = "mntnattchimageseq")
    val mntnattchimageseq: String? = null,
    @field:Element(name = "mntnid")  // 산 코드
    val mntId: String? = null,
    @field:Element(name = "mntninfodscrt")  // 산정보개관(개관)
    val mntninfodscrt: String? = null,
    @field:Element(name = "mntninfodtlinfocont")  // 상세정보
    val mntInfo: String? = null,
    @field:Element(name = "mntninfohght") // 산 높이
    val mntHeight: String? = null,
    @field:Element(name = "mntninfomangrtlno")
    val mntninfomangrtlno: String? = null,
    @field:Element(name = "mntninfomapdnldfilenm")
    val mntninfomapdnldfilenm: String? = null,
    @field:Element(name = "mntninfomngmemnbdnm")
    val mntninfomngmemnbdnm: String? = null,
    @field:Element(name = "mntninfopoflc") // 상세 주소
    val mntninfopoflc: String? = null,
    @field:Element(name = "mntninfotrnspinfoimageseq")
    val mntninfotrnspinfoimageseq: String,
    @field:Element(name = "mntnnm")  // 산 이름
    val mntName: String? = null,
    @field:Element(name = "mntnsbttlinfo")  // 산정보부제
    val mntSubInfo: String? = null,
    @field:Element(name = "pbtrninfodscrt")
    val pbtrninfodscrt: String? = null,
    @field:Element(name = "ptmntrcmmncoursdscrt")
    var ptmntrcmmncoursdscrt: String? = null,
    @field:Element(name = "rcmmncoursimageseq")
    val rcmmncoursimageseq: String? = null,
)