package VO;

import java.time.LocalDateTime;

public class UsersVo {
	private String user_id;
	private String user_pass;
	private String email;
	private String username;
	private String phone_number;
	private String address;
	private int role;
	private LocalDateTime created_at;


	
	
	public UsersVo(String userId, String name, String email) {
        this.user_id = userId;
        this.username = name;
        this.email = email;
    }
	public UsersVo(String userId, String name, String email, String userpass) {
		this.user_id = userId;
		this.username = name;
		this.email = email;
		this.user_pass = userpass;
	}
	

	
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public int getRole() {
		return role;
	}
	public void setRole(int Role) {
		this.role = Role;
	}
	public String getUser_pass() {
		return user_pass;
	}
	public void setUser_pass(String user_pass) {
		this.user_pass = user_pass;
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
	public LocalDateTime getCreated_at() {
		return created_at;
	}
	public void setCreated_at(LocalDateTime created_at) {
		this.created_at = created_at;
	}
	@Override
	public String toString() {
		return "UsersVo [user_id=" + user_id + ", user_pass=" + user_pass + ", email=" + email + ", username="
				+ username + ", phone_number=" + phone_number + ", address=" + address + ", created_at=" + created_at
				+ "]";
	}
	
	public UsersVo() {
	
	};

	public UsersVo(String user_id, String user_pass, String username, String address, String email, String phone_number) {
        this.user_id = user_id;
        this.user_pass = user_pass;
        this.username = username;
        this.address = address;
        this.email = email;
        this.phone_number = phone_number;
    }

    // 필요한 경우 role과 created_at까지 포함하는 생성자
    public UsersVo(String user_id, String user_pass, String username, String address, String email, String phone_number, int role, LocalDateTime created_at) {
        this.user_id = user_id;
        this.user_pass = user_pass;
        this.username = username;
        this.address = address;
        this.email = email;
        this.phone_number = phone_number;
        this.role = role;
        this.created_at = created_at;
    }
    public UsersVo(String user_id, String user_pass) {
        this.user_id = user_id;
        this.user_pass = user_pass;
    }
	
}
