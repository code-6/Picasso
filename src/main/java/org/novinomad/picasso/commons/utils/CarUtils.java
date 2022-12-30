package org.novinomad.picasso.commons.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.RandomUtils;
import org.novinomad.picasso.domain.erm.entities.tour_participants.Driver;

import java.util.*;

@UtilityClass
public class CarUtils {
    private static final Map<String, List<String>> brandsAndModels = new HashMap<>();

    static {
        brandsAndModels.put("Toyota", List.of("AVALON", "BELTA", "CAMRY", "CENTURY", "COROLLA", "CROWN", "PRIUS", "FJ CRUISER", "HARRIER", "LAND CRUISER", "RAV4"));
        brandsAndModels.put("Nissan", List.of("LEAF", "MICRA (K13)", "ALMERA", "ALTIMA", "MAXIMA", "SKYLINE", "PATHFINDER", "QASHQAI", "SERENA", "GT-R", "Z"));
        brandsAndModels.put("Mercedes-Benz", List.of("SPRINTER", "CITAN", "EQB", "GLS-CLASS", "E-CLASS", "C-CLASS", "A-CLASS", "AMG GT", "AMG ONE", "AMG SL", "G-CLASS"));
    }

    public static Driver.Car random() {
        Driver.Car rndCar = null;
        int totalBrandsCount = brandsAndModels.keySet().size();
        int randomBrandIdx = RandomUtils.nextInt(0, totalBrandsCount - 1);
        int i = 0;
        for (Map.Entry<String, List<String>> brandAndModels : brandsAndModels.entrySet()) {
            if(i == randomBrandIdx) {
                List<String> carModels = brandAndModels.getValue();
                int totalModelsCount = carModels.size();
                int randomModelIdx = RandomUtils.nextInt(1, totalModelsCount - 1);
                String carRndNum = String.valueOf(RandomUtils.nextInt(1000, 9999));
                rndCar = new Driver.Car(brandAndModels.getKey(), carModels.get(randomModelIdx), carRndNum);
            }
            i++;
        }
        return rndCar;
    }

    public static Set<Driver.Car> randomSet() {
        return randomSet(1, 0);
    }

    public static Set<Driver.Car> randomSet(int minCount, int maxCount) {
        maxCount = Math.abs(maxCount);

        int maxAvailableModelsCount = getTotalModelsCount();

        if(minCount < 1 || minCount > maxCount) minCount = 1;

        if(maxCount > maxAvailableModelsCount || maxCount < minCount) maxCount = maxAvailableModelsCount;

        int carsCountToReturn = RandomUtils.nextInt(minCount, maxCount);

        Set<Driver.Car> cars = new HashSet<>();

        while(cars.size() < carsCountToReturn) cars.add(random());

        return cars;
    }

    public static int getTotalModelsCount() {
        int count = 0;

        for (Map.Entry<String, List<String>> brandAndModels : brandsAndModels.entrySet()) {
            count++;
            count += brandAndModels.getValue().size();
        }
        return count;
    }
}
