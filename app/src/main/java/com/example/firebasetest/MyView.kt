package com.example.firebasetest

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.grpc.BinaryLog

@Composable
fun MainView() {
    var text by remember { mutableStateOf("") }
    val fireStore = Firebase.firestore
    val user = User("Lisa", "Damon")

    fireStore
        .collection("users")
        .add("test")
}

@Composable
fun Exercise() {

    var text by remember { mutableStateOf("") }
    var text2 by remember { mutableStateOf("") }

    val fireStore = Firebase.firestore


    //Here we use Blog-object to send the message String to Firestore
    //Another field contains the name used as id for the document.
    Column() {
        OutlinedTextField(value = text, onValueChange = {text = it})
        OutlinedTextField(value = text2, onValueChange = {text2 = it})
        Button(onClick = {
            fireStore
                .collection("blogs")
                .document(text)
                .set( Blog(text2) )
        }) {
            Text(text = "Send")
        }
    }

}