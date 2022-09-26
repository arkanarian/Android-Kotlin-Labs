package com.example.lab1

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.ScrollingMovementMethod
import android.util.TypedValue
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_fields.*
import kotlinx.android.synthetic.main.main_activity.*


class MainActivity : AppCompatActivity(), Communicator {

    // val const = 0
    // var variable = 9
    // lateinit var tv : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val fragmentKeyboard = KeyboardFragment()
        val fragmentFields = FieldsFragment()

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerKeyboard, fragmentKeyboard)
            .replace(R.id.fragmentContainerFields, fragmentFields)
            .commit()

//

    }

//    override fun sendDigit(digit: Int) {
//        // Get FieldsFragment
//        val fieldsFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainerFields) as FieldsFragment
//        //calling the updateText method of the FragmentB
//        fieldsFragment.getDigit(digit)
//    }

//    override fun passDataCom(editTextInput: String) {
//        val bundle = Bundle()
//        bundle.putString("message", editTextInput)
//
//        val transaction = this.supportFragmentManager.beginTransaction()
//        val fragmentFields = FieldsFragment()
//
//        fragmentFields.arguments = bundle
//        transaction.replace(R.id.fragmentContainerFields, fragmentFields)
//        transaction.commit()
//    }

    override fun passDataCom(digit: String) {
        val callingFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainerFields) as FieldsFragment
        callingFragment.getDigit(digit)
    }

    override fun isContainsDot(): Boolean {
        val callingFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainerFields) as FieldsFragment
        return callingFragment.isContainsDotFragment()
    }

    override fun isOnlyZeroInput(): Boolean {
        val callingFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainerFields) as FieldsFragment
        return callingFragment.isOnlyZeroInput()
    }

    override fun replaceZeroDigit(digit: String) {
        val callingFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainerFields) as FieldsFragment
        return callingFragment.replaceZeroDigit(digit)
    }

    override fun clearAllFields() {
        val callingFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainerFields) as FieldsFragment
        return callingFragment.clearAllFields()
    }

    override fun backspaceAction() {
        val callingFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainerFields) as FieldsFragment
        return callingFragment.backspaceAction()
    }

    override fun swipeAction() {
        val callingFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainerFields) as FieldsFragment
        return callingFragment.swipeAction()
    }

    override fun copyAction(view: View){
        val callingFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainerFields) as FieldsFragment
        val s = resources.getResourceEntryName(view.getId())
        var textToCopy = ""
        if(s == "btnCopyOuput") textToCopy = callingFragment.getOutputField()
        else if(s == "btnCopyInput") textToCopy = callingFragment.getInputField()
        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("text", textToCopy)
        clipboardManager.setPrimaryClip(clipData)
        showToast("Copyed!")
    }
    private fun showToast(message:String){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }
}

