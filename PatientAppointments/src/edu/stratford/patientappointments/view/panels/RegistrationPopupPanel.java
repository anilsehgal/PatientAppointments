package edu.stratford.patientappointments.view.panels;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import edu.stratford.patientappointments.dao.PatientAppointmentsDAO;
import edu.stratford.patientappointments.model.valueobjects.ApplicationUserVO;
import edu.stratford.patientappointments.model.valueobjects.PatientAppointmentsApplicationSession;
import edu.stratford.patientappointments.view.utils.PatientAppointmentUIUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.PopupControl;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class RegistrationPopupPanel extends PopupControl {

	private TextField firstNameField;
	private TextField lastNameField;
	private DatePicker dobField;
	private TextField emailField;
	private PasswordField passwordField;
	private PasswordField confirmPasswordField;
	private RadioButton adminButton;
	private RadioButton patientButton;
	private RadioButton doctorButton;

	public RegistrationPopupPanel() {
		
		super();
		sizeToScene();
		setHideOnEscape(true);
		setAutoHide(true);
		setAnchorLocation( AnchorLocation.WINDOW_TOP_LEFT );
		final GridPane layoutPane = new GridPane();
		layoutPane.setHgap(10);
		layoutPane.setVgap(10);
		layoutPane.setPadding(new Insets(10, 10, 10, 10));
		
		Text dialogTitle = new Text("Employee Registration");
		dialogTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		layoutPane.add(dialogTitle, 1, 0, 2, 1);
		
		
		Text patientFirstNameTitle = new Text("First Name:");
		patientFirstNameTitle.setFont(Font.font("Arial", FontWeight.BOLD, 10));
		layoutPane.add(patientFirstNameTitle, 1, 1);
		firstNameField = new TextField();
		firstNameField.setPromptText( "First Name" );
		layoutPane.add(firstNameField, 2, 1);

		
		Text patientLastNameTitle = new Text("Last Name:");
		patientLastNameTitle.setFont(Font.font("Arial", FontWeight.BOLD, 10));
		layoutPane.add(patientLastNameTitle, 1, 2);
		lastNameField = new TextField();
		lastNameField.setPromptText( "Last Name" );
		layoutPane.add(lastNameField, 2, 2);

		
		Text patientDOBTitle = new Text("Date of Birth:");
		patientDOBTitle.setFont(Font.font("Arial", FontWeight.BOLD, 10));
		layoutPane.add(patientDOBTitle, 1, 3);
		dobField = new DatePicker();
		dobField.setPromptText( "MM/DD/YYYY" );
		layoutPane.add(dobField, 2, 3);
		
		Text patientEmailTitle = new Text("Email:");
		patientEmailTitle.setFont(Font.font("Arial", FontWeight.BOLD, 10));
		layoutPane.add(patientEmailTitle, 1, 4);
		emailField = new TextField();
		emailField.setPromptText( "someone@example.com" );
		layoutPane.add(emailField, 2, 4);

		
		Text patientPasswordTitle = new Text("Password:");
		patientPasswordTitle.setFont(Font.font("Arial", FontWeight.BOLD, 10));
		layoutPane.add(patientPasswordTitle, 1, 5);
		passwordField = new PasswordField();
		passwordField.setPromptText( "Password" );
		layoutPane.add(passwordField, 2, 5);
		
		
		Text patientConfirmPasswordTitle = new Text("Confirm Password:");
		patientConfirmPasswordTitle.setFont(Font.font("Arial", FontWeight.BOLD, 10));
		layoutPane.add(patientConfirmPasswordTitle, 1, 6);
		confirmPasswordField = new PasswordField();
		confirmPasswordField.setPromptText( "Confirm Password" );
		layoutPane.add(confirmPasswordField, 2, 6);
		
		
		ToggleGroup group = new ToggleGroup();

		patientButton = new RadioButton("Patient");
		patientButton.setToggleGroup(group);

		adminButton = new RadioButton("Admin");
		adminButton.setToggleGroup(group);
		adminButton.setSelected(true);
		
		doctorButton = new RadioButton("Doctor");
		doctorButton.setToggleGroup(group);
		
		HBox hBox = new HBox(patientButton, adminButton, doctorButton);
		layoutPane.add(hBox, 2, 7);
		
		final Text statusText = new Text();
		layoutPane.add(statusText, 1, 8, 2, 1);
		
		Button registerButton = new Button( "Register" );
		registerButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {

				String validMessage = areAllValid();
				if ( validMessage.trim().length() > 0 ) {
					
					PatientAppointmentUIUtils.showMessage(statusText, validMessage, Color.RED);
					return;
				}
				int nextId = 0;
				try {
					nextId = PatientAppointmentsDAO.INSTANCE.getMaxUserId() + 1;
				} catch (Exception e) {
					
					PatientAppointmentUIUtils.showMessage(statusText, e.getMessage(), Color.RED);
				}
				LocalDateTime localDate = dobField.getValue().atStartOfDay();
				ZonedDateTime zonedBirthDateTime = localDate.atZone(ZoneId.systemDefault());
				Instant instant = Instant.from(zonedBirthDateTime);
				Date date = Date.from(instant);
				String userType = "Y";
				if ( doctorButton.isSelected() ) {
					
					userType = "D";
				} else if (patientButton.isSelected()) {
					
					userType = "N";
				} else if (adminButton.isSelected()) {
					
					userType = "Y";
				}
				ApplicationUserVO applicationUserVO = new ApplicationUserVO(nextId, firstNameField.getText(), lastNameField.getText(), date, emailField.getText(), passwordField.getText(), userType);
				int rowsModified = 0;
				try {
					rowsModified = PatientAppointmentsDAO.INSTANCE.insertUser(applicationUserVO);
				} catch (Exception e) {
					
					PatientAppointmentUIUtils.showMessage(statusText, e.getMessage(), Color.RED);
				}
				if ( rowsModified == 1 ) {
					
					
					if ( !applicationUserVO.getIsAdmin().equals( "N" ) ) {
						
						PatientAppointmentsApplicationSession.INSTANCE.setCurrentSessionInfoVO(applicationUserVO);
						PatientAppointmentsApplicationSession.INSTANCE.getEventBus().post(applicationUserVO);
					}
					PatientAppointmentUIUtils.showMessage(statusText, "Thankyou for Registering!", Color.GREEN, new EventHandler<ActionEvent>() {
						
						@Override
						public void handle(ActionEvent event) {
							
							hide();
						}
					});
				}
			}
		});
		
		layoutPane.add(registerButton, 1, 9);
		getScene().setFill( Color.WHITE );
		getScene().setRoot(layoutPane);
		String cssDefault = "-fx-border-color: black;\n"
                + "-fx-border-insets: 2;\n"
                + "-fx-border-width: 1;\n"
                + "-fx-border-style: solid;\n";
		layoutPane.setStyle(cssDefault);
	}
	
	private String areAllValid() {
		
		if( firstNameField.getText().trim().length() > 0 &&
		lastNameField.getText().trim().length() > 0 &&
		emailField.getText().trim().length() > 0 &&
		passwordField.getText().trim().length() > 0 &&
		confirmPasswordField.getText().trim().length() > 0 &&
		dobField.getValue() != null ){
			
			if ( dobField.getValue().isAfter( LocalDate.now() ) ) {
				
				return "Patient Date of Birth can not be greater than today!";
			} else if ( !passwordField.getText().equals(confirmPasswordField.getText()) ) {
				
				return "Passwords do not match!";
			} else {
				
				return "";
			}
		} else {
			
			return "All fields are mandatory!";
		}
		
	}
}
