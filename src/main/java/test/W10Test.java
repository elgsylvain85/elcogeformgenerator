package test;

import java.util.ArrayList;
import java.util.List;

import cd.gcd.formgenerator.css.Css;
import cd.gcd.w10menu.W10MenuButton;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class W10Test extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(final Stage primaryStage) {

		primaryStage.setTitle("Popup Example");
		HBox layout = new HBox(10);
		layout.getChildren().addAll(create());
		Scene scene = new Scene(layout);
		scene.getStylesheets().add(Css.DarkTheme.url());
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public W10MenuButton create() {
		List<W10MenuButton.W10MenuItem> list = new ArrayList<W10MenuButton.W10MenuItem>();
		for (int i = 0; i < 100; i++) {

			final int ii = i;
			EventHandler<ActionEvent> aevent = new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					System.out.println("Menu " + ii);
				}
			};

			W10MenuButton.W10MenuItem w10 = null;
			if (i < 50)
				w10 = new W10MenuButton.W10MenuItem("Menu " + i, true, "setting.png", aevent);
			else
				w10 = new W10MenuButton.W10MenuItem("Menu " + i, false, "setting.png", aevent);
			list.add(w10);
		}

		W10MenuButton w10 = new W10MenuButton();
		w10.reload(list);

		return w10;
	}
}