package VO;

import java.sql.Date;

public class HistoryVo {
    private String transaction_id;  // VARCHAR2는 Java에서 String으로 처리
    private String buyer_id;
    private String seller_id;
    private int post_id;
    private Date transaction_date;
    private int transaction_status;  // 거래 상태는 int

    // 기본 생성자
    public HistoryVo() {
    }

    // 모든 필드를 포함하는 생성자
    public HistoryVo(String transactionId, String buyerId, String sellerId, int postId, Date transactionDate, int transactionStatus) {
        this.transaction_id = transactionId;
        this.buyer_id = buyerId;
        this.seller_id = sellerId;
        this.post_id = postId;
        this.transaction_date = transactionDate;
        this.transaction_status = transactionStatus;
    }

    // Getter와 Setter

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getBuyer_id() {
        return buyer_id;
    }

    public void setBuyer_id(String buyer_id) {
        this.buyer_id = buyer_id;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public Date getTransaction_date() {
        return transaction_date;
    }

    public void setTransaction_date(java.util.Date date) {
        this.transaction_date = new Date(date.getTime());
    }

    public int getTransaction_status() {
        return transaction_status;
    }

    public void setTransaction_status(int transaction_status) {
        this.transaction_status = transaction_status;
    }

    @Override
    public String toString() {
        return "HistoryVo [transaction_id=" + transaction_id + ", buyer_id=" + buyer_id + ", seller_id=" + seller_id
                + ", post_id=" + post_id + ", transaction_date=" + transaction_date + ", transaction_status=" + transaction_status + "]";
    }
}
