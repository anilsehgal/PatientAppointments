package edu.stratford.patientappointments.view.panels;

import com.google.common.eventbus.Subscribe;

import edu.stratford.patientappointments.model.valueobjects.ApplicationUserVO;
import edu.stratford.patientappointments.model.valueobjects.PatientAppointmentsApplicationSession;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class UserInformationPanel extends HBox {

	ImageView leftImageView;

	Text text;

	public UserInformationPanel() {

		super();
		PatientAppointmentsApplicationSession.INSTANCE.getEventBus().register(this);
		Image leftImage = new Image( getClass().getClassLoader().getResourceAsStream( "edu/stratford/patientappointments/view/panels/images/Question.png" ), 15, 15, true, true ); 
		leftImageView = new ImageView(leftImage);
		getChildren().add(leftImageView);
		text = new Text("Sign In");
		getChildren().add(text);
	}

	@Subscribe
	public void rerender(ApplicationUserVO applicationUserVO) {

		if ( applicationUserVO != null ) {

			Image leftImage = new Image( getClass().getClassLoader().getResourceAsStream( "edu/stratford/patientappointments/view/panels/images/Appointment.png" ), 15, 15, true, true );
			text.setText( applicationUserVO.getLastName() + ", " + applicationUserVO.getFirstName() );
			leftImageView.setImage( leftImage );
		} else {

			Image leftImage = new Image( getClass().getClassLoader().getResourceAsStream( "edu/stratford/patientappointments/view/panels/images/Question.png" ), 15, 15, true, true );
			text.setText( "Sign In" );
			leftImageView.setImage( leftImage );
		}
	}

	@Subscribe
	public void rerender(String s) {

		Image leftImage = new Image( getClass().getClassLoader().getResourceAsStream( "edu/stratford/patientappointments/view/panels/images/Question.png" ), 15, 15, true, true );
		text.setText( "Sign In" );
		leftImageView.setImage( leftImage );
	}
}
