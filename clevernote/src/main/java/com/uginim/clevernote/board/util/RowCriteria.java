package com.uginim.clevernote.board.util;

/**
 * 게시글 페이지에서 처음행과 끝행를 구함
 * @author Hyeonuk
 *
 */
public class RowCriteria {
	private int currentPage;		// 현재 페이지
	private int rowsPerPage;     // 한페이지에 보여줄 행 수
	
	private final int DEFAULT_ROW_PER_PAGE = 10;
	
	// 생성자
	public RowCriteria(int curPage) {
		this.currentPage = curPage;
		this.rowsPerPage = DEFAULT_ROW_PER_PAGE;
	}

	/* getters & setters */
	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		currentPage = (currentPage < 1 ) ? 1 : currentPage; 
		this.currentPage = currentPage;
	}

	public int getRowsPerPage() {
		return rowsPerPage;
	}

	public void setRowsPerPage(int rowPerPage) {
		this.rowsPerPage = rowPerPage;
	}
	
	// 시작 row
	// (현재 페이지 -1) * "한 페이지에 보여줄 row 수" + 1
	public long getStartRowNum() {
		return (getCurrentPage() - 1 ) * getRowsPerPage() + 1;
	}
	
	// 종료 row
	// 현재 페이지 * "한 페이지에 보여줄 row 수"
	public long getEndRowNum() {
		return getCurrentPage() * getRowsPerPage();
	}

	@Override
	public String toString() {
		return "RowCriteria [currentPage=" + currentPage + ", rowsPerPage=" + rowsPerPage + ", DEFAULT_ROW_PER_PAGE="
				+ DEFAULT_ROW_PER_PAGE + ", getStartRowNum()=" + getStartRowNum() + ", getEndRowNum()=" + getEndRowNum()
				+ "]";
	}
	
	
}
