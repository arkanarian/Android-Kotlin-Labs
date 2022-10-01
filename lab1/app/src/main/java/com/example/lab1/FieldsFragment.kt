package com.example.lab1

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import kotlinx.android.synthetic.main.fragment_fields.*
import kotlinx.android.synthetic.main.fragment_fields.view.*
import kotlinx.android.synthetic.main.fragment_keyboard.*
import kotlinx.android.synthetic.main.main_activity.*


/**
 * A simple [Fragment] subclass.
 * Use the [FieldsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FieldsFragment : Fragment() {
    private lateinit var communicator: Communicator

    var choosedUnit1: Int = 1
    var choosedUnit2: Int = 1
    var choosedGeneralUnit: Int = 1
    var inputString: String = "0"
    var outputString: String = "0"
    var isSwiped: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_fields, container, false)

        communicator = activity as Communicator
//        restoreInput(savedInstanceState, view)
        Log.i("onCreateView-------->",inputString)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.i("onViewCreated-------->",inputString)
        super.onViewCreated(view, savedInstanceState)
        tvInput.movementMethod = ScrollingMovementMethod()
        tvInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(tvInput.text.length > 10)
                    tvInput.setTextSize(TypedValue.COMPLEX_UNIT_SP,24f)
                else
                    tvInput.setTextSize(TypedValue.COMPLEX_UNIT_SP,48f)
                Log.i("tvInputTextChanged------------>", isSwiped.toString())
                if(isSwiped == false){
                    if(tvInput.text.length > 0) {
                        Log.i("tvInputTextChanged------------>", "Calculated")
                        tvOutput.text = calculateResult()
                    }
                    else {
                        tvOutput.text = ""
                    }
                }
                else {
                    Log.i("tvInputTextChanged------------>", "notCalculated")
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        tvOutput.movementMethod = ScrollingMovementMethod()
        tvOutput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(tvOutput.text.length > 10)
                    tvOutput.setTextSize(TypedValue.COMPLEX_UNIT_SP,24f)
                else
                    tvOutput.setTextSize(TypedValue.COMPLEX_UNIT_SP,48f)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })


        btnCurrency.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                btnDistance.setChecked(false)
                btnWeight.setChecked(false)
                btnInput1.setChecked(true)
                btnOutput1.setChecked(true)
                btnInput1.text = "EUR"
//                btnInput1.textOn = "EUR"
//                btnInput1.textOff = "EUR"
                btnOutput1.text = "EUR"
//                btnOutput1.textOn = "EUR"
//                btnOutput1.textOff = "EUR"
                btnInput2.text = "USD"
//                btnInput2.textOn = "USD"
//                btnInput2.textOff = "USD"
                btnOutput2.text = "USD"
//                btnOutput2.textOn = "USD"
//                btnOutput2.textOff = "USD"
                btnInput3.text = "BYN"
//                btnInput3.textOn = "BYN"
//                btnInput3.textOff = "BYN"
                btnOutput3.text = "BYN"
//                btnOutput3.textOn = "BYN"
//                btnOutput3.textOff = "BYN"
            } else {
            }
        }
        btnCurrency.setOnClickListener {
            if (btnCurrency.isChecked){
                tvInput.text = "0"
                tvOutput.text = "0"
            }
        }
        btnDistance.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                btnCurrency.setChecked(false)
                btnWeight.setChecked(false)
                btnInput1.setChecked(true)
                btnOutput1.setChecked(true)
                btnInput1.text = "KM"
//                btnInput1.textOn = "KM"
//                btnInput1.textOff = "KM"
                btnOutput1.text = "KM"
//                btnOutput1.textOn = "KM"
//                btnOutput1.textOff = "KM"
                btnInput2.text = "M"
//                btnInput2.textOn = "M"
//                btnInput2.textOff = "M"
                btnOutput2.text = "M"
//                btnOutput2.textOn = "M"
//                btnOutput2.textOff = "M"
                btnInput3.text = "CM"
//                btnInput3.textOn = "CM"
//                btnInput3.textOff = "CM"
                btnOutput3.text = "CM"
//                btnOutput3.textOn = "CM"
//                btnOutput3.textOff = "CM"
            } else {
            }
        }
        btnDistance.setOnClickListener {
            if (btnDistance.isChecked){
                tvInput.text = "0"
                tvOutput.text = "0"
            }
        }
        btnWeight.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                btnCurrency.setChecked(false)
                btnDistance.setChecked(false)
                btnInput1.setChecked(true)
                btnOutput1.setChecked(true)
                btnInput1.text = "MG"
//                btnInput1.textOn = "MG"
//                btnInput1.textOff = "MG"
                btnOutput1.text = "MG"
//                btnOutput1.textOn = "MG"
//                btnOutput1.textOff = "MG"
                btnInput2.text = "G"
//                btnInput2.textOn = "G"
//                btnInput2.textOff = "G"
                btnOutput2.text = "G"
//                btnOutput2.textOn = "G"
//                btnOutput2.textOff = "G"
                btnInput3.text = "KG"
//                btnInput3.textOn = "KG"
//                btnInput3.textOff = "KG"
                btnOutput3.text = "KG"
//                btnOutput3.textOn = "KG"
//                btnOutput3.textOff = "KG"
            } else {
            }
        }
        btnWeight.setOnClickListener {
            if (btnWeight.isChecked){
                tvInput.text = "0"
                tvOutput.text = "0"
            }
        }
        // Toggle Buttons Units Input
        btnInput1.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                btnInput2.setChecked(false)
                btnInput3.setChecked(false)
                if(isSwiped == false){
                    equalsAction()
                }
            } else {
            }
        }
        btnInput2.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                btnInput1.setChecked(false)
                btnInput3.setChecked(false)
                if(isSwiped == false){
                    equalsAction()
                }
            } else {
            }
        }
        btnInput3.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                btnInput1.setChecked(false)
                btnInput2.setChecked(false)
                if(isSwiped == false){
                    equalsAction()
                }
            } else {
            }
        }
        // Toggle Buttons Units Output
        btnOutput1.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                btnOutput2.setChecked(false)
                btnOutput3.setChecked(false)
                if(isSwiped == false){
                    equalsAction()
                }
            } else {
            }
        }
        btnOutput2.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                btnOutput1.setChecked(false)
                btnOutput3.setChecked(false)
                if(isSwiped == false){
                    equalsAction()
                }
            } else {
            }
        }
        btnOutput3.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                btnOutput1.setChecked(false)
                btnOutput2.setChecked(false)
                if(isSwiped == false){
                    equalsAction()
                }
            } else {
            }
        }
        btnCopyInput.setOnClickListener {
            copyAction(btnCopyInput)
        }
    }

    private fun equalsAction(){
        tvOutput.text = calculateResult()
    }

    fun getDigit(digit: String){
        tvInput.append(digit)
    }
    fun isContainsDotFragment(): Boolean{
        return tvInput.text.toString().contains(".")
    }
    fun isOnlyZeroInput(): Boolean{
        Log.d("Tag", tvInput.text.toString())
        Log.d("Tag", (tvInput.text == "0").toString())
        return tvInput.text.toString() == "0"
    }
    fun replaceZeroDigit(digit: String){
        tvInput.text = digit
    }
    fun clearAllFields(){
        tvInput.text = "0"
        tvOutput.text = "0"
    }
    fun backspaceAction(){
        val length = tvInput.length()
        if(length > 0)
            tvInput.text = tvInput.text.subSequence(0,length-1)
    }
    private fun calculateResult(): String{
        var number = tvInput.text.toString()
        var firstUnit : Float = 0f
        var secondUnit : Float = 0f
        // -----
        var whichUnitInput : Int = 0
        if(btnInput1.isChecked) whichUnitInput = 1
        else if(btnInput2.isChecked) whichUnitInput = 2
        else if(btnInput3.isChecked) whichUnitInput = 3
        var whichUnitOutput : Int = 0
        if(btnOutput1.isChecked) whichUnitOutput = 1
        else if(btnOutput2.isChecked) whichUnitOutput = 2
        else if(btnOutput3.isChecked) whichUnitOutput = 3
        // -----
        if(btnCurrency.isChecked){
            val cur = Currency()
            when (whichUnitInput){
                1 -> firstUnit = cur.EUR
                2 -> firstUnit = cur.USD
                3 -> firstUnit = cur.BYN
                else -> firstUnit = 1f
            }
            when (whichUnitOutput){
                1 -> secondUnit = cur.EUR
                2 -> secondUnit = cur.USD
                3 -> secondUnit = cur.BYN
                else -> secondUnit = 1f
            }
        }
        else if(btnDistance.isChecked){
            val dst = Distance()
            when (whichUnitInput){
                1 -> firstUnit = dst.kilometers
                2 -> firstUnit = dst.meters
                3 -> firstUnit = dst.centimeters
                else -> firstUnit = 1f
            }
            when (whichUnitOutput){
                1 -> secondUnit = dst.kilometers
                2 -> secondUnit = dst.meters
                3 -> secondUnit = dst.centimeters
                else -> secondUnit = 1f
            }
        }
        else if(btnWeight.isChecked){
            val wgh = Weight()
            when (whichUnitInput){
                1 -> firstUnit = wgh.milligrams
                2 -> firstUnit = wgh.grams
                3 -> firstUnit = wgh.kilograms
                else -> firstUnit = 1f
            }
            when (whichUnitOutput){
                1 -> secondUnit = wgh.milligrams
                2 -> secondUnit = wgh.grams
                3 -> secondUnit = wgh.kilograms
                else -> secondUnit = 1f
            }
        }
        var output = Calculations.Calculate(number, firstUnit, secondUnit)
        return output
    }
    fun swipeAction(){
        var btnOutput : Int = 0
        var numInput = tvOutput.text.toString()
        Log.i("numInput", numInput)
        var numOutput = tvInput.text.toString()
        Log.i("numOutput", numOutput)
        isSwiped = true
        tvOutput.text = numOutput
        isSwiped = true
        tvInput.text = numInput
        if(btnInput1.isChecked) btnOutput = 1
        else if(btnInput2.isChecked) btnOutput = 2
        else if(btnInput3.isChecked) btnOutput = 3
        if(btnOutput1.isChecked) btnInput1.setChecked(true)
        else if(btnOutput2.isChecked) btnInput2.setChecked(true)
        else if(btnOutput3.isChecked) btnInput3.setChecked(true)
        if(btnOutput == 1) btnOutput1.setChecked(true)
        else if(btnOutput == 2) btnOutput2.setChecked(true)
        else if(btnOutput == 3) btnOutput3.setChecked(true)
        isSwiped = false
    }
    fun copyAction(view: View){
        communicator.copyAction(view)
    }
    fun getInputField(): String{
        return tvInput.text.toString()
    }
    fun getSelectedUnit(): Int{
        if(btnCurrency.isChecked) return 1
        else if(btnDistance.isChecked) return 2
        else if(btnWeight.isChecked) return 3
        return 0
    }
    fun getSelectedUnitInput(): Int{
        if(btnInput1.isChecked) return 1
        else if(btnInput2.isChecked) return 2
        else if(btnInput3.isChecked) return 3
        return 0
    }
    fun getSelectedUnitOutput(): Int{
        if(btnOutput1.isChecked) return 1
        else if(btnOutput2.isChecked) return 2
        else if(btnOutput3.isChecked) return 3
        return 0
    }
    fun setSelectedUnit(selected: Int) {
        if(selected == 1) btnCurrency.isChecked = true
        else if(selected == 2) btnDistance.isChecked = true
        else if(selected == 3) btnWeight.isChecked = true
    }
    fun setSelectedUnitInput(selected: Int) {
        if(selected == 1) btnInput1.isChecked = true
        else if(selected == 2) btnInput2.isChecked = true
        else if(selected == 3) btnInput3.isChecked = true
    }
    fun setSelectedUnitOutput(selected: Int) {
        if(selected == 1) btnOutput1.isChecked = true
        else if(selected == 2) btnOutput2.isChecked = true
        else if(selected == 3) btnOutput3.isChecked = true
    }
    fun setInputField(str: String){
        tvInput.text = str
    }
    fun getOutputField(): String{
        return tvOutput.text.toString()
    }



//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        outState.putString("inputString", tvInput.text.toString())
//        Log.i("onSaveInstanceState------->", tvInput.text.toString())
//    }
//
//    fun restoreInput(savedInstanceState: Bundle?, view: View) {
//        inputString = savedInstanceState?.getString("inputString") ?: ""
//        view.tvInput.text = inputString
//        Log.i("restoreInput------->", inputString)
//    }


}