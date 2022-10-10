package com.example.lab1

import android.view.View

interface Communicator {
    fun passDataCom(digit: String)
    fun isContainsDot(): Boolean
    fun isContainsDotAdd(): Boolean
    fun replaceZeroDigit(digit: String)
    fun clearAllFields()
    fun backspaceAction()
    fun swipeAction()
    fun copyAction(view: View)
    fun isOnlyZeroInput(): Boolean
    fun pasteAction(view: View)
}