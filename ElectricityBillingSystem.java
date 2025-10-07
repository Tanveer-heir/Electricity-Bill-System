import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.*;
import java.util.*;

public class ElectricityBillingSystem extends JFrame implements ActionListener, KeyListener {
    private JTextField meterField, nameField, addressField, phoneField, prevField, currField;
    private JButton calcBtn, saveBtn, resetBtn, viewHistoryBtn, printBtn, clearBillBtn, settingsBtn;
    private JTextArea resultArea;
    private JCheckBox autoCalcBox;

    // Tariff (can be modified at runtime)
    private double[] rates = { 5, 7, 10, 12 };
    private double baseCharge = 50;
    private double taxPercent = 8;
    private double overuseSurchargePercent = 5;

    public ElectricityBillingSystem() {
        setTitle("Electricity Billing System");
        setSize(650, 540);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Title (optional decoration)
        JLabel titleLbl = new JLabel("Electricity Billing System", SwingConstants.CENTER);
        titleLbl.setFont(new Font("Serif", Font.BOLD, 22));
        titleLbl.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(titleLbl, BorderLayout.NORTH);

        // ----------- INPUT PANEL -----------
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Consumer & Meter Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy = 0;

        inputPanel.add(new JLabel("Meter Number:"), gbc);
        gbc.gridx = 1;
        meterField = new JTextField(13);
        inputPanel.add(meterField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Consumer Name:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(13);
        inputPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1;
        addressField = new JTextField(13);
        inputPanel.add(addressField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        phoneField = new JTextField(13);
        inputPanel.add(phoneField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Previous Reading:"), gbc);
        gbc.gridx = 1;
        prevField = new JTextField(13);
        prevField.addKeyListener(this);
        inputPanel.add(prevField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        inputPanel.add(new JLabel("Current Reading:"), gbc);
        gbc.gridx = 1;
        currField = new JTextField(13);
        currField.addKeyListener(this);
        inputPanel.add(currField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        autoCalcBox = new JCheckBox("Auto Calculate");
        gbc.gridwidth = 2;
        inputPanel.add(autoCalcBox, gbc);

        add(inputPanel, BorderLayout.WEST);

        // ----------- OUTPUT PANEL -----------
        resultArea = new JTextArea(16, 30);
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Bill Output"));
        add(scrollPane, BorderLayout.CENTER);

        // ----------- BUTTON PANEL -----------
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 10)); // nice spacing
        calcBtn = new JButton("Calculate Bill");
        saveBtn = new JButton("Save Bill");
        resetBtn = new JButton("Reset Form");
        clearBillBtn = new JButton("Clear Bill");
        viewHistoryBtn = new JButton("View Bill History");
        printBtn = new JButton("Print Bill");
        settingsBtn = new JButton("Tariff Settings");

        btnPanel.add(calcBtn);
        btnPanel.add(saveBtn);
        btnPanel.add(resetBtn);
        btnPanel.add(clearBillBtn);
        btnPanel.add(printBtn);
        btnPanel.add(viewHistoryBtn);
        btnPanel.add(settingsBtn);

        add(btnPanel, BorderLayout.SOUTH);

        // ---- Action Listeners ----
        calcBtn.addActionListener(this);
        saveBtn.setEnabled(false);
        saveBtn.addActionListener(this);
        resetBtn.addActionListener(this);
        clearBillBtn.addActionListener(this);
        printBtn.addActionListener(this);
        printBtn.setEnabled(false);
        viewHistoryBtn.addActionListener(this);
        settingsBtn.addActionListener(this);

        // Consistent look & feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
    }

    // [calculateBill, saveBillToFile, showBillHistory, showTariffSettingsDialog,
    // printBill methods unchanged...]

    private String calculateBill(String meterNum, String name, String addr, String phone, String prev, String curr) {
        int prevReading, currReading;
        try {
            prevReading = Integer.parseInt(prev);
            currReading = Integer.parseInt(curr);
            if (currReading < prevReading)
                return "Error: Current reading cannot be less than previous reading!";
            if (prevReading < 0 || currReading < 0)
                return "Error: Meter readings should be non-negative!";
        } catch (NumberFormatException ex) {
            return "Error: Invalid reading format.";
        }
        int units = currReading - prevReading;

        double bill = baseCharge, cost = 0;
        if (units <= 100)
            cost = units * rates[0];
        else if (units <= 200)
            cost = 100 * rates[0] + (units - 100) * rates[1];
        else if (units <= 300)
            cost = 100 * rates[0] + 100 * rates[1] + (units - 200) * rates[2];
        else
            cost = 100 * rates[0] + 100 * rates[1] + 100 * rates[2] + (units - 300) * rates[3];

        bill += cost;
        double tax = (taxPercent / 100.0) * bill;
        bill += tax;
        double surcharge = 0;
        if (units > 300) {
            surcharge = (overuseSurchargePercent / 100.0) * bill;
            bill += surcharge;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String billDate = sdf.format(new Date());
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 15);
        String dueDate = sdf.format(cal.getTime());

        return "----- ELECTRICITY BILL -----\n"
                + "Bill Date: " + billDate + "\n"
                + "Due Date: " + dueDate + "\n"
                + "Meter No: " + meterNum + "\n"
                + "Consumer Name: " + name + "\n"
                + "Address: " + addr + "\n"
                + "Phone: " + phone + "\n"
                + "Units Consumed: " + units + "\n"
                + String.format("Base Charge: Rs. %.2f\n", baseCharge)
                + String.format("Energy Charge: Rs. %.2f\n", cost)
                + String.format("Tax (%.1f%%): Rs. %.2f\n", taxPercent, tax)
                + (surcharge > 0 ? String.format("Surcharge (%.1f%%): Rs. %.2f\n", overuseSurchargePercent, surcharge)
                        : "")
                + "------------------------------\n"
                + String.format("Total Amount Payable: Rs. %.2f\n", bill)
                + "-----------------------------\n";
    }

    private void saveBillToFile(String billDetails) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("bills.txt", true))) {
            writer.write(billDetails);
            writer.write("\n");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error saving bill to file.");
        }
    }

