package com.devbrackets.android.androidmarkup.parser.markdown

import com.devbrackets.android.androidmarkup.parser.core.MarkupElement
import com.devbrackets.android.androidmarkup.parser.core.SpanType
import org.commonmark.node.*

/**
 * A Converter that will take the commonmark-java
 * node structure and convert it to a
 * [com.devbrackets.android.androidMarkup.parser.core.MarkupDocument]
 */
open class MarkdownDocumentConverter {

    open fun convert(node: Node) : MarkupElement {
        val converterVisitor = ConverterVisitor()
        node.accept(converterVisitor)

        return converterVisitor.rootElement
    }

    open class Builder {
        open fun build() : MarkdownDocumentConverter {
            return MarkdownDocumentConverter()
        }
    }

    open class ConverterVisitor : AbstractVisitor() {
        val rootElement = MarkupElement(null)
        var currentElement = rootElement

        override fun visit(blockQuote: BlockQuote) {
            super.visit(blockQuote)
        }

        override fun visit(code: Code) {
            super.visit(code)
        }

        override fun visit(fencedCodeBlock: FencedCodeBlock) {
            super.visit(fencedCodeBlock)
        }

        override fun visit(heading: Heading) {
            super.visit(heading)
        }

        override fun visit(thematicBreak: ThematicBreak) {
            super.visit(thematicBreak)
        }

        override fun visit(htmlInline: HtmlInline) {
            super.visit(htmlInline)
        }

        override fun visit(htmlBlock: HtmlBlock) {
            super.visit(htmlBlock)
        }

        override fun visit(image: Image) {
            super.visit(image)
        }

        override fun visit(indentedCodeBlock: IndentedCodeBlock) {
            super.visit(indentedCodeBlock)
        }

        override fun visit(paragraph: Paragraph) {
            super.visit(paragraph)

            var element = MarkupElement(currentElement)
            element.spanType = SpanType.TEXT
            currentElement.addChild(element)

            element.text = "\n"
        }

        override fun visit(orderedList: OrderedList) {
            val parent = currentElement

            currentElement = MarkupElement(currentElement)
            currentElement.spanType = SpanType.ORDERED_LIST
            currentElement.parent?.addChild(currentElement)

            visitChildren(orderedList)

            currentElement = parent
        }

        override fun visit(bulletList: BulletList) {
            val parent = currentElement

            currentElement = MarkupElement(currentElement)
            currentElement.spanType = SpanType.UNORDERED_LIST
            currentElement.parent?.addChild(currentElement)

            visitChildren(bulletList)

            currentElement = parent
        }

        override fun visit(listItem: ListItem) {
            super.visit(listItem)
        }

        override fun visit(emphasis: Emphasis) {
            val parent = currentElement

            currentElement = MarkupElement(currentElement)
            currentElement.spanType = SpanType.ITALIC
            currentElement.parent?.addChild(currentElement)

            visitChildren(emphasis)

            currentElement = parent
        }

        override fun visit(strongEmphasis: StrongEmphasis) {
            val parent = currentElement

            currentElement = MarkupElement(currentElement)
            currentElement.spanType = SpanType.BOLD
            currentElement.parent?.addChild(currentElement)

            visitChildren(strongEmphasis)

            currentElement = parent
        }

        override fun visit(text: Text) {
            var element = MarkupElement(currentElement)
            element.spanType = SpanType.TEXT
            currentElement.addChild(element)

            element.text = text.literal.orEmpty()
        }

        override fun visit(hardLineBreak: HardLineBreak) {
            var element = MarkupElement(currentElement)
            element.spanType = SpanType.TEXT
            currentElement.addChild(element)

            element.text = "\n"
        }
    }
}