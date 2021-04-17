package uz.usoft.a24seven.ui.seach

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.usoft.a24seven.MainActivity
import uz.usoft.a24seven.R
import uz.usoft.a24seven.databinding.ActivityAuthBinding
import uz.usoft.a24seven.databinding.ActivitySearchBinding
import uz.usoft.a24seven.network.utils.NoConnectivityException
import uz.usoft.a24seven.network.utils.Resource
import uz.usoft.a24seven.ui.category.selectedSubCategory.ProductPagingListAdapter
import uz.usoft.a24seven.ui.category.selectedSubCategory.SelectedSubCategoryFragmentDirections
import uz.usoft.a24seven.utils.*

class SearchActivity : AppCompatActivity()  {

    private lateinit var adapter: ProductPagingListAdapter
    private lateinit var binding: ActivitySearchBinding
    private val viewModel: SearchViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_backicon)



        adapter = ProductPagingListAdapter(this)
        adapter.onItemClick = {
            val  returnIntent = Intent();
            returnIntent.putExtra(MainActivity.SEARCH_RESULT,it.id);
            setResult(Activity.RESULT_OK,returnIntent);
            finish()
        }
        binding.searchQuery.requestFocus()

        binding.searchResultsRecycler.adapter = adapter
        binding.searchResultsRecycler.layoutManager = GridLayoutManager(this, 2)
        binding.searchResultsRecycler.addItemDecoration(SpacesItemDecoration(toDp(16)))


        binding.searchQuery.doAfterTextChanged {
            if (it!!.isNotBlank())
                search(it.toString())
        }
    }

    private fun search(query:String) {
        lifecycleScope.launch {
            viewModel.getSearchResponse(query).collect {
                adapter.submitData(it)
                return@collect
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        setResult(Activity.RESULT_CANCELED,Intent());
        finish()
        return super.onOptionsItemSelected(item)
    }
}