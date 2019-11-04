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
	
	//�������ݿ�
	public void connectMySQL(String database)
	{
		try{
			conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/db_Tetris?useSSL=false", "root", "");
			s=conn.createStatement();
		}
		catch (Exception e) {
			// TODO: handle exception
			System.out.println("���ӷ�����ʧ�ܣ�");
			//e.getStackTrace();
		}
		
	}
	
	//��ѯ���
	public ResultSet select(String sql)throws SQLException
	{
		if(sql==null||sql.equals(""))
		{
			System.out.println("�޲�ѯ���");
			return null;
		}
		result=s.executeQuery(sql);
		return result;
	}
	
	//�������
	public void update(String sql)throws SQLException
	{
		if(sql==null||sql.equals(""))
		{
			System.out.println("�޸������");
		}
		s.executeUpdate(sql);
		//System.out.println("Ӱ�������Ϊ��"+i);
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
