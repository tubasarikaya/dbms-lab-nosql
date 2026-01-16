
package app;

import static spark.Spark.*;
import com.google.gson.Gson;
import app.store.*;
import app.model.Student;

public class Main {
    public static void main(String[] args) {
        ipAddress("0.0.0.0");  // Listen on all network interfaces
        port(8080);
        Gson gson = new Gson();

        System.out.println("Starting NoSQL Lab Application...");
        System.out.println("Initializing data stores...");
        
        RedisStore.init();
        HazelcastStore.init();
        MongoStore.init();

        System.out.println("All data stores initialized successfully!");
        System.out.println("Server is running on http://localhost:8080");
        System.out.println("Endpoints:");
        System.out.println("  - Redis:     http://localhost:8080/nosql-lab-rd/student_no=<id>");
        System.out.println("  - Hazelcast: http://localhost:8080/nosql-lab-hz/student_no=<id>");
        System.out.println("  - MongoDB:   http://localhost:8080/nosql-lab-mon/student_no=<id>");

        get("/nosql-lab-rd/*", (req, res) -> {
            res.type("application/json");
            String param = req.splat()[0]; // "student_no=2025000001"
            String id;
            if (param.startsWith("student_no=")) {
                id = param.substring("student_no=".length());
            } else {
                id = param;
            }
            Student student = RedisStore.get(id);
            if (student == null) {
                res.status(404);
                return gson.toJson(new ErrorResponse("Student not found: " + id));
            }
            return gson.toJson(student);
        });

        get("/nosql-lab-hz/*", (req, res) -> {
            res.type("application/json");
            String param = req.splat()[0]; // "student_no=2025000001"
            String id;
            if (param.startsWith("student_no=")) {
                id = param.substring("student_no=".length());
            } else {
                id = param;
            }
            Student student = HazelcastStore.get(id);
            if (student == null) {
                res.status(404);
                return gson.toJson(new ErrorResponse("Student not found: " + id));
            }
            return gson.toJson(student);
        });

        get("/nosql-lab-mon/*", (req, res) -> {
            res.type("application/json");
            String param = req.splat()[0]; // "student_no=2025000001"
            String id;
            if (param.startsWith("student_no=")) {
                id = param.substring("student_no=".length());
            } else {
                id = param;
            }
            Student student = MongoStore.get(id);
            if (student == null) {
                res.status(404);
                return gson.toJson(new ErrorResponse("Student not found: " + id));
            }
            return gson.toJson(student);
        });
    }
    
    static class ErrorResponse {
        String error;
        ErrorResponse(String error) {
            this.error = error;
        }
    }
}
