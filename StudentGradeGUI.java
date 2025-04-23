import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;

public class StudentGradeGUI extends JFrame {
    private StudentManager manager = new StudentManager();
    private JTable table;
    private DefaultTableModel model;
    private JTextField nameField, marksField, searchField;

    public StudentGradeGUI() {
        setTitle("Student Grade Manager");
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Table
        model = new DefaultTableModel(new String[]{"Name", "Marks", "Grade"}, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Form panel
        JPanel form = new JPanel(new GridLayout(2, 4, 10, 10));
        nameField = new JTextField();
        marksField = new JTextField();
        searchField = new JTextField();

        form.add(new JLabel("Name:"));
        form.add(nameField);
        form.add(new JLabel("Marks:"));
        form.add(marksField);

        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");
        JButton searchBtn = new JButton("Search");

        form.add(addBtn);
        form.add(updateBtn);
        form.add(deleteBtn);
        form.add(searchBtn);
        add(form, BorderLayout.NORTH);

        // Button panel
        JPanel buttons = new JPanel(new FlowLayout());
        JButton saveBtn = new JButton("Save");
        JButton loadBtn = new JButton("Load");
        JButton exportBtn = new JButton("Export");
        JButton sortNameBtn = new JButton("Sort by Name");
        JButton sortMarksBtn = new JButton("Sort by Marks");
        JButton statsBtn = new JButton("Show Stats");

        buttons.add(saveBtn);
        buttons.add(loadBtn);
        buttons.add(exportBtn);
        buttons.add(sortNameBtn);
        buttons.add(sortMarksBtn);
        buttons.add(statsBtn);
        add(buttons, BorderLayout.SOUTH);

        // Action Listeners
        addBtn.addActionListener(e -> {
            String name = nameField.getText();
            int marks;
            try {
                marks = Integer.parseInt(marksField.getText());
                manager.addStudent(new Student(name, marks));
                refreshTable();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Enter valid marks.");
            }
        });

        updateBtn.addActionListener(e -> {
            int selected = table.getSelectedRow();
            if (selected >= 0) {
                String name = nameField.getText();
                int marks;
                try {
                    marks = Integer.parseInt(marksField.getText());
                    manager.updateStudent(selected, new Student(name, marks));
                    refreshTable();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Enter valid marks.");
                }
            }
        });

        deleteBtn.addActionListener(e -> {
            int selected = table.getSelectedRow();
            if (selected >= 0) {
                manager.removeStudent(selected);
                refreshTable();
            }
        });

        searchBtn.addActionListener(e -> {
            String keyword = nameField.getText();
            List<Student> results = manager.search(keyword);
            model.setRowCount(0);
            for (Student s : results) {
                model.addRow(new Object[]{s.getName(), s.getMarks(), s.getGrade()});
            }
        });

        saveBtn.addActionListener(e -> {
            try {
                manager.saveToFile("students.dat");
                JOptionPane.showMessageDialog(this, "Saved successfully.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error saving file.");
            }
        });

        loadBtn.addActionListener(e -> {
            try {
                manager.loadFromFile("students.dat");
                refreshTable();
                JOptionPane.showMessageDialog(this, "Loaded successfully.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error loading file.");
            }
        });

        exportBtn.addActionListener(e -> {
            try {
                manager.exportReport("student_report.txt");
                JOptionPane.showMessageDialog(this, "Report exported.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Export failed.");
            }
        });

        sortNameBtn.addActionListener(e -> {
            manager.sortByName();
            refreshTable();
        });

        sortMarksBtn.addActionListener(e -> {
            manager.sortByMarks();
            refreshTable();
        });

        statsBtn.addActionListener(e -> {
            Student topper = manager.getTopper();
            Student lowest = manager.getLowest();
            double avg = manager.getAverageMarks();
            String msg = String.format("Topper: %s (%d)\nLowest: %s (%d)\nAverage: %.2f",
                topper.getName(), topper.getMarks(),
                lowest.getName(), lowest.getMarks(), avg);
            JOptionPane.showMessageDialog(this, msg);
        });
    }

    private void refreshTable() {
        model.setRowCount(0);
        for (Student s : manager.getStudents()) {
            model.addRow(new Object[]{s.getName(), s.getMarks(), s.getGrade()});
        }
    }
}