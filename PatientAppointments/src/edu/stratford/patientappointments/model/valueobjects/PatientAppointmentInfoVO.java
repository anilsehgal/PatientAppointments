package edu.stratford.patientappointments.model.valueobjects;

import java.util.Date;
/**
 * <h1>PatientAppointmentInfoVO</h1>
 * 
 * The PatientAppointmentInfoVO class is a value object (VO, also known as model object), 
 * which would be used to store a patient’s appointment information. 
 *
 * @author  Anil Sehgal
 * @version 1.0
 * @since   2016-01-18 
 */
public class PatientAppointmentInfoVO {

	private String appointmentId;
	
	/**
	 * the class member for storing the first name of the patient
	 */
	private String firstName;

	/**
	 * the class member for storing the last name of the patient 
	 */
	private String lastName;

	/**
	 * the class member for storing the date of birth as a Java Date object for all patients
	 */
	private Date dateOfBirth;

	/**
	 * the class member for storing the name of the selected doctor for all patient appointment info objects
	 */
	private String doctorId;

	/**
	 * the class member for storing the day required for appointment as a Java Date object for all patients
	 */
	private Date dayRequiredForAppointment;

	/**
	 *  the class member for storing the hours part of appointment time for all patients in 24 HR format, HH
	 */
	private int appointmentTimeHours;

	/**
	 * the class member for storing the minutes part of appointment time for all patients, mm
	 */
	private int appointmentTimeMinutes;

	/**
	 * the class member for storing the patient email address
	 */
	private String patientEmailAddress;
	
	/**
	 * the unique id given to every patient to facilitate record search
	 */
	private double patientMedicalRecordNumber;
	
	/**
	 * the description of ailment
	 */
	private String ailmentDescription;
	
	private Date rescheduleDate;
	
	private int rescheduleTimeHours;
	
	private int rescheduleTimeMinutes;
	
	private int appointmentStatus;
	
	public PatientAppointmentInfoVO(){}

	public PatientAppointmentInfoVO(String appointmentId, String firstName, String lastName, Date dateOfBirth,
			String doctorId, Date dayRequiredForAppointment, int appointmentTimeHours, int appointmentTimeMinutes,
			String patientEmailAddress, double patientMedicalRecordNumber, String ailmentDescription,
			Date rescheduleDate, int rescheduleTimeHours, int rescheduleTimeMinutes, int appointmentStatus) {
		super();
		this.appointmentId = appointmentId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
		this.doctorId = doctorId;
		this.dayRequiredForAppointment = dayRequiredForAppointment;
		this.appointmentTimeHours = appointmentTimeHours;
		this.appointmentTimeMinutes = appointmentTimeMinutes;
		this.patientEmailAddress = patientEmailAddress;
		this.patientMedicalRecordNumber = patientMedicalRecordNumber;
		this.ailmentDescription = ailmentDescription;
		this.rescheduleDate = rescheduleDate;
		this.rescheduleTimeHours = rescheduleTimeHours;
		this.rescheduleTimeMinutes = rescheduleTimeMinutes;
		this.appointmentStatus = appointmentStatus;
	}

	public PatientAppointmentInfoVO(String appointmentId, int appointmentStatus) {
		super();
		this.appointmentId = appointmentId;
		this.appointmentStatus = appointmentStatus;
	}

	public PatientAppointmentInfoVO(String appointmentId, Date dayRequiredForAppointment, int appointmentTimeHours,
			int appointmentTimeMinutes) {
		super();
		this.appointmentId = appointmentId;
		this.dayRequiredForAppointment = dayRequiredForAppointment;
		this.appointmentTimeHours = appointmentTimeHours;
		this.appointmentTimeMinutes = appointmentTimeMinutes;
	}
	
	/**
	 * @param appointmentId the appointmentId to set
	 */
	public void setAppointmentId(String appointmentId) {
		this.appointmentId = appointmentId;
	}

	/**
	 * @param rescheduleDate the rescheduleDate to set
	 */
	public void setRescheduleDate(Date rescheduleDate) {
		this.rescheduleDate = rescheduleDate;
	}

	/**
	 * @param rescheduleTimeHours the rescheduleTimeHours to set
	 */
	public void setRescheduleTimeHours(int rescheduleTimeHours) {
		this.rescheduleTimeHours = rescheduleTimeHours;
	}

	/**
	 * @param rescheduleTimeMinutes the rescheduleTimeMinutes to set
	 */
	public void setRescheduleTimeMinutes(int rescheduleTimeMinutes) {
		this.rescheduleTimeMinutes = rescheduleTimeMinutes;
	}

	/**
	 * @param appointmentStatus the appointmentStatus to set
	 */
	public void setAppointmentStatus(int appointmentStatus) {
		this.appointmentStatus = appointmentStatus;
	}

	/**
	 * @return the rescheduleDate
	 */
	public Date getRescheduleDate() {
		return rescheduleDate;
	}

	/**
	 * @return the rescheduleTimeHours
	 */
	public int getRescheduleTimeHours() {
		return rescheduleTimeHours;
	}

	/**
	 * @return the rescheduleTimeMinutes
	 */
	public int getRescheduleTimeMinutes() {
		return rescheduleTimeMinutes;
	}

	/**
	 * @return the appointmentStatus
	 */
	public int getAppointmentStatus() {
		return appointmentStatus;
	}

	/**
	 * @return the appointmentId
	 */
	public String getAppointmentId() {
		return appointmentId;
	}

	/**
	 * @return the doctorId
	 */
	public String getDoctorId() {
		return doctorId;
	}

	/**
	 * @return the ailmentDescription
	 */
	public String getAilmentDescription() {
		return ailmentDescription;
	}

	/**
	 * @param ailmentDescription the ailmentDescription to set
	 */
	public void setAilmentDescription(String ailmentDescription) {
		this.ailmentDescription = ailmentDescription;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @return the dateOfBirth
	 */
	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	/**
	 * @return the dayRequiredForAppointment
	 */
	public Date getDayRequiredForAppointment() {
		return dayRequiredForAppointment;
	}

	/**
	 * @return the appointmentTimeHours
	 */
	public int getAppointmentTimeHours() {
		return appointmentTimeHours;
	}

	/**
	 * @return the appointmentTimeMinutes
	 */
	public int getAppointmentTimeMinutes() {
		return appointmentTimeMinutes;
	}

	/**
	 * @return the patientEmailAddress
	 */
	public String getPatientEmailAddress() {
		return patientEmailAddress;
	}

	/**
	 * @return the patientMedicalRecordNumber
	 */
	public double getPatientMedicalRecordNumber() {
		return patientMedicalRecordNumber;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PatientAppointmentInfoVO [appointmentId=" + appointmentId + ", firstName=" + firstName + ", lastName="
				+ lastName + ", dateOfBirth=" + dateOfBirth + ", doctorId=" + doctorId + ", dayRequiredForAppointment="
				+ dayRequiredForAppointment + ", appointmentTimeHours=" + appointmentTimeHours
				+ ", appointmentTimeMinutes=" + appointmentTimeMinutes + ", patientEmailAddress=" + patientEmailAddress
				+ ", patientMedicalRecordNumber=" + patientMedicalRecordNumber + ", ailmentDescription="
				+ ailmentDescription + ", rescheduleDate=" + rescheduleDate + ", rescheduleTimeHours="
				+ rescheduleTimeHours + ", rescheduleTimeMinutes=" + rescheduleTimeMinutes + ", appointmentStatus="
				+ appointmentStatus + "]";
	}

}
