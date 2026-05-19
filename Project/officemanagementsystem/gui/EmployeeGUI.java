package officemanagementsystem.gui;
 
import officemanagementsystem.entity.Employee;
import officemanagementsystem.fileio.EmployeeFileIO;
 
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
 
public class EmployeeGUI extends JFrame {
 
    private JTextField idField;
    private JTextField nameField;
    private JTextField ageField;
    private JTextField rankField;
    private JTextField searchField;
 
    private JTable table;
    private DefaultTableModel tableModel;
 
    public EmployeeGUI() {
 
        setTitle("Office Management System");
        setSize(850, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
 
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 8, 8));
 
        inputPanel.setBorder(BorderFactory.createTitledBorder("Employee Details"));
        inputPanel.add(new JLabel("ID (exactly 8 digits):"));
 
        idField = new JTextField();
 
        inputPanel.add(idField);
        inputPanel.add(new JLabel("Name:"));
 
        nameField = new JTextField();
 
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Age:"));
 
        ageField = new JTextField();
 
        inputPanel.add(ageField);
        inputPanel.add(new JLabel("Rank:"));
 
        rankField = new JTextField();
        inputPanel.add(rankField);
 
        JPanel searchPanel = new JPanel(new BorderLayout(5, 5));
 
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search (by ID or Name)"));
        searchField = new JTextField();
 
