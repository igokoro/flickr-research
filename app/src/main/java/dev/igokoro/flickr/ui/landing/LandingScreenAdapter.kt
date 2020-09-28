package dev.igokoro.flickr.ui.landing

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.igokoro.flickr.databinding.ItemRecentBinding
import dev.igokoro.flickr.databinding.ItemTagBinding
import dev.igokoro.flickr.ktx.text
import dev.igokoro.flickr.utils.GlideRequests
import javax.inject.Inject

internal class MainScreenAdapter constructor(
    private val glide: GlideRequests,
    private val viewModel: LandingViewModel,
    itemCallback: ItemCallbackImpl
) : ListAdapter<PhotoCluster, RecyclerView.ViewHolder>(itemCallback) {

    override fun onCreateViewHolder(
        container: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(container.context)

        return when (viewType) {
            0 -> RecentClusterViewHolder(
                glide = glide,
                binding = ItemRecentBinding.inflate(
                    inflater,
                    container,
                    false
                )
            ) { viewModel.onRecentClick() }
            1 -> TagClusterViewHolder(
                glide = glide,
                binding = ItemTagBinding.inflate(
                    inflater,
                    container,
                    false
                )
            ) { viewModel.onTagClick(it) }
            else -> throw IllegalArgumentException("Unknown type: $viewType")
        }
    }

    override fun onBindViewHolder(
        viewHolder: RecyclerView.ViewHolder,
        position: Int
    ) {
        when (viewHolder) {
            is RecentClusterViewHolder -> viewHolder.bind(getItem(position))
            is TagClusterViewHolder -> viewHolder.bind(getItem(position))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type
    }
}

internal class MainScreenAdapterFactory @Inject constructor(
    private val glide: GlideRequests,
    private val itemCallback: ItemCallbackImpl
) {
    fun build(viewModel: LandingViewModel): MainScreenAdapter {
        return MainScreenAdapter(
            viewModel = viewModel,
            glide = glide,
            itemCallback = itemCallback
        )
    }
}

internal class ItemCallbackImpl @Inject constructor() : DiffUtil.ItemCallback<PhotoCluster>() {

    // this implementation is a little weak for real world
    // but will do for exercise purposes
    override fun areItemsTheSame(
        p0: PhotoCluster,
        p1: PhotoCluster
    ) = p0.label == p1.label

    override fun areContentsTheSame(
        p0: PhotoCluster,
        p1: PhotoCluster
    ) = p0 == p1
}

internal class RecentClusterViewHolder(
    private val glide: GlideRequests,
    private val binding: ItemRecentBinding,
    private val onClick: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(data: PhotoCluster) {
        binding.label.text(data.label)
        binding.flow.setOnClickListener { onClick() }
        if (data.photos.isNotEmpty()) {
            glide
                .load(data.photos[0])
                .fitCenter()
                .into(binding.slot1)
            glide
                .load(data.photos[1])
                .fitCenter()
                .into(binding.slot2)
            glide
                .load(data.photos[2])
                .fitCenter()
                .into(binding.slot3)
            glide
                .load(data.photos[3])
                .fitCenter()
                .into(binding.slot4)
            glide
                .load(data.photos[4])
                .fitCenter()
                .into(binding.slot5)
            glide
                .load(data.photos[5])
                .fitCenter()
                .into(binding.slot6)
            glide
                .load(data.photos[6])
                .fitCenter()
                .into(binding.slot7)
            glide
                .load(data.photos[7])
                .fitCenter()
                .into(binding.slot8)
        }
    }
}

internal class TagClusterViewHolder(
    private val glide: GlideRequests,
    private val binding: ItemTagBinding,
    private val onClick: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(data: PhotoCluster) {
        binding.label.text(data.label)
        glide
            .load(data.photos.first())
            .fitCenter()
            .into(binding.image)
        binding.image.setOnClickListener { onClick((data.label as RemoteText).text) }
    }
}

