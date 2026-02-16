package com.example.proyecto_german.Util

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.example.proyecto_german.Model.PerforacionModel
import com.example.proyecto_german.Model.Profundidad
import org.apache.poi.ss.usermodel.BorderStyle
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.ss.usermodel.VerticalAlignment
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class ManipularExcel {
    fun copiarPlantillaPublica(context: Context, nombreArchivo: String): File {
        val carpeta = File(context.getExternalFilesDir(null), "SPT")
        if (!carpeta.exists()) {
            carpeta.mkdirs()
        }
        val archivoDestino = File(carpeta, nombreArchivo)
        context.assets.open("Formato_registro_perforacion_base.xlsx")
            .use { input ->
                archivoDestino.outputStream().use { outPut ->
                    input.copyTo(outPut)
                }
            }
        return archivoDestino
    }

    fun completarExcel(
        archivo: File,
        perforacion: PerforacionModel,
        profundidades: List<Profundidad>
    ) {
        val workbook = XSSFWorkbook(FileInputStream(archivo))
        val sheet = workbook.getSheetAt(0);
        var ultimoSucs: String = ""
        //Datos generales -- Cabecera superior
        sheet.getRow(6).getCell(7).setCellValue(perforacion.cliente)
        sheet.getRow(6).getCell(34).setCellValue(perforacion.fecha)
        sheet.getRow(7).getCell(7).setCellValue(perforacion.atencion)
        sheet.getRow(7).getCell(34).setCellValue(perforacion.numeroPerforacion.toString())
        sheet.getRow(8).getCell(7).setCellValue(perforacion.proyecto)
        sheet.getRow(9).getCell(7).setCellValue(perforacion.localizacion)
        sheet.getRow(9).getCell(34).setCellValue(perforacion.profundidadMetros)
        //Fila 12 - Datos intermedios
        sheet.getRow(11).getCell(8).setCellValue("E: ${perforacion.coordenadaE} °")
        sheet.getRow(11).getCell(12).setCellValue("N: ${perforacion.coordenadaE} °")
        if (perforacion.nivelFreatico) {
            sheet.getRow(11).getCell(18).setCellValue("SI")
        } else {
            sheet.getRow(11).getCell(18).setCellValue("NO")
        }
        sheet.getRow(11).getCell(23).setCellValue(perforacion.lecturaInicial)
        sheet.getRow(11).getCell(29).setCellValue(perforacion.lecturaFinal)
        sheet.getRow(11).getCell(34).setCellValue(perforacion.estadoDelTiempo)
        profundidades.forEach { profundidad ->
            if (determinarSiHayGolpes(profundidad) && (ultimoSucs == profundidad.sucs.toString() || ultimoSucs == "")) {
                escribirLineaDivisoraDeProfundidades(
                    workbook,
                    sheet,
                    profundidad,
                    false
                )
            } else {
                escribirLineaDivisoraDeProfundidades(
                    workbook, sheet, profundidad, true
                )
            }
            escribirDatos(
                profundidad,
                sheet,
            )
            ultimoSucs = profundidad.sucs.toString()
        }
        FileOutputStream(archivo).use { output ->
            workbook.write(output)
        }
        workbook.close()
    }

    private fun escribirDatos(
        profundidad: Profundidad,
        sheet: XSSFSheet,
    ) {
        val filaFinal = profuntidadANumeroDeFila(profundidad.profundidad_final)
        val filaInicial = profuntidadANumeroDeFila(profundidad.profundidad_inicial)
        sheet.getRow(filaFinal-4).getCell(6).setCellValue(profundidad.numero_muestra)
        sheet.getRow(filaFinal-4).getCell(7).setCellValue(profundidad.tipo)
        sheet.getRow(filaFinal-4).getCell(9).setCellValue(profundidad.golpes1.toString())
        sheet.getRow(filaFinal-4).getCell(10).setCellValue(profundidad.golpes2.toString())
        sheet.getRow(filaFinal-4).getCell(11).setCellValue(profundidad.golpes3.toString())
        sheet.getRow(filaInicial).getCell(12).setCellValue(profundidad.sucs.toString())
        sheet.getRow(filaInicial).getCell(13).setCellValue(profundidad.descripcion)
    }

    private fun determinarSiHayGolpes(profundidad: Profundidad): Boolean {
        return profundidad.golpes1 != 0 || profundidad.golpes2 != 0 || profundidad.golpes3 != 0;
    }

    fun exportarSTP(
        context: Context,
        perforacion: PerforacionModel,
        profundidades: List<Profundidad>
    ) {
        val nombreArchivo = "STP_${perforacion.numeroPerforacion}.xlsx"
        val archivo = copiarPlantillaPublica(context, nombreArchivo)
        completarExcel(archivo, perforacion, profundidades)
        compartirArchivo(context, archivo)
    }

    fun compartirArchivo(context: Context, archivo: File) {
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            archivo
        )
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        val chooser = Intent.createChooser(intent, "Compartir archivo SPT")
        context.startActivity(chooser)
    }

    private fun profuntidadANumeroDeFila(profundiadMts: Double): Int {
        //Los 0.0 mts están ubicados en la fila 18
        val filaInicial: Int = 18;
        val filaDadoProfundiad: Double = (117.0 - 18.0) / (10.0 - 0.0);
        val fila = filaInicial + (filaDadoProfundiad * profundiadMts).toInt()
        if (fila > 117) {
            return 117
        } else if (fila < 18) {
            return 18
        }
        return fila
    }

    private fun escribirLineaDivisoraDeProfundidades(
        workbook: XSSFWorkbook,
        sheet: XSSFSheet,
        profundidad: Profundidad,
        cambioSucs: Boolean
    ) {
        val estilo = workbook.createCellStyle()
        var filaFinal = profuntidadANumeroDeFila(profundidad.profundidad_final)
        var filaInicial = profuntidadANumeroDeFila(profundidad.profundidad_inicial)
        estilo.alignment = HorizontalAlignment.CENTER
        if (cambioSucs) {
            estilo.borderBottom = BorderStyle.THIN
            estilo.borderLeft = BorderStyle.THIN
            estilo.borderRight = BorderStyle.THIN
            estilo.borderTop = BorderStyle.NONE
            for (i in 6..38) {
                sheet.getRow(filaFinal).getCell(i).cellStyle = estilo
            }
        } else {
            //Estilos para la columna número y tipo
            estilo.borderBottom = BorderStyle.DOTTED
            estilo.borderLeft = BorderStyle.DASHED
            estilo.borderRight = BorderStyle.DASHED
            estilo.borderTop = BorderStyle.DOTTED
            estilo.fillForegroundColor = IndexedColors.LEMON_CHIFFON.index
            estilo.fillPattern = FillPatternType.SOLID_FOREGROUND
            estilo.setBottomBorderColor(IndexedColors.GREY_40_PERCENT.index)
            estilo.setTopBorderColor(IndexedColors.GREY_40_PERCENT.index)
            sheet.addMergedRegion(CellRangeAddress(filaFinal - 4, filaFinal, 6, 6))
            sheet.addMergedRegion(CellRangeAddress(filaFinal - 4, filaFinal, 7, 8))
            //Columna 6
            sheet.getRow(filaFinal).getCell(6).cellStyle = estilo;
            sheet.getRow(filaFinal-4).getCell(6).cellStyle = estilo;
            //Columna 7
            sheet.getRow(filaFinal).getCell(7).cellStyle = estilo;
            sheet.getRow(filaFinal-4).getCell(7).cellStyle = estilo;
            estilo.borderRight = BorderStyle.THIN
            sheet.getRow(filaFinal).getCell(8).cellStyle = estilo;
            sheet.getRow(filaFinal-4).getCell(8).cellStyle = estilo;
            for (i in 9..11) {
                sheet.addMergedRegion(CellRangeAddress(filaFinal-4, filaFinal, i, i))
            }
            //Estilos Columna 9
            estilo.borderLeft = BorderStyle.MEDIUM
            estilo.borderRight = BorderStyle.DOTTED
            sheet.getRow(filaFinal-4).getCell(9).cellStyle = estilo;
            sheet.getRow(filaFinal).getCell(9).cellStyle = estilo;
            //Estilos Columna 10
            estilo.borderLeft = BorderStyle.DOTTED
            estilo.borderRight = BorderStyle.DOTTED
            sheet.getRow(filaFinal-4).getCell(10).cellStyle = estilo;
            sheet.getRow(filaFinal).getCell(10).cellStyle = estilo;
            //Estilos Columna 11
            estilo.borderLeft = BorderStyle.DOTTED
            estilo.borderRight = BorderStyle.MEDIUM
            sheet.getRow(filaFinal-4).getCell(11).cellStyle = estilo;
            sheet.getRow(filaFinal).getCell(11).cellStyle = estilo;
        }
    }
}