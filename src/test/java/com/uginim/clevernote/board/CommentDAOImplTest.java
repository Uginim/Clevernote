package com.uginim.clevernote.board;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import com.uginim.clevernote.board.dao.CommentDAO;
import com.uginim.clevernote.board.vo.BoardCommentVO;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "file:src/main/webapp/WEB-INF/spring/root-context.xml")
public class CommentDAOImplTest {
	public static final Logger logger = LoggerFactory.getLogger(CommentDAOImplTest.class);
	@Inject
	CommentDAO commentDAO;
	
//	@Transactional // 이거 붙으니까 테스트 후에 되돌아옴... 대박인데?
	@Test
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
		commentDAO.insert(comment2);
		logger.info(comment2.toString());
	}
}
