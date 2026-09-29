package com.rollinkxx.stockitydemo.domain

/** Historical or provider-origin candle. The source must be tracked outside this value object. */
data class Candle(
    val timestampMillis: Long,
    val open: Double,
    val high: Double,
    val low: Double,
    val close: Double,
    val volume: Double? = null,
) {
    init {
        require(timestampMillis >= 0) { "timestampMillis must be non-negative" }
        require(listOf(open, high, low, close).all { it.isFinite() && it > 0.0 }) {
            "OHLC prices must be finite and positive"
        }
        require(high >= maxOf(open, close) && low <= minOf(open, close) && high >= low) {
            "Candle high/low must contain its open and close"
        }
        require(volume == null || (volume.isFinite() && volume >= 0.0)) {
            "Volume must be finite and non-negative when present"
        }
    }
}

data class MacdValue(val macd: Double, val signal: Double, val histogram: Double)
data class SupportResistance(val support: Double, val resistance: Double)

object IndicatorCalculator {
    /** EMA uses an initial n-period SMA, then the standard alpha=2/(n+1) recurrence. */
    fun ema(values: List<Double>, period: Int): List<Double?> {
        require(period > 0) { "period must be positive" }
        require(values.all { it.isFinite() }) { "values must be finite" }
        if (values.size < period) return List(values.size) { null }

        val result = MutableList<Double?>(values.size) { null }
        var current = values.take(period).average()
        result[period - 1] = current
        val alpha = 2.0 / (period + 1.0)
        for (index in period until values.size) {
            current = alpha * values[index] + (1.0 - alpha) * current
            result[index] = current
        }
        return result
    }

    /** Wilder RSI over the final n-period window; null means insufficient history. */
    fun rsi(values: List<Double>, period: Int = 14): Double? {
        require(period > 0) { "period must be positive" }
        require(values.all { it.isFinite() }) { "values must be finite" }
        if (values.size <= period) return null

        var averageGain = 0.0
        var averageLoss = 0.0
        for (index in 1..period) {
            val delta = values[index] - values[index - 1]
            if (delta > 0.0) averageGain += delta else averageLoss -= delta
        }
        averageGain /= period
        averageLoss /= period

        for (index in (period + 1) until values.size) {
            val delta = values[index] - values[index - 1]
            val gain = maxOf(delta, 0.0)
            val loss = maxOf(-delta, 0.0)
            averageGain = (averageGain * (period - 1) + gain) / period
            averageLoss = (averageLoss * (period - 1) + loss) / period
        }
        if (averageLoss == 0.0) return 100.0
        val relativeStrength = averageGain / averageLoss
        return 100.0 - 100.0 / (1.0 + relativeStrength)
    }

    /** Standard 12/26/9 MACD by default. Returns null until its signal EMA is initialized. */
    fun macd(
        values: List<Double>,
        fastPeriod: Int = 12,
        slowPeriod: Int = 26,
        signalPeriod: Int = 9,
    ): MacdValue? {
        require(fastPeriod > 0 && slowPeriod > fastPeriod && signalPeriod > 0) {
            "MACD periods must be positive and slowPeriod must exceed fastPeriod"
        }
        require(values.all { it.isFinite() }) { "values must be finite" }
        val fast = ema(values, fastPeriod)
        val slow = ema(values, slowPeriod)
        val firstValid = slowPeriod - 1
        if (values.size <= firstValid) return null
        val macdSeries = (firstValid until values.size).map { index ->
            fast[index]!! - slow[index]!!
        }
        val signalSeries = ema(macdSeries, signalPeriod)
        val latestMacd = macdSeries.last()
        val latestSignal = signalSeries.last() ?: return null
        return MacdValue(latestMacd, latestSignal, latestMacd - latestSignal)
    }

    /** Wilder ATR; the first true range is high-low and subsequent values include prior close. */
    fun atr(candles: List<Candle>, period: Int = 14): Double? {
        require(period > 0) { "period must be positive" }
        if (candles.size < period) return null
        requireStrictlyIncreasing(candles)
        val trueRanges = candles.mapIndexed { index, candle ->
            if (index == 0) candle.high - candle.low
            else {
                val previousClose = candles[index - 1].close
                maxOf(
                    candle.high - candle.low,
                    kotlin.math.abs(candle.high - previousClose),
                    kotlin.math.abs(candle.low - previousClose),
                )
            }
        }
        var value = trueRanges.take(period).average()
        for (index in period until trueRanges.size) {
            value = (value * (period - 1) + trueRanges[index]) / period
        }
        return value
    }

    /** Simple rolling extrema, not a claim that a level has been tested or confirmed. */
    fun supportResistance(candles: List<Candle>, lookback: Int): SupportResistance? {
        require(lookback > 0) { "lookback must be positive" }
        if (candles.size < lookback) return null
        requireStrictlyIncreasing(candles)
        val window = candles.takeLast(lookback)
        return SupportResistance(
            support = window.minOf { it.low },
            resistance = window.maxOf { it.high },
        )
    }

    private fun requireStrictlyIncreasing(candles: List<Candle>) {
        require(candles.zipWithNext().all { (left, right) -> right.timestampMillis > left.timestampMillis }) {
            "candle timestamps must be strictly increasing"
        }
    }
}
