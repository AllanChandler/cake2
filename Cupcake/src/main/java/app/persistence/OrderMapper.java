package app.persistence;

import app.entities.Order;
import app.exceptions.DatabaseException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class OrderMapper {
    public static List<Order> getOrders(ConnectionPool connectionPool) throws DatabaseException {
        List<Order> orderList = new ArrayList<>();
        String sql = "SELECT order_id, antal, sum FROM public.order"; // Fjern user_id fra SELECT-sætningen

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                int antal = rs.getInt("antal");
                double sum = rs.getDouble("sum");
                // Fjern user_id fra ResultSet og add metoden til Order-objektet
                orderList.add(new Order(orderId, antal, sum));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving orders: " + e.getMessage());
        }
        return orderList;
    }
}
