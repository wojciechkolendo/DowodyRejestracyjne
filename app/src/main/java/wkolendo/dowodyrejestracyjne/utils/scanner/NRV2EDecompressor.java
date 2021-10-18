/*
 * NRV2E Decompressor (based on UCL library by Markus F.X.J Oberhumer)
 * Copyright (C) 2018 Bartosz Soja
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package wkolendo.dowodyrejestracyjne.utils.scanner;

/**
 * @author Bartosz Soja
 * ported from https://bitbucket.org/bsoja/nrv2e-csharp
 * based on UCL library created by: Markus F.X.J. Oberhumer
 */
public final class NRV2EDecompressor {
    private NRV2EDecompressor() {

    }

    public static byte[] decompress(byte[] src) {
        // first 4 bytes are output size
        int out_size = (src[3] << 24) & 0xff000000 |
                (src[2] << 16) & 0xff0000 |
                (src[1] << 8) & 0xff00 |
                src[0] & 0xff;

        BitReader bitsReader = new BitReader(src, 4);
        byte[] dst = new byte[out_size];
        int olen = 0;
        long last_m_off = 1;

        for (; ; ) {
            long m_off = 1;
            long m_len;

            if (!bitsReader.isDataAvailable()) {
                return dst;
            }

            while (bitsReader.readBit() == 1) {
                dst[olen++] = (byte) bitsReader.readByte();
            }
            for (; ; ) {
                if (!bitsReader.isDataAvailable()) {
                    return dst;
                }

                m_off = m_off * 2 + bitsReader.readBit();

                if (bitsReader.readBit() == 1) {
                    break;
                }
                m_off = (m_off - 1) * 2 + bitsReader.readBit();
            }
            if (m_off == 2) {
                m_off = last_m_off;
                m_len = bitsReader.readBit();
            } else {
                m_off = (m_off - 3) * 0x100 + bitsReader.readByte();
                if (m_off == 1) {
                    break;
                }
                m_len = (m_off ^ 1) & 1;
                m_off >>= 1;
                last_m_off = ++m_off;
            }

            if (!bitsReader.isDataAvailable()) {
                return dst;
            }

            if (m_len > 0) {
                m_len = 1L + bitsReader.readBit();
            } else if (bitsReader.readBit() == 1) {
                m_len = 3L + bitsReader.readBit();
            } else {
                m_len++;
                do {
                    if (!bitsReader.isDataAvailable()) {
                        return dst;
                    }
                    m_len = m_len * 2 + bitsReader.readBit();
                    if (!bitsReader.isDataAvailable()) {
                        return dst;
                    }
                } while (bitsReader.readBit() == 0);
                m_len += 3;
            }
            m_len += (long) (m_off >= (-0xFFFB00 & Short.MAX_VALUE) ? 1 : 0);

            int m_pos = (int) (olen - m_off);
            dst[olen++] = dst[m_pos++];
            do {
                dst[olen++] = dst[m_pos++];
            } while (--m_len > 0);
        }

        return dst;
    }
}
