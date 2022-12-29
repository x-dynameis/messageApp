package application;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.util.Callback;

public class AddButton {
	//TableViewにボタンを表示させるクラス

	
    public AddButton(TableView table,String masterID) {
        TableColumn<Article, Void> colBtn = new TableColumn("Button");

        Callback<TableColumn<Article, Void>, TableCell<Article, Void>> cellFactory = new Callback<TableColumn<Article, Void>, TableCell<Article, Void>>() {
            @Override
            public TableCell<Article, Void> call(final TableColumn<Article, Void> param) {
                final TableCell<Article, Void> cell = new TableCell<Article, Void>() {

                	private final Button btn = new Button("Action");
                    {
                    	
                        btn.setOnAction((ActionEvent event) -> {
                        	Article index = getTableView().getItems().get(getIndex());
//                            System.out.println("selectedData: " + index.getB_subject());
                        	try {
                        		WindowOpen window = new WindowOpen("Sample.fxml");
                        		window.getController().setFromText(index.getB_from());
                        		window.getController().setMainArea(index.getB_main());
                        		window.getController().setDateText(index.getB_date() + " " + index.getB_time());
                        		window.getController().setToText(index.getB_to());
                        		window.getController().setSubject(index.getB_subject());
                        		window.getController().cidSetVisibleFalse();
                        		window.getController().cidSearchVisibleFalse();
                        		window.getController().cidResultVisibleFalse();
                        		window.getController().customerLabelUnvisible();
                        		window.getController().FromComboUnvisible();
                        		window.getController().ToComboUnvisible();
                        		window.getController().btnAction1Unvisible();
                        		
                        		LocalDateTime nowDate = LocalDateTime.now();
                        		DateTimeFormatter java8Format = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                        		String java8Disp = nowDate.format( java8Format );
                        		
//                        		System.out.println(index.getId() + ":" + masterID + ":" + java8Disp);
                        		String sql="insert into tbl_read(read_id,r_date,user_id) values(" + index.getId() + ",'" + java8Disp + " "  + 
                        				"'," + masterID + ")";
//                        		System.out.println(sql);
                        		
                        		DBAccess db = new DBAccess();
                        		if (db.readCount(index.getId(), masterID)==0) {
                        			System.out.println("True==0");
                        			db.updateDb(sql);
                        		}else {
                        			System.out.println("True>0");
                        		}
                        		db.DisCon();                        		
                        		window.getStage().showAndWait();
                        		
                  
          
                        	}catch(Exception ex) {
                        		ex.printStackTrace();
                        	}
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };
        colBtn.setCellFactory(cellFactory);
        table.getColumns().add(colBtn);
        
    }
}
