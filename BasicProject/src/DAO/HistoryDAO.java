package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import VO.HistoryVo;

public class HistoryDAO {
	private Connection con = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;
	
	private void disConnect() {
		if(rs != null) try { rs.close(); } catch(Exception e) {}
		if(ps != null) try { ps.close(); } catch(Exception e) {}
		if(con != null) try { con.close(); } catch(Exception e) {}
	}
	
	//거래 내역 추가
	public int addTransaction(HistoryVo history) {
		int cnt = 0;
		
		String sql = "INSERT INTO TRANSACTION_HISTORY "
	}
	
	
	
	//거래 내역 전체 조회
	//특정 사용자 거래 조회
	//거래 내역 수정(상태 변경)
	//거래 내역 삭제
		
	}

