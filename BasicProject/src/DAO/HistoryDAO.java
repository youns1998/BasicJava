package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import UTIL.DBUtil;
import VO.HistoryVo;
import DAO.*;

public class HistoryDAO {
	private Connection con = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;
	private PostDao postdao;
	private void disConnect() {
		if(rs != null) try { rs.close(); } catch(Exception e) {}
		if(ps != null) try { ps.close(); } catch(Exception e) {}
		if(con != null) try { con.close(); } catch(Exception e) {}
	}
	
	//거래 내역 추가
	public void addTransaction(HistoryVo history) {
		 String sql = "INSERT INTO TRANSACTION (buyer_id, seller_id, post_id, transaction_date) VALUES (?, ?, ?, ?)";

	       try (Connection con = DBUtil.getConnection();
	            PreparedStatement ps = con.prepareStatement(sql)) {
	             
	           ps.setString(1, history.getBuyer_id());
	           ps.setString(2, history.getSeller_id());
	           ps.setInt(3, history.getPost_id());
	           ps.setDate(4, new java.sql.Date(history.getTransaction_date().getTime()));
	           ps.executeUpdate();
	            
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }finally {
	        	disConnect();
	        }
    }
	
	
	//거래 내역 조회
	public List<HistoryVo> getTransactionHistory(String userId) {
        List<HistoryVo> historyList = new ArrayList<>();
        String sql = "SELECT * FROM TRANSACTION WHERE AND buyer_id = ? OR seller_id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
             
            ps.setString(1, userId);
            ps.setString(2, userId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                HistoryVo history = new HistoryVo();
                history.setTransaction_id(rs.getString("transaction_id"));
                history.setBuyer_id(rs.getString("buyer_id"));
                history.setSeller_id(rs.getString("seller_id"));
                history.setPost_id(rs.getInt("post_id"));
                history.setTransaction_date(rs.getDate("transaction_date"));
                historyList.add(history);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        postdao.checkPostConditionAndAddTransaction(0, userId, userId);
        }
        return historyList;
    }
	
	
	
	
	
	
	//특정 사용자 거래 조회
	//거래 내역 수정(상태 변경)
	//거래 내역 삭제
		
	}

