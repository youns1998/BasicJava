package VO;

import java.time.LocalDateTime;

public class PostVo {
	    
	private int post_id;
	private String user_id;
	private String username;
	private int category_id;
	private String title;
	private String content;
	private int price;
	private int condition;
	private int role;
	public int getRole() {
		return role;
	}
	public void setRole(int role) {
		this.role = role;
	}

	private LocalDateTime created_at; 
	private LocalDateTime updated_at;
	
	public int getPost_id() {
		return post_id;
	}
	public void setPost_id(int post_id) {
		this.post_id = post_id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getCategory_id() {
		return category_id;
	}
	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getCondition() {
		return condition;
	}
	public void setCondition(int condition) {
		
		this.condition = condition;
	}
	public LocalDateTime getCreated_at() {
		return created_at;
	}
	public void setCreated_at(LocalDateTime created_at) {
		this.created_at = created_at;
	}
	public LocalDateTime getUpdated_at() {
		return updated_at;
	}
	public void setUpdated_at(LocalDateTime updated_at) {
		this.updated_at = updated_at;
	}
	
	@Override
	public String toString() {
		return "PostVo [post_id=" + post_id + ", user_id=" + user_id + ", username=" + username + ", category_id="
				+ category_id + ", title=" + title + ", content=" + content + ", price=" + price + ", condition="
				+ condition + ", created_at=" + created_at + ", updated_at=" + updated_at + "]";
	}
	
	
	
}


	