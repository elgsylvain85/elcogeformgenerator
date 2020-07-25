package cd.gcd.formgenerator;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.controlsfx.control.table.TableFilter;

import cd.gcd.formgenerator.annotation.AreaTextField;
import cd.gcd.formgenerator.annotation.DateTime;
import cd.gcd.formgenerator.annotation.DisableField;
import cd.gcd.formgenerator.annotation.IgnoreField;
import cd.gcd.formgenerator.icons.Icon;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import jfxtras.labs.scene.control.BigDecimalField;
import jfxtras.scene.control.LocalDateTimeTextField;

public class FormGenerator extends SplitPane {
   private Map<String, Object> controls;
//   private List<Field> fieldsToIgnore;
   private Map<Object, Field> fieldlinked;
   private BorderPane formPane;
   private GridPane inputformPane;
   private GridPane buttonPane;
   private Label title;
   public static final String TABLEVIEW_DATAS = "TABLEVIEW_DATAS";
   public static final String BUTTON_VALID = "BUTTON_VALID";
   public static final String BUTTON_NEW = "BUTTON_NEW";
   public static final String CONTEXTMENU_DATAS = "CONTEXTMENU_DATAS";
   public static final String NEXT_IM_DATAS = "NEXT_IM_DATAS";
   public static final String PREVIOUS_IM_DATAS = "PREVIOUS_IM_DATAS";
   public static final String ALL_IM_DATAS = "ALL_IM_DATAS";
   public static final String GOTO_IM_DATAS = "GOTO_IM_DATAS";
   public static final String FIND_IM_DATAS = "FIND_IM_DATAS";
   public static final String REFRESH_IM_DATAS = "REFRESH_IM_DATAS";
   public static final String CURRENTPAGE_LABEL = "CURRENTPAGE_LABEL";
   public static final String TOTALPAGE_LABEL = "TOTALPAGE_LABEL";

