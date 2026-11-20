package com.danilo.proyectobanqueteria.ui.natives

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

fun crearFoto(context: Context): Uri {
    val archivo = File.createTempFile("comprobante_",".jpg",context.cacheDir)
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        archivo
    )
}

fun guardarComprobante(context: Context, uri: Uri): String? {
    return try {
        val carpeta = File(context.filesDir,"comprobantes")
        if(!carpeta.exists()) carpeta.mkdirs()

        val archivoDestino = File(carpeta, "comp_${System.currentTimeMillis()}.jpg")
        context.contentResolver.openInputStream(uri)?.use { input ->
            FileOutputStream(archivoDestino).use { output ->
                input.copyTo(output)
            }
        }
        archivoDestino.absolutePath
    }catch(e: Exception){
        e.printStackTrace()
        null
    }
}