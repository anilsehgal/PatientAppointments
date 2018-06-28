package edu.stratford.patientappointments.view.panels;

import java.text.SimpleDateFormat;

import javax.mail.MessagingException;

import edu.stratford.patientappointments.dao.PatientAppointmentsDAO;
import edu.stratford.patientappointments.model.valueobjects.PatientAppointmentInfoVO;
import edu.stratford.patientappointments.service.ReminderSender;
import edu.stratford.patientappointments.view.utils.PatientAppointmentUIUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.PopupControl;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class EmailContentPopup extends PopupControl {
	
	private Stage primaryStage;
	private TextArea emailArea;
	private TextField toField;
	
	public EmailContentPopup( Stage primaryStage ) {
		
		super();
		this.primaryStage = primaryStage;
		sizeToScene();
		setHideOnEscape(true);
		setAutoHide(true);
		toField = new TextField();
		BorderPane layoutPane = new BorderPane();
		
		GridPane topPane = new GridPane();
		topPane.prefWidthProperty().bind(prefWidthProperty());
		topPane.add(new Text("To: "), 0, 0);
		topPane.add(toField, 1, 0);
		toField.prefWidthProperty().bind(prefWidthProperty().subtract(25));
		
		HBox hbox = new HBox(20);
		final Text mailMessage = new Text();
		emailArea = new TextArea();
		emailArea.prefWidthProperty().bind(prefWidthProperty());
		emailArea.prefHeightProperty().bind(prefHeightProperty().subtract(25));
		Button sendButton = new Button("Send Email");
		
		sendButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {

				try {
					ReminderSender.INSTANCE.generateAndSendEmail("anil.sehgal10@gmail.com", "anil.sehgal10@gmail.com", "anil.sehgal10@gmail.com", "Health and Wellness: Appointment Reminder", emailArea.getText());
					
					PatientAppointmentUIUtils.showMessage(mailMessage, "Email Sent Successfully!", Color.GREEN, new EventHandler<ActionEvent>() {
						
						@Override
						public void handle(ActionEvent event) {

							mailMessage.setText("");
							hide();
						}
					}); 
				} catch (MessagingException e) {

					PatientAppointmentUIUtils.showMessage(mailMessage, e.getMessage(), Color.RED);
				}
			}
		});
		hbox.getChildren().addAll(sendButton, mailMessage);
		layoutPane.setTop(topPane);
		layoutPane.setCenter(emailArea);
		layoutPane.setBottom(hbox );
		getScene().setFill( Color.WHITE );
		getScene().setRoot(layoutPane);
		String cssDefault = "-fx-border-color: black;\n"
				+ "-fx-border-insets: 1;\n"
				+ "-fx-border-width: 1;\n"
				+ "-fx-border-style: solid;\n";
		layoutPane.setStyle(cssDefault);
		setPrefSize(400, 400);
		
	}
	
	void showPopup( PatientAppointmentInfoVO appointmentInfoVO ) {
		
		show( primaryStage );
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		String appointmentTimeHours = appointmentInfoVO.getAppointmentTimeHours() > 9?appointmentInfoVO.getAppointmentTimeHours() + "": "0" + appointmentInfoVO.getAppointmentTimeHours();
		String appointmentTimeMinutes = appointmentInfoVO.getAppointmentTimeMinutes() > 9?appointmentInfoVO.getAppointmentTimeMinutes() + "": "0" + appointmentInfoVO.getAppointmentTimeMinutes();
		String appointmentDate = dateFormat.format(appointmentInfoVO.getDayRequiredForAppointment()) + " " + appointmentTimeHours + ":" + appointmentTimeMinutes;
		
		String text = "Dear " + appointmentInfoVO.getFirstName() + "\n\n\n";
		text += "This is to remind you of your upcoming following appointment:\n\n\t";
		
		try {
			text += "  ID: " + appointmentInfoVO.getAppointmentId() + "\n\t";
			text += "With: " + PatientAppointmentsDAO.INSTANCE.getDoctorById(appointmentInfoVO.getDoctorId()).toString() + "\n\t";
		} catch (Exception e) {
			
			e.printStackTrace();
			text += "With: " + appointmentInfoVO.getDoctorId() + "\n\t";
		}
		
		text += "  At: " + appointmentDate + "\n\t";
		text += " For: " + appointmentInfoVO.getAilmentDescription() + "\n\n\n";
		text += "Best Regards,\n";
		text += "Health and Wellness Team.";
		
		toField.setText(appointmentInfoVO.getPatientEmailAddress() + ";");
		emailArea.setText(text);
	}
}
