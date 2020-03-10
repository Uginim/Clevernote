package com.uginim.clevernote.board.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.uginim.clevernote.board.service.BoardManager;
import com.uginim.clevernote.board.vo.AttachmentFileVO;
import com.uginim.clevernote.board.vo.BoardTypeVO;
import com.uginim.clevernote.board.vo.BoardPostVO;
import com.uginim.clevernote.user.service.LoginManager;
import com.uginim.clevernote.user.service.LoginService;
import com.uginim.clevernote.user.vo.UserVO;

@Controller
@RequestMapping("/board")
public class BoardController {
	private static final Logger logger = LoggerFactory.getLogger(BoardController.class);
	
	@Inject
	BoardManager boardManager;
	
	
	
	@ModelAttribute
	public void getBoardTypes(Model model) {
		List<BoardTypeVO> boardTypes =  boardManager.getAllBoardTypes();
		logger.info(boardTypes.toString());
		model.addAttribute("boardTypes",boardTypes); 
	}
	
	// 게시글 작성 양식
	@GetMapping("/write/{returnPage}")
	public String writingForm(
			@ModelAttribute @PathVariable String returnPage,
			Model model, 
			HttpServletRequest request
			) {
		
		model.addAttribute("board",new BoardPostVO());

		return "board/writing-form";
	}
	
	// 게시글 업로드
	// org.springframework.web.multipart.commons.CommonsMultipartResolver를 bean으로 등록하지 않으면 binding 되지 않음
	@PostMapping(path="/write/{returnPage}")
	public String write(
			@PathVariable String returnPage,
//			@Valid 
			@ModelAttribute("board") BoardPostVO board,
			BindingResult result,
//			HttpServletRequest request
			HttpSession session
			) {
		
		if(result.hasErrors()) {
			return "/board/writing-form";
		}
		
		UserVO user = (UserVO) session.getAttribute(LoginService.KEY_LOGGED_IN_USERINFO);
		board.setUserNum(user.getUserNum());
		board.setUsername(user.getUsername());
		logger.info("게시글 post:" +board.toString());
		boardManager.write(board);
		return "redirect:/board/view/"+returnPage+"/"+board.getPostNum();
	}
	
	// 목록 보기
	@GetMapping({
		"/list",
		 "/list/{curPage}",
		 "/list/{curPage}/{searchType}/{keyword}"
	})
	public String getBoard(
			@PathVariable(required=false) String curPage,
			@PathVariable(required=false) String searchType,
			@PathVariable(required=false) String keyword,
			HttpSession session,
			Model model) {
		UserVO user = (UserVO) session.getAttribute(LoginService.KEY_LOGGED_IN_USERINFO);
		int reqPage = (curPage==null)? 1:Integer.valueOf(curPage);
		model.addAttribute("list", boardManager.list(reqPage, searchType, keyword));
		model.addAttribute("pageManager", boardManager.buildPageCriteria(reqPage, searchType, keyword));
		return "board/list";
	}
	
	// 게시글 보기
	@GetMapping("/view/{returnPage}/{postNum}")
	public String view(
		@ModelAttribute @PathVariable String returnPage,
		@PathVariable long postNum,
		Model model
			) {
		Map<String,Object> map = boardManager.view(postNum);
		BoardPostVO board = (BoardPostVO)map.get("board");
		
		logger.info(board.toString());
		List<AttachmentFileVO> files = null;
		if(map.get("files") != null) {
			files = (List<AttachmentFileVO>)map.get("files");
		}
		
		model.addAttribute("board", board);
		model.addAttribute("files", files);
		return "/board/view-page";
	}
	
	// 첨부파일 다운로드
	@GetMapping("/file/{attachmentNum}")
	public ResponseEntity<byte[]> getFile(@PathVariable long attachmentNum){
		AttachmentFileVO attachment = boardManager.viewFile(attachmentNum);
		logger.info("getFile" + attachment.toString());
		
		final HttpHeaders headers = new HttpHeaders();
		String[] mediaType = attachment.getMimetype().split("/");
		headers.setContentType(new MediaType(mediaType[0], mediaType[1]));
		headers.setContentLength(attachment.getFileSize());
		String filename = null;
		try {
			filename = new String(attachment.getName().getBytes("euc-kr"),"ISO-8859-1");			
		}catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		headers.setContentDispositionFormData("attachment", filename);
		return new ResponseEntity<byte[]>(attachment.getData(),headers,HttpStatus.OK);
	}
	
	// 게시글 삭제
	@GetMapping("/delete/{returnPage}/{postNum}")
	public String delete(
		@PathVariable String returnPage,
		@PathVariable long postNum,
		Model model
			) {
		// 1) 게시글 및 첨부파일 삭제
		boardManager.delete(postNum);
		// 2) 게시글 목록 가져오기
//		model.addAttribute("list", boardManager.list(returnPage, searchType, keyword))
		
		return "redirect:/board/list/"+returnPage;
	}
	
	// 첨부파일 1건 삭제
	@DeleteMapping("/file/{fid}")
	@ResponseBody
	public ResponseEntity<String> fileDelete(
			@PathVariable long attachmentNum
			) {
		int state = boardManager.deleteAttachment(attachmentNum);
		if(state == 1) {
			return new ResponseEntity<String>("success",HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("failed",HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/modify/{postNum}/{returnPage}")
	public String getModifyingPage(
		@PathVariable String returnPage,
		@PathVariable(value = "postNum") long postNum,
		Model model
		) {
		logger.info("modify request:" + postNum);
		Map<String,Object> map = boardManager.view(postNum);
		model.addAttribute("board", map.get("board"));
		model.addAttribute("files", map.get("files"));
		model.addAttribute("returnPage", returnPage);
		return "board/modifying-form";
	}
	// 게시글 수정
	@PostMapping("/modify/{returnPage}")
	public String modify(
		@PathVariable String returnPage,
		@Valid @ModelAttribute("board") BoardPostVO board,
		BindingResult result
			) {
		if(result.hasErrors()) {
			return "/board/reading-page";
		}
		logger.info("게시글 수정 내용:"+ board.toString());
		boardManager.modify(board);
		return "redirect:/board/view/"+returnPage+"/"+board.getPostNum();
	}
	
	// 답글 양식
	@GetMapping("/reply/page/{returnPage}/{postNum}")
	public String replyForm(
		@ModelAttribute @PathVariable String returnPage,
		@PathVariable long postNum,
		Model model
			) {
		Map<String,Object> map = boardManager.view(postNum);
		BoardPostVO board = (BoardPostVO) map.get("board");
		board.setTitle("[답글]" +board.getTitle());
		board.setContent("[원글]" +board.getContent());
		model.addAttribute("board", board);
		
		return "board/replying-form";
	}
	
	// 답글 처리
	@PostMapping("/reply/{returnPage}")
	public String reply(
			@PathVariable String returnPage,
			@Valid @ModelAttribute("board") BoardPostVO replyBoard,
			BindingResult result,
			HttpSession session
			) {
		logger.info("답글처리 :" + replyBoard.toString());
		if(result.hasErrors()) {
			return "/board/replying-form";
		}
		UserVO user =(UserVO) session.getAttribute(LoginManager.KEY_LOGGED_IN_USERINFO);
		replyBoard.setUserNum(user.getUserNum());
		replyBoard.setUsername(user.getUsername());
		boardManager.reply(replyBoard);
		

		//		return "redirect:/board/list/"+returnPage;
		return "redirect:/board/view/"+returnPage+"/"+replyBoard.getPostNum();

	}
	
}
