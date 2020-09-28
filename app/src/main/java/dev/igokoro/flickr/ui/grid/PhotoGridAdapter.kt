package dev.igokoro.flickr.ui.grid

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dev.igokoro.flickr.data.Photo
import dev.igokoro.flickr.databinding.ItemPhotoBinding
import dev.igokoro.flickr.ui.landing.PhotoUrlConverter
import dev.igokoro.flickr.utils.GlideRequests
import javax.inject.Inject

internal class PhotoGridAdapter @Inject constructor(
    itemCallback: ItemCallbackImpl,
    private val glide: GlideRequests,
    private val photoConverter: PhotoUrlConverter
) : PagingDataAdapter<Photo, PhotoViewHolder>(itemCallback) {
    override fun onBindViewHolder(
        viewHolder: PhotoViewHolder,
        position: Int
    ) {
        viewHolder.bind(getItem(position)!!)
    }

    override fun onCreateViewHolder(
        container: ViewGroup,
        viewType: Int
    ): PhotoViewHolder {
        return PhotoViewHolder(
            glide,
            toPhotoUrl = photoConverter,
            ItemPhotoBinding.inflate(
                LayoutInflater.from(container.context),
                container,
                false
            )
        )
    }

    override fun onViewRecycled(holder: PhotoViewHolder) {
        holder.onRecycled()
    }
}

internal class ItemCallbackImpl @Inject constructor() : DiffUtil.ItemCallback<Photo>() {
    override fun areItemsTheSame(
        p0: Photo,
        p1: Photo
    ) = p0.id == p1.id

    override fun areContentsTheSame(
        p0: Photo,
        p1: Photo
    ) = p0 == p1

}

class PhotoViewHolder(
    private val glide: GlideRequests,
    private val toPhotoUrl: PhotoUrlConverter,
    private val binding: ItemPhotoBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(photo: Photo) {
        glide.load(toPhotoUrl(photo))
            .placeholder(ColorDrawable(Color.GRAY))
            .error(ColorDrawable(Color.RED))
            .into(binding.root)
    }

    fun onRecycled() {
        glide.clear(binding.root)
    }
}