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
import uz.usoft.a24seven.databinding.FragmentSelectedNewsBinding
import uz.usoft.a24seven.utils.SpacesItemDecoration

class SelectedNewsFragment : Fragment() {

    private var _binding: FragmentSelectedNewsBinding? = null
    private val binding get() = _binding!!
    private lateinit var newsAdapter: NewsListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
        setUpAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSelectedNewsBinding.inflate(inflater, container, false)
        setUpRecyclerView()
        return binding.root
    }

    private fun setUpAdapter() {
        newsAdapter = NewsListAdapter(requireContext())
        newsAdapter.onItemClick = {
            findNavController().navigate(R.id.action_nav_selectedNews_self)
        }
    }

    private fun setUpRecyclerView() {
        binding.otherNewsRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.otherNewsRecycler.adapter = newsAdapter
        binding.otherNewsRecycler.addItemDecoration(SpacesItemDecoration(16, false))
    }
}