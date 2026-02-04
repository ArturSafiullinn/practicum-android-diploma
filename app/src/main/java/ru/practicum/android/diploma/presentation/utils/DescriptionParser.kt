package ru.practicum.android.diploma.presentation.utils

class DescriptionParser(
    private val headingDictionary: HeadingDictionary
) {

    private companion object {
        private const val COLON = ":"

        private const val MAX_PLAIN_LIST_ITEM_LENGTH = 220
        private const val MAX_LIST_ITEM_LENGTH = 140
        private const val SHORT_NO_PUNCT_LENGTH = 60

        private val BULLET_PREFIXES = listOf("•", "-", "—", "*")
        private val REQUIREMENT_PREFIXES = listOf("Опыт", "Умение", "Знание", "Навыки", "Понимание")
    }

    fun parseDescription(raw: String): List<DescriptionBlock> {
        val lines = raw.normalizedNonEmptyLines()
        if (lines.isEmpty()) return emptyList()

        val buffers = Buffers()
        val blocks = mutableListOf<DescriptionBlock>()

        for (line in lines) {
            val bullet = line.bulletItemOrNull()

            when {
                bullet != null -> {
                    buffers.flushParagraphTo(blocks)
                    buffers.addBullet(bullet)
                    buffers.enableListMode()
                }

                isHeading(line) -> {
                    buffers.flushParagraphTo(blocks)
                    buffers.flushBulletsTo(blocks)
                    blocks += DescriptionBlock.Heading(normalizeHeading(line))
                    buffers.enableListMode()
                }

                buffers.listMode && line.looksLikePlainListItem() -> {
                    buffers.flushParagraphTo(blocks)
                    buffers.addBullet(line)
                }

                line.looksLikeListItem() -> {
                    buffers.flushParagraphTo(blocks)
                    buffers.addBullet(line)
                    buffers.enableListMode()
                }

                else -> {
                    buffers.flushBulletsTo(blocks)
                    buffers.addParagraphLine(line)
                    buffers.disableListMode()
                }
            }
        }

        buffers.flushBulletsTo(blocks)
        buffers.flushParagraphTo(blocks)

        return blocks
    }

    private fun String.normalizedNonEmptyLines(): List<String> =
        trim()
            .takeIf { it.isNotBlank() }
            ?.lines()
            ?.map { it.trim() }
            ?.filter { it.isNotBlank() }
            .orEmpty()

    private fun String.bulletItemOrNull(): String? {
        val prefix = BULLET_PREFIXES.firstOrNull { startsWith(it) }
        return prefix
            ?.let { removePrefix(it).trim() }
            ?.takeIf { it.isNotBlank() }
    }

    private fun isHeading(line: String): Boolean {
        val normalized = line.removeSuffix(COLON).trim().lowercase()
        val byDictionary = headingDictionary.resolve(normalized) != null
        val byColon = line.endsWith(COLON)
        return byColon || byDictionary
    }

    private fun normalizeHeading(rawHeading: String): String {
        val clean = rawHeading.removeSuffix(COLON).trim()
        val resolved = headingDictionary.resolve(clean.lowercase())
        return resolved ?: clean.withFirstLetterUppercase()
    }

    private fun String.withFirstLetterUppercase(): String =
        replaceFirstChar { ch -> if (ch.isLowerCase()) ch.titlecase() else ch.toString() }

    private fun String.looksLikePlainListItem(): Boolean {
        val valid = !endsWith(COLON) &&
            isNotBlank() &&
            !contains(".") &&
            length <= MAX_PLAIN_LIST_ITEM_LENGTH
        return valid
    }

    private fun String.looksLikeListItem(): Boolean {
        val baseChecksOk = !endsWith(COLON) &&
            isNotBlank() &&
            !contains(".") &&
            length <= MAX_LIST_ITEM_LENGTH

        val startsLikeRequirement = REQUIREMENT_PREFIXES.any { startsWith(it) }

        val shortNoPunct = length <= SHORT_NO_PUNCT_LENGTH &&
            !contains(",") &&
            !contains(";")

        return baseChecksOk && (startsLikeRequirement || shortNoPunct)
    }

    private class Buffers {
        private val bulletBuffer = mutableListOf<String>()
        private val paragraphBuffer = mutableListOf<String>()

        var listMode: Boolean = false
            private set

        fun enableListMode() {
            listMode = true
        }

        fun disableListMode() {
            listMode = false
        }

        fun addBullet(text: String) {
            bulletBuffer += text
        }

        fun addParagraphLine(text: String) {
            paragraphBuffer += text
        }

        fun flushBulletsTo(out: MutableList<DescriptionBlock>) {
            if (bulletBuffer.isNotEmpty()) {
                out += DescriptionBlock.Bullets(bulletBuffer.toList())
                bulletBuffer.clear()
            }
        }

        fun flushParagraphTo(out: MutableList<DescriptionBlock>) {
            if (paragraphBuffer.isNotEmpty()) {
                out += DescriptionBlock.Paragraph(paragraphBuffer.joinToString("\n"))
                paragraphBuffer.clear()
            }
        }
    }
}
