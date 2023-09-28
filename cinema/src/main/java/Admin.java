import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static util.SHA256Util.getSHA256;

public class Admin extends User {
    private int adminId;

    public Admin(int adminId, String username, String password) {
        super(username, password);
        this.adminId = adminId;
    }

    public int getAdminId() {
        return adminId;
    }

    public boolean updatePassword(String newPassword) {
        boolean status = false;

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cinema", "root", "20012001")) {
            String sql = "UPDATE Admins SET password = ? WHERE admin_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, getSHA256(newPassword));
                preparedStatement.setInt(2, this.adminId);

                int rowsUpdated = preparedStatement.executeUpdate();
                status = rowsUpdated > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }

    public static Admin validateAdmin(String username, String password) {
        Admin admin = null;

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cinema", "root", "20012001")) {
            String sql = "SELECT * FROM Admins WHERE username = ? AND password = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, getSHA256(password));

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int adminId = resultSet.getInt("admin_id");
                        admin = new Admin(adminId, username, password);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return admin;
    }

    public boolean resetCustomerPassword(String username, String newPassword) {
        boolean status = false;

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cinema", "root", "20012001")) {
            String sql = "UPDATE Customers SET password = ? WHERE username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, getSHA256(newPassword)); // 这里应该是哈希过的密码，这里为简单起见，直接使用了明文密码
                preparedStatement.setString(2, username);

                int rowsUpdated = preparedStatement.executeUpdate();
                status = rowsUpdated > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return status;
    }

    public List<Customer> listAllCustomers() {
        List<Customer> customers = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cinema", "root", "20012001")) {
            String sql = "SELECT user_id, username, user_level, registration_time, total_spend, total_purchases, phone_number, email, is_locked, is_deleted FROM Customers";
            try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    String username = resultSet.getString("username");
                    String userLevel = resultSet.getString("user_level");
                    String phoneNumber = resultSet.getString("phone_number");
                    String email = resultSet.getString("email");

                    Customer customer = new Customer(username, "dummyPassword", userLevel, phoneNumber, email);
                    customer.setUserId(resultSet.getInt("user_id"));
                    customer.setRegistrationTime(resultSet.getTimestamp("registration_time"));
                    customer.setTotalSpend(resultSet.getFloat("total_spend"));
                    customer.setTotalPurchases(resultSet.getInt("total_purchases"));
                    customer.setLocked(resultSet.getBoolean("is_locked"));
                    customer.setDeleted(resultSet.getBoolean("is_deleted"));

                    customers.add(customer);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customers;
    }

    public boolean deleteCustomer(String username) {
        boolean status = false; // Used to indicate whether the deletion was successful

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cinema", "root", "20012001")) {
            String sql = "UPDATE Customers SET is_deleted = TRUE WHERE username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, username);

                int rowsUpdated = preparedStatement.executeUpdate();
                status = rowsUpdated > 0; // If rowsUpdated is more than 0, it means the operation was successful
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return status;
    }

    public Optional<Customer> getCustomerByUsername(String username) {
        Optional<Customer> customer = Optional.empty();

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cinema", "root", "20012001")) {
            String sql = "SELECT user_id, username, user_level, registration_time, total_spend, total_purchases, phone_number, email, is_locked, is_deleted " +
                    "FROM Customers WHERE username = ? AND is_deleted = FALSE";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, username);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        String userLevel = resultSet.getString("user_level");
                        String phoneNumber = resultSet.getString("phone_number");
                        String email = resultSet.getString("email");

                        Customer foundCustomer = new Customer(username, "dummyPassword", userLevel, phoneNumber, email);
                        foundCustomer.setUserId(resultSet.getInt("user_id"));
                        foundCustomer.setRegistrationTime(resultSet.getTimestamp("registration_time"));
                        foundCustomer.setTotalSpend(resultSet.getFloat("total_spend"));
                        foundCustomer.setTotalPurchases(resultSet.getInt("total_purchases"));
                        foundCustomer.setLocked(resultSet.getBoolean("is_locked"));
                        foundCustomer.setDeleted(resultSet.getBoolean("is_deleted"));

                        customer = Optional.of(foundCustomer);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customer;
    }

    public Optional<Customer> getCustomerById(int userId) {
        Optional<Customer> customer = Optional.empty();

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cinema", "root", "20012001")) {
            String sql = "SELECT user_id, username, user_level, registration_time, total_spend, total_purchases, phone_number, email, is_locked, is_deleted " +
                    "FROM Customers WHERE user_id = ? AND is_deleted = FALSE";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, userId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        String username = resultSet.getString("username");
                        String userLevel = resultSet.getString("user_level");
                        String phoneNumber = resultSet.getString("phone_number");
                        String email = resultSet.getString("email");

                        Customer foundCustomer = new Customer(username, "dummyPassword", userLevel, phoneNumber, email);
                        foundCustomer.setUserId(resultSet.getInt("user_id"));
                        foundCustomer.setRegistrationTime(resultSet.getTimestamp("registration_time"));
                        foundCustomer.setTotalSpend(resultSet.getFloat("total_spend"));
                        foundCustomer.setTotalPurchases(resultSet.getInt("total_purchases"));
                        foundCustomer.setLocked(resultSet.getBoolean("is_locked"));
                        foundCustomer.setDeleted(resultSet.getBoolean("is_deleted"));

                        customer = Optional.of(foundCustomer);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customer;
    }

}

