package com.example.lab2

interface RecyclerClickListener {
    fun onItemRemoveClick(position: Int)
    fun onItemEditClick(position: Int)
    fun onItemClick(position: Int)
}
interface RecyclerClickListenerPhase {
    fun onItemRemoveClick(position: Int)
    fun onItemEditClick(position: Int)
}
interface RecyclerClickListenerPhaseStart {
    fun onItemClick(position: Int)
}