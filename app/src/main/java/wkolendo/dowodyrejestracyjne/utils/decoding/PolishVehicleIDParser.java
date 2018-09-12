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

package wkolendo.dowodyrejestracyjne.utils.decoding;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.client.result.TextParsedResult;

/**
 * Parsing data from AZTEC codes in Polish Vehicle ID Card
 */
public final class PolishVehicleIDParser extends ResultParser {

  @Override
  public TextParsedResult parse(Result result) {
    if (result.getBarcodeFormat() != BarcodeFormat.AZTEC) {
      return null;
    }

    try {
      byte[] debased = Base64.decode(getMassagedText(result));
      byte[] decompress = NRV2EDecompressor.decompress(debased);
      return new TextParsedResult(new String(decompress, "UnicodeLittle"), null);
    } catch (Exception e) {
      return new TextParsedResult("Error: " + e.getClass().getCanonicalName() + ": "
        + e.getMessage(), null);
    }
  }

}
