package com.zhangwww.videoplayer.data

import com.zhangwww.videoplayer.config.IParseRule

data class ParseInfo(
    val name: String,
    val jxUrl: String,
    val parseRule: IParseRule
)