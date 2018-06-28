package edu.stratford.patientappointments.view.panels;

import com.google.common.eventbus.Subscribe;

import edu.stratford.patientappointments.model.valueobjects.ApplicationUserVO;
import edu.stratford.patientappointments.model.valueobjects.PatientAppointmentsApplicationSession;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class ApplicationMenuBar extends HBox {

	private Stage primaryStage;
	
	Menu loggedInUserInformationMenu;
	
	public ApplicationMenuBar( Stage primaryStage ) {
		
		super();
		this.primaryStage = primaryStage;
		PatientAppointmentsApplicationSession.INSTANCE.getEventBus().register(this);
	    loggedInUserInformationMenu = new Menu();
	    loggedInUserInformationMenu.setGraphic( new UserInformationPanel());
	    MenuItem item = new MenuItem( "Admin Sign In" );
    	item.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {

				LoginPopupPanel loginPopupPanel = new LoginPopupPanel( ApplicationMenuBar.this.primaryStage, "Y" );
				if ( !loginPopupPanel.isShowing() ) {
					
					loginPopupPanel.show( ApplicationMenuBar.this.primaryStage );
				} else {
					
					loginPopupPanel.hide();
				}
			}
		});
    	MenuItem item2 = new MenuItem( "Doctor Sign In" );
    	item2.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {

				LoginPopupPanel loginPopupPanel = new LoginPopupPanel( ApplicationMenuBar.this.primaryStage, "D" );
				if ( !loginPopupPanel.isShowing() ) {
					
					loginPopupPanel.show( ApplicationMenuBar.this.primaryStage );
				} else {
					
					loginPopupPanel.hide();
				}
			}
		});
	    loggedInUserInformationMenu.getItems().addAll(item,item2);

	    MenuBar menuBarRight = new MenuBar();
	    menuBarRight.getMenus().addAll(loggedInUserInformationMenu);

	    FlowPane flow = new FlowPane(Orientation.HORIZONTAL);
	    flow.setAlignment(Pos.TOP_RIGHT);
	    flow.setHgap(0);
	    flow.getChildren().addAll( menuBarRight);

	   setAlignment(Pos.CENTER_LEFT);
	   HBox.setHgrow(flow, Priority.ALWAYS);
	   getChildren().addAll(flow);
	   setStyle("-fx-background-color: lightgray;");
	}
	
	@Subscribe
	public void rerender(ApplicationUserVO applicationUserVO) {
		
		loggedInUserInformationMenu.getItems().clear();
		if ( applicationUserVO != null ) {
			
			MenuItem item = new MenuItem( "Sign Out" );
	    	item.setOnAction(new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent event) {

					PatientAppointmentsApplicationSession.INSTANCE.setCurrentSessionInfoVO(null);
					PatientAppointmentsApplicationSession.INSTANCE.getEventBus().post( "SignOut" );
				}
			});
	    	loggedInUserInformationMenu.getItems().add(item);
		} 
	}
	
	@Subscribe
	public void rerender(String s) {
		
		loggedInUserInformationMenu.getItems().clear();
		MenuItem item = new MenuItem( "Admin Sign In" );
    	item.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {

				LoginPopupPanel loginPopupPanel = new LoginPopupPanel( ApplicationMenuBar.this.primaryStage, "Y" );
				if ( !loginPopupPanel.isShowing() ) {
					
					loginPopupPanel.show( ApplicationMenuBar.this.primaryStage );
				} else {
					
					loginPopupPanel.hide();
				}
			}
		});
    	MenuItem item2 = new MenuItem( "Doctor Sign In" );
    	item2.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {

				LoginPopupPanel loginPopupPanel = new LoginPopupPanel( ApplicationMenuBar.this.primaryStage, "D" );
				if ( !loginPopupPanel.isShowing() ) {
					
					loginPopupPanel.show( ApplicationMenuBar.this.primaryStage );
				} else {
					
					loginPopupPanel.hide();
				}
			}
		});
	    loggedInUserInformationMenu.getItems().addAll(item,item2);
	}
}
