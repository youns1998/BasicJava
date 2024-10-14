package VO;

public class PostVo {
	
	private int post_id;
	private String title;
	private String content;
	private String user_id;
	private String reg_date;
	
	private String user_name;

	public int getpost_id() {
		return post_id;
	}

	public void setpost_id(int post_id) {
		this.post_id = post_id;
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

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getReg_date() {
		return reg_date;
	}

	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	@Override
	public String toString() {
		return "BoardVO [post_id=" + post_id + ", title=" + title + ", content=" + content + ", user_id=" + user_id
				+ ", reg_date=" + reg_date + ", user_name=" + user_name + "]";
	}
	
}
