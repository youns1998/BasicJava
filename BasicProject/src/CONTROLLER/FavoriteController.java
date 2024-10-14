package CONTROLLER;

public class FavoriteController {
	private static FavoriteController instance;

	private FavoriteController() {

	}

	public static FavoriteController getInstance() {
		if (instance == null)
			instance = new FavoriteController();
		return instance;
	}
}
