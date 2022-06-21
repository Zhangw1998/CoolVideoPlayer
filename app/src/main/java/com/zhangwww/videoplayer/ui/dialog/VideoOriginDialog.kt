package com.zhangwww.videoplayer.ui.dialog

import android.annotation.SuppressLint
import android.content.Context
import com.blankj.utilcode.util.ScreenUtils
import com.lxj.xpopup.core.CenterPopupView
import com.zhangwww.videoplayer.R
import com.zhangwww.videoplayer.config.OriginSource
import com.zhangwww.videoplayer.databinding.DialogOriginSourceBinding

@SuppressLint("ViewConstructor")
class VideoOriginDialog(
    context: Context, private val onOriginSourceSelected: (source: OriginSource) -> Unit
) : CenterPopupView(context) {

    override fun getImplLayoutId(): Int = R.layout.dialog_origin_source

    override fun getMaxWidth(): Int = ScreenUtils.getScreenWidth()

    override fun onCreate() {
        super.onCreate()
        val binding = DialogOriginSourceBinding.bind(popupImplView)
        binding.btnIqiyi.setOnClickListener {
            onItemClick(OriginSource.IQIYI)
        }
        binding.btnTencent.setOnClickListener {
            onItemClick(OriginSource.TENCENT)
        }
        binding.btnYouku.setOnClickListener {
            onItemClick(OriginSource.YOUKU)
        }
        binding.btnMango.setOnClickListener {
            onItemClick(OriginSource.MANGO)
        }
    }

    private fun onItemClick(source: OriginSource) {
        dismiss()
        onOriginSourceSelected.invoke(source)
    }


}