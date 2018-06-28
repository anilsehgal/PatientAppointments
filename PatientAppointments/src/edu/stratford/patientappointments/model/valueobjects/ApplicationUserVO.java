package edu.stratford.patientappointments.model.valueobjects;

import java.util.Date;

public class ApplicationUserVO {

	private double medicalRecordNumber;
	private String firstName;
	private String lastName;
	private Date dateOfBirth;
	private String userEmailAddress;
	private String applicationPassword;
	private String isAdmin;
	
	public ApplicationUserVO() {
		super();
	}
	
	public ApplicationUserVO(double medicalRecordNumber, String firstName, String lastName, Date dateOfBirth,
			String userEmailAddress, String applicationPassword, String isAdmin) {
		super();
		this.medicalRecordNumber = medicalRecordNumber;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
		this.userEmailAddress = userEmailAddress;
		this.applicationPassword = applicationPassword;
		this.isAdmin = isAdmin;
	}
	/**
	 * @return the medicalRecordNumber
	 */
	public double getMedicalRecordNumber() {
		return medicalRecordNumber;
	}
	/**
	 * @param medicalRecordNumber the medicalRecordNumber to set
	 */
	public void setMedicalRecordNumber(double medicalRecordNumber) {
		this.medicalRecordNumber = medicalRecordNumber;
	}
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * @return the dateOfBirth
	 */
	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	/**
	 * @param dateOfBirth the dateOfBirth to set
	 */
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	/**
	 * @return the userEmailAddress
	 */
	public String getUserEmailAddress() {
		return userEmailAddress;
	}
	/**
	 * @param userEmailAddress the userEmailAddress to set
	 */
	public void setUserEmailAddress(String userEmailAddress) {
		this.userEmailAddress = userEmailAddress;
	}
	/**
	 * @return the applicationPassword
	 */
	public String getApplicationPassword() {
		return applicationPassword;
	}
	/**
	 * @param applicationPassword the applicationPassword to set
	 */
	public void setApplicationPassword(String applicationPassword) {
		this.applicationPassword = applicationPassword;
	}
	/**
	 * @return the isAdmin
	 */
	public String getIsAdmin() {
		return isAdmin;
	}
	/**
	 * @param isAdmin the isAdmin to set
	 */
	public void setIsAdmin(String isAdmin) {
		this.isAdmin = isAdmin;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return firstName + " " + lastName;
	}
	
	
}
