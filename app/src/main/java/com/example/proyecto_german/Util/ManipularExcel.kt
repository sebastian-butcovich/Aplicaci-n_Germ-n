package com.example.proyecto_german.Util

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.example.proyecto_german.Model.PerforacionModel
import com.example.proyecto_german.Model.Profundidad
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class ManipularExcel {
    fun copiarPlantillaPublica(context: Context, nombreArchivo: String): File {
        val carpeta = File(context.getExternalFilesDir(null), "SPT")
        if(!carpeta.exists()){
            carpeta.mkdirs()
        }
        val archivoDestino = File(carpeta,nombreArchivo)
        context.assets.open("Formato_registro_perforacion_base.xlsx")
            .use { input ->
                archivoDestino.outputStream().use { outPut ->
                    input.copyTo(outPut)
                }
            }
        return archivoDestino
    }
    fun completarExcel(
        archivo:File,
        perforacion: PerforacionModel,
        profundidades:List<Profundidad>
    ){
        val workbook = XSSFWorkbook(FileInputStream(archivo))
        val sheet = workbook.getSheetAt(0);

        //Datos generales
        sheet.getRow(6).getCell(7).setCellValue(perforacion.cliente)
        sheet.getRow(6).getCell(34).setCellValue(perforacion.fecha)
        sheet.getRow(7).getCell(7).setCellValue(perforacion.atencion)
        sheet.getRow(7).getCell(34).setCellValue(perforacion.numeroPerforacion.toString())
        sheet.getRow(8).getCell(7).setCellValue(perforacion.proyecto)
        sheet.getRow(9).getCell(7).setCellValue(perforacion.localizacion)
        sheet.getRow(9).getCell(34).setCellValue(perforacion.profundidadMetros)
        FileOutputStream(archivo).use { output->
            workbook.write(output)
        }
        workbook.close()
    }
    fun exportarSTP(
        context:Context,
        perforacion: PerforacionModel,
        profundidades:List<Profundidad>
    ){
        val nombreArchivo = "STP_${perforacion.numeroPerforacion}.xlsx"
        val archivo = copiarPlantillaPublica(context,nombreArchivo)
        completarExcel(archivo,perforacion,profundidades)
        compartirArchivo(context,archivo)
    }
    fun compartirArchivo(context: Context, archivo:File){
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            archivo
        )
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            putExtra(Intent.EXTRA_STREAM,uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        val chooser = Intent.createChooser(intent,"Compartir archivo SPT")
        context.startActivity(chooser)
    }

}