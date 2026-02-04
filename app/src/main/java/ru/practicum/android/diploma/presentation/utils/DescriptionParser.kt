package ru.practicum.android.diploma.presentation.utils

class DescriptionParser(
    private val headingDictionary: HeadingDictionary
) {
    private val bulletPrefixes = listOf("•", "-", "—", "*")

    fun parseDescription(raw: String): List<DescriptionBlock> {
        val text = raw.trim()
        if (text.isBlank()) return emptyList()

        val lines = text.lines()
            .map { it.trim() }
            .filter { it.isNotBlank() }

        val blocks = mutableListOf<DescriptionBlock>()
        val bulletBuffer = mutableListOf<String>()
        val paragraphBuffer = mutableListOf<String>()

        var listMode = false

        fun flushParagraph() {
            if (paragraphBuffer.isNotEmpty()) {
                blocks += DescriptionBlock.Paragraph(paragraphBuffer.joinToString("\n"))
                paragraphBuffer.clear()
            }
        }

        fun flushBullets() {
            if (bulletBuffer.isNotEmpty()) {
                blocks += DescriptionBlock.Bullets(bulletBuffer.toList())
                bulletBuffer.clear()
            }
        }

        fun bulletItemOrNull(line: String): String? {
            val prefix = bulletPrefixes.firstOrNull { line.startsWith(it) } ?: return null
            return line.removePrefix(prefix).trim().takeIf { it.isNotBlank() }
        }

        fun headingKey(line: String): String =
            line.removeSuffix(":").trim().lowercase()

        fun isHeading(line: String): Boolean {
            if (line.endsWith(":")) return true
            return headingDictionary.resolve(headingKey(line)) != null
        }

        fun normalizeHeading(rawHeading: String): String {
            val clean = rawHeading.removeSuffix(":").trim()
            val resolved = headingDictionary.resolve(clean.lowercase())
            return resolved ?: clean.replaceFirstChar { ch ->
                if (ch.isLowerCase()) ch.titlecase() else ch.toString()
            }
        }

        fun looksLikePlainListItem(line: String): Boolean {
            if (line.endsWith(":")) return false
            if (line.isBlank()) return false
            if (line.contains(".")) return false
            if (line.length > 220) return false
            return true
        }

        fun looksLikeListItem(line: String): Boolean {
            if (line.endsWith(":")) return false
            if (line.length > 140) return false
            if (line.contains(".")) return false
            if (line.isBlank()) return false

            val startsLikeRequirement = listOf("Опыт", "Умение", "Знание", "Навыки", "Понимание")
                .any { line.startsWith(it) }

            val shortNoPunct = line.length <= 60 && !line.contains(",") && !line.contains(";")

            return startsLikeRequirement || shortNoPunct
        }

        for (line in lines) {
            val bullet = bulletItemOrNull(line)

            when {
                bullet != null -> {
                    flushParagraph()
                    bulletBuffer += bullet
                    listMode = true
                }

                isHeading(line) -> {
                    flushParagraph()
                    flushBullets()
                    blocks += DescriptionBlock.Heading(normalizeHeading(line))
                    listMode = true
                }

                listMode && looksLikePlainListItem(line) -> {
                    flushParagraph()
                    bulletBuffer += line
                }

                looksLikeListItem(line) -> {
                    flushParagraph()
                    bulletBuffer += line
                    listMode = true
                }

                else -> {
                    flushBullets()
                    paragraphBuffer += line
                    listMode = false
                }
            }
        }

        flushBullets()
        flushParagraph()

        return blocks
    }
}
