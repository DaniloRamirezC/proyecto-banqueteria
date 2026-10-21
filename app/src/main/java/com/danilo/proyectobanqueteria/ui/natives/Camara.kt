package com.danilo.proyectobanqueteria.ui.natives

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

fun crearFoto(context: Context): Uri {
    val archivo = File.createTempFile("comprobante_",".jpg",context.cacheDir)
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        archivo
    )
}