package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class BulletinR extends Application{

	@Override
	public void start(Stage stage) throws Exception{
		try {
			Parent root = FXMLLoader.load(getClass().getResource("bulletinR.fxml"));
			Scene scene = new Scene(root);
			
			stage.setTitle("Sending Application");
			stage.setScene(scene);
			stage.show();

		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		launch(args);
	}

}
