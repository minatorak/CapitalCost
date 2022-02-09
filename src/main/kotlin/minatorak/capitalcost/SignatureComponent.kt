package minatorak.capitalcost

import org.bouncycastle.crypto.Digest
import org.bouncycastle.crypto.digests.MD5Digest
import org.bouncycastle.crypto.digests.SHA256Digest
import org.bouncycastle.crypto.digests.SHA384Digest
import org.bouncycastle.crypto.digests.SHA512Digest
import org.bouncycastle.crypto.macs.HMac
import org.bouncycastle.crypto.params.KeyParameter
import org.springframework.stereotype.Component

@Component
class SignatureComponent {

    fun hmacWithBouncyCastle(data: String, secretKey: String, algorithm: String = "HmacSHA256"): String? {
        val digest = getHashDigest(algorithm)
        val hMac = HMac(digest)
        hMac.init(KeyParameter(secretKey.toByteArray()));
        val hmacIn = data.toByteArray()
        hMac.update(hmacIn, 0, hmacIn.size)
        val hmacOut = ByteArray(hMac.macSize)

        hMac.doFinal(hmacOut, 0)
        return bytesToHex(hmacOut)
    }


    private fun getHashDigest(algorithm: String): Digest? {
        when (algorithm) {
            "HmacMD5" -> return MD5Digest()
            "HmacSHA256" -> return SHA256Digest()
            "HmacSHA384" -> return SHA384Digest()
            "HmacSHA512" -> return SHA512Digest()
        }
        return SHA256Digest()
    }

    private fun bytesToHex(hash: ByteArray): String? {
        val hexString = StringBuilder(2 * hash.size)
        for (h in hash) {
            val hex = Integer.toHexString(0xff and h.toInt())
            if (hex.length == 1) hexString.append('0')
            hexString.append(hex)
        }
        return hexString.toString()
    }
}