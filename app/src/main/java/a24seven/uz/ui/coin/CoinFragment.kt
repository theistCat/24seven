package a24seven.uz.ui.coin

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import a24seven.uz.R
import a24seven.uz.databinding.FragmentCoinItemsBinding
import a24seven.uz.network.models.CoinProduct
import a24seven.uz.network.models.OrderCoinProduct
import a24seven.uz.ui.utils.BaseFragment
import a24seven.uz.utils.SpacesItemDecoration
import a24seven.uz.utils.observeEvent
import a24seven.uz.utils.showSnackbar
import a24seven.uz.utils.toDp

class CoinFragment : BaseFragment<FragmentCoinItemsBinding>(FragmentCoinItemsBinding::inflate) {


    private lateinit var adapter: CoinProductsListAdapter
    private val viewModel: CoinViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpAdapter()
    }

    override fun setUpUI() {
        super.setUpUI()
    }

    override fun <T : Any> onSuccess(data: T) {
        super.onSuccess(data)
        binding.empty.isVisible = true

        when (data) {
            is Int -> {
                binding.balance.text = getString(R.string.balance_, data as Int)
            }
            is OrderCoinProduct -> {
                showSnackbar("Сделка проведена успешно")
                viewModel.getCoins()
            }
            else -> {
                adapter.updateList(data as ArrayList<CoinProduct>)
            }
        }
    }

    override fun setUpObservers() {
        super.setUpObservers()
        observeEvent(viewModel.getCoinProductsResponse, ::handle)
        observeEvent(viewModel.getCoinResponse, ::handle)
        observeEvent(viewModel.getOrderCoinResponse, ::handle)
    }

    private fun setUpAdapter() {
        adapter = CoinProductsListAdapter(requireContext(), isGrid = true)

        viewModel.getCoinProducts()
        viewModel.getCoins()
        adapter.addToCart = {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("")
                .setMessage("вы точно хотите поменять ${it.price} монет на данный товар?")

                .setNegativeButton("Отменить") { dialog, which ->
                    // Respond to negative button press
                    dialog.dismiss()

                }
                .setPositiveButton("Обменять") { dialog, which ->
                    // Respond to positive button press
                    viewModel.orderCoins(it.id, 1)
                }
                .show()
        }
    }

    override fun setUpRecyclers() {
        binding.coinsProductsRecycler.adapter = adapter
        binding.coinsProductsRecycler.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.coinsProductsRecycler.addItemDecoration(SpacesItemDecoration(toDp(16)))
    }
}