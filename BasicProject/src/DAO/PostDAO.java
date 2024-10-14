package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

public List<PostVo> getpostList(){
	List<PostVo> postList = null;
	
	String sql = "SELECT A.POST_ID, A.TITLE, A.CONTENT, B.USERNAME, A.CREATED_AT"
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
			pvo.setpost_id(rs.getInt("POST_ID"));
			pvo.setTitle(rs.getString("TITLE"));
			pvo.setContent(rs.getString("CONTENT"));
			pvo.setUser_name(rs.getString("USERNAME"));
			pvo..setCreated_at(rs.getTimestamp("CREATED_AT").toLocalDateTime());
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
	
	}
//게시글 전체 조회
//게시글 상세 조회
//게시글 수정
//게시글 삭제
	

}