package cd.gcd.w10menu;

import java.util.List;


import org.controlsfx.control.PopOver;

import cd.gcd.formgenerator.icons.Icon;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

/**
 * This class extend javafx.scene.control.Button. Its instance propase a menu
 * popup of W10MenuButton.W10MenuItem and a 'MenuButton' with all same items
 * 
 * @author mulambas
 *
 */
public class W10MenuButton extends Button {

	private MenuButton menuButton;
	private GridPane gridPane;
	private PopOver popoverMenu;

	public W10MenuButton() {

		super("", new ImageView(new Image(Icon.HOME.url())));

		Pane root = new Pane();

		root.setId("context-menu");

		menuButton = new MenuButton("", new ImageView(Icon.MENU.url()));

		gridPane = new GridPane();
		ScrollPane scrollPane = new ScrollPane(gridPane);

		menuButton.setLayoutX(30);
		menuButton.setLayoutY(30);
		menuButton.setPopupSide(Side.BOTTOM);

		scrollPane.setLayoutX(130);
		scrollPane.setLayoutY(30);
		scrollPane.setPrefWidth(330);
		scrollPane.setPrefHeight(330);

		root.getChildren().add(menuButton);
		root.getChildren().add(scrollPane);

		popoverMenu = new PopOver(root);
		popoverMenu.setTitle("");
		this.setOnAction((event) -> popoverMenu.show(this));
	}

	/**
	 * Fill this instance with W10MenuButton.W10MenuItem
	 * 
	 * @param w10MenuItems
	 */
	public void reload(List<W10MenuButton.W10MenuItem> w10MenuItems) {

		menuButton.getItems().clear();
		gridPane.getChildren().clear();

		int row = 0;
		int col = 0;

		for (W10MenuItem w10MenuItem : w10MenuItems) {

			menuButton.getItems().add(w10MenuItem);

			if (w10MenuItem.pin) {

				Button button = new Button(w10MenuItem.getText());
				button.setTooltip(new Tooltip(w10MenuItem.getText()));
				button.setId("buttontransparent");
				button.setPrefHeight(99);
				button.setPrefWidth(99);
				button.setOnAction(w10MenuItem.getOnAction());
				button.setAlignment(Pos.BASELINE_LEFT);
				if (w10MenuItem.image != null) {
					button.setGraphic(new ImageView(w10MenuItem.image));
				}

				gridPane.add(button, col, row);
				col++;
				if (col >= 3) {
					col = 0;
					row++;
				}
			}
		}
	}

	/**
	 * hide its popup menu if detached
	 */
	public void hidepopoverMenu() {
		if (!this.popoverMenu.isDetached())
			this.popoverMenu.hide();
	}
	
	public void showpopoverMenu() {
		this.popoverMenu.show(this);
	}

	/**
	 * extends javafx.scene.control.MenuItem; Its instance is the menu item for
	 * 'W10MenuButton', once to add, it shows it on the 'MenuButton' and on the menu
	 * popup with icon if 'pin' attribute is at true
	 * 
	 * @author mulambas
	 *
	 */
	public static class W10MenuItem extends MenuItem {

		private boolean pin;
		private Image image;

		/**
		 * Constructor complete
		 * 
		 * @param label
		 *            : label of item on the menu
		 * @param pin
		 *            : true to show that too on the popup menu
		 * @param iconpath
		 *            : icon to show on the popup menu
		 * @param event
		 *            : event of item
		 */
		public W10MenuItem(String label, boolean pin, String iconpath, EventHandler<ActionEvent> event) {
			super(label);
			this.pin = pin;

			if (iconpath != null) {
				try {
					image = new Image(iconpath);
					this.setGraphic(new ImageView(image));
				} catch (Exception ex) {
					System.err.println("Error loading menu icon");
				}
			}

			this.setOnAction(event);

		}

		@Override
		public String toString() {
			return this.getText();
		}
	}
}