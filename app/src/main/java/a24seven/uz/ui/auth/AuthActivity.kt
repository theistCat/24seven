package a24seven.uz.ui.auth

import a24seven.uz.MainActivity
import a24seven.uz.R
import a24seven.uz.databinding.ActivityAuthBinding
import a24seven.uz.network.utils.NoConnectivityException
import a24seven.uz.network.utils.Resource
import a24seven.uz.utils.*
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding
    private val viewModel: AuthViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        setSupportActionBar(binding.toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_backicon)
        binding.plus.visibility = View.INVISIBLE


        binding.policy.setOnClickListener {
            val fm = this.supportFragmentManager
            val policyDialogFragment = PolicyDialogFragment()

            policyDialogFragment.show(fm, "AddressList")
            policyDialogFragment.isCancelable = true
        }

        binding.getCode.setOnClickListener {

            if (binding.userPhone.showErrorIfNotFilled() && binding.userPhone.text.length == 12)
                viewModel.getFirstStepResponse(binding.userPhone.text.toString())
            else binding.userPhone.error = this.getString(R.string.warning_fill_the_fields)
        }

        binding.userPhone.doAfterTextChanged {
            if (binding.userPhone.text.isNotEmpty())
                binding.plus.visibility = View.VISIBLE
            else
                binding.plus.visibility = View.INVISIBLE
        }

        binding.getCode.setOnClickListener {
            viewModel.getVerifyResponse(
                binding.userPhone.text.toString(),
                binding.verifyCode.text.toString()
            )
        }

        viewModel.verifyResponse.observe(this, Observer {
            it.getContentIfNotHandled()?.let { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        binding.loaderAuth.showAsProgress()
                    }

                    is Resource.Success -> {
                        binding.loaderAuth.hideProgress()
                        val returnIntent = Intent();
                        returnIntent.putExtra(
                            MainActivity.ACCESS_TOKEN,
                            resource.data.access_token
                        );
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish()
                    }

                    is Resource.GenericError -> {
                        binding.loaderAuth.hideProgress()
                        showSnackbar(
                            resource.errorResponse.jsonResponse.optString("error") ?: "error"
                        )
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
            it.getContentIfNotHandled()?.let { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        binding.loaderGetCode.showAsProgress()
                    }

                    is Resource.Success -> {
                        binding.loaderGetCode.hideProgress()
                        binding.motionLayout.transitionToEnd()
                    }

                    is Resource.GenericError -> {
                        showSnackbar(
                            resource.errorResponse.jsonResponse.optString("error") ?: "error"
                        )

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
        setResult(Activity.RESULT_CANCELED, Intent());
        finish()
        return super.onOptionsItemSelected(item)
    }

}