package a24seven.uz.ui.seach

import a24seven.uz.MainActivity
import a24seven.uz.R
import a24seven.uz.data.PrefManager
import a24seven.uz.databinding.FragmentSearchBinding
import a24seven.uz.network.models.CartItem
import a24seven.uz.ui.category.selectedSubCategory.ProductPagingListAdapter
import a24seven.uz.ui.utils.BaseFragment
import a24seven.uz.utils.SpacesItemDecoration
import a24seven.uz.utils.navigate
import a24seven.uz.utils.toDp
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

class SearchFragment : BaseFragment<FragmentSearchBinding>(FragmentSearchBinding::inflate) {

    private lateinit var adapter: ProductPagingListAdapter
    private val viewModel: SearchViewModel by viewModel()

    private var updatePosition: Int = -1
    private var updateValue: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpAdapter()
    }

    private fun setUpAdapter() {
        adapter = ProductPagingListAdapter(requireContext())
        adapter.onItemClick = {
            val action = SearchFragmentDirections.actionNavSearchToNavSelectedProduct(
                selectedCategoryName = "",
                productId = it.id
            )
            navigate(action)
        }

        adapter.onFavClick = { product, position ->
            updatePosition = position
            updateValue = !product.is_favorite

            if (!product.is_favorite) {
                viewModel.addFav(product.id)
            } else {
                viewModel.removeFav(product.id)
            }
        }

        adapter.addToCart = {
            viewModel.addToCart(CartItem(it.id, 1))
        }
    }

    private fun search(query: String) {
        lifecycleScope.launch {
            viewModel.getSearchResponse(query).collect {
                adapter.submitData(it)
                return@collect
            }
        }
    }

    override fun setUpUI() {
        super.setUpUI()

        binding.searchQuery.requestFocus()
    }


    override fun setUpObservers() {
        viewModel.addToCartResponse.observe(
            viewLifecycleOwner
        ) { result ->
            result?.let {
                if (it.toInt() != -1) {
                    PrefManager.getInstance(requireContext()).edit().putBoolean(it.toString(), true)
                        .apply()
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }

    override fun setUpRecyclers() {
        binding.searchResultsRecycler.adapter = adapter
        binding.searchResultsRecycler.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.searchResultsRecycler.addItemDecoration(SpacesItemDecoration(toDp(16)))
    }


    override fun setUpOnClickListeners() {

        binding.searchQuery.doAfterTextChanged {
            if (it!!.isNotBlank())
                search(it.toString())
        }

        binding.speechRecognize.setOnClickListener {
            when {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.RECORD_AUDIO
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // You can use the API that requires the permission.
                    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                    intent.putExtra(
                        RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                    )
                    intent.putExtra(
                        RecognizerIntent.EXTRA_LANGUAGE,
                        Locale("ru").language
                    )
                    intent.putExtra(
                        RecognizerIntent.EXTRA_PROMPT,
                        getString(R.string.speak_to_text)
                    )
                    try {
                        MainActivity.openSpeechToText.launch(intent)
                    } catch (e: Exception) {
                        Toast
                            .makeText(
                                requireContext(), " " + e.message,
                                Toast.LENGTH_SHORT
                            )
                            .show()
                    }
                }

                else -> {
                    MainActivity.requestPermissionLauncher.launch(
                        Manifest.permission.RECORD_AUDIO
                    )
                }
            }
        }
    }


}