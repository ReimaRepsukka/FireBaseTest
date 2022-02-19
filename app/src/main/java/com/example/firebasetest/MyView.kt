package com.example.firebasetest

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.google.firebase.auth.ktx.auth

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase
import io.grpc.BinaryLog

//This composable just testing in lessons
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
    var email = remember { mutableStateOf("") }
    var pw = remember { mutableStateOf("") }
    var info = remember { mutableStateOf("") }

    Column() {

        MyOutlineTextField(text = email, label = "Email", isPw = false)
        MyOutlineTextField(text = pw, label = "Password", isPw = true)

        OutlinedButton( onClick = {login(email.value, pw.value, info)}) {
            Text(text = "Login")
        }

        Text(text = info.value)
    }
}

/**
 * Signs the user in fire authentication and sets user email to info.
 */
fun login(email:String, pw:String, info: MutableState<String>){
    Firebase.auth
        .signInWithEmailAndPassword(email, pw)
        .addOnSuccessListener {
            info.value = "You are logged in with account ${it.user!!.email.toString()}"
        }
}

@Composable
fun Exercise5() {
    var sportName = remember { mutableStateOf("")}
    val sportlist = remember { mutableStateOf(listOf<String>()) }
    val dist = remember { mutableStateOf(0.0) }

    Column() {

        MyOutlineTextField(text = sportName, label = "Sport", isPw = false)

        OutlinedButton( onClick = {  getSports(sportlist, dist, sportName.value ) }){
            Text(text = "Get workouts")
        }

        //Text for each sport
        sportlist.value.forEach {  Text(text = it) }
        //Sum of distances
        Text(text = "Sum of distance is $dist")
    }
}

/**
 * This function gets the sports by name from the firestore
 * and calculates the sum of distances.
 */
fun getSports(
    sports: MutableState<List<String>>,
    distance: MutableState<Double>,
    sportName: String
){
    Firebase.firestore
        .collection("workout")
        .whereEqualTo("sport", sportName)
        .get()
        .addOnSuccessListener {
            var distSum = 0.0
            val fireSports = mutableListOf<String>()
            for(workout in it){
                distSum += workout.getDouble("distance") ?: 0.0
                fireSports.add( workout.get("sport").toString() )
            }

            distance.value = distSum
            sports.value = fireSports
        }
}


@Composable
fun Exercise4() {
    var name = remember { mutableStateOf("") }
    val msg = remember { mutableStateOf("") }

    Column() {

        MyOutlineTextField(text = name, label =  "Name", isPw = false)

        OutlinedButton( onClick = { getBlogMessage(name.value, msg) }){
            Text(text = "Get message")
        }
        
        Text(text = msg.value)
    }
}

/**
 * Gets blog message from firestore
 */
fun getBlogMessage(name: String, msg: MutableState<String>) {
    Firebase.firestore
        .collection("blogs")
        .document(name)
        .get()
        .addOnSuccessListener {
            msg.value = it.get("message").toString()
        }
}

@Composable
fun Exercise() {

    var name = remember { mutableStateOf("") }
    var msg = remember { mutableStateOf("") }

    //Here we use Blog-object to send the message String to Firestore
    //Another field contains the name used as id for the document.
    Column() {
        MyOutlineTextField(text = name, label = "Name" , isPw = false )
        MyOutlineTextField(text = msg, label = "Message" , isPw = false)

        Button(onClick = { sendBlogMsg(name.value, msg.value) }) {
            Text(text = "Send")
        }
    }
}

/**
 * Sends blog message to firestore
 */
fun sendBlogMsg(name:String, msg:String){
    Firebase.firestore
        .collection("blogs")
        .document(name)
        .set(Blog(msg))
}

/**
 * Common reusable textfield for all composables
 */
@Composable
fun MyOutlineTextField(text: MutableState<String>, label: String, isPw: Boolean) {
    OutlinedTextField(
        value = text.value,
        onValueChange = { text.value = it },
        label = { Text(label) },
        visualTransformation =
            if(isPw)
                PasswordVisualTransformation()
            else
                VisualTransformation.None
    )
}