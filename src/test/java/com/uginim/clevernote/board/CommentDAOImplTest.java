package com.uginim.clevernote.board;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import com.uginim.clevernote.board.dao.CommentDAO;
import com.uginim.clevernote.board.vo.BoardCommentVO;
import com.uginim.clevernote.board.vo.VoteVO;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "file:src/main/webapp/WEB-INF/spring/root-context.xml")
public class CommentDAOImplTest {
	public static final Logger logger = LoggerFactory.getLogger(CommentDAOImplTest.class);
	@Inject
	CommentDAO commentDAO;
	
	@Transactional // 이거 붙으니까 테스트 후에 되돌아옴... 대박인데?
	@Test
	@DisplayName("댓글과 답댓글 삽입 확인")
	public void insertComment() {
		BoardCommentVO comment = new BoardCommentVO();
		comment.setContent("새 댓글1");
		comment.setPostNum(81);
		comment.setUserNum(397);
		comment.setUsername("유저네임");		
		commentDAO.insert(comment);
		logger.info(comment.toString());
		BoardCommentVO comment2 = new BoardCommentVO();
		comment2.setContent("새 댓글2");
		comment2.setPostNum(81);
		comment2.setUserNum(397);
		comment2.setUsername("유저네임");		
		comment2.setParentNum(comment.getCommentNum());		
		comment2.setTargetNum(comment.getCommentNum());		
		commentDAO.insert(comment2);
		logger.info(comment2.toString());
	}
	
	@Test
	@DisplayName("댓글 리스트 가져오기")
	public void selectComment() {
		logger.info("댓글 리스트 가져오기");
//		댓글 개수 늘려서 테스트 해보기 
//		댓글 리스트 가져오기
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2019);
		cal.set(Calendar.MONTH, Calendar.MARCH);
		cal.set(Calendar.DAY_OF_MONTH, 12);
		cal.set(Calendar.HOUR, 2);
		cal.set(Calendar.MINUTE, 45);
		cal.set(Calendar.SECOND, 12);
		Date lastCommentTime = cal.getTime();		
		logger.info("lastCommentTime:"+lastCommentTime );
		List<BoardCommentVO> comments = commentDAO.selectRootCommentList(81, lastCommentTime, 3);
		logger.info("comments:"+comments.toString());
		for(BoardCommentVO comment:comments) {
			List<BoardCommentVO> children = commentDAO.selectChildCommentList(comment.getCommentNum(), lastCommentTime, 3);
			logger.info(comment.getCommentNum()+"의 children: "+children.toString());
		}
		logger.info("restCommentCount:"+commentDAO.countRestComment(81, lastCommentTime));
		logger.info("restCommentCount:"+commentDAO.countRestComment(81, 5));
	}
	
	@Test
	public void selectVote() {
//		List<VoteVO> list=  commentDAO.selectMyAllVotes(397, 81);
	}
	
	
}
