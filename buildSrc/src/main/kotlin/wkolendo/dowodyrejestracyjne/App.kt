object App {
    const val ID = "wkolendo.dowodyrejestracyjne"

    const val COMPILE_SDK = 31

    const val MIN_SDK = 21

    const val TARGET_SDK = 31

    const val FLAVOR_DIMENSION = "default"

    const val VERSION = "1.0.2"

    const val CODE = 3

}

fun String.setupName(): String {
    return "[${App.CODE}][${App.VERSION}]DowodyRejestracyjne.apk"
}