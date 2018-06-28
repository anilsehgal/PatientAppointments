package edu.stratford.patientappointments.view.panels;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.common.eventbus.Subscribe;

import edu.stratford.patientappointments.dao.PatientAppointmentsDAO;
import edu.stratford.patientappointments.model.valueobjects.ApplicationUserVO;
import edu.stratford.patientappointments.model.valueobjects.DoctorInfoVO;
import edu.stratford.patientappointments.model.valueobjects.PatientAppointmentInfoVO;
import edu.stratford.patientappointments.model.valueobjects.PatientAppointmentsApplicationSession;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.scene.control.agenda.Agenda.LocalDateTimeRange;

public class AgendaPanel extends BorderPane
{
	Agenda agenda = new Agenda();
	HBox nav = new HBox();
	Stage primaryStage;
	public AgendaPanel(Stage primaryStage) {

		super();
		this.primaryStage = primaryStage;
		PatientAppointmentsApplicationSession.INSTANCE.getEventBus().register(this);
		setCenter(agenda);
		setBottom(nav);
		render();
        Button prevButton = new Button( "", new ImageView( new Image(getClass().getClassLoader().getResourceAsStream("edu/stratford/patientappointments/view/panels/images/prev.png"), 15, 15, true, true) ));
        Button nextButton = new Button( "", new ImageView( new Image(getClass().getClassLoader().getResourceAsStream("edu/stratford/patientappointments/view/panels/images/next.png"), 15, 15, true, true) ));
        Button refreshButton = new Button("Refresh");
		nav.getChildren().addAll(prevButton, nextButton, refreshButton);
		nextButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {

				if (agenda.getDisplayedLocalDateTime() != null) {
					
					agenda.setDisplayedLocalDateTime(agenda.getDisplayedLocalDateTime().plusDays(7));
				}
			}
		});
		prevButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				
				if (agenda.getDisplayedLocalDateTime() != null) {
					
					agenda.setDisplayedLocalDateTime(agenda.getDisplayedLocalDateTime().minusDays(7));
				}
			}
		});
		refreshButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {

				render();
			}
		});
	}

	private void render() {

		agenda.appointments().clear();
		
		final Map<String, Agenda.AppointmentGroup> appointmentGroupMap = new TreeMap<String, Agenda.AppointmentGroup>();
		for (Agenda.AppointmentGroup lAppointmentGroup : agenda.appointmentGroups()) {

			appointmentGroupMap.put(lAppointmentGroup.getDescription(), lAppointmentGroup);
		}

		List<PatientAppointmentInfoVO> appointmentInfos = new ArrayList<>();
		try {
			appointmentInfos = PatientAppointmentsDAO.INSTANCE.getAllAppointments();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		Appointment[] allAppointments = new Appointment[appointmentInfos.size()];

		int index = 0;
		for (PatientAppointmentInfoVO appointmentInfoVO : appointmentInfos) {

			LocalDate appointmentDate = appointmentInfoVO.getDayRequiredForAppointment().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			DoctorInfoVO doctorInfoVO = null;
			ApplicationUserVO applicationUserVO = null;
			try {
				doctorInfoVO = PatientAppointmentsDAO.INSTANCE.getDoctorById(appointmentInfoVO.getDoctorId());
				applicationUserVO = PatientAppointmentsDAO.INSTANCE.getPatientByRecordNumber(appointmentInfoVO.getPatientMedicalRecordNumber());
			} catch (Exception e) {

				e.printStackTrace();
			}
			final Agenda.AppointmentImplLocal appointment = new Agenda.AppointmentImplLocal()
					.withStartLocalDateTime(LocalDateTime.of(appointmentDate, LocalTime.of(appointmentInfoVO.getAppointmentTimeHours(), appointmentInfoVO.getAppointmentTimeMinutes())))
					.withEndLocalDateTime(LocalDateTime.of(appointmentDate, LocalTime.of(appointmentInfoVO.getAppointmentTimeHours() + 1, appointmentInfoVO.getAppointmentTimeMinutes())))
					.withSummary("Dr. " + doctorInfoVO.getDoctorFirstName() + "/" + applicationUserVO.getLastName() + ", " + applicationUserVO.getFirstName() + "\nFor:\n" + appointmentInfoVO.getAilmentDescription())
					.withDescription(appointmentInfoVO.getAppointmentId());
			appointment.startLocalDateTime().addListener(new ChangeListener<LocalDateTime>() {

				@Override
				public void changed(ObservableValue<? extends LocalDateTime> observable, LocalDateTime oldValue,
						LocalDateTime newValue) {
					
					if ( newValue != null && oldValue != null && !newValue.isEqual(oldValue) ) {
						
						LocalDate localDate = newValue.toLocalDate();
						ZonedDateTime zonedAppointmentDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
						Instant appointmentInstant = Instant.from(zonedAppointmentDateTime);
						Date appointmentDate = Date.from(appointmentInstant);
						
						PatientAppointmentInfoVO appointmentInfoVO = new PatientAppointmentInfoVO(appointment.getDescription(), appointmentDate, newValue.getHour(), newValue.getMinute());
						int rescheduled = PatientAppointmentsDAO.INSTANCE.rescheduleAppointment(appointmentInfoVO);
						if ( rescheduled == 1 ) {
							
							PatientAppointmentsApplicationSession.INSTANCE.getEventBus().post(appointmentInfoVO);
						} else {
							
							System.err.println( "error occured in updating appointments" );
						}
					}
				}
			});
			
			if ( appointmentInfoVO.getDoctorId().endsWith("1") ) {
				
				appointment.setAppointmentGroup(appointmentGroupMap.get("group05" ));
			} else if (appointmentInfoVO.getDoctorId().endsWith("2")) {
				
				appointment.setAppointmentGroup(appointmentGroupMap.get("group09" ));
			} else if (appointmentInfoVO.getDoctorId().endsWith("3")) {
				
				appointment.setAppointmentGroup(appointmentGroupMap.get("group14" ));
			} else if (appointmentInfoVO.getDoctorId().endsWith("4")) {
				
				appointment.setAppointmentGroup(appointmentGroupMap.get("group22" ));
			}
			if ( appointmentInfoVO.getAppointmentStatus() == 10 ) {
				
				appointment.setAppointmentGroup(appointmentGroupMap.get("group18" ));
			}
			allAppointments[index] = appointment;
			index++;
		}

		agenda.setAllowResize(false);
		if ( "Y".equalsIgnoreCase(PatientAppointmentsApplicationSession.INSTANCE.getCurrentSessionInfoVO().getIsAdmin()) ) {
			
			agenda.setAllowDragging(true);
			agenda.newAppointmentCallbackProperty().set(new Callback<Agenda.LocalDateTimeRange, Agenda.Appointment>()
	        {
	            @Override
	            public Agenda.Appointment call(LocalDateTimeRange dateTimeRange)
	            {
	            	
	            	final Agenda.AppointmentImplLocal appointmentImplLocal = new Agenda.AppointmentImplLocal()
	                        .withStartLocalDateTime( dateTimeRange.getStartLocalDateTime() )
	                        .withEndLocalDateTime( dateTimeRange.getEndLocalDateTime() )
	                        .withSummary("new")
	                        .withDescription("new")
	                        .withAppointmentGroup(appointmentGroupMap.get("group05" ) );
	            	final NewAgendaPopupPanel agendaPopupPanel = new NewAgendaPopupPanel(primaryStage, dateTimeRange);
	            	agendaPopupPanel.setOnHidden(new EventHandler<WindowEvent>() {
						
						@Override
						public void handle(WindowEvent event) {

							if ( NewAgendaPopupPanel.createdAppointment != null ) {
								
								PatientAppointmentsApplicationSession.INSTANCE.getEventBus().post(NewAgendaPopupPanel.createdAppointment);
								NewAgendaPopupPanel.createdAppointment = null;
							} else {
								
								render();
							}
						}
					});
	            	agendaPopupPanel.show(primaryStage);
	                return appointmentImplLocal;

	            }

	        });
		} else {
			
			agenda.setAllowDragging(false);
		}
		
		agenda.setEditAppointmentCallback(new Callback<Agenda.Appointment, Void>() {
			
			@Override
			public Void call(Appointment param) {

				return null;
			}
		});

		agenda.appointments().addAll(allAppointments);
		agenda.setDisplayedLocalDateTime(LocalDateTime.now());
	}
	
	@Subscribe
	public void rerender(PatientAppointmentInfoVO patientAppointmentInfoVO) {

		render();
	}
}