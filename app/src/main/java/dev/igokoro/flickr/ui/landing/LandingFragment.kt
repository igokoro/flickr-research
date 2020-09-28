package dev.igokoro.flickr.ui.landing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applySystemWindowInsetsToPadding
import dev.igokoro.flickr.databinding.FragmentLandingBinding
import dev.igokoro.flickr.ktx.autoDispose
import javax.inject.Inject

@AndroidEntryPoint
class LandingFragment : Fragment() {
    @Inject
    internal lateinit var adapterFactory: MainScreenAdapterFactory

    private val viewModel: LandingViewModel by viewModels<LandingViewModelImpl>()

    private val adapter: MainScreenAdapter by lazy { adapterFactory.build(viewModel) }
    private var binding: FragmentLandingBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLandingBinding.inflate(
            inflater,
            container,
            false
        )
        return binding!!.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        binding!!.apply {
            main.applySystemWindowInsetsToPadding(top = true)
            list.applySystemWindowInsetsToPadding(bottom = true)
            list.adapter = adapter
            val navController = findNavController()
            toolbar.setupWithNavController(
                navController,
                AppBarConfiguration(navController.graph)
            )
            viewModel.progress.observe(viewLifecycleOwner) { active ->
                if (active) {
                    progress.show()
                } else {
                    progress.hide()
                }
            }
//            viewModel
        }



        viewModel.content.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        viewModel.navigation
            .autoDispose(viewLifecycleOwner.lifecycle)
            .subscribe { findNavController().navigate(it) }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}