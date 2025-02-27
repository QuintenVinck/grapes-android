package com.spendesk.grapes.compose.textfield

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import com.spendesk.grapes.compose.theme.GrapesTheme

/**
 * @author jean-philippe
 * @since 06/01/2023, Fri
 **/

@Composable
@ExperimentalMaterial3Api
@Suppress("LongParameterList")
internal fun GrapesBaseTextField(
    value: String,
    placeholderValue: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    helperText: String? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = GrapesTheme.typography.bodyRegular,
    textPadding: PaddingValues = GrapesTextFieldDefaults.textFieldPadding(),
    isError: Boolean = false,
    onClick: (() -> Unit)? = null,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    singleLine: Boolean = false,
    colors: GrapesTextFieldColors = GrapesTextFieldDefaults.textFieldColors(),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    /**
     * Mapping from string to TextFieldValue copy-pasted from string version of [androidx.compose.foundation.text.BasicTextField]
     * The code style has been kept as it is in androidx source code on purpose, to be able to compare it easily.
     */

    // Holds the latest internal TextFieldValue state. We need to keep it to have the correct value
    // of the composition.
    var textFieldValueState by remember { mutableStateOf(TextFieldValue(text = value)) }
    // Holds the latest TextFieldValue that BasicTextField was recomposed with. We couldn't simply
    // pass `TextFieldValue(text = value)` to the CoreTextField because we need to preserve the
    // composition.
    val textFieldValue = textFieldValueState.copy(text = value)

    SideEffect {
        if (textFieldValue.selection != textFieldValueState.selection ||
            textFieldValue.composition != textFieldValueState.composition
        ) {
            textFieldValueState = textFieldValue
        }
    }
    // Last String value that either text field was recomposed with or updated in the onValueChange
    // callback. We keep track of it to prevent calling onValueChange(String) for same String when
    // CoreTextField's onValueChange is called multiple times without recomposition in between.
    var lastTextValue by remember(value) { mutableStateOf(value) }

    SideEffect {
        if (textFieldValueState.text != lastTextValue) {
            textFieldValueState = textFieldValueState.copy(text = value)
        }
    }

    GrapesBaseTextField(
        value = textFieldValueState,
        placeholderValue = placeholderValue,
        onValueChange = { newTextFieldValueState ->
            textFieldValueState = newTextFieldValueState

            val stringChangedSinceLastInvocation = lastTextValue != newTextFieldValueState.text
            lastTextValue = newTextFieldValueState.text

            if (stringChangedSinceLastInvocation) {
                onValueChange(newTextFieldValueState.text)
            }
        },
        modifier = modifier,
        helperText = helperText,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        textPadding = textPadding,
        isError = isError,
        onClick = onClick,
        keyboardActions = keyboardActions,
        keyboardOptions = keyboardOptions,
        singleLine = singleLine,
        colors = colors,
        visualTransformation = visualTransformation,
        interactionSource = interactionSource,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
    )
}

