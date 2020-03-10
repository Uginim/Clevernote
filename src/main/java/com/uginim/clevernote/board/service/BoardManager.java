package com.uginim.clevernote.board.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.uginim.clevernote.board.dao.BoardDAO;
import com.uginim.clevernote.board.util.PageManager;
import com.uginim.clevernote.board.util.RowCriteria;
import com.uginim.clevernote.board.util.SearchedRowCriteria;
import com.uginim.clevernote.board.vo.AttachmentFileVO;
import com.uginim.clevernote.board.vo.BoardTypeVO;
import com.uginim.clevernote.board.vo.BoardVO;

@Service
public class BoardManager implements BoardService {
	public static final Logger logger= LoggerFactory.getLogger(BoardManager.class);
	
	@Inject
	BoardDAO boardDAO;
	
	// 게시판 분류 읽어오기
	@Override
	public List<BoardTypeVO> getAllBoardTypes() {
		return boardDAO.getBoardTypes();
	}

	// 게시글 작성
	@Override
	@Transactional
	public int write(BoardVO board) {
		// 1) 게시글 insert
		int state = boardDAO.insertBoard(board);
		
		// 2) 첨부파일
		logger.info("첨부파일의 수:"+board.getFiles().size());
		if(hasFile(board)) {
			writeFiles(board.getFiles(), board.getBoardNum());
		}
		return state;
	}
	
	// 첨부파일 저장하기
	private void writeFiles(List<MultipartFile> files, long boardNum) {
		AttachmentFileVO attachment = new AttachmentFileVO();
		for(MultipartFile file : files) {			
			logger.info("파일 첨부: " + file.getOriginalFilename() + " size:" + file.getSize());
			if(file.getSize()>0) {
				// 게시글 번호
				attachment.setBoardNum(boardNum);
				// 첨부파일 명
				attachment.setName(file.getOriginalFilename());
				// 첨부파일 크기
				attachment.setFileSize(file.getSize());
				// 첨부파일 타입
				attachment.setMimetype(file.getContentType());
				// 첨부파일
				try {
					attachment.setData(file.getBytes());
					boardDAO.insertAttachmentFile(attachment);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	// 게시글 수정
	@Override
	@Transactional
	public int modify(BoardVO board) {
		// 1) 게시글 수정
		int state = boardDAO.update(board);
		// 2) 첨부파일 추가
		if(hasFile(board)) {
			writeFiles(board.getFiles(), board.getBoardNum());
		}
		return state;
	}
	// 파일을 소유 여부 
	private boolean hasFile(BoardVO board) {
		return board.getFiles() !=null && board.getFiles().size() > 0;
	}

	// 게시글 삭제
	@Override
	@Transactional
	public int delete(long boardNum) {
		boardDAO.deleteAllAttachments(boardNum);
		return boardDAO.delete(boardNum) ;
		
	}

	// 첨부파일 1개 삭제
	@Override
	public int deleteAttachment(long attachmentNum) {
		return boardDAO.deleteAttachmentFile(attachmentNum);
	}
	
	// 첨부파일 1개 수정
	@Override
	public int modifyAttachment(AttachmentFileVO attachment) {
		return boardDAO.updateAttachmentFile(attachment);
	}

	// 게시글 읽기
	@Override
	@Transactional
	public Map<String, Object> view(long boardNum) {
		// 1) 게시글 가져오기
		BoardVO board = boardDAO.selectOne(boardNum);
		// 2) 첨부파일 가져오기
		List<AttachmentFileVO> files = boardDAO.selectAllAttachments(boardNum);
		// 3) 조회수 + 1 증가
		board.setHit(board.getHit()+1);
		boardDAO.update(board);
		
		Map<String,Object> map = new HashMap<>();
		map.put("board",board);
		if(files != null && files.size() > 0) {
			map.put("files",files);
		}
		return map;
	}

	// 게시글 목록
	@Override
	public List<BoardVO> list(int curPage, String searchType, String keyword) {
		RowCriteria rowCriteria = new RowCriteria(curPage);
		return boardDAO.selectBoards(
				rowCriteria.getStartRowNum(), 
				rowCriteria.getEndRowNum(), 
				searchType, 
				keyword);
	}

	// 페이지 관리자 만들기
	@Override
	public PageManager buildPageCriteria(int curPage, String searchType, String keyword) {
		PageManager pageManager = null;
		SearchedRowCriteria searchedRowCriteria = null;
		long totalBoardRows = boardDAO.countTotalBoardRows(searchType, keyword);
		
		searchedRowCriteria = new SearchedRowCriteria(curPage, searchType, keyword);
		pageManager = new PageManager(searchedRowCriteria, totalBoardRows);
		
		logger.info("pageManager:"+pageManager.toString());
		return pageManager;
	}

	// 게시글 답글 작성
	@Override
	@Transactional
	public int reply(BoardVO replyBoard) {
		// 1) 게시글 답글 작성
		logger.info("before boardNum :"+replyBoard.getBoardNum());
		int state = boardDAO.insertReplyBoard(replyBoard);
		logger.info("after boardNum :"+replyBoard.getBoardNum());
		// 3) 첨부파일 있는 경우
		logger.info("첨부개수"+replyBoard.getFiles().size());
		if(hasFile(replyBoard)) {
			writeFiles(replyBoard.getFiles(), replyBoard.getBoardNum());
		}
		return state;
	}

	// 첨부파일 가져오기
	@Override
	public AttachmentFileVO viewFile(long attachmentNum) {
		return boardDAO.getAttachment(attachmentNum);
	}

}
