import java.sql.*;

public class Manager extends User {
    private int managerId;

    public Manager(int managerId, String username, String password) {
        super(username, password);
        this.managerId = managerId;
    }

    public int getManagerId() {
        return managerId;
    }

    public boolean updatePassword(String newPassword) {
        boolean status = false;

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cinema", "root", "20012001")) {
            String sql = "UPDATE Managers SET password = ? WHERE manager_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, newPassword);
                preparedStatement.setInt(2, this.managerId);

                int rowsUpdated = preparedStatement.executeUpdate();
                status = rowsUpdated > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }

    public static Manager validateManager(String username, String password) {
        Manager manager = null;

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cinema", "root", "20012001")) {
            String sql = "SELECT * FROM Managers WHERE username = ? AND password = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int managerId = resultSet.getInt("manager_id");
                        manager = new Manager(managerId, username, password);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return manager;
    }

    public boolean addMovie(Movie movie) {
        boolean status = false;

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cinema", "root", "20012001")) {
            String sql = "INSERT INTO Movies (title, director, main_cast, synopsis, duration, category_id) VALUES (?, ?, ?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, movie.getTitle());
                preparedStatement.setString(2, movie.getDirector());
                preparedStatement.setString(3, movie.getMainCast());
                preparedStatement.setString(4, movie.getSynopsis());
                preparedStatement.setInt(5, movie.getDuration());
                preparedStatement.setInt(6, movie.getCategory().getCategoryId());

                int rowsInserted = preparedStatement.executeUpdate();
                status = rowsInserted > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }

    public boolean updateMovie(Movie movie) {
        boolean status = false;

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cinema", "root", "20012001")) {
            String sql = "UPDATE Movies SET title = ?, director = ?, main_cast = ?, synopsis = ?, duration = ?, category_id = ? WHERE movie_id = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, movie.getTitle());
                preparedStatement.setString(2, movie.getDirector());
                preparedStatement.setString(3, movie.getMainCast());
                preparedStatement.setString(4, movie.getSynopsis());
                preparedStatement.setInt(5, movie.getDuration());
                preparedStatement.setInt(6, movie.getCategory().getCategoryId());
                preparedStatement.setInt(7, movie.getMovieId());

                int rowsUpdated = preparedStatement.executeUpdate();
                status = rowsUpdated > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }

    public boolean deleteMovie(int movieId) {
        boolean status = false;

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cinema", "root", "20012001")) {
            String sql = "DELETE FROM Movies WHERE movie_id = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, movieId);

                int rowsDeleted = preparedStatement.executeUpdate();
                status = rowsDeleted > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }

    public Movie getMovieById(int movieId) {
        Movie movie = null;

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cinema", "root", "20012001")) {
            String sql = "SELECT * FROM Movies WHERE movie_id = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, movieId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        String title = resultSet.getString("title");
                        String director = resultSet.getString("director");
                        String mainCast = resultSet.getString("main_cast");
                        String synopsis = resultSet.getString("synopsis");
                        int duration = resultSet.getInt("duration");
                        int categoryId = resultSet.getInt("category_id");

                        Category category = Category.getCategoryById(categoryId);
                        movie = new Movie(title, director, mainCast, synopsis, duration, category);
                        movie.setMovieId(movieId);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return movie;
    }

    public boolean addShowing(Showing showing) {
        boolean status = false;
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cinema", "root", "20012001")) {
            String sql = "INSERT INTO Showings (movie_id, hall_id, start_time, price) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, showing.getMovie().getMovieId());
                preparedStatement.setInt(2, showing.getHall().getHallId());
                preparedStatement.setTimestamp(3, showing.getStartTime());
                preparedStatement.setFloat(4, showing.getPrice());

                int rowsInserted = preparedStatement.executeUpdate();
                status = rowsInserted > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }

    public boolean deleteShowing(int showingId) {
        boolean status = false;
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cinema", "root", "20012001")) {
            String sql = "DELETE FROM Showings WHERE showing_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, showingId);

                int rowsDeleted = preparedStatement.executeUpdate();
                status = rowsDeleted > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }

    public boolean updateShowing(Showing showing) {
        boolean status = false;
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cinema", "root", "20012001")) {
            String sql = "UPDATE Showings SET movie_id = ?, hall_id = ?, start_time = ?, price = ? WHERE showing_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, showing.getMovie().getMovieId());
                preparedStatement.setInt(2, showing.getHall().getHallId());
                preparedStatement.setTimestamp(3, showing.getStartTime());
                preparedStatement.setFloat(4, showing.getPrice());
                preparedStatement.setInt(5, showing.getShowingId());

                int rowsUpdated = preparedStatement.executeUpdate();
                status = rowsUpdated > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }

    public Showing getShowingById(int showingId) {
        Showing showing = null;
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cinema", "root", "20012001")) {
            String sql = "SELECT * FROM Showings WHERE showing_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, showingId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int movieId = resultSet.getInt("movie_id");
                        int hallId = resultSet.getInt("hall_id");
                        Timestamp startTime = resultSet.getTimestamp("start_time");
                        float price = resultSet.getFloat("price");

                        Movie movie = getMovieById(movieId); // Assuming you have this method in place.
                        Hall hall = getHallById(hallId); // You will need a similar method in the Hall class.

                        showing = new Showing(movie, hall, startTime, price);
                        showing.setShowingId(showingId);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return showing;
    }

    public Hall getHallById(int hallId) {
        Hall hall = null;

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cinema", "root", "20012001")) {
            String sql = "SELECT * FROM Halls WHERE hall_id = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, hallId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        hall = new Hall();
                        hall.setHallId(resultSet.getInt("hall_id"));
                        hall.setNumberOfRows(resultSet.getInt("number_of_rows"));
                        hall.setSeatsPerRow(resultSet.getInt("seats_per_row"));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return hall;
    }


}
