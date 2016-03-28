package com.devbrackets.android.androidmarkup.parser.markdown

import android.graphics.Typeface
import android.support.test.runner.AndroidJUnit4
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import com.devbrackets.android.androidmarkup.text.style.ListSpan
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TestToMd {
    val parser = MarkdownParser()

    @Test
    fun noStyles() {
        val sourceString = "unStyled text"
        var spanned = SpannableStringBuilder(sourceString)

        var resultString = parser.fromSpanned(spanned)
        Assert.assertEquals(sourceString, resultString)
    }

    @Test
    fun simpleItalic() {
        val sourceString = "Italic text"
        var spanned = SpannableStringBuilder(sourceString)
        spanned.setSpan(StyleSpan(Typeface.ITALIC), 0, sourceString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        val resultString = parser.fromSpanned(spanned)
        Assert.assertEquals("_Italic text_", resultString)
    }

    @Test
    fun simpleBold() {
        val sourceString = "Bold text"
        var spanned = SpannableStringBuilder(sourceString)
        spanned.setSpan(StyleSpan(Typeface.BOLD), 0, sourceString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        val resultString = parser.fromSpanned(spanned)
        Assert.assertEquals( "**Bold text**", resultString)
    }

    @Test
    fun simpleOrderedList() {
        val sourceString = "line one\nline two"
        var spanned = SpannableStringBuilder(sourceString)
        spanned.setSpan(ListSpan(ListSpan.Type.NUMERICAL), 0, sourceString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        val resultString = parser.fromSpanned(spanned)
        Assert.assertEquals("", resultString) //todo
    }

    @Test
    fun simpleUnOrderedList() {
        val sourceString = "line one\nline two"
        var spanned = SpannableStringBuilder(sourceString)
        spanned.setSpan(ListSpan(ListSpan.Type.BULLET), 0, sourceString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        val resultString = parser.fromSpanned(spanned)
        Assert.assertEquals("", resultString) //todo
    }
}