package com.sentinel.deeptrace.ui.dashboard.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.sentinel.deeptrace.R
import com.sentinel.deeptrace.data.model.MarketData
import com.sentinel.deeptrace.ui.theme.SentinelDimens

@Composable
fun SentinelHeader(data: MarketData?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = SentinelDimens.HeaderVerticalPadding),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        StatusHeaderItem(
            label = stringResource(R.string.header_system),
            score = data?.systemScore ?: 0.0
        )
        StatusHeaderItem(
            label = stringResource(R.string.header_sp500),
            score = data?.sp500Score ?: 0.0
        )
        StatusHeaderItem(
            label = stringResource(R.string.header_nasdaq),
            score = data?.nasdaqScore ?: 0.0
        )
    }
}