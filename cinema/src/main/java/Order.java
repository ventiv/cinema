import java.sql.Timestamp;

public class Order {
    private int orderId;
    private Customer user;
    private String paymentMethod;
    private float paymentAmount;
    private Timestamp orderTime;
    private boolean isPaid;

    public Order() {}

    public Order(Customer user, String paymentMethod, float paymentAmount, Timestamp orderTime, boolean isPaid) {
        this.user = user;
        this.paymentMethod = paymentMethod;
        this.paymentAmount = paymentAmount;
        this.orderTime = orderTime;
        this.isPaid = isPaid;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Customer getUser() {
        return user;
    }

    public void setUser(Customer user) {
        this.user = user;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public float getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(float paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public Timestamp getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Timestamp orderTime) {
        this.orderTime = orderTime;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }
}
