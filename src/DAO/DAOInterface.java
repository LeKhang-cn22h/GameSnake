package DAO;

import java.util.ArrayList;

public interface DAOInterface <T> {
	public int insert(T t);
	public T selectById (T t);
	public ArrayList<T> selectAll();
	public int update(T t);
	public int delete(T t);
	public ArrayList<T> selectByCondition();
}
