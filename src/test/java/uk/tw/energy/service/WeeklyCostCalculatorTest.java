package uk.tw.energy.service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.tw.energy.domain.ElectricityReading;
import uk.tw.energy.domain.PricePlan;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
public class WeeklyCostCalculatorTest {


    private WeeklyCostCalculator weeklyCostCalculator;
    private PricePlan pricePlan1;
    private PricePlan pricePlan2;
    private List<ElectricityReading> electricityReadings;

    @BeforeEach
    public void setUp() {
        // Create PeakTimeMultiplier objects
        List<PricePlan.PeakTimeMultiplier> peakTimeMultipliers1 = Arrays.asList(
                new PricePlan.PeakTimeMultiplier(DayOfWeek.MONDAY, BigDecimal.valueOf(1.2)),
                new PricePlan.PeakTimeMultiplier(DayOfWeek.SATURDAY, BigDecimal.valueOf(1.5))
        );

        List<PricePlan.PeakTimeMultiplier> peakTimeMultipliers2 = Arrays.asList(
                new PricePlan.PeakTimeMultiplier(DayOfWeek.TUESDAY, BigDecimal.valueOf(1.1)),
                new PricePlan.PeakTimeMultiplier(DayOfWeek.SUNDAY, BigDecimal.valueOf(1.3))
        );

        // Create PricePlan objects with unit rates and the peak time multipliers
        pricePlan1 = new PricePlan("Plan-A", "Supplier-A", BigDecimal.valueOf(0.2), peakTimeMultipliers1);
        pricePlan2 = new PricePlan("Plan-B", "Supplier-B", BigDecimal.valueOf(0.3), peakTimeMultipliers2);

        // Create a list of price plans and pass it to the WeeklyCostCalculator
        List<PricePlan> pricePlans = List.of(pricePlan1, pricePlan2);
        weeklyCostCalculator = new WeeklyCostCalculator(pricePlans);

        // Create a common list of ElectricityReadings that can be reused across tests
        electricityReadings = List.of(
                new ElectricityReading(Instant.now(), BigDecimal.valueOf(10)),
                new ElectricityReading(Instant.now(), BigDecimal.valueOf(20)),
                new ElectricityReading(Instant.now(), BigDecimal.valueOf(30))
        );
    }

    @Test
    public void shouldCalculateWeeklyTotalCost() {
        BigDecimal totalCost = weeklyCostCalculator.calculateWeeklyTotalCost(electricityReadings, BigDecimal.valueOf(0.2));
        assertThat(totalCost).isEqualTo(BigDecimal.valueOf(12.00));  // (10 + 20 + 30) * 0.2
    }

    @Test
    public void shouldCalculateWeeklyAverageCost() {
        BigDecimal averageCost = weeklyCostCalculator.calculateWeeklyAverageCost(electricityReadings, BigDecimal.valueOf(0.2));
        assertThat(averageCost).isEqualTo(BigDecimal.valueOf(4.00));  // Total cost / number of readings
    }

    @Test
    public void shouldCalculateWeeklyTotalCostPerPricePlan() {
        BigDecimal totalCostForPlanA = weeklyCostCalculator.calculateWeeklyTotalCostPerPricePlan(electricityReadings, pricePlan1);
        assertThat(totalCostForPlanA).isEqualTo(BigDecimal.valueOf(12.00));  // (10 + 20 + 30) * 0.2
    }

    @Test
    public void shouldCalculateWeeklyAverageCostPerPricePlan() {
        BigDecimal averageCostForPlanB = weeklyCostCalculator.calculateWeeklyAverageCostPerPricePlan(electricityReadings, pricePlan2);
        assertThat(averageCostForPlanB).isEqualTo(BigDecimal.valueOf(6.00));  // (10 + 20 + 30) * 0.3 / 3
    }

    @Test
    public void shouldCalculateWeeklyAverageCostForAllPlans() {
        Map<String, BigDecimal> averageCostPerPlan = weeklyCostCalculator.calculateWeeklyAverageCostForAllPlans(electricityReadings);
        assertThat(averageCostPerPlan).containsEntry("Plan-A", BigDecimal.valueOf(4.00));  // Plan-A's average cost
        assertThat(averageCostPerPlan).containsEntry("Plan-B", BigDecimal.valueOf(6.00));  // Plan-B's average cost
    }

    @Test
    public void shouldCalculateWeeklyTotalCostForAllPlans() {
        Map<String, BigDecimal> totalCostPerPlan = weeklyCostCalculator.calculateWeeklyTotalCostForAllPlans(electricityReadings);
        assertThat(totalCostPerPlan).containsEntry(pricePlan1.getPlanName(), BigDecimal.valueOf(12.00));  // (10 + 20 + 30) * 0.2
        assertThat(totalCostPerPlan).containsEntry(pricePlan2.getPlanName(), BigDecimal.valueOf(18.00));  // (10 + 20 + 30) * 0.3
    }
}
