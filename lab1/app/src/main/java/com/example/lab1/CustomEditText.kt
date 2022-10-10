//package com.example.lab1
//
//import android.content.Context
//import android.util.AttributeSet
//import android.view.ActionMode
//import android.view.Menu
//import android.view.MenuItem
//import android.widget.EditText
//
//
///**
// * This is a thin veneer over EditText, with copy/paste/spell-check removed.
// */
//class NoMenuEditText : EditText {
//    private final var context: Context
//
//    /** This is a replacement method for the base TextView class' method of the same name. This
//     * method is used in hidden class android.widget.Editor to determine whether the PASTE/REPLACE popup
//     * appears when triggered from the text insertion handle. Returning false forces this window
//     * to never appear.
//     * @return false
//     */
//    fun canPaste(): Boolean {
//        return false
//    }
//
//    /** This is a replacement method for the base TextView class' method of the same name. This method
//     * is used in hidden class android.widget.Editor to determine whether the PASTE/REPLACE popup
//     * appears when triggered from the text insertion handle. Returning false forces this window
//     * to never appear.
//     * @return false
//     */
//    override fun isSuggestionsEnabled(): Boolean {
//        return false
//    }
//
//    constructor(context: Context) : super(context) {
//        this.context = context
//        init()
//    }
//
//    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
//        this.context = context
//        init()
//    }
//
//    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
//        context,
//        attrs,
//        defStyle
//    ) {
//        this.context = context
//        init()
//    }
//
//    private fun init() {
//        this.customSelectionActionModeCallback = ActionModeCallbackInterceptor()
//        this.isLongClickable = false
//    }
//
//    /**
//     * Prevents the action bar (top horizontal bar with cut, copy, paste, etc.) from appearing
//     * by intercepting the callback that would cause it to be created, and returning false.
//     */
//    private inner class ActionModeCallbackInterceptor : ActionMode.Callback {
//        private val TAG = NoMenuEditText::class.java.simpleName
//        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
//            return false
//        }
//
//        override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
//            return false
//        }
//
//        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
//            return false
//        }
//
//        override fun onDestroyActionMode(mode: ActionMode) {}
//    }
//}
