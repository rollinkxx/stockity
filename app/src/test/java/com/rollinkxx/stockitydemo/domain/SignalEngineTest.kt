package com.rollinkxx.stockitydemo.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class SignalEngineTest {
    @Test fun unconfiguredFeedProducesExplicitNoTrade() {
        val result = SignalEngine.evaluate(MarketDataStatus.UNCONFIGURED)
        assertEquals(TradeSignal.NO_TRADE, result.signal)
        assertEquals(0, result.confidence)
        assertFalse(result.reasons.isEmpty())
    }

    @Test fun staleAndDisconnectedFeedsCannotProduceSignals() {
        listOf(MarketDataStatus.STALE, MarketDataStatus.DISCONNECTED).forEach { state ->
            assertEquals(TradeSignal.NO_TRADE, SignalEngine.evaluate(state).signal)
        }
    }
}
