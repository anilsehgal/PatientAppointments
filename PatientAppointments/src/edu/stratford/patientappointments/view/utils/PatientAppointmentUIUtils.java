package edu.stratford.patientappointments.view.utils;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class PatientAppointmentUIUtils {


	public static void showMessage(final Text field, String message, Color color) {

		field.setText( message );
		field.setFill(color);
		PauseTransition delay = new PauseTransition(Duration.seconds(2));
		delay.setOnFinished( new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				field.setText("");
			}
		} );
		delay.play();
	}
	
	public static void showMessage(final Text field, String message, Color color, EventHandler<ActionEvent> eventHandler) {

		field.setText( message );
		field.setFill(color);
		PauseTransition delay = new PauseTransition(Duration.seconds(2));
		delay.setOnFinished( eventHandler );
		delay.play();
	}
}
