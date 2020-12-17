package uz.usoft.a24seven.ui.news

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.newsRecycler
import kotlinx.android.synthetic.main.fragment_selected_news.*
import uz.usoft.a24seven.R
import uz.usoft.a24seven.utils.SpacesItemDecoration

class SelectedNewsFragment : Fragment() {

    private lateinit var newsAdapter: NewsListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_selected_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newsAdapter= NewsListAdapter()
        otherNewsRecycler.layoutManager=
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
        otherNewsRecycler.adapter=newsAdapter
        otherNewsRecycler.addItemDecoration(SpacesItemDecoration(16,false))

        newsAdapter.onItemClick={
            findNavController().navigate(R.id.action_nav_selectedNews_self)
        }

    }

}