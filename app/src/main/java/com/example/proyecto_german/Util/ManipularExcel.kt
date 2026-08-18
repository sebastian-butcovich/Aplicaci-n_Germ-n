package com.example.proyecto_german.Util

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.example.proyecto_german.Model.Perforacion
import com.example.proyecto_german.Model.Profundidad
//import com.example.proyecto_german.Model.Sucs
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
        perforacion: Perforacion,
        profundidadConGolpes: List<ProfundidadConGolpes>,
        context: Context
    ):Boolean {
        var seFormateo = false;
        var seEscribio = false;
        var seDioEstilo = false;
        if (!archivo.exists()) {
            mostrarDialogoError("Archvio no encontrado", "No se encontró el archivo de plantilla",context);
            return false
        }else if(chequeoDatosEnGeneral(profundidadConGolpes)){
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
                 seFormateo = formatearCeldas(workbook, sheet, profundidadConGolpe,context)
                 seEscribio = escribirDatos(profundidadConGolpe, sheet)
                 seDioEstilo = darEstiloALasEntradas(workbook, sheet, profundidadConGolpe)
            }
            if(seFormateo && seEscribio && seDioEstilo){
                FileOutputStream(archivo).use { output ->
                    workbook.write(output)
                }
            }else{
                return false
            }
            workbook.close()
        }else{
            mostrarDialogoError("Error en los datos","Los datos no son consistentes",context)
            return false
        }
        return true
    }

    private fun formatearCeldas(
        workbook: XSSFWorkbook,
        sheet: XSSFSheet,
        profundidadConGolpes: ProfundidadConGolpes,
        context: Context
    ):Boolean {
        //Obtener fila inicial fila final
        //Si no tiene golpes simplemente formatear sucs y descripción
        //Se puede dar el caso de que no tenga golpes
        var filaInicial = profuntidadANumeroDeFila(profundidadConGolpes.profundidad.profundidadInicial!!)-1
        var filaFinal = profuntidadANumeroDeFila(profundidadConGolpes.profundidad.profundidadFinal!!)
        val estilo = EstiloExcel()
        //Juntar celdas de Sucs y Descripcion
        //Socs
        try {
            sheet.addMergedRegion(CellRangeAddress(filaInicial, filaFinal, 12, 12))
            //Descripcion
            sheet.addMergedRegion(CellRangeAddress(filaInicial, filaFinal, 13, 15))
            //Aplicar estilos a las columnas
            val golpes = profundidadConGolpes.golpes
            for (golpe in golpes) {
                filaInicial = profuntidadANumeroDeFila(golpe.profundidad_inicial)
                filaFinal = profuntidadANumeroDeFila(golpe.profundidad_final)
                sheet.addMergedRegion(CellRangeAddress(filaInicial, filaFinal - 1, 6, 6))
                sheet.addMergedRegion(CellRangeAddress(filaInicial, filaFinal - 1, 7, 8))
                sheet.addMergedRegion(CellRangeAddress(filaInicial, filaFinal - 1, 9, 9))
                sheet.addMergedRegion(CellRangeAddress(filaInicial, filaFinal - 1, 10, 10))
                sheet.addMergedRegion(CellRangeAddress(filaInicial, filaFinal - 1, 11, 11))
            }
            return true;
        }catch(e:IllegalArgumentException){
            mostrarDialogoError("Error en los datos","Los datos no son consistentes",context)
            return false
        }
    }

    private fun escribirDatos(
        profundidadConGolpes: ProfundidadConGolpes,
        sheet: XSSFSheet,
    ):Boolean {
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

        if (profundidadConGolpes.profundidad.sucs.equals("VACIO")) {
            sheet.getRow(filaInicial).getCell(12)
                .setCellValue(profundidadConGolpes.profundidad.sucs.toString())
        }
        sheet.getRow(filaInicial).getCell(13)
            .setCellValue(profundidadConGolpes.profundidad.descripcion)
        return true
    }

    fun exportarSTP(
        context: Context,
        perforacion: Perforacion,
        profundidadConGolpes: List<ProfundidadConGolpes>
    ) {
        val nombreArchivo = "STP_${perforacion.numeroPerforacion}.xlsx"
        val archivo = copiarPlantillaPublica(context, nombreArchivo)
        val completo = completarExcel(archivo, perforacion, profundidadConGolpes, context)
        if(completo){
            compartirArchivo(context, archivo)
        }
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
        val filaFinal = profuntidadANumeroDeFila(profundidad.profundidadFinal!!)
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

    }
    private fun darEstiloALasEntradas(
        workbook: XSSFWorkbook,
        sheet: XSSFSheet,
        profundidadConGolpes: ProfundidadConGolpes):Boolean{
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
        return true
    }
}