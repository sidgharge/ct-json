package com.homeprojects.ct.ctjson.test;

import java.util.List;

import com.homeprojects.ct.ctjson.annotations.JsonDeserialize;

@JsonDeserialize
public class User {

	private int age;

	private String name;

	private boolean isActive;

//	private List<Integer> favNums;

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	@Override
	public String toString() {
		return "User [age=" + age + ", name=" + name + ", isActive=" + isActive + "]";
	}

//	public List<Integer> getFavNums() {
//		return favNums;
//	}
//
//	public void setFavNums(List<Integer> favNums) {
//		this.favNums = favNums;
//	}
//
//	@Override
//	public String toString() {
//		return "User [age=" + age + ", name=" + name + ", isActive=" + isActive + ", favNums=" + favNums + "]";
//	}

	
	
}
