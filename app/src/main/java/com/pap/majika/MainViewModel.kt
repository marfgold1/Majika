package com.pap.majika

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private val _currentMenu = MutableLiveData<String>("twibbon")
    val currentMenu = _currentMenu
}