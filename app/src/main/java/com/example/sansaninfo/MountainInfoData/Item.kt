package com.example.sansaninfo.MountainInfoData

import com.google.gson.annotations.SerializedName

data class Item(

    @SerializedName("crcmrsghtngetcimageseq")
    val crcmrsghtngetcimageseq: String,
    @SerializedName("crcmrsghtnginfodscrt")
    val crcmrsghtnginfodscrt: String,
    @SerializedName("crcmrsghtnginfoetcdscrt")
    val crcmrsghtnginfoetcdscrt: String,
    @SerializedName("hkngpntdscrt")
    val hkngpntdscrt: Hkngpntdscrt,
    @SerializedName("hndfmsmtnmapimageseq")
    val hndfmsmtnmapimageseq: String,
    @SerializedName("hndfmsmtnslctnrson") // 설명1
    val hndfmsmtnslctnrson: String,
    @SerializedName("mntnattchimageseq")
    val mntnattchimageseq: String,
    @SerializedName("mntnid")  // 산 id
    val mntnid: Int,
    @SerializedName("mntninfodscrt")  // 설명2
    val mntninfodscrt: String,
    @SerializedName("mntninfodtlinfocont")  // 설명3
    val mntninfodtlinfocont: String,
    @SerializedName("mntninfohght") // 산 높이
    val mntninfohght: Int,
    @SerializedName("mntninfomangrtlno")
    val mntninfomangrtlno: String,
    @SerializedName("mntninfomapdnldfilenm")
    val mntninfomapdnldfilenm: String,
    @SerializedName("mntninfomngmemnbdnm")
    val mntninfomngmemnbdnm: String,
    @SerializedName("mntninfopoflc") // 상세 주소
    val mntninfopoflc: String,
    @SerializedName("mntninfotrnspinfoimageseq")
    val mntninfotrnspinfoimageseq: String,
    @SerializedName("mntnnm")  // 산 이름
    val mntnnm: String,
    @SerializedName("mntnsbttlinfo")
    val mntnsbttlinfo: String,
    @SerializedName("pbtrninfodscrt")
    val pbtrninfodscrt: String,
    @SerializedName("ptmntrcmmncoursdscrt")
    val ptmntrcmmncoursdscrt: Ptmntrcmmncoursdscrt,
    @SerializedName("rcmmncoursimageseq")
    val rcmmncoursimageseq: String
)