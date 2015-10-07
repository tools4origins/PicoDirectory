package org.erwan.picodirectory;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.handlebars.HandlebarsTemplateEngine;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.ArrayList;
import java.util.Optional;

// We could used import static spark.Spark.*; but for me the Joker must stay a Batman villains.
import static spark.Spark.after;
import static spark.Spark.get;
import static spark.Spark.delete;
import static spark.Spark.put;

public class PicoDirectory {
    public static void main(String[] args) {
        ArrayList<User> users = new ArrayList<>();
        users.add(new User("TOUILLE Sacha", "sacha.touille@example.com"));
        users.add(new User("POTE Jack", "jack.pote@example.com"));
        users.add(new User("ERPOLO Walt", "walt.erpolo@example.com"));

        get("/Directory/", (request, response) -> users, new XMLTransformer());

        get("/Directory/:email", (request, response) -> {
            Optional<User> user = getUserByMail(users, request);
            return user.isPresent() ? user : handleUserNotFound(request, response);
        }, new XMLTransformer());

        delete("/Directory/:email", (request, response) -> {
            Optional<User> user = getUserByMail(users, request);

            if (user.isPresent()) {
                users.remove(user.get());
                return emptyResponse(response);
            } else {
                return handleUserNotFound(request, response);
            }
        });

        put("/Directory/", (request, response) -> {
            try {
                String email = request.params("email");
                new InternetAddress(email).validate();
                users.add(new User(request.params("name"), request.params("email")));
                return emptyResponse(response);
            } catch (AddressException e) {
                response.status(400);
                return "Email is not a valid email : " + e.getMessage() + ".";
            }
        });
    }

    private static Object emptyResponse(Response response) {
        response.status(201);
        return "";
    }

    private static Optional<User> getUserByMail(ArrayList<User> users, Request request) {
        return users.stream()
                .filter(u -> u.getEmail().equals(request.params("email")))
                .findAny();
    }

    private static Object handleUserNotFound(Request request, Response response) {
        response.status(404);
        return "User with email " + request.params(":email") + " not found :(";
    }
}
