package uk.tw.energy.service;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import uk.tw.energy.domain.ElectricityReading;
import uk.tw.energy.domain.PricePlan;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
public class WeeklyCostCalculator {
	 private final List<PricePlan> pricePlans;

	    @Autowired
	    public WeeklyCostCalculator(List<PricePlan> pricePlans) {
	        this.pricePlans = pricePlans;
	    }
    // A) Weekly total cost and weekly average cost
    public BigDecimal calculateWeeklyTotalCost(List<ElectricityReading> readings, BigDecimal unitRate) {
        Optional<BigDecimal> totalConsumption = readings.stream()
                .map(ElectricityReading::reading)
                .reduce(BigDecimal::add);
        return totalConsumption.get().multiply(unitRate);
    }

    public BigDecimal calculateWeeklyAverageCost(List<ElectricityReading> readings, BigDecimal unitRate) {
        BigDecimal totalCost = calculateWeeklyTotalCost(readings, unitRate);
        return totalCost.divide(BigDecimal.valueOf(readings.size()));
    }

    // B) Weekly total cost and average per price plan
    public BigDecimal calculateWeeklyTotalCostPerPricePlan(List<ElectricityReading> readings, PricePlan pricePlan) {
        return calculateWeeklyTotalCost(readings, pricePlan.getUnitRate());
    }

    public BigDecimal calculateWeeklyAverageCostPerPricePlan(List<ElectricityReading> readings, PricePlan pricePlan) {
        return calculateWeeklyAverageCost(readings, pricePlan.getUnitRate());
    }
    
    
    
    public Map<String, BigDecimal> calculateWeeklyAverageCostForAllPlans(List<ElectricityReading> readings) {
        Map<String, BigDecimal> averageCostPerPlan = new HashMap<>();

        for (PricePlan pricePlan : pricePlans) {
            BigDecimal averageCost = calculateWeeklyAverageCostPerPricePlan(readings, pricePlan);
            averageCostPerPlan.put(pricePlan.getPlanName(), averageCost);
        }
        return averageCostPerPlan;
    }
	public Map<String, BigDecimal> calculateWeeklyTotalCostForAllPlans(List<ElectricityReading> readings) {
		// TODO Auto-generated method stub
        Map<String, BigDecimal> totalCostPerPlan = new HashMap<>();
        for (PricePlan pricePlan : pricePlans) {
            BigDecimal totalCost = calculateWeeklyTotalCostPerPricePlan(readings, pricePlan);
            totalCostPerPlan.put(pricePlan.getPlanName(), totalCost);
        }
		return totalCostPerPlan;

	}

}
