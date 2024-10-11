package kr.or.ddit.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import kr.or.ddit.util.DBUtil;
import kr.or.ddit.vo.BoardVO;



public class BoardDaoImpl {
	
	private Connection con = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;
	
	//싱글톤 패턴 ---------------------------------------------------------
	private static BoardDaoImpl dao;
	
	private BoardDaoImpl(){}
	
	public static BoardDaoImpl getInstance(){
		if(dao == null){
			dao = new BoardDaoImpl();
		}
		return dao;
	}
	// --------------------------------------------------------------------
	
	private void disConnect() {
		if(rs != null) try { rs.close(); } catch(Exception e) {}
		if(ps != null) try { ps.close(); } catch(Exception e) {}
		if(con != null) try { con.close(); } catch(Exception e) {}
	}
	
	// 게시글 전체 목록을 가져와 List에 저장하여 반환하는 메서드
	public List<BoardVO> getBoardList(){
		List<BoardVO> boardList = null;
		
		String sql = "SELECT A.BOARD_NO, A.TITLE, A.CONTENT, B.USER_NAME, A.REG_DATE"
				+ " FROM TB_JDBC_BOARD A LEFT OUTER JOIN TB_JDBC_USER B"
				+ " ON A.USER_ID = B.USER_ID"
				+ " ORDER BY A.BOARD_NO DESC";
		try {
			con = DBUtil.getConnection();
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			boardList = new ArrayList<BoardVO>();
			while(rs.next()) {
				BoardVO bvo = new BoardVO();
				bvo.setBoard_no(rs.getInt("BOARD_NO"));
				bvo.setTitle(rs.getString("TITLE"));
				bvo.setContent(rs.getString("CONTENT"));
				bvo.setUser_name(rs.getString("USER_NAME"));
				bvo.setReg_date(rs.getString("REG_DATE"));
				boardList.add(bvo);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disConnect();
		}
		return boardList;
	}
	
	
	// 추가할 게시글 정보가 저장된 VO객체를 매개변수로 받아서 추가하는 메서드
	public int insertBoard(BoardVO boardVo) {
		int cnt = 0;
		
		String sql = "INSERT INTO TB_JDBC_BOARD (BOARD_NO, TITLE, CONTENT, USER_ID, REG_DATE) "
				+ " VALUES( (SELECT NVL(MAX(BOARD_NO), 0) + 1 FROM TB_JDBC_BOARD), ?, ?, ?, SYSDATE)";
		
		try {
			con = DBUtil.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, boardVo.getTitle());
			ps.setString(2, boardVo.getContent());
			ps.setString(3, boardVo.getUser_id());
			
			cnt = ps.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disConnect();
		}
		
		return cnt;
	}
	
	
	// 게시글 번호를 매개변수로 받아서 해당 게시글의 내용을 가져와 VO에 저장하여 반환하는 메서드
	public BoardVO getBoard(int boardNo) {
		BoardVO boardVo = null;
		String sql = "SELECT A.BOARD_NO, A.TITLE, A.CONTENT, B.USER_NAME, A.REG_DATE"
				+ " FROM TB_JDBC_BOARD A"
				+ " LEFT OUTER JOIN TB_JDBC_USER B"
				+ " ON A.USER_ID = B.USER_ID"
				+ " WHERE BOARD_NO = ?";
		
		try {
			con = DBUtil.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, boardNo);
			
			rs = ps.executeQuery();
			if(rs.next()) {
				boardVo = new BoardVO();
				boardVo.setBoard_no(rs.getInt("BOARD_NO"));
				boardVo.setTitle(rs.getString("TITLE"));
				boardVo.setContent(rs.getString("CONTENT"));
				boardVo.setUser_name(rs.getString("USER_NAME"));
				boardVo.setReg_date(rs.getString("REG_DATE"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disConnect();
		}
		
		return boardVo;
	}
	
	// 수정할 게시글 정보가 저장된 VO객체를 매개변수로 받아서 수정하는 메서드
	public int updateBoard(BoardVO boardVo) {
		int cnt = 0;
		
		String sql = "UPDATE TB_JDBC_BOARD SET "
				+ " TITLE = ? , CONTENT = ? , REG_DATE = SYSDATE "
				+ " WHERE BOARD_NO = ? ";
		
		try {
			con = DBUtil.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, boardVo.getTitle());
			ps.setString(2, boardVo.getContent());
			ps.setInt(3, boardVo.getBoard_no());
			
			cnt = ps.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disConnect();
		}
		
		return cnt;
	}
	
	// 게시글 번호를 매개변수로 받아서 해당 게시글 정보를 삭제하는 메서드
	public int deleteBoard(int boardNo) {
		int cnt = 0;
		
		String sql = "DELETE FROM TB_JDBC_BOARD "
				+ " WHERE BOARD_NO = ? ";
		
		try {
			con = DBUtil.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, boardNo);
			
			cnt = ps.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disConnect();
		}
		
		return cnt;
	}
}
