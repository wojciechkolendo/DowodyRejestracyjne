/*
 * Copyright 2008 ZXing authors
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

package wkolendo.dowodyrejestracyjne.utils.decoding;

/**
 * @author Bartosz Soja
 */
public class BitReader {
  private byte[] data;
  private int dataPosition;
  private int currentByte;
  private int bitPosition;

  public BitReader(byte[] data) {
    this.data = data;
    dataPosition = 1;
  }

  public BitReader(byte[] data, int startIdx) {
    this.data = data;
    dataPosition = startIdx;
  }

  public boolean isDataAvailable() {
    return dataPosition < data.length;
  }

  public int readBit() {
    if (!isDataAvailable()) {
      throw new RuntimeException("No more data available!");
    }

    if (bitPosition == 0) {
      currentByte = data[dataPosition++] & 0xff;
      bitPosition = 8;
    }
    return ((currentByte >>> --bitPosition) & 1) & 0xff;
  }

  public int readByte() {
    if (!isDataAvailable()) {
      throw new RuntimeException("No more data available!");
    }

    return data[dataPosition++] & 0xff;
  }
}
