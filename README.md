# Electricity Billing System (Java Swing GUI)

A complete desktop application for calculating, viewing, and managing consumer electricity bills with a modern graphical interface in Java.

## Features

- **Input Consumer Details**  
  Enter meter number, name, address, phone, previous and current readings.

- **Smart Billing Calculation**  
  Slab-based calculation with settings for:
  - Rates for each consumption slab (editable)
  - Fixed base charge
  - Bill tax (percentage, editable)
  - Overuse surcharge (for high consumption)

- **Real-Time Calculation**  
  Option to auto-calculate bill as you type readings.

- **Save/Load/Print Bills**  
  - Save calculated bills to a file
  - View all previously saved bills
  - Print any bill via system printer

- **History & Tariff Management**  
  - View full bill history in a pop-up dialog
  - Easily modify tariff rates and all calculation parameters in-app

- **Modern, Organized GUI**  
  - Grouped input panels
  - Large, clear bill output section
  - Buttons for all actions with tooltips

## How to Run

1. **Clone or Download the Project**

2. **Compile**
    ```
    javac ElectricityBillingSystem.java
    ```

3. **Run**
    ```
    java ElectricityBillingSystem
    ```

## Usage

- Fill in all customer details and meter readings.
- Click **Calculate Bill** or enable **Auto Calculate** for real-time billing.
- Review and print/save the bill, or reset form for a new entry.
- Edit tariff/rate details any time with the **Tariff Settings** button.
- Click **View Bill History** to see all previously generated bills.

## Data Files

- Bills are saved in a text file named `bills.txt` in the application's working directory.
- Tariff settings can be reset in every session or changed when needed.

## Customization

- All calculation rates and charges can be set via the **Tariff Settings** dialog.

## Dependencies

- Pure Java (JDK 8 or above)
- No external libraries required (uses `javax.swing` and standard Java library)

## Author

Tanveer Singh

---

*Designed for assignment/demonstration purposes. Fork and extend as desired!*
