package org.example;

public class Car {
    private long car_id;
    private String model;


    public Car(long car_id, String model) {
        this.car_id = car_id;
        this.model = model;
    }

    public long getCar_id() {
        return car_id;
    }

    public String getModel() {
        return model;
    }


    @Override
    public String toString() {
        return "Car{" +
                "car_id=" + car_id +
                ", model='" + model + '\'' +
                '}';
    }
}
