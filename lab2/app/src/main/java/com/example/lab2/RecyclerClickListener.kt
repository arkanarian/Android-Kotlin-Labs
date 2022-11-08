package com.example.lab2

interface RecyclerClickListener {
    fun onItemRemoveClick(position: Int)
    fun onItemEditClick(position: Int)
    fun onItemClick(position: Int)
}
interface RecyclerClickListenerPhase {
    fun onItemRemoveClick(position: Int)
    fun afterTextChangedName(position: Int, s: String)
    fun afterTextChangedDuration(position: Int, s: String)
    fun afterTextChangedDurationRest(position: Int, s: String)
    fun afterTextChangedRepetitions(position: Int, s: String)
}