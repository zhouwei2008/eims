import org.jpos.iso.ISOUtil
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESKeySpec

/**
 *
 * @author Administrator
 */
class DESCodec {
    static encode = { String target ->
        def cipher = getCipher(Cipher.ENCRYPT_MODE)
        return org.jpos.iso.ISOUtil.hexString(cipher.doFinal(target.bytes))
    }

    static decode = { String target ->
        def cipher = getCipher(Cipher.DECRYPT_MODE)
        return new String(cipher.doFinal(org.jpos.iso.ISOUtil.hex2byte(target)))
    }

    private static getCipher(mode) {
        def keySpec = new DESKeySpec(getPassword())
        def cipher = Cipher.getInstance("DES")
        def keyFactory = SecretKeyFactory.getInstance("DES")
        cipher.init(mode, keyFactory.generateSecret(keySpec))
        return cipher
    }

    //密码长度必须是8的倍数
    private static getPassword() { "passfordesnishiwode&0082".getBytes("UTF-8") }

    static void main(args)
    {
        def strmi=encode("account")
        println strmi
        println decode(strmi)
    }
}