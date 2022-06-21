package com.zhangwww.videoplayer.ui.main

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zhangwww.videoplayer.R
import com.zhangwww.videoplayer.data.ParseInfo
import com.zhangwww.videoplayer.databinding.ItemParseBinding

class ParseItemAdapter : BaseQuickAdapter<ParseInfo, BaseViewHolder>(R.layout.item_parse) {

    var onItemClick: (ParseInfo) -> Unit = {}

    override fun convert(holder: BaseViewHolder, item: ParseInfo) {
        val binding = ItemParseBinding.bind(holder.itemView)
        binding.btnSource.text = item.name
        binding.btnSource.setOnClickListener {
            onItemClick.invoke(item)
        }
    }
}