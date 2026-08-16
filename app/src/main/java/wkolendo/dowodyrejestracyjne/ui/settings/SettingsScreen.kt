package wkolendo.dowodyrejestracyjne.ui.settings

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import wkolendo.dowodyrejestracyjne.BuildConfig
import wkolendo.dowodyrejestracyjne.R
import wkolendo.dowodyrejestracyjne.models.ThemeMode
import wkolendo.dowodyrejestracyjne.repository.CertificateRepository
import wkolendo.dowodyrejestracyjne.repository.SettingsRepository
import wkolendo.dowodyrejestracyjne.ui.theme.DRTheme
import wkolendo.dowodyrejestracyjne.utils.Screen
import wkolendo.dowodyrejestracyjne.utils.TrackScreen

/**
 * Settings, hand written because Compose has no counterpart to PreferenceFragmentCompat and
 * `res/xml/settings.xml`.
 *
 * Stateful wrapper: reads the repository and turns the two "open something" rows into intents.
 */
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TrackScreen(Screen.SETTINGS)

    val context = LocalContext.current
    val saveScans by SettingsRepository.saveScans.collectAsStateWithLifecycle()
    val themeMode by SettingsRepository.themeMode.collectAsStateWithLifecycle()

    SettingsContent(
        saveScans = saveScans,
        themeMode = themeMode,
        onThemeModeChange = SettingsRepository::setThemeMode,
        appVersion = BuildConfig.VERSION_NAME,
        onBack = onBack,
        onSaveScansChange = SettingsRepository::setSaveScans,
        onClearHistory = CertificateRepository::deleteCertificates,
        onContactClick = { context.openEmailChooser() },
        onVersionClick = { context.openGooglePlay() },
        modifier = modifier,
    )
}

/**
 * The confirmation dialog's visibility is local UI state and stays here; everything the app has to
 * act on is hoisted to the caller.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsContent(
    saveScans: Boolean,
    themeMode: ThemeMode,
    onThemeModeChange: (ThemeMode) -> Unit,
    onBack: () -> Unit,
    appVersion: String,
    onSaveScansChange: (Boolean) -> Unit,
    onClearHistory: () -> Unit,
    onContactClick: () -> Unit,
    onVersionClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showClearHistoryDialog by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings_settings)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(R.drawable.ic_chevron_left_24dp),
                            contentDescription = stringResource(R.string.action_back),
                        )
                    }
                },
            )
        },
    ) { contentPadding ->
        SettingsList(
            saveScans = saveScans,
            themeMode = themeMode,
            onThemeModeChange = onThemeModeChange,
            appVersion = appVersion,
            onSaveScansChange = onSaveScansChange,
            onClearHistoryClick = { showClearHistoryDialog = true },
            onContactClick = onContactClick,
            onVersionClick = onVersionClick,
            modifier = Modifier.padding(contentPadding),
        )
    }

    if (showClearHistoryDialog) {
        TrackScreen(Screen.DIALOG_CLEAR_HISTORY)
        ClearHistoryDialog(
            onConfirm = {
                showClearHistoryDialog = false
                onClearHistory()
            },
            onDismiss = { showClearHistoryDialog = false },
        )
    }
}

@Composable
private fun SettingsList(
    saveScans: Boolean,
    themeMode: ThemeMode,
    onThemeModeChange: (ThemeMode) -> Unit,
    appVersion: String,
    onSaveScansChange: (Boolean) -> Unit,
    onClearHistoryClick: () -> Unit,
    onContactClick: () -> Unit,
    onVersionClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        CategoryHeader(stringResource(R.string.settings_scanner))

        SettingsItem(
            icon = R.drawable.ic_floppy_disk_on_surface_24dp,
            title = stringResource(R.string.settings_save_scans),
            summary = stringResource(R.string.settings_save_scans_summary),
            onClick = { onSaveScansChange(!saveScans) },
            trailing = { Switch(checked = saveScans, onCheckedChange = onSaveScansChange) },
        )

        SettingsItem(
            icon = R.drawable.ic_trash_can_clock_on_surface_24dp,
            title = stringResource(R.string.settings_clear_history),
            summary = stringResource(R.string.settings_clear_history_summary),
            onClick = onClearHistoryClick,
        )

        HorizontalDivider()
        CategoryHeader(stringResource(R.string.settings_appearance))
        ThemeModeGroup(selected = themeMode, onSelect = onThemeModeChange)

        HorizontalDivider()
        CategoryHeader(stringResource(R.string.settings_about))

        SettingsItem(
            icon = R.drawable.ic_envelope_on_surface_24dp,
            title = stringResource(R.string.settings_contact),
            summary = stringResource(R.string.settings_contact_summary),
            onClick = onContactClick,
        )

        SettingsItem(
            icon = R.drawable.ic_code_on_surface_24dp,
            title = stringResource(R.string.settings_app_version),
            summary = stringResource(R.string.settings_app_version_summary, appVersion),
            onClick = onVersionClick,
        )
    }
}

/**
 * Radio group for the theme. The whole row is the touch target and carries the RadioButton role, so
 * the button itself takes no click of its own — that is the accessibility pattern for these lists.
 */
