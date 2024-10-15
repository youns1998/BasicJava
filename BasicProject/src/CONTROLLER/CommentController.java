package CONTROLLER;

import UTIL.Command;

public class CommentController {

	private static CommentController instance;

	private CommentController() {

	}

	public static CommentController getInstance() {
		if (instance == null)
			instance = new CommentController();
		return instance;
	}
	public Command commentInsert() {
		
		return Command.POST_DETAIL;
	}
}
