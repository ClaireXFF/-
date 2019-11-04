package Tetris;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQL {
	private Connection conn=null;
	private Statement s=null;
	private ResultSet result=null;
	
	//连接数据库
	public void connectMySQL(String database)
	{
		try{
			conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/db_Tetris?useSSL=false", "root", "");
			s=conn.createStatement();
		}
		catch (Exception e) {
			// TODO: handle exception
			System.out.println("连接服务器失败！");
			//e.getStackTrace();
		}
		
	}
	
	//查询语句
	public ResultSet select(String sql)throws SQLException
	{
		if(sql==null||sql.equals(""))
		{
			System.out.println("无查询语句");
			return null;
		}
		result=s.executeQuery(sql);
		return result;
	}
	
	//更新语句
	public void update(String sql)throws SQLException
	{
		if(sql==null||sql.equals(""))
		{
			System.out.println("无更新语句");
		}
		s.executeUpdate(sql);
		//System.out.println("影响的行数为："+i);
	}
	
	public void closeMySQL() throws SQLException
	{
		if(result!=null)
		{
			result.close();
			result=null;
		}
		if(s!=null)
		{
			s.close();
			s=null;
		}
		if(conn!=null)
		{
			conn.close();
			conn=null;
		}
	}
}
