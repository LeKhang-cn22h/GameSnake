package model;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Snake {
    private List<Position> body;  // Cơ thể của rắn lưu trữ dưới dạng danh sách các vị trí
    private String currentDirection;
//    private Food food;  // Tham chiếu đến đối tượng Food
    private GameConfig gameConfig;
    private Random random;  // Để tạo vị trí ngẫu nhiên cho thức ăn
    private String lastDirection; // Hướng trước đó

 // Trong class Snake
    public Snake(int startRow, int startCol, GameConfig gameConfig) {
        body = new LinkedList<>();
        body.add(new Position(startRow, startCol));  // Khởi tạo vị trí đầu tiên của rắn
        currentDirection = "NONE";  // Hướng di chuyển ban đầu
//        this.food = food;  // Gán tham chiếu thức ăn
        this.gameConfig = gameConfig;  // Gán tham chiếu cấu hình game
        random = new Random();  // Khởi tạo đối tượng Random
        lastDirection = "NONE"; // Khởi tạo hướng trước đó

    }

    public List<Position> getBody() {
        return body;
    }

    public Position getHead() {
        return body.get(0);  // Trả về vị trí của đầu rắn (vị trí đầu tiên trong danh sách body)
    }

    public void move(Position newHead) {
//        Position newHead = calculateNewHeadPosition();  // Tính toán vị trí mới của đầu rắn

        // Kiểm tra nếu đầu rắn ăn trúng thức ăn
//        if (newHead.equals(food.getPosition())) {
//            grow();  // Nếu ăn được mồi, rắn dài ra
////            food.setPosition(generateRandomFoodPosition());  // Đặt lại vị trí thức ăn ngẫu nhiên
//        } else {
            body.add(0, newHead);  // Nếu không ăn mồi, di chuyển đầu rắn và giữ nguyên chiều dài
            body.remove(body.size()-1);  // Loại bỏ phần đuôi để rắn duy trì kích thước
//        }
    }

    // Làm cho rắn dài ra khi ăn mồi
 // Phương thức để làm cho rắn dài ra khi ăn mồi
    public void grow(Position newHead) {
        // Tính toán vị trí mới của phần đầu rắn
//        Position newHead = calculateNewHeadPosition();  // Tính toán vị trí mới cho đầu rắn

        // Thêm phần mới vào đầu danh sách body
        body.add(body.size(), newHead);  // Thêm đầu rắn mới vào vị trí đầu tiên của body
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
    public void updateDirectionAfterMove() {
        lastDirection = currentDirection; // Lưu hướng hiện tại thành hướng cuối cùng sau mỗi lượt di chuyển
    }
    // Tính toán vị trí mới của đầu rắn dựa trên hướng hiện tại
    public Position calculateNewHeadPosition(int modeGame) {
        Position currentHead = getHead();
        int newRow = currentHead.getRow();
        int newCol = currentHead.getCol();
        if(modeGame != 2 ) {
	        switch (currentDirection) {
	            case "UP": newRow--; break;
	            case "DOWN": newRow++; break;
	            case "LEFT": newCol--; break;
	            case "RIGHT": newCol++; break;
	        }
	        return new Position(newRow, newCol);  // Trả về vị trí mới của đầu rắn
        }
        switch (currentDirection) {
	        case "UP":
	        	if(newRow-1<0) {
	        		newRow = gameConfig.getMapSize()-1;
	        		break;
	        	}
	        	newRow--; 
	        	break;
	        case "DOWN":
	        	if(newRow+1>gameConfig.getMapSize()-1) {
	        		newRow = 0;
	        		break;
	        	}
	        	newRow++; 
	        	break;
	        case "LEFT":
	        	if(newCol-1<0) {
	        		newCol = gameConfig.getMapSize()-1;
	        		break;
	        	}
	        	newCol--; 
	        	break;
	        case "RIGHT": 
	        	if(newCol+1>gameConfig.getMapSize()-1) {
	        		newCol = 0;
	        		break;
	        	}
	        	newCol++; 
	        	break;
    }
        return new Position(newRow, newCol);

    }

	public String getCurrentDirection() {
		// TODO Auto-generated method stub
		return currentDirection;
	}
    

   

    
}
