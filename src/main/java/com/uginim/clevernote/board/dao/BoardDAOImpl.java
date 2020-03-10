package com.uginim.clevernote.board.dao;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.uginim.clevernote.board.vo.AttachmentFileVO;
import com.uginim.clevernote.board.vo.BoardTypeVO;
import com.uginim.clevernote.board.vo.BoardPostVO;

@Repository
public class BoardDAOImpl implements BoardDAO {

	private static final Logger logger = LoggerFactory.getLogger(BoardDAOImpl.class);
	
	
	@Inject
	SqlSessionTemplate sqlSession;
	
	/* Create */
	// 게시글 작성
	@Override
	public int insertBoard(BoardPostVO board) {
		return sqlSession.insert("mappers.BoardDAO-mapper.insertBoard", board);
	}
	
	// 첨부파일 insert
	@Override
	public int insertAttachmentFile(AttachmentFileVO attachment) {
		return sqlSession.insert("mappers.BoardDAO-mapper.insertAttachmentFile", attachment);
	}

	// 답글 넣기
	@Transactional
	@Override
	public int insertReplyBoard(BoardPostVO board) {
		//1) 이전답글 step업데이트
		updateStep(board.getPostGroup(),board.getStep());
		//2) 답글달기
		return sqlSession.insert("mappers.BoardDAO-mapper.insertReplyBoard", board);	
	}
	//이전 답글 step 업데이트
	private int updateStep(long postGroup, int step) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("postGroup", postGroup);
		map.put("step",step);
		return sqlSession.update("mappers.BoardDAO-mapper.updateStep",map);
	}
	
	/* Read */
	// 게시글 타입 불러오기
	@Override
	public List<BoardTypeVO> getBoardTypes() {
		return sqlSession.selectList("mappers.BoardDAO-mapper.getBoardTypes");
	}
	
	// 게시글 하나 읽어오기
	@Override
	public BoardPostVO selectOne(long postNum) {
		return sqlSession.selectOne("mappers.BoardDAO-mapper.selectOne", postNum);
	}
	
	// 특정 게시글의 첨부파일 가져오기
	@Override
	public List<AttachmentFileVO> selectAllAttachments(long postNum) {
		return sqlSession.selectList("mappers.BoardDAO-mapper.selectAllAttachments",postNum);

	}

	// 첨부파일 열기	
	@Override
	public AttachmentFileVO getAttachment(long attachmentNum) {
		return sqlSession.selectOne("mappers.BoardDAO-mapper.getAttachment", attachmentNum);
	}

	// 게시글 목록 가져오기
		// 1) 전체
	@Override
	public List<BoardPostVO> selectAllBoards() {
		return sqlSession.selectList("mappers.BoardDAO-mapper.selectAllBoards");
	}
	// 2) 검색어 게시글 검색(전체, 제목, 내용, 작성자ID, 별칭)

	@Override
	public List<BoardPostVO> selectBoards(long startRowNum, long endRowNum, String searchType, String keyword) {
		Map<String,Object> map = new HashMap<>();
		map.put("startRowNum", startRowNum);
		map.put("endRowNum", endRowNum);
		map.put("searchType",searchType);
		if(keyword != null) {
			map.put("list",Arrays.asList(keyword.split("\\s+")));
		}
		map.put("andor","or");
		return sqlSession.selectList("mappers.BoardDAO-mapper.selectBoards", map);
	}
	
	// 전체 게시글 수
	@Override
	public long countTotalBoardRows(String searchType, String keyword) {
		logger.info("keyword :" + keyword);
		Map<String,Object> map = new HashMap<>();
		map.put("searchType",searchType);
		
		if(keyword != null) {
			map.put("list",Arrays.asList(keyword.split("\\s+")));
		}
		map.put("andor","or");
		logger.info("keyword2 :" + map.get("list"));
		return sqlSession.selectOne("mappers.BoardDAO-mapper.countTotalBoardRows",map);
	}

	/* Update */
	// 게시글 수정
	@Override
	public int update(BoardPostVO boardVO) {
		return sqlSession.update("mappers.BoardDAO-mapper.update", boardVO );
	}
	// 첨부파일 수정
	@Override
	public int updateAttachmentFile(AttachmentFileVO attachment) {
		return sqlSession.update("mappers.BoardDAO-mapper.updateAttachmentFile",attachment);
	}

	/* Delete */
	// 게시글 삭제
	@Override
	public int delete(long postNum) {
		return sqlSession.delete("mappers.BoardDAO-mapper.delete", postNum );
	}

	// 첨부파일 1개 삭제
	@Override
	public int deleteAttachmentFile(long attachmentNum) {
		return sqlSession.delete("mappers.BoardDAO-mapper.deleteAttachmentFile", attachmentNum );
	}

	// 첨부파일 전체 삭제
	@Override
	public int deleteAllAttachments(long postNum) {
		return sqlSession.delete("mappers.BoardDAO-mapper.deleteAllAttachments", postNum );
	}

}
