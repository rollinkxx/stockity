package com.rollinkxx.stockitydemo.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class OfflineBacktesterTest {
    private fun candle(index: Int, open: Double, close: Double) =
        Candle(index.toLong() * 60_000, open, maxOf(open, close) + 1.0, minOf(open, close) - 1.0, close)

    private val config = OfflineBacktestConfig(
        initialBalance = 100.0,
        stake = 10.0,
        winPayoutRatio = 0.8,
        feePerTrade = 0.1,
        expiryBars = 1,
        dataSourceLabel = "test fixture only",
    )

    @Test fun decisionsSeeOnlyPastAndFillsUseNextBarOpen() {
        val candles = listOf(
            candle(0, 10.0, 10.5),
            candle(1, 11.0, 12.0),
            candle(2, 12.0, 11.0),
            candle(3, 10.0, 10.5),
        )
        val historyLengths = mutableListOf<Int>()
        val result = OfflineBacktester.run(candles, config) { history ->
            historyLengths += history.size
            TradeSignal.BUY
        }
        assertEquals(listOf(1, 2, 3), historyLengths)
        assertEquals(3, result.trades.size)
        assertEquals(11.0, result.trades[0].entryPrice, 1e-9)
        assertEquals(12.0, result.trades[0].expiryPrice, 1e-9)
        assertEquals(HypotheticalOutcome.WIN, result.trades[0].outcome)
        assertEquals(7.9, result.trades[0].pnl, 1e-9)
        assertEquals(HypotheticalOutcome.LOSS, result.trades[1].outcome)
        assertEquals(-10.1, result.trades[1].pnl, 1e-9)
        assertEquals(7.9, result.trades[2].pnl, 1e-9)
        assertEquals(105.7, result.endingBalance, 1e-9)
        assertEquals(2.0 / 3.0, result.winRate!!, 1e-9)
    }

    @Test fun noTradeSignalCreatesNoHypotheticalPosition() {
        val result = OfflineBacktester.run(listOf(candle(0, 10.0, 10.0), candle(1, 10.0, 10.0)), config) {
            TradeSignal.NO_TRADE
        }
        assertTrue(result.isHypothetical)
        assertTrue(result.trades.isEmpty())
        assertEquals(100.0, result.endingBalance, 1e-9)
        assertNull(result.winRate)
    }

    @Test fun expiryTradesDoNotOverlapAndInsufficientFutureBarsAreReported() {
        val cfg = config.copy(expiryBars = 2)
        val result = OfflineBacktester.run(
            listOf(
                candle(0, 10.0, 10.0),
                candle(1, 10.0, 11.0),
                candle(2, 11.0, 10.0),
                candle(3, 10.0, 12.0),
            ),
            cfg,
        ) { TradeSignal.BUY }
        assertEquals(1, result.trades.size)
        assertEquals(0, result.trades[0].decisionIndex)
        assertEquals(1, result.trades[0].entryIndex)
        assertEquals(2, result.trades[0].expiryIndex)
        assertEquals(1, result.skippedInsufficientFutureBars)
    }

    @Test(expected = IllegalArgumentException::class)
    fun backtestRequiresExplicitSourceLabel() {
        config.copy(dataSourceLabel = " ")
    }
}
