
package app.store;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import app.model.Student;

public class HazelcastStore {
    static HazelcastInstance hz;
    static IMap<String, Student> map;

    public static void init() {
        hz = HazelcastClient.newHazelcastClient();
        map = hz.getMap("students");
        System.out.println("Hazelcast: Inserting 10,000 students...");
        for (int i = 0; i < 10000; i++) {
            String id = "2025" + String.format("%06d", i);
            Student s = new Student(id, "Student Name " + i, "Computer Science");
            map.put(id, s);
        }
        System.out.println("Hazelcast: 10,000 students inserted successfully!");
    }

    public static Student get(String id) {
        return map.get(id);
    }
}
