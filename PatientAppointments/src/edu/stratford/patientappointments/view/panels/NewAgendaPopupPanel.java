package edu.stratford.patientappointments.view.panels;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;

import edu.stratford.patientappointments.dao.PatientAppointmentsDAO;
import edu.stratford.patientappointments.model.valueobjects.ApplicationUserVO;
import edu.stratford.patientappointments.model.valueobjects.DoctorInfoVO;
import edu.stratford.patientappointments.model.valueobjects.PatientAppointmentInfoVO;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.PopupControl;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import jfxtras.scene.control.agenda.Agenda;

public class NewAgendaPopupPanel extends PopupControl {
	
	private ComboBox<ApplicationUserVO> patientChooser;
	
	private ComboBox<DoctorInfoVO> doctorChooser;
	
	private TextArea patientAilmentField;

	public static PatientAppointmentInfoVO createdAppointment;
	
	public NewAgendaPopupPanel( Stage primaryStage, final Agenda.LocalDateTimeRange dateTimeRange ) {

		super();
		sizeToScene();
		setHideOnEscape(true);
		setAutoHide(true);
		setAnchorLocation( AnchorLocation.WINDOW_TOP_LEFT );
		final GridPane layoutPane = new GridPane();
		layoutPane.setHgap(10);
		layoutPane.setVgap(10);
		layoutPane.setPadding(new Insets(10, 10, 10, 10));

		Text dialogTitle = new Text("Quick Appointment");
		dialogTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		layoutPane.add(dialogTitle, 1, 0, 2, 1);

		Text patientChooserTitle = new Text("For Patient:");
		patientChooserTitle.setFont(Font.font("Arial", FontWeight.BOLD, 10));
		layoutPane.add(patientChooserTitle, 1, 1);
		patientChooser = new ComboBox<>();
		patientChooser.setPromptText( "Select Patient" );
		layoutPane.add(patientChooser, 2, 1);
		Text doctorChooserTitle = new Text("With Doctor:");
		doctorChooserTitle.setFont(Font.font("Arial", FontWeight.BOLD, 10));
		layoutPane.add(doctorChooserTitle, 1, 2);
		doctorChooser = new ComboBox<>();
		doctorChooser.setPromptText( "Select Doctor" );
		layoutPane.add(doctorChooser, 2, 2);
		
		doctorChooser.setCellFactory(new Callback<ListView<DoctorInfoVO>, ListCell<DoctorInfoVO>>() {
			
			@Override
			public ListCell<DoctorInfoVO> call(ListView<DoctorInfoVO> param) {
			
				ListCell<DoctorInfoVO> cell = new ListCell<DoctorInfoVO>() {
					
					@Override
					protected void updateItem(DoctorInfoVO item, boolean empty) {

						super.updateItem(item, empty);
						if ( item != null ) {
							
							setText( item.getDoctorLastName() + ", " + item.getDoctorFirstName() );
						}
					}
				};
				return cell;
			}
			
			
		});
		
		
		
		try {
			
			doctorChooser.getItems().addAll(PatientAppointmentsDAO.INSTANCE.getAllDoctors());
			patientChooser.getItems().addAll(PatientAppointmentsDAO.INSTANCE.getAllPatients());
		} catch (Exception e) {

			e.printStackTrace();
		}
		
		Text patientSymptomTitle = new Text("Ailment/Symptoms:");
		patientSymptomTitle.setFont(Font.font("Arial", FontWeight.BOLD, 10));
		layoutPane.add(patientSymptomTitle, 1, 3);
		patientAilmentField = new TextArea("");
		patientAilmentField.setPromptText( "Briefly describe the ailment or symptoms" );
		layoutPane.add(patientAilmentField, 2, 3);
		
		Button createButton = new Button( "Create Appointment" );
		layoutPane.add(createButton, 1, 4);
		createButton.setOnAction( new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				
				LocalDateTime dayRequired = dateTimeRange.getStartLocalDateTime();
				LocalDate localDate = dayRequired.toLocalDate();
				ZonedDateTime zonedAppointmentDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
				Instant appointmentInstant = Instant.from(zonedAppointmentDateTime);
				Date dayRequiredForAppointment = Date.from(appointmentInstant);
				
				DoctorInfoVO selectedDoctor = doctorChooser.getSelectionModel().getSelectedItem();
				ApplicationUserVO selectedPatient = patientChooser.getSelectionModel().getSelectedItem();
				String appointmentId =  ( (int) selectedPatient.getMedicalRecordNumber() )+ "_" + Calendar.getInstance().getTimeInMillis();
				PatientAppointmentInfoVO appointmentInfoVO = new PatientAppointmentInfoVO(appointmentId, selectedPatient.getFirstName(), selectedPatient.getLastName(), selectedPatient.getDateOfBirth(), 
						selectedDoctor.getDoctorId(), dayRequiredForAppointment, dayRequired.getHour(), dayRequired.getMinute(), selectedPatient.getUserEmailAddress(), selectedPatient.getMedicalRecordNumber(), patientAilmentField.getText(), null, 0, 0, 1);
				try {
					int inserted = PatientAppointmentsDAO.INSTANCE.insertAppointment(appointmentInfoVO);
					if ( inserted == 1 ) {
						
						createdAppointment = appointmentInfoVO;
					} else {
						
						System.err.println( "Appointment " + appointmentInfoVO + " Could not be inserted" );
					}
					hide();
				} catch (Exception e) {

					e.printStackTrace();
				}
			}
		});
		
		getScene().setFill( Color.WHITE );
		getScene().setRoot(layoutPane);
		String cssDefault = "-fx-border-color: black;\n"
				+ "-fx-border-insets: 1;\n"
				+ "-fx-border-width: 1;\n"
				+ "-fx-border-style: solid;\n";
		layoutPane.setStyle(cssDefault);
	}
}
