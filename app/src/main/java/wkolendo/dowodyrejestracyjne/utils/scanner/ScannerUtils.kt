package wkolendo.dowodyrejestracyjne.utils.scanner

import com.google.mlkit.vision.barcode.Barcode
import java.io.ByteArrayOutputStream

private val BYTE_ARRAY = intArrayOf(
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63,
    52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1,
    -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14,
    15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1,
    -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
    41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1
)

private class BitReader(private val data: ByteArray, private var dataPosition: Int) {
    private var currentByte = 0
    private var bitPosition = 0
    val isDataAvailable: Boolean
        get() = dataPosition < data.size

    fun readBit(): Int {
        if (!isDataAvailable) throw RuntimeException("No more data available!")
        if (bitPosition == 0) {
            currentByte = data[dataPosition++].toInt() and 0xff
            bitPosition = 8
        }
        return currentByte ushr --bitPosition and 1 and 0xff
    }

    fun readByte(): Int {
        if (!isDataAvailable) throw RuntimeException("No more data available!")
        return data[dataPosition++].toInt()  and 0xff
    }
}


private fun String.decodeBase64(): ByteArray {
    val bytes = this.toByteArray()
    val buffer = ByteArrayOutputStream()
    var i = 0
    while (i < bytes.size) {
        var byte: Int
        byte = if (BYTE_ARRAY[bytes[i].toInt()] != -1) {
            BYTE_ARRAY[bytes[i].toInt()] and 0xFF shl 18
        } else {
            i++
            continue
        }
        var num = 0
        if (i + 1 < bytes.size && BYTE_ARRAY[bytes[i + 1].toInt()] != -1) {
            byte = byte or (BYTE_ARRAY[bytes[i + 1].toInt()] and 0xFF shl 12)
            num++
        }
        if (i + 2 < bytes.size && BYTE_ARRAY[bytes[i + 2].toInt()] != -1) {
            byte = byte or (BYTE_ARRAY[bytes[i + 2].toInt()] and 0xFF shl 6)
            num++
        }
        if (i + 3 < bytes.size && BYTE_ARRAY[bytes[i + 3].toInt()] != -1) {
            byte = byte or (BYTE_ARRAY[bytes[i + 3].toInt()] and 0xFF)
            num++
        }
        while (num > 0) {
            val c = byte and 0xFF0000 shr 16
            buffer.write(c)
            byte = byte shl 8
            num--
        }
        i += 4
    }
    return buffer.toByteArray()
}

private fun ByteArray.decompress(): ByteArray {
    // first 4 bytes are output size
    val outSize: Int = this[3].toInt() shl 24 and -0x1000000 or 
            (this[2].toInt() shl 16 and 0xff0000) or 
            (this[1].toInt() shl 8 and 0xff00) or 
            (this[0].toInt() and 0xff)
    val bitsReader = BitReader(this, 4)
    val dst = ByteArray(outSize)
    var olen = 0
    var mOffLast: Long = 1
    while (true) {
        var mOff: Long = 1
        var mLen: Long
        if (!bitsReader.isDataAvailable) {
            return dst
        }
        while (bitsReader.readBit() == 1) {
            dst[olen++] = bitsReader.readByte().toByte()
        }
        while (true) {
            if (!bitsReader.isDataAvailable) {
                return dst
            }
            mOff = mOff * 2 + bitsReader.readBit()
            if (bitsReader.readBit() == 1) {
                break
            }
            mOff = (mOff - 1) * 2 + bitsReader.readBit()
        }
        if (mOff == 2L) {
            mOff = mOffLast
            mLen = bitsReader.readBit().toLong()
        } else {
            mOff = (mOff - 3) * 0x100 + bitsReader.readByte()
            if (mOff == 1L) {
                break
            }
            mLen = mOff xor 1 and 1
            mOff = mOff shr 1
            mOffLast = ++mOff
        }
        if (!bitsReader.isDataAvailable) {
            return dst
        }
        when {
            mLen > 0 -> {
                mLen = 1L + bitsReader.readBit()
            }
            bitsReader.readBit() == 1 -> {
                mLen = 3L + bitsReader.readBit()
            }
            else -> {
                mLen++
                do {
                    if (!bitsReader.isDataAvailable) {
                        return dst
                    }
                    mLen = mLen * 2 + bitsReader.readBit()
                    if (!bitsReader.isDataAvailable) {
                        return dst
                    }
                } while (bitsReader.readBit() == 0)
                mLen += 3
            }
        }
        mLen += (if (mOff >= -0xFFFB00 and Short.MAX_VALUE.toInt()) 1 else 0).toLong()
        var mPos = (olen - mOff).toInt()
        dst[olen++] = dst[mPos++]
        do {
            dst[olen++] = dst[mPos++]
        } while (--mLen > 0)
    }
    return dst
}

@Throws(NullPointerException::class)
fun Barcode.decode() = String(rawValue!!.decodeBase64().decompress(), Charsets.UTF_16LE)