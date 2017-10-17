package cn.mageek.quiz.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity // 会自动生成该table
public class Article implements Serializable{
	@Id //主键
	private Long id;
    private String title;
	private Integer age;
	private String address;

	public Article() {
		super();
	}
	public Article(Long id, String title, Integer age, String address) {
		super();
		this.id = id;
		this.title = title;
		this.age = age;
		this.address = address;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
}
