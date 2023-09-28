import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class FrontDesk extends User {
    private int frontdeskId;

    public FrontDesk(int frontdeskId, String username, String password) {
        super(username, password);
        this.frontdeskId = frontdeskId;
    }

    public int getFrontdeskId() {
        return frontdeskId;
    }

    public boolean updatePassword(String newPassword) {
        boolean status = false;

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cinema", "root", "20012001")) {
            String sql = "UPDATE FrontDesks SET password = ? WHERE frontdesk_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, newPassword);
                preparedStatement.setInt(2, this.frontdeskId);

                int rowsUpdated = preparedStatement.executeUpdate();
                status = rowsUpdated > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }

    public static FrontDesk validateFrontDesk(String username, String password) {
        FrontDesk frontDesk = null;

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cinema", "root", "20012001")) {
            String sql = "SELECT * FROM FrontDesks WHERE username = ? AND password = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int frontdeskId = resultSet.getInt("frontdesk_id");
                        frontDesk = new FrontDesk(frontdeskId, username, password);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return frontDesk;
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

    public boolean createSeatOrderAndTicket(int showingId, int seatRow, int seatNumber, int userId, String paymentMethod, float paymentAmount) {
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
}
