package com.rollinkxx.stockitydemo.domain

import kotlin.math.max

data class OfflineBacktestConfig(
    val initialBalance: Double,
    val stake: Double,
    /** Gross payout per unit stake on a win; the modeled fee is deducted separately. */
    val winPayoutRatio: Double,
    /** Per-trade modeled cost; zero is allowed only as an explicit assumption. */
    val feePerTrade: Double,
    /** Number of bars from entry bar through the expiry bar, inclusive. */
    val expiryBars: Int,
    val dataSourceLabel: String,
) {
    init {
        require(initialBalance.isFinite() && initialBalance > 0.0)
        require(stake.isFinite() && stake > 0.0 && stake <= initialBalance)
        require(winPayoutRatio.isFinite() && winPayoutRatio >= 0.0)
        require(feePerTrade.isFinite() && feePerTrade >= 0.0)
        require(expiryBars > 0)
        require(dataSourceLabel.isNotBlank())
    }
}

enum class HypotheticalOutcome { WIN, LOSS, TIE }

data class HypotheticalTrade(
    val decisionIndex: Int,
    val entryIndex: Int,
    val expiryIndex: Int,
    val signal: TradeSignal,
    val entryPrice: Double,
    val expiryPrice: Double,
    val outcome: HypotheticalOutcome,
    val pnl: Double,
)

data class OfflineBacktestResult(
    val dataSourceLabel: String,
    val isHypothetical: Boolean,
    val initialBalance: Double,
    val endingBalance: Double,
    val trades: List<HypotheticalTrade>,
    val winRate: Double?,
    val expectancy: Double?,
    val profitFactor: Double?,
    val maxDrawdown: Double,
    val skippedInsufficientFutureBars: Int,
)

/**
 * Local-only model. It has no network access, account state, or order provider. A decision callback
 * receives a prefix ending at the decision close; hypothetical entry occurs at the next bar's open.
 * Trades do not overlap: a new decision is made after the prior trade's expiry close.
 */
object OfflineBacktester {
    fun run(
        candles: List<Candle>,
        config: OfflineBacktestConfig,
        decide: (decisionHistory: List<Candle>) -> TradeSignal,
    ): OfflineBacktestResult {
        require(candles.zipWithNext().all { (left, right) -> right.timestampMillis > left.timestampMillis }) {
            "candle timestamps must be strictly increasing"
        }
        val trades = mutableListOf<HypotheticalTrade>()
        var balance = config.initialBalance
        var peak = balance
        var maxDrawdown = 0.0
        var skipped = 0
        var decisionIndex = 0

        while (decisionIndex < candles.lastIndex && balance >= config.stake) {
            val signal = decide(candles.subList(0, decisionIndex + 1).toList())
            if (signal == TradeSignal.NO_TRADE) {
                decisionIndex++
                continue
            }

            val entryIndex = decisionIndex + 1
            val expiryIndex = entryIndex + config.expiryBars - 1
            if (expiryIndex >= candles.size) {
                skipped++
                break
            }
            val entry = candles[entryIndex].open
            val expiry = candles[expiryIndex].close
            val outcome = when {
                expiry == entry -> HypotheticalOutcome.TIE
                expiry > entry && signal == TradeSignal.BUY -> HypotheticalOutcome.WIN
                expiry < entry && signal == TradeSignal.SELL -> HypotheticalOutcome.WIN
                else -> HypotheticalOutcome.LOSS
            }
            val pnl = when (outcome) {
                HypotheticalOutcome.WIN -> config.stake * config.winPayoutRatio - config.feePerTrade
                HypotheticalOutcome.LOSS -> -config.stake - config.feePerTrade
                HypotheticalOutcome.TIE -> -config.feePerTrade
            }
            balance += pnl
            peak = max(peak, balance)
            if (peak > 0.0) maxDrawdown = max(maxDrawdown, (peak - balance) / peak)
            trades += HypotheticalTrade(
                decisionIndex = decisionIndex,
                entryIndex = entryIndex,
                expiryIndex = expiryIndex,
                signal = signal,
                entryPrice = entry,
                expiryPrice = expiry,
                outcome = outcome,
                pnl = pnl,
            )
            decisionIndex = expiryIndex
        }

        val wins = trades.filter { it.outcome == HypotheticalOutcome.WIN }
        val positivePnl = trades.filter { it.pnl > 0.0 }.sumOf { it.pnl }
        val negativePnl = -trades.filter { it.pnl < 0.0 }.sumOf { it.pnl }
        return OfflineBacktestResult(
            dataSourceLabel = config.dataSourceLabel,
            isHypothetical = true,
            initialBalance = config.initialBalance,
            endingBalance = balance,
            trades = trades,
            winRate = if (trades.isEmpty()) null else wins.size.toDouble() / trades.size,
            expectancy = if (trades.isEmpty()) null else trades.sumOf { it.pnl } / trades.size,
            profitFactor = if (negativePnl == 0.0) null else positivePnl / negativePnl,
            maxDrawdown = maxDrawdown,
            skippedInsufficientFutureBars = skipped,
        )
    }
}
