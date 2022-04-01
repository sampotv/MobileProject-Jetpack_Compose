package com.example.mobiiliprojektir9

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable

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
           register(this, password, email)
        }
    }
    fun register(context: ComponentActivity, password: String, email: String){
        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        Log.d("auth", "create user")
        auth.createUserWithEmailAndPassword(
            email.trim(),
            password.trim()
        ).addOnCompleteListener(context){ task ->
            if (task.isSuccessful){
                Log.d("AUTH", "Success!")
            }else{
                Log.d("AUTH", "Failed: ${task.exception}")
                Toast.makeText(context, "${task.exception}", Toast.LENGTH_SHORT).show()
            }
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
