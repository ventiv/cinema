public class Seat {
    private int seatId;
    private Showing showing;
    private int seatRow;
    private int seatNumber;

    public Seat() {}

    public Seat(Showing showing, int seatRow, int seatNumber) {
        this.showing = showing;
        this.seatRow = seatRow;
        this.seatNumber = seatNumber;
    }

    public int getSeatId() {
        return seatId;
    }

    public void setSeatId(int seatId) {
        this.seatId = seatId;
    }

    public Showing getShowing() {
        return showing;
    }

    public void setShowing(Showing showing) {
        this.showing = showing;
    }

    public int getSeatRow() {
        return seatRow;
    }

    public void setSeatRow(int seatRow) {
        this.seatRow = seatRow;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    @Override
    public String toString() {
        return "Seat{" +
                "seatId=" + seatId +
                ", showing=" + showing +
                ", seatRow=" + seatRow +
                ", seatNumber=" + seatNumber +
                '}';
    }
}
