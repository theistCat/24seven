package uz.usoft.a24seven.ui.profile.paymentMethod

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import uz.usoft.a24seven.databinding.FragmentMyPaymentMethodBinding
import uz.usoft.a24seven.utils.SpacesItemDecoration

class MyPaymentMethodFragment : Fragment() {

    private var _binding: FragmentMyPaymentMethodBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: PaymentMethodListAdapter
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
        // Inflate the layout for this fragment
        _binding = FragmentMyPaymentMethodBinding.inflate(inflater, container, false)
        setUpRecycler()
        setUpClickListener()
        return binding.root
    }

    private fun setUpAdapter() {
        adapter = PaymentMethodListAdapter()


    }

    private fun setUpRecycler() {
        binding.paymentMethodRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.paymentMethodRecycler.adapter = adapter
        binding.paymentMethodRecycler.addItemDecoration(
            SpacesItemDecoration(
                (resources.displayMetrics.density * 16 + 0.5f).toInt(),
                true,
                1
            )
        )

    }

    private fun setUpClickListener() {
        binding.savePaymentMethod.setOnClickListener {

        }
    }

}

