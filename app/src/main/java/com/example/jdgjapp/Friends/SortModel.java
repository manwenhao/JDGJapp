package com.example.jdgjapp.Friends;

public class SortModel extends Contact {


	public SortModel(String id,String name, String number,String dept, String sortKey) {
		super(id,name, number,dept, sortKey);
	}

	public String sortLetters; //显示数据拼音的首字母

	public SortToken sortToken=new SortToken();
}
