package com.piyush.thoughtflow.navigation

object Routes {
    const val HOME = "home"
    const val PROCESSING = "processing"
    const val EDITOR = "editor/{documentId}"
    const val HISTORY = "history"
    const val SETTINGS = "settings"

    fun editor(documentId: String) = "editor/$documentId"
}
