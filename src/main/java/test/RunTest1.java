package test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;

import cd.gcd.formgenerator.FormGenerator;
import cd.gcd.utilitiesresources.css.Css;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class RunTest1 extends Application {

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		Field[] toignore = new Field[] { POJO.class.getDeclaredField("id") };

		FormGenerator form = new FormGenerator(POJO.class, "", toignore, true, true, true);

		((ComboBox<Integer>) form.getControl("phones", ComboBox.class)).getItems().addAll(81, 89, 99, 85);

		((Button) form.getControl(FormGenerator.BUTTON_VALID, Button.class)).setOnAction((event) -> {
			try {

				POJO pojo = new POJO();
				pojo.setId(1);
				pojo.setAdress("kinshasa");
				pojo.setBirthday(new Date());

				pojo.setPhones(new ArrayList<Integer>());
				pojo.getPhones().add(81);

				form.objectToForm(pojo);

			} catch (Exception e) {
				e.printStackTrace();
			}
		});


		AnchorPane.setBottomAnchor(form, 0.0);
		AnchorPane.setTopAnchor(form, 0.0);
		AnchorPane.setLeftAnchor(form, 0.0);
		AnchorPane.setRightAnchor(form, 0.0);
		
		Scene scene = new Scene(new AnchorPane(form), 800, 600);
		primaryStage.setScene(scene);

		scene.getStylesheets().add(Css.DarkTheme.url());

		primaryStage.show();
	}
}
