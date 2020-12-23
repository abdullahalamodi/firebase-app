package dev.alamodi.firebaseapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.firebase.firestore.FirebaseFirestore

private const val ARG_IMAGE = "image"

class AddNewsDialog : DialogFragment() {
    private lateinit var titleEt: EditText
    private lateinit var detailsEt: EditText
    private lateinit var dateEt: EditText
    private lateinit var addBtn: Button
    private lateinit var db: FirebaseFirestore

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        db = FirebaseFirestore.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.add_news_dialog, container, false);
        titleEt = view?.findViewById(R.id.title_ev)!!
        detailsEt = view.findViewById(R.id.details_ev)
        dateEt = view.findViewById(R.id.date_ev)
        addBtn = view.findViewById(R.id.add_btn)

        addBtn.setOnClickListener {
            if (titleEt.text.isNotBlank() &&
                detailsEt.text.isNotBlank() &&
                dateEt.text.isNotBlank()
            ) {
                val title = titleEt.text.toString()
                val details = detailsEt.text.toString()
                val date = dateEt.text.toString()
                val news = News(title = title,details = details,date = date)
                addNews(news)
            }else{
                Toast.makeText(requireContext(), "some fields are blank", Toast.LENGTH_LONG).show()
            }
        }
        return view;
    }

    fun addNews(news: News) {
        // Add a new document with a generated ID
        db.collection("news")
            .add(news)
            .addOnSuccessListener {
                targetFragment?.let {
                    (it as Callbacks).onSuccessAddNews()
                }
                dismiss()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_LONG).show()
            }
    }


    companion object {
        fun newInstance() = AddNewsDialog()
    }


    interface Callbacks {
        fun onSuccessAddNews()
    }


}