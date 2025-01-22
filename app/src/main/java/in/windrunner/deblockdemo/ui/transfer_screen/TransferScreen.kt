package `in`.windrunner.deblockdemo.ui.transfer_screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.windrunner.deblockdemo.CustomCurrencyAmount
import `in`.windrunner.deblockdemo.CustomCurrencyAmount.Companion.CURRENCY_ETH_CODE
import `in`.windrunner.deblockdemo.R
import `in`.windrunner.deblockdemo.forceKeyboardShow
import `in`.windrunner.deblockdemo.isDecimalCompatible
import `in`.windrunner.deblockdemo.swapCommaWithDot
import `in`.windrunner.deblockdemo.ui.theme.DeblockDemoTheme
import java.math.BigDecimal
import java.util.Currency

@Preview
@Composable
fun TransferScreenDefault() {
    DeblockDemoTheme {
        TransferScreenContent(
            transferCalcModel = TransferCalcModel(
                enteredAmount = CustomCurrencyAmount(
                    1998.toBigDecimal(),
                    Currency.getInstance("USD")
                ),
                selectedCurrency = Currency.getInstance("USD"),
                selectedCurrencyIconRes = R.drawable.ic_us,
                equivalentAmount = CustomCurrencyAmount(1.2.toBigDecimal(), CURRENCY_ETH_CODE),
                maxAvailableAmount = CustomCurrencyAmount(3450.toBigDecimal(), CURRENCY_ETH_CODE),
                transferFeeAmount = CustomCurrencyAmount(0.0013.toBigDecimal(), CURRENCY_ETH_CODE),
                isTransferAllowed = true
            ),
            onSelectedAmountChange = {},
            onSwapCurrencyClick = {},
            onTransferClick = {},
            onAmountEnterConfirmClick = {},
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 10.dp)
        )
    }
}

@Composable
fun TransferScreen(modifier: Modifier, viewModel: TransferScreenViewModel = hiltViewModel()) {
    val transferModel = viewModel.transferCalcModel.collectAsState()

    TransferScreenContent(
        transferCalcModel = transferModel.value,
        onSelectedAmountChange = viewModel::onAmountChange,
        onSwapCurrencyClick = viewModel::onSwapCurrencyClick,
        onTransferClick = viewModel::onTransferClick,
        onAmountEnterConfirmClick = viewModel::onTransferClick,
        modifier = modifier
    )
}

@Composable
private fun TransferScreenContent(
    transferCalcModel: TransferCalcModel?,
    onSelectedAmountChange: (BigDecimal?) -> Unit,
    onSwapCurrencyClick: () -> Unit,
    onTransferClick: () -> Unit,
    onAmountEnterConfirmClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // TODO: should add loading state for the TransferWidget?
            if (transferCalcModel != null) {
                TransferWidget(
                    transferCalcModel = transferCalcModel,
                    onSelectedAmountChange = onSelectedAmountChange,
                    onSwapCurrencyClick = onSwapCurrencyClick,
                    onAmountEnterConfirmClick = onAmountEnterConfirmClick,
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .align(Alignment.TopCenter)
                )
            }

            Button(
                enabled = transferCalcModel?.isTransferAllowed == true,
                onClick = onTransferClick,
                shape = RectangleShape,
                modifier = Modifier
                    .imePadding()
                    .padding(horizontal = 22.dp, vertical = 10.dp)
                    .height(56.dp)
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            ) {
                val label = if (transferCalcModel?.enteredAmount?.isCurrencyEth() == true) {
                    stringResource(
                        R.string.label_send_amount,
                        transferCalcModel.enteredAmount.getFormatted()
                    )
                } else {
                    stringResource(
                        R.string.label_send_amount_of_eth,
                        transferCalcModel?.enteredAmount?.getFormatted().orEmpty()
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
    onSelectedAmountChange: (BigDecimal?) -> Unit,
    onSwapCurrencyClick: () -> Unit,
    onAmountEnterConfirmClick: () -> Unit,
    modifier: Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.label_send_etherium),
            style = MaterialTheme.typography.titleLarge,
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
                onSwapCurrencyClick = onSwapCurrencyClick,
                onAmountEnterConfirmClick = onAmountEnterConfirmClick,
                modifier = Modifier.padding(horizontal = 22.dp)
            )

            SwapCurrenciesPairButton(
                onClick = onSwapCurrencyClick,
                modifier = Modifier.align(Alignment.CenterStart)
            )
        }

        TransferFeeLabel(
            transferCalcModel.transferFeeAmount?.getFormatted().orEmpty(),
            modifier = Modifier.padding(start = 22.dp, top = 15.dp, end = 22.dp)
        )
    }
}

@Composable
private fun Calculator(
    transferCalcModel: TransferCalcModel,
    onSelectedAmountChange: (BigDecimal?) -> Unit,
    onSwapCurrencyClick: () -> Unit,
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
                    color = Color.Gray,
                )
            }

            CurrencyPanel(
                transferCalcModel = transferCalcModel,
                onSwapCurrencyClick = onSwapCurrencyClick,
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
    amountEntered = transferCalcModel.enteredAmount.number
        .takeIf { it > 0.toBigDecimal() }
        ?.toString()
        .orEmpty()

    BasicTextField(
        value = amountEntered,
        onValueChange = { newValue ->
            val noCommaValue = newValue.swapCommaWithDot()
            if (noCommaValue.isDecimalCompatible()) {
                amountEntered = noCommaValue
                onSelectedAmountChange(noCommaValue.toBigDecimalOrNull())
            }
        },
        cursorBrush = SolidColor(Color.Transparent),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Decimal,
            imeAction = if (transferCalcModel.isTransferAllowed) {
                ImeAction.Send
            } else {
                ImeAction.None
            },
        ),
        keyboardActions = KeyboardActions(
            onSend = { onAmountEnterConfirmClick() }
        ),
        decorationBox = { innerTextField ->
            Row(
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = transferCalcModel.enteredAmount.currencySymbol,
                    style = MaterialTheme.typography.titleLarge,
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
    onSwapCurrencyClick: () -> Unit,
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
        Row(modifier = Modifier.weight(0.7f)) {
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

            FilledIconButton(
                shape = RectangleShape,
                colors = IconButtonDefaults.filledIconButtonColors().copy(
                    containerColor = Color.Transparent
                ),
                onClick = onSwapCurrencyClick,
                modifier = Modifier
                    .padding(start = 5.dp, top = 5.dp)
                    .size(15.dp)
                    .align(Alignment.Top)
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_drop_arrow),
                    contentDescription = "Currency selector"
                )
            }
        }
    }
}

@Composable
private fun SwapCurrenciesPairButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    FilledIconButton(
        shape = CircleShape,
        colors = IconButtonDefaults.filledIconButtonColors().copy(
            containerColor = MaterialTheme.colorScheme.surface
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
            contentDescription = "Transfer Arrows",
            tint = Color.Black,
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
            modifier = Modifier.padding(start = 5.dp)
        )
    }.takeIf { feeText.isNotEmpty() }
}