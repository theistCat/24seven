package a24seven.uz.ui.auth.singIn

import a24seven.uz.databinding.ActivitySingInBinding
import a24seven.uz.ui.auth.AuthViewModel
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class SingInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySingInBinding
    private val viewModel: AuthViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingInBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}