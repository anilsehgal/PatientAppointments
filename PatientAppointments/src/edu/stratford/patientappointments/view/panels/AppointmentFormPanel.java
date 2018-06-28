package edu.stratford.patientappointments.view.panels;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.eventbus.Subscribe;

import edu.stratford.patientappointments.dao.PatientAppointmentsDAO;
import edu.stratford.patientappointments.model.valueobjects.ApplicationUserVO;
import edu.stratford.patientappointments.model.valueobjects.AppointmentCountVO;
import edu.stratford.patientappointments.model.valueobjects.DoctorInfoVO;
import edu.stratford.patientappointments.model.valueobjects.DoctorScheduleVO;
import edu.stratford.patientappointments.model.valueobjects.PatientAppointmentInfoVO;
import edu.stratford.patientappointments.model.valueobjects.PatientAppointmentsApplicationSession;
import edu.stratford.patientappointments.view.utils.PatientAppointmentUIUtils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Callback;

public class AppointmentFormPanel extends GridPane {

	private TextField patientMedicalRecordIdField;

	private TextField patientFirstNameField;

	private TextField patientLastNameField;

	private DatePicker patientDOBField;

	private TextField patientEmailField;

	private Spinner<Integer> minutesBox;

	private Spinner<Integer> hoursBox;

	private DatePicker appointmentDateField;

	private ComboBox<DoctorInfoVO> chooseDoctorField;

	private TextArea patientAilmentField;

	private Button searchButton;

	private Text messageText;

	private Button confirmAppointmentButton;

	private HBox appointmentTimeBox;

	private Map<LocalDateTime, Integer> appointmentCountMap = new HashMap<>();
	
