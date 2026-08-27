package com.piyush.thoughtflow.ai

import com.piyush.thoughtflow.domain.model.FormattedDocument

interface DocumentFormatter {
    val id: String
    suspend fun isAvailable(): Boolean
    suspend fun format(text: String): FormattedDocument
}

interface FormatterStrategy : DocumentFormatter