   public FormGenerator(Class<?> classe, String iconpath, boolean generevalidbutton, boolean generetableview, boolean generateform) {
      this.setOrientation(Orientation.HORIZONTAL);
      this.controls = new HashMap();
      this.fieldlinked = new HashMap();
      this.inputformPane = new GridPane();
      this.buttonPane = new GridPane();
      this.formPane = new BorderPane();
      this.formPane.setCenter(this.inputformPane);
      this.formPane.setBottom(this.buttonPane);
      MenuItem nextMI = new MenuItem("next records", new ImageView(Icon.NEXT.url()));
      MenuItem prevMI = new MenuItem("previous records", new ImageView(Icon.PREVIOUS.url()));
      MenuItem gotoMI = new MenuItem("goto", new ImageView(Icon.RUN.url()));
      MenuItem allMI = new MenuItem("all records", new ImageView(Icon.ALL.url()));
      MenuItem findMI = new MenuItem("find", new ImageView(Icon.SEARCH.url()));
      MenuItem refreshMI = new MenuItem("refresh", new ImageView(Icon.REFRESH.url()));
//      MenuItem ascvsItem = new MenuItem("export to csv", new ImageView(Icon.REPORT.url()));
      this.controls.put("NEXT_IM_DATAS" + MenuItem.class.getName(), nextMI);
      this.controls.put("PREVIOUS_IM_DATAS" + MenuItem.class.getName(), prevMI);
      this.controls.put("ALL_IM_DATAS" + MenuItem.class.getName(), allMI);
      this.controls.put("FIND_IM_DATAS" + MenuItem.class.getName(), findMI);
      this.controls.put("REFRESH_IM_DATAS" + MenuItem.class.getName(), refreshMI);
      this.controls.put("GOTO_IM_DATAS" + MenuItem.class.getName(), gotoMI);
      this.getItems().addAll(new Node[]{new ScrollPane(this.formPane)});
      this.inputformPane.setHgap(10.0D);
      this.inputformPane.setVgap(10.0D);
      this.inputformPane.setPadding(new Insets(10.0D));
      this.buttonPane.setHgap(10.0D);
      this.buttonPane.setPadding(new Insets(10.0D));
//      this.fieldsToIgnore = Arrays.asList(fieldsToIgnore);
      int c = 0;
      int r = 0;
      if (iconpath != null) {
         try {
            ImageView iv = new ImageView(iconpath);
            this.inputformPane.add(iv, c, r);
            GridPane.setValignment(iv, VPos.TOP);
            GridPane.setHalignment(iv, HPos.LEFT);
         } catch (Exception var27) {
            System.err.println("Error loading menu icon");
         }
      }

      this.title = new Label(UtilityFormGenerator.formatToLabel(classe.getSimpleName()));
      this.title.getStyleClass().add("title");
      this.inputformPane.add(this.title, c + 1, r);
      GridPane.setValignment(this.title, VPos.TOP);
      GridPane.setHalignment(this.title, HPos.LEFT);
      Label currentpage_label = new Label();
      Label totalpage_label = new Label();
      GridPane gp = new GridPane();
      gp.add(new Label("page : "), 0, 0);
      gp.add(currentpage_label, 1, 0);
      gp.add(new Label("/"), 2, 0);
      gp.add(totalpage_label, 3, 0);
      this.inputformPane.add(gp, c + 3, r);
      this.controls.put("CURRENTPAGE_LABEL" + Label.class.getName(), currentpage_label);
      this.controls.put("TOTALPAGE_LABEL" + Label.class.getName(), totalpage_label);
      if (generateform) {
         Field[] var19 = classe.getDeclaredFields();
         int var20 = var19.length;

         for(int var21 = 0; var21 < var20; ++var21) {
            Field field = var19[var21];
            if (field.getAnnotation(IgnoreField.class) == null) {
               Class<?> fieldtype = field.getType();
               Control control = null;
               Label label = new Label(UtilityFormGenerator.formatToLabel(field.getName()));
               this.controls.put(field.getName() + Label.class.getName(), label);
               this.fieldlinked.put(label, field);
               ++r;
               this.inputformPane.add(label, c, r);
               GridPane.setValignment(label, VPos.TOP);
               GridPane.setHalignment(label, HPos.LEFT);
               if (fieldtype == String.class) {
                  if (field.getAnnotation(AreaTextField.class) != null) {
                     control = new TextArea();
                  } else {
                     control = new TextField();
                  }
               } else if (fieldtype == Integer.class) {
                  control = new BigDecimalField();
               } else if (fieldtype == Double.class) {
                  control = new BigDecimalField(BigDecimal.ZERO, new BigDecimal("0.05"), new DecimalFormat("#,##0.00"));
               } else if (fieldtype == Date.class) {
                  if (field.getAnnotation(DateTime.class) != null) {
                     control = new LocalDateTimeTextField();
                  } else {
                     control = new DatePicker();
                  }
               } else if (fieldtype == Boolean.class) {
                  control = new CheckBox();
               } else if (fieldtype.isEnum()) {
                  control = new ComboBox();
               } else if (fieldtype != byte[].class && fieldtype != Byte[].class) {
                  if (!List.class.isAssignableFrom(fieldtype) && !fieldtype.isArray()) {
                     control = new ComboBox();
                  } else {
                     ListView<Object> lv = new ListView();
                     this.controls.put(field.getName() + ListView.class.getName(), lv);
                     this.fieldlinked.put(lv, field);
                     this.inputformPane.add(lv, c + 1 + 1, r);
                     GridPane.setValignment(lv, VPos.TOP);
                     GridPane.setHalignment(lv, HPos.LEFT);
                     control = new ComboBox();
                  }
               } else {
                  ImageView iv = new ImageView();
                  iv.setFitHeight(100.0D);
                  iv.setFitWidth(100.0D);
                  this.controls.put(field.getName() + ImageView.class.getName(), iv);
                  this.fieldlinked.put(iv, field);
                  this.inputformPane.add(iv, c + 1 + 1, r);
                  GridPane.setValignment(iv, VPos.TOP);
                  GridPane.setHalignment(iv, HPos.LEFT);
                  control = new Button("", new ImageView(Icon.IMPORT.url()));
               }

               String controlname = field.getName() + control.getClass().getName();
               if (field.getAnnotation(DisableField.class) != null) {
                  ((Control)control).setDisable(true);
               }

               this.controls.put(controlname, control);
               this.fieldlinked.put(control, field);
               this.inputformPane.add((Node)control, c + 1, r);
               GridPane.setValignment((Node)control, VPos.TOP);
               GridPane.setHalignment((Node)control, HPos.LEFT);
            }
         }

         if (generevalidbutton) {
            ++r;
            Button savebutton = new Button("valid", new ImageView(Icon.VALID.url()));
            this.controls.put("BUTTON_VALID" + Button.class.getName(), savebutton);
            this.buttonPane.add(savebutton, 0, 0);
            Button newbutton = new Button("new", new ImageView(Icon.CANCEL.url()));
            this.controls.put("BUTTON_NEW" + Button.class.getName(), newbutton);
            this.buttonPane.add(newbutton, 1, 0);
            newbutton.setOnAction((e) -> {
               this.resetForm();
            });
         }
      }

      if (generetableview) {
         TableFilter<?> datasTView = UtilityFormGenerator.createTableView(classe);
         this.controls.put("TABLEVIEW_DATAS" + TableFilter.class.getName(), datasTView);
         if (generateform) {
            datasTView.getTableView().addEventHandler(MouseEvent.MOUSE_PRESSED, (event) -> {
               try {
                  Object selected = datasTView.getTableView().getSelectionModel().getSelectedItem();
                  if (selected != null) {
                     this.objectToForm(selected);
                  }
               } catch (Exception var4) {
                  var4.printStackTrace();
               }

            });
            datasTView.getTableView().addEventHandler(KeyEvent.KEY_RELEASED, (event) -> {
               try {
                  Object selected = datasTView.getTableView().getSelectionModel().getSelectedItem();
                  if (selected != null) {
                     this.objectToForm(selected);
                  }
               } catch (Exception var4) {
                  var4.printStackTrace();
               }

            });
         }

//         ascvsItem.setOnAction((e) -> {
//            List<?> dataset = null;
//            dataset = datasTView.getTableView().getSelectionModel().getSelectedItems();
//            FileChooser fc = new FileChooser();
//            fc.setInitialFileName(classe.getSimpleName());
//            fc.getExtensionFilters().add(new ExtensionFilter("csv", new String[]{"*.csv"}));
//            File f = fc.showSaveDialog((Window)null);
//            if (f != null) {
//               try {
//                  GCDUtility.asCSV(classe, dataset, f);
//                  UtilityFormGenerator.showalert("done", AlertType.INFORMATION, "", (ImageView)null);
//               } catch (IOException | SecurityException var7) {
//                  var7.printStackTrace();
//               }
//            }
//
//         });
         ContextMenu contextmenu = new ContextMenu();
         this.controls.put("CONTEXTMENU_DATAS" + ContextMenu.class.getName(), contextmenu);
         contextmenu.getItems().addAll(new MenuItem[]{nextMI, prevMI, gotoMI, allMI, findMI, refreshMI});
         datasTView.getTableView().setContextMenu(contextmenu);
         this.getItems().addAll(new Node[]{datasTView.getTableView()});
      }

   }

