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



public class PostDAO {
	private Connection con = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;
	
private void disConnect() {
		if(rs != null) try { rs.close(); } catch(Exception e) {}
		if(ps != null) try { ps.close(); } catch(Exception e) {}
		if(con != null) try { con.close(); } catch(Exception e) {}
	}

//전체 게시판  List<PostVo>에 저장
public List<PostVo> getpostList(){
	List<PostVo> postList = new ArrayList<>();
	
	String sql = "SELECT A.POST_ID, A.TITLE, A.CONTENT, B.USERNAME, "
			+ "A.CREATED_AT, A.UPDATED_AT"
			+ " FROM POST A LEFT OUTER JOIN USERS B"
			+ " ON A.USER_ID = B.USER_ID"
			+ " ORDER BY A.POST_ID DESC";
	try {
		con = DBUtil.getConnection();
		ps = con.prepareStatement(sql);
		rs = ps.executeQuery();
		
		postList = new ArrayList<PostVo>();
		while(rs.next()) {
			PostVo pvo = new PostVo();
			pvo.setPost_id(rs.getInt("POST_ID"));
			pvo.setTitle(rs.getString("TITLE"));
			pvo.setContent(rs.getString("CONTENT"));
			pvo.setUsername(rs.getString("USERNAME"));
			pvo.setCreated_at(rs.getTimestamp("CREATED_AT").toLocalDateTime());
			pvo.setUpdated_at(rs.getTimestamp("UPDATED_AT").toLocalDateTime());
			postList.add(pvo);
		}
		
	} catch (SQLException e) {
		e.printStackTrace();
	} finally {
		disConnect();
	}
	return postList;
}

//게시글추가
	public int insertpost(PostVo PostVo) {
int cnt = 0;
		
		String sql = "INSERT INTO POST (POST_ID, USER_ID, CATEGORY_ID, TITLE, CONTENT, CREATED_AT, UPDATED_AT) "
				+ " VALUES( (SELECT NVL(MAX(POST_ID), 0) + 1 FROM POST), ?, ?, ?, SYSDATE)";
		
		try {
			con = DBUtil.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, PostVo.getUser_id());
			ps.setInt(2, PostVo.getCategory_id());
			ps.setString(3, PostVo.getTitle());
			ps.setString(4, PostVo.getContent());
			ps.setTimestamp(5, Timestamp.valueOf(PostVo.getCreated_at()));
			ps.setTimestamp(6, Timestamp.valueOf(PostVo.getUpdated_at())); 
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

String sql = "SELECT * FROM POSTS";

try {
	con = DBUtil.getConnection();
	ps=con.prepareStatement(sql);
	rs = ps.executeQuery();
	while(rs.next()) {
		PostVo  postvo = new PostVo();
		
		postvo.setCreated_at(rs.getTimestamp("CREATED_AT").toLocalDateTime());
		postvo.setUpdated_at(rs.getTimestamp("UPDATE_AT").toLocalDateTime());
		postvo.setPost_id(rs.getInt("POST_ID"));
		postvo.setTitle(rs.getNString("TITLE"));
		postvo.setUser_id(rs.getString("USER_ID"));
		postvo.setUsername(rs.getString("USERNAME"));
		postvo.setPrice(rs.getInt("PRICE"));
		postvo.setContent(rs.getString("CONTENT"));
		postvo.setCategory_id(rs.getInt("CATEGORY_ID"));
		postvo.setCondition(rs.getString("CONDITION"));
		postlist.add(postvo);
		
		
	}
} catch (Exception e) {
	e.printStackTrace();
}finally {
	disConnect();
}
		return postlist;
	}
	
	
//게시글 상세 조회 (유저 ID를 받아와서 조회)
	public PostVo getPost(String user_id) {
		  PostVo postvo = null;
		String sql = "SELECT *FROM posts WHERE user_id = ?";
		
try {
	con=DBUtil.getConnection();
	ps=con.prepareStatement(sql);
	ps.setString(1, user_id);
	rs=ps.executeQuery();
	
	 if (rs.next()) {
		 	postvo.setCreated_at(rs.getTimestamp("CREATED_AT").toLocalDateTime());
			postvo.setUpdated_at(rs.getTimestamp("UPDATE_AT").toLocalDateTime());
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
}finally {
	disConnect();
}
		return postvo;
}
	
	
//게시글 수정
//게시글 삭제
	

	
	
	
	
	
	
	
	
	
	
}









