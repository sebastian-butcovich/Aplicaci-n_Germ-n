package com.example.proyecto_german.Util

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.example.proyecto_german.Model.GolpesStp
import com.example.proyecto_german.Model.PerforacionModel
import com.example.proyecto_german.Model.Profundidad
import com.example.proyecto_german.Model.Sucs
import com.example.proyecto_german.Model.Temporales.ProfundidadConGolpes
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
        profundidadConGolpes: List<ProfundidadConGolpes>
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
        profundidadConGolpes.forEach { profundidadConGolpe ->
            escribirLineaDivisoraDeProfundidades(
                workbook,
                sheet,
                profundidadConGolpe.profundidad
            )
            formatearCeldas(workbook,sheet,profundidadConGolpe)
            escribirDatos(profundidadConGolpe, sheet)
            darEstiloALasEntradas(workbook,sheet,profundidadConGolpe)
        }
        FileOutputStream(archivo).use { output ->
            workbook.write(output)
        }
        workbook.close()
    }

    private fun formatearCeldas(
        workbook: XSSFWorkbook,
        sheet: XSSFSheet,
        profundidadConGolpes: ProfundidadConGolpes
    ) {
        //Obtener fila inicial fila final
        //Si no tiene golpes simplemente formatear sucs y descripción
        var filaFinal = profuntidadANumeroDeFila(profundidadConGolpes.profundidad.profundidadFinal!!)-1
        var filaInicial = profuntidadANumeroDeFila(profundidadConGolpes.profundidad.profundidadInicial!!)
        val estilo = EstiloExcel()
        //Juntar celdas de Sucs y Descripcion
        //Socs
        sheet.addMergedRegion(CellRangeAddress(filaInicial,filaFinal,12,12))
        //Descripcion
        sheet.addMergedRegion(CellRangeAddress(filaInicial,filaFinal,13,15))
        //Aplicar estilos a las columnas
        val golpes = profundidadConGolpes.golpes
        for(golpe in golpes){
            filaInicial = profuntidadANumeroDeFila(golpe.profundidad_inicial)
            filaFinal = profuntidadANumeroDeFila(golpe.profundidad_final)
            sheet.addMergedRegion(CellRangeAddress(filaInicial,filaFinal-1,6,6))
            sheet.addMergedRegion(CellRangeAddress(filaInicial,filaFinal-1,7,8))
            sheet.addMergedRegion(CellRangeAddress(filaInicial,filaFinal-1,9,9))
            sheet.addMergedRegion(CellRangeAddress(filaInicial,filaFinal-1,10,10))
            sheet.addMergedRegion(CellRangeAddress(filaInicial,filaFinal-1,11,11))
        }
    }

    private fun escribirDatos(
        profundidadConGolpes: ProfundidadConGolpes,
        sheet: XSSFSheet,
    ) {
        val golpes = profundidadConGolpes.golpes.toList().orEmpty();
        var filaInicial: Int
        filaInicial =
            profuntidadANumeroDeFila(profundidadConGolpes.profundidad.profundidadInicial!!)

            for (i in 0..golpes.size - 1) {
                val filaInicialGolpe =
                    profuntidadANumeroDeFila(profundidadConGolpes.golpes[i].profundidad_inicial)
                if (golpes[i].numero_muestra != null) {
                    sheet.getRow(filaInicialGolpe).getCell(6).setCellValue(golpes[i].numero_muestra!!)
                }
                sheet.getRow(filaInicialGolpe).getCell(7).setCellValue(golpes[i].tipo)
                if (golpes[i].golpes1 != null) {
                    sheet.getRow(filaInicialGolpe).getCell(9)
                        .setCellValue(golpes[i].golpes1.toString())
                }
                if (golpes[i].golpes2 != null) {
                    sheet.getRow(filaInicialGolpe).getCell(10)
                        .setCellValue(golpes[i].golpes2.toString())
                }
                if (golpes[i].golpes3 != null) {

                    sheet.getRow(filaInicialGolpe).getCell(11)
                        .setCellValue(golpes[i].golpes3.toString())
                }
            }

        if (profundidadConGolpes.profundidad.sucs != Sucs.VACIO) {
            sheet.getRow(filaInicial).getCell(12)
                .setCellValue(profundidadConGolpes.profundidad.sucs.toString())
        }
        sheet.getRow(filaInicial).getCell(13)
            .setCellValue(profundidadConGolpes.profundidad.descripcion)
    }

    fun exportarSTP(
        context: Context,
        perforacion: PerforacionModel,
        profundidadConGolpes: List<ProfundidadConGolpes>
    ) {
        val nombreArchivo = "STP_${perforacion.numeroPerforacion}.xlsx"
        val archivo = copiarPlantillaPublica(context, nombreArchivo)
        completarExcel(archivo, perforacion, profundidadConGolpes)
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
    ) {
        val filaFinal = profuntidadANumeroDeFila(profundidad.profundidadFinal!!)-1
        var filaInicial = profuntidadANumeroDeFila(profundidad.profundidadInicial!!)-1
        val estilo = EstiloExcel()
        if (filaInicial == 0) {
            filaInicial = 24
        }
        if(filaInicial > 18){
            for(i in 6..38){
                if(sheet.getRow(filaFinal).getCell(i) == null){
                    sheet.getRow(filaFinal).createCell(i)
                }
                sheet.getRow(filaInicial).getCell(i).cellStyle = estilo.modificarEstioloColumnaNro(workbook,sheet,filaInicial,i)
            }
        }

//            //Estilos para la columna número y tipo
//            estilo.borderBottom = BorderStyle.DOTTED
//            estilo.borderLeft = BorderStyle.DASHED
//            estilo.borderRight = BorderStyle.DASHED
//            estilo.borderTop = BorderStyle.DOTTED
//            estilo.fillForegroundColor = IndexedColors.LEMON_CHIFFON.index
//            estilo.fillPattern = FillPatternType.SOLID_FOREGROUND
//            estilo.setBottomBorderColor(IndexedColors.GREY_40_PERCENT.index)
//            estilo.setTopBorderColor(IndexedColors.GREY_40_PERCENT.index)
//            sheet.addMergedRegion(CellRangeAddress(filaFinal - 4, filaFinal, 6, 6))
//            sheet.addMergedRegion(CellRangeAddress(filaFinal - 4, filaFinal, 7, 8))
//            //Columna 6
//            sheet.getRow(filaFinal).getCell(6).cellStyle = estilo;
//            sheet.getRow(filaFinal-4).getCell(6).cellStyle = estilo;
//            //Columna 7
//            sheet.getRow(filaFinal).getCell(7).cellStyle = estilo;
//            sheet.getRow(filaFinal-4).getCell(7).cellStyle = estilo;
//            estilo.borderRight = BorderStyle.THIN
//            sheet.getRow(filaFinal).getCell(8).cellStyle = estilo;
//            sheet.getRow(filaFinal-4).getCell(8).cellStyle = estilo;
//            for (i in 9..11) {
//                sheet.addMergedRegion(CellRangeAddress(filaFinal-4, filaFinal, i, i))
//            }
//            //Estilos Columna 9
//            estilo.borderLeft = BorderStyle.MEDIUM
//            estilo.borderRight = BorderStyle.DOTTED
//            sheet.getRow(filaFinal-4).getCell(9).cellStyle = estilo;
//            sheet.getRow(filaFinal).getCell(9).cellStyle = estilo;
//            //Estilos Columna 10
//            estilo.borderLeft = BorderStyle.DOTTED
//            estilo.borderRight = BorderStyle.DOTTED
//            sheet.getRow(filaFinal-4).getCell(10).cellStyle = estilo;
//            sheet.getRow(filaFinal).getCell(10).cellStyle = estilo;
//            //Estilos Columna 11
//            estilo.borderLeft = BorderStyle.DOTTED
//            estilo.borderRight = BorderStyle.MEDIUM
//            sheet.getRow(filaFinal-4).getCell(11).cellStyle = estilo;
//            sheet.getRow(filaFinal).getCell(11).cellStyle = estilo;
//        }
    }
    private fun darEstiloALasEntradas(workbook: XSSFWorkbook,sheet: XSSFSheet,profundidadConGolpes: ProfundidadConGolpes){
        val golpes = profundidadConGolpes.golpes
        var i:Int =0
        val estilos = EstiloExcel()
        for(golpe in golpes){
            var filaInicial = profuntidadANumeroDeFila(golpe.profundidad_inicial)
            var filaFinal = profuntidadANumeroDeFila(golpe.profundidad_final)
            sheet.getRow(filaInicial).getCell(6).cellStyle = estilos.estilarGolpes(
                workbook,sheet,filaInicial,6,i,golpes.size-1,
                filaFinal
            )
            sheet.getRow(filaInicial).getCell(7).cellStyle = estilos.estilarGolpes(
                workbook,sheet,filaInicial,7,i,golpes.size-1,
                filaFinal
            )
            sheet.getRow(filaInicial).getCell(8).cellStyle = estilos.estilarGolpes(
                workbook,sheet,filaInicial,8,i,golpes.size-1,
                filaFinal
            )
            sheet.getRow(filaInicial).getCell(9).cellStyle = estilos.estilarGolpes(
                workbook,sheet,filaInicial,9,i,golpes.size-1,
                filaFinal
            )
            sheet.getRow(filaInicial).getCell(10).cellStyle = estilos.estilarGolpes(
                workbook,sheet,filaInicial,10,i,golpes.size-1,
                filaFinal
            )
            sheet.getRow(filaInicial).getCell(11).cellStyle = estilos.estilarGolpes(
                workbook,sheet,filaInicial,11,i,golpes.size-1,
                filaFinal
            )
            i++
        }
    }
}