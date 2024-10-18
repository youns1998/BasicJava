package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import UTIL.DBUtil;
import VO.HistoryVo;
import DAO.*;

public class HistoryDAO {

	// 거래 내역 추가
	public void addTransaction(HistoryVo history) {
	    String checkSql = "SELECT COUNT(*) FROM TRANSACTION WHERE POST_ID = ?";
	    String updateSql = "UPDATE TRANSACTION SET BUYER_ID = ?, SELLER_ID = ?, TRANSACTION_DATE = ?, TRANSACTION_STATUS = ? WHERE POST_ID = ?";
	    String insertSql = "INSERT INTO TRANSACTION (buyer_id, seller_id, post_id, transaction_date, transaction_status) VALUES (?, ?, ?, ?, ?)";

	    try (Connection con = DBUtil.getConnection(); 
	         PreparedStatement checkPs = con.prepareStatement(checkSql);
	         PreparedStatement updatePs = con.prepareStatement(updateSql);
	         PreparedStatement insertPs = con.prepareStatement(insertSql)) {

	        // 먼저 POST_ID가 이미 존재하는지 확인
	        checkPs.setInt(1, history.getPost_id());
	        ResultSet rs = checkPs.executeQuery();
	        rs.next();
	        int count = rs.getInt(1);

	        if (count > 0) {
	            // POST_ID가 이미 존재하면 UPDATE
	            updatePs.setString(1, history.getBuyer_id());
	            updatePs.setString(2, history.getSeller_id());
	            updatePs.setDate(3, new java.sql.Date(history.getTransaction_date().getTime()));
	            updatePs.setInt(4, history.getTransaction_status());
	            updatePs.setInt(5, history.getPost_id());
	            updatePs.executeUpdate();
	            System.out.println("기존 거래 내역이 업데이트되었습니다.");
	        } else {
	            // POST_ID가 없으면 INSERT
	            insertPs.setString(1, history.getBuyer_id());
	            insertPs.setString(2, history.getSeller_id());
	            insertPs.setInt(3, history.getPost_id());
	            insertPs.setDate(4, new java.sql.Date(history.getTransaction_date().getTime()));
	            insertPs.setInt(5, history.getTransaction_status());
	            insertPs.executeUpdate();
	            System.out.println("새로운 거래 내역이 추가되었습니다.");
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}


	// 거래 상태 업데이트
	public void updateTransactionStatus(int postId, int status) {
		String sql = "UPDATE TRANSACTION SET transaction_status = ? WHERE post_id = ?";
		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, status);
			ps.setInt(2, postId);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 거래 내역 삭제 메서드
	public void deleteTransactionByPost(int postId) {
		String sql = "DELETE FROM TRANSACTION WHERE POST_ID = ?";

		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, postId);
			int result = ps.executeUpdate();

			if (result > 0) {
				System.out.println("해당 게시물과 연관된 거래 내역이 삭제되었습니다.");
			} else {
				System.out.println("삭제할 거래 내역이 없습니다.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 거래 내역 삭제
	public void deleteTransaction(int postId) {
		String sql = "DELETE FROM TRANSACTION WHERE post_id = ?";
		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, postId);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 거래 내역 조회
	// 거래 내역 조회 메서드에서 거래 상태 출력
	public List<HistoryVo> getTransactionHistory(String userId) {
	    List<HistoryVo> historyList = new ArrayList<>();
	    String sql = "SELECT TRANSACTION_ID, BUYER_ID, SELLER_ID, POST_ID, TRANSACTION_DATE, TRANSACTION_STATUS " +
	                 "FROM TRANSACTION WHERE BUYER_ID = ? OR SELLER_ID = ?";

	    try (Connection conn = DBUtil.getConnection(); 
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setString(1, userId);
	        pstmt.setString(2, userId);
	        ResultSet rs = pstmt.executeQuery();

	        while (rs.next()) {
	            HistoryVo history = new HistoryVo();
	            history.setTransaction_id(rs.getString("TRANSACTION_ID"));
	            history.setBuyer_id(rs.getString("BUYER_ID"));
	            history.setSeller_id(rs.getString("SELLER_ID"));
	            history.setPost_id(rs.getInt("POST_ID"));
	            history.setTransaction_date(rs.getDate("TRANSACTION_DATE"));
	            int transactionStatus = rs.getInt("TRANSACTION_STATUS");
	            history.setTransaction_status(transactionStatus);

	            // 거래 상태를 문자열로 변환하여 출력
	            String statusString;
	            switch (transactionStatus) {
	                case 1:
	                    statusString = "판매중";
	                    break;
	                case 2:
	                    statusString = "예약중";
	                    break;
	                case 3:
	                    statusString = "거래완료";
	                    break;
	                default:
	                    statusString = "알 수 없음";
	                    break;
	            }

	            System.out.println("======= 거래 내역 =======");
	            System.out.printf("거래 번호: %s | 구매자 ID: %s | 판매자 ID: %s | 게시글 ID: %d | 거래 날짜: %s | 거래 상태: %s\n",
	                              history.getTransaction_id(), history.getBuyer_id(), history.getSeller_id(),
	                              history.getPost_id(), history.getTransaction_date(), statusString);

	            historyList.add(history);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return historyList;
	}
	
	// 특정 게시글(postId)에 대한 거래 내역에서 구매자 ID 가져오기
	public String getBuyerIdFromTransaction(int postId) {
        String buyerId = null;
        String sql = "SELECT BUYER_ID FROM TRANSACTION WHERE POST_ID = ? AND TRANSACTION_STATUS = 2";  // 예약중 상태인 트랜잭션

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, postId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                buyerId = rs.getString("BUYER_ID");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return buyerId;  // 없을 경우 null 반환
    }




}
