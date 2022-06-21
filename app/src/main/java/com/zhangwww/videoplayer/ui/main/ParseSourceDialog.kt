package com.zhangwww.videoplayer.ui.main

import android.annotation.SuppressLint
import android.content.Context
import com.blankj.utilcode.util.ScreenUtils
import com.lxj.xpopup.core.CenterPopupView
import com.zhangwww.videoplayer.R
import com.zhangwww.videoplayer.config.IParseRule
import com.zhangwww.videoplayer.data.ParseInfo
import com.zhangwww.videoplayer.databinding.DialogParseSourceBinding

@SuppressLint("ViewConstructor")
class ParseSourceDialog(
    context: Context,
    private val onParseClick: (ParseInfo) -> Unit
) : CenterPopupView(context) {

    override fun getImplLayoutId(): Int = R.layout.dialog_parse_source

    override fun getMaxWidth(): Int = ScreenUtils.getScreenWidth()

    override fun onCreate() {
        super.onCreate()
        val binding = DialogParseSourceBinding.bind(popupImplView)
        binding.rvParse.adapter = ParseItemAdapter().apply {
            setList(IParseRule.parseList)
            onItemClick = {
                dismiss()
                onParseClick.invoke(it)
            }
        }
    }

}