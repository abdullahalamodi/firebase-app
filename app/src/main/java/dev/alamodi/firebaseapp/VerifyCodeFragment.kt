package dev.alamodi.firebaseapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class VerifyCodeFragment : Fragment() {
    private lateinit var codeTv: TextView
    private lateinit var codeVerifyBtn: Button
    private lateinit var callbacks: Callbacks
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        callbacks = (context as Callbacks)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_verify_code, container, false)
        codeTv = view.findViewById(R.id.verify_code_tv)
        codeVerifyBtn = view.findViewById(R.id.submit_btn)

        codeVerifyBtn.setOnClickListener {
            if (codeTv.text.isNotBlank()) {
                val code = codeTv.text.toString()
                callbacks.onVerificationButtonClicked(code)
            }
        }
        return view;
    }

    companion object {
        @JvmStatic
        fun newInstance() = VerifyCodeFragment()
    }

    interface Callbacks {
        fun onVerificationButtonClicked(code: String)
    }
}