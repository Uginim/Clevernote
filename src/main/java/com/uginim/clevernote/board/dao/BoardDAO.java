package com.uginim.clevernote.board.dao;

import java.util.List;

import com.uginim.clevernote.board.vo.AttachmentFileVO;
import com.uginim.clevernote.board.vo.BoardTypeVO;
import com.uginim.clevernote.board.vo.BoardPostVO;

public interface BoardDAO {

	/* Create */
	// 게시글 작성
	int insertBoard(BoardPostVO board);
	
	// 첨부파일 insert
	int insertAttachmentFile(AttachmentFileVO attachment);
	
	// 답글 넣기
	int insertReplyBoard(BoardPostVO board);

	
	/* Read */
	// 게시글 타입 불러오기
	List<BoardTypeVO> getBoardTypes();
	// 게시글 하나 읽어오기
	BoardPostVO selectOne(long postNum);
	// 특정 게시글의 첨부파일 가져오기
	List<AttachmentFileVO> selectAllAttachments(long postNum);
	// 첨부파일 열기	
	AttachmentFileVO getAttachment(long attachmentNum);
	
	// 게시글 목록 가져오기
	// 1) 전체
	List<BoardPostVO> selectAllBoards();
	// 2) 검색어 게시글 검색(전체, 제목, 내용, 작성자ID, 별칭)
	List<BoardPostVO> selectBoards(long startRowNum, long endRowNum, String searchType, String keyword);	
	// 전체 게시글 수 
	long countTotalBoardRows(String searchType, String keyword);
	
	/* Update */
	// 게시글 수정
	int update(BoardPostVO boardVO);
//	//조회수 +1 증가
//	int updateHit(String bnum);
	// 첨부파일 수정
	int updateAttachmentFile(AttachmentFileVO attachment);
	/* Delete */
	// 게시글 삭제
	int delete(long postNum);
	// 첨부파일 1개 삭제
	int deleteAttachmentFile(long attachmentNum);
	
	// 첨부파일 전체 삭제
	int deleteAllAttachments(long postNum);
	
}
