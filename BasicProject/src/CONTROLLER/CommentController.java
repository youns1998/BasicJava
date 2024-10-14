package CONTROLLER;

public class CommentController {

	private static CommentController instance;

	private CommentController() {

	}

	public static CommentController getInstance() {
		if (instance == null)
			instance = new CommentController();
		return instance;
	}

}
