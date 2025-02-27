package com.spendesk.grapes.compose.selectors

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.spendesk.grapes.compose.theme.GrapesTheme

/**
 * @author : dany
 * @since : 23/02/2023, Thu
 **/
@Composable
fun GrapesCheckboxText(
    text: String,
    onChecked: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    isChecked: Boolean = false,
    isEnabled: Boolean = true,
) {
    val onToggleCheck = { onChecked(!isChecked) }

    Row(
        modifier = modifier
            .clickable(
                enabled = isEnabled,
                onClickLabel = text,
                role = Role.Checkbox,
                onClick = onToggleCheck
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        GrapesCheckbox(
            isChecked = isChecked,
            isEnabled = isEnabled,
            onCheckedChange = { onToggleCheck.invoke() },
        )
        Spacer(Modifier.padding(end = GrapesTheme.dimensions.paddingSmall))
        Text(
            text = text,
            color = GrapesTheme.colors.mainComplementary,
            style = GrapesTheme.typography.bodyRegular
        )
    }
}

@Composable
@Preview(
    widthDp = 422,
    showBackground = true,
)
private fun GrapesCheckboxTextPreview() {
    GrapesTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            GrapesCheckboxText(text = "Checkbox selected and enabled", isChecked = true, isEnabled = true, onChecked = { Log.i("GrapesCheckboxText", "action checkbox clicked: $it") })
            GrapesCheckboxText(text = "Checkbox unselected and enabled", isChecked = false, isEnabled = true, onChecked = { Log.i("GrapesCheckboxText", "action checkbox clicked: $it") })
            GrapesCheckboxText(text = "Checkbox selected and disabled", isChecked = true, isEnabled = false, onChecked = { Log.i("GrapesCheckboxText", "action checkbox clicked: $it") })
            GrapesCheckboxText(text = "Checkbox unselected and disabled", isChecked = false, isEnabled = false, onChecked = { Log.i("GrapesCheckboxText", "action checkbox clicked: $it") })

            Spacer(modifier = Modifier.size(16.dp))

            var isChecked by remember { mutableStateOf(false) }

            GrapesCheckboxText(text = "Checkbox toggle", isChecked = isChecked, isEnabled = true, onChecked = { isChecked = it })
        }
    }
}
