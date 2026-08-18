package com.example.proyecto_german.Util

import com.example.proyecto_german.Model.GolpesStp
import com.example.proyecto_german.Model.Profundidad
import com.example.proyecto_german.Model.Temporales.ProfundidadConGolpes


 fun chequearGolpeConsistente(golpes: List<GolpesStp>): Boolean {
    val listAux = golpes;
    for (gol in golpes) {
        //La profundidad inicial del golpe nuevo es menor que la profundidad inicial del golpe
        //La profundida inicial
        for(golAux in listAux){
            if (gol.id != golAux.id) {
                if (golAux.profundidad_inicial >= gol.profundidad_inicial && golAux.profundidad_inicial <= gol.profundidad_final) {
                    return false
                }
                if (golAux.profundidad_final >= gol.profundidad_inicial && golAux.profundidad_final <= gol.profundidad_final) {
                    return false
                }
                if (golAux.profundidad_inicial >= gol.profundidad_inicial && golAux.profundidad_final <= gol.profundidad_final) {
                    return false;
                }
            }
        }

    }
    return true
}
 fun chequearConsistenciaProfundidad(listaProfundidad: List<Profundidad>): Boolean{
    if (!listaProfundidad.isEmpty()) {
        for (profundidad in listaProfundidad) {
            for (profundidadAux in listaProfundidad) {
                if (profundidad.profundidadFinal != null
                    && profundidad.profundidadInicial != null && profundidadAux.id != profundidad.id
                ) {
                    if(profundidadAux.profundidadInicial!! >= profundidad.profundidadInicial && profundidadAux.profundidadInicial <= profundidad.profundidadFinal){
                        return false
                    }
                    if(profundidadAux.profundidadFinal!! >= profundidad.profundidadInicial && profundidadAux.profundidadFinal <= profundidad.profundidadFinal){
                        return false
                    }
                    if(profundidadAux.profundidadInicial >= profundidad.profundidadInicial && profundidadAux.profundidadFinal <= profundidad.profundidadFinal){
                        return false;
                    }
                }
            }
        }
    }
    return true
}
 fun chequeoDatosEnGeneral(profundidadConGolpes:List<ProfundidadConGolpes>):Boolean{
    //Generar lista de profundidades
    val listaProfundidad = profundidadConGolpes.map {it.profundidad}
    //Chequear consistencia de profundidades
    if(!chequearConsistenciaProfundidad(listaProfundidad)){
        return false
    }
    //De cada profundidad tomo los golpes y chequeo que sean consistentes
    for(profundidadConGolpe in profundidadConGolpes){
        val listaGolpes = profundidadConGolpe.golpes
        if(!chequearGolpeConsistente(listaGolpes)){
            return false
        }
    }
    //Con esto creo que puedo determinar si los datos son correctos para generar el Excel.
    return true;
}