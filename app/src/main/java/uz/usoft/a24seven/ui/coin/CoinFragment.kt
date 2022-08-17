package uz.usoft.a24seven.ui.coin

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.android.ext.android.bind
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.usoft.a24seven.R
import uz.usoft.a24seven.databinding.FragmentCoinItemsBinding
import uz.usoft.a24seven.network.models.CoinProduct
import uz.usoft.a24seven.network.models.OrderCoinProduct
import uz.usoft.a24seven.ui.home.ProductsListAdapter
import uz.usoft.a24seven.ui.products.NewProductsFragmentDirections
import uz.usoft.a24seven.ui.products.ProductViewModel
import uz.usoft.a24seven.ui.utils.BaseFragment
import uz.usoft.a24seven.utils.*

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