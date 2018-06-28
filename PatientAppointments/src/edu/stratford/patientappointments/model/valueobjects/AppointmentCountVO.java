package edu.stratford.patientappointments.model.valueobjects;

import java.util.Date;

public class AppointmentCountVO {
	
	private Date scheduleDate;
	
	private int appointmentCount;

	/**
	 * @return the scheduleDate
	 */
	public Date getScheduleDate() {
		return scheduleDate;
	}

	/**
	 * @param scheduleDate the scheduleDate to set
	 */
	public void setScheduleDate(Date scheduleDate) {
		this.scheduleDate = scheduleDate;
	}

	/**
	 * @return the appointmentCount
	 */
	public int getAppointmentCount() {
		return appointmentCount;
	}

	/**
	 * @param appointmentCount the appointmentCount to set
	 */
	public void setAppointmentCount(int appointmentCount) {
		this.appointmentCount = appointmentCount;
	}
	
	
}
