package model;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Snake {
    private List<Position> body;  // Cơ thể của rắn lưu trữ dưới dạng danh sách các vị trí
    private String currentDirection;
    private Food food;  // Tham chiếu đến đối tượng Food
    private GameConfig gameConfig;
    private Random random;  // Để tạo vị trí ngẫu nhiên cho thức ăn

 // Trong class Snake
    public Snake(int startRow, int startCol, Food food, GameConfig gameConfig) {
        body = new LinkedList<>();
        body.add(new Position(startRow, startCol));  // Khởi tạo vị trí đầu tiên của rắn
        currentDirection = "NONE";  // Hướng di chuyển ban đầu
        this.food = food;  // Gán tham chiếu thức ăn
        this.gameConfig = gameConfig;  // Gán tham chiếu cấu hình game
        random = new Random();  // Khởi tạo đối tượng Random
    }

    public List<Position> getBody() {
        return body;
    }

    public Position getHead() {
        return body.get(0);  // Trả về vị trí của đầu rắn (vị trí đầu tiên trong danh sách body)
    }

    public void move() {
        Position newHead = calculateNewHeadPosition();  // Tính toán vị trí mới của đầu rắn

        // Kiểm tra nếu đầu rắn ăn trúng thức ăn
        if (newHead.equals(food.getPosition())) {
            grow();  // Nếu ăn được mồi, rắn dài ra
//            food.setPosition(generateRandomFoodPosition());  // Đặt lại vị trí thức ăn ngẫu nhiên
        } else {
            body.add(0, newHead);  // Nếu không ăn mồi, di chuyển đầu rắn và giữ nguyên chiều dài
            body.remove(body.size()-1);  // Loại bỏ phần đuôi để rắn duy trì kích thước
        }
    }

    // Làm cho rắn dài ra khi ăn mồi
 // Phương thức để làm cho rắn dài ra khi ăn mồi
    public void grow() {
        // Tính toán vị trí mới của phần đầu rắn
        Position newHead = calculateNewHeadPosition();  // Tính toán vị trí mới cho đầu rắn

        // Thêm phần mới vào đầu danh sách body
        body.add(0, newHead);  // Thêm đầu rắn mới vào vị trí đầu tiên của body
    }


    // Thay đổi hướng di chuyển của rắn
    public void changeDirection(String direction) {
        // Đảm bảo rằng rắn không thể quay ngược lại chính nó
        if ((direction.equals("UP") && !currentDirection.equals("DOWN")) ||
            (direction.equals("DOWN") && !currentDirection.equals("UP")) ||
            (direction.equals("LEFT") && !currentDirection.equals("RIGHT")) ||
            (direction.equals("RIGHT") && !currentDirection.equals("LEFT"))) {
            currentDirection = direction;
        }
    }

    // Tính toán vị trí mới của đầu rắn dựa trên hướng hiện tại
    private Position calculateNewHeadPosition() {
        Position currentHead = getHead();
        int newRow = currentHead.getRow();
        int newCol = currentHead.getCol();

        switch (currentDirection) {
            case "UP": newRow--; break;
            case "DOWN": newRow++; break;
            case "LEFT": newCol--; break;
            case "RIGHT": newCol++; break;
        }

        return new Position(newRow, newCol);  // Trả về vị trí mới của đầu rắn
    }

   

    
}
