package DAO;
import java.util.ArrayList;

import model.Score;

public class ScoreDAO implements DAOInterface <Score> {
	public static ScoreDAO getInstance() {
		return new ScoreDAO();
	}
	@Override
	public int insert(Score s) {
			s.getCurrentScore();
		return 0;
	}

	@Override
	public Score selectById(Score t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Score> selectAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(Score t) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(Score t) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ArrayList<Score> selectByCondition() {
		// TODO Auto-generated method stub
		return null;
	}

}
