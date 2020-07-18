package cd.gcd.formgenerator;

import cd.gcd.utilitiesresources.utilities.GCDUtility;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Window;
import javafx.util.Pair;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.table.TableFilter;

public class UtilityFormGenerator {
   public static Pair<String, String> showLoginui(String css) {
      BorderPane bp = new BorderPane();
      bp.setPadding(new Insets(10.0D, 50.0D, 50.0D, 50.0D));
      TextField loginTField = new TextField();
      PasswordField passwordField = new PasswordField();
      GridPane gridPane = new GridPane();
      gridPane.setPadding(new Insets(20.0D, 20.0D, 20.0D, 20.0D));
      gridPane.setHgap(5.0D);
      gridPane.setVgap(5.0D);
      gridPane.add(new Label("Username"), 0, 0);
      gridPane.add(loginTField, 1, 0);
      gridPane.add(new Label("Password"), 0, 1);
      gridPane.add(passwordField, 1, 1);
      bp.setCenter(gridPane);
      ButtonType btntype = new ButtonType("valid", ButtonData.OK_DONE);
      Dialog<Pair<String, String>> dialog = new Dialog();
      dialog.getDialogPane().getStylesheets().add(css);
      dialog.getDialogPane().getButtonTypes().add(btntype);
      dialog.getDialogPane().setContent(bp);
      dialog.setResultConverter((param) -> {
         return param == btntype ? new Pair(loginTField.getText(), passwordField.getText()) : null;
      });
      Optional<Pair<String, String>> result = dialog.showAndWait();
      return result.isPresent() ? (Pair)result.get() : null;
   }

   public static String showPasswordLoginui(String css) {
      BorderPane bp = new BorderPane();
      bp.setPadding(new Insets(10.0D, 50.0D, 50.0D, 50.0D));
      PasswordField passwordField = new PasswordField();
      GridPane gridPane = new GridPane();
      gridPane.setPadding(new Insets(20.0D, 20.0D, 20.0D, 20.0D));
      gridPane.setHgap(5.0D);
      gridPane.setVgap(5.0D);
      gridPane.add(new Label("Password"), 0, 1);
      gridPane.add(passwordField, 1, 1);
      bp.setCenter(gridPane);
      ButtonType btntype = new ButtonType("valid", ButtonData.OK_DONE);
      Dialog<String> dialog = new Dialog();
      dialog.getDialogPane().getStylesheets().add(css);
      dialog.getDialogPane().getButtonTypes().add(btntype);
      dialog.getDialogPane().setContent(bp);
      dialog.setResultConverter((param) -> {
         return param == btntype ? passwordField.getText() : null;
      });
      Optional<String> result = dialog.showAndWait();
      return result.isPresent() ? (String)result.get() : null;
   }

   public static String showconfirmpassword(boolean hash, String oldpassword, String css) {
      String newpassword = "";

      while("".equals(newpassword)) {
         PasswordField txtfold = new PasswordField();
         txtfold.setPromptText("old password");
         PasswordField txtfnew = new PasswordField();
         txtfnew.setPromptText("new password");
         PasswordField txtfconfirm = new PasswordField();
         txtfconfirm.setPromptText("confirm password");
         GridPane gpane = new GridPane();
         gpane.add(txtfold, 0, 0);
         gpane.add(txtfnew, 0, 1);
         gpane.add(txtfconfirm, 0, 2);
         ButtonType btnapply = new ButtonType("apply", ButtonData.APPLY);
         ButtonType btncancel = new ButtonType("cancel", ButtonData.CANCEL_CLOSE);
         Dialog<String> dial = new Dialog();
         dial.getDialogPane().getStylesheets().add(css);
         dial.getDialogPane().setContent(gpane);
         dial.getDialogPane().getButtonTypes().addAll(new ButtonType[]{btnapply, btncancel});
         dial.setResultConverter((buttontype) -> {
            if (buttontype == btnapply) {
               String inoldpwd = txtfold.getText();
               if (hash) {
                  inoldpwd = String.valueOf(inoldpwd.hashCode());
               }

               if (inoldpwd.equals(oldpassword)) {
                  if (txtfnew.getText().equals(txtfconfirm.getText())) {
                     return txtfnew.getText();
                  }

                  showalert("new password not match", AlertType.ERROR, css, (ImageView)null);
               } else {
                  showalert("incorrect password", AlertType.ERROR, css, (ImageView)null);
               }
            } else if (buttontype == btncancel) {
               return null;
            }

            return "";
         });
         Optional<String> result = dial.showAndWait();
         if (result.isPresent()) {
            newpassword = (String)result.get();
         } else {
            newpassword = null;
         }
      }

      return newpassword;
   }

   public static String showInputAlert(String title, String css) {
      TextInputDialog tid = new TextInputDialog(title);
      tid.getDialogPane().getStylesheets().add(css);
      tid.setTitle(title);
      tid.setHeaderText("");
      Optional<String> r = tid.showAndWait();
      return r.isPresent() ? (String)r.get() : null;
   }

   public static ButtonType showalert(String message, AlertType alerttype, String css, ImageView graphic) {
      Alert a = new Alert(alerttype);
      if (css != null) {
         a.getDialogPane().getStylesheets().add(css);
      }

      a.setContentText(message);
      a.setHeaderText("");
      a.setTitle(alerttype.name());
      if (graphic != null) {
         a.setGraphic(graphic);
      }

      Optional<ButtonType> op = a.showAndWait();
      return op.isPresent() ? (ButtonType)op.get() : null;
   }

   public static void shownotification(String message, int notifytype) {
      Platform.runLater(() -> {
         Notifications not = Notifications.create();
         not.text(message);
         switch(notifytype) {
         case -1:
            not.showError();
            break;
         case 0:
            not.showInformation();
            break;
         case 1:
            not.showConfirm();
            break;
         case 2:
            not.showWarning();
         }

      });
   }

   public static void popover(Parent pane, int x, int y, Node parent, String rootCSS2) {
      Dialog<Object> dialog = new Dialog();
      Window window = dialog.getDialogPane().getScene().getWindow();
      window.setOnCloseRequest((event) -> {
         window.hide();
      });
      dialog.getDialogPane().getScene().getStylesheets().add(rootCSS2);
      dialog.getDialogPane().setContent(pane);
      dialog.showAndWait();
   }

   public static <T> TableFilter<T> createTableView(Class<T> classe, Field[] fieldsToIgnore) {
      TableView<T> tview = new TableView();
      List<Field> fields = GCDUtility.arrayToList(fieldsToIgnore);
      Field[] var4 = classe.getDeclaredFields();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         Field f = var4[var6];
         if (fields == null || !fields.contains(f)) {
            TableColumn<T, Object> col = new TableColumn(GCDUtility.formatToLabel(f.getName()));
            col.setCellValueFactory(new PropertyValueFactory(f.getName()));
            tview.getColumns().add(col);
         }
      }

      TableFilter<T> tableFilter = new TableFilter(tview);
      return tableFilter;
   }
}
