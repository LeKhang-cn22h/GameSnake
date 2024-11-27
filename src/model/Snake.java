package model;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javafx.scene.media.AudioClip;

public class Snake {
    private List<Position> body;  // Cơ thể của rắn lưu trữ dưới dạng danh sách các vị trí
    private String currentDirection;  // Hướng di chuyển hiện tại của rắn
    private GameConfig gameConfig;  // Cấu hình trò chơi
    private Random random;  // Để tạo vị trí ngẫu nhiên cho thức ăn
    private String lastDirection; // Hướng di chuyển trước đó
    private AudioClip moveSound;  // Âm thanh khi rắn di chuyển

    // Constructor của class Snake
    public Snake(int startRow, int startCol, GameConfig gameConfig) {
        body = new LinkedList<>();
        body.add(new Position(startRow, startCol));  // Khởi tạo vị trí đầu tiên của rắn
        currentDirection = "NONE";  // Hướng di chuyển ban đầu
        this.gameConfig = gameConfig;  // Gán tham chiếu cấu hình game
        random = new Random();  // Khởi tạo đối tượng Random
        lastDirection = "NONE"; // Khởi tạo hướng trước đó
        initializeSounds();  // Khởi tạo âm thanh di chuyển
    }

    // Lấy cơ thể rắn
    public List<Position> getBody() {
        return body;
    }

    // Lấy vị trí đầu của rắn
    public Position getHead() {
        return body.get(0);  // Trả về vị trí của đầu rắn (vị trí đầu tiên trong danh sách body)
    }

    // Di chuyển rắn tới vị trí mới
    public void move(Position newHead) {
        body.add(0, newHead);  // Nếu không ăn mồi, di chuyển đầu rắn và giữ nguyên chiều dài
        body.remove(body.size()-1);  // Loại bỏ phần cuối của rắn
        if (currentDirection != "NONE") {
            SnakeMove();  // Phát âm thanh khi rắn di chuyển
        }
    }

    // Khởi tạo âm thanh di chuyển
    private void initializeSounds() {
        URL moveResource = getClass().getResource("/view/music/SoundMove.ogg");
        if (moveResource != null) {
            moveSound = new AudioClip(moveResource.toExternalForm());
        } else {
            System.out.println("Không tìm thấy file âm thanh di chuyển! Kiểm tra lại đường dẫn.");
        }
    }

    // Phát âm thanh di chuyển
    public void SnakeMove() {
        if (moveSound != null) {
            if (!moveSound.isPlaying()) {
                moveSound.play();  // Phát âm thanh di chuyển nếu chưa phát
            } else {
                System.out.println("Âm thanh đang phát, không phát lại");
            }
        } else {
            System.out.println("moveSound chưa được khởi tạo");
        }
    }

    // Làm cho rắn dài ra khi ăn mồi
    public void grow(Position newHead) {
        body.add(body.size(), newHead); // Thêm đầu rắn mới vào vị trí đầu tiên của body
    }

    // Thay đổi hướng di chuyển của rắn
    public void changeDirection(String direction) {
        // Đảm bảo rằng rắn không thể quay ngược lại chính nó
        if ((direction.equals("UP") && !lastDirection.equals("DOWN")) ||
            (direction.equals("DOWN") && !lastDirection.equals("UP")) ||
            (direction.equals("LEFT") && !lastDirection.equals("RIGHT")) ||
            (direction.equals("RIGHT") && !lastDirection.equals("LEFT"))) {
            currentDirection = direction;
        }
    }

    // Cập nhật hướng di chuyển sau khi rắn di chuyển
    public void updateDirectionAfterMove() {
        lastDirection = currentDirection; // Lưu hướng hiện tại thành hướng cuối cùng sau mỗi lượt di chuyển
    }

    // Tính toán vị trí mới của đầu rắn dựa trên hướng hiện tại
    public Position calculateNewHeadPosition(int modeGame) {
        Position currentHead = getHead();
        int newRow = currentHead.getRow();
        int newCol = currentHead.getCol();
        
        if (modeGame != 2) {
            // Tính toán vị trí mới của đầu rắn trong chế độ game bình thường
            switch (currentDirection) {
                case "UP": newRow--; break;
                case "DOWN": newRow++; break;
                case "LEFT": newCol--; break;
                case "RIGHT": newCol++; break;
            }
            return new Position(newRow, newCol);  // Trả về vị trí mới của đầu rắn
        }
        
        // Tính toán vị trí mới của đầu rắn trong chế độ tự do (xuyên tường)
        switch (currentDirection) {
            case "UP":
                if (newRow - 1 < 0) {
                    newRow = gameConfig.getMapSize() - 1;  // Xuyên tường từ trên xuống dưới
                    break;
                }
                newRow--;
                break;
            case "DOWN":
                if (newRow + 1 > gameConfig.getMapSize() - 1) {
                    newRow = 0;  // Xuyên tường từ dưới lên trên
                    break;
                }
                newRow++;
                break;
            case "LEFT":
                if (newCol - 1 < 0) {
                    newCol = gameConfig.getMapSize() - 1;  // Xuyên tường từ trái qua phải
                    break;
                }
                newCol--;
                break;
            case "RIGHT":
                if (newCol + 1 > gameConfig.getMapSize() - 1) {
                    newCol = 0;  // Xuyên tường từ phải qua trái
                    break;
                }
                newCol++;
                break;
        }
        return new Position(newRow, newCol);  // Trả về vị trí mới của đầu rắn
    }

    // Lấy hướng di chuyển hiện tại
    public String getCurrentDirection() {
        return currentDirection;
    }
}
