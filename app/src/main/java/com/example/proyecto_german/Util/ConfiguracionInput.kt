package com.example.proyecto_german.Util

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

fun EditText.configurarLimitesMaximoDouble(maximoCaracteres: Int, valorMinimo: Number, valorMaximo:Number) {
   this.filters = arrayOf(android.text.InputFilter.LengthFilter(maximoCaracteres))
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(p0: Editable?) {
            val texto = p0.toString()
            if(texto.isEmpty()) return
            val valorA = p0.toString().toDoubleOrNull()
            val limiteMinimo = valorMinimo.toDouble()
            val limiteMaximo = valorMaximo.toDouble()
            if (valorA != null && valorA > limiteMaximo) {
                val mensajeLimite = if(limiteMaximo % 1 == 0.0) limiteMaximo.toInt() else limiteMaximo
                this@configurarLimitesMaximoDouble.error = "El valor máximo permitido es $mensajeLimite "
            }
            if(valorA != null && valorA < limiteMinimo){
                val mensajeLimite = if(limiteMinimo % 1 == 0.0) limiteMinimo.toInt() else limiteMaximo
                this@configurarLimitesMaximoDouble.error = "El valor mínimo permitido es $mensajeLimite "
            }
        }
    })
}