package com.uginim.clevernote.board.util;

import lombok.Data;

@Data
public class SearchedRowCriteria extends RowCriteria{
	
	private String searchType;
	private String keyword;
	
	public SearchedRowCriteria(int curPage) {
		super(curPage);
	}
	
	public SearchedRowCriteria(int curPage, String searchType, String keyword) {
		this(curPage);
		this.searchType = searchType;
		this.keyword = keyword;
	}
	
}
