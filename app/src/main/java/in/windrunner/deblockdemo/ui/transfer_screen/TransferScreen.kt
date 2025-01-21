package `in`.windrunner.deblockdemo.ui.transfer_screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.windrunner.deblockdemo.CustomCurrencyAmount
import `in`.windrunner.deblockdemo.CustomCurrencyAmount.Companion.CURRENCY_ETH_CODE
import `in`.windrunner.deblockdemo.R
import `in`.windrunner.deblockdemo.ui.theme.DeblockDemoTheme
import java.util.Currency

@Preview
@Composable
fun TransferScreenDefault() {
    DeblockDemoTheme {
        TransferScreenContent(
            transferCalcModel = TransferCalcModel(
                selectedAmount = CustomCurrencyAmount(1998.toBigDecimal(), Currency.getInstance("USD")),
                selectedAmountIconRes = R.drawable.ic_us,
                equivalentAmount = CustomCurrencyAmount(1.2.toBigDecimal(), CURRENCY_ETH_CODE),
                maxAvailableAmount = CustomCurrencyAmount(3450.toBigDecimal(), CURRENCY_ETH_CODE),
                transferFeeAmount = CustomCurrencyAmount(0.0013.toBigDecimal(), CURRENCY_ETH_CODE),
            ),
            onSwapCurrencyClick = {},
            onTransferClick = {},
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
        onSwapCurrencyClick = viewModel::onSwapCurrencyClick,
        onTransferClick = viewModel::onTransferClick,
        modifier = modifier
    )
}

@Composable
private fun TransferScreenContent(
    transferCalcModel: TransferCalcModel?,
    onSwapCurrencyClick: () -> Unit,
    onTransferClick: () -> Unit,
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
                    onSwapCurrencyClick = onSwapCurrencyClick,
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .align(Alignment.TopCenter)
                )
            }

            Button(
                enabled = transferCalcModel?.transferFeeAmount != null,
                onClick = onTransferClick,
                shape = RectangleShape,
                modifier = Modifier
                    .padding(horizontal = 22.dp, vertical = 10.dp)
                    .height(56.dp)
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            ) {
                val label = if (transferCalcModel?.selectedAmount?.isCurrencyEth() == true) {
                    stringResource(
                        R.string.label_send_amount,
                        transferCalcModel.selectedAmount.getFormatted()
                    )
                } else {
                    stringResource(
                        R.string.label_send_amount_of_eth,
                        transferCalcModel?.selectedAmount?.getFormatted().orEmpty()
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
    onSwapCurrencyClick: () -> Unit,
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
                onSwapCurrencyClick = onSwapCurrencyClick,
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
    onSwapCurrencyClick: () -> Unit,
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
                Text(
                    text = transferCalcModel.selectedAmount.getFormatted(),
                    style = MaterialTheme.typography.titleLarge,
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
        Row(
            modifier = Modifier
                .weight(0.7f)
        ) {
            Image(
                painter = painterResource(transferCalcModel.selectedAmountIconRes),
                contentDescription = "Currency Flag",
                modifier = Modifier.size(24.dp)
            )

            Text(
                text = transferCalcModel.selectedAmount.currencyCode,
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