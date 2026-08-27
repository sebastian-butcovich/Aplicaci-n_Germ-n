package com.example.proyecto_german.Util

import android.app.AlertDialog
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.example.proyecto_german.R
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
fun EditText.actualizarProfundidadFinalEnBaseAlInicial(profundidadInicial: TextInputEditText, profundidadFinal: TextInputEditText){
    this.addTextChangedListener(object:TextWatcher{
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {
            if(profundidadInicial.hasFocus()){
                val valorA = profundidadInicial.text.toString();
                if(valorA.isNotEmpty() && valorA.toDoubleOrNull() != null && valorA.toDouble() < 30 && valorA.toDouble() > 0){
                    val nuevoValor = valorA.toDoubleOrNull()?.plus(R.string.paso.toDouble()) ?: (0.0)
                    profundidadFinal.setText(nuevoValor.toString())
                }else{
                    profundidadFinal.setText("")
                }
            }
        }
    })
}
fun mostrarDialogoError(title:String,mensaje:String,context: Context){
    AlertDialog.Builder(context)
        .setTitle(title)
        .setMessage(mensaje)
        .setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        .setCancelable(true)
        .create()
        .show()
}
fun Date?.aTextoFormato():String{
    if(this == null) return ""
    val formato = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
    return formato.format(this)
}