package dev.igokoro.flickr.ui.grid

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.paging.LoadState
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applySystemWindowInsetsToPadding
import dev.igokoro.flickr.databinding.FragmentPhotoGridBinding
import dev.igokoro.flickr.ktx.autoDispose
import dev.igokoro.flickr.ktx.title
import kotlinx.android.parcel.Parcelize
import javax.inject.Inject

@AndroidEntryPoint
class PhotoGridFragment : Fragment() {
    // would be really nice to completely hide ViewModel implementation from View
    private val viewModel: PhotoGridViewModel by viewModels<PhotoGridViewModelImpl>()

    @Inject
    internal lateinit var adapter: PhotoGridAdapter

    private var binding: FragmentPhotoGridBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPhotoGridBinding.inflate(
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
            root.applySystemWindowInsetsToPadding(top = true)
            grid.applySystemWindowInsetsToPadding(bottom = true)
            val navController = findNavController()
            toolbar.setupWithNavController(
                navController,
                AppBarConfiguration(navController.graph)
            )
            adapter.addLoadStateListener {
                if (it.append is LoadState.Loading) {
                    progress.show()
                } else {
                    progress.hide()
                }
            }
            toolbar.title(viewModel.title)
            grid.adapter = adapter
            viewModel.photos
                .autoDispose(viewLifecycleOwner.lifecycle)
                .subscribe {
                    adapter.submitData(
                        viewLifecycleOwner.lifecycle,
                        it
                    )
                }
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}

enum class RollType {
    RECENT, TAG
}

@Parcelize
data class PhotoGridParam(
    val rollType: RollType,
    val tag: String? = null
) : Parcelable
