package Project;
import java.sql.*;
import java.util.Scanner;

public class LibraryManager {

    // Oracle DB 연결123
    private static Connection connect() throws SQLException {
    	
    	try {
    	    Class.forName("oracle.jdbc.driver.OracleDriver");
    	} catch (ClassNotFoundException e) {
    	    e.printStackTrace();
    	}
    	String url = "jdbc:oracle:thin:@localhost:1588/FREEPDB1";
        String user = "libraryuser";
        String password = "java";
        System.out.println("asd");
        return DriverManager.getConnection(url, user, password);
    }

    // 회원 가입 기능
    private static void signUp(Connection conn) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n=== 회원 가입 ===");
        System.out.print("사용자 이름: ");
        String username = scanner.nextLine();
        System.out.print("비밀번호: ");
        String password = scanner.nextLine();

        String insertSql = "INSERT INTO members (member_id, username, password, role_id) "
                         + "VALUES (member_seq.NEXTVAL, ?, ?, 1)";
        PreparedStatement stmt = conn.prepareStatement(insertSql);
        stmt.setString(1, username);
        stmt.setString(2, password);
        
        try {
            stmt.executeUpdate();
            System.out.println("회원 가입이 완료되었습니다!");
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("이미 존재하는 사용자 이름입니다. 다시 시도해주세요.");
        }
    }

    // 회원 로그인
    private static int login(Connection conn, String username, String password) throws SQLException {
        String sql = "SELECT member_id, role_id FROM members WHERE username = ? AND password = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, username);
        stmt.setString(2, password);
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            System.out.println("로그인 성공!");
            int roleId = rs.getInt("role_id");
            return roleId;  // 1: 일반 회원, 2: 관리자
        } else {
            System.out.println("로그인 실패! 사용자 이름 또는 비밀번호가 틀렸습니다.");
            return -1;
        }
    }

    // 도서 목록 출력 (테이블 형식으로 개선)
    private static void listBooks(Connection conn) throws SQLException {
        String sql = "SELECT book_id, title, author, genre, available FROM books";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║ 도서 ID │ 제목                      │ 저자         │ 장르     │ 대출 가능 ║");
        System.out.println("╟─────────┼───────────────────────────┼──────────────┼──────────┼──────────╢");

        while (rs.next()) {
            String available = rs.getString("available").equals("Y") ? "가능" : "대출중";
            System.out.printf("║ %-7d │ %-25s │ %-10s │ %-8s │ %-8s ║\n",
                              rs.getInt("book_id"),
                              rs.getString("title"),
                              rs.getString("author"),
                              rs.getString("genre"),
                              available);
        }
        
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
    }

    // 메인 프로그램
    public static void main(String[] args) {
        try (Connection conn = connect(); Scanner scanner = new Scanner(System.in)) {
            System.out.println("===== 도서 관리 프로그램 =====");

            // 메인 메뉴: 로그인 또는 회원 가입 선택
            while (true) {
                System.out.println("\n1. 로그인");
                System.out.println("2. 회원 가입");
                System.out.print("선택: ");
                int action = scanner.nextInt();
                scanner.nextLine();  // 버퍼 비우기
                
                if (action == 1) {
                    System.out.print("사용자 이름: ");
                    String username = scanner.nextLine();
                    System.out.print("비밀번호: ");
                    String password = scanner.nextLine();

                    int roleId = login(conn, username, password);
                    if (roleId == -1) return;  // 로그인 실패
                    
                    // 로그인 성공 후 역할에 따른 메뉴 표시
                    while (true) {
                        if (roleId == 2) {  // 관리자
                            System.out.println("\n===== 관리자 메뉴 =====");
                            System.out.println("1. 도서 목록 보기");
                            System.out.println("2. 종료");
                            System.out.print("선택: ");
                            int choice = scanner.nextInt();

                            if (choice == 1) {
                                listBooks(conn);
                            } else if (choice == 2) {
                                System.out.println("프로그램을 종료합니다.");
                                break;
                            }
                        } else if (roleId == 1) {  // 일반 회원
                            System.out.println("\n===== 회원 메뉴 =====");
                            System.out.println("1. 도서 목록 보기");
                            System.out.println("2. 종료");
                            System.out.print("선택: ");
                            int choice = scanner.nextInt();

                            if (choice == 1) {
                                listBooks(conn);
                            } else if (choice == 2) {
                                System.out.println("프로그램을 종료합니다.");
                                break;
                            }
                        }
                    }
                } else if (action == 2) {
                    signUp(conn);  // 회원 가입
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
