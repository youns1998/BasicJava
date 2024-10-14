package VO;

import java.sql.Date;

public class HistoryVo {
	private int transaction_id;
	private String buyer_id;
	private String seller_id;
	private int post_id;
	private Date transaction_date;
	
	public int getTransaction_id() {
		return transaction_id;
	}
	public void setTransaction_id(int transaction_id) {
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
	public void setTransaction_date(Date transaction_date) {
		this.transaction_date = transaction_date;
	}
	
	@Override
	public String toString() {
		return "HistoryVo [transaction_id=" + transaction_id + ", buyer_id=" + buyer_id + ", seller_id=" + seller_id
				+ ", post_id=" + post_id + ", transaction_date=" + transaction_date + "]";
	}
	
	
}
