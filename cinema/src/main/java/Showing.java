import java.sql.Timestamp;

public class Showing {
    private int showingId;
    private Movie movie;
    private Hall hall;
    private Timestamp startTime;
    private float price;

    public Showing() {}

    public Showing(Movie movie, Hall hall, Timestamp startTime, float price) {
        this.movie = movie;
        this.hall = hall;
        this.startTime = startTime;
        this.price = price;
    }

    public int getShowingId() {
        return showingId;
    }

    public void setShowingId(int showingId) {
        this.showingId = showingId;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Hall getHall() {
        return hall;
    }

    public void setHall(Hall hall) {
        this.hall = hall;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
