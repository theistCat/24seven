package uz.usoft.a24seven.ui.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.usoft.a24seven.MainActivity
import uz.usoft.a24seven.R
import uz.usoft.a24seven.data.PrefManager
import uz.usoft.a24seven.databinding.ActivityAuthBinding
import uz.usoft.a24seven.network.utils.NoConnectivityException
import uz.usoft.a24seven.network.utils.Resource
import uz.usoft.a24seven.utils.*

class AuthActivity : AppCompatActivity() {
    private lateinit var binding:ActivityAuthBinding
    private val viewModel:AuthViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_backicon)

        binding.getCode.setOnClickListener {
            binding.userPhone.showErrorIfNotFilled()
            if(binding.userPhone.text.isNotBlank())
                viewModel.getFirstStepResponse(binding.userPhone.text.toString())
        }

        binding.auth.setOnClickListener {
            viewModel.getVerifyResponse(binding.userPhone.text.toString(),binding.verifyCode.text.toString())
        }

        viewModel.verifyResponse.observe(this, Observer {
                it.getContentIfNotHandled()?.let {resource->
                    when (resource) {
                        is Resource.Loading -> {
                            binding.loaderAuth.showAsProgress()
                        }
                        is Resource.Success -> {
                            binding.loaderAuth.hideProgress()
                            val  returnIntent = Intent();
                            returnIntent.putExtra(MainActivity.ACCESS_TOKEN,resource.data.access_token);
                            setResult(Activity.RESULT_OK,returnIntent);
                            finish()
                        }
                        is Resource.GenericError -> {
                            binding.loaderAuth.hideProgress()
                            showSnackbar(resource.errorResponse.jsonResponse.getString("error"))
                        }
                        is Resource.Error -> {
                            binding.loaderAuth.hideProgress()
                            if (resource.exception is NoConnectivityException) {
                                //showNoConnectionDialog(this::onRetry)
                            } else resource.exception.message?.let { it1 -> showSnackbar(it1) }
                        }
                    }
                }
        })

        viewModel.firstStepResponse.observe(this, Observer {
            it.getContentIfNotHandled()?.let {resource->
                when (resource) {
                    is Resource.Loading -> {
                        binding.loaderGetCode.showAsProgress()
                    }
                    is Resource.Success -> {
                        binding.loaderGetCode.hideProgress()
                        binding.motionLayout.transitionToEnd()
                    }
                    is Resource.GenericError -> {
                        showSnackbar(resource.errorResponse.jsonResponse.getString("error"))

                        binding.loaderGetCode.hideProgress()
                    }
                    is Resource.Error -> {
                        binding.loaderGetCode.hideProgress()
                        if (resource.exception is NoConnectivityException) {
                            //showNoConnectionDialog(this::onRetry)
                        } else resource.exception.message?.let { it1 -> showSnackbar(it1) }
                    }
                }
            }
        })
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        setResult(Activity.RESULT_CANCELED,Intent());
        finish()
        return super.onOptionsItemSelected(item)
    }

}