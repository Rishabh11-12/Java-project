import java.io.*;
import java.util.*;

public class StudentManager {
    private List<Student> students = new ArrayList<>();

    public void addStudent(Student s) {
        students.add(s);
    }

    public void removeStudent(int index) {
        students.remove(index);
    }

    public List<Student> getStudents() {
        return students;
    }

    public void updateStudent(int index, Student updated) {
        students.set(index, updated);
    }

    public void saveToFile(String filename) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(students);
        }
    }

    @SuppressWarnings("unchecked")
    public void loadFromFile(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            students = (List<Student>) in.readObject();
        }
    }

    public Student getTopper() {
        return students.stream().max(Comparator.comparing(Student::getMarks)).orElse(null);
    }

    public Student getLowest() {
        return students.stream().min(Comparator.comparing(Student::getMarks)).orElse(null);
    }

    public double getAverageMarks() {
        return students.stream().mapToInt(Student::getMarks).average().orElse(0.0);
    }

    public List<Student> search(String keyword) {
        keyword = keyword.toLowerCase();
        List<Student> result = new ArrayList<>();
        for (Student s : students) {
            if (s.getName().toLowerCase().contains(keyword)) {
                result.add(s);
            }
        }
        return result;
    }

    public void sortByName() {
        students.sort(Comparator.comparing(Student::getName));
    }

    public void sortByMarks() {
        students.sort(Comparator.comparing(Student::getMarks).reversed());
    }

    public void exportReport(String filename) throws IOException {
        try (PrintWriter writer = new PrintWriter(filename)) {
            writer.println("Student Report:");
            writer.println("--------------------------");
            for (Student s : students) {
                writer.printf("Name: %-20s Marks: %3d Grade: %s%n", s.getName(), s.getMarks(), s.getGrade());
            }
            writer.printf("%nTopper: %s (%d)%n", getTopper().getName(), getTopper().getMarks());
            writer.printf("Lowest: %s (%d)%n", getLowest().getName(), getLowest().getMarks());
            writer.printf("Class Average: %.2f%n", getAverageMarks());
        }
    }
}