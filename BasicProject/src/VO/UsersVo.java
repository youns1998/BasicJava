package VO;

public class UsersVo {
	private String user_id;
	private String email;
	private String username;
	private String phone_number;
	private String address;
	private String created_at;
	
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPhone_number() {
		return phone_number;
	}
	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCreated_at() {
		return created_at;
	}
	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
	
	@Override
	public String toString() {
		return "UsersVo [user_id=" + user_id + ", email=" + email + ", username=" + username + ", phone_number="
				+ phone_number + ", address=" + address + ", created_at=" + created_at + "]";
	}
	
	
}
