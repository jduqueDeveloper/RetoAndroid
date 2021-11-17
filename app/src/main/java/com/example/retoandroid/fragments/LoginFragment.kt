package com.example.retoandroid.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.retoandroid.R
import com.example.retoandroid.utils.AESCrypt
import com.example.retoandroid.databinding.FragmentLoginBinding
import com.example.retoandroid.viewModels.LoginViewModel

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var username :String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        loginViewModel = LoginViewModel()

        binding.buttonLogin.setOnClickListener {
            username = binding.txtUser.text.toString()
            val password = binding.txtPassword.text.toString()
            val passwordEncrypt = AESCrypt.encrypt(password)
            loginViewModel.login(user = username, passwordEncrypt = passwordEncrypt)
        }

        loginViewModel.isConected.observe(viewLifecycleOwner, {
           if(it){
               findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToTasksFragment(username))
           }else{
               binding.txtUserContainer.error = getString(R.string.error_login)
               binding.txtPasswordContainer.error = getString(R.string.error_login)
           }
        })


        binding.txtRegisterUnderline.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }
}