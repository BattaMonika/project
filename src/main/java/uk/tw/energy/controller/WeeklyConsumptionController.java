package uk.tw.energy.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import uk.tw.energy.domain.ElectricityReading;
import uk.tw.energy.domain.PricePlan;
import uk.tw.energy.service.MeterReadingService;
import uk.tw.energy.service.WeeklyConsumptionCalculator;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/weekly-consumption")
public class WeeklyConsumptionController {
	 private final WeeklyConsumptionCalculator weeklyConsumptionCalculator;
	    private final MeterReadingService meterReadingService;

	    @Autowired
	    public WeeklyConsumptionController(WeeklyConsumptionCalculator weeklyConsumptionCalculator, MeterReadingService meterReadingService) {
	        this.weeklyConsumptionCalculator = weeklyConsumptionCalculator;
	        this.meterReadingService = meterReadingService;
	    }

	    // Endpoint to get weekly total consumption
	    @GetMapping("/total/{smartMeterId}")
	    public ResponseEntity<BigDecimal> getWeeklyTotal(@PathVariable String smartMeterId) {
	        List<ElectricityReading> readings = meterReadingService.getReadings(smartMeterId)
	                .orElseThrow(() -> new RuntimeException("No readings found for smart meter ID: " + smartMeterId));
	        BigDecimal totalConsumption = weeklyConsumptionCalculator.calculateWeeklyTotal(readings);
	        return ResponseEntity.ok(totalConsumption);
	    }

	    // Endpoint to get weekly average consumption
	    @GetMapping("/average/{smartMeterId}")
	    public ResponseEntity<BigDecimal> getWeeklyAverage(@PathVariable String smartMeterId) {
	        List<ElectricityReading> readings = meterReadingService.getReadings(smartMeterId)
	                .orElseThrow(() -> new RuntimeException("No readings found for smart meter ID: " + smartMeterId));
	        BigDecimal averageConsumption = weeklyConsumptionCalculator.calculateWeeklyAverage(readings);
	        return ResponseEntity.ok(averageConsumption);
	    }

	    // Endpoint to get weekly total consumption per price plan
	    @PostMapping("/total/{smartMeterId}/price-plan")
	    public ResponseEntity<BigDecimal> getWeeklyTotalPerPricePlan(@PathVariable String smartMeterId, @RequestBody PricePlan pricePlan) {
	        List<ElectricityReading> readings = meterReadingService.getReadings(smartMeterId)
	                .orElseThrow(() -> new RuntimeException("No readings found for smart meter ID: " + smartMeterId));
	        BigDecimal totalConsumption = weeklyConsumptionCalculator.calculateWeeklyTotalPerPricePlan(readings, pricePlan);
	        return ResponseEntity.ok(totalConsumption);
	    }

	    // Endpoint to get weekly average consumption per price plan
	    @PostMapping("/average/{smartMeterId}/price-plan")
	    public ResponseEntity<BigDecimal> getWeeklyAveragePerPricePlan(@PathVariable String smartMeterId, @RequestBody PricePlan pricePlan) {
	        List<ElectricityReading> readings = meterReadingService.getReadings(smartMeterId)
	                .orElseThrow(() -> new RuntimeException("No readings found for smart meter ID: " + smartMeterId));
	        BigDecimal averageConsumption = weeklyConsumptionCalculator.calculateWeeklyAveragePerPricePlan(readings, pricePlan);
	        return ResponseEntity.ok(averageConsumption);
	    }
}
