package org.example;

import java.io.IOException;
import java.sql.SQLException;


public class Main {
    public static void main(String[] args) throws IOException, SQLException {
        DAO dao = new DAO();
        dao.findHumanInfoByHumanId(3);
        dao.findAllHumans();
    }
}