@Composable
private fun ThemeModeGroup(
    selected: ThemeMode,
    onSelect: (ThemeMode) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.selectableGroup()) {
        ThemeModeItem(R.drawable.ic_circle_half_stroke_24dp, R.string.settings_theme_system, ThemeMode.SYSTEM, selected, onSelect)
        ThemeModeItem(R.drawable.ic_sun_24dp, R.string.settings_theme_light, ThemeMode.LIGHT, selected, onSelect)
        ThemeModeItem(R.drawable.ic_moon_24dp, R.string.settings_theme_dark, ThemeMode.DARK, selected, onSelect)
    }
}

@Composable
private fun ThemeModeItem(
    @DrawableRes icon: Int,
    @StringRes title: Int,
    mode: ThemeMode,
    selected: ThemeMode,
    onSelect: (ThemeMode) -> Unit,
) {
    val isSelected = mode == selected
    ListItem(
        headlineContent = { Text(stringResource(title)) },
        leadingContent = {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        },
        trailingContent = { RadioButton(selected = isSelected, onClick = null) },
        modifier = Modifier.selectable(
            selected = isSelected,
            role = Role.RadioButton,
            onClick = { onSelect(mode) },
        ),
    )
}

@Composable
private fun CategoryHeader(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
    )
}

@Composable
private fun SettingsItem(
    @DrawableRes icon: Int,
    title: String,
    summary: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    trailing: @Composable (() -> Unit)? = null,
) {
    ListItem(
        headlineContent = { Text(title) },
        supportingContent = { Text(summary) },
        leadingContent = {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        },
        trailingContent = trailing,
        modifier = modifier.clickable(onClick = onClick),
    )
}

@Composable
private fun ClearHistoryDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.settings_clear_history)) },
        text = { Text(stringResource(R.string.settings_clear_history_message)) },
        confirmButton = {
            TextButton(onClick = onConfirm) { Text(stringResource(R.string.settings_clear_history_confirm)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.action_cancel)) }
        },
    )
}

@Preview(name = "Ustawienia", showBackground = true)
@Composable
private fun SettingsScreenPreview() {
    DRTheme(dynamicColor = false) {
        SettingsContent(
            saveScans = true,
            themeMode = ThemeMode.SYSTEM,
            onThemeModeChange = {},
            appVersion = "1.1.0",
            onBack = {},
            onSaveScansChange = {},
            onClearHistory = {},
            onContactClick = {},
            onVersionClick = {},
        )
    }
}

@Preview(name = "Ustawienia — ciemny", showBackground = true)
@Composable
private fun SettingsScreenDarkPreview() {
    DRTheme(darkTheme = true, dynamicColor = false) {
        SettingsContent(
            saveScans = false,
            themeMode = ThemeMode.DARK,
            onThemeModeChange = {},
            appVersion = "1.1.0",
            onBack = {},
            onSaveScansChange = {},
            onClearHistory = {},
            onContactClick = {},
            onVersionClick = {},
        )
    }
}
