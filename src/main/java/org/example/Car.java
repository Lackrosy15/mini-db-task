package org.example;

public class Car {
    private long car_id;
    private String model;
    private long human_id;


    public Car(long car_id, String model, long human_id) {
        this.car_id = car_id;
        this.model = model;
        this.human_id = human_id;
    }

    public long getCar_id() {
        return car_id;
    }

    public String getModel() {
        return model;
    }

    public long getHuman_id() {
        return human_id;
    }

    @Override
    public String toString() {
        return "Car{" +
                "car_id=" + car_id +
                ", model='" + model + '\'' +
                ", human_id=" + human_id +
                '}';
    }
}
