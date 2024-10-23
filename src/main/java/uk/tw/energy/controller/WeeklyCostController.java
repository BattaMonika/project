package uk.tw.energy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import uk.tw.energy.domain.ElectricityReading;
import uk.tw.energy.domain.PricePlan;
import uk.tw.energy.service.MeterReadingService;
import uk.tw.energy.service.WeeklyCostCalculator;

import java.math.BigDecimal; // Import BigDecimal
import java.util.List;


@RestController
@RequestMapping("/weekly-cost")
public class WeeklyCostController {

	    private final WeeklyCostCalculator weeklyCostCalculator;
	    private final MeterReadingService meterReadingService;

	    @Autowired
	    public WeeklyCostController(WeeklyCostCalculator weeklyCostCalculator, MeterReadingService meterReadingService) {
	        this.weeklyCostCalculator = weeklyCostCalculator;
	        this.meterReadingService = meterReadingService;
	    }

	    // Endpoint to get weekly total cost
	    @GetMapping("/total/{smartMeterId}/{unitRate}")
	    public ResponseEntity<BigDecimal> getWeeklyTotalCost(@PathVariable String smartMeterId, @PathVariable BigDecimal unitRate) {
	        List<ElectricityReading> readings = meterReadingService.getReadings(smartMeterId)
	                .orElseThrow(() -> new RuntimeException("No readings found for smart meter ID: " + smartMeterId));
	        BigDecimal totalCost = weeklyCostCalculator.calculateWeeklyTotalCost(readings, unitRate);
	        return ResponseEntity.ok(totalCost);
	    }

	    // Endpoint to get weekly average cost
	    @GetMapping("/average/{smartMeterId}/{unitRate}")
	    public ResponseEntity<BigDecimal> getWeeklyAverageCost(@PathVariable String smartMeterId, @PathVariable BigDecimal unitRate) {
	        List<ElectricityReading> readings = meterReadingService.getReadings(smartMeterId)
	                .orElseThrow(() -> new RuntimeException("No readings found for smart meter ID: " + smartMeterId));
	        BigDecimal averageCost = weeklyCostCalculator.calculateWeeklyAverageCost(readings, unitRate);
	        return ResponseEntity.ok(averageCost);
	    }

	    // Endpoint to get weekly total cost per price plan
	    @PostMapping("/total/{smartMeterId}/price-plan")
	    public ResponseEntity<BigDecimal> getWeeklyTotalCostPerPricePlan(@PathVariable String smartMeterId, @RequestBody PricePlan pricePlan) {
	        List<ElectricityReading> readings = meterReadingService.getReadings(smartMeterId)
	                .orElseThrow(() -> new RuntimeException("No readings found for smart meter ID: " + smartMeterId));
	        BigDecimal totalCost = weeklyCostCalculator.calculateWeeklyTotalCostPerPricePlan(readings, pricePlan);
	        return ResponseEntity.ok(totalCost);
	    }

	    // Endpoint to get weekly average cost per price plan
	    @PostMapping("/average/{smartMeterId}/price-plan")
	    public ResponseEntity<BigDecimal> getWeeklyAverageCostPerPricePlan(@PathVariable String smartMeterId, @RequestBody PricePlan pricePlan) {
	        List<ElectricityReading> readings = meterReadingService.getReadings(smartMeterId)
	                .orElseThrow(() -> new RuntimeException("No readings found for smart meter ID: " + smartMeterId));
	        BigDecimal averageCost = weeklyCostCalculator.calculateWeeklyAverageCostPerPricePlan(readings, pricePlan);
	        return ResponseEntity.ok(averageCost);
	    }
}
