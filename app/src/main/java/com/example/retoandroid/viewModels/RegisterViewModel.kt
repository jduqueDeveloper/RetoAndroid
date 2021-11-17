package com.example.retoandroid.viewModels

import androidx.lifecycle.ViewModel
import com.example.retoandroid.models.User
import com.example.retoandroid.USERS
import com.google.firebase.firestore.FirebaseFirestore

class RegisterViewModel : ViewModel() {

        private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun postUserFireBase(user: User) {
        firestore.collection(USERS).add(user)
    }

}