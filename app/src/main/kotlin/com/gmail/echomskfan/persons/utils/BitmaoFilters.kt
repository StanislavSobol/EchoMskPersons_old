package com.gmail.echomskfan.persons.utils

import android.graphics.*

internal object BitmapFilters {

    private val colorMatrixBinary: ColorMatrix
        get() {
            val colorMatrix = ColorMatrix()
            colorMatrix.setSaturation(0f)

            val m = 255f
            val t = -255 * 128f

            val threshold = ColorMatrix(floatArrayOf(m, 0f, 0f, 1f, t, 0f, m, 0f, 1f, t, 0f, 0f, m, 1f, t, 0f, 0f, 0f, 1f, 0f))
            colorMatrix.postConcat(threshold)

            return colorMatrix
        }

    private val colorMatrixInvert: ColorMatrix
        get() =
            ColorMatrix(floatArrayOf(-1f, 0f, 0f, 0f, 255f, 0f, -1f, 0f, 0f, 255f, 0f, 0f, -1f, 0f, 255f, 0f, 0f, 0f, 1f, 0f))

    private val colorMatrixAlphaBlue: ColorMatrix
        get() =
            ColorMatrix(floatArrayOf(0f, 0f, 0f, 0f, 0f, 0.3f, 0f, 0f, 0f, 50f, 0f, 0f, 0f, 0f, 255f, 0.2f, 0.4f, 0.4f, 0f, -30f))

    fun setFilter(original: Bitmap): Bitmap {
        val bitmap = Bitmap.createBitmap(original.width,
                original.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val paint = Paint()
        paint.colorFilter = ColorMatrixColorFilter(colorMatrixAlphaBlue)
        canvas.drawBitmap(original, 0f, 0f, paint)

        return bitmap
    }
}