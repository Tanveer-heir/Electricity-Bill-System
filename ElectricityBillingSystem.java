import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class ElectricityBillingSystem extends JFrame implements ActionListener {
    private JTextField meterField, nameField, prevField, currField;
    private JButton calcBtn, saveBtn, resetBtn;
    private JTextArea resultArea;

    public ElectricityBillingSystem() {
        setTitle("Electricity Billing System");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));

       
        panel.add(new JLabel("Meter Number:"));
        meterField = new JTextField();
        panel.add(meterField);

        panel.add(new JLabel("Consumer Name:"));
        nameField = new JTextField();
        panel.add(nameField);

        panel.add(new JLabel("Previous Reading:"));
        prevField = new JTextField();
        panel.add(prevField);

        panel.add(new JLabel("Current Reading:"));
        currField = new JTextField();
        panel.add(currField);

        
        calcBtn = new JButton("Calculate Bill");
        calcBtn.addActionListener(this);
        panel.add(calcBtn);

        saveBtn = new JButton("Save Bill");
        saveBtn.addActionListener(this);
        saveBtn.setEnabled(false);
        panel.add(saveBtn);

        resetBtn = new JButton("Reset");
        resetBtn.addActionListener(this);
        panel.add(resetBtn);

        resultArea = new JTextArea(6, 20);
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        add(panel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private String calculateBill(String meterNum, String name, String prev, String curr) {
        int prevReading, currReading;
        try {
            prevReading = Integer.parseInt(prev);
            currReading = Integer.parseInt(curr);
            if (currReading < prevReading) {
                return "Error: Current reading cannot be less than previous reading!";
            }
            if (prevReading < 0 || currReading < 0)
                return "Error: Meter readings should be non-negative!";
        } catch (NumberFormatException ex) {
            return "Error: Invalid reading format.";
        }

        int units = currReading - prevReading;
        double bill = 0;

        if (units <= 100) bill = units * 5;
        else if (units <= 200) bill = 100 * 5 + (units - 100) * 7;
        else if (units <= 300) bill = 100 * 5 + 100 * 7 + (units - 200) * 10;
        else bill = 100 * 5 + 100 * 7 + 100 * 10 + (units - 300) * 12;

        // Simple summary
        return "----- ELECTRICITY BILL -----\n"
                + "Meter No: " + meterNum + "\n"
                + "Consumer Name: " + name + "\n"
                + "Units Consumed: " + units + "\n"
                + "Amount Payable: Rs. " + bill + "\n"
                + "---------------------------\n";
    }

    // Save bill to file
    private void saveBillToFile(String billDetails) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("bills.txt", true))) {
            writer.write(billDetails);
            writer.write("\n");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error saving bill to file.");
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == calcBtn) {
            String meterNum = meterField.getText();
            String name = nameField.getText();
            String prev = prevField.getText();
            String curr = currField.getText();

            String bill = calculateBill(meterNum, name, prev, curr);
            resultArea.setText(bill);

            if (!bill.startsWith("Error")) {
                saveBtn.setEnabled(true);
            } else {
                saveBtn.setEnabled(false);
            }
        } else if (e.getSource() == saveBtn) {
            String billDetails = resultArea.getText();
            saveBillToFile(billDetails);
            JOptionPane.showMessageDialog(this, "Bill saved successfully!");
            saveBtn.setEnabled(false);
        } else if (e.getSource() == resetBtn) {
            meterField.setText("");
            nameField.setText("");
            prevField.setText("");
            currField.setText("");
            resultArea.setText("");
            saveBtn.setEnabled(false);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ElectricityBillingSystem().setVisible(true));
    }
}
