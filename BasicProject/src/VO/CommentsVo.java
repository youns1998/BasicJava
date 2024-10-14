package VO;

import java.time.LocalDateTime;

public class CommentsVo {
	private int comment_id;
	private int post_id;
	private String user_id;
	private String content;
	private LocalDateTime created_at;
	
	public int getComment_id() {
		return comment_id;
	}
	public void setComment_id(int comment_id) {
		this.comment_id = comment_id;
	}
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public LocalDateTime getCreated_at() {
		return created_at;
	}
	public void setCreated_at(LocalDateTime created_at) {
		this.created_at = created_at;
	}
	
	@Override
	public String toString() {
		return "CommentsVo [comment_id=" + comment_id + ", post_id=" + post_id + ", user_id=" + user_id + ", content="
				+ content + ", created_at=" + created_at + "]";
	}
	
}
