package com.example.firebasetest

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.google.firebase.auth.ktx.auth

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase
import io.grpc.BinaryLog

@Composable
fun MainView() {
    var text by remember { mutableStateOf("") }

    val fAuth = Firebase.auth
    val fireStore = Firebase.firestore

    fAuth
        .signInWithEmailAndPassword("reima@reima.com", "reimarii")
        .addOnSuccessListener {
            fireStore
                .collection("blogs")
                .document(it.user!!.uid)   //only the authorizer user has access
                .set( Blog("My personal message") )
        }


}


@Composable
fun Exercise7() {
    var email by remember { mutableStateOf("") }
    var pw by remember { mutableStateOf("") }
    var info by remember { mutableStateOf("") }
    val fAuth = Firebase.auth


    Column() {

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") })

        OutlinedTextField(
            value = pw,
            onValueChange = { pw = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation())

        OutlinedButton(
            onClick = {
                fAuth
                    .signInWithEmailAndPassword(email,pw)
                    .addOnSuccessListener {
                        info = "You are logged in with account ${it.user!!.email.toString()}"
                    }
            }
        ) {
            Text(text = "Login")
        }

        Text(text = info)
    }

}

@Composable
fun Test() {

}

@Composable
fun Exercise5() {
    var sport_name by remember { mutableStateOf("") }
    var sportlist by remember { mutableStateOf(mutableListOf<String>()) }
    var dist by remember { mutableStateOf(0.0) }

    val fireStore = Firebase.firestore

    Column() {

        OutlinedTextField(
            value = sport_name,
            onValueChange = { sport_name = it },
            label = { Text("Sport") })

        OutlinedButton(
            onClick = {
                fireStore
                    .collection("workout")
                    .whereEqualTo("sport", sport_name)
                    .get()
                    .addOnSuccessListener {
                        var distance = 0.0
                        val sports = mutableListOf<String>()
                        for(workout in it){
                            distance += workout.getDouble("distance") ?: 0.0
                            sports.add( workout.get("sport").toString() )
                        }

                        dist =  distance
                        sportlist = sports

                    }
            }
        ) {
            Text(text = "Get workouts")
        }

        sportlist.forEach { 
            Text(text = it)
        }
        Text(text = "Sum of distance is $dist")

    }
}


@Composable
fun Exercise4() {
    var text by remember { mutableStateOf("") }
    var msg by remember { mutableStateOf("") }

    val fireStore = Firebase.firestore

    Column() {
        
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Name") })
        
        OutlinedButton(
            onClick = {
                fireStore
                    .collection("blogs")
                    .document(text)
                    .get()
                    .addOnSuccessListener {
                        msg = it.get("message").toString()
                    }
            }
        ) {
            Text(text = "Get message")
        }
        
        Text(text = msg)
    }
}

@Composable
fun Exercise() {

    var text by remember { mutableStateOf("") }
    var text2 by remember { mutableStateOf("") }

    val fireStore = Firebase.firestore


    //Here we use Blog-object to send the message String to Firestore
    //Another field contains the name used as id for the document.
    Column() {
        OutlinedTextField(value = text, onValueChange = { text = it })
        OutlinedTextField(value = text2, onValueChange = { text2 = it })
        Button(onClick = {
            fireStore
                .collection("blogs")
                .document(text)
                .set(Blog(text2))
        }) {
            Text(text = "Send")
        }
    }

}