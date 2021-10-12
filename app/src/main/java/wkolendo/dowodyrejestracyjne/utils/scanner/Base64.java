/*
 * Copyright 2007 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package wkolendo.dowodyrejestracyjne.utils.scanner;

import java.io.ByteArrayOutputStream;

/**
 * @author EmilHernvall (GitHub)
 */
public final class Base64 {

	public static byte[] decode(String data) {
		int[] tbl = {
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
				-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
		byte[] bytes = data.getBytes();
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		for (int i = 0; i < bytes.length; ) {
			int b = 0;
			if (tbl[bytes[i]] != -1) {
				b = (tbl[bytes[i]] & 0xFF) << 18;
			} else {
				i++;
				continue;
			}

			int num = 0;
			if (i + 1 < bytes.length && tbl[bytes[i + 1]] != -1) {
				b = b | ((tbl[bytes[i + 1]] & 0xFF) << 12);
				num++;
			}
			if (i + 2 < bytes.length && tbl[bytes[i + 2]] != -1) {
				b = b | ((tbl[bytes[i + 2]] & 0xFF) << 6);
				num++;
			}
			if (i + 3 < bytes.length && tbl[bytes[i + 3]] != -1) {
				b = b | (tbl[bytes[i + 3]] & 0xFF);
				num++;
			}

			while (num > 0) {
				int c = (b & 0xFF0000) >> 16;
				buffer.write((char) c);
				b <<= 8;
				num--;
			}
			i += 4;
		}
		return buffer.toByteArray();
	}
}
