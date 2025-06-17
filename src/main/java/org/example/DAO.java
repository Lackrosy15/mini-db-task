package org.example;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class DAO {
    Properties properties;

    public DAO() throws IOException {
        this.properties = new Properties();
        this.properties.load(new BufferedReader(new FileReader("C:\\Users\\User\\IdeaProjects\\mini-db-project\\src\\main\\java\\org\\example\\prop.properties")));
    }

    public Human findHumanInfoByHumanId(long id) throws SQLException {
        String sql = "SELECT h.id, h.name, h.age, c.id AS car_id, c.model FROM humans h LEFT JOIN cars_humans_association cha ON h.id = cha.human_id LEFT JOIN cars c ON cha.car_id = c.id WHERE h.id = ?;";

        try (Connection connection = DriverManager.getConnection(
                properties.getProperty("db-url"),
                properties.getProperty("username"),
                properties.getProperty("password"));

             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                Human human = null;
                List<Car> cars = new ArrayList<>();

                while (resultSet.next()) {
                    if (human == null) {
                        human = new Human(resultSet.getLong("id"), resultSet.getString("name"), resultSet.getInt("age"), cars);
                    }

                    long carId = resultSet.getLong("car_id");

                    if (!resultSet.wasNull()) {
                        cars.add(new Car(
                                carId,
                                resultSet.getString("model")
                        ));
                    }
                }
                System.out.println(human);
                return human;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding human by id: " + id, e);
        }
    }

    public List<Human> findAllHumans() {
        String sql = "SELECT h.id, h.name, h.age, c.id as car_id, c.model FROM humans h LEFT JOIN cars_humans_association cha ON h.id = cha.human_id LEFT JOIN cars c ON cha.car_id = c.id;";

        try (Connection connection = DriverManager.getConnection(
                properties.getProperty("db-url"),
                properties.getProperty("username"),
                properties.getProperty("password"));
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                List<Human> allHumans = new ArrayList<>();

                while (resultSet.next()) {
                    long currentHumanId = resultSet.getLong("id");

                    Optional human = allHumans.stream().filter(h -> h.getHuman_id() == currentHumanId)
                            .findFirst()
                            .map(human1 -> {
                                try {
                                    return human1.getCars().add(new Car(resultSet.getLong("car_id"), resultSet.getString("model")));
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                            });

                    if (!human.isPresent()) {
                        List<Car> humanCars = new ArrayList<>();
                        long carId = resultSet.getLong("car_id");
                        if (!resultSet.wasNull()) {
                            humanCars.add(new Car(carId, resultSet.getString("model")));
                        }
                        allHumans.add(new Human(currentHumanId, resultSet.getString("name"), resultSet.getInt("age"), humanCars));
                    }
                }
                System.out.println(allHumans);
                return allHumans;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}





