package com.rollinkxx.stockitydemo.domain

/** Account classification must be verified by a trusted provider before any order can be enabled. */
enum class AccountMode { DEMO, REAL, UNKNOWN }

enum class MarketDataStatus { VALID, STALE, DISCONNECTED, UNCONFIGURED }

data class TradingPermit(
    val accountMode: AccountMode,
    val marketDataStatus: MarketDataStatus,
    val riskLimitsPassed: Boolean,
    val emergencyStopActive: Boolean,
)

object TradingSafety {
    fun mayOpenDemoTrade(permit: TradingPermit): Boolean =
        permit.accountMode == AccountMode.DEMO &&
            permit.marketDataStatus == MarketDataStatus.VALID &&
            permit.riskLimitsPassed &&
            !permit.emergencyStopActive

    fun requireDemoTradeAllowed(permit: TradingPermit) {
        if (!mayOpenDemoTrade(permit)) {
            throw TradingNotAllowedException("Order blocked: demo account, valid data, risk approval, and active system are all required")
        }
    }
}

class TradingNotAllowedException(message: String) : IllegalStateException(message)
