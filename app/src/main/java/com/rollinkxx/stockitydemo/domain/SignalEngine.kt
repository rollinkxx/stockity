package com.rollinkxx.stockitydemo.domain

enum class TradeSignal { BUY, SELL, NO_TRADE }

data class SignalDecision(
    val signal: TradeSignal,
    val confidence: Int,
    val reasons: List<String>,
)

/**
 * Deliberately conservative until a verified provider supplies valid, timestamped candles and a
 * reviewed strategy implementation is connected. No placeholder or synthetic price is produced.
 */
object SignalEngine {
    fun evaluate(marketDataStatus: MarketDataStatus): SignalDecision =
        SignalDecision(
            signal = TradeSignal.NO_TRADE,
            confidence = 0,
            reasons = when (marketDataStatus) {
                MarketDataStatus.VALID -> listOf("No verified market feed or strategy input is configured")
                MarketDataStatus.STALE -> listOf("Market data is stale")
                MarketDataStatus.DISCONNECTED -> listOf("Market data connection is disconnected")
                MarketDataStatus.UNCONFIGURED -> listOf("Stockity realtime integration is not configured")
            },
        )
}
