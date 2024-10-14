package CONTROLLER;

public class CategoryController {
	private static CategoryController instance;

	private CategoryController() {

	}

	public static CategoryController getInstance() {
		if (instance == null)
			instance = new CategoryController();
		return instance;
	}
}
