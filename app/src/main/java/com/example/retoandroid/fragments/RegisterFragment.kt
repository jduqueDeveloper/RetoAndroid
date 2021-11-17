package com.example.retoandroid.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.retoandroid.models.User
import com.example.retoandroid.R
import com.example.retoandroid.utils.AESCrypt
import com.example.retoandroid.databinding.FragmentRegisterBinding
import com.example.retoandroid.viewModels.RegisterViewModel

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    lateinit var navController: NavController
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navController = Navigation.findNavController(view)
        binding.txtTitleRegister.text = getString(R.string.create_user)
        binding.buttonRegister.text = getString(R.string.create)

        registerViewModel = RegisterViewModel()

        setOnChangeListenerTextUser()

        binding.buttonRegister.setOnClickListener {
            if (binding.txtUser.text!!.isNotEmpty()) {
                if (binding.txtPasswordRegister.text.toString() ==
                    binding.txtPasswordAgainRegister.text.toString()
                ) {
                    val passwordEncrypt =
                        AESCrypt.encrypt(binding.txtPasswordAgainRegister.text.toString())
                    val user = User(
                        username = binding.txtUser.text.toString(),
                        password = passwordEncrypt
                    )
                    registerViewModel.postUserFireBase(user = user)
                    navController.popBackStack()
                } else {
                    binding.txtPasswordRegisterContainer.error = getString(R.string.error_password)
                    binding.txtPasswordAgainRegisterContainer.error =
                        getString(R.string.error_password)
                }
            } else {
                binding.txtUserRegisterContainer.error = getString(R.string.error_user_empty)
            }
        }

        binding.imgBack.setOnClickListener {
            navController.popBackStack()
        }

    }

    private fun setOnChangeListenerTextUser() {
        binding.txtUser.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().isEmpty()) {
                    binding.txtUserRegisterContainer.error = getString(R.string.error_user_empty)
                } else {
                    binding.txtUserRegisterContainer.error = null
                }
            }
        })
    }

}