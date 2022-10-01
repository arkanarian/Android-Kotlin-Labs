package com.example.lab1

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.ScrollingMovementMethod
import android.util.Log
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

    private var fragmentKeyboard: Fragment = KeyboardFragment()
    private var fragmentFields: Fragment = FieldsFragment()
    var choosedUnitInput: Int = 1
    var choosedUnitOutput: Int = 1
    var choosedGeneralUnit: Int = 1
    var inputString: String = "0"
    var outputString: String = "0"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        Log.i("onCreate--------->", "Activity")
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerKeyboard, fragmentKeyboard)
            .replace(R.id.fragmentContainerFields, fragmentFields)
            .commit()
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

//    override fun onConfigurationChanged(newConfig: Configuration) {
//        super.onConfigurationChanged(newConfig)
//        setContentView(R.layout.main_activity)
//
//        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
//            val fragmentKeyboard = KeyboardFragment()
//            val fragmentFields = FieldsFragment()
//
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.fragmentContainerKeyboard, fragmentKeyboard)
//                .replace(R.id.fragmentContainerFields, fragmentFields)
//                .commit()
//
//        }
//    }
}

