package uz.usoft.a24seven.ui.news

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_home.*
import uz.usoft.a24seven.R
import uz.usoft.a24seven.utils.SpacesItemDecoration

class NewsFragment : Fragment() {

    private lateinit var newsAdapter: NewsListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newsAdapter= NewsListAdapter(true)
        newsRecycler.layoutManager=
            GridLayoutManager(requireContext(), 2)
        newsRecycler.adapter=newsAdapter
        newsRecycler.addItemDecoration(SpacesItemDecoration(16,true,2))

        newsAdapter.onItemClick={
            findNavController().navigate(R.id.action_nav_news_to_selectedNewsFragment)
        }
    }
}