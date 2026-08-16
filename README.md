# Skaner DR — Dowody Rejestracyjne

Android app that reads the Aztec code printed on Polish vehicle registration certificates and turns
it into a readable, copyable form. Scanned certificates are kept in a local history so the data can
be looked up again without the document at hand.

<a href='https://play.google.com/store/apps/details?id=wkolendo.dowodyrejestracyjne&pcampaignid=MKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png' height='60'/></a>

---

## What it does

Polish registration certificates carry an Aztec 2D barcode holding the full vehicle and owner
record. Reading it by eye is impossible and the data is not plain text, so the app does the whole
pipeline on device:

1. **Capture** — CameraX feeds frames to an ML Kit detector restricted to `FORMAT_AZTEC`.
2. **Decode** — the payload is Base64, wrapping a stream compressed with **NRV2E** from the UCL
   family. No maintained Android library covers this format, so `utils/scanner/` bundles a
   third-party implementation — see [Credits](#credits).
3. **Parse** — the decompressed bytes are UTF-16LE text with pipe-delimited fields, mapped onto a
   `Certificate` model whose properties follow the official field codes (A, B, C.1.1, D.1, E, F.1 …).
4. **Present** — a read-only form grouped into vehicle basics, owner/keeper, technical parameters and
   miscellany, with copy and share actions.

**Certificate data never leaves the device.** It is decoded, displayed and stored locally, and no
part of it — no registration number, VIN, name, address or national ID — is sent anywhere. The app
uses Firebase Crashlytics and Analytics for crash reports and screen-level usage statistics; neither
receives any scanned data. Advertising ID collection is switched off.

## Tech stack

| Area | Choice |
|---|---|
| Language | Kotlin 2.4 |
| UI | Jetpack Compose, Material 3 with dynamic color |
| Navigation | Navigation 3 (`androidx.navigation3`) |
| Camera | CameraX + `camera-compose` (`CameraXViewfinder`) |
| Barcode | ML Kit Barcode Scanning |
| Persistence | Room (KSP) |
| Async | Coroutines + Flow |
| Diagnostics | Firebase Crashlytics + Analytics, Timber |
| Build | AGP 9, Gradle 9, version catalog |
| Min / target SDK | 26 / 37 |

## Architecture

Single `ComponentActivity`, no fragments and no XML layouts. Unidirectional data flow: state travels
down as plain values, events travel up as lambdas.

```
MainActivity            enableEdgeToEdge + setContent
 └── DRApp              back stack (NavBackStack) + destination → screen mapping
      ├── StartScreen   history list, scanner dialog, camera permission
      ├── DetailsScreen read-only certificate form, copy / share
      └── SettingsScreen hand written (Compose has no PreferenceFragment equivalent)
```

Responsibilities are split three ways:

- **ViewModel** — business logic and screen state (`StartViewModel`: decode, parse, persist).
- **State holder** — UI logic that needs composition scope. `BarcodeScannerState` owns the camera and
  the detector, so they live exactly as long as the viewfinder is on screen; leaving the composition
  cancels the coroutine and releases the camera.
- **Composable** — drawing only. Every screen splits into a thin stateful wrapper and a stateless
  `…Content` that renders from plain parameters, which keeps `@Preview` usable throughout.

Data access goes through `CertificateRepository` (Room, exposed as a `StateFlow`) and
`SettingsRepository`.

Navigation destinations are `@Serializable` values in `ui/navigation/NavKeys.kt`, and the back stack
is an ordinary observable list — going back is `removeLastOrNull()`. `DetailsKey` carries the whole
certificate rather than a database id, because a scan is still shown when the user has turned history
off and there is no row to look up.

## Project layout

```
app/src/main/java/wkolendo/dowodyrejestracyjne/
├── models/            Certificate — Room entity + serialisable nav argument
├── repository/        certificate and settings repositories, Room database
├── ui/
│   ├── details/       certificate form, shared DetailField, clipboard/share text
│   ├── navigation/    NavKey destinations
│   ├── settings/      settings screen and its intents
│   ├── start/         history list
│   │   └── scan/      scanner UI + BarcodeScannerState (camera/ML Kit ownership)
│   ├── theme/         Material 3 color scheme, typography, shapes
│   └── DRApp.kt       back stack and destination mapping
└── utils/scanner/     Base64, BitReader, NRV2E decompressor (third-party, see Credits)
```

## Building

```bash
./gradlew assembleDebug
```

JDK 21 or newer is required (the module compiles against Java 21).

The build expects `app/google-services.json`, which is not in the repository. Create a Firebase
project, register an Android app with the package name `wkolendo.dowodyrejestracyjne` and drop the
generated file into `app/`. Nothing else needs configuring.

## Credits

The decoding sources in `utils/scanner/` are not my own work:

- **`NRV2EDecompressor.java`** — Bartosz Soja, ported from
  [nrv2e-csharp](https://bitbucket.org/bsoja/nrv2e-csharp), based on the
  [UCL](https://www.oberhumer.com/opensource/ucl/) library by Markus F.X.J. Oberhumer. GPL v3.
- **`BitReader.java`** — Bartosz Soja, derived from ZXing (Copyright 2008 ZXing authors).
- **`Base64.java`** — Emil Hernvall, derived from ZXing (Copyright 2007 ZXing authors).

## License

GNU General Public License v3.0 — see [LICENSE](LICENSE). The GPL is inherited from the bundled
NRV2E decompressor listed above.
