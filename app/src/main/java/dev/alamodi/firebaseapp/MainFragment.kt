package dev.alamodi.firebaseapp

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore


class MainFragment : Fragment(),AddNewsDialog.Callbacks{

    lateinit var newsListViewModel: NewsListViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ContentLoadingProgressBar
    private lateinit var adapter: NewsAdapter
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = FirebaseFirestore.getInstance()
        newsListViewModel =
            ViewModelProviders.of(this).get(NewsListViewModel::class.java)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        recyclerView = view.findViewById(R.id.news_recycler_view)
//        progressBar = view.findViewById(R.id.progress_circular)
        recyclerView.layoutManager = LinearLayoutManager(context)
        return view;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getNews()
    }

    private fun updateUI(newsList: List<News>) {
//        progressBar.hide();
        adapter = NewsAdapter(newsList)
        recyclerView.adapter = adapter
        Toast.makeText(requireContext(),newsList[0].title,Toast.LENGTH_LONG).show()
    }

    // Create a new user with a first and last name


    fun getNews() {
        db.collection("news")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                        val newsList = mutableListOf<News>();
                    for (news in task.result!!) {
                        Log.d("TAG", news.id + " => " + news.data)
                        newsList.add(News.newsFromJson(news.id,news.data))
                    }
                        updateUI(newsList);
                } else {
                    Log.w("TAG", "Error getting documents.", task.exception)
                }
            }
    }

    companion object {
        fun newInstance() = MainFragment();
    }

    //menu inflate
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.news_menu, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_news -> {
                AddNewsDialog.newInstance().apply {
                    setTargetFragment(this@MainFragment, 0)
                    show(this@MainFragment.requireFragmentManager(), "addNews")
                }
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
    private class NewsHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        private val newsTitle = itemView.findViewById(R.id.news_title) as TextView
        private val newsDes = itemView.findViewById(R.id.news_des) as TextView
        private val newsDate = itemView.findViewById(R.id.news_date) as TextView

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(news: News) {
            newsTitle.text = news.title;
            newsDes.text = news.details;
            newsDate.text = news.date
        }

        override fun onClick(p0: View?) {}
    }

    private class NewsAdapter(private val newsList: List<News>) :
        RecyclerView.Adapter<NewsHolder>() {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): NewsHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.news_item_list, parent, false)
            return NewsHolder(view)
        }

        override fun getItemCount(): Int = newsList.size

        override fun onBindViewHolder(holder: NewsHolder, position: Int) {
            val news = newsList[position]
            holder.bind(news)
        }

    }

    override fun onSuccessAddNews() {
        getNews()
    }
}