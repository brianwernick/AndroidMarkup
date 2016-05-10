package com.devbrackets.android.androidmarkup.text.style

import android.support.annotation.ColorInt
import android.text.TextPaint
import android.text.style.CharacterStyle
import android.text.style.UpdateAppearance

class ColorUnderlineSpan(@ColorInt var color: Int, var thickness: Float = 1.0f) : CharacterStyle(), UpdateAppearance {
    override fun updateDrawState(tp: TextPaint) {
        try {
            //NOTE: setUnderlineText is a public method, but is hidden in the SDK
            val method = TextPaint::class.java.getMethod("setUnderlineText", Integer.TYPE, java.lang.Float.TYPE)
            method.invoke(tp, color, thickness)
        } catch (e: Exception) {
            tp.isUnderlineText = true
        }
    }
}