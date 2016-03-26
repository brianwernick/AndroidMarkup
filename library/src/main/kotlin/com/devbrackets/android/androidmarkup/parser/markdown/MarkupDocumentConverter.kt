package com.devbrackets.android.androidmarkup.parser.markdown

import com.devbrackets.android.androidmarkup.parser.core.MarkupElement
import org.commonmark.node.*

/**
 * A Converter that will take the commonmark-java
 * node structure and convert it to a
 * [com.devbrackets.android.androidMarkup.parser.core.MarkupDocument]
 */
open class MarkupDocumentConverter {

    open fun convert(node: Node) : MarkupElement {
        val converterVisitor = ConverterVisitor()
        node.accept(converterVisitor)

        return converterVisitor.markupElement
    }

    open class Builder {
        open fun build() : MarkupDocumentConverter {
            return MarkupDocumentConverter()
        }
    }

    //TODO: actually use the visitor to convert
    open class ConverterVisitor : AbstractVisitor() {
        var markupElement = MarkupElement(null)

        override fun visit(blockQuote: BlockQuote?) {
            super.visit(blockQuote)
        }

        override fun visit(bulletList: BulletList?) {
            super.visit(bulletList)
        }

        override fun visit(code: Code?) {
            super.visit(code)
        }

        override fun visit(emphasis: Emphasis?) {
            super.visit(emphasis)
        }

        override fun visit(fencedCodeBlock: FencedCodeBlock?) {
            super.visit(fencedCodeBlock)
        }

        override fun visit(hardLineBreak: HardLineBreak?) {
            super.visit(hardLineBreak)
        }

        override fun visit(heading: Heading?) {
            super.visit(heading)
        }

        override fun visit(thematicBreak: ThematicBreak?) {
            super.visit(thematicBreak)
        }

        override fun visit(htmlInline: HtmlInline?) {
            super.visit(htmlInline)
        }

        override fun visit(htmlBlock: HtmlBlock?) {
            super.visit(htmlBlock)
        }

        override fun visit(image: Image?) {
            super.visit(image)
        }

        override fun visit(indentedCodeBlock: IndentedCodeBlock?) {
            super.visit(indentedCodeBlock)
        }

        override fun visit(link: Link?) {
            super.visit(link)
        }

        override fun visit(listItem: ListItem?) {
            super.visit(listItem)
        }

        override fun visit(orderedList: OrderedList?) {
            super.visit(orderedList)
        }

        override fun visit(paragraph: Paragraph?) {
            super.visit(paragraph)
        }

        override fun visit(softLineBreak: SoftLineBreak?) {
            super.visit(softLineBreak)
        }

        override fun visit(strongEmphasis: StrongEmphasis?) {
            super.visit(strongEmphasis)
        }

        override fun visit(text: Text?) {
            super.visit(text)
        }
    }
}