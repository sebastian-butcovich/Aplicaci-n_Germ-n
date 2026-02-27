package com.example.proyecto_german.Util

import org.apache.poi.ss.usermodel.BorderStyle
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.VerticalAlignment
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.xssf.usermodel.XSSFCellStyle
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
    fun centrarTextoGrandes(workbook: Workbook, sheet: XSSFSheet, fila:Int, columna:Int): XSSFCellStyle{
        val estiloOriginal = sheet.getRow(fila).getCell(columna).cellStyle
        val nuevoEstilo = workbook.createCellStyle()
        nuevoEstilo.cloneStyleFrom(estiloOriginal)
        nuevoEstilo.alignment = HorizontalAlignment.CENTER
        nuevoEstilo.verticalAlignment = VerticalAlignment.CENTER
        return nuevoEstilo as XSSFCellStyle
    }
}