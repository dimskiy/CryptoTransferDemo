package `in`.windrunner.cryptotransferdemo.domain.model

import java.math.BigDecimal

data class TransferModel(
    val ethAmountToSend: BigDecimal,
    val recipientWalletId: String = "",
    // Any other info required to make the transaction
)