package com.rollinkxx.stockitydemo.domain

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TradingSafetyTest {
    private fun permit(
        accountMode: AccountMode = AccountMode.DEMO,
        marketDataStatus: MarketDataStatus = MarketDataStatus.VALID,
        riskLimitsPassed: Boolean = true,
        emergencyStopActive: Boolean = false,
    ) = TradingPermit(accountMode, marketDataStatus, riskLimitsPassed, emergencyStopActive)

    @Test fun allowsOnlyVerifiedDemoWithValidMarketData() {
        assertTrue(TradingSafety.mayOpenDemoTrade(permit()))
        assertFalse(TradingSafety.mayOpenDemoTrade(permit(accountMode = AccountMode.REAL)))
        assertFalse(TradingSafety.mayOpenDemoTrade(permit(accountMode = AccountMode.UNKNOWN)))
    }

    @Test fun blocksUnsafeDataAndRiskStates() {
        assertFalse(TradingSafety.mayOpenDemoTrade(permit(marketDataStatus = MarketDataStatus.STALE)))
        assertFalse(TradingSafety.mayOpenDemoTrade(permit(marketDataStatus = MarketDataStatus.DISCONNECTED)))
        assertFalse(TradingSafety.mayOpenDemoTrade(permit(marketDataStatus = MarketDataStatus.UNCONFIGURED)))
        assertFalse(TradingSafety.mayOpenDemoTrade(permit(riskLimitsPassed = false)))
        assertFalse(TradingSafety.mayOpenDemoTrade(permit(emergencyStopActive = true)))
    }

    @Test(expected = TradingNotAllowedException::class)
    fun throwingGuardRejectsUnknownAccount() {
        TradingSafety.requireDemoTradeAllowed(permit(accountMode = AccountMode.UNKNOWN))
    }
}
