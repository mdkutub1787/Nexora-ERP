package com.kutub.smarterp.ui.reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kutub.smarterp.data.local.PreferencesManager
import com.kutub.smarterp.data.model.SaleEntity
import com.kutub.smarterp.data.repository.SaleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val saleRepository: SaleRepository,
    preferencesManager: PreferencesManager
) : ViewModel() {

    val currency = preferencesManager.currencyFlow

    private val _totalRevenue = MutableStateFlow(0.0)
    val totalRevenue: StateFlow<Double> = _totalRevenue.asStateFlow()

    private val _totalSalesCount = MutableStateFlow(0)
    val totalSalesCount: StateFlow<Int> = _totalSalesCount.asStateFlow()

    private val _todayRevenue = MutableStateFlow(0.0)
    val todayRevenue: StateFlow<Double> = _todayRevenue.asStateFlow()

    private val _weeklyRevenue = MutableStateFlow<List<Double>>(List(7) { 0.0 })
    val weeklyRevenue: StateFlow<List<Double>> = _weeklyRevenue.asStateFlow()

    init {
        loadReportData()
    }

    private fun loadReportData() {
        viewModelScope.launch {
            saleRepository.getAllSales()
                .catch { /* Handle error */ }
                .collect { sales ->
                    calculateMetrics(sales)
                    calculateWeeklyChart(sales)
                }
        }
    }

    private fun calculateMetrics(sales: List<SaleEntity>) {
        _totalSalesCount.value = sales.size
        _totalRevenue.value = sales.sumOf { it.finalAmount }

        val todayStart = getStartOfDay()
        _todayRevenue.value = sales
            .filter { it.saleDate >= todayStart }
            .sumOf { it.finalAmount }
    }

    private fun calculateWeeklyChart(sales: List<SaleEntity>) {
        // Last 7 days revenue
        val calendar = Calendar.getInstance()
        val dailyTotals = DoubleArray(7) { 0.0 }
        
        // Start from 6 days ago (index 0) to today (index 6)
        for (i in 6 downTo 0) {
            calendar.timeInMillis = System.currentTimeMillis()
            calendar.add(Calendar.DAY_OF_YEAR, -i)
            val startOfDay = getStartOfDay(calendar)
            val endOfDay = startOfDay + (24 * 60 * 60 * 1000) - 1

            val revenueForDay = sales
                .filter { it.saleDate in startOfDay..endOfDay }
                .sumOf { it.finalAmount }
            
            dailyTotals[6 - i] = revenueForDay
        }
        
        _weeklyRevenue.value = dailyTotals.toList()
    }

    private fun getStartOfDay(calendar: Calendar = Calendar.getInstance()): Long {
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
}


