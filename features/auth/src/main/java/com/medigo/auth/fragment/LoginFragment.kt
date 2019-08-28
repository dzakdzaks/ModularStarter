package com.medigo.auth.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.medigo.auth.databinding.FragmentLoginBinding
import com.medigo.auth.viewmodel.LoginViewModel
import com.medigo.common.base.BaseFragment
import com.medigo.common.base.BaseViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class LoginFragment: BaseFragment() {

    private val viewModel: LoginViewModel by viewModel()
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.toolbar.setupWithNavController(findNavController())
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // Add observer to _profile(MediatorLiveData) to trigger onchange
        binding.viewmodel?.profile?.observe(viewLifecycleOwner, Observer {

        })
    }

    override fun getViewModel(): BaseViewModel = viewModel

}