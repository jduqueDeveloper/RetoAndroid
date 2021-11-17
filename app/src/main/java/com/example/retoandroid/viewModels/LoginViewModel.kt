package com.example.retoandroid.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.retoandroid.PASSWORD
import com.example.retoandroid.USERNAME
import com.example.retoandroid.USERS
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source

class LoginViewModel : ViewModel() {

    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _isConected = MutableLiveData<Boolean>()
    val isConected: LiveData<Boolean> get() = _isConected

    private val _user = MutableLiveData<String>()
    val user: LiveData<String> get() = _user

    fun login(user: String, passwordEncrypt: String) {
        val source = Source.CACHE
        firestore.collection(USERS)
            .whereEqualTo(USERNAME, user)
            .whereEqualTo(PASSWORD, passwordEncrypt)
            .get(source)
            .addOnSuccessListener { document ->
                _isConected.value = !document.isEmpty
                _user.value = user
            }
    }
}