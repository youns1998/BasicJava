package CONTROLLER;

public class HistoryController {
	private static HistoryController instance;

	private HistoryController() {

	}

	public static HistoryController getInstance() {
		if (instance == null)
			instance = new HistoryController();
		return instance;
	}
}
