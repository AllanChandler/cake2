package app.persistence;

import app.entities.Order;
import app.exceptions.DatabaseException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class CustomerMapper {
    public static List<Order> getCustomer(ConnectionPool connectionPool) throws DatabaseException {
        List<Order> customerList = new ArrayList<>();
        String sql = "SELECT order_id, antal, sum, user_id FROM public.order"; // Tilføj user_id til SELECT-sætningen

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                int antal = rs.getInt("antal");
                double sum = rs.getDouble("sum");
                int userId = rs.getInt("user_id"); // Tilføj user_id til ResultSet
                customerList.add(new Order(orderId, antal, sum, userId)); // Tilføj user_id til Order-objektet
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving orders: " + e.getMessage());
        }
        return customerList;
    }
}