package me.leon.asymmetric

import cn.hutool.core.util.StrUtil
import cn.hutool.crypto.SecureUtil
import cn.hutool.crypto.SmUtil
import cn.hutool.crypto.asymmetric.KeyType
import kotlin.test.assertEquals
import me.leon.encode.base.base64
import me.leon.ext.crypto.allCurves
import me.leon.ext.crypto.curveMultiply
import org.junit.Test

class Sm2Test {

    // for check result
    @Test
    fun hutool() {
        val text = "我是一段测试aaaa"

        val pair = SecureUtil.generateKeyPair("SM2")
        val privateKey = pair.private.encoded
        val publicKey = pair.public.encoded

        println(privateKey.base64())
        println(publicKey.base64())

        val sm2 = SmUtil.sm2(privateKey, publicKey)
        val encryptStr = sm2.encryptBcd(text, KeyType.PublicKey)
        println(encryptStr)
        val decryptStr = StrUtil.utf8Str(sm2.decryptFromBcd(encryptStr, KeyType.PrivateKey))
        println(decryptStr)
    }

    @Test
    fun dotTimes() {
        val dot1X =
            "09F9DF311E5421A150DD7D161E4BC5C672179FAD1833FC076BB08FF356F35020".toBigInteger(16)
        val dot1Y =
            "CCEA490CE26775A52DC6EA718CC1AA600AED05FBF35E084A6632F6072DA9AD13".toBigInteger(16)
        val k = "59276E27D506861A16680F3AD9C02DCCEF3CC1FA3CDBE4CE6D54B80DEAC1BC21".toBigInteger(16)

        val expectedX = "335e18d751e51f040e27d468138b7ab1dc86ad7f981d7d416222fd6ab3ed230d"
        val expectedY = "ab743ebcfb22d64f7b6ab791f70658f25b48fa93e54064fdbfbed3f0bd847ac9"
        println(allCurves.joinToString("\n"))
        "sm2p256v1".curveMultiply(dot1X, dot1Y, k).also {
            println(it)
            assertEquals(expectedX, it.first)
            assertEquals(expectedY, it.second)
        }
    }
}
