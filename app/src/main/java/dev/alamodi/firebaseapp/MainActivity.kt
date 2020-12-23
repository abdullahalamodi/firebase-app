package dev.alamodi.firebaseapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.lang.Exception

class MainActivity : AppCompatActivity(),
    LoginFragment.CallBacks, SignupFragment.CallBacks, PhoneNumFragment.Callbacks {
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = Firebase.auth
        val isFragmentContainerEmpty = savedInstanceState == null
        if (isFragmentContainerEmpty) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragmentContainer, LoginFragment.newInstance())
                .commit()
        }

        // [START config_signin]
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()
        // [END config_signin]

        googleSignInClient = GoogleSignIn.getClient(this, gso)

    }


    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e)
                Toast.makeText(this,task.exception.toString(),Toast.LENGTH_LONG).show()
                // [START_EXCLUDE]
//                updateUI(null)
                // [END_EXCLUDE]
            }
        }else{
            try{
                Toast.makeText(this,resultCode.toString(),Toast.LENGTH_LONG).show()

            }catch (e:Exception){
                Toast.makeText(this,"cast error",Toast.LENGTH_LONG).show()
            }
        }
    }

    //on google btn click
    private fun signInByGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    // [START auth_with_google]
    private fun firebaseAuthWithGoogle(idToken: String) {
        // [START_EXCLUDE silent]
        // [END_EXCLUDE]
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "signInWithCredential:success")
                    val user = auth.currentUser
                    replaceFragment(MainFragment.newInstance())
//                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "signInWithCredential:failure", task.exception)
                    // [START_EXCLUDE]
                    // [END_EXCLUDE]
//                    updateUI(null)
                    Toast.makeText(this,task.exception.toString(), Toast.LENGTH_LONG).show()
                }

                // [END_EXCLUDE]
            }
    }


    override fun onSuccessLogin() {
        replaceFragment(MainFragment.newInstance())
    }

    override fun onSignupViewClicked() {
        replaceFragment(SignupFragment.newInstance())
    }

    override fun onSuccessSignup() {
        replaceFragment(MainFragment.newInstance())
    }

    override fun onLoginViewClicked() {
        replaceFragment(LoginFragment.newInstance())
    }

    override fun onGoogleButtonClicked() {
        signInByGoogle()
    }

    override fun onPhoneButtonClicked() {
        replaceFragment(PhoneNumFragment.newInstance())
    }

    override fun onSuccessSendCode() {
        replaceFragment(VerifyCodeFragment.newInstance())
    }

    override fun onSuccessVerifyPhoneNum() {
        replaceFragment(MainFragment.newInstance())
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }
}