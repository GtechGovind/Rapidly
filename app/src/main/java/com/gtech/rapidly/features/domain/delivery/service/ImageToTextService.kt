package com.gtech.rapidly.features.domain.delivery.service

import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.gtech.rapidly.app.RapidlyApp
import com.gtech.rapidly.features.common.firestore.model.Restaurant
import com.gtech.rapidly.utils.error
import com.gtech.rapidly.utils.misc.RuntimeCache
import kotlinx.coroutines.tasks.await

object ImageToTextService {

    data class HotelBill(
        var dateTime: String? = null,
        var restaurant: Restaurant? = null,
        var billNumber: String? = null,
        var amount: Double? = null
    )

    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    val scanner = GmsDocumentScanning
        .getClient(
            GmsDocumentScannerOptions.Builder()
                .setScannerMode(GmsDocumentScannerOptions.SCANNER_MODE_BASE)
                .setPageLimit(1)
                .setGalleryImportAllowed(true)
                .setResultFormats(GmsDocumentScannerOptions.RESULT_FORMAT_JPEG)
                .build()
        )

    suspend fun processImage(imageUri: Uri): String {
        try {
            val result = recognizer.process(
                InputImage
                    .fromFilePath(
                        RapidlyApp.instance,
                        imageUri
                    )
            ).await()
            return result.text
        } catch (e: Exception) {
            error(e)
        }
        return ""
    }

    suspend fun parseHotelBill(result: String): HotelBill {

        val hotelBill = HotelBill()

        try {
            result.split("\n").forEach {
                if (
                    it.contains("Bill No") ||
                    it.contains("Bi11 No") ||
                    it.contains("B111 No")
                ) {
                    hotelBill.billNumber = it.split(":")[1].trim()
                    return@forEach
                }
            }
        } catch (_: Exception) {
        }

        val candidates = mutableListOf<Double>()
        result.split("\n").forEach {
            val tempAmount = it.toDoubleOrNull() ?: 0.0
            if (tempAmount > 0.0 && tempAmount < 30_000.0) {
                candidates.add(tempAmount)
            }
        }

        if (candidates.isNotEmpty()) {
            hotelBill.amount = candidates.maxOrNull()
        }

        try {
            RuntimeCache.getRestaurants().forEach {
                if (result.uppercase().contains(it.uniqueIdentifier.uppercase())) {
                    hotelBill.restaurant = it
                    return@forEach
                }
            }
        } catch (_: Exception) { }

        return hotelBill

    }

}