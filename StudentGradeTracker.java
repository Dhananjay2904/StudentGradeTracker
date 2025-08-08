 import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class StudentGradeTracker extends JFrame {
    private JTextField nameField, subjectField, marksField;
    private JTable table;
    private DefaultTableModel tableModel;
    private static final String FILE_NAME = "grades.txt";

    public StudentGradeTracker() {
        setTitle("Student Grade Tracker");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Top Panel for input
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());

        nameField = new JTextField(10);
        subjectField = new JTextField(10);
        marksField = new JTextField(5);
        JButton addButton = new JButton("Add");

        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Subject:"));
        inputPanel.add(subjectField);
        inputPanel.add(new JLabel("Marks:"));
        inputPanel.add(marksField);
        inputPanel.add(addButton);

        // Table setup
        tableModel = new DefaultTableModel(new String[]{"Name", "Subject", "Marks"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        addButton.addActionListener(e -> addEntry());
        loadDataFromFile();
    }

    private void addEntry() {
        String name = nameField.getText().trim();
        String subject = subjectField.getText().trim();
        String marksStr = marksField.getText().trim();

        if (name.isEmpty() || subject.isEmpty() || marksStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return;
        }

        try {
            int marks = Integer.parseInt(marksStr);
            tableModel.addRow(new Object[]{name, subject, marks});
            saveToFile(name, subject, marks);
            nameField.setText("");
            subjectField.setText("");
            marksField.setText("");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for marks!");
        }
    }

    private void saveToFile(String name, String subject, int marks) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(name + "," + subject + "," + marks);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadDataFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    tableModel.addRow(new Object[]{parts[0], parts[1], parts[2]});
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentGradeTracker().setVisible(true));
    }
}
