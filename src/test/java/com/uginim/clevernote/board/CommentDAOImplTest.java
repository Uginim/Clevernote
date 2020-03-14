package com.uginim.clevernote.board;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.junit.jupiter.api.Disabled;
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
	@Disabled
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
	
	@Disabled
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
		List<BoardCommentVO> comments = commentDAO.selectRootCommentList(81, lastCommentTime, 10);
		logger.info("comments:"+comments.toString());
		for(BoardCommentVO comment:comments) {
			if(comment.getChildrenCount()>0) {
				List<BoardCommentVO> children = commentDAO.selectChildCommentList(comment.getCommentNum(), lastCommentTime, 3);
				logger.info(comment.getCommentNum()+"의 children: \n"+children.toString());				
			}
		}
		logger.info("restCommentCount:"+commentDAO.countRestComment(81, lastCommentTime));
		logger.info("restCommentCount:"+commentDAO.countRestComment(81, 5));
		
		logger.info("내 댓글 리스트:\n"+commentDAO.selectAllMyComments(397));
		logger.info("내 댓글 리스트:\n"+commentDAO.selectAllMyComments(397,81));
		
		logger.info("모든 댓글 변경내역:\n"+commentDAO.selectAllHistory(81, lastCommentTime));
		BoardCommentVO comment =  commentDAO.selectOneComment(45);
		logger.info("댓글 하나 가져오기:\n"+comment );
		comment.setContent(comment.getContent()+" 수정됨: ["+ new Date() +"] ");
		logger.info("댓글 하나 가져오기:\n"+commentDAO.update(comment ));		
		BoardCommentVO updatedComment =  commentDAO.selectOneComment(45);
		logger.info("수정된 댓글:\n"+comment );
		BoardCommentVO newComment = new BoardCommentVO();
		newComment.setContent("새 댓글["+ new Date() +"]");
		newComment.setPostNum(81);
		newComment.setUserNum(397);
		newComment.setUsername("유저네임");		
		commentDAO.insert(newComment);
		logger.info(newComment.toString());
		logger.info("댓글삭제하기:\n"+commentDAO.delete(newComment.getCommentNum()) );
	}
	
	@Disabled
	@Test
	public void selectOneComment() {
		// 루트댓글
		BoardCommentVO rootComment =  commentDAO.selectOneComment(3);
		logger.info("root comment:\n"+rootComment);
		// 자식 댓글
		BoardCommentVO childComment =  commentDAO.selectOneComment(133);
		logger.info("child comment:\n"+childComment);
	}
	
//	@Disabled
	@Test
//	@Transactional
	@DisplayName("투표기능 ")
	public void selectVote() {
		
		List<VoteVO> list=  commentDAO.selectMyAllVotes(397, 81);
		logger.info("vote list:\n"+list);
		
		VoteVO newVote = new VoteVO();
		newVote.setCommentNum(43);
		newVote.setType('L');
		newVote.setUserNum(397);
		logger.info("insertresult: "+commentDAO.insertNewVote(newVote));		
		logger.info("deleteResult: "+commentDAO.deleteVote(newVote.getVoteNum()));
		VoteVO newVote2 = new VoteVO();
		newVote2.setCommentNum(43);
		newVote2.setType('D');
		newVote2.setUserNum(397);
		logger.info("insertresult2: "+commentDAO.mergeNewVote(newVote2));
//		logger.info("deleteResult2: "+commentDAO.deleteVote(newVote2.getCommentNum(),newVote2.getUserNum()));
		newVote2.setCommentNum(43);
		newVote2.setType('D');
		newVote2.setUserNum(398);
		logger.info("insertresult2: "+commentDAO.mergeNewVote(newVote2));
//		logger.info("deleteResult2: "+commentDAO.deleteVote(newVote2.getCommentNum(),newVote2.getUserNum()));
		newVote2.setCommentNum(43);
		newVote2.setType('D');
		newVote2.setUserNum(399);
		logger.info("insertresult2: "+commentDAO.mergeNewVote(newVote2));
//		logger.info("deleteResult2: "+commentDAO.deleteVote(newVote2.getCommentNum(),newVote2.getUserNum()));
		newVote2.setCommentNum(43);
		newVote2.setType('L');
		newVote2.setUserNum(399);
		logger.info("insertresult2: "+commentDAO.mergeNewVote(newVote2));
	}
	
	
}
