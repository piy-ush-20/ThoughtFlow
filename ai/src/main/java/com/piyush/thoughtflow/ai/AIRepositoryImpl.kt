package com.piyush.thoughtflow.ai

import com.piyush.thoughtflow.domain.model.FormattedDocument
import com.piyush.thoughtflow.domain.repository.AIRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AIRepositoryImpl @Inject constructor(
    private val formatter: AdaptiveDocumentFormatter,
) : AIRepository {
    override suspend fun format(text: String): FormattedDocument = formatter.format(text)
}
