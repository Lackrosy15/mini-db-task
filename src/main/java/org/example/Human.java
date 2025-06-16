package org.example;

import java.util.List;

public class Human {
    private long human_id;
    private String name;
    private int age;
    private List<Car> cars;

    public Human(long human_id, String name, int age, List<Car> cars) {
        this.human_id = human_id;
        this.name = name;
        this.age = age;
        this.cars = cars;
    }

    public long getHuman_id() {
        return human_id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public List<Car> getCars() {
        return cars;
    }


    @Override
    public String toString() {
        return "Human{" +
                "human_id=" + human_id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", cars=" + cars +
                '}';
    }
}
