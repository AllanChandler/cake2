package app;

import app.config.ThymeleafConfig;
import app.controllers.BundController;
import app.controllers.CustomerController;
import app.controllers.OrderController;
import app.controllers.ToppingController;
import app.persistence.*;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;

public class Main {

    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String URL = "jdbc:postgresql://localhost:5432/%s?currentSchema=public";
    private static final String DB = "cupcake";

    private static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);


    public static void main(String[] args) {
        // Initializing Javalin and Jetty webserver

        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public");
            config.fileRenderer(new JavalinThymeleaf(ThymeleafConfig.templateEngine()));
        }).start(7070);

        // Routing

        app.get("/", ctx -> {
            ctx.attribute("toppingList", ToppingMapper.getToppings(connectionPool));
            ctx.attribute("bundList", BundMapper.getBunds(connectionPool));
            ctx.render("Cupcake.html");
        });

        app.get("/order", ctx -> {
            ctx.attribute("orderList", OrderMapper.getOrders(connectionPool));
            ctx.render("Order.html");
        });

        app.get("/customer", ctx -> {
            ctx.attribute("customerList", CustomerMapper.getCustomer(connectionPool));
            ctx.render("Customer.html");
        });

        BundController.addRoutes(app, connectionPool);
        OrderController.addRoutes(app, connectionPool);
        ToppingController.addRoutes(app, connectionPool);
        CustomerController.addRoutes(app, connectionPool);

    }
}