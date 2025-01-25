package `in`.windrunner.cryptotransferdemo.ui.transfer_screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import `in`.windrunner.cryptotransferdemo.CustomCurrencyAmount
import `in`.windrunner.cryptotransferdemo.CustomCurrencyAmount.Companion.CURRENCY_ETH_CODE
import `in`.windrunner.cryptotransferdemo.R
import `in`.windrunner.cryptotransferdemo.forceKeyboardShow
import `in`.windrunner.cryptotransferdemo.isDecimalCompatible
import `in`.windrunner.cryptotransferdemo.ofEtherium
import `in`.windrunner.cryptotransferdemo.swapCommaWithDot
import `in`.windrunner.cryptotransferdemo.ui.NavScreen
import `in`.windrunner.cryptotransferdemo.ui.theme.CryptoTransferDemoTheme
import java.math.BigDecimal
import java.util.Currency

@Preview
@Composable
fun TransferScreenDefault() {
    CryptoTransferDemoTheme {
        TransferScreenContent(
            transferCalcModel = TransferCalcModel(
                baseAmount = CustomCurrencyAmount(
                    1998.toBigDecimal(),
                    Currency.getInstance("USD")
                ),
                selectedCurrency = Currency.getInstance("USD"),
                selectedCurrencyIconRes = R.drawable.ic_us,
                equivalentAmount = CustomCurrencyAmount(1.2.toBigDecimal(), CURRENCY_ETH_CODE),
                maxAvailableAmount = CustomCurrencyAmount(3450.toBigDecimal(), CURRENCY_ETH_CODE),
                isTransferAllowed = true
            ),
            transferFeeModel = 0.0.toBigDecimal().ofEtherium(),
            onSelectedAmountChange = {},
            onSwapCurrencyClick = {},
            onChangeCurrencyClick = {},
            onTransferClick = {},
            onAmountEnterConfirmClick = {},
            isContentEnabled = true,
        )
    }
}

@Composable
fun TransferScreen(
    onErrorDisplay: (Throwable) -> Unit,
    viewModel: TransferScreenViewModel = hiltViewModel(),
    navController: NavController,
    modifier: Modifier,
) {
    val transferModel = viewModel.transferCalcModel.collectAsState()
    var transferModelCache by remember { mutableStateOf<Result<TransferCalcModel>?>(null) }

    val transferFeeModel = viewModel.transferFee.collectAsState()
    transferFeeModel.value?.exceptionOrNull()?.let(onErrorDisplay::invoke)

    val isContentEnabled = transferModel.value?.isSuccess == true
    transferModel.value?.takeIf(Result<*>::isSuccess)?.let { transferModelCache = it }

    transferModelCache?.getOrNull()?.let {
        TransferScreenContent(
            transferCalcModel = it,
            transferFeeModel = transferFeeModel.value?.getOrNull(),
            onSelectedAmountChange = viewModel::onAmountChange,
            onSwapCurrencyClick = viewModel::onSwapCurrencyClick,
            onChangeCurrencyClick = {
                navController.navigate(NavScreen.CurrencySelector.route)
            },
            onTransferClick = { onErrorDisplay(Exception("Let's imagine ETH was sent :)")) },
            onAmountEnterConfirmClick = { onErrorDisplay(Exception("Let's imagine ETH was sent :)")) },
            isContentEnabled = isContentEnabled,
            modifier = modifier
        )
    }
    transferModel.value?.exceptionOrNull()?.let(onErrorDisplay::invoke)
}

@Composable
private fun TransferScreenContent(
    transferCalcModel: TransferCalcModel?,
    transferFeeModel: CustomCurrencyAmount?,
    onSelectedAmountChange: (BigDecimal?) -> Unit,
    onSwapCurrencyClick: () -> Unit,
    onChangeCurrencyClick: () -> Unit,
    onTransferClick: () -> Unit,
    onAmountEnterConfirmClick: () -> Unit,
    isContentEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            if (transferCalcModel != null) {
                TransferWidget(
                    transferCalcModel = transferCalcModel,
                    transferFeeModel = transferFeeModel,
                    onSelectedAmountChange = onSelectedAmountChange,
                    onSwapCurrencyClick = onSwapCurrencyClick,
                    onChangeCurrencyClick = onChangeCurrencyClick,
                    onAmountEnterConfirmClick = onAmountEnterConfirmClick,
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .align(Alignment.TopCenter)
                )
            }

            Button(
                enabled = isContentEnabled && transferCalcModel?.isTransferAllowed == true,
                onClick = onTransferClick,
                shape = RectangleShape,
                modifier = Modifier
                    .imePadding()
                    .padding(horizontal = 22.dp, vertical = 10.dp)
                    .height(56.dp)
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            ) {
                val label = if (transferCalcModel?.baseAmount?.isCurrencyEth() == true) {
                    stringResource(
                        R.string.label_send_amount,
                        transferCalcModel.baseAmount.getFormatted()
                    )
                } else {
                    stringResource(
                        R.string.label_send_amount_of_eth,
                        transferCalcModel?.baseAmount?.getFormatted().orEmpty()
                    )
                }

                Text(text = label)
            }
        }
    }
}

