import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Category {
    private int categoryId;
    private String categoryName;

    public static Category getCategoryById(int categoryId) {
        Category category = null;

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cinema", "root", "20012001")) {
            String sql = "SELECT * FROM Categories WHERE category_id = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, categoryId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        String categoryName = resultSet.getString("category_name");

                        category = new Category();
                        category.setCategoryId(categoryId);
                        category.setCategoryName(categoryName);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return category;
    }

    public Category() {}

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
