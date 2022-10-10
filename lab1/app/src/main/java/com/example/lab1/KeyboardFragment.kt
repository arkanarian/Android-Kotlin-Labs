package com.example.lab1

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import kotlinx.android.synthetic.main.fragment_fields.*
import kotlinx.android.synthetic.main.fragment_keyboard.*
import kotlinx.android.synthetic.main.fragment_keyboard.view.*

class KeyboardFragment : Fragment() {
    private lateinit var communicator: Communicator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_keyboard, container, false)
        communicator = activity as Communicator
        view.btnDigit1.setOnClickListener {
            NumberAction(view.btnDigit1)
        }
        view.btnDigit1.setOnClickListener {
            NumberAction(view.btnDigit1)
        }
        view.btnDigit2.setOnClickListener {
            NumberAction(view.btnDigit2)
        }
        view.btnDigit3.setOnClickListener {
            NumberAction(view.btnDigit3)
        }
        view.btnDigit4.setOnClickListener {
            NumberAction(view.btnDigit4)
        }
        view.btnDigit5.setOnClickListener {
            NumberAction(view.btnDigit5)
        }
        view.btnDigit6.setOnClickListener {
            NumberAction(view.btnDigit6)
        }
        view.btnDigit7.setOnClickListener {
            NumberAction(view.btnDigit7)
        }
        view.btnDigit8.setOnClickListener {
            NumberAction(view.btnDigit8)
        }
        view.btnDigit9.setOnClickListener {
            NumberAction(view.btnDigit9)
        }
        view.btnDigit0.setOnClickListener {
            NumberAction(view.btnDigit0)
        }
        view.btnDot.setOnClickListener {
            NumberAction(view.btnDot)
        }
        view.btnClearAll.setOnClickListener {
            allClearAction(view.btnClearAll)
        }
        view.btnBackspace.setOnClickListener {
            backspaceAction(view.btnBackspace)
        }
        view.btnSwipe.setOnClickListener {
            swipeAction(view.btnSwipe)
        }
        return view
    }
    fun NumberAction(view: View){
        if(view is Button)
        {
            if(view.text == "."){
                if(!communicator.isContainsDotAdd())
                    communicator.passDataCom(view.text.toString())
            }
            else if(communicator.isOnlyZeroInput()){
                communicator.replaceZeroDigit(view.text.toString())
            }
            else
                communicator.passDataCom(view.text.toString())
        }
    }
    fun allClearAction(view: View){
        communicator.clearAllFields()
    }
    fun backspaceAction(view: View){
        communicator.backspaceAction()
    }
    fun swipeAction(view: View){
        communicator.swipeAction()
    }

}