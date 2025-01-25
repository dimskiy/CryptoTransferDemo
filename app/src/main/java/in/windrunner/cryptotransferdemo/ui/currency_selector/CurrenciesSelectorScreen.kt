package `in`.windrunner.cryptotransferdemo.ui.currency_selector

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import `in`.windrunner.cryptotransferdemo.R
import `in`.windrunner.cryptotransferdemo.getError
import `in`.windrunner.cryptotransferdemo.ofEtherium
import `in`.windrunner.cryptotransferdemo.ofFiatCurrency
import `in`.windrunner.cryptotransferdemo.ui.theme.CryptoTransferDemoTheme
import java.util.Currency

@Preview
@Composable
fun CurrencyBottomSheetPreview() {
    CryptoTransferDemoTheme {
        CurrenciesSelector(
            currencyEntries = listOf(
                SelectableCurrencyItem(
                    Currency.getInstance("USD"),
                    R.drawable.ic_us,
                    0.003.toBigDecimal().ofEtherium(),
                    1.0.toBigDecimal().ofFiatCurrency(Currency.getInstance("USD"))
                ),
                SelectableCurrencyItem(
                    Currency.getInstance("EUR"),
                    R.drawable.ic_eu,
                    0.004.toBigDecimal().ofEtherium(),
                    1.2.toBigDecimal().ofFiatCurrency(Currency.getInstance("USD")),
                    isSelected = true
                ),
                SelectableCurrencyItem(
                    Currency.getInstance("GBP"),
                    R.drawable.ic_uk,
                    0.005.toBigDecimal().ofEtherium(),
                    1.3.toBigDecimal().ofFiatCurrency(Currency.getInstance("USD"))
                )
            ),
            onCurrencySelected = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrenciesSelectorScreen(
    onError: (Throwable) -> Unit,
    navController: NavController,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    viewModel: CurrenciesSelectorViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val currenciesState = viewModel.currenciesList.collectAsState()

    ModalBottomSheet(
        onDismissRequest = { navController.popBackStack() },
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
        modifier = modifier
    ) {
        val currenciesListResult = currenciesState.value

        when {
            currenciesListResult == null -> {
                LoadingIndicator()
            }

            currenciesListResult.isSuccess -> currenciesListResult.getOrNull()?.let {
                CurrenciesSelector(
                    currencyEntries = it,
                    onCurrencySelected = {
                        viewModel.onCurrencySelected(it)
                        navController.popBackStack()
                    },
                    modifier = Modifier
                )
            }

            else -> {
                onError(currenciesListResult.getError())
                navController.popBackStack()
            }
        }
    }
}

@Composable
private fun LoadingIndicator() {
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(horizontal = 50.dp, vertical = 10.dp)
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 5.dp,
            modifier = Modifier.size(80.dp)
        )
    }
}

@Composable
private fun CurrenciesSelector(
    currencyEntries: List<SelectableCurrencyItem>,
    onCurrencySelected: (Currency) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp)
    ) {
        Text(
            text = stringResource(R.string.displayed_currency),
            style = MaterialTheme.typography.titleMedium,
            fontSize = 20.sp,
            modifier = Modifier.fillMaxWidth(),
        )

        LazyColumn(
            modifier = Modifier.padding(top = 32.dp),
        ) {
            items(currencyEntries) { item ->
                CurrencyListItem(
                    item = item,
                    onCurrencySelected = onCurrencySelected,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(18.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Info,
                contentDescription = "",
                modifier = Modifier
                    .size(20.dp)
                    .weight(0.2f)
            )

            Column(modifier = Modifier.weight(0.8f)) {
                Text(
                    text = stringResource(R.string.info_currency_eth_sending),
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 13.sp,
                )
            }
        }
    }
}

@Composable
private fun CurrencyListItem(
    item: SelectableCurrencyItem,
    onCurrencySelected: (Currency) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shadowElevation = 1.dp,
        shape = RoundedCornerShape(size = 6.dp),
        border = if (item.isSelected) {
            BorderStroke(width = 0.5.dp, color = MaterialTheme.colorScheme.onSurface)
        } else BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.surfaceVariant),
        modifier = modifier
            .fillMaxWidth()
            .clickable { onCurrencySelected(item.currency) }
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = item.currencyFlagRes),
                    contentDescription = "",
                    modifier = Modifier.size(32.dp)
                )

                Column(
                    modifier = Modifier.padding(start = 16.dp)
                ) {
                    Text(
                        text = item.currency.displayName,
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Text(
                        text = item.currency.currencyCode,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = item.ethAmount.getFormatted(),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.End
                )
                Text(
                    text = item.baseFiatCurrencyAmount.getFormatted(),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp,
                    textAlign = TextAlign.End
                )
            }
        }
    }
}