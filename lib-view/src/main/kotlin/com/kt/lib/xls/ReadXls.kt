package com.kt.lib.xls

import android.content.Context
import jxl.Workbook

class ReadXls {
    companion object {
        inline fun <reified T> readByRow(path: String, ctx: Context): List<T> {
            return readByRow(path, ctx, 0)
        }

        inline fun <reified T> readByRow(path: String, ctx: Context, rowIndex: Int): List<T> {
            val list = ArrayList<T>()
            val aClass = T::class.java
            val fields = aClass.declaredFields
            val workbook = Workbook.getWorkbook(ctx.assets.open(path))
            val sheet = workbook.getSheet(0)
            var rows = sheet.rows
            var obj: T?
            while (rows > rowIndex) {
                obj = aClass.newInstance()
                rows--
                for (field in fields) {
                    if (field.isAnnotationPresent(Column::class.java)) {
                        val annotation = field.getAnnotation(Column::class.java)
                        field.isAccessible = true
                        field.set(obj, sheet.getCell(annotation.value, rows).contents)
                    }
                }
                list.add(obj)
            }
            workbook.close()
            return list
        }
    }

    class GZDJEntityCopy { // 唯一标识 private java.lang.String id; // 日期 @Excel(name="日期") private String riqi; // 运维单位 @Excel(name="运维单位") private String ywdw; // 故障类型 @Excel(name="故障类型") private String gzlx; // 重合闸情况 @Excel(name="重合闸情况") private String chzqk; // 变电站电压等级 @Excel(name="变电站电压等级") private String bdzdydj; // 所属变电站 @Excel(name="所属变电站") private String ssbdz; // 开关编号 @Excel(name="开关编号") private String kgbh; // 线路名称 @Excel(name="线路名称") private String xlmc; // 支线名称 @Excel(name="支线名称") private String zxmc; // 变台 @Excel(name="变台") private String bt; // 故障原因 @Excel(name="故障原因") private String gzyy; // 故障分类1 @Excel(name="故障分类1") private String gzfl1; // 故障分类2 @Excel(name="故障分类2") private String gzfl2; // 故障分类3 @Excel(name="故障分类3") private String gzfl3; // 责任单位 @Excel(name="责任单位") private String zrdw; // 责任班组 @Excel(name="责任班组") private String zrbz; // 责任人 @Excel(name="责任人") private String zrr; // 故障点上级开关名称 @Excel(name="故障点上级开关名称") private String gzd_sjkg_mc; // 故障点上级开关动作情况 @Excel(name="故障点上级开关动作情况") private String gzd_sjkg_dzqk; // 停电时间 @Excel(name="停电时间") private String tdsj; // 非故障区复电时间 @Excel(name="非故障区复电时间") private String fgzfdsj; // 恢复时间 @Excel(name="恢复时间") private String fdsj; // 分钟数 @Excel(name="分钟数") private String tdsc; // 是否录入停电系统 @Excel(name="是否录入停电系统") private String yn_lrtdxt; // 停电配变数量 @Excel(name="停电配变数量") private String tdpbs; // 影响用户数量 @Excel(name="影响用户数量") private String yxyhs; // 备注 @Excel(name="备注") private String bz; // 故障分析 @Excel(name="故障分析") private String gzfx; // 新增时间 @Excel(name="新增时间") private String createdate;

//        Gzdsjkgmc   Kgdzqk    Fdsj  sflrtdxt   tdpbsl
//        改gzd_sjkg_mc    fdsj   yn_lrtdxt   tdpbs  yxyhs
//        无 kgdzqk  gxgz   zxgz  byqgz
    }
}