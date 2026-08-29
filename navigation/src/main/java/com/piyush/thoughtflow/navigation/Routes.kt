package com.piyush.thoughtflow.navigation

object Routes {
    const val HOME = "home"
    const val DOCUMENTS = "documents"
    const val CREATE = "create"
    const val TEMPLATES = "templates"
    const val PROFILE = "profile"
    const val VOICE = "voice"
    const val PROCESSING = "processing"
    const val EDITOR = "editor/{documentId}"
    const val STORE = "store"
    const val SETTINGS = "settings"
    /** @deprecated Prefer [DOCUMENTS] */
    const val HISTORY = DOCUMENTS

    fun editor(documentId: String) = "editor/$documentId"
}
