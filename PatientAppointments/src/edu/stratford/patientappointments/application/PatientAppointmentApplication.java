package edu.stratford.patientappointments.application;

import com.google.common.eventbus.Subscribe;

import edu.stratford.patientappointments.model.valueobjects.ApplicationUserVO;
import edu.stratford.patientappointments.model.valueobjects.PatientAppointmentsApplicationSession;
import edu.stratford.patientappointments.view.panels.AgendaPanel;
import edu.stratford.patientappointments.view.panels.ApplicationMenuBar;
import edu.stratford.patientappointments.view.panels.AppointmentFormPanel;
import edu.stratford.patientappointments.view.panels.HospitalAppointmentViewerPanel;
import edu.stratford.patientappointments.view.panels.LoginPopupPanel;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class PatientAppointmentApplication extends Application {
	
	Stage primaryStage;
	
	Scene scene;
	
	TabPane tabPane;
	
	Tab allAppointmentsTab;
	
	Tab agendaTab;
	
	@Override
	public void start(Stage primaryStage) {
		
		System.setProperty("glass.accessible.force", "false");
		this.primaryStage = primaryStage;
		try {
			
			PatientAppointmentsApplicationSession.INSTANCE.getEventBus().register(this);
			SplitPane rootBox = new SplitPane();
			tabPane = new TabPane();
			AppointmentFormPanel appointmentFormPanel = new AppointmentFormPanel();
			Tab newAppointmentsTab = new Tab( "New Appointment", appointmentFormPanel );
			newAppointmentsTab.setClosable(false);

			rootBox.getItems().add( tabPane );
			tabPane.getTabs().addAll(newAppointmentsTab );
			
			BorderPane borderPane = new BorderPane();
			ApplicationMenuBar applicationMenuBar = new ApplicationMenuBar(primaryStage);
			borderPane.setTop( applicationMenuBar );
			borderPane.setCenter( rootBox );
			
			scene = new Scene( borderPane );
		
			scene.getStylesheets().add( getClass().getResource( "application.css" ).toExternalForm() );
			primaryStage.setScene( scene );
			primaryStage.setTitle( "Health and Wellness" );
		//	primaryStage.setResizable(false);
			primaryStage.setWidth(1000);
			primaryStage.setHeight(600);
		//	primaryStage.setMaximized(true);
			primaryStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream( "edu/stratford/patientappointments/view/panels/images/Hospital.png" ) ) );
			primaryStage.show();
			LoginPopupPanel loginPopupPanel = new LoginPopupPanel(primaryStage, "Y");
			loginPopupPanel.show(primaryStage);
		} catch( Exception e ) {

			e.printStackTrace();
		}
	}

	public static void main( String[] args ) {

		launch( args );
	}
	
	@Subscribe
	public void rerender(ApplicationUserVO applicationUserVO) {

		HospitalAppointmentViewerPanel hospitalAppointmentViewerPanel = new HospitalAppointmentViewerPanel(primaryStage);
		allAppointmentsTab = new Tab( "All Appointments", hospitalAppointmentViewerPanel );
		allAppointmentsTab.setClosable(false);
		AgendaPanel agendaPanel = new AgendaPanel(primaryStage);
		agendaTab = new Tab( "Agenda", agendaPanel );
		agendaTab.setClosable(false);
		tabPane.getTabs().addAll(allAppointmentsTab, agendaTab);
	}
	
	@Subscribe
	public void rerender(String s) {

		if ( allAppointmentsTab != null && tabPane.getTabs().contains( allAppointmentsTab ) ) {
			
			tabPane.getTabs().remove(allAppointmentsTab);
		}
		if ( agendaTab != null && tabPane.getTabs().contains( agendaTab ) ) {
			
			tabPane.getTabs().remove(agendaTab);
		}
	}
}