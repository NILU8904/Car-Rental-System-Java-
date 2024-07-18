import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

public class VehicleRentalSystem {
    private JFrame frame;
    private JPanel panelMain;
    private CardLayout cardLayout;
    private ArrayList<Vehicle> vehicles;

    public VehicleRentalSystem() {
        frame = new JFrame("Vehicle Rental System");
        panelMain = new JPanel();
        cardLayout = new CardLayout();
        vehicles = new ArrayList<>();

        panelMain.setLayout(cardLayout);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        initializePanels();

        frame.add(panelMain);
        frame.setVisible(true);
    }

    private void initializePanels() {
        JPanel homePanel = new JPanel();
        homePanel.setLayout(new GridLayout(4, 1, 10, 10));
        homePanel.setBackground(Color.LIGHT_GRAY);

        JButton btnManageVehicles = createStyledButton("Manage Vehicles");
        btnManageVehicles.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelMain, "manageVehicles");
                ((ManageVehiclesPanel) panelMain.getComponent(1)).updateVehicleList();
            }
        });

        JButton btnRentVehicle = createStyledButton("Rent Vehicle");
        btnRentVehicle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelMain, "rentVehicle");
                ((RentVehiclePanel) panelMain.getComponent(2)).updateVehicleList();
            }
        });

        JButton btnReturnVehicle = createStyledButton("Return Vehicle");
        btnReturnVehicle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelMain, "returnVehicle");
                ((ReturnVehiclePanel) panelMain.getComponent(3)).updateVehicleList();
            }
        });

        JButton btnSearchVehicle = createStyledButton("Search Vehicle");
        btnSearchVehicle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelMain, "searchVehicle");
            }
        });

        homePanel.add(btnManageVehicles);
        homePanel.add(btnRentVehicle);
        homePanel.add(btnReturnVehicle);
        homePanel.add(btnSearchVehicle);

        panelMain.add(homePanel, "home");

        // Initialize other panels
        panelMain.add(new ManageVehiclesPanel(), "manageVehicles");
        panelMain.add(new RentVehiclePanel(), "rentVehicle");
        panelMain.add(new ReturnVehiclePanel(), "returnVehicle");
        panelMain.add(new SearchVehiclePanel(), "searchVehicle");

        cardLayout.show(panelMain, "home");
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(VehicleRentalSystem::new);
    }

    abstract class Vehicle {
        private String model;
        private String number;
        private boolean isRented;
        private String renterName;
        private String renterContact;
        private int rentalRate;

        public Vehicle(String model, String number, int rentalRate) {
            this.model = model;
            this.number = number;
            this.isRented = false;
            this.renterName = "";
            this.renterContact = "";
            this.rentalRate = rentalRate;
        }

        public String getModel() {
            return model;
        }

        public String getNumber() {
            return number;
        }

        public boolean isRented() {
            return isRented;
        }

        public void rent(String renterName, String renterContact) {
            this.isRented = true;
            this.renterName = renterName;
            this.renterContact = renterContact;
        }

        public void returnVehicle() {
            this.isRented = false;
            this.renterName = "";
            this.renterContact = "";
        }

        public int getRentalRate() {
            return rentalRate;
        }

        @Override
        public String toString() {
            return model + " (" + number + ")";
        }
    }

    class Car extends Vehicle {
        public Car(String model, String number) {
            super(model, number, 100 + new Random().nextInt(900)); // Random rate between 100 and 1000 rupees
        }
    }

    class Bike extends Vehicle {
        public Bike(String model, String number) {
            super(model, number, 50 + new Random().nextInt(450)); // Random rate between 50 and 500 rupees
        }
    }

    class ManageVehiclesPanel extends JPanel {
        private JTextField txtModel;
        private JTextField txtNumber;
        private JComboBox<String> comboVehicleType;
        private JTextArea txtAreaVehicles;

        public ManageVehiclesPanel() {
            setLayout(new BorderLayout(10, 10));
            setBackground(Color.WHITE);

            txtModel = new JTextField(20);
            txtNumber = new JTextField(20);
            comboVehicleType = new JComboBox<>(new String[]{"Car", "Bike"});
            JButton btnAddVehicle = createStyledButton("Add Vehicle");
            btnAddVehicle.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String model = txtModel.getText();
                    String number = txtNumber.getText();
                    String vehicleType = (String) comboVehicleType.getSelectedItem();
                    if (!model.isEmpty() && !number.isEmpty()) {
                        if (vehicleType.equals("Car")) {
                            vehicles.add(new Car(model, number));
                        } else {
                            vehicles.add(new Bike(model, number));
                        }
                        updateVehicleList();
                        txtModel.setText("");
                        txtNumber.setText("");
                        JOptionPane.showMessageDialog(frame, "Vehicle added successfully!");
                    } else {
                        JOptionPane.showMessageDialog(frame, "Vehicle model and number cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            txtAreaVehicles = new JTextArea();
            txtAreaVehicles.setEditable(false);
            txtAreaVehicles.setFont(new Font("Arial", Font.PLAIN, 14));
            txtAreaVehicles.setBackground(new Color(240, 240, 240));

            JPanel inputPanel = new JPanel();
            inputPanel.setLayout(new GridLayout(5, 1, 10, 10));
            inputPanel.setBackground(Color.WHITE);
            inputPanel.add(new JLabel("Vehicle Model:"));
            inputPanel.add(txtModel);
            inputPanel.add(new JLabel("Vehicle Number:"));
            inputPanel.add(txtNumber);
            inputPanel.add(new JLabel("Vehicle Type:"));
            inputPanel.add(comboVehicleType);
            inputPanel.add(btnAddVehicle);

            JButton btnBack = createStyledButton("Back");
            btnBack.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    cardLayout.show(panelMain, "home");
                }
            });

            add(inputPanel, BorderLayout.NORTH);
            add(new JScrollPane(txtAreaVehicles), BorderLayout.CENTER);
            add(btnBack, BorderLayout.SOUTH);
        }

        private void updateVehicleList() {
            StringBuilder vehicleList = new StringBuilder();
            for (Vehicle vehicle : vehicles) {
                vehicleList.append(vehicle.toString()).append(" - ").append(vehicle.isRented() ? "Rented" : "Available").append("\n");
            }
            txtAreaVehicles.setText(vehicleList.toString());
        }
    }

    class RentVehiclePanel extends JPanel {
        private JComboBox<Vehicle> comboVehicles;
        private JTextField txtRenterName;
        private JTextField txtRenterContact;
        private JTextArea txtAreaStatus;

        public RentVehiclePanel() {
            setLayout(new BorderLayout(10, 10));
            setBackground(Color.WHITE);

            comboVehicles = new JComboBox<>();
            txtRenterName = new JTextField(20);
            txtRenterContact = new JTextField(20);
            JButton btnRentVehicle = createStyledButton("Rent Vehicle");
            btnRentVehicle.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Vehicle vehicle = (Vehicle) comboVehicles.getSelectedItem();
                    if (vehicle != null) {
                        String renterName = txtRenterName.getText();
                        String renterContact = txtRenterContact.getText();
                        if (!vehicle.isRented() && !renterName.isEmpty() && !renterContact.isEmpty()) {
                            vehicle.rent(renterName, renterContact);
                            updateVehicleList();
                            JOptionPane.showMessageDialog(frame, "Vehicle rented successfully!");
                        } else {
                            JOptionPane.showMessageDialog(frame, "Please fill all renter details and select an available vehicle.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            });

            txtAreaStatus = new JTextArea();
            txtAreaStatus.setEditable(false);
            txtAreaStatus.setFont(new Font("Arial", Font.PLAIN, 14));
            txtAreaStatus.setBackground(new Color(240, 240, 240));

            JPanel inputPanel = new JPanel();
            inputPanel.setLayout(new GridLayout(6, 1, 10, 10));
            inputPanel.setBackground(Color.WHITE);
            inputPanel.add(new JLabel("Available Vehicles:"));
            inputPanel.add(comboVehicles);
            inputPanel.add(new JLabel("Renter Name:"));
            inputPanel.add(txtRenterName);
            inputPanel.add(new JLabel("Renter Contact:"));
            inputPanel.add(txtRenterContact);
            inputPanel.add(btnRentVehicle);

            JButton btnBack = createStyledButton("Back");
            btnBack.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    cardLayout.show(panelMain, "home");
                }
            });

            add(inputPanel, BorderLayout.NORTH);
            add(new JScrollPane(txtAreaStatus), BorderLayout.CENTER);
            add(btnBack, BorderLayout.SOUTH);

            updateVehicleList();
        }

        private void updateVehicleList() {
            comboVehicles.removeAllItems();
            for (Vehicle vehicle : vehicles) {
                if (!vehicle.isRented()) {
                    comboVehicles.addItem(vehicle);
                }
            }
        }
    }

    class ReturnVehiclePanel extends JPanel {
        private JComboBox<Vehicle> comboVehicles;
        private JTextArea txtAreaStatus;

        public ReturnVehiclePanel() {
            setLayout(new BorderLayout(10, 10));
            setBackground(Color.WHITE);

            comboVehicles = new JComboBox<>();
            JButton btnReturnVehicle = createStyledButton("Return Vehicle");
            btnReturnVehicle.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Vehicle vehicle = (Vehicle) comboVehicles.getSelectedItem();
                    if (vehicle != null) {
                        if (vehicle.isRented()) {
                            showBill(vehicle);
                            vehicle.returnVehicle();
                            updateVehicleList();
                            JOptionPane.showMessageDialog(frame, "Vehicle returned successfully!");
                        } else {
                            JOptionPane.showMessageDialog(frame, "Vehicle is not currently rented.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            });

            txtAreaStatus = new JTextArea();
            txtAreaStatus.setEditable(false);
            txtAreaStatus.setFont(new Font("Arial", Font.PLAIN, 14));
            txtAreaStatus.setBackground(new Color(240, 240, 240));

            JPanel inputPanel = new JPanel();
            inputPanel.setLayout(new GridLayout(4, 1, 10, 10));
            inputPanel.setBackground(Color.WHITE);
            inputPanel.add(new JLabel("Rented Vehicles:"));
            inputPanel.add(comboVehicles);
            inputPanel.add(btnReturnVehicle);

            JButton btnBack = createStyledButton("Back");
            btnBack.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    cardLayout.show(panelMain, "home");
                }
            });

            add(inputPanel, BorderLayout.NORTH);
            add(new JScrollPane(txtAreaStatus), BorderLayout.CENTER);
            add(btnBack, BorderLayout.SOUTH);

            updateVehicleList();
        }

        private void updateVehicleList() {
            comboVehicles.removeAllItems();
            for (Vehicle vehicle : vehicles) {
                if (vehicle.isRented()) {
                    comboVehicles.addItem(vehicle);
                }
            }
        }

        private void showBill(Vehicle vehicle) {
            int rentalRate = vehicle.getRentalRate();
            int rentalDays = Integer.parseInt(JOptionPane.showInputDialog(frame, "Enter the number of days the vehicle was rented:"));
            int totalAmount = rentalRate * rentalDays;
            JOptionPane.showMessageDialog(frame, "Vehicle Model: " + vehicle.getModel() + "\n" +
                    "Vehicle Number: " + vehicle.getNumber() + "\n" +
                    "Renter Name: " + vehicle.renterName + "\n" +
                    "Renter Contact: " + vehicle.renterContact + "\n" +
                    "Rental Rate: " + rentalRate + " per day\n" +
                    "Total Amount: " + totalAmount);
        }
    }

    class SearchVehiclePanel extends JPanel {
        private JTextField txtSearchModel;
        private JTextArea txtAreaResults;

        public SearchVehiclePanel() {
            setLayout(new BorderLayout(10, 10));
            setBackground(Color.WHITE);

            txtSearchModel = new JTextField(20);
            JButton btnSearch = createStyledButton("Search");
            btnSearch.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String searchModel = txtSearchModel.getText();
                    StringBuilder results = new StringBuilder();
                    for (Vehicle vehicle : vehicles) {
                        if (vehicle.getModel().equalsIgnoreCase(searchModel)) {
                            results.append(vehicle.toString()).append(" - ").append(vehicle.isRented() ? "Rented" : "Available").append("\n");
                        }
                    }
                    if (results.length() == 0) {
                        results.append("No vehicles found for the model: ").append(searchModel);
                    }
                    txtAreaResults.setText(results.toString());
                }
            });

            txtAreaResults = new JTextArea();
            txtAreaResults.setEditable(false);
            txtAreaResults.setFont(new Font("Arial", Font.PLAIN, 14));
            txtAreaResults.setBackground(new Color(240, 240, 240));

            JPanel inputPanel = new JPanel();
            inputPanel.setLayout(new GridLayout(4, 1, 10, 10));
            inputPanel.setBackground(Color.WHITE);
            inputPanel.add(new JLabel("Search Vehicle Model:"));
            inputPanel.add(txtSearchModel);
            inputPanel.add(btnSearch);

            JButton btnBack = createStyledButton("Back");
            btnBack.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    cardLayout.show(panelMain, "home");
                }
            });

            add(inputPanel, BorderLayout.NORTH);
            add(new JScrollPane(txtAreaResults), BorderLayout.CENTER);
            add(btnBack, BorderLayout.SOUTH);
        }
    }
}
