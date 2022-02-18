package com.example.firebasetest

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class UserViewModel: ViewModel() {
    init {

    }

    val fAuth = Firebase.auth
}