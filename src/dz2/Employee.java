package dz2;

import java.util.Arrays;
import java.util.List;

public class Employee {
        private String name;
        private int age;
        private String position;

        public Employee(String name, int age, String position) {
            this.name = name;
            this.age = age;
            this.position = position;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        public String getPosition() {
            return position;
        }

        @Override
        public String toString() {
            return "Employee{name='" + name + "', age=" + age + ", position='" + position + "'}";
        }

        public static List<Employee> createEmployeeList() {
        return Arrays.asList(
                new Employee("Alice", 45, "Engineer"),
                new Employee("Bob", 38, "Manager"),
                new Employee("Charlie", 52, "Engineer"),
                new Employee("Diana", 29, "Designer"),
                new Employee("Eve", 41, "Engineer"),
                new Employee("Frank", 31, "Engineer"),
                new Employee("Grace", 47, "Engineer"),
                new Employee("Henry", 55, "Director"),
                new Employee("Ivy", 36, "Engineer"),
                new Employee("Jack", 50, "Engineer")
        );
    }
}
