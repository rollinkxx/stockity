package com.rollinkxx.stockitydemo.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class MarketAnalysisTest {
    private fun candle(index: Int, open: Double, high: Double, low: Double, close: Double) =
        Candle(index.toLong() * 60_000, open, high, low, close)

    @Test fun emaSeedsWithSmaThenUsesStandardRecurrence() {
        val result = IndicatorCalculator.ema(listOf(1.0, 2.0, 3.0, 4.0, 5.0), 3)
        assertNull(result[1])
        assertEquals(2.0, result[2]!!, 1e-9)
        assertEquals(3.0, result[3]!!, 1e-9)
        assertEquals(4.0, result[4]!!, 1e-9)
    }

    @Test fun rsiUsesWilderConventionAndHandlesOneSidedMoves() {
        assertEquals(100.0, IndicatorCalculator.rsi(listOf(1.0, 2.0, 3.0, 4.0), 2)!!, 1e-9)
        assertEquals(0.0, IndicatorCalculator.rsi(listOf(4.0, 3.0, 2.0, 1.0), 2)!!, 1e-9)
        assertNull(IndicatorCalculator.rsi(listOf(1.0, 2.0), 2))
    }

    @Test fun macdReturnsNullUntilSignalEmaIsReady() {
        assertNull(IndicatorCalculator.macd((1..30).map(Int::toDouble)))
        val value = IndicatorCalculator.macd((1..40).map(Int::toDouble))
        assertNotNull(value)
        assertEquals(value!!.macd - value.signal, value.histogram, 1e-9)
    }

    @Test fun atrAndRollingExtremaUseOnlySuppliedCandles() {
        val candles = listOf(
            candle(0, 10.0, 12.0, 9.0, 11.0),
            candle(1, 11.0, 13.0, 10.0, 12.0),
            candle(2, 12.0, 14.0, 11.0, 13.0),
        )
        assertEquals(3.0, IndicatorCalculator.atr(candles, 2)!!, 1e-9)
        val levels = IndicatorCalculator.supportResistance(candles, 2)!!
        assertEquals(10.0, levels.support, 1e-9)
        assertEquals(14.0, levels.resistance, 1e-9)
        assertNull(IndicatorCalculator.supportResistance(candles, 4))
    }

    @Test(expected = IllegalArgumentException::class)
    fun invalidOhlcIsRejected() {
        candle(1, 10.0, 9.0, 8.0, 10.0)
    }

    @Test fun indicatorParametersMustBeValid() {
        try {
            IndicatorCalculator.ema(listOf(1.0), 0)
            throw AssertionError("expected invalid period to be rejected")
        } catch (_: IllegalArgumentException) {
            assertTrue(true)
        }
    }
}