@Composable
@ExperimentalMaterial3Api
@Suppress("LongParameterList")
internal fun GrapesBaseTextField(
    value: TextFieldValue,
    placeholderValue: String,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    helperText: String? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = GrapesTheme.typography.bodyRegular,
    textPadding: PaddingValues = GrapesTextFieldDefaults.textFieldPadding(),
    isError: Boolean = false,
    onClick: (() -> Unit)? = null,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    singleLine: Boolean = false,
    colors: GrapesTextFieldColors = GrapesTextFieldDefaults.textFieldColors(),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) {

    val textColor = colors.textColor(enabled).value
    val mergedTextStyle = textStyle.merge(TextStyle(color = textColor))

    if (onClick != null && readOnly) {
        LaunchedEffect(interactionSource) {
            interactionSource.interactions.collect { interaction ->
                if (interaction is PressInteraction.Release) {
                    onClick.invoke()
                }
            }
        }
    }

    Column(
        modifier = modifier.width(IntrinsicSize.Min)
    ) {
        BasicTextField(
            enabled = enabled,
            readOnly = readOnly,
            value = value,
            onValueChange = onValueChange,
            textStyle = mergedTextStyle,
            cursorBrush = SolidColor(colors.cursorColor(isError).value),
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            interactionSource = interactionSource,
            singleLine = singleLine,
            decorationBox = @Composable { innerTextField ->
                GrapesBasicTextFieldDecorationBox(
                    value = value,
                    innerTextField = innerTextField,
                    enabled = enabled,
                    singleLine = singleLine,
                    visualTransformation = visualTransformation,
                    interactionSource = interactionSource,
                    placeholderValue = placeholderValue,
                    leadingIcon = leadingIcon,
                    trailingIcon = trailingIcon,
                    isError = isError,
                    colors = colors,
                    contentPadding = textPadding,
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(
                    minWidth = GrapesTextFieldDefaults.MinWidth,
                    minHeight = GrapesTextFieldDefaults.MinHeight,
                )
                .shadow(
                    elevation = GrapesTextFieldDefaults.Elevation,
                    shape = GrapesTextFieldDefaults.TextFieldShape,
                )
                .background(
                    color = colors.backgroundColor(enabled).value,
                    shape = GrapesTextFieldDefaults.TextFieldShape
                ),
        )

        if (!helperText.isNullOrEmpty()) {
            GrapesHelperText(
                text = helperText,
                modifier = Modifier.fillMaxWidth(),
                enabled = enabled,
                isError = isError
            )
        }
    }
}

@Composable
@Suppress("LongParameterList")
internal fun GrapesHelperText(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    textStyle: TextStyle = GrapesTheme.typography.bodyXs,
    isError: Boolean = false,
    colors: GrapesTextFieldColors = GrapesTextFieldDefaults.textFieldColors(),
    contentPadding: PaddingValues = GrapesTextFieldDefaults.textFieldPadding(),
) {
    val textColor = colors.helperTextColor(enabled, isError).value
    val mergedTextStyle = textStyle.merge(TextStyle(color = textColor))

    val layoutDirection = LocalLayoutDirection.current

    val topPadding = contentPadding.calculateTopPadding()
    val endPadding = contentPadding.calculateEndPadding(layoutDirection)
    val startPadding = contentPadding.calculateStartPadding(layoutDirection)

    Box(
        modifier = modifier
            .padding(start = startPadding, end = endPadding),
        propagateMinConstraints = true,
    ) {
        Text(
            text = text,
            modifier = Modifier
                .paddingFromBaseline(top = topPadding),
            style = mergedTextStyle,
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
@Suppress("LongParameterList")
private fun GrapesBasicTextFieldDecorationBox(
    value: TextFieldValue,
    placeholderValue: String,
    contentPadding: PaddingValues,
    enabled: Boolean,
    isError: Boolean,
    singleLine: Boolean,
    colors: GrapesTextFieldColors,
    visualTransformation: VisualTransformation,
    interactionSource: MutableInteractionSource,
    innerTextField: @Composable () -> Unit,
    leadingIcon: @Composable (() -> Unit)?,
    trailingIcon: @Composable (() -> Unit)?,
) {
    val styledLeadingIcon: (@Composable () -> Unit)? = leadingIcon?.let {
        @Composable {
            val contentColor by colors.leadingIconColor(
                enabled = enabled,
                isError = isError,
            )
            CompositionLocalProvider(
                LocalContentColor provides contentColor,
                content = leadingIcon,
            )
        }
    }

    val styledTrailingIcon: (@Composable () -> Unit)? = trailingIcon?.let {
        @Composable {
            val contentColor by colors.trailingIconColor(
                enabled = enabled,
                isError = isError,
            )
            CompositionLocalProvider(
                LocalContentColor provides contentColor,
                content = trailingIcon,
            )
        }
    }

    OutlinedTextFieldDefaults.DecorationBox(
        value = value.text,
        innerTextField = innerTextField,
        enabled = enabled,
        singleLine = singleLine,
        visualTransformation = visualTransformation,
        interactionSource = interactionSource,
        contentPadding = contentPadding,
        placeholder = {
            Text(
                text = placeholderValue,
                style = GrapesTheme.typography.bodyRegular,
                color = colors.placeholderColor(enabled = enabled).value,
            )
        },
        leadingIcon = styledLeadingIcon,
        trailingIcon = styledTrailingIcon,
        container = {
            GrapesTextFieldDefaults.BorderBox(
                enabled = enabled,
                isError = isError,
                interactionSource = interactionSource,
                colors = colors
            )
        },
    )
}
