package wkolendo.dowodyrejestracyjne.ui.details

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import wkolendo.dowodyrejestracyjne.R

/**
 * One read-only field of the certificate: a label and its value, optionally with a leading icon.
 *
 * Rendered as a disabled [OutlinedTextField] to keep the look the old `TextInputLayout` had. The
 * disabled colours are overridden because the defaults grey the text out, and here the field is
 * read-only rather than unavailable — the value still has to be perfectly legible.
 */
@Composable
fun DetailField(
    @StringRes label: Int,
    value: String?,
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int? = null,
    onCopy: (() -> Unit)? = null,
) {
    // Nothing to copy from an empty field, so the button only shows when there is a value.
    val copyAction = onCopy?.takeUnless { value.isNullOrBlank() }

    OutlinedTextField(
        value = value.orEmpty(),
        onValueChange = {},
        modifier = modifier.fillMaxWidth(),
        enabled = false,
        readOnly = true,
        label = { Text(stringResource(label)) },
        leadingIcon = icon?.let { iconRes ->
            { Icon(painter = painterResource(iconRes), contentDescription = null) }
        },
        trailingIcon = copyAction?.let { action ->
            {
                // Compose has no inherited "disabled" the way View groups do, so this button stays
                // clickable even though the field itself is disabled.
                IconButton(onClick = action) {
                    Icon(
                        painter = painterResource(R.drawable.ic_copy_on_surface_24dp),
                        contentDescription = stringResource(R.string.details_copy),
                    )
                }
            }
        },
        shape = MaterialTheme.shapes.medium,
        colors = OutlinedTextFieldDefaults.colors(
            disabledTextColor = MaterialTheme.colorScheme.onSurface,
            disabledBorderColor = MaterialTheme.colorScheme.outline,
            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
        ),
    )
}

/**
 * Appends a unit to a value, e.g. "1560" -> "1560 kg".
 *
 * Blank values stay blank. The data binding version formatted unconditionally, so an empty field
 * used to render as a lone " kg".
 */
@Composable
fun String?.withUnit(@StringRes formatRes: Int): String? =
    takeUnless { it.isNullOrBlank() }?.let { stringResource(formatRes, it) }
