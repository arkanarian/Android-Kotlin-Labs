package com.example.lab1

import android.content.ClipData
import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_fields.*
import kotlinx.android.synthetic.main.main_activity.*


class MainActivity : AppCompatActivity(), Communicator {

    private var fragmentKeyboard: Fragment = KeyboardFragment()
    private var fragmentFields: Fragment = FieldsFragment()
    var choosedUnitInput: Int = 1
    var choosedUnitOutput: Int = 1
    var choosedGeneralUnit: Int = 1
    var inputString: String = "0"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerKeyboard, fragmentKeyboard)
            .replace(R.id.fragmentContainerFields, fragmentFields)
            .commit()
    }

    override fun passDataCom(digit: String) {
        Log.d("passData------>", "hello")
        val callingFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainerFields) as FieldsFragment
        if(digit == "." && callingFragment.getInputLength()==0){
            showToast("You can't add dot without specifying number")
            return
        }
        callingFragment.getDigit(digit)
    }

    override fun isContainsDot(): Boolean {
        val callingFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainerFields) as FieldsFragment
        return callingFragment.isContainsDotFragment()
    }

    override fun isContainsDotAdd(): Boolean {
        val callingFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainerFields) as FieldsFragment
        if(callingFragment.isContainsDotFragment()){
            showToast("Field contains a dot")
        }
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
    override fun pasteAction(view: View){
        val callingFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainerFields) as FieldsFragment
        val s = resources.getResourceEntryName(view.getId())
        var textToPaste = ""
//        if(s == "btnCopyOuput") textToCopy = callingFragment.getOutputField()
//        else if(s == "btnCopyInput") textToCopy = callingFragment.getInputField()
        val clipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        if (clipboardManager.hasPrimaryClip()) {
            val clip: ClipData? = clipboardManager.getPrimaryClip()
            if (clip?.description?.hasMimeType(MIMETYPE_TEXT_PLAIN) == true) {
                textToPaste = clip?.getItemAt(0)?.coerceToText(this).toString()
            }
        }
        Log.i("PastedText----->", textToPaste)

        if(textToPaste.contains(".") && callingFragment.isContainsDotFragment()) {
            showToast("Unable to paste because of dot")
            return
        }
        try {
            textToPaste.toDouble()
        } catch (e: NumberFormatException) {
            showToast("Unable to paste because it is not a number")
            return
        }
        if (!TextUtils.isEmpty(textToPaste)) callingFragment.pasteAction(textToPaste)

//        val clipData = ClipData.newPlainText("text", textToCopy)
//        clipboardManager.setPrimaryClip(clipData)
        showToast("Pasted!")
    }

    private fun showToast(message:String){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val callingFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainerFields) as FieldsFragment
        inputString = callingFragment.getInputField()
        choosedGeneralUnit = callingFragment.getSelectedUnit()
        choosedUnitInput = callingFragment.getSelectedUnitInput()
        choosedUnitOutput = callingFragment.getSelectedUnitOutput()
        Log.i("onSaveInstanceStateActivity--------->",inputString)
        Log.i("onSaveInstanceStateActivity--------->",choosedGeneralUnit.toString())
        outState.putString("inputString", inputString)
        outState.putInt("choosedGeneralUnit", choosedGeneralUnit)
        outState.putInt("choosedUnitInput", choosedUnitInput)
        outState.putInt("choosedUnitOutput", choosedUnitOutput)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        inputString = savedInstanceState.getString("inputString").toString()
        choosedGeneralUnit = savedInstanceState.getInt("choosedGeneralUnit")
        choosedUnitInput = savedInstanceState.getInt("choosedUnitInput")
        choosedUnitOutput = savedInstanceState.getInt("choosedUnitOutput")
        Log.i("onRestoreInstanceState--------->",inputString)
        Log.i("onRestoreInstanceState--------->",choosedGeneralUnit.toString())
        val callingFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainerFields) as FieldsFragment
        callingFragment.setInputField(inputString)
        callingFragment.setSelectedUnit(choosedGeneralUnit)
        callingFragment.setSelectedUnitInput(choosedUnitInput)
        callingFragment.setSelectedUnitOutput(choosedUnitOutput)
    }
}

