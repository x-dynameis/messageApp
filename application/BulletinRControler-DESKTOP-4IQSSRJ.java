package application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
//import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
//import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
//import javafx.stage.Stage;
import java.net.URL;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

public class BulletinRControler  implements Initializable{

	@FXML private TableView<Article> table;
	@FXML private TableColumn<Article,String> idCol;
	@FXML private TableColumn<Article,String> dateCol;
	@FXML private TableColumn<Article,String> timeCol;
	@FXML private TableColumn<Article,String> subCol;
	@FXML private TableColumn<Article,String> contCol;
	@FXML private TableColumn<Article,String>writerCol;
	@FXML private TableColumn<Article,String> readerCol;
	@FXML private ComboBox<String> searchName;
	private ObservableList<Article> data;
    @FXML private TextField fromField;
    @FXML private TextField toField;
    @FXML    private TextField userName;
    @FXML    private ComboBox<Employee> userNo;
    @FXML    private Button btnTest;

//    private ObjectProperty<Employee> selectedData;
    private String masterName;
    private String masterId;


//    private ObservableList<Employee> Emp;
    
	@Override
	public void initialize(URL url,ResourceBundle bundle) {
		 ReadingFile rf= new ReadingFile("userFile.txt");
		 masterId= rf.id;
		 masterName=rf.name;

		 
		data = FXCollections.observableArrayList();
		table.itemsProperty().setValue(data);
		table.setItems(data);
//		addButtonToTable();
		AddButton btn1 = new AddButton(table);
		
		
		AddCheckBox checkbox1 = new AddCheckBox(table);	
		
		AddCheckBox checkbox2 = new AddCheckBox(table);
		idCol.setCellValueFactory(new PropertyValueFactory<Article,String>("id"));
		dateCol.setCellValueFactory(new PropertyValueFactory<Article,String>("b_date"));
		timeCol.setCellValueFactory(new PropertyValueFactory<Article,String>("b_time"));
		subCol.setCellValueFactory(new PropertyValueFactory<Article,String>("b_subject"));
		contCol.setCellValueFactory(new PropertyValueFactory<Article,String>("b_main"));
		writerCol.setCellValueFactory(new PropertyValueFactory<Article,String>("b_from"));
		readerCol.setCellValueFactory(new PropertyValueFactory<Article,String>("b_to"));

		

		
		setDateText();
		tableShow();
//		userNo.setEditable(true);
		new AddComboBox(userNo,"select id,name from t_user where retire = 0");
		//リストの値をテキストボックスに入ロyくする
//		 selectedData = new SimpleObjectProperty<Employee>();
//		 selectedData.bind(userNo.getSelectionModel().selectedItemProperty());
//		 userName.textProperty().bind(selectedData.asString());

		 userNo.getSelectionModel().selectedItemProperty().addListener((r,o,newValue) ->{
			 if(newValue==null ) {
				 userName.textProperty().set("選択なし");
			 }else {
				 userName.textProperty().set(newValue.getName());

			 }
		 });
		 userNo.setValue(new Employee(masterId,masterName));


//	        // マウスクリック時に動作する
//	        table.setOnMouseClicked(event -> {
//	        	
//	            System.out.println( "行が選択されたぜ！！");
//	        });
//		userNo.getSelectionModel().select(0);
		
//		searchName.getItems().add(0,"指定なし");
//		searchName.getItems().add(1,"1");
//		searchName.getItems().add(2,"2");
//		searchName.getItems().add(3,"3");
//		searchName.getSelectionModel().select(0);
	}
	//検索ボタン
    @FXML
	public void btnSearch(ActionEvent e) {
//		System.out.println(searchName.getSelectionModel().selectedIndexProperty());
//    	System.out.println("btnSearch Clickd!");
    	tableShow();

	}
    @FXML
    public void btnNewForm(ActionEvent e) {
    	try {
//    	FuncLib func = new FuncLib();
//    		func.showSecondWindow("Sample.fxml");
    	WindowOpen window = new WindowOpen("Sample.fxml");
//    	window.getController().setSubject("大成功！！！");
    	window.getController().dateTextUnvisible();
    	window.getController().searchUnvisible();
    	window.getController().setFromCombo(new Employee(masterId,masterName));

//    	window.getController().setFromId(new Employee(masterId,masterName));
    	window.getStage().showAndWait();

    	}catch(Exception ex) {
    		ex.printStackTrace();
    	}
    }
//TableViewにDataBaseのResultSetを表示する
    public void tableShow() {
    	data.clear();
		ResultSet rs;
		DBAccess db = new DBAccess();
		String fromDate = fromField.getText();
		String toDate = toField.getText();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = sdf.parse(toDate);
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.add(c.DAY_OF_MONTH, +1);
			Date oneDay = c.getTime();
//			toDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(oneDay);
			toDate = new SimpleDateFormat("yyyy-MM-dd").format(oneDay);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		String SQL ="SELECT bt.id, bt.b_date, bt.b_subject, bt.b_main,"
				+ " bt.b_from, u1.name AS fromName, bt.b_to, u2.name as toName "
				+ "FROM tbl_bulletin as bt "
				+ "LEFT JOIN t_user as u1 ON bt.b_from = u1.id "
				+ "left join t_user as u2 on bt.b_to = u2.id "
				+ "where bt.b_date Between '" + fromDate + "' and '" + toDate +"' "
				+ "order by bt.b_date DESC";

//		System.out.println(SQL);

		try {

			rs = db.executeQuery(SQL);
			

			while(rs.next()) {
				data.add(new Article(rs.getString(1),rs.getString(2),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(6),rs.getString(8)));
			}

		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			db.DisCon();
		}
    	
    }
    //日付検索用のテキストボックスに日付をセットする
    public void setDateText() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
//		Calendar c2 = Calendar.getInstance();
		c.setTime(new Date());
		c.add(c.DAY_OF_MONTH, -7);
//		c2.setTime(new Date());
//		c2.add(c2.DAY_OF_MONTH, +1);
		Date sevenDay=c.getTime();
//		Date today=c2.getTime();
		Date today=new Date();
		
		fromField.setText(sdf.format(sevenDay));
		toField.setText(sdf.format(today));
    }
    public String getMasterId() {
    	return masterId;
    }
    

}