	public AppointmentFormPanel() {

		super();
		PatientAppointmentsApplicationSession.INSTANCE.getEventBus().register(this);
		setHgap(10);
		setVgap(10);
		setPadding(new Insets(10, 10, 10, 10));

		Text patientRecordIdTitle = new Text("Medical Record Id:");
		patientRecordIdTitle.setFont(Font.font("Arial", FontWeight.BOLD, 10));
		add(patientRecordIdTitle, 1, 0);
		patientMedicalRecordIdField = new TextField();
		patientMedicalRecordIdField.setPromptText( "Medical Record Id" );
		patientMedicalRecordIdField.setEditable( false );
		add(patientMedicalRecordIdField, 2, 0);

		searchButton = new Button();
		Image searchImage = new Image( getClass().getClassLoader().getResourceAsStream( "edu/stratford/patientappointments/view/panels/images/Search.png" ), 15, 15, true, true );
		searchButton.setGraphic(new ImageView(searchImage));
		add(searchButton, 3, 0);
		searchButton.setVisible(false);
		searchButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				if ( patientMedicalRecordIdField.getText().trim().length() == 0 ) {

					PatientAppointmentUIUtils.showMessage(messageText,"No Medical Record number provided.", Color.RED);
				} else {

					ApplicationUserVO applicationUserVO = null;
					try {
						applicationUserVO = PatientAppointmentsDAO.INSTANCE.getPatientByRecordNumber(Double.parseDouble(patientMedicalRecordIdField.getText()));
					}  catch (Exception e) {

						PatientAppointmentUIUtils.showMessage(messageText,e.getMessage(), Color.RED);
					}
					if ( applicationUserVO != null ) {

						patientFirstNameField.setText( applicationUserVO.getFirstName() );
						patientLastNameField.setText( applicationUserVO.getLastName() );
						Date dob = applicationUserVO.getDateOfBirth();
						patientDOBField.setValue( dob.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() );
						patientEmailField.setText( applicationUserVO.getUserEmailAddress() );
						patientFirstNameField.setEditable(false);
						patientLastNameField.setEditable(false);
						patientDOBField.setEditable(false);
						patientEmailField.setEditable(false);
					} else {

						PatientAppointmentUIUtils.showMessage(messageText,"No Medical Record found for " + patientMedicalRecordIdField.getText(), Color.RED);
					}
				}
			}
		});

		Text patientFirstNameTitle = new Text("First Name:");
		patientFirstNameTitle.setFont(Font.font("Arial", FontWeight.BOLD, 10));
		add(patientFirstNameTitle, 1, 1);
		patientFirstNameField = new TextField();
		patientFirstNameField.setPromptText( "First Name" );
		add(patientFirstNameField, 2, 1);


		Text patientLastNameTitle = new Text("Last Name:");
		patientLastNameTitle.setFont(Font.font("Arial", FontWeight.BOLD, 10));
		add(patientLastNameTitle, 1, 2);
		patientLastNameField = new TextField();
		patientLastNameField.setPromptText( "Last Name" );
		add(patientLastNameField, 2, 2);


		Text patientDOBTitle = new Text("Date of Birth:");
		patientDOBTitle.setFont(Font.font("Arial", FontWeight.BOLD, 10));
		add(patientDOBTitle, 1, 3);
		patientDOBField = new DatePicker();
		patientDOBField.setPromptText( "MM/DD/YYYY" );
		add(patientDOBField, 2, 3);

		patientDOBField.setDayCellFactory(new Callback<DatePicker, DateCell>() {

			@Override
			public DateCell call(final DatePicker datePicker) {
				return new DateCell() {
					@Override
					public void updateItem(LocalDate item, boolean empty) {
						super.updateItem(item, empty);

						if (item.isAfter( LocalDate.now() ) ) {

							setDisable(true);
							setStyle("-fx-background-color: 'silver';");
						} 
					}
				};
			}
		});

		messageText = new Text();
		add(messageText, 1, 10, 2, 1);

		Text doctorTitle = new Text("Select Doctor:");
		doctorTitle.setFont(Font.font("Arial", FontWeight.BOLD, 10));
		add(doctorTitle, 1, 4);
		chooseDoctorField = new ComboBox<DoctorInfoVO>();
		chooseDoctorField.setPromptText( "Select Doctor" );
		chooseDoctorField.setCellFactory(new Callback<ListView<DoctorInfoVO>, ListCell<DoctorInfoVO>>() {
			@Override
			public ListCell<DoctorInfoVO> call(ListView<DoctorInfoVO> param) {

				return new ListCell<DoctorInfoVO>(){
					@Override
					public void updateItem( DoctorInfoVO doctor, boolean empty ) {

						super.updateItem( doctor, empty );
						if(!empty) {

							Image icon = null;
							String maleIconPath = "edu/stratford/patientappointments/view/panels/images/DoctorMale.png";
							String femaleIconPath = "edu/stratford/patientappointments/view/panels/images/DoctorFemale.png";
							if ( "Female".equalsIgnoreCase( doctor.getDoctorGender() ) ) {

								icon = new Image(getClass().getClassLoader().getResourceAsStream(femaleIconPath));
							} else {

								icon = new Image(getClass().getClassLoader().getResourceAsStream(maleIconPath));
							}

							ImageView iconImageView = new ImageView(icon);
							iconImageView.setFitHeight(30);
							iconImageView.setPreserveRatio(true);
							HBox hBox = new HBox();
							VBox vBox = new VBox();
							vBox.getChildren().add( new Text(doctor.getDoctorLastName() + ", " + doctor.getDoctorFirstName()) );
							Text text = new Text(doctor.getDoctorSpecialization() + " (" + doctor.getDoctorDescription() + ")");
							text.setFont(Font.font("Arial", FontWeight.NORMAL, 9));
							text.setFill(Color.web("#ABCDEF"));
							vBox.getChildren().add( text );
							hBox.getChildren().add(iconImageView);
							hBox.getChildren().add(vBox);
							setGraphic(hBox);
						} else {

							setText( "" );
						}
					}
				};
			}
		});

		try {
			chooseDoctorField.getItems().addAll(PatientAppointmentsDAO.INSTANCE.getAllDoctors());
		} catch (Exception e) {

			e.printStackTrace();
			PatientAppointmentUIUtils.showMessage(messageText,e.getMessage(), Color.RED);
		}
		add(chooseDoctorField, 2, 4);

		chooseDoctorField.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<DoctorInfoVO>() {

			@Override
			public void changed(ObservableValue<? extends DoctorInfoVO> observable, DoctorInfoVO oldValue,
					DoctorInfoVO newValue) {

				appointmentDateField.setValue(null);
				appointmentCountMap.clear();
				if ( newValue != null ) {
					try {
						
						List<AppointmentCountVO> appointments = PatientAppointmentsDAO.INSTANCE.getBookedAppointments(newValue.getDoctorId());
						for (AppointmentCountVO appointment : appointments) {
							
							appointmentCountMap.put(appointment.getScheduleDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(), appointment.getAppointmentCount());
						}
					} catch (Exception e) {
						
						e.printStackTrace();
					}
				}
			}
		});

		Text appointmentDateTitle = new Text("Appointment Date:");
		appointmentDateTitle.setFont(Font.font("Arial", FontWeight.BOLD, 10));
		add(appointmentDateTitle, 1, 5);
		appointmentDateField = new DatePicker();
		appointmentDateField.setPromptText( "MM/DD/YYYY" );
		appointmentDateField.setEditable(false);
		add(appointmentDateField, 2, 5);
		
		appointmentDateField.setDayCellFactory(new Callback<DatePicker, DateCell>() {
			@Override
			public DateCell call(final DatePicker datePicker) {
				return new DateCell() {
					@Override
					public void updateItem(LocalDate item, boolean empty) {
						super.updateItem(item, empty);

						if (item.isBefore( LocalDate.now().plusDays( 1 ) ) ) {
							setDisable(true);
							setStyle("-fx-background-color: 'silver';");
						} else {

							DoctorInfoVO selectedDoctor = chooseDoctorField.getSelectionModel().getSelectedItem();
							if ( selectedDoctor != null ) {

								int doctorHolidayCode = selectedDoctor.getWeeklyHolidayCode();
								if (item.getDayOfWeek().getValue() == doctorHolidayCode) {

									setDisable(true);
									setStyle("-fx-background-color: 'silver';");
								} else {
									
									final ProgressBar pb = new ProgressBar();
									pb.setPrefWidth(30);
									pb.setPrefHeight(2);
									
									
									double d = appointmentCountMap.get(item.atStartOfDay())==null?0:(double)appointmentCountMap.get(item.atStartOfDay());
									pb.progressProperty().addListener(new ChangeListener<Number>() {

										@Override
										public void changed(ObservableValue<? extends Number> observable, Number oldValue,
												Number newValue) {

											double progress = newValue == null ? 0 : newValue.doubleValue();
											pb.getStyleClass().removeAll(RED_BAR, ORANGE_BAR, YELLOW_BAR, GREEN_BAR);
											if (progress < 0.35) {
												
												pb.getStyleClass().add( GREEN_BAR );
												availability = "Good";
											} else if (progress < 0.5) {
												
												pb.getStyleClass().add( YELLOW_BAR );
												availability = "Medium";
											} else if (progress < 0.7) {
												
												pb.getStyleClass().add( ORANGE_BAR );
												availability = "Low";
											} else {
												
												pb.getStyleClass().add( RED_BAR );
												availability = "Very Low";
											}
										}
									});
									
									VBox box = new VBox();
									box.setAlignment(Pos.CENTER);
									box.getChildren().add(new Text(getText()));
									box.getChildren().add(pb);
									
									setGraphic(box);
									setText("");
									
									pb.setProgress((float)d/5f);
									
									setTooltip( new Tooltip( "Request Appointment:\nDr. " + selectedDoctor.getDoctorLastName() + ", " + selectedDoctor.getDoctorFirstName() + "\nfor " + item.format(DateTimeFormatter.ISO_LOCAL_DATE ) + "\n" + "Availability: " + availability ) );
								}
							} else {

								setDisable(true);
								setStyle("-fx-background-color: 'silver';");
							}

						}
					}
				};
			}
		});

		appointmentDateField.valueProperty().addListener(new ChangeListener<LocalDate>() {

			@Override
			public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue,
					LocalDate newValue) {

				if ( newValue != null && !newValue.equals(oldValue) ) {

					LocalDateTime localAppointmentDate = newValue.atStartOfDay();
					ZonedDateTime zonedAppointmentDateTime = localAppointmentDate.atZone(ZoneId.systemDefault());
					Instant appointmentInstant = Instant.from(zonedAppointmentDateTime);
					Date appointmentDate = Date.from(appointmentInstant);
					DoctorInfoVO doctorInfoVO = chooseDoctorField.getValue();
					if ( doctorInfoVO != null ) {

						DoctorScheduleVO scheduleForDate = new DoctorScheduleVO(doctorInfoVO.getDoctorId(), appointmentDate, 0, 0);
						try {
							DoctorScheduleVO doctorScheduleVO = PatientAppointmentsDAO.INSTANCE.getDoctorScheduleForDate(scheduleForDate);
							if ( doctorScheduleVO != null ) {

								appointmentTimeBox.getChildren().remove(hoursBox);
								hoursBox = new Spinner<Integer>( doctorScheduleVO.getInTime(), doctorScheduleVO.getOutTime(), doctorScheduleVO.getInTime() );
								hoursBox.setPrefWidth( 60 );
								appointmentTimeBox.getChildren().add( 0, hoursBox );
							}
						} catch (Exception e) {

							PatientAppointmentUIUtils.showMessage(messageText,e.getMessage(), Color.RED);
						}
					}
				}
			}
		});

		Text appointmentTimeTitle = new Text("Appointment Time (HH:MM):");
		appointmentTimeTitle.setFont(Font.font("Arial", FontWeight.BOLD, 10));
		add(appointmentTimeTitle, 1, 6);
		appointmentTimeBox = new HBox();
		hoursBox = new Spinner<Integer>( 9, 20, 9 );
		hoursBox.setPrefWidth( 60 );

		List<Integer> minList = Arrays.asList(new Integer[]{0, 15,30, 45});
		minutesBox = new Spinner<Integer>( FXCollections.observableList( minList ) );
		minutesBox.setPrefWidth( 60 );
		appointmentTimeBox.getChildren().add( hoursBox );
		Text sepText = new Text( ":" );
		sepText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		appointmentTimeBox.getChildren().add( sepText );
		appointmentTimeBox.getChildren().add( minutesBox );
		add(appointmentTimeBox, 2, 6);


		Text patientEmailTitle = new Text("Email:");
		patientEmailTitle.setFont(Font.font("Arial", FontWeight.BOLD, 10));
		add(patientEmailTitle, 1, 7);
		patientEmailField = new TextField();
		patientEmailField.setPromptText( "someone@example.com" );
		add(patientEmailField, 2, 7);

		Text patientSymptomTitle = new Text("Ailment/Symptoms:");
		patientSymptomTitle.setFont(Font.font("Arial", FontWeight.BOLD, 10));
		add(patientSymptomTitle, 1, 8);
		patientAilmentField = new TextArea("");
		patientAilmentField.setPromptText( "Briefly describe the ailment or symptoms" );
		add(patientAilmentField, 2, 8);



		confirmAppointmentButton = new Button( "Confirm Appointment" );
		add(confirmAppointmentButton, 2, 9);

		confirmAppointmentButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				if ( PatientAppointmentsApplicationSession.INSTANCE.getCurrentSessionInfoVO() != null ) {

					if ( allAllValid().trim().length() > 0 ) {

						PatientAppointmentUIUtils.showMessage(messageText,allAllValid(), Color.RED);
						return;
					}

					LocalDateTime localBirthDate = patientDOBField.getValue().atStartOfDay();
					ZonedDateTime zonedBirthDateTime = localBirthDate.atZone(ZoneId.systemDefault());
					Instant birthInstant = Instant.from(zonedBirthDateTime);
					Date birthDate = Date.from(birthInstant);

					LocalDateTime localAppointmentDate = appointmentDateField.getValue().atStartOfDay();
					ZonedDateTime zonedAppointmentDateTime = localAppointmentDate.atZone(ZoneId.systemDefault());
					Instant appointmentInstant = Instant.from(zonedAppointmentDateTime);
					Date appointmentDate = Date.from(appointmentInstant);

					PatientAppointmentInfoVO patientAppointmentInfoVO = new PatientAppointmentInfoVO(patientMedicalRecordIdField.getText() + "_" + Calendar.getInstance().getTimeInMillis(), patientFirstNameField.getText(), patientLastNameField.getText(), birthDate, chooseDoctorField.getSelectionModel().getSelectedItem().getDoctorId(), appointmentDate, hoursBox.getValue(), minutesBox.getValue(), patientEmailField.getText(), Double.parseDouble(patientMedicalRecordIdField.getText()), patientAilmentField.getText(), null, 0, 0, 1);
					try {
						PatientAppointmentsDAO.INSTANCE.insertAppointment(patientAppointmentInfoVO);
					} catch (Exception e) {
						e.printStackTrace();
						PatientAppointmentUIUtils.showMessage(messageText, e.getMessage(), Color.RED);
					}
					PatientAppointmentsApplicationSession.INSTANCE.getEventBus().post(patientAppointmentInfoVO);
					PatientAppointmentUIUtils.showMessage(messageText, "Appointment Booked Successfully!", Color.GREEN);
				} else {

					/*Alert alert = new Alert(AlertType.WARNING, "You need to log in to create appointments!", ButtonType.OK);
					Optional<ButtonType> result = alert.showAndWait();
					if (result.isPresent() && result.get() == ButtonType.OK) {

						// open sign in dialog
					}*/
					PatientAppointmentUIUtils.showMessage(messageText, "You need to log in to create appointments! \nClick on Sign In Menu!", Color.RED);
				}
			}
		});
	}

	public String allAllValid() {

		if( patientFirstNameField.getText().trim().length() > 0 &&
				patientLastNameField.getText().trim().length() > 0 &&
				patientDOBField.getValue() != null &&
				patientDOBField.getValue().isBefore(LocalDate.now()) &&
				chooseDoctorField.getSelectionModel().getSelectedItem() != null && 
				appointmentDateField.getValue() != null &&
				appointmentDateField.getValue().isAfter(LocalDate.now()) &&
				patientEmailField.getText().trim().length() > 0 &&
				patientMedicalRecordIdField.getText().trim().length() > 0 &&
				patientAilmentField.getText().trim().length() > 0) {

			return "";
		} else {

			return "All fields are mandatory!";
		}
	}

	@Subscribe
	public void rerender(ApplicationUserVO applicationUserVO) {

		if ( applicationUserVO != null && "N".equals( applicationUserVO.getIsAdmin() ) ) {

			patientMedicalRecordIdField.setText( applicationUserVO.getMedicalRecordNumber() + "" );
			patientFirstNameField.setText( applicationUserVO.getFirstName() );
			patientLastNameField.setText( applicationUserVO.getLastName() );
			Date dob = applicationUserVO.getDateOfBirth();
			patientDOBField.setValue( dob.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() );
			patientEmailField.setText( applicationUserVO.getUserEmailAddress() );
			patientMedicalRecordIdField.setEditable(false);
			patientFirstNameField.setEditable(false);
			patientLastNameField.setEditable(false);
			patientDOBField.setEditable(false);
			patientEmailField.setEditable(false);
			searchButton.setVisible(false);
		} else if ( "Y".equals( applicationUserVO.getIsAdmin() ) ) {

			patientMedicalRecordIdField.setEditable(true);
			patientFirstNameField.setEditable(true);
			patientLastNameField.setEditable(true);
			patientDOBField.setEditable(true);
			patientEmailField.setEditable(true);
			searchButton.setVisible(true);
		} else if ("D".equals( applicationUserVO.getIsAdmin() )) {
			
			patientMedicalRecordIdField.setEditable(true);
			patientFirstNameField.setEditable(true);
			patientLastNameField.setEditable(true);
			patientDOBField.setEditable(true);
			patientEmailField.setEditable(true);
			searchButton.setVisible(true);
			String doctorId = "DOC-" + ( ( int ) applicationUserVO.getMedicalRecordNumber() );
			List<DoctorInfoVO> allItems = new ArrayList<>(chooseDoctorField.getItems());
			chooseDoctorField.getItems().clear();
			for ( DoctorInfoVO anItem : allItems ) {
				
				if ( anItem.getDoctorId().equals(doctorId) ) {
					
					chooseDoctorField.setPromptText("Select Doctor");
					chooseDoctorField.getItems().add( anItem );					
					break;
				}
			}
		}
	}
	@Subscribe
	public void rerender(String s) {

		patientMedicalRecordIdField.setText( "" );
		patientFirstNameField.setText( "" );
		patientLastNameField.setText( "" );
		patientDOBField.setValue( LocalDate.now() );
		patientEmailField.setText( "" );
		appointmentDateField.setValue( LocalDate.now() );
		patientAilmentField.setText("");
		
		try {
			
			chooseDoctorField.getItems().clear();
			chooseDoctorField.getItems().addAll(PatientAppointmentsDAO.INSTANCE.getAllDoctors());
		} catch (Exception e) {

			e.printStackTrace();
			PatientAppointmentUIUtils.showMessage(messageText, e.getMessage(), Color.RED);
		}
		patientMedicalRecordIdField.setEditable(false);
		patientFirstNameField.setEditable(true);
		patientLastNameField.setEditable(true);
		patientDOBField.setEditable(true);
		patientEmailField.setEditable(true);
		searchButton.setVisible(false);
	}


	private String availability = "";
	private static final String RED_BAR    = "red-bar";
	private static final String YELLOW_BAR = "yellow-bar";
	private static final String ORANGE_BAR = "orange-bar";
	private static final String GREEN_BAR  = "green-bar";
}
