package com.hiloipa.app.hilo.utils

import android.util.Base64
import java.nio.charset.Charset
import java.security.Key
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

/**
 * Created by eduardalbu on 21.02.2018.
 */
object AESCrypt {
    private val ALGORITHM = "AES"
    private val KEY = "CyL4J^\\mJ4>Vc/cQ"

    @Throws(Exception::class)
    fun encrypt(value: String): String {
        val key = generateKey()
        val cipher = Cipher.getInstance(AESCrypt.ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val encryptedByteValue = cipher.doFinal(value.toByteArray(charset("utf-8")))
        return Base64.encodeToString(encryptedByteValue, Base64.DEFAULT)

    }

    @Throws(Exception::class)
    fun decrypt(value: String): String {
        val key = generateKey()
        val cipher = Cipher.getInstance(AESCrypt.ALGORITHM)
        cipher.init(Cipher.DECRYPT_MODE, key)
        val decryptedValue64 = Base64.decode(value, Base64.DEFAULT)
        val decryptedByteValue = cipher.doFinal(decryptedValue64)
        return String(decryptedByteValue, Charset.forName("utf-8"))

    }

    @Throws(Exception::class)
    private fun generateKey(): Key {
        return SecretKeySpec(KEY.toByteArray(), ALGORITHM)
    }
}