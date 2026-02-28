package com.example.proyecto_german.Util

import org.apache.poi.ss.usermodel.BorderStyle
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.ss.usermodel.VerticalAlignment
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import org.apache.poi.xssf.usermodel.XSSFColor
import org.apache.poi.xssf.usermodel.XSSFSheet

class EstiloExcel() {
    fun modificarEstioloColumnaNro(workbook: Workbook, sheet: XSSFSheet, fila:Int, columna:Int): XSSFCellStyle{
        val estiloOriginal = sheet.getRow(fila).getCell(columna).cellStyle
        val nuevoEstilo = workbook.createCellStyle()
        nuevoEstilo.cloneStyleFrom(estiloOriginal)
        nuevoEstilo.alignment = HorizontalAlignment.CENTER
        nuevoEstilo.borderBottom = BorderStyle.THIN
        return nuevoEstilo as XSSFCellStyle
    }
    fun estilarGolpes(workbook: Workbook, sheet: XSSFSheet, fila:Int, columna:Int,posicion:Int,
                      tamanioLista:Int,
                      filaFinal:Int): XSSFCellStyle{
        val color = XSSFColor(
            byteArrayOf(
                255.toByte(),
                242.toByte(),
                204.toByte()
            )
        )
        val estiloOriginal = sheet.getRow(fila).getCell(columna).cellStyle
        val nuevoEstilo = workbook.createCellStyle()
        nuevoEstilo.cloneStyleFrom(estiloOriginal)
        nuevoEstilo.setFillForegroundColor(color)
        nuevoEstilo.fillPattern = FillPatternType.SOLID_FOREGROUND
        nuevoEstilo.alignment = HorizontalAlignment.CENTER
        nuevoEstilo.verticalAlignment = VerticalAlignment.BOTTOM
        if(posicion !=0 && posicion != tamanioLista  ){
            nuevoEstilo.borderBottom = BorderStyle.DOTTED
            nuevoEstilo.borderTop = BorderStyle.DOTTED
            nuevoEstilo.bottomBorderColor = IndexedColors.GREY_50_PERCENT.index
            nuevoEstilo.topBorderColor = IndexedColors.GREY_50_PERCENT.index
        }else{
            if(posicion == 0){
                nuevoEstilo.borderBottom = BorderStyle.DOTTED
                nuevoEstilo.bottomBorderColor = IndexedColors.GREY_50_PERCENT.index
            }
            if(posicion == tamanioLista){
                if(fila != filaFinal){
                    nuevoEstilo.borderBottom = BorderStyle.DOTTED
                    nuevoEstilo.borderTop = BorderStyle.DOTTED
                    nuevoEstilo.bottomBorderColor = IndexedColors.GREY_50_PERCENT.index
                    nuevoEstilo.topBorderColor = IndexedColors.GREY_50_PERCENT.index
                }else{
                    nuevoEstilo.borderTop = BorderStyle.DOTTED
                    nuevoEstilo.topBorderColor = IndexedColors.GREY_50_PERCENT.index
                }
            }
        }
        return nuevoEstilo as XSSFCellStyle
    }
}