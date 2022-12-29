package application;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.swing.plaf.metal.MetalIconFactory.TreeControlIcon;

import java.sql.DriverManager;

public class DBAccess {
	private Connection conn = null;
	
	public DBAccess(){
		super();

		try {
			conn = DriverManager.getConnection(
//				"jdbc:mysql://localhost/sincere?user=ozaki&password=gargoyl");
				"jdbc:mysql://sw02-moriyama/sincere?user=ozaki&password=gargoyl");
//			System.out.println("DBOK!!");
//			conn.close();
		}catch(SQLException ex){
			//接続できない場合
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: "+ ex.getSQLState());
			System.out.println("VendorError "+ ex.getErrorCode());
		}
		
	}
	public ResultSet executeQuery(String sql) throws Exception{
		PreparedStatement stmt = conn.prepareStatement(sql);
		return stmt.executeQuery();
	}
	public ResultSet executeQuery(String sql,List<Object> param)throws Exception{
		PreparedStatement stmt=conn.prepareStatement(sql);
		for(int i=0;i<param.size();i++) {
			if(param.get(i)instanceof String) {
				stmt.setString(i, (String)param.get(i));
			}else if(param.get(i)instanceof Integer) {
				stmt.setInt(i, (int)param.get(i));
			}
		}
		return stmt.executeQuery();
	}
	public void updateDb(String sql) {
//		Connection conn;
		Statement stat;

		try {
//			conn = DriverManager.getConnection(
//				"jdbc:mysql://localhost/sincere?user=ozaki&password=gargoyl");
			
			try {
				//クエリ
				stat = this.conn.createStatement();

				int result =stat.executeUpdate(sql);
				System.out.println(result + "件");
				
			}finally{
				// finally method
				this.conn.close();
			}
					
		}catch(SQLException ex) {
			//接続できない場合
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: "+ ex.getSQLState());
			System.out.println("VendorError "+ ex.getErrorCode());
		}
	}
	public void DisCon() {
		try {
			conn.close();
			if(conn.isClosed()) {
//			System.out.println("close OK!!");
			}else {
			System.out.println("connected!!");
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	public String sqlNull(String string) {
		if(string==null || string.length()==0) {
			return "null";
		}else {
			return string;
		}
	}


}
