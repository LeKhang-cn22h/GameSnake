package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import database.DatabaseConnection;
import model.User;

public class UserDAO implements DAOInterface <User>{

	@Override
	public int insert(User user) {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (Connection conn=DatabaseConnection.getConnection();
    			PreparedStatement ps= conn.prepareStatement(sql)){
				ps.setString(1, user.getUsername());
				ps.setString(2, user.getPassword());
				return ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
        
	}
	public int getIdByUserName(String username){
		String sql="SELECT id FROM users WHERE username=?";
		
		int idUser=-1;
		try(Connection conn=DatabaseConnection.getConnection();
				PreparedStatement ps=conn.prepareStatement(sql)){
			ps.setString(1, username);
			ResultSet rs=ps.executeQuery();
			if(rs.next()) {
				idUser=rs.getInt("id");
			}
			
		}catch(SQLException e){
			e.printStackTrace();
				
		}
		return idUser;
	}
	public boolean checkUsernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Lỗi khi kiểm tra
        }
    }
	@Override
	public User selectById(User t) {
		// TODO Auto-generated method stlub
		return null;
	}

	@Override
	public ArrayList<User> selectAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(User t) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(User t) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ArrayList<User> selectByCondition() {
		// TODO Auto-generated method stub
		return null;
	}

}
