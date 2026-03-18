package com.sentinel.deeptrace.ui.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.sentinel.deeptrace.R
import com.sentinel.deeptrace.ui.dashboard.components.*
import com.sentinel.deeptrace.ui.dashboard.dialogs.*
import com.sentinel.deeptrace.ui.theme.*
import com.sentinel.deeptrace.data.model.WatchlistItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SentinelScreen(viewModel: SentinelViewModel) {
    var isExpanded by remember { mutableStateOf(false) }
    var showAddDialog by remember { mutableStateOf(false) }
    var itemToEdit: WatchlistItem? by remember { mutableStateOf(null) }

    val data = viewModel.marketData
    val allWatchlistItems by viewModel.watchlist.collectAsState()

    val systemHedges = allWatchlistItems.filter { it.isPermanent }
    val userWatchlist = allWatchlistItems.filter { !it.isPermanent }

    Scaffold(
        containerColor = SentinelBackground,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.app_name).uppercase(),
                        color = SentinelBlue,
                        style = MaterialTheme.typography.labelLarge
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = SentinelBackground)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = SentinelBlue,
                contentColor = Color.White,
                shape = MaterialTheme.shapes.large
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.desc_add_asset)
                )
            }
        }
    ) { innerPadding ->

        if (showAddDialog) {
            AddStockDialog(
                onDismiss = { showAddDialog = false },
                onConfirm = { symbol, name ->
                    viewModel.addStock(symbol, name)
                    showAddDialog = false
                }
            )
        }

        itemToEdit?.let { currentItem ->
            EditStockDialog(
                item = currentItem,
                onDismiss = { itemToEdit = null },
                onConfirm = { newSymbol, newName ->
                    viewModel.updateStock(currentItem, newName, newSymbol)
                    itemToEdit = null
                }
            )
        }

        if (data == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = SentinelBlue)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = SentinelDimens.ScreenPadding)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = SentinelDimens.HeaderVerticalPadding),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    StatusHeaderItem(stringResource(R.string.header_system), data.systemScore)
                    StatusHeaderItem(stringResource(R.string.header_sp500), data.sp500Score)
                    StatusHeaderItem(stringResource(R.string.header_nasdaq), data.nasdaqScore)
                }

                HorizontalDivider(color = SentinelDivider)

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = SentinelDimens.SpacingLarge)
                        .clickable { isExpanded = !isExpanded },
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(containerColor = SentinelCardBlue)
                ) {
                    Column(modifier = Modifier.padding(SentinelDimens.CardPadding)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Analytics, contentDescription = null, tint = SentinelBlue)
                                Spacer(Modifier.width(SentinelDimens.SpacingSmall))
                                Text(
                                    text = stringResource(R.string.market_intelligence),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = SentinelBlue
                                )
                            }
                            Icon(
                                imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = stringResource(R.string.desc_expand_intelligence),
                                tint = SentinelBlue
                            )
                        }

                        if (isExpanded) {
                            Spacer(modifier = Modifier.height(SentinelDimens.SpacingMedium))
                            systemHedges.forEach { hedge ->
                                SystemIntelligenceItem(hedge)
                                Spacer(modifier = Modifier.height(SentinelDimens.SpacingSmall))
                            }
                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = SentinelDimens.SpacingSmall),
                                color = SentinelBlue.copy(alpha = 0.1f)
                            )
                            DetailRow(stringResource(R.string.label_vix), data.vix.toString(), SentinelOrange)
                            DetailRow(stringResource(R.string.label_skew), data.skew.toString(), SentinelBlue)
                            DetailRow(stringResource(R.string.label_fed_repo), data.fedRepoFlow, SentinelTurquoise)
                        }
                    }
                }

                Text(
                    text = stringResource(R.string.my_watchlist),
                    modifier = Modifier.padding(top = SentinelDimens.SpacingExtraLarge, bottom = SentinelDimens.SpacingSmall),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(SentinelDimens.ListSpacing)
                ) {
                    items(userWatchlist) { item ->
                        WatchlistItemComponent(
                            item = item,
                            onDelete = { viewModel.removeStock(item) },
                            onEdit = { itemToEdit = item }
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = SentinelDimens.HeaderVerticalPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.live_mode),
                        style = MaterialTheme.typography.labelLarge,
                        color = SentinelTurquoise
                    )
                }
            }
        }
    }
}