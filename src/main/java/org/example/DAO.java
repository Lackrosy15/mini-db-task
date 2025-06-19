package org.example;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class DAO {
    Properties properties;

    public DAO() throws IOException {
        this.properties = new Properties();
        this.properties.load(new BufferedReader(new FileReader("C:\\Users\\User\\IdeaProjects\\mini-db-project\\src\\main\\java\\org\\example\\prop.properties")));
    }

    public Human findHumanInfoByHumanId(long id) throws SQLException {
        String sql = "SELECT h.id AS human_id, h.name, h.age, c.id AS car_id, c.model FROM humans h FULL JOIN cars c ON h.id = c.human_id WHERE h.id = ?;";

        try (Connection connection = DriverManager.getConnection(
                properties.getProperty("db-url"),
                properties.getProperty("username"),
                properties.getProperty("password"));

             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                Human human = null;
                List<Car> cars = new ArrayList<>();
                Long humanId = null;
                String humanName = null;
                Integer humanAge = null;

                while (resultSet.next()) {
                    if (human == null) {
                        humanId = resultSet.getLong("human_id");
                        humanName = resultSet.getString("name");
                        humanAge = resultSet.getInt("age");
                        human = new Human(humanId, humanName, humanAge, cars);
                    }
                    long car_id = resultSet.getLong("car_id");

                    if (!resultSet.wasNull()) {
                        cars.add(new Car(car_id, resultSet.getString("model"), humanId));
                    }
                }
                System.out.println(human);
                return human;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding human by id: " + id, e);
        }
    }

    public Collection<Human> findAllHumans() {
        String sql = "SELECT h.id AS human_id, h.name, h.age, c.id AS car_id, c.model FROM humans h FULL JOIN cars c ON h.id = c.human_id;";

        try (Connection connection = DriverManager.getConnection(
                properties.getProperty("db-url"),
                properties.getProperty("username"),
                properties.getProperty("password"));
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            HashMap<Long, Human> map = new HashMap<>();

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    Long humanId = resultSet.getLong("human_id");
                    String humanName = resultSet.getString("name");
                    Integer humanAge = resultSet.getInt("age");

                    Long carId = resultSet.getLong("car_id");
                    String model = resultSet.getString("model");

                    Human human = map.get(humanId);

                    if (human == null) {
                        List<Car> cars = new ArrayList<>();
                        if (model != null) {
                            cars.add(new Car(carId, model, humanId));
                        }
                        map.put(humanId, new Human(humanId, humanName, humanAge, cars));
                    } else {
                        human.getCars().add(new Car(carId, model, humanId));
                    }
                }

                System.out.println(map.values());
                return map.values();

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}





