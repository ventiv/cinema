public class Ticket {
    private int ticketId;
    private Order order;
    private Seat seat;
    private boolean isCollected;

    public Ticket() {}

    public Ticket(Order order, Seat seat, boolean isCollected) {
        this.order = order;
        this.seat = seat;
        this.isCollected = isCollected;
    }

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Seat getSeat() {
        return seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }

    public boolean isCollected() {
        return isCollected;
    }

    public void setCollected(boolean collected) {
        isCollected = collected;
    }
}
