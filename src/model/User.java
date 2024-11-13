package model;

public class User {
    private int id;            // ID của người dùng
    private String username;   // Tên đăng nhập
    private String password;   // Mật khẩu

    // Constructor không tham số
    public User() {
    }

    // Constructor đầy đủ tham số
    public User(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    // Constructor để tạo user mà không cần ID (ID thường do cơ sở dữ liệu tự sinh)
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getter và Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Phương thức toString để dễ in ra thông tin của đối tượng
    @Override
    public String toString() {
        return "User{id=" + id + ", username='" + username + "', password='" + password + "'}";
    }
}

