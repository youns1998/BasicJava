package DAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import UTIL.DBUtil;
import UTIL.ScanUtil;
import VO.PostVo;

public class PostDao {
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
		if(rs != null) try { rs.close(); } catch(Exception e) {}
		if(ps != null) try { ps.close(); } catch(Exception e) {}
		if(con != null) try { con.close(); } catch(Exception e) {}
	}

////전체 게시판  List<PostVo>에 저장
//public List<PostVo> getpostList(){
//	List<PostVo> postList = new ArrayList<>();
//	
//	String sql = "SELECT A.POST_ID, A.TITLE, A.CONTENT, B.USERNAME, "
//			+ "A.CREATED_AT, A.UPDATED_AT"
//			+ " FROM POST A LEFT OUTER JOIN USERS B"
//			+ " ON A.USER_ID = B.USER_ID"
//			+ " ORDER BY A.POST_ID DESC";
//	try {
//		con = DBUtil.getConnection();
//		ps = con.prepareStatement(sql);
//		rs = ps.executeQuery();
//		
//		postList = new ArrayList<PostVo>();
//		while(rs.next()) {
//			PostVo pvo = new PostVo();
//			pvo.setPost_id(rs.getInt("POST_ID"));
//			pvo.setTitle(rs.getString("TITLE"));
//			pvo.setContent(rs.getString("CONTENT"));
//			pvo.setUsername(rs.getString("USERNAME"));
//			pvo.setCreated_at(rs.getTimestamp("CREATED_AT").toLocalDateTime());
//			pvo.setUpdated_at(rs.getTimestamp("UPDATED_AT").toLocalDateTime());
//			postList.add(pvo);
//		}
//	} catch (SQLException e) {
//		e.printStackTrace();
//	} finally {
//		disConnect();
//	}
//	return postList;
//}

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
			ps.setString(6, PostVo.getCondition());
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

String sql = "SELECT * FROM POST ORDER BY CREATED_AT DESC";

try {
	con = DBUtil.getConnection();
	ps=con.prepareStatement(sql);
	rs = ps.executeQuery();
	while(rs.next()) {
		PostVo  postvo = new PostVo();
		
		postvo.setPost_id(rs.getInt("POST_ID"));
		postvo.setUser_id(rs.getString("USER_ID"));
		postvo.setCategory_id(rs.getInt("CATEGORY_ID"));
		postvo.setTitle(rs.getNString("TITLE"));
		postvo.setContent(rs.getString("CONTENT"));
		postvo.setPrice(rs.getInt("PRICE"));
		postvo.setCondition(rs.getString("CONDITION"));
		postvo.setCreated_at(rs.getTimestamp("CREATED_AT").toLocalDateTime());
		postvo.setUpdated_at(rs.getTimestamp("UPDATED_AT").toLocalDateTime());
		postlist.add(postvo);
	}
} catch (Exception e) {
	e.printStackTrace();
}finally {
	disConnect();
}
		return postlist;
	}
	
	
//게시글 상세 조회 (POST ID를 받아와서 조회)
	public PostVo getPost(int post_id) {
		  PostVo postvo = new PostVo();
		String sql = "SELECT *FROM POST WHERE POST_ID = ?";
		
try {
	con=DBUtil.getConnection();
	ps=con.prepareStatement(sql);
	ps.setInt(1, post_id);
	rs=ps.executeQuery();
	
	 if (rs.next()) {
		 //	postvo.setCreated_at(rs.getTimestamp("CREATED_AT").toLocalDateTime());
		//	postvo.setUpdated_at(rs.getTimestamp("UPDATED_AT").toLocalDateTime());
			postvo.setPost_id(rs.getInt("POST_ID"));
			postvo.setTitle(rs.getNString("TITLE"));
			postvo.setUser_id(rs.getString("USER_ID"));
			postvo.setPrice(rs.getInt("PRICE"));
			postvo.setContent(rs.getString("CONTENT"));
			postvo.setCategory_id(rs.getInt("CATEGORY_ID"));
			postvo.setCondition(rs.getString("CONDITION"));
	 }	else {
			System.out.println("해당 게시물이 존재하지 않습니다");
			}
	 
	 } catch (Exception e) {
	e.printStackTrace();
}	finally {
	disConnect();
}
		return postvo;
}
	//게시글 전체 조회
	 public int updatePost(PostVo postvo) {
	        int cnt = 0;

	        String sql = "UPDATE POST SET TITLE = ?, CONTENT = ?, PRICE = ?, CONDITION = ?, CATEGORY_ID = ?, UPDATED_AT = SYSDATE WHERE POST_ID = ?";

	        try (Connection con = DBUtil.getConnection();
	             PreparedStatement ps = con.prepareStatement(sql)) {

	            ps.setString(1, postvo.getTitle());
	            ps.setString(2, postvo.getContent());
	            ps.setInt(3, postvo.getPrice());
	            ps.setString(4, postvo.getCondition());
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
	            System.out.println("수정할 항목을 선택하세요:");
	            System.out.println("1. 제목");
	            System.out.println("2. 내용");
	            System.out.println("3. 가격");
	            System.out.println("4. 상태");
	            System.out.println("0. 종료");

	            int choice = ScanUtil.nextInt();
	            switch (choice) {
	                case 1:
	                    System.out.print("새 제목을 입력하세요: ");
	                    String newTitle = ScanUtil.nextLine();
	                    postvo.setTitle(newTitle);
	                    break;
	                case 2:
	                    System.out.print("새 내용을 입력하세요: ");
	                    String newContent = ScanUtil.nextLine();
	                    postvo.setContent(newContent);
	                    break;
	                case 3:
	                    System.out.print("새 가격을 입력하세요: ");
	                    int newPrice = ScanUtil.nextInt();
	                    postvo.setPrice(newPrice);
	                    break;
	                case 4:
	                    System.out.print("새 상태를 입력하세요: ");
	                    String newCondition = ScanUtil.nextLine();
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
	                    System.out.println("게시물이 수정되었습니다.");
	                } else {
	                    System.out.println("게시물 수정에 실패했습니다.");
	                }
	            } catch (Exception e) {
	                System.out.println("게시물 수정 중 오류가 발생했습니다: " + e.getMessage());
	            }
	        }
	    }
	
//게시글 삭제
	
public int deletePost(int post_id) {
	int cnt =0;
	String sql= "DELETE FROM POST WHERE POST_ID = ?";
	
	try {
		con=DBUtil.getConnection();
		ps= con.prepareStatement(sql);
		
		ps.setInt(1, post_id);
		cnt = ps.executeUpdate();
		
	} catch (Exception e) {
		e.printStackTrace();
	}finally {
		disConnect();
	}
		return cnt;
 }
}


