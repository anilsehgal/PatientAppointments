package edu.stratford.patientappointments.model.valueobjects;

import com.google.common.eventbus.EventBus;

public class PatientAppointmentsApplicationSession {

	private PatientAppointmentsApplicationSession(){}
	
	public static final PatientAppointmentsApplicationSession INSTANCE = new PatientAppointmentsApplicationSession();
	
	private ApplicationUserVO currentSessionInfoVO;

	private EventBus eventBus = new EventBus();
	
	/**
	 * @return the currentSessionInfoVO
	 */
	public ApplicationUserVO getCurrentSessionInfoVO() {
		return currentSessionInfoVO;
	}

	/**
	 * @param currentSessionInfoVO the currentSessionInfoVO to set
	 */
	public void setCurrentSessionInfoVO(ApplicationUserVO currentSessionInfoVO) {
		this.currentSessionInfoVO = currentSessionInfoVO;
	}

	/**
	 * @return the eventBus
	 */
	public EventBus getEventBus() {
		return eventBus;
	}
}
