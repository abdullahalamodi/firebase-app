package dev.alamodi.firebaseapp

import android.os.Bundle
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

class LoginFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var callBacks: CallBacks
    private lateinit var emailView: TextView
    private lateinit var password: TextView
    private lateinit var loginBtn: Button
    private lateinit var singupView: TextView
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
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        emailView = view.findViewById(R.id.email_tv)
        password = view.findViewById(R.id.password_tv)
        loginBtn = view.findViewById(R.id.login_btn)
        singupView = view.findViewById(R.id.signup_tv)
        googleBtn = view.findViewById(R.id.google_btn)
        phoneBtn = view.findViewById(R.id.phone_btn)

        loginBtn.setOnClickListener {
            if (emailView.text.isNotBlank() && password.text.isNotBlank()) {
                val email: String = emailView.text.toString()
                val password: String = password.text.toString()
                signin(email, password)
            } else {
                Toast.makeText(
                    requireContext(), "some fields empty",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
        googleBtn.setOnClickListener {
            callBacks.onGoogleButtonClicked()
        }

        singupView.setOnClickListener {
            callBacks.onSignupViewClicked()
        }


        phoneBtn.setOnClickListener {
            callBacks.onPhoneButtonClicked()
        }
        return view;
    }

    fun signin(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    callBacks.onSuccessLogin()
                } else {
                    Toast.makeText(
                        requireContext(), "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }


    companion object {
        @JvmStatic
        fun newInstance() = LoginFragment()
    }


    interface CallBacks {
        fun onSuccessLogin()
        fun onSignupViewClicked()
        fun onGoogleButtonClicked()
        fun onPhoneButtonClicked()
    }
}