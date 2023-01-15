package com.zaze.utils.regular

/**
 * Description : 规则匹配
 * @author zaze
 * @version 2022/6/10 - 02:35
 */
object RegularProcessor {

    /**
     * 匹配Url
     */
    @JvmStatic
    fun matchUrl(input: String): UrlRegular {
        return UrlRegular(input)
    }

    /**
     * 匹配ip
     */
    @JvmStatic
    fun matchIp(input: String): IpRegular {
        return IpRegular(input)
    }
}

