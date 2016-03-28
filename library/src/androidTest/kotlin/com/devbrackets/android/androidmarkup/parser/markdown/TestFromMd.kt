package com.devbrackets.android.androidmarkup.parser.markdown

import android.graphics.Typeface
import android.support.test.runner.AndroidJUnit4
import android.text.style.StyleSpan
import com.devbrackets.android.androidmarkup.text.style.ListSpan
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TestFromMd {
    val parser = MarkdownParser()

    @Test
    fun noMarkdown() {
        val testString = "some non markdown text"

        var spanned = parser.toSpanned(testString)
        var spans = spanned.getSpans(0, spanned.length, Any::class.java)
        Assert.assertTrue(spans.size == 0)
    }

    @Test
    fun simpleItalic() {
        val italicString = "_some italic text_"

        var spanned = parser.toSpanned(italicString)
        var spans = spanned.getSpans(0, spanned.length, StyleSpan::class.java)
        Assert.assertTrue(spans.size > 0)
        Assert.assertTrue(spans[0].style == Typeface.ITALIC)
    }

    @Test
    fun simpleBold() {
        val boldString = "**some bold text**"

        var spanned = parser.toSpanned(boldString)
        var spans = spanned.getSpans(0, spanned.length, StyleSpan::class.java)
        Assert.assertTrue(spans.size > 0)
        Assert.assertTrue(spans[0].style == Typeface.BOLD)
    }

    @Test
    fun simpleOrderedList() {
        val orderedListString = "0. line one\n0. line two"

        var spanned = parser.toSpanned(orderedListString)
        var spans = spanned.getSpans(0, spanned.length, ListSpan::class.java)
        Assert.assertTrue(spans.size > 0)
        Assert.assertTrue(spans[0].type == ListSpan.Type.NUMERICAL)
    }

    @Test
    fun simpleUnOrderedList() {
        val unOrderedListString = "* line one\n* line two"

        var spanned = parser.toSpanned(unOrderedListString)
        var spans = spanned.getSpans(0, spanned.length, ListSpan::class.java)
        Assert.assertTrue(spans.size > 0)
        Assert.assertTrue(spans[0].type == ListSpan.Type.BULLET)
    }

    @Test
    fun multipleSpans() {
        val testString = "Some **Bold** and _Italic_ text"

        var spanned = parser.toSpanned(testString)
        Assert.assertEquals("Some Bold and Italic text", spanned.toString())

        var spans = spanned.getSpans(0, spanned.length, StyleSpan::class.java)
        Assert.assertTrue(spans.size == 2)

        //Verifies the Bold span
        spans = spanned.getSpans(5, 9, StyleSpan::class.java)
        Assert.assertEquals(spans.size, 1)
        Assert.assertEquals(spans[0].style, Typeface.BOLD)

        //Verifies the Italic span
        spans = spanned.getSpans(14, 20, StyleSpan::class.java)
        Assert.assertEquals(spans.size, 1)
        Assert.assertEquals(spans.get(0).style, Typeface.ITALIC)
    }
}