    private void showBillHistory() {
        try (BufferedReader reader = new BufferedReader(new FileReader("bills.txt"))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
                sb.append(line).append("\n");
            JTextArea histArea = new JTextArea(sb.toString());
            histArea.setEditable(false);
            JScrollPane scroll = new JScrollPane(histArea);
            scroll.setPreferredSize(new Dimension(430, 330));
            JOptionPane.showMessageDialog(this, scroll, "All Bills", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "No bill history found.");
        }
    }

    private void showTariffSettingsDialog() {
        JTextField[] fields = new JTextField[4];
        for (int i = 0; i < 4; ++i)
            fields[i] = new JTextField(String.valueOf(rates[i]));
        JTextField baseField = new JTextField(String.valueOf(baseCharge));
        JTextField taxField = new JTextField(String.valueOf(taxPercent));
        JTextField surchargeField = new JTextField(String.valueOf(overuseSurchargePercent));
        Object[] inputs = {
                "First 100 units:", fields[0],
                "Next 100 units:", fields[1],
                "Next 100 units:", fields[2],
                "Above 300 units:", fields[3],
                "Base Charge:", baseField,
                "Tax (%)", taxField,
                "Surcharge (%) for units > 300:", surchargeField
        };
        int op = JOptionPane.showConfirmDialog(this, inputs, "Modify Tariff", JOptionPane.OK_CANCEL_OPTION);
        if (op == JOptionPane.OK_OPTION) {
            try {
                for (int i = 0; i < 4; ++i)
                    rates[i] = Double.parseDouble(fields[i].getText());
                baseCharge = Double.parseDouble(baseField.getText());
                taxPercent = Double.parseDouble(taxField.getText());
                overuseSurchargePercent = Double.parseDouble(surchargeField.getText());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Invalid entry!");
            }
        }
    }

    private void printBill() {
        try {
            resultArea.print();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Print failed.");
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == calcBtn) {
            String bill = calculateBill(meterField.getText(), nameField.getText(), addressField.getText(),
                    phoneField.getText(), prevField.getText(), currField.getText());
            resultArea.setText(bill);
            saveBtn.setEnabled(!bill.startsWith("Error"));
            printBtn.setEnabled(!bill.startsWith("Error"));
        } else if (e.getSource() == saveBtn) {
            saveBillToFile(resultArea.getText());
            JOptionPane.showMessageDialog(this, "Bill saved successfully!");
            saveBtn.setEnabled(false);
        } else if (e.getSource() == resetBtn) {
            meterField.setText("");
            nameField.setText("");
            addressField.setText("");
            phoneField.setText("");
            prevField.setText("");
            currField.setText("");
            resultArea.setText("");
            saveBtn.setEnabled(false);
            printBtn.setEnabled(false);
        } else if (e.getSource() == viewHistoryBtn) {
            showBillHistory();
        } else if (e.getSource() == printBtn) {
            printBill();
        } else if (e.getSource() == clearBillBtn) {
            resultArea.setText("");
            printBtn.setEnabled(false);
        } else if (e.getSource() == settingsBtn) {
            showTariffSettingsDialog();
        }
    }

    public void keyReleased(KeyEvent e) {
        if (autoCalcBox.isSelected()) {
            String bill = calculateBill(meterField.getText(), nameField.getText(), addressField.getText(),
                    phoneField.getText(), prevField.getText(), currField.getText());
            resultArea.setText(bill);
            boolean ok = !bill.startsWith("Error");
            saveBtn.setEnabled(ok);
            printBtn.setEnabled(ok);
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ElectricityBillingSystem().setVisible(true));
    }
}
