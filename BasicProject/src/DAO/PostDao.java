package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import UTIL.Command;
import UTIL.DBUtil;
import UTIL.ScanUtil;
import VO.HistoryVo;
import VO.PostVo;

public class PostDao {
	public static final int CONDITION_SALE = 1; // 판매중
	public static final int CONDITION_RESERVED = 2; // 예약중
	public static final int CONDITION_SOLD_OUT = 3; // 판매완료
	private Connection con = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;
	private static PostDao instance;

	public static PostDao getInstance() {
		if (instance == null)
			instance = new PostDao();
		return instance;
	}

	private void disConnect() {
		if (rs != null)
			try {
				rs.close();
			} catch (Exception e) {
			}
		if (ps != null)
			try {
				ps.close();
			} catch (Exception e) {
			}
		if (con != null)
			try {
				con.close();
			} catch (Exception e) {
			}
	}

//게시글추가
	public int insertPost(PostVo PostVo) {
		int cnt = 0;
		String sql = "INSERT INTO POST (POST_ID, USER_ID, PRICE, CATEGORY_ID, TITLE, CONTENT, CONDITION, CREATED_AT, UPDATED_AT) "
				+ " VALUES( (SELECT NVL(MAX(POST_ID), 0) + 1 FROM POST), ?, ?, ?, ?, ?, ?, SYSDATE, SYSDATE)";

		try {
			con = DBUtil.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, PostVo.getUser_id());
			ps.setInt(2, PostVo.getPrice());
			ps.setInt(3, PostVo.getCategory_id());
			ps.setString(4, PostVo.getTitle());
			ps.setString(5, PostVo.getContent());
			ps.setInt(6, PostVo.getCondition());
//			ps.setTimestamp(5, Timestamp.valueOf(PostVo.getCreated_at()));
//			ps.setTimestamp(6, Timestamp.valueOf(PostVo.getUpdated_at())); 
			cnt = ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disConnect();
		}
		return cnt;
	}

