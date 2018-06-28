package edu.stratford.patientappointments.view.panels;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.common.eventbus.Subscribe;

import edu.stratford.patientappointments.dao.PatientAppointmentsDAO;
import edu.stratford.patientappointments.model.valueobjects.DoctorInfoVO;
import edu.stratford.patientappointments.model.valueobjects.PatientAppointmentInfoVO;
import edu.stratford.patientappointments.model.valueobjects.PatientAppointmentsApplicationSession;
import edu.stratford.patientappointments.view.utils.PatientAppointmentUIUtils;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

public class HospitalAppointmentViewerPanel extends BorderPane {

	TableView<PatientAppointmentInfoVO> table;
	
	Text messageText;
	
	Stage primaryStage; 
	
	ObservableList<PatientAppointmentInfoVO> allAppointments;
	
	Map<String, DoctorInfoVO> doctorsMap = new HashMap<>();
	
	private final Image blueImage = new Image( getClass().getClassLoader().getResourceAsStream( "edu/stratford/patientappointments/view/panels/images/blue.png" ), 25, 25, true, true );
	private final Image redImage = new Image( getClass().getClassLoader().getResourceAsStream( "edu/stratford/patientappointments/view/panels/images/red.png" ), 25, 25, true, true );
	private final Image grayImage = new Image( getClass().getClassLoader().getResourceAsStream( "edu/stratford/patientappointments/view/panels/images/gray.png" ), 25, 25, true, true );
	private final Image yellowImage = new Image( getClass().getClassLoader().getResourceAsStream( "edu/stratford/patientappointments/view/panels/images/yellow.png" ), 25, 25, true, true );
	private final Image greenImage = new Image( getClass().getClassLoader().getResourceAsStream( "edu/stratford/patientappointments/view/panels/images/green.png" ), 25, 25, true, true );
	
	
	public HospitalAppointmentViewerPanel(Stage primaryStage) {

		super();
		this.primaryStage = primaryStage;
		PatientAppointmentsApplicationSession.INSTANCE.getEventBus().register(this);
		Text medicalRecordNoText = new Text( "All Appointments" );
		medicalRecordNoText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		HBox hBox = new HBox();
		hBox.getChildren().add(medicalRecordNoText);
		Button refresher = new Button("Refresh");
		refresher.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				
			//	table.getItems().clear();

				try {
					allAppointments.clear();
					
					allAppointments.addAll( PatientAppointmentsDAO.INSTANCE.getAllAppointments() );
					table.refresh();
				} catch (Exception e) {
					
					PatientAppointmentUIUtils.showMessage(messageText, e.getMessage(), Color.RED); 
				}
			}
		});
		hBox.getChildren().add(refresher);
		
		try {
			
			allAppointments = FXCollections.observableArrayList(PatientAppointmentsDAO.INSTANCE.getAllAppointments());
		} catch (Exception e) {

			PatientAppointmentUIUtils.showMessage(messageText, e.getMessage(), Color.RED); 
		}
		
		
		table = new TableView<>(allAppointments);
		table.setEditable(false);
			
		TableColumn<PatientAppointmentInfoVO, PatientAppointmentInfoVO> statusColumn = new TableColumn<>("Status");
		TableColumn<PatientAppointmentInfoVO, String> firstNameColumn = new TableColumn<>("First Name");
		TableColumn<PatientAppointmentInfoVO, String> lastNameColumn = new TableColumn<>("Last Name");
		TableColumn<PatientAppointmentInfoVO, Date> dateOfBirthColumn = new TableColumn<>("Date of birth");
		TableColumn<PatientAppointmentInfoVO, String> doctorNameColumn = new TableColumn<>("Name of doctor");
		TableColumn<PatientAppointmentInfoVO, Date> appointmentDateColumn = new TableColumn<>("Appointment Date");
		TableColumn<PatientAppointmentInfoVO, String> appointmentTimeColumn = new TableColumn<>("Appointment Time");
		TableColumn<PatientAppointmentInfoVO, String> ailmentColumn = new TableColumn<>("Ailment");
		TableColumn<PatientAppointmentInfoVO, Date> rescheduleDateColumn = new TableColumn<>("Reschedule Date");
		TableColumn<PatientAppointmentInfoVO, String> rescheduleTimeColumn = new TableColumn<>("Reschedule Time");
		TableColumn<PatientAppointmentInfoVO, PatientAppointmentInfoVO> emailColumn = new TableColumn<>( "Email Reminder" );
		TableColumn<PatientAppointmentInfoVO, PatientAppointmentInfoVO> actionColumn = new TableColumn<>( "Action" );

		statusColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PatientAppointmentInfoVO, PatientAppointmentInfoVO>, ObservableValue<PatientAppointmentInfoVO>>() {
			@Override
			public ObservableValue<PatientAppointmentInfoVO> call(TableColumn.CellDataFeatures<PatientAppointmentInfoVO, PatientAppointmentInfoVO> p) {
				return new SimpleObjectProperty<PatientAppointmentInfoVO>(p.getValue());
			}
		});
		
		statusColumn.setCellFactory(new Callback<TableColumn<PatientAppointmentInfoVO,PatientAppointmentInfoVO>, TableCell<PatientAppointmentInfoVO,PatientAppointmentInfoVO>>() {
			
			@Override
			public TableCell<PatientAppointmentInfoVO, PatientAppointmentInfoVO> call(TableColumn<PatientAppointmentInfoVO, PatientAppointmentInfoVO> param) {
				
				TableCell<PatientAppointmentInfoVO, PatientAppointmentInfoVO> tableCell = new TableCell<PatientAppointmentInfoVO, PatientAppointmentInfoVO>() {
					
					@Override
					protected void updateItem(final PatientAppointmentInfoVO item, boolean empty) {

						super.updateItem(item, empty);
						if ( item != null ) {
							
							setText("");
							HBox hBox = new HBox(10);
							if (item.getAppointmentStatus() == 1) {
								
								hBox.getChildren().addAll(new ImageView(blueImage), new Text("New"));
							} else if (item.getAppointmentStatus() == 5) {
								
								hBox.getChildren().addAll(new ImageView(greenImage), new Text("Approved"));
							} else if (item.getAppointmentStatus() == 10) {
								
								hBox.getChildren().addAll(new ImageView(redImage), new Text("Rejected"));
							} else if (item.getAppointmentStatus() == 15) {
								
								hBox.getChildren().addAll(new ImageView(yellowImage), new Text("Reschedule Requested"));
							} else if (item.getAppointmentStatus() == 20) {
								
								hBox.getChildren().addAll(new ImageView(grayImage), new Text("Cancelled By Patient"));
							}
							setGraphic(hBox);
						} else {
							
							setGraphic(null);
						}
					}
				};
				return tableCell;
			}
		});
		
		firstNameColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PatientAppointmentInfoVO, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(TableColumn.CellDataFeatures<PatientAppointmentInfoVO, String> p) {
				return new SimpleStringProperty(p.getValue().getFirstName());
			}
		});
		
		ailmentColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PatientAppointmentInfoVO, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(TableColumn.CellDataFeatures<PatientAppointmentInfoVO, String> p) {
				return new SimpleStringProperty(p.getValue().getAilmentDescription());
			}
		});
		ailmentColumn.setPrefWidth(150);
		ailmentColumn.setCellFactory(new Callback<TableColumn<PatientAppointmentInfoVO,String>, TableCell<PatientAppointmentInfoVO,String>>() {
			
			@Override
			public TableCell<PatientAppointmentInfoVO, String> call(TableColumn<PatientAppointmentInfoVO, String> param) {
				TableCell<PatientAppointmentInfoVO, String> cell = new TableCell<PatientAppointmentInfoVO, String>() {
					
					private Text label;
					@Override
					public void updateItem(String item, boolean empty) {
						
						super.updateItem(item, empty);
						if (!isEmpty()) {
							label = new Text(item.toString());
							label.setWrappingWidth(140);
							setGraphic(label);
						}
					}
				};
				return cell;
			}
		});

		lastNameColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PatientAppointmentInfoVO, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(TableColumn.CellDataFeatures<PatientAppointmentInfoVO, String> p) {
				return new SimpleStringProperty(p.getValue().getLastName());
			}
		});

		dateOfBirthColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PatientAppointmentInfoVO, Date>, ObservableValue<Date>>() {
			@Override
			public ObservableValue<Date> call(TableColumn.CellDataFeatures<PatientAppointmentInfoVO, Date> p) {
				return new SimpleObjectProperty<>(p.getValue().getDateOfBirth());
			}


		});

		dateOfBirthColumn.setCellFactory(new Callback<TableColumn<PatientAppointmentInfoVO,Date>, TableCell<PatientAppointmentInfoVO,Date>>() {

			@Override
			public TableCell<PatientAppointmentInfoVO, Date> call(TableColumn<PatientAppointmentInfoVO, Date> param) {

				TableCell<PatientAppointmentInfoVO, Date> tableCell = new TableCell<PatientAppointmentInfoVO, Date>(){

					@Override
					protected void updateItem(Date item, boolean empty) {

						super.updateItem(item, empty);
						if ( item != null ) {

							SimpleDateFormat dateFormat = new SimpleDateFormat( "MM/dd/yyyy" );
							setText( dateFormat.format(item) );
						} else {

							setText("");
						}
					}
				};
				return tableCell;
			}
		});



		doctorNameColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PatientAppointmentInfoVO, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(TableColumn.CellDataFeatures<PatientAppointmentInfoVO, String> p) {
				
				try {
					if (!doctorsMap.containsKey(p.getValue().getDoctorId())) {
						
						doctorsMap.put(p.getValue().getDoctorId(), PatientAppointmentsDAO.INSTANCE.getDoctorById(p.getValue().getDoctorId()));
					}
					return new SimpleStringProperty(doctorsMap.get(p.getValue().getDoctorId()).toString());
				} catch (Exception e) {
					
					e.printStackTrace();
					return new SimpleStringProperty(p.getValue().getDoctorId());
				}
			}
		});

		appointmentDateColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PatientAppointmentInfoVO, Date>, ObservableValue<Date>>() {
			@Override
			public ObservableValue<Date> call(TableColumn.CellDataFeatures<PatientAppointmentInfoVO, Date> p) {
				return new SimpleObjectProperty<>(p.getValue().getDayRequiredForAppointment());
			}
		});

		appointmentDateColumn.setCellFactory(new Callback<TableColumn<PatientAppointmentInfoVO,Date>, TableCell<PatientAppointmentInfoVO,Date>>() {

			@Override
			public TableCell<PatientAppointmentInfoVO, Date> call(TableColumn<PatientAppointmentInfoVO, Date> param) {

				TableCell<PatientAppointmentInfoVO, Date> tableCell = new TableCell<PatientAppointmentInfoVO, Date>(){

					@Override
					protected void updateItem(Date item, boolean empty) {

						super.updateItem(item, empty);
						if ( item != null ) {

							SimpleDateFormat dateFormat = new SimpleDateFormat( "MM/dd/yyyy" );
							setText( dateFormat.format(item) );
						} else {

							setText("");
						}
					}
				};
				return tableCell;
			}
		});

		appointmentTimeColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PatientAppointmentInfoVO, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(TableColumn.CellDataFeatures<PatientAppointmentInfoVO, String> p) {
				
				String appointmentTimeHours = p.getValue().getAppointmentTimeHours() > 9?p.getValue().getAppointmentTimeHours() + "": "0" + p.getValue().getAppointmentTimeHours();
				String appointmentTimeMinutes = p.getValue().getAppointmentTimeMinutes() > 9?p.getValue().getAppointmentTimeMinutes() + "": "0" + p.getValue().getAppointmentTimeMinutes();
				return new SimpleStringProperty(appointmentTimeHours + ":" + appointmentTimeMinutes);
			}
		});
		
		rescheduleDateColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PatientAppointmentInfoVO, Date>, ObservableValue<Date>>() {
			@Override
			public ObservableValue<Date> call(TableColumn.CellDataFeatures<PatientAppointmentInfoVO, Date> p) {
				return new SimpleObjectProperty<>(p.getValue().getRescheduleDate());
			}
		});

		rescheduleDateColumn.setCellFactory(new Callback<TableColumn<PatientAppointmentInfoVO,Date>, TableCell<PatientAppointmentInfoVO,Date>>() {

			@Override
			public TableCell<PatientAppointmentInfoVO, Date> call(TableColumn<PatientAppointmentInfoVO, Date> param) {

				TableCell<PatientAppointmentInfoVO, Date> tableCell = new TableCell<PatientAppointmentInfoVO, Date>(){

					@Override
					protected void updateItem(Date item, boolean empty) {

						super.updateItem(item, empty);
						if ( item != null ) {

							SimpleDateFormat dateFormat = new SimpleDateFormat( "MM/dd/yyyy" );
							setText( dateFormat.format(item) );
						} else {

							setText("");
						}
					}
				};
				return tableCell;
			}
		});
		
		rescheduleTimeColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PatientAppointmentInfoVO, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(TableColumn.CellDataFeatures<PatientAppointmentInfoVO, String> p) {
				
				if (p.getValue().getRescheduleDate() != null) {
					
					String rescheduleTimeHours = p.getValue().getRescheduleTimeHours() > 9?p.getValue().getRescheduleTimeHours() + "": "0" + p.getValue().getRescheduleTimeHours();
					String rescheduleTimeMinutes = p.getValue().getRescheduleTimeMinutes() > 9?p.getValue().getRescheduleTimeMinutes() + "": "0" + p.getValue().getRescheduleTimeMinutes();
					return new SimpleStringProperty(rescheduleTimeHours + ":" + rescheduleTimeMinutes);
				} else {
					
					return new SimpleStringProperty("");
				}
			}
		});

		emailColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PatientAppointmentInfoVO, PatientAppointmentInfoVO>, ObservableValue<PatientAppointmentInfoVO>>() {
			@Override
			public ObservableValue<PatientAppointmentInfoVO> call(TableColumn.CellDataFeatures<PatientAppointmentInfoVO, PatientAppointmentInfoVO> p) {
				return new SimpleObjectProperty<PatientAppointmentInfoVO>(p.getValue());
			}
		});
		
		emailColumn.setCellFactory(new Callback<TableColumn<PatientAppointmentInfoVO,PatientAppointmentInfoVO>, TableCell<PatientAppointmentInfoVO,PatientAppointmentInfoVO>>() {
			
			@Override
			public TableCell<PatientAppointmentInfoVO, PatientAppointmentInfoVO> call(TableColumn<PatientAppointmentInfoVO, PatientAppointmentInfoVO> param) {
				
				TableCell<PatientAppointmentInfoVO, PatientAppointmentInfoVO> tableCell = new TableCell<PatientAppointmentInfoVO, PatientAppointmentInfoVO>() {
					
					@Override
					protected void updateItem(final PatientAppointmentInfoVO item, boolean empty) {

						super.updateItem(item, empty);
						if ( item != null ) {
							
							HBox hBox = new HBox();
							hBox.getChildren().add( new ImageView( new Image(getClass().getClassLoader().getResourceAsStream("edu/stratford/patientappointments/view/panels/images/Mail.png"), 15, 15, true, true) ));
							hBox.getChildren().add(new Text(item.getPatientEmailAddress()));
							Button button = new Button();
							button.setGraphic(hBox);
							setGraphic(button);
							button.setDisable( item.getAppointmentStatus() == 20 || item.getAppointmentStatus() == 15 || item.getAppointmentStatus() == 10 || item.getAppointmentStatus() == 1 );
							
							if ( (new Date()).getTime() > item.getDayRequiredForAppointment().getTime() ) {
								
								button.setDisable(true);
							}
							
							button.setOnAction(new EventHandler<ActionEvent>() {
								
								@Override
								public void handle(ActionEvent event) {

									EmailContentPopup contentPopup = new EmailContentPopup(HospitalAppointmentViewerPanel.this.primaryStage);
									contentPopup.showPopup(item);
								}
							});
						} else {
							
							setGraphic(null);
						}
					}
				};
				return tableCell;
			}
		});

		actionColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PatientAppointmentInfoVO, PatientAppointmentInfoVO>, ObservableValue<PatientAppointmentInfoVO>>() {
			@Override
			public ObservableValue<PatientAppointmentInfoVO> call(TableColumn.CellDataFeatures<PatientAppointmentInfoVO, PatientAppointmentInfoVO> p) {
				return new SimpleObjectProperty<PatientAppointmentInfoVO>(p.getValue());
			}
		});

		actionColumn.setCellFactory(new Callback<TableColumn<PatientAppointmentInfoVO,PatientAppointmentInfoVO>, TableCell<PatientAppointmentInfoVO,PatientAppointmentInfoVO>>() {

			@Override
			public TableCell<PatientAppointmentInfoVO, PatientAppointmentInfoVO> call(
					TableColumn<PatientAppointmentInfoVO, PatientAppointmentInfoVO> param) {

				TableCell<PatientAppointmentInfoVO, PatientAppointmentInfoVO> tableCell = new TableCell<PatientAppointmentInfoVO, PatientAppointmentInfoVO>(){

					@Override
					protected void updateItem(final PatientAppointmentInfoVO item, boolean empty) {

						super.updateItem(item, empty);
						if (item != null) {
							
							HBox hBox = new HBox(10);
							Button approveButton = new Button();
							approveButton.setGraphic(new ImageView( new Image(getClass().getClassLoader().getResourceAsStream("edu/stratford/patientappointments/view/panels/images/Approve.png"), 15, 15, true, true) ));
							

							Button rejectButton = new Button();
							rejectButton.setGraphic(new ImageView( new Image(getClass().getClassLoader().getResourceAsStream("edu/stratford/patientappointments/view/panels/images/AppointmentCancel.png"), 15, 15, true, true) ));
							
							hBox.getChildren().addAll(approveButton, rejectButton);
							setGraphic(hBox);
							approveButton.setDisable( item.getAppointmentStatus() == 20 || item.getAppointmentStatus() == 5 || item.getAppointmentStatus() == 10 );
							rejectButton.setDisable( item.getAppointmentStatus() == 20 || item.getAppointmentStatus() == 5 || item.getAppointmentStatus() == 10 );
							
							if ( (new Date()).getTime() > item.getDayRequiredForAppointment().getTime() ) {
								
								approveButton.setDisable(true);
								rejectButton.setDisable(true);
							}
							
							approveButton.setOnAction(new EventHandler<ActionEvent>() {
								
								@Override
								public void handle(ActionEvent event) {
									
									if ( item.getAppointmentStatus() == 1 ) {
										
										PatientAppointmentInfoVO appointmentInfoVO = new PatientAppointmentInfoVO(item.getAppointmentId(), 5);
										int approved = PatientAppointmentsDAO.INSTANCE.respondToAppointment( appointmentInfoVO );
									    if (approved == 1) {

											try {
												
												// TODO Send approval mail
												allAppointments.clear();
												allAppointments.addAll( PatientAppointmentsDAO.INSTANCE.getAllAppointments() );
												table.refresh();
											} catch (Exception e) {
												
												PatientAppointmentUIUtils.showMessage(messageText, e.getMessage(), Color.RED); 
											}
											
									    }
									} else if ( item.getAppointmentStatus() == 15 ) {
										
										PatientAppointmentInfoVO appointmentInfoVO = new PatientAppointmentInfoVO(item.getAppointmentId(), item.getRescheduleDate(), item.getRescheduleTimeHours(), item.getRescheduleTimeMinutes());
										int approved = PatientAppointmentsDAO.INSTANCE.rescheduleAppointment( appointmentInfoVO );
									    if (approved == 1) {

											try {
												
												// TODO Send approval mail
												allAppointments.clear();
												allAppointments.addAll( PatientAppointmentsDAO.INSTANCE.getAllAppointments() );
												table.refresh();
											} catch (Exception e) {
												
												PatientAppointmentUIUtils.showMessage(messageText, e.getMessage(), Color.RED); 
											}
											
									    }
									}
								}
							});
							
							rejectButton.setOnAction(new EventHandler<ActionEvent>() {
								
								@Override
								public void handle(ActionEvent event) {

									PatientAppointmentInfoVO appointmentInfoVO = new PatientAppointmentInfoVO(item.getAppointmentId(), 10);
									int rejected = PatientAppointmentsDAO.INSTANCE.respondToAppointment( appointmentInfoVO );
								    if (rejected == 1) {

										try {
											// TODO Send rejection mail
											allAppointments.clear();
											allAppointments.addAll( PatientAppointmentsDAO.INSTANCE.getAllAppointments() );
											table.refresh();
										} catch (Exception e) {
											
											PatientAppointmentUIUtils.showMessage(messageText, e.getMessage(), Color.RED); 
										}
										
								    }
								}
							});
						}
					}
				};
				return tableCell;
			}
		});
		
		messageText = new Text();
		
		table.getColumns().addAll( statusColumn, firstNameColumn, lastNameColumn, dateOfBirthColumn, doctorNameColumn, 
				appointmentDateColumn, appointmentTimeColumn, ailmentColumn, rescheduleDateColumn, rescheduleTimeColumn, emailColumn, actionColumn );

		setTop(hBox);
		setCenter(table);
		setBottom(messageText);
	}
	
	@Subscribe
	public void rerender(PatientAppointmentInfoVO patientAppointmentInfoVO) {

		try {
			allAppointments.clear();
			allAppointments.addAll( PatientAppointmentsDAO.INSTANCE.getAllAppointments() );
			table.refresh();
		} catch (Exception e) {
			
			PatientAppointmentUIUtils.showMessage(messageText, e.getMessage(), Color.RED); 
		}
	}
}
