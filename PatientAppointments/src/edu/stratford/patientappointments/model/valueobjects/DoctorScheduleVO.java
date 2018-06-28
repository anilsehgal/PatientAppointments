package edu.stratford.patientappointments.model.valueobjects;

import java.util.Date;

public class DoctorScheduleVO {
	
	/**
	 * The Doctor ID
	 */
	private String doctorId;
	
	/**
	 * The Doctor Schedule Date
	 */
	private Date scheduleDate;
	
	/**
	 * The in time hours
	 */
	private int inTime;
	
	/**
	 * The out time hours
	 */
	private int outTime;

	public DoctorScheduleVO(){}
	
	/**
	 * 
	 * @param doctorId doctorId
	 * @param scheduleDate scheduleDate
	 * @param inTime inTime
	 * @param outTime outTime
	 */
	public DoctorScheduleVO(String doctorId, Date scheduleDate, int inTime, int outTime) {
		super();
		this.doctorId = doctorId;
		this.scheduleDate = scheduleDate;
		this.inTime = inTime;
		this.outTime = outTime;
	}

	/**
	 * @return the doctorId
	 */
	public String getDoctorId() {
		return doctorId;
	}

	/**
	 * @return the scheduleDate
	 */
	public Date getScheduleDate() {
		return scheduleDate;
	}

	/**
	 * @return the inTime
	 */
	public int getInTime() {
		return inTime;
	}

	/**
	 * @return the outTime
	 */
	public int getOutTime() {
		return outTime;
	}
	
	
}
