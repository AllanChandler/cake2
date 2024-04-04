package app.controllers;

import app.entities.Order;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import io.javalin.http.Context;
import io.javalin.Javalin;

import java.util.List;
public class OrderController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("/orderList", ctx -> showOrderList(ctx, connectionPool));
    }

    public static void showOrderList(Context ctx, ConnectionPool connectionPool) {
        try {
            // Retrieve the order list from the database using your connectionPool
            List<Order> orderList = OrderMapper.getOrders(connectionPool);

            // Add the order list to the context
            ctx.attribute("orderList", orderList);

            // Render the order list view
            ctx.render("Order.html");
        } catch (DatabaseException e) {
            // Handle database exception
            ctx.status(500).result("Error retrieving order list: " + e.getMessage());
        }
    }
}
