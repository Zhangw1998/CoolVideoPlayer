package com.zhangwww.videoplayer.config

import com.zhangwww.videoplayer.data.ParseInfo


interface IParseRule {

    val url: String

    fun isSuccess(requestUrl: String?, originSource: OriginSource): Boolean

    companion object {
        val MU = MuJXParseRule()
        val PARWIX = ParwixParseRule()
        val XIHU = XiHuParseRule()

        val parseList = listOf(
            ParseInfo("MU解析", MuJXParseRule.jxUrl, MU),
            ParseInfo("Parwix解析(未实现)", ParwixParseRule.jxUrl, PARWIX),
            ParseInfo("XiHu解析", XiHuParseRule.jxUrl, XIHU)
        )
    }
}

class MuJXParseRule(override val url: String = jxUrl) : IParseRule {

    companion object {
        const val jxUrl = "https://jx.m3u8.pw/?url="
    }

    override fun isSuccess(requestUrl: String?, originSource: OriginSource): Boolean {
        if (requestUrl == null) return false
        return when (originSource) {
            OriginSource.IQIYI -> {
                requestUrl.endsWith(".m3u8")
            }
            OriginSource.TENCENT -> {
                requestUrl.contains(".mp4?name=")
            }
            OriginSource.MANGO -> {
                requestUrl.contains(".m3u8?")
            }
            OriginSource.YOUKU -> {
                requestUrl.contains(".mp4?name=")
            }
        }
    }
}

class ParwixParseRule(override val url: String = jxUrl) : IParseRule {

    companion object {
        const val jxUrl = "https://jx.parwix.com:4433/player/?url="
    }

    override fun isSuccess(requestUrl: String?, originSource: OriginSource): Boolean {
        if (requestUrl == null) return false
        // https://211.99.101.171:4433/c/sign.php?url=3cda1c4b020bcf54917951dd1e230c9c.m3u8&vkey=18176aCArs4Bu4h6G3KI5rEnRzpLclNXWAA66ftS
        // https://ali2.a.kwimgs.com/ufile/adsocial/886aa216-3fb1-41da-a3ff-7159b136da02.jpg
        // todo 需要分析m3u8文件，自定义DataSource?
        requestUrl.contains(".m3u8&vkey=")
        return false
    }
}

class XiHuParseRule(override val url: String = jxUrl) : IParseRule {

    companion object {
        const val jxUrl = "https://yparse.jn1.cc/index.php?url="
    }

    override fun isSuccess(requestUrl: String?, originSource: OriginSource): Boolean {
        if (requestUrl == null) return false
        return (requestUrl.contains(".m3u8"))
    }

}