@Composable
private fun TransferWidget(
    transferCalcModel: TransferCalcModel,
    transferFeeModel: CustomCurrencyAmount?,
    onSelectedAmountChange: (BigDecimal?) -> Unit,
    onSwapCurrencyClick: () -> Unit,
    onChangeCurrencyClick: () -> Unit,
    onAmountEnterConfirmClick: () -> Unit,
    modifier: Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.label_send_etherium),
            style = MaterialTheme.typography.titleLarge,
            fontSize = 32.sp,
            fontWeight = FontWeight.W500,
            modifier = Modifier.padding(start = 25.dp)
        )

        Box(
            modifier = Modifier
                .padding(top = 30.dp)
                .fillMaxWidth()
        ) {
            Calculator(
                transferCalcModel = transferCalcModel,
                onSelectedAmountChange = onSelectedAmountChange,
                onChangeCurrencyClick = onChangeCurrencyClick,
                onAmountEnterConfirmClick = onAmountEnterConfirmClick,
                modifier = Modifier.padding(horizontal = 22.dp)
            )

            SwapCurrenciesPairButton(
                onClick = onSwapCurrencyClick,
                modifier = Modifier.align(Alignment.CenterStart)
            )
        }

        TransferFeeLabel(
            transferFeeModel?.getFormatted().orEmpty(),
            modifier = Modifier.padding(start = 22.dp, top = 15.dp, end = 22.dp)
        )
    }
}

@Composable
private fun Calculator(
    transferCalcModel: TransferCalcModel,
    onSelectedAmountChange: (BigDecimal?) -> Unit,
    onChangeCurrencyClick: () -> Unit,
    onAmountEnterConfirmClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shadowElevation = 2.dp,
        border = BorderStroke(color = Color.LightGray, width = 1.dp),
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.width(350.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 35.dp, top = 25.dp, bottom = 25.dp)
                    .weight(0.6f)
            ) {
                AmountEnterField(
                    transferCalcModel = transferCalcModel,
                    onSelectedAmountChange = onSelectedAmountChange,
                    onAmountEnterConfirmClick = onAmountEnterConfirmClick
                )

                Text(
                    text = transferCalcModel.equivalentAmount.getFormatted(),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            CurrencyPanel(
                transferCalcModel = transferCalcModel,
                onChangeCurrencyClick = onChangeCurrencyClick,
                modifier = Modifier
                    .padding(top = 10.dp)
                    .weight(0.4f)
            )
        }
    }
}

@Composable
private fun AmountEnterField(
    transferCalcModel: TransferCalcModel,
    onSelectedAmountChange: (BigDecimal?) -> Unit,
    onAmountEnterConfirmClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var amountEntered by remember { mutableStateOf("") }
    amountEntered = transferCalcModel.baseAmount.number.toString()

    BasicTextField(
        value = TextFieldValue(amountEntered, TextRange(amountEntered.length)),
        onValueChange = { newValue ->
            val noCommaValue = newValue.text.swapCommaWithDot()
            if (noCommaValue.isDecimalCompatible()) {
                amountEntered = noCommaValue
                onSelectedAmountChange(noCommaValue.toBigDecimalOrNull())
            }
        },
        cursorBrush = SolidColor(Color.Transparent),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Decimal,
            imeAction = ImeAction.Send,
        ),
        keyboardActions = KeyboardActions(
            onSend = { onAmountEnterConfirmClick() }
        ),
        decorationBox = { innerTextField ->
            Row(
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = transferCalcModel.baseAmount.currencySymbol,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.W500,
                    modifier = Modifier.padding(end = 5.dp)
                )
                if (amountEntered.isEmpty()) {
                    Text(
                        text = "0",
                        style = MaterialTheme.typography.titleLarge,
                    )
                } else {
                    innerTextField()
                }
            }
        },
        readOnly = false,
        textStyle = MaterialTheme.typography.titleLarge,
        modifier = modifier
            .forceKeyboardShow()
            .onKeyEvent { keyEvent ->
                if (keyEvent.type == KeyEventType.KeyUp && keyEvent.key == Key.Backspace) {
                    amountEntered = ""
                    onSelectedAmountChange(0.toBigDecimal())
                    true
                } else {
                    false
                }
            },
    )
}

@Composable
private fun CurrencyPanel(
    transferCalcModel: TransferCalcModel,
    onChangeCurrencyClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .height(80.dp)
            .wrapContentWidth(),
    ) {
        Text(
            text = stringResource(
                R.string.label_max_amount,
                transferCalcModel.maxAvailableAmount.getFormatted()
            ),
            style = MaterialTheme.typography.labelMedium,
            color = Color.Blue,
            modifier = Modifier
                .weight(0.3f)
                .padding(bottom = 4.dp)
        )
        Row(
            modifier = Modifier
                .weight(0.7f)
                .clickable { onChangeCurrencyClick() }
        ) {
            Box(modifier = Modifier.size(24.dp)) {
                transferCalcModel.selectedCurrencyIconRes?.let {
                    Image(
                        painter = painterResource(it),
                        contentDescription = "Currency Flag"
                    )
                }
            }

            Text(
                text = transferCalcModel.selectedCurrency.currencyCode,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 10.dp)
            )

            Image(
                painter = painterResource(R.drawable.ic_drop_arrow),
                contentDescription = "",
                modifier = Modifier
                    .padding(start = 5.dp, top = 5.dp)
                    .size(12.dp)
                    .align(Alignment.Top)
            )
        }
    }
}

@Composable
private fun SwapCurrenciesPairButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilledIconButton(
        shape = CircleShape,
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        onClick = onClick,
        modifier = modifier
            .size(48.dp)
            .border(
                border = BorderStroke(color = Color.LightGray, width = 1.dp),
                shape = CircleShape
            )
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_arrows),
            contentDescription = "",
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
private fun TransferFeeLabel(feeText: String, modifier: Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.ic_gas),
            contentDescription = "Transfer fee icon",
            modifier = Modifier.size(11.dp)
        )

        Text(
            text = stringResource(R.string.label_est_network_fees, feeText),
            style = MaterialTheme.typography.labelSmall,
            fontSize = 12.sp,
            modifier = Modifier.padding(start = 5.dp)
        )
    }.takeIf { feeText.isNotEmpty() }
}