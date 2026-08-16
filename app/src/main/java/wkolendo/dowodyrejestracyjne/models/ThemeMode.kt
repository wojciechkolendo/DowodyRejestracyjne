package wkolendo.dowodyrejestracyjne.models

/**
 * How the app decides between the light and dark colour scheme.
 *
 * Persisted by name rather than by ordinal, so reordering the entries cannot silently change what
 * an existing user has chosen.
 */
enum class ThemeMode {
    SYSTEM,
    LIGHT,
    DARK,
}
