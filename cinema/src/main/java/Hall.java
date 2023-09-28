public class Hall {
    private int hallId;
    private int numberOfRows;
    private int seatsPerRow;

    public Hall() {}

    public Hall(int numberOfRows, int seatsPerRow) {
        this.numberOfRows = numberOfRows;
        this.seatsPerRow = seatsPerRow;
    }

    public int getHallId() {
        return hallId;
    }

    public void setHallId(int hallId) {
        this.hallId = hallId;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public void setNumberOfRows(int numberOfRows) {
        this.numberOfRows = numberOfRows;
    }

    public int getSeatsPerRow() {
        return seatsPerRow;
    }

    public void setSeatsPerRow(int seatsPerRow) {
        this.seatsPerRow = seatsPerRow;
    }
}
