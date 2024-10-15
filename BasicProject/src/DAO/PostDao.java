package DAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import UTIL.DBUtil;
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

String sql = "SELECT * FROM POST ORDER BY IS_NOTICE DESC, CREATED_AT DESC";

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
		 	postvo.setCreated_at(rs.getTimestamp("CREATED_AT").toLocalDateTime());
			postvo.setUpdated_at(rs.getTimestamp("UPDATED_AT").toLocalDateTime());
			postvo.setPost_id(rs.getInt("POST_ID"));
			postvo.setTitle(rs.getNString("TITLE"));
			postvo.setUser_id(rs.getString("USER_ID"));
			postvo.setUsername(rs.getString("USERNAME"));
			postvo.setPrice(rs.getInt("PRICE"));
			postvo.setContent(rs.getString("CONTENT"));
			postvo.setCategory_id(rs.getInt("CATEGORY_ID"));
			postvo.setCondition(rs.getString("CONDITION"));
}
	 } catch (Exception e) {
	e.printStackTrace();
}	finally {
	disConnect();
}
		return postvo;
}
	
//게시글 수정
	public int updatePost(PostVo postvo) {
		int cnt = 0;		
		
	String sql= "UPDATE POST SET TITLE = ?, CONTENT = ?, PRICE = ?, CONDITION = ?, "
			+ " CATEGORY_ID = ?, UPDATED_AT = SYSDATE WHERE POST_ID = ?";	
	
	try {
		con=DBUtil.getConnection();
		ps= con.prepareStatement(sql);
		
		ps.setString(1, postvo.getTitle());
		ps.setString(2, postvo.getContent());
		ps.setInt(3, postvo.getPrice());
		ps.setString(4, postvo.getCondition());
		ps.setInt(5, postvo.getCategory_id());
//		ps.setTimestamp(6, Timestamp.valueOf(postvo.getUpdated_at()));
		ps.setInt(7, postvo.getPost_id());
		
		cnt = ps.executeUpdate();
		
		
	} catch (Exception e) {
		e.printStackTrace();
	}finally {
		disConnect();
	}
		return cnt;
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


