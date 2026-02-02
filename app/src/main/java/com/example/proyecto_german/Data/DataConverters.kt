package com.example.proyecto_german.Data

import androidx.room.TypeConverter
import com.example.proyecto_german.Model.Sucs
import java.util.Date

class DataConverters {
    @TypeConverter
    fun fromTimestamp(value:Long?): Date?{
        return value?.let{Date(it)}
    }
    @TypeConverter
    fun dateToTimestamp(date:Date?):Long?{
        return date?.time
    }
    //Convierte un Sucs en un String
    @TypeConverter
    fun esSucs(dato: Sucs):String = dato.name
    //Convierte un string leido de la base datos en un Sucs (enumerativo)
    @TypeConverter
    fun aSucs(leeido:String):Sucs = Sucs.valueOf(leeido)
}