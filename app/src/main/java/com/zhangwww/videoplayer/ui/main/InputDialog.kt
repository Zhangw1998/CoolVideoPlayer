package com.zhangwww.videoplayer.ui.main


import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import com.blankj.utilcode.util.ScreenUtils
import com.lxj.xpopup.core.BottomPopupView
import com.zhangwww.videoplayer.R
import com.zhangwww.videoplayer.databinding.DialogInputBinding

@SuppressLint("ViewConstructor")
class InputDialog(
    context: Context,
    private val onClick: (String) -> Unit
) : BottomPopupView(context) {

    override fun getImplLayoutId(): Int = R.layout.dialog_input

    override fun getPopupWidth(): Int = ScreenUtils.getScreenWidth()

    override fun onCreate() {
        super.onCreate()
        val binding: DialogInputBinding = DialogInputBinding.bind(popupImplView)
        binding.ivSend.setOnClickListener {
            val content = binding.etInput.text?.toString() ?: ""
            if (content.isEmpty()) return@setOnClickListener
            onClick.invoke(content)
            dismiss()
        }
        binding.etInput.addTextChangedListener(onTextChanged = { content, _, _, _ ->
            val colorResId = if (content.isNullOrBlank()) R.color.white else R.color.purple_500
            val color = ResourcesCompat.getColor(resources, colorResId, null)
            binding.ivSend.imageTintList = ColorStateList.valueOf(color)
        })
    }
}