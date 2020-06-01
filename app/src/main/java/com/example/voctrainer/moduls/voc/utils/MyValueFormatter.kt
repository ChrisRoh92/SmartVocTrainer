package com.example.voctrainer.moduls.voc.utils

import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.DecimalFormat

class MyValueFormatter(var lineChart: LineChart): ValueFormatter()
{
    var mFormat: DecimalFormat
    private var percentSignSeparated: Boolean = false

    init {
        mFormat = DecimalFormat("###,###,##0.0")
        percentSignSeparated = true
    }


    override fun getFormattedValue(value: Float): String {
        return mFormat.format(value) + if (percentSignSeparated) " %" else "%"
    }

    override fun getPieLabel(value: Float, pieEntry: PieEntry?): String {
        return if (lineChart != null) {
            // Converted to percent
            getFormattedValue(value)
        } else {
            // raw value, skip percent sign
            mFormat.format(value)
        }
    }
}