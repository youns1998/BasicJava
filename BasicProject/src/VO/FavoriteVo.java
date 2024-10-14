package VO;

public class FavoriteVo {
	private String user_id;
	private int post_id;
	private String created_at;
	
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
	public String getCreated_at() {
		return created_at;
	}
	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
	
	@Override
	public String toString() {
		return "FavoriteVo [user_id=" + user_id + ", post_id=" + post_id + ", created_at=" + created_at + "]";
	}
	
}