   public void formToObject(Object object) throws Exception {
      Field[] var2 = object.getClass().getDeclaredFields();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Field field = var2[var4];

         try {
            if (field.getAnnotation(IgnoreField.class) == null) {
               Class<?> fieldtype = field.getType();
               String settername = field.getName().substring(1);
               settername = "set" + field.getName().substring(0, 1).toUpperCase() + settername;
               Method setter = object.getClass().getDeclaredMethod(settername, fieldtype);
               Object value = null;

               try {
                  if (fieldtype == String.class) {
                     if (field.getAnnotation(AreaTextField.class) != null) {
                        value = ((TextArea)this.controls.get(field.getName() + TextArea.class.getName())).getText();
                     } else {
                        value = ((TextField)this.controls.get(field.getName() + TextField.class.getName())).getText();
                     }

                     if (value.equals("")) {
                        value = null;
                     }
                  } else if (fieldtype == Integer.class) {
                     value = ((BigDecimalField)this.controls.get(field.getName() + BigDecimalField.class.getName())).getNumber().intValue();
                  } else if (fieldtype == Double.class) {
                     value = ((BigDecimalField)this.controls.get(field.getName() + BigDecimalField.class.getName())).getNumber().doubleValue();
                  } else if (fieldtype == Date.class) {
                     Instant instant;
                     if (field.getAnnotation(DateTime.class) != null) {
                        instant = ((LocalDateTimeTextField)this.controls.get(field.getName() + LocalDateTimeTextField.class.getName())).getLocalDateTime().atZone(ZoneId.systemDefault()).toInstant();
                     } else {
                        instant = ((LocalDate)((DatePicker)this.controls.get(field.getName() + DatePicker.class.getName())).getValue()).atStartOfDay(ZoneId.systemDefault()).toInstant();
                     }

                     value = Date.from(instant);
                  } else if (fieldtype == Boolean.class) {
                     value = ((CheckBox)this.controls.get(field.getName() + CheckBox.class.getName())).isSelected();
                  } else if (fieldtype.isEnum()) {
                     ComboBox<Object> cbox = (ComboBox)this.controls.get(field.getName() + ComboBox.class.getName());
                     value = cbox.getSelectionModel().getSelectedItem();
                  } else if (List.class.isAssignableFrom(fieldtype)) {
                     List<Object> lv = new ArrayList();
                     lv.addAll(((ListView)this.controls.get(field.getName() + ListView.class.getName())).getItems());
                     value = lv;
                  } else {
                     if (fieldtype.isArray()) {
                        throw new UnsupportedOperationException("array type is not supported : " + field + " " + fieldtype);
                     }

                     value = ((ComboBox)this.controls.get(field.getName() + ComboBox.class.getName())).getSelectionModel().getSelectedItem();
                  }
               } catch (NullPointerException var11) {
                  value = null;
               }

               setter.invoke(object, fieldtype.cast(value));
            }
         } catch (Exception var12) {
            System.err.println("error while 'formToObject' for " + field + " with " + object);
            var12.printStackTrace();
         }
      }

   }

   public void objectToForm(Object object) throws Exception {
      this.resetForm();
      Field[] var2 = object.getClass().getDeclaredFields();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Field field = var2[var4];

         try {
            if (field.getAnnotation(IgnoreField.class) == null) {
               Class<?> fieldtype = field.getType();
               String gettername = field.getName().substring(1);
               if (fieldtype == Boolean.TYPE) {
                  gettername = "is" + field.getName().substring(0, 1).toUpperCase() + gettername;
               } else {
                  gettername = "get" + field.getName().substring(0, 1).toUpperCase() + gettername;
               }

               Method getter = object.getClass().getDeclaredMethod(gettername);
               Object value = getter.invoke(object);
               if (fieldtype == String.class) {
                  if (value == null) {
                     value = "";
                  }

                  if (field.getAnnotation(AreaTextField.class) != null) {
                     ((TextArea)this.controls.get(field.getName() + TextArea.class.getName())).setText((String)value);
                  } else {
                     ((TextField)this.controls.get(field.getName() + TextField.class.getName())).setText((String)value);
                  }
               } else if (fieldtype == Integer.class) {
                  if (value == null) {
                     value = 0;
                  }

                  ((BigDecimalField)this.controls.get(field.getName() + BigDecimalField.class.getName())).setNumber(BigDecimal.valueOf((long)(Integer)value));
               } else if (fieldtype == Double.class) {
                  if (value == null) {
                     value = 0.0D;
                  }

                  ((BigDecimalField)this.controls.get(field.getName() + BigDecimalField.class.getName())).setNumber(BigDecimal.valueOf((Double)value));
               } else if (fieldtype == Date.class) {
                  if (value == null) {
                     if (field.getAnnotation(DateTime.class) != null) {
                        ((LocalDateTimeTextField)this.controls.get(field.getName() + LocalDateTimeTextField.class.getName())).setLocalDateTime((LocalDateTime)null);
                     } else {
                        ((DatePicker)this.controls.get(field.getName() + DatePicker.class.getName())).setValue(null);
                     }
                  } else if (field.getAnnotation(DateTime.class) != null) {
                     ((LocalDateTimeTextField)this.controls.get(field.getName() + LocalDateTimeTextField.class.getName())).setLocalDateTime(LocalDateTime.ofInstant(((Date)value).toInstant(), ZoneId.systemDefault()));
                  } else {
                     ((DatePicker)this.controls.get(field.getName() + DatePicker.class.getName())).setValue(((Date)value).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                  }
               } else if (fieldtype == Boolean.class) {
                  if (value == null) {
                     value = false;
                  }

                  ((CheckBox)this.controls.get(field.getName() + CheckBox.class.getName())).setSelected((Boolean)value);
               } else if (fieldtype.isEnum()) {
                  ComboBox<Object> cbox = (ComboBox)this.controls.get(field.getName() + ComboBox.class.getName());
                  cbox.getSelectionModel().select(value);
               } else if (fieldtype != byte[].class && fieldtype != Byte[].class) {
                  if (List.class.isAssignableFrom(fieldtype)) {
                     if (value == null) {
                        value = new ArrayList();
                     }

                     ((ListView)this.controls.get(field.getName() + ListView.class.getName())).getItems().addAll((List)value);
                  } else {
                     if (fieldtype.isArray()) {
                        throw new UnsupportedOperationException("array type is not supported : " + field + " " + fieldtype);
                     }

                     ((ComboBox)this.controls.get(field.getName() + ComboBox.class.getName())).getSelectionModel().select(value);
                  }
               } else if (value != null) {
                  ImageView iv = (ImageView)this.controls.get(field.getName() + ImageView.class.getName());

                  try {
                     iv.setImage(new Image(new ByteArrayInputStream((byte[])((byte[])value))));
                  } catch (Exception var12) {
                     iv.setImage((Image)null);
                  }
               }
            }
         } catch (Exception var13) {
            System.err.println("error while 'objectToForm' for " + field + " with " + object);
            var13.printStackTrace();
         }
      }

   }

   public void resetForm() {
      Iterator var1 = this.controls.keySet().iterator();

      while(var1.hasNext()) {
         String key = (String)var1.next();
         Object c = this.controls.get(key);
         if (c instanceof TextArea) {
            ((TextArea)this.controls.get(key)).clear();
         } else if (c instanceof TextField) {
            ((TextField)this.controls.get(key)).clear();
         } else if (c instanceof BigDecimalField) {
            ((BigDecimalField)this.controls.get(key)).setText("0");
         } else if (c instanceof LocalDateTimeTextField) {
            ((LocalDateTimeTextField)this.controls.get(key)).setLocalDateTime((LocalDateTime)null);
         } else if (c instanceof CheckBox) {
            ((CheckBox)this.controls.get(key)).setSelected(false);
         } else if (c instanceof ComboBox) {
            ((ComboBox)this.controls.get(key)).getSelectionModel().clearSelection();
         } else if (c instanceof ListView) {
            ((ListView)this.controls.get(key)).getItems().clear();
         } else if (c instanceof ImageView) {
            ((ImageView)this.controls.get(key)).setImage((Image)null);
         }
      }

   }

   public Object getControl(String fieldname, Class<?> controltype) throws Exception {
      Object c = this.controls.get(fieldname + controltype.getName());
      return c;
   }

   public List<Object> allControls() {
      List<Object> result = new ArrayList();
      Iterator var2 = this.controls.values().iterator();

      while(var2.hasNext()) {
         Object c = var2.next();
         result.add(c);
      }

      return result;
   }

   public void addControl(String controlname, Control control, int col, int row) {
      this.controls.put(controlname + control.getClass().getName(), control);
      this.inputformPane.add(control, col, row);
      GridPane.setValignment(control, VPos.TOP);
      GridPane.setHalignment(control, HPos.LEFT);
   }

   public Map<Object, Field> getFieldlinked() {
      return this.fieldlinked;
   }

   public GridPane getInputformPane() {
      return this.inputformPane;
   }

   public void setTitle(Label title) {
      this.title = title;
   }
}
