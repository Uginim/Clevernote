package com.uginim.clevernote.board;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.uginim.clevernote.board.dao.BoardDAO;
import com.uginim.clevernote.board.vo.BoardVO;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/root-context.xml"})
public class BoardDAOImplXMLTest {
	private final static Logger logger
	= LoggerFactory.getLogger(BoardDAOImplXMLTest.class);
	
	@Inject
	BoardDAO boardDAO;
	
	//게시글 목록
	@Test
	@Named("게시글목록")
	@Disabled
	public void list() {
		List<BoardVO> list= boardDAO.selectAllBoards();
		assertNotNull(list);
		logger.info(list.toString());
	}
	
	//게시글 목록 (페이지,검색어 추가)
	@Test
	@Named("게시글목록(페이징)")
	@Disabled
	public void list3() {
		
//		int reqPage = 1;
//		RecordCriteria recordCriteria = new RecordCriteria(reqPage);
//		logger.info(""+recordCriteria.getStartRec());
//		logger.info(""+recordCriteria.getEndRec());
//		List<BoardVO> list = boardDAO.list( recordCriteria.getStartRec(), 
//																				recordCriteria.getEndRec(), 
//																				"TC", "코비드 제목");
//		logger.info("검색결과 :" + list.size());
//		for(BoardVO boardVO : list) {
//			logger.info(boardVO.toString());
//		}
		
	}
	
	//총레코드 카운트
	@Test
	@Disabled
	public void totalRecordCount() {
		
//		int cnt = boardDAO.totalRecordCount("TC", "코비드");
//		logger.info("총레코드수: " + cnt);
	}
}













