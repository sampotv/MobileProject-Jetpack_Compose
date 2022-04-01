package com.example.mobiiliprojektir9

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser



class Register(password: String, email: String): ComponentActivity() {
    val password = password
    val email = email
    var user: FirebaseUser? = null
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent() {
            register(this, auth, password, email)
        }
    }
}

fun register(context: ComponentActivity, auth: FirebaseAuth, password: String, email: String){
    val auth = auth
    Log.d("auth", "create user")
    auth.createUserWithEmailAndPassword(
        email.trim(),
        password.trim()
    ).addOnCompleteListener(context){ task ->
        if (task.isSuccessful){
            Log.d("AUTH", "Success!")
        }else{
            Log.d("AUTH", "Failed: ${task.exception}")
        }
    }
}
//    private fun signIn() {
//
//        val providers = arrayListOf(
//            AuthUI.IdpConfig.EmailBuilder().build()
//        )
//        val signInIntent = AuthUI.getInstance()
//            .createSignInIntentBuilder()
//            .setAvailableProviders(providers)
//            .build()
//        signInLauncher.launch(signInIntent)
//    }
//
//    private val signInLauncher = registerForActivityResult (
//        FirebaseAuthUIActivityResultContract()
//    ){
//            res -> signInResult(res)
//    }
//
//    private fun signInResult(result: FirebaseAuthUIAuthenticationResult){
//        val response = result.idpResponse
//        if(result.resultCode == RESULT_OK){
//            user = FirebaseAuth.getInstance().currentUser
//        } else{
//            Log.e("RegisterDriver.kt", "Error logging in " + response?.error?.errorCode)
//        }
//    }
