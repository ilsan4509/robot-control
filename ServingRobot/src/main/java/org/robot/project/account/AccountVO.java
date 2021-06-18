package org.robot.project.account;

public class AccountVO {
	int user_seq;
	String user_id;
	String user_pw;
	String user_role;
	String user_name;
	String user_gender;
	String user_call;
	String user_age;
	String user_mail;
	int store_seq;
	String store_name;
	String user_enter_date;
	public int getUser_seq() {
		return user_seq;
	}
	public void setUser_seq(int user_seq) {
		this.user_seq = user_seq;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getUser_pw() {
		return user_pw;
	}
	public void setUser_pw(String user_pw) {
		this.user_pw = user_pw;
	}
	public String getUser_role() {
		return user_role;
	}
	public void setUser_role(String user_role) {
		this.user_role = user_role;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getUser_gender() {
		return user_gender;
	}
	public void setUser_gender(String user_gender) {
		this.user_gender = user_gender;
	}
	public String getUser_call() {
		return user_call;
	}
	public void setUser_call(String user_call) {
		this.user_call = user_call;
	}
	public String getUser_age() {
		return user_age;
	}
	public void setUser_age(String user_age) {
		this.user_age = user_age;
	}
	public String getUser_mail() {
		return user_mail;
	}
	public void setUser_mail(String user_mail) {
		this.user_mail = user_mail;
	}
	public int getStore_seq() {
		return store_seq;
	}
	public void setStore_seq(int store_seq) {
		this.store_seq = store_seq;
	}
	public String getStore_name() {
		return store_name;
	}
	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}
	public String getUser_enter_date() {
		return user_enter_date;
	}
	public void setUser_enter_date(String user_enter_date) {
		this.user_enter_date = user_enter_date;
	}
	@Override
	public String toString() {
		return "AccountVO [user_seq=" + user_seq + ", user_id=" + user_id + ", user_pw=" + user_pw + ", user_role=" + user_role + ", user_name=" + user_name + ", user_gender=" + user_gender + ", user_call=" + user_call + ", user_age=" + user_age + ", user_mail=" + user_mail + ", store_seq="
				+ store_seq + ", store_name=" + store_name + ", user_enter_date=" + user_enter_date + "]";
	}
}
