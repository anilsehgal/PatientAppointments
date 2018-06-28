package edu.stratford.patientappointments.model.valueobjects;

public class DoctorInfoVO {
	
	private String doctorId;
	
	private String doctorFirstName;
	
	private String doctorLastName;
	
	private String doctorGender;

	private String doctorSpecialization;
	
	private String doctorDescription;

	private int weeklyHolidayCode;
	
	public DoctorInfoVO(){}
	
	/**
	 * 
	 * @param doctorId doctorId
	 * @param doctorFirstName doctorFirstName
	 * @param doctorLastName doctorLastName
	 * @param doctorGender doctorGender
	 * @param doctorSpecialization doctorSpecialization
	 * @param doctorDescription doctorDescription
	 * @param weeklyHolidayCode weeklyHolidayCode
	 */
	public DoctorInfoVO(String doctorId, String doctorFirstName, String doctorLastName, String doctorGender,
			String doctorSpecialization, String doctorDescription, int weeklyHolidayCode) {
		super();
		this.doctorId = doctorId;
		this.doctorFirstName = doctorFirstName;
		this.doctorLastName = doctorLastName;
		this.doctorGender = doctorGender;
		this.doctorSpecialization = doctorSpecialization;
		this.doctorDescription = doctorDescription;
		this.weeklyHolidayCode = weeklyHolidayCode;
	}

	


	/**
	 * @return the weeklyHolidayCode
	 */
	public int getWeeklyHolidayCode() {
		return weeklyHolidayCode;
	}




	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		
		if ( obj != null && obj instanceof DoctorInfoVO ) {
			
			return doctorId.equals(((DoctorInfoVO) obj).doctorId);
		}
		return super.equals(obj);
	}



	/**
	 * @return the doctorId
	 */
	public String getDoctorId() {
		return doctorId;
	}

	/**
	 * @return the doctorFirstName
	 */
	public String getDoctorFirstName() {
		return doctorFirstName;
	}

	/**
	 * @return the doctorLastName
	 */
	public String getDoctorLastName() {
		return doctorLastName;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return doctorLastName + ", " + doctorFirstName;
	}

	/**
	 * @return the doctorGender
	 */
	public String getDoctorGender() {
		return doctorGender;
	}



	/**
	 * @return the doctorSpecialization
	 */
	public String getDoctorSpecialization() {
		return doctorSpecialization;
	}



	/**
	 * @return the doctorDescription
	 */
	public String getDoctorDescription() {
		return doctorDescription;
	}
	
	
}
