package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import UTIL.DBUtil;
import VO.CategoryVo;
import VO.UsersVo;

public class CategoryDAO {
    private Connection con = null;  // 데이터베이스 연결 객체
    private PreparedStatement ps = null;  // SQL 쿼리 실행 객체
    private ResultSet rs = null;  // 쿼리 결과 저장 객체
    
    private static CategoryDAO instance;  // CategoryDAO 싱글톤 인스턴스를 저장할 변수

    // 기본 생성자 - 외부에서 인스턴스를 생성하지 못하도록 private 설정
    private CategoryDAO() {}

    // 싱글톤 패턴을 적용하여 단일 인스턴스 반환
    public static CategoryDAO getInstance() {
        if (instance == null)
            instance = new CategoryDAO();  // 인스턴스가 없을 경우 생성
        return instance;  // 이미 존재하는 인스턴스를 반환
    }
    
    // 데이터베이스 연결 해제 메서드
    private void disConnect() {
        if(rs != null) try { rs.close(); } catch(Exception e) {}  // ResultSet 닫기
        if(ps != null) try { ps.close(); } catch(Exception e) {}  // PreparedStatement 닫기
        if(con != null) try { con.close(); } catch(Exception e) {}  // Connection 닫기
    }
    
    // 카테고리 목록 조회 메서드
    public List<CategoryVo> getCategoryList(){
        List<CategoryVo> cateList = null;
        String sql = "SELECT * FROM CATEGORY ORDER BY CATEGORY_ID";  // 카테고리 목록을 정렬하여 조회하는 SQL

        try {
            con = DBUtil.getConnection();  // 데이터베이스 연결
            ps = con.prepareStatement(sql);  // SQL 쿼리 준비
            rs = ps.executeQuery();  // 쿼리 실행 및 결과 반환
            
            cateList = new ArrayList<CategoryVo>();  // 결과를 저장할 리스트 초기화
            while(rs.next()) {  // 결과 집합에서 한 행씩 읽음
                CategoryVo catevo = new CategoryVo();
                catevo.setCategory_id((rs.getInt("CATEGORY_ID")));  // 카테고리 ID 설정
                catevo.setCategory_name((rs.getString("CATEGORY_NAME")));  // 카테고리 이름 설정
                cateList.add(catevo);  // 리스트에 추가
            }
        } catch (SQLException e) {
            e.printStackTrace();  // 예외 발생 시 오류 메시지 출력
        } finally {
            disConnect();  // 연결 해제
        }
        return cateList;  // 카테고리 목록 반환
    }
    
    // 카테고리 선택 조회 메서드
    public CategoryVo getCategory(int category_id) {
        CategoryVo cate = null;
        String sql = "SELECT * FROM CATEGORY WHERE CATEGORY_ID = ?";  // 특정 카테고리를 조회하는 SQL
        try {
            con = DBUtil.getConnection();  // 데이터베이스 연결
            ps = con.prepareStatement(sql);  // SQL 쿼리 준비
            ps.setInt(1, category_id);  // 쿼리에 카테고리 ID 바인딩
            rs = ps.executeQuery();  // 쿼리 실행
            
            if (rs.next()) {  // 결과가 있을 경우
                cate = new CategoryVo();
                cate.setCategory_id(rs.getInt("CATEGORY_ID"));  // 카테고리 ID 설정
                cate.setCategory_name(rs.getString("CATEGORY_NAME"));  // 카테고리 이름 설정
            }
        } catch (Exception e) {
            e.printStackTrace();  // 예외 발생 시 오류 메시지 출력
        } finally {
            disConnect();  // 연결 해제
        }
        return cate;  // 조회된 카테고리 반환
    }
    
    // 카테고리 추가 메서드
    public int InsertCategory(CategoryVo cate) {
        int cnt = 0;
        String sql = "INSERT INTO CATEGORY (CATEGORY_ID, CATEGORY_NAME) VALUES (?,?) ";  // 카테고리 추가 SQL
        try {
            con = DBUtil.getConnection();  // 데이터베이스 연결
            ps = con.prepareStatement(sql);  // SQL 쿼리 준비
            ps.setInt(1, cate.getCategory_id());  // 카테고리 ID 바인딩
            ps.setString(2, cate.getCategory_name());  // 카테고리 이름 바인딩
            cnt = ps.executeUpdate();  // 쿼리 실행 후 영향받은 행 수 반환
        } catch (SQLException e) {
            e.printStackTrace();  // 예외 발생 시 오류 메시지 출력
        } finally {
            disConnect();  // 연결 해제
        }
        return cnt;  // 추가된 행 수 반환
    }
    
    // 카테고리 수정 메서드
    public int UpdateCategory(CategoryVo cate, int newid) {
        int cnt = 0;
        String sql = "UPDATE CATEGORY SET CATEGORY_ID = ? , CATEGORY_NAME = ? WHERE CATEGORY_ID = ?";  // 카테고리 수정 SQL
        
        try {
            con = DBUtil.getConnection();  // 데이터베이스 연결
            ps = con.prepareStatement(sql);  // SQL 쿼리 준비
            ps.setInt(1, newid);  // 새로운 카테고리 ID 바인딩
            ps.setString(2, cate.getCategory_name());  // 새로운 카테고리 이름 바인딩
            ps.setInt(3, cate.getCategory_id());  // 기존 카테고리 ID 바인딩
            cnt = ps.executeUpdate();  // 쿼리 실행 후 영향받은 행 수 반환
        } catch (SQLException e) {
            e.printStackTrace();  // 예외 발생 시 오류 메시지 출력
        } finally {
            disConnect();  // 연결 해제
        }
        return cnt;  // 수정된 행 수 반환
    }

    // 카테고리 중복 여부 확인 메서드
    public boolean isCategoryIdExists(int categoryId) {
        String sql = "SELECT COUNT(*) FROM CATEGORY WHERE CATEGORY_ID = ?";  // 카테고리 존재 여부를 확인하는 SQL
        try (Connection con = DBUtil.getConnection();  // 자동 자원 해제를 위한 try-with-resources 사용
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, categoryId);  // 카테고리 ID 바인딩
            ResultSet rs = ps.executeQuery();  // 쿼리 실행
            if (rs.next()) {
                return rs.getInt(1) > 0;  // 존재하면 true, 아니면 false 반환
            }
        } catch (SQLException e) {
            e.printStackTrace();  // 예외 발생 시 오류 메시지 출력
        }
        return false;  // 기본적으로 false 반환
    }
    
    // 카테고리 삭제 메서드
    public int DeleteCategory(CategoryVo cate) {
        int cnt = 0;
        String sql = "DELETE FROM CATEGORY WHERE CATEGORY_ID = ?";  // 카테고리 삭제 SQL
        
        try {
            con = DBUtil.getConnection();  // 데이터베이스 연결
            ps = con.prepareStatement(sql);  // SQL 쿼리 준비
            ps.setInt(1, cate.getCategory_id());  // 카테고리 ID 바인딩
            cnt = ps.executeUpdate();  // 쿼리 실행 후 영향받은 행 수 반환
        } catch (SQLException e) {
            e.printStackTrace();  // 예외 발생 시 오류 메시지 출력
        } finally {
            disConnect();  // 연결 해제
        }
        return cnt;  // 삭제된 행 수 반환
    }
}
