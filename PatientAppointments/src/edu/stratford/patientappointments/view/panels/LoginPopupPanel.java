package edu.stratford.patientappointments.view.panels;

import edu.stratford.patientappointments.dao.PatientAppointmentsDAO;
import edu.stratford.patientappointments.model.valueobjects.ApplicationUserVO;
import edu.stratford.patientappointments.model.valueobjects.PatientAppointmentsApplicationSession;
import edu.stratford.patientappointments.view.utils.PatientAppointmentUIUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.PopupControl;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginPopupPanel extends PopupControl {
	
	private Stage primaryStage;
	
	private Text errorMessage;

	private TextField emailField;

	private PasswordField passwordField;
	
	public LoginPopupPanel( Stage primaryStage, final String userType ) {

		super();
		this.primaryStage = primaryStage;
		sizeToScene();
		setHideOnEscape(true);
		setAutoHide(true);
		setAnchorLocation( AnchorLocation.WINDOW_TOP_LEFT );
		final GridPane layoutPane = new GridPane();
		layoutPane.setHgap(10);
		layoutPane.setVgap(10);
		layoutPane.setPadding(new Insets(10, 10, 10, 10));

		Text dialogTitle = new Text((userType.equals("Y")?"Admin":"Doctor") + " Sign In");
		dialogTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		layoutPane.add(dialogTitle, 1, 0, 2, 1);

		Text patientEmailTitle = new Text("Email:");
		patientEmailTitle.setFont(Font.font("Arial", FontWeight.BOLD, 10));
		layoutPane.add(patientEmailTitle, 1, 1);
		emailField = new TextField("anil.sehgal10@gmail.com");
		emailField.setPromptText( "someone@example.com" );
		layoutPane.add(emailField, 2, 1);


		Text patientPasswordTitle = new Text("Password:");
		patientPasswordTitle.setFont(Font.font("Arial", FontWeight.BOLD, 10));
		layoutPane.add(patientPasswordTitle, 1, 2);
		passwordField = new PasswordField();
		passwordField.setPromptText( "Password" );
		layoutPane.add(passwordField, 2, 2);

		Button signInButton = new Button( "Employee Sign In" );
		Hyperlink signUpLink = new Hyperlink( "New User? Sign Up" );
		
		signInButton.setOnAction( new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				if (areAllValid().trim().length() > 0) {
					
					PatientAppointmentUIUtils.showMessage(errorMessage, areAllValid(), Color.RED);
					return;
				}
				
				ApplicationUserVO applicationUserVO = new ApplicationUserVO();
				applicationUserVO.setUserEmailAddress(emailField.getText());
				applicationUserVO.setApplicationPassword(passwordField.getText());
				applicationUserVO.setIsAdmin( userType );
				try {
					applicationUserVO = PatientAppointmentsDAO.INSTANCE.getUserByEmail(applicationUserVO);
				} catch (Exception e) {
					
					PatientAppointmentUIUtils.showMessage(errorMessage, areAllValid(), Color.RED);
				}
				if ( applicationUserVO != null ) {
					
					PatientAppointmentsApplicationSession.INSTANCE.setCurrentSessionInfoVO(applicationUserVO);
					PatientAppointmentsApplicationSession.INSTANCE.getEventBus().post(applicationUserVO);
					PatientAppointmentUIUtils.showMessage(errorMessage, "SignIn Successful!", Color.GREEN, new EventHandler<ActionEvent>() {
						
						@Override
						public void handle(ActionEvent event) {

							hide();
						}
					});
				} else {
					
					PatientAppointmentUIUtils.showMessage(errorMessage, "Incorrect Employee Credentials!", Color.RED);
				}
				
			}
		});
		
		signUpLink.setOnAction( new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				hide();
				RegistrationPopupPanel registrationPopupPanel = new RegistrationPopupPanel();
				registrationPopupPanel.show(LoginPopupPanel.this.primaryStage);
			}
		});

		errorMessage = new Text();
		
		layoutPane.add(errorMessage, 1, 3, 2, 1);
		
		layoutPane.add(signInButton, 1, 4);
		layoutPane.add(signUpLink, 2, 4);
		getScene().setFill( Color.WHITE );
		getScene().setRoot(layoutPane);
		String cssDefault = "-fx-border-color: black;\n"
				+ "-fx-border-insets: 1;\n"
				+ "-fx-border-width: 1;\n"
				+ "-fx-border-style: solid;\n";
		layoutPane.setStyle(cssDefault);
	}
	
	private String areAllValid() {

		if( emailField.getText().trim().length() > 0 &&
		passwordField.getText().trim().length() > 0 ) {
			
			return "";
		} else {
			
			return "All fields are mandatory!";
		}
	}
}
