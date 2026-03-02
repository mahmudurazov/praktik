import java.util.*;
import java.util.stream.Collectors;

public class CarCenter {
    private String centerName;
    private Set<Car> vehicles;

    public CarCenter(String centerName) {
        this.centerName = centerName;
        this.vehicles = new HashSet<>();
    }

    public boolean addVehicle(Car vehicle) {
        return vehicles.add(vehicle);
    }

    public List<Car> getVehiclesSortedByYear() {
        return vehicles.stream()
                .sorted()
                .collect(Collectors.toList());
    }

    public Set<Car> getVehicles() {
        return vehicles;
    }

    public int getVehiclesCount() {
        return vehicles.size();
    }

    public List<Car> findByManufacturer(String manufacturer) {
        return vehicles.stream()
                .filter(v -> v.getManufacturer().equalsIgnoreCase(manufacturer))
                .collect(Collectors.toList());
    }

    public double getAveragePriceByCategory(CarCategory category) {
        return vehicles.stream()
                .filter(v -> v.getCategory() == category)
                .mapToDouble(Car::getPrice)
                .average()
                .orElse(0);
    }
}