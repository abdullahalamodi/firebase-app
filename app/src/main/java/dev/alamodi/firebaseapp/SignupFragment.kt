package dev.alamodi.firebaseapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class SignupFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var callBacks: CallBacks
    private lateinit var usernameView: TextView
    private lateinit var emailView: TextView
    private lateinit var password: TextView
    private lateinit var conPassword: TextView
    private lateinit var signupBtn: Button
    private lateinit var loginView: TextView
    private lateinit var googleBtn: Button
    private lateinit var phoneBtn: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        callBacks = (context as CallBacks)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_signup, container, false)
        usernameView = view.findViewById(R.id.username_tv)
        emailView = view.findViewById(R.id.email_tv)
        password = view.findViewById(R.id.passwoed_tv)
        conPassword = view.findViewById(R.id.con_passwoed_tv)
        signupBtn = view.findViewById(R.id.signup_btn)
        loginView = view.findViewById(R.id.signin_tv)
        googleBtn = view.findViewById(R.id.google_btn)
        phoneBtn = view.findViewById(R.id.phone_btn)


        signupBtn.setOnClickListener {
            if (checkFieldsIsNotEmpty()) {
                val password: String = password.text.toString()
                val conPassword: String = conPassword.text.toString()
                val username: String = usernameView.text.toString()
                val email: String = emailView.text.toString()
                if (password == conPassword) {
                    signup(username, email, password)
                } else {
                    Toast.makeText(
                        requireContext(), "password not comparable",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    requireContext(), "some fields empty",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        loginView.setOnClickListener {
            callBacks.onLoginViewClicked()
        }

        googleBtn.setOnClickListener {
            callBacks.onGoogleButtonClicked()
        }

        phoneBtn.setOnClickListener {
            callBacks.onPhoneButtonClicked()
        }

        return view;
    }

    private fun checkFieldsIsNotEmpty(): Boolean {
        if (usernameView.text.isNotBlank() &&
            emailView.text.isNotBlank() &&
            password.text.isNotBlank() &&
            conPassword.text.isNotBlank()
        )
            return true
        return false
    }

    private fun signup(username: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "createUserWithEmail:success")
                    val user = auth.currentUser
                    callBacks.onSuccessSignup()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        requireContext(), "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
//                    updateUI(null)
                }

                // ...
            }
    }

    companion object {
        @JvmStatic
        fun newInstance() = SignupFragment()
    }

    interface CallBacks {
        fun onSuccessSignup()
        fun onLoginViewClicked()
        fun onGoogleButtonClicked()
        fun onPhoneButtonClicked()
    }
}