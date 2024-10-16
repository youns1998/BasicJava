package VO;

import java.time.LocalDateTime;
s
public class FavoriteVo {
	private String user_id;
	private int post_id;
	private String post_title;
	 private String author;    // 작성자 추가
	private LocalDateTime created_at;
	
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public int getPost_id() {
		return post_id;
	}
	public void setPost_id(int post_id) {
		this.post_id = post_id;
	}
	
	public LocalDateTime getCreated_at() {
		return created_at;
	}
	public void setCreated_at(LocalDateTime created_at) {
		this.created_at = created_at;
	}
	
	public String getPost_title() {
	    return post_title;
	}

	public void setPost_title(String post_title) {
	    this.post_title = post_title;
	}
		
	public String getAuthor() {
	    return author;
    }

	public void setAuthor(String author) {
	    this.author = author;
	}
	
	
}
