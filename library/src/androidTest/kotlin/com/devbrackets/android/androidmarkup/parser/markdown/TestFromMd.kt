package com.devbrackets.android.androidmarkup.parser.markdown

import android.support.test.runner.AndroidJUnit4
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TestFromMd {
    val parser = MarkdownParser()

    @Test
    fun noMarkdown() {
        val testString = "some non markdown text"

        val spanned = parser.toSpanned(testString)
        Assert.assertNotNull(spanned)
    }
}