        JButton searchBtn = new JButton("Search");
 
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchBtn, BorderLayout.EAST);
 
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
       
        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");
        JButton viewAllBtn = new JButton("View All");
        JButton clearBtn = new JButton("Clear");
 
        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(viewAllBtn);
        buttonPanel.add(clearBtn);
 
        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
 
        topPanel.add(searchPanel, BorderLayout.NORTH);
        topPanel.add(buttonPanel, BorderLayout.CENTER);
        topPanel.add(inputPanel, BorderLayout.SOUTH);
       
 
        JPanel northPanel = new JPanel(new BorderLayout());
 
        northPanel.add(topPanel, BorderLayout.CENTER);
       
 
        String[] columns = {"ID", "Name", "Age", "Rank"};
 
        tableModel = new DefaultTableModel(columns, 0) {
 
            @Override
            public boolean isCellEditable(int row, int column) {
 
                return false;
            }
        };
 
        table = new JTable(tableModel);
        table.setRowHeight(22);
 
        JScrollPane scrollPane = new JScrollPane(table);
 
        scrollPane.setBorder(BorderFactory.createTitledBorder("Employee Records"));
 
        add(northPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
 
        addBtn.addActionListener(e -> addEmployee());
        updateBtn.addActionListener(e -> updateEmployee());
        deleteBtn.addActionListener(e -> deleteEmployee());
        searchBtn.addActionListener(e -> searchEmployee());
        viewAllBtn.addActionListener(e -> {
 
            searchField.setText("");
            viewAll();
        });
 
        clearBtn.addActionListener(e -> clearFields());
        table.getSelectionModel().addListSelectionListener(e -> {
 
            int row = table.getSelectedRow();
 
            if (row >= 0) {
 
                idField.setText(String.valueOf(tableModel.getValueAt(row, 0)));
                nameField.setText(String.valueOf(tableModel.getValueAt(row, 1)));
                ageField.setText(String.valueOf(tableModel.getValueAt(row, 2)));
                rankField.setText(String.valueOf(tableModel.getValueAt(row, 3)));
            }
        });
 
        try {
            EmployeeFileIO.createFileIfNotExists();
        }
        catch (IOException ex) {
            showError("Error creating file: " + ex.getMessage());
        }
 
        viewAll();
        setLocationRelativeTo(null);
        setVisible(true);
    }
 
    private boolean isValidId(String id) {
 
        if (id.isEmpty()) {
            showError("ID is required!");
            return false;
        }
 
        if (!id.matches("\\d{8}")) {
            showError("ID must be exactly 8 digits (numbers only).\n"
                    + "Minimum: 8 digits, Maximum: 8 digits.");
            return false;
        }
 
        return true;
    }
 
    private boolean isValidAllFields(String id, String name, String age, String rank) {
 
        if (name.isEmpty() || age.isEmpty() || rank.isEmpty()) {
            showError("All fields are required!");
            return false;
        }
 
        if (!isValidId(id)) {
            return false;
        }
 
        if (name.contains(",") || age.contains(",") || rank.contains(",")) {
            showError("Commas is not allowed!");
            return false;
        }
 
        try {
            Integer.parseInt(age);
        }
        catch (NumberFormatException ex) {
            showError("Age must be a number!");
            return false;
        }
 
        return true;
    }
 
    private void addEmployee() {
 
        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String age = ageField.getText().trim();
        String rank = rankField.getText().trim();
 
        if (!isValidAllFields(id, name, age, rank)) {
            return;
        }
 
        if (EmployeeFileIO.idExists(id)) {
            showError("Duplicate ID! A employee with ID \" + id + \" already exists.");
            return;
        }
 
        try {
            EmployeeFileIO.addEmployee(new Employee(id, name, age, rank));
 
            showInfo("Employee added successfully!");
            clearFields();
            viewAll();
        }
        catch (IOException ex) {
            showError("Error: " + ex.getMessage());
        }
    }
 
    private void updateEmployee() {
 
        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String age = ageField.getText().trim();
        String rank = rankField.getText().trim();
 
        if (!isValidAllFields(id, name, age, rank)) {
            return;
        }
 
        try {
            boolean updated = EmployeeFileIO.updateEmployee(
                    new Employee(id, name, age, rank));
 
            if (updated) {
                showInfo("Employee updated successfully!");
                clearFields();
                viewAll();
            }
            else {
                showError("Employee ID not found!");
            }
        }
        catch (IOException ex) {
            showError("Error: " + ex.getMessage());
        }
    }
 
    private void deleteEmployee() {
        String id = idField.getText().trim();
 
        if (!isValidId(id)) {
            return;
        }
 
        int confirm = JOptionPane.showConfirmDialog( this,
                "Are you sure you want to delete Employee ID: " + id + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );
 
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
 
        try {
            boolean deleted = EmployeeFileIO.deleteEmployee(id);
 
            if (deleted) {
                showInfo("Employee deleted successfully!");
                clearFields();
                viewAll();
            }
            else {
                showError("Employee ID not found!");
            }
        }
        catch (IOException ex) {
            showError("Error: " + ex.getMessage());
        }
    }
 
    private void searchEmployee() {
        String keyword = searchField.getText().trim();
 
        if (keyword.isEmpty()) {
            showError("Enter ID or Name to search!");
            return;
        }
 
        Object[][] results = EmployeeFileIO.searchEmployees(keyword);
        tableModel.setRowCount(0);
 
        for (int i = 0; i < results.length; i++) {
            tableModel.addRow(results[i]);
        }
       
        if (results.length == 0)
            showInfo("No matching employee found.");
    }
 
    private void viewAll() {
 
        Object[][] rows = EmployeeFileIO.getAllEmployees();
        tableModel.setRowCount(0);
 
        for (int i = 0; i < rows.length; i++) {
            if (rows[i][0] != null) {
                tableModel.addRow(rows[i]);
            }
        }
    }
 
    private void clearFields() {
 
        idField.setText("");
        nameField.setText("");
        ageField.setText("");
        rankField.setText("");
        searchField.setText("");
        table.clearSelection();
    }
 
    private void showInfo(String msg) {
 
        JOptionPane.showMessageDialog( this, msg, "Info",
                JOptionPane.INFORMATION_MESSAGE);
    }
 
    private void showError(String msg) {
 
        JOptionPane.showMessageDialog( this, msg, "Error",
                JOptionPane.ERROR_MESSAGE);
    }
}
 