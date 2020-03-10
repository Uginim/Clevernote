package com.uginim.clevernote.board.util;

public class PageManager{
	private int pagesNumOnViewPage;	// 페이지에 표시 될 페이지 링크 수
	private int startPageNum;		// 현 페이지에서 표시될 시작 페이지
	private int endPageNum;			// 현 페이지에서 표시될 끝 페이지번호
	
	
	private long totalRowsNum;		// 전체 행 게수
	private int finalEndPage;		// 최종 페이지
	
//	private boolean prev; 			// 이전 페이지
//	private boolean next;			// 다음페이지
	
	private RowCriteria rowCreteria;	// 한페이지에 보여줄 레코드 수, 찾기
	
	

	private final int DEFAULT_PAGES_NUM_ON_VIEW_PAGE = 10;
	
	public PageManager(RowCriteria rowCriteria, long totalRowsNum) {
		this.rowCreteria = rowCriteria;
		this.totalRowsNum = totalRowsNum;
		this.pagesNumOnViewPage = DEFAULT_PAGES_NUM_ON_VIEW_PAGE;
		init();
	}
	
	private void init() {
		// 현재페이지의 끝 페이지 번호 =
		// ceil("현재페이지"/"한페이지에 표시될 페이지링크 수") * "한페이지에 표시될 페이지링크 수"
		double currentPage = getRowCreteria().getCurrentPage();
		int pagesNumOnViewPage = getPagesNumOnViewPage();
		int endPageNum 
			= (int) (
					Math.ceil(currentPage/ pagesNumOnViewPage) *  
					pagesNumOnViewPage
					);
		setEndPageNum(endPageNum);
		
		// 현 페이지의 시작 페이지 번호 = 
		// "한 페이지의 끝 페이지" - * "한페이지에 표시될 페이지링크 수" + 1
		int startPageNum 
			= endPageNum - pagesNumOnViewPage +1;
		setStartPageNum(startPageNum);
		
		// 최종 페이지 번호 = 
		// ceil("전체 row 수" / "한페이지에 표시될 페이지링크 수")
		int rowsPerPage = getRowCreteria().getRowsPerPage();
		int finalEndPage = (int)Math.ceil( (double)this.totalRowsNum / rowsPerPage );
		setFinalEndPage(finalEndPage);
		if(finalEndPage < endPageNum) {
			setEndPageNum(finalEndPage);
		}
		
	}
	
	/* getters &  setters */	
	public int getStartPageNum() {
		return startPageNum;
	}
	private void setStartPageNum(int startPageNum) {
		this.startPageNum = startPageNum;
	}	
	public int getEndPageNum() {
		return endPageNum;
	}
	private void setEndPageNum(int endPageNum) {
		this.endPageNum = endPageNum;
	}
	public RowCriteria getRowCreteria() {
		return rowCreteria;
	}
	public RowCriteria getRc() {
		return getRowCreteria();
	}
	public void setRowCreteria(RowCriteria rowCreteria) {
		this.rowCreteria = rowCreteria;
	}

	public int getPagesNumOnViewPage() {
		return pagesNumOnViewPage;
	}

	public int getFinalEndPage() {
		return finalEndPage;
	}

	private void setFinalEndPage(int finalEndPage) {
		this.finalEndPage = finalEndPage;
	}
	
	public long getTotalRowsNum() {
		return totalRowsNum;
	}

	public void setTotalRowsNum(long totalRowsNum) {
		this.totalRowsNum = totalRowsNum;
	}

	// 이전 페이지가 있는 지 판단
	public boolean isPrev() {
		return getStartPageNum() == 1 ? false : true;
	}
	// 다음 페이지가 있는 지 판단
	public boolean isNext() {
		long totalRowsNum = getTotalRowsNum() ;
		int rowsPerPage = getRowCreteria().getRowsPerPage();
		return totalRowsNum > getEndPageNum() * rowsPerPage ? true: false;
	}

	//getter() : FindCriteria
	public SearchedRowCriteria getSearchedRowCriteria() {
		
		if(rowCreteria instanceof SearchedRowCriteria) {
			return (SearchedRowCriteria)rowCreteria; 
		}
		
		return null;
	} 
	public SearchedRowCriteria getSrc() {
		return getSearchedRowCriteria();
	}
	
	
	@Override
	public String toString() {
		return "PageManager [pagesNumOnViewPage=" + pagesNumOnViewPage + ", startPageNum=" + startPageNum
				+ ", endPageNum=" + endPageNum + ", totalRowsNum=" + totalRowsNum + ", finalEndPage=" + finalEndPage
				+ ", rowCreteria=" + rowCreteria + ", DEFAULT_PAGES_NUM_ON_VIEW_PAGE=" + DEFAULT_PAGES_NUM_ON_VIEW_PAGE
				+ ", hasPrev()=" + isPrev() + ", hasNext()=" + isNext() + "]";
	}
	
	
}
