package org.example;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DAO {


    Properties properties;

    public DAO() throws IOException {
        this.properties = new Properties();
        this.properties.load(new BufferedReader(new FileReader("C:\\Users\\User\\IdeaProjects\\mini-db-project\\src\\main\\java\\org\\example\\prop.properties")));
    }

    public Human findHumanInfoByHumanId(long id) throws SQLException {
        try (Connection connection = DriverManager.getConnection(properties.getProperty("db-url"), properties.getProperty("username"), properties.getProperty("password"))) {
            String sql = "SELECT h.id, h.name, h.age, c.id as car_id, c.model FROM humans h LEFT JOIN cars_humans_association cha ON h.id = cha.human_id LEFT JOIN cars c ON cha.car_id = c.id WHERE h.id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);

            var resultSet = preparedStatement.executeQuery();

            Human human = null;
            List<Car> cars = new ArrayList<>();

            while (resultSet.next()) {

                if (human == null) {
                    long humanId = resultSet.getLong("id");
                    String humanName = resultSet.getString("name");
                    int humanAge = resultSet.getInt("age");
                    human = new Human(humanId, humanName, humanAge, cars);
                }

                long carId = resultSet.getLong("car_id");
                String carModel = resultSet.getString("model");

                if (carId != 0) {
                    cars.add(new Car(carId, carModel));
                }


            }
            System.out.println(human);
            return human;
        }
    }
}