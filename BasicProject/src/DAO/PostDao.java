package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import UTIL.DBUtil;
import UTIL.ScanUtil;
import VO.CommentsVo;
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

    // 싱글톤 패턴을 사용한 인스턴스 생성
    public static PostDao getInstance() {
        if (instance == null)
            instance = new PostDao();
        return instance;
    }

    // 데이터베이스 연결 해제 메서드
    private void disConnect() {
        if (rs != null)
            try { rs.close(); } catch (Exception e) {}
        if (ps != null)
            try { ps.close(); } catch (Exception e) {}
        if (con != null)
            try { con.close(); } catch (Exception e) {}
    }

    // 게시글 추가 메서드
    public int insertPost(PostVo PostVo) {
        int cnt = 0;
        String sql = "INSERT INTO POST (POST_ID, USER_ID, PRICE, CATEGORY_ID, TITLE, CONTENT, CONDITION, CREATED_AT, UPDATED_AT) "
                   + "VALUES( (SELECT NVL(MAX(POST_ID), 0) + 1 FROM POST), ?, ?, ?, ?, ?, ?, SYSDATE, SYSDATE)";

        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, PostVo.getUser_id());
            ps.setInt(2, PostVo.getPrice());
            ps.setInt(3, PostVo.getCategory_id());
            ps.setString(4, PostVo.getTitle());
            ps.setString(5, PostVo.getContent());
            ps.setInt(6, PostVo.getCondition());
            cnt = ps.executeUpdate();
            
            PostVo.setCreated_at(LocalDateTime.now());
            PostVo.setUpdated_at(LocalDateTime.now());

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disConnect();
        }
        return cnt;
    }

    // 게시글 전체 조회 메서드
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

    // 내가 쓴 글 전체 조회 메서드
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

    // 게시글 상세 조회 메서드 (POST ID로 조회)
    public PostVo getPost(int post_id) {
        PostVo postvo = new PostVo();
        String sql = "SELECT *FROM POST WHERE POST_ID = ?";

        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, post_id);
            rs = ps.executeQuery();

            if (rs.next()) {
                postvo.setCreated_at(rs.getTimestamp("CREATED_AT").toLocalDateTime());
                postvo.setUpdated_at(rs.getTimestamp("UPDATED_AT").toLocalDateTime());
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

    // 게시글 상세 조회 메서드 (USER_ID로 조회)
    public PostVo getPostuser(String userid) {
        PostVo postvo = new PostVo();
        String sql = "SELECT *FROM POST WHERE USER_ID = ?";

        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, userid);
            rs = ps.executeQuery();

            if (rs.next()) {
                postvo.setCreated_at(rs.getTimestamp("CREATED_AT").toLocalDateTime());
                postvo.setUpdated_at(rs.getTimestamp("UPDATED_AT").toLocalDateTime());
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

    // 게시글 수정 메서드
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

    // 게시글 수정 선택 메서드
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
                    int newCondition = ScanUtil.nextInt("변경할 거래 상태를 입력하세요 \n 1.판매중\t2.예약중\t3.판매완료\n선택 >> ");
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

    // 게시글 삭제 메서드
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

    // 특정 게시물의 상태를 확인하고 거래 완료 시 거래 내역 추가 메서드
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

    // 게시물 상태 업데이트 메서드
    public static void updatePostCondition(int postId, int newCondition, String buyerId, String sellerId, Integer commentId) {
        String checkTransactionSql = "SELECT COUNT(*) FROM TRANSACTION WHERE POST_ID = ?";
        String updateTransactionSql = "UPDATE TRANSACTION SET TRANSACTION_STATUS = ?, BUYER_ID = ? WHERE POST_ID = ?";
        String insertTransactionSql = "INSERT INTO TRANSACTION (TRANSACTION_ID, BUYER_ID, SELLER_ID, POST_ID, TRANSACTION_DATE, TRANSACTION_STATUS) "
                                    + "VALUES ((SELECT NVL(MAX(TRANSACTION_ID), 0) + 1 FROM TRANSACTION), ?, ?, ?, SYSDATE, ?)";

        try (Connection con = DBUtil.getConnection(); 
             PreparedStatement psCheck = con.prepareStatement(checkTransactionSql);
             PreparedStatement psUpdate = con.prepareStatement(updateTransactionSql);
             PreparedStatement psInsert = con.prepareStatement(insertTransactionSql)) {

            // 기존 트랜잭션 내역이 있는지 확인
            psCheck.setInt(1, postId);
            ResultSet rs = psCheck.executeQuery();
            rs.next();
            int transactionCount = rs.getInt(1);

            // 게시물 상태 업데이트 (POST 테이블)
            updatePostStatus(postId, newCondition);

            if (transactionCount > 0) {
                // 기존 거래가 있는 경우
                if (newCondition == 3) {  // 거래 완료 시 구매자 정보 유지
                    PreparedStatement psGetBuyer = con.prepareStatement("SELECT BUYER_ID FROM TRANSACTION WHERE POST_ID = ?");
                    psGetBuyer.setInt(1, postId);
                    ResultSet buyerResult = psGetBuyer.executeQuery();
                    if (buyerResult.next()) {
                        buyerId = buyerResult.getString("BUYER_ID");  // 기존 구매자 ID 사용
                    }
                }
                psUpdate.setInt(1, newCondition);
                psUpdate.setString(2, buyerId); // 예약/완료 중인 경우 구매자 정보 업데이트
                psUpdate.setInt(3, postId);
                psUpdate.executeUpdate();
                System.out.println("거래 상태가 업데이트되었습니다.");
            } else {
                // 거래 내역이 없으면 새로 추가
                psInsert.setString(1, buyerId);
                psInsert.setString(2, sellerId);
                psInsert.setInt(3, postId);
                psInsert.setInt(4, newCondition);
                psInsert.executeUpdate();
                System.out.println("새로운 거래 내역이 추가되었습니다.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 게시물 상태 업데이트 로직
    private static void updatePostStatus(int postId, int newCondition) throws SQLException {
        String sql = "UPDATE POST SET CONDITION = ? WHERE POST_ID = ?";
        try (Connection con = DBUtil.getConnection(); PreparedStatement psPost = con.prepareStatement(sql)) {
            psPost.setInt(1, newCondition);
            psPost.setInt(2, postId);
            psPost.executeUpdate();
        }
    }

    // 게시글 검색 메서드 (거래 완료된 상품 제외)
    public List<PostVo> searchPosts(String keyword, Integer categoryId) {
        List<PostVo> postlist = new ArrayList<PostVo>();
        StringBuilder sql = new StringBuilder("SELECT * FROM POST WHERE (TITLE LIKE ? OR CONTENT LIKE ? OR CATEGORY_ID LIKE ?) AND CONDITION <> ?");

        if (categoryId != null) {
            sql.append(" AND CATEGORY_ID = ?");
        }
        sql.append(" ORDER BY CREATED_AT");
        try {
            con = DBUtil.getConnection();
            ps = con.prepareStatement(sql.toString());
            ps.setString(1, "%" + keyword + "%"); // 제목 검색
            ps.setString(2, "%" + keyword + "%"); // 내용 검색
            ps.setString(3, "%" + categoryId + "%"); // 내용 검색
            ps.setInt(4, CONDITION_SOLD_OUT); // 거래 완료 상태 제외
            
            if (categoryId != null) {
                ps.setInt(5, categoryId); // 카테고리 검색
            }
            
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