//게시글 전체 조회
	public List<PostVo> getAllPosts() {
		List<PostVo> postlist = new ArrayList<PostVo>();

		String sql = "SELECT * FROM POST ORDER BY CREATED_AT";

		try {
			con = DBUtil.getConnection();
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				PostVo postvo = new PostVo();

				postvo.setPost_id(rs.getInt("POST_ID"));
				postvo.setUser_id(rs.getString("USER_ID"));
				postvo.setCategory_id(rs.getInt("CATEGORY_ID"));
				postvo.setTitle(rs.getNString("TITLE"));
				postvo.setContent(rs.getString("CONTENT"));
				postvo.setPrice(rs.getInt("PRICE"));
				postvo.setCondition(rs.getInt("CONDITION"));
				postvo.setCreated_at(rs.getTimestamp("CREATED_AT").toLocalDateTime());
				postvo.setUpdated_at(rs.getTimestamp("UPDATED_AT").toLocalDateTime());
				postlist.add(postvo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			disConnect();
		}
		return postlist;
	}

//내가 쓴글 전체 조회
	public List<PostVo> getAllPosts(String userid) {
		List<PostVo> postlist = new ArrayList<PostVo>();

		String sql = "SELECT * FROM POST WHERE USER_ID = ? ";

		try {
			con = DBUtil.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, userid);
			rs = ps.executeQuery();
			while (rs.next()) {
				PostVo postvo = new PostVo();
				postvo.setPost_id(rs.getInt("POST_ID"));
				postvo.setUser_id(rs.getString("USER_ID"));
				postvo.setCategory_id(rs.getInt("CATEGORY_ID"));
				postvo.setTitle(rs.getNString("TITLE"));
				postvo.setContent(rs.getString("CONTENT"));
				postvo.setPrice(rs.getInt("PRICE"));
				postvo.setCondition(rs.getInt("CONDITION"));
				postvo.setCreated_at(rs.getTimestamp("CREATED_AT").toLocalDateTime());
				postvo.setUpdated_at(rs.getTimestamp("UPDATED_AT").toLocalDateTime());
				postlist.add(postvo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			disConnect();
		}
		return postlist;
	}

//게시글 상세 조회 (POST ID를 받아와서 조회)
	public PostVo getPost(int post_id) {
		PostVo postvo = new PostVo();
		String sql = "SELECT *FROM POST WHERE POST_ID = ?";

		try {
			con = DBUtil.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, post_id);
			rs = ps.executeQuery();

			if (rs.next()) {
				// postvo.setCreated_at(rs.getTimestamp("CREATED_AT").toLocalDateTime());
				// postvo.setUpdated_at(rs.getTimestamp("UPDATED_AT").toLocalDateTime());
				postvo.setPost_id(rs.getInt("POST_ID"));
				postvo.setTitle(rs.getNString("TITLE"));
				postvo.setUser_id(rs.getString("USER_ID"));
				postvo.setPrice(rs.getInt("PRICE"));
				postvo.setContent(rs.getString("CONTENT"));
				postvo.setCategory_id(rs.getInt("CATEGORY_ID"));
				postvo.setCondition(rs.getInt("CONDITION"));
			} else {
				System.out.println("해당 게시물이 존재하지 않습니다");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			disConnect();
		}
		return postvo;
	}

	// 게시글 상세 조회 (USER_ID를 받아와서 조회)
	public PostVo getPostuser(String userid) {
		PostVo postvo = new PostVo();
		String sql = "SELECT *FROM POST WHERE USER_ID = ?";

		try {
			con = DBUtil.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, userid);
			rs = ps.executeQuery();

			if (rs.next()) {
				// postvo.setCreated_at(rs.getTimestamp("CREATED_AT").toLocalDateTime());
				// postvo.setUpdated_at(rs.getTimestamp("UPDATED_AT").toLocalDateTime());
				postvo.setPost_id(rs.getInt("POST_ID"));
				postvo.setTitle(rs.getNString("TITLE"));
				postvo.setUser_id(rs.getString("USER_ID"));
				postvo.setPrice(rs.getInt("PRICE"));
				postvo.setContent(rs.getString("CONTENT"));
				postvo.setCategory_id(rs.getInt("CATEGORY_ID"));
				postvo.setCondition(rs.getInt("CONDITION"));
			} else {
				System.out.println("해당 게시물이 존재하지 않습니다");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			disConnect();
		}
		return postvo;
	}

	// 게시글 전체 정보 수정
	public int updatePost(PostVo postvo) {
		int cnt = 0;

		String sql = "UPDATE POST SET TITLE = ?, CONTENT = ?, PRICE = ?, CONDITION = ?, CATEGORY_ID = ?, UPDATED_AT = SYSDATE WHERE POST_ID = ?";

		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, postvo.getTitle());
			ps.setString(2, postvo.getContent());
			ps.setInt(3, postvo.getPrice());
			ps.setInt(4, postvo.getCondition());
			ps.setInt(5, postvo.getCategory_id());
			ps.setInt(6, postvo.getPost_id());

			cnt = ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return cnt;
	}

	// 선택 수정 메서드
	public void updatePostSelect(PostVo postvo) {
		while (true) {
			System.out.println();
			System.out.println("수정할 항목을 선택하세요 >>");
			System.out.println("1.제목 2.내용 3.가격 4.상태 0.종료");

			int choice = ScanUtil.nextInt();
			switch (choice) {
			case 1:
				System.out.print("변경할 제목을 입력하세요: ");
				String newTitle = ScanUtil.nextLine();
				postvo.setTitle(newTitle);
				break;
			case 2:
				System.out.print("변경할 내용을 입력하세요: ");
				String newContent = ScanUtil.nextLine();
				postvo.setContent(newContent);
				break;
			case 3:
				System.out.print("변경할 가격을 입력하세요: ");
				int newPrice = ScanUtil.nextInt();
				postvo.setPrice(newPrice);
				break;
			case 4:
				int newCondition = ScanUtil.nextInt("변경할 상태를 입력하세요 \n 1.판매중\t2.예약중\t3.판매완료\n선택 >> ");
				postvo.setCondition(newCondition);
				break;
			case 0:
				System.out.println("수정을 종료합니다.");
				return;
			default:
				System.out.println("잘못된 선택입니다. 다시 시도하세요.");
				continue;
			}

			// 수정된 내용을 데이터베이스에 반영
			try {
				int result = updatePost(postvo); // 게시물 업데이트
				if (result > 0) {
					System.out.println("게시물이 수정되었습니다.\n " + "1.더 수정 하기 0.되돌아 가기");
					int y = ScanUtil.nextInt();
					if (y == 1) {
						continue;
					}
					if (y == 0) {
						break;
					}

				} else {
					System.out.println("게시물 수정에 실패했습니다.");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

//게시글 삭제

	public int deletePost(int post_id) {
		int cnt = 0;
		String sql = "DELETE FROM POST WHERE POST_ID = ?";

		try {
			con = DBUtil.getConnection();
			ps = con.prepareStatement(sql);

			ps.setInt(1, post_id);
			cnt = ps.executeUpdate();
			if (cnt > 0) {
				System.out.println("게시물이 삭제되었습니다");
			} else {
				System.out.println("게시물 삭제가 실패했습니다.");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			disConnect();
		}
		return cnt;
	}

// 특정 게시물의 상태를 확인하고 거래 완료 시 거래 내역 추가
	public void checkPostConditionAndAddTransaction(int postId, String buyerId, String sellerId) {
		String sql = "SELECT CONDITION FROM POST WHERE post_id = ?";
		String condition = null;

		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, postId);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				condition = rs.getString("CONDITION");
			}

			if ("거래완료".equals(condition)) {
				HistoryVo history = new HistoryVo();
				history.setBuyer_id(buyerId);
				history.setSeller_id(sellerId);
				history.setPost_id(postId);
				history.setTransaction_date(new java.util.Date()); // 현재 시간

				HistoryDAO historyDAO = new HistoryDAO();
				historyDAO.addTransaction(history);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

// 거래상태 업데이트 
	public static void updatePostCondition(int postId, String newCondition, String buyerId, String sellerId) {
		String sql = "UPDATE POST SET CONDITION = ? WHERE post_id = ?";

		try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, newCondition);
			ps.setInt(2, postId);
			ps.executeUpdate();

			// 상태 변경 후 거래 내역 추가 확인
			PostDao postDAO = new PostDao();
			postDAO.checkPostConditionAndAddTransaction(postId, buyerId, sellerId);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// 게시글 검색 (거래 완료된 상품 제외)
	public List<PostVo> searchPosts(String keyword) {
	    List<PostVo> postlist = new ArrayList<PostVo>();
	    String sql = "SELECT * FROM POST WHERE (TITLE LIKE ? OR CONTENT LIKE ?) AND CONDITION <> ? ORDER BY CREATED_AT";

	    try {
	        con = DBUtil.getConnection();
	        ps = con.prepareStatement(sql);
	        ps.setString(1, "%" + keyword + "%"); // 제목 검색
	        ps.setString(2, "%" + keyword + "%"); // 내용 검색
	        ps.setInt(3, CONDITION_SOLD_OUT); // 거래 완료 상태 제외
	        rs = ps.executeQuery();

	        while (rs.next()) {
	            PostVo postvo = new PostVo();
	            postvo.setPost_id(rs.getInt("POST_ID"));
	            postvo.setUser_id(rs.getString("USER_ID"));
	            postvo.setCategory_id(rs.getInt("CATEGORY_ID"));
	            postvo.setTitle(rs.getNString("TITLE"));
	            postvo.setContent(rs.getString("CONTENT"));
	            postvo.setPrice(rs.getInt("PRICE"));
	            postvo.setCondition(rs.getInt("CONDITION"));
	            postvo.setCreated_at(rs.getTimestamp("CREATED_AT").toLocalDateTime());
	            postvo.setUpdated_at(rs.getTimestamp("UPDATED_AT").toLocalDateTime());
	            postlist.add(postvo);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        disConnect();
	    }
	    return postlist;
	}
	
}
