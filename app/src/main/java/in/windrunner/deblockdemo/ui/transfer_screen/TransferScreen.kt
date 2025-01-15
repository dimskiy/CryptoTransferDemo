package `in`.windrunner.deblockdemo.ui.transfer_screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.windrunner.deblockdemo.R
import `in`.windrunner.deblockdemo.ui.theme.DeblockDemoTheme

@Preview
@Composable
fun TransferScreenDefault() {
    DeblockDemoTheme {
        TransferScreenContent(
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 10.dp)
        )
    }
}

@Composable
fun TransferScreenContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.label_send_etherium),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(start = 20.dp, top = 20.dp)
        )

        Box(
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxWidth()
        ) {
            Calculator(modifier = Modifier.padding(horizontal = 22.dp))

            SwapCurrenciesPairButton(modifier = Modifier.align(Alignment.CenterStart))
        }
    }
}

@Composable
private fun Calculator(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .border(
                border = BorderStroke(color = Color.Gray, width = 2.dp),
                shape = RoundedCornerShape(8.dp)
            )

    ) {
        AmountsPanel(
            modifier = Modifier
                .padding(start = 10.dp)
                .weight(1f)
        )

        CurrencyPanel(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun AmountsPanel(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.wrapContentSize()
    ) {
        TextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("$ Amount") },
        )
        Text(
            text = "Equivalent in ETH",
            color = Color.Black.copy(alpha = 0.5f),
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
private fun CurrencyPanel(modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(start = 8.dp)
    ) {
        Text(
            text = "max 1200 ETH",
            color = Color.Blue,
            fontSize = 10.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                imageVector = Icons.Filled.Info,
                contentDescription = "USD Flag",
                modifier = Modifier.size(24.dp)
            )

            Text(
                text = "USD",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}

@Composable
private fun SwapCurrenciesPairButton(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(48.dp)
            .background(color = Color.Gray, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Build,
            contentDescription = "Transfer Arrows",
            tint = Color.Black,
            modifier = Modifier.size(24.dp)
        )
    }
}