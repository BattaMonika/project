package uk.tw.energy.service;
import org.springframework.stereotype.Service;

import uk.tw.energy.domain.ElectricityReading;
import uk.tw.energy.domain.PricePlan;

import java.math.BigDecimal;
import java.util.List;

@Service
public class WeeklyConsumptionCalculator {
	 // A) Weekly average and weekly total
    public BigDecimal calculateWeeklyTotal(List<ElectricityReading> readings) {
        return readings.stream()
                .map(ElectricityReading::reading)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateWeeklyAverage(List<ElectricityReading> readings) {
        return calculateWeeklyTotal(readings).divide(BigDecimal.valueOf(readings.size()), BigDecimal.ROUND_HALF_UP);
    }

    // B) Weekly total and average per price plan
    public BigDecimal calculateWeeklyTotalPerPricePlan(List<ElectricityReading> readings, PricePlan pricePlan) {
        BigDecimal totalConsumption = calculateWeeklyTotal(readings);
        return totalConsumption.multiply(pricePlan.getUnitRate());
    }

    public BigDecimal calculateWeeklyAveragePerPricePlan(List<ElectricityReading> readings, PricePlan pricePlan) {
        BigDecimal averageConsumption = calculateWeeklyAverage(readings);
        return averageConsumption.multiply(pricePlan.getUnitRate());
    }
}
