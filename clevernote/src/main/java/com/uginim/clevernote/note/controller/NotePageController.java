package com.uginim.clevernote.note.controller;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.uginim.clevernote.note.service.NoteService;

@RequestMapping("/note")
@Controller
public class NotePageController {
	
	public static final Logger logger = LoggerFactory.getLogger(NotePageController.class);
	@Inject
	NoteService noteManager;
	
	
	//노트 메인페이지 받아오기
	@GetMapping()
	public String getNotePage() { 
		return "note/note";
	}
}
