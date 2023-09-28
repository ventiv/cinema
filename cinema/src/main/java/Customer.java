import java.sql.*;
import java.util.*;

import static util.SHA256Util.getSHA256;

public class Customer extends User{
    // Fields corresponding to the columns in the Customers table
    private int userId;

    @Override
    public String toString() {
        return "Customer{" +
                "userId=" + userId +
                ", userLevel='" + userLevel + '\'' +
                ", registrationTime=" + registrationTime +
                ", totalSpend=" + totalSpend +
                ", totalPurchases=" + totalPurchases +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", isLocked=" + isLocked +
                ", isDeleted=" + isDeleted +
                '}';
    }

    private String userLevel;
    private Timestamp registrationTime;
    private float totalSpend;
    private int totalPurchases;
    private String phoneNumber;
    private String email;
    private boolean isLocked;
    private boolean isDeleted;

    private static Connection con;

    // Constructor
    public Customer(String username, String password, String userLevel, String phoneNumber, String email) {
        super(username, password);
        this.userLevel = userLevel;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.registrationTime = new Timestamp(System.currentTimeMillis());
        // Other fields are set to their default values
        try {
            // Establishing Connection
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cinema", "root", "20012001");
            // SQL for inserting the new customer
            String sql = "INSERT INTO Customers(username, password, user_level, phone_number, email) VALUES(?, ?, ?, ?, ?)";
            try (PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                pst.setString(1, getUsername());
                pst.setString(2, getPassword());
                pst.setString(3, this.userLevel);
                pst.setString(4, this.phoneNumber);
                pst.setString(5, this.email);
                pst.executeUpdate();

                // Get the generated user ID
                try (ResultSet generatedKeys = pst.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        this.userId = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Creating user failed, no ID obtained.");
                    }
                }

                // Now you can execute a SELECT statement to get the row for the inserted user
                String selectSql = "SELECT * FROM Customers WHERE user_id = ?";
                try (PreparedStatement selectPst = con.prepareStatement(selectSql)) {
                    selectPst.setInt(1, this.userId);
                    try (ResultSet rs = selectPst.executeQuery()) {
                        if (rs.next()) {
                            // Assuming other fields are also needed to be set
                            this.totalSpend = rs.getFloat("total_spend");
                            this.totalPurchases = rs.getInt("total_purchases");
                            this.isLocked = rs.getBoolean("is_locked");
                            this.isDeleted = rs.getBoolean("is_deleted");
                        }
                    }
                }
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
    }

    public boolean updatePassword(String newPassword) {
        boolean status = false; // 用于指示密码是否成功更新

        try {
            // 检查是否有有效的数据库连接
            if (con == null || con.isClosed()) {
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cinema", "root", "20012001");
            }

            // SQL for updating the customer's password
            String sql = "UPDATE Customers SET password = ? WHERE user_id = ?";
            try (PreparedStatement pst = con.prepareStatement(sql)) {
                pst.setString(1, getSHA256(newPassword));
                pst.setInt(2, this.userId);

                int rowsUpdated = pst.executeUpdate();
                status = rowsUpdated > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return status;
    }

    public static Customer validateCustomer(String username, String password) {
        Customer customer = null;

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cinema", "root", "20012001")) {
            String sql = "SELECT * FROM Customers WHERE username = ? AND password = ?";
            try (PreparedStatement pst = con.prepareStatement(sql)) {
                pst.setString(1, username);
                pst.setString(2, getSHA256(password));

                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        int userId = rs.getInt("user_id");
                        String userLevel = rs.getString("user_level");
                        String phoneNumber = rs.getString("phone_number");
                        String email = rs.getString("email");

                        customer = new Customer(username, password, userLevel, phoneNumber, email);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customer;
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(String userLevel) {
        this.userLevel = userLevel;
    }

    public Timestamp getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(Timestamp registrationTime) {
        this.registrationTime = registrationTime;
    }

    public float getTotalSpend() {
        return totalSpend;
    }

    public void setTotalSpend(float totalSpend) {
        this.totalSpend = totalSpend;
    }

    public int getTotalPurchases() {
        return totalPurchases;
    }

    public void setTotalPurchases(int totalPurchases) {
        this.totalPurchases = totalPurchases;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public static Connection getCon() {
        return con;
    }

    public static void setCon(Connection con) {
        Customer.con = con;
    }

    public List<Movie> listAllMovies() {
        List<Movie> movies = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cinema", "root", "20012001")) {
            String sql = "SELECT m.*, c.category_name FROM Movies m JOIN Categories c ON m.category_id = c.category_id";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Movie movie = new Movie();
                        movie.setMovieId(resultSet.getInt("movie_id"));
                        movie.setTitle(resultSet.getString("title"));
                        movie.setDirector(resultSet.getString("director"));
                        movie.setMainCast(resultSet.getString("main_cast"));
                        movie.setSynopsis(resultSet.getString("synopsis"));
                        movie.setDuration(resultSet.getInt("duration"));

                        Category category = new Category(); // Assuming you have a Category class.
                        category.setCategoryId(resultSet.getInt("category_id"));
                        category.setCategoryName(resultSet.getString("category_name"));

                        movie.setCategory(category);

                        movies.add(movie);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return movies;
    }

    public List<Showing> listAllShowings() {
        List<Showing> showings = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cinema", "root", "20012001")) {
            String sql = "SELECT s.*, m.*, h.* " +
                    "FROM Showings s " +
                    "JOIN Movies m ON s.movie_id = m.movie_id " +
                    "JOIN Halls h ON s.hall_id = h.hall_id";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Showing showing = new Showing();
                        showing.setShowingId(resultSet.getInt("s.showing_id"));
                        showing.setStartTime(resultSet.getTimestamp("s.start_time"));

                        Movie movie = new Movie();
                        movie.setMovieId(resultSet.getInt("m.movie_id"));
                        movie.setTitle(resultSet.getString("m.title"));
                        movie.setDirector(resultSet.getString("m.director"));
                        movie.setMainCast(resultSet.getString("m.main_cast"));
                        movie.setSynopsis(resultSet.getString("m.synopsis"));
                        movie.setDuration(resultSet.getInt("m.duration"));

                        Hall hall = new Hall();
                        hall.setHallId(resultSet.getInt("h.hall_id"));
                        hall.setNumberOfRows(resultSet.getInt("h.number_of_rows"));
                        hall.setSeatsPerRow(resultSet.getInt("h.seats_per_row"));

                        showing.setMovie(movie);
                        showing.setHall(hall);

                        showings.add(showing);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return showings;
    }

    public boolean buyTicket(int showingId, int seatRow, int seatNumber, int userId, String paymentMethod, float paymentAmount) {
        boolean status = false;

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cinema", "root", "20012001")) {
            connection.setAutoCommit(false); // Start a transaction

            try {
                // Create a new seat
                String sqlSeat = "INSERT INTO Seats (showing_id, seat_row, seat_number) VALUES (?, ?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(sqlSeat, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    preparedStatement.setInt(1, showingId);
                    preparedStatement.setInt(2, seatRow);
                    preparedStatement.setInt(3, seatNumber);

                    preparedStatement.executeUpdate();

                    try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            int seatId = generatedKeys.getInt(1);

                            // Create a new order
                            String sqlOrder = "INSERT INTO Orders (user_id, payment_method, payment_amount, is_paid) VALUES (?, ?, ?, true)";
                            try (PreparedStatement preparedStatementOrder = connection.prepareStatement(sqlOrder, PreparedStatement.RETURN_GENERATED_KEYS)) {
                                preparedStatementOrder.setInt(1, userId);
                                preparedStatementOrder.setString(2, paymentMethod);
                                preparedStatementOrder.setFloat(3, paymentAmount);

                                preparedStatementOrder.executeUpdate();

                                try (ResultSet generatedOrderKeys = preparedStatementOrder.getGeneratedKeys()) {
                                    if (generatedOrderKeys.next()) {
                                        int orderId = generatedOrderKeys.getInt(1);

                                        // Create a new ticket
                                        String sqlTicket = "INSERT INTO Tickets (order_id, seat_id) VALUES (?, ?)";
                                        try (PreparedStatement preparedStatementTicket = connection.prepareStatement(sqlTicket)) {
                                            preparedStatementTicket.setInt(1, orderId);
                                            preparedStatementTicket.setInt(2, seatId);

                                            preparedStatementTicket.executeUpdate();

                                            status = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                connection.commit(); // Commit the transaction if all steps are successful
            } catch (Exception e) {
                connection.rollback(); // Rollback the transaction in case of any failure
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }


    public void displaySeatMap(int showingId) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cinema", "root", "20012001")) {
            String sql = "SELECT seat_row, seat_number " +
                    "FROM Seats " +
                    "LEFT JOIN Tickets ON Seats.seat_id = Tickets.seat_id " +
                    "WHERE showing_id = ? AND Tickets.ticket_id IS NOT NULL";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, showingId);

                // 获取被占用的座位
                ResultSet resultSet = preparedStatement.executeQuery();
                Map<Integer, Set<Integer>> occupiedSeats = new HashMap<>();
                while (resultSet.next()) {
                    int seatRow = resultSet.getInt("seat_row");
                    int seatNumber = resultSet.getInt("seat_number");

                    occupiedSeats
                            .computeIfAbsent(seatRow, k -> new HashSet<>())
                            .add(seatNumber);
                }

                // 显示座位图
                displaySeatMap(occupiedSeats);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displaySeatMap(Map<Integer, Set<Integer>> occupiedSeats) {
        System.out.print("  ");
        for (int i = 1; i <= 12; i++) {
            System.out.printf("%2d ", i);
        }
        System.out.println();

        for (int i = 1; i <= 7; i++) {
            System.out.printf("%2d ", i);
            for (int j = 1; j <= 11; j++) {
                char seatChar = occupiedSeats.containsKey(i) && occupiedSeats.get(i).contains(j) ? 'X' : 'O';
                System.out.print(seatChar + "  ");
            }
            System.out.println();
        }
    }

    public boolean collectTicket(int ticketId) {
        boolean status = false; // 用于指示票是否成功取得

        try {
            // 检查是否有有效的数据库连接
            if (con == null || con.isClosed()) {
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cinema", "root", "20012001");
            }

            // SQL for updating the ticket's is_collected field
            String sql = "UPDATE Tickets SET is_collected = TRUE WHERE ticket_id = ?";
            try (PreparedStatement pst = con.prepareStatement(sql)) {
                pst.setInt(1, ticketId);

                int rowsUpdated = pst.executeUpdate();
                status = rowsUpdated > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return status;
    }

    public boolean payOrder(int orderId) {
        boolean status = false; // 用于指示订单是否成功支付

        try {
            // 检查是否有有效的数据库连接
            if (con == null || con.isClosed()) {
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cinema", "root", "20012001");
            }

            // SQL for updating the order's is_paid field
            String sql = "UPDATE Orders SET is_paid = true WHERE order_id = ? AND user_id = ?";
            try (PreparedStatement pst = con.prepareStatement(sql)) {
                pst.setInt(1, orderId);
                pst.setInt(2, this.userId);

                int rowsUpdated = pst.executeUpdate();
                status = rowsUpdated > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return status;
    }


}
