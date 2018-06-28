package edu.stratford.patientappointments.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import edu.stratford.patientappointments.model.valueobjects.ApplicationUserVO;
import edu.stratford.patientappointments.model.valueobjects.AppointmentCountVO;
import edu.stratford.patientappointments.model.valueobjects.DoctorInfoVO;
import edu.stratford.patientappointments.model.valueobjects.DoctorScheduleVO;
import edu.stratford.patientappointments.model.valueobjects.PatientAppointmentInfoVO;
import edu.stratford.patientappointments.model.valueobjects.PatientAppointmentsApplicationSession;

public class PatientAppointmentsDAO extends BaseDAO {
	
	private PatientAppointmentsDAO(){}
	
	public static final PatientAppointmentsDAO INSTANCE = new PatientAppointmentsDAO();
	
	public ApplicationUserVO getUserByEmail( ApplicationUserVO emailPass ) throws Exception{
		
		SqlSession session = getSqlSessionFactory().openSession();
		ApplicationUserVO applicationUserVO = session.selectOne( "getUserByEmailAndPassword", emailPass );
		if ( applicationUserVO!= null && !applicationUserVO.getApplicationPassword().equals( emailPass.getApplicationPassword() ) ) {
			
			applicationUserVO = null;
		}
		session.close();
		return applicationUserVO;
	}
	
	public ApplicationUserVO getPatientByRecordNumber( double medicalRecordNumber ) throws Exception{
		
		SqlSession session = getSqlSessionFactory().openSession();
		ApplicationUserVO applicationUserVO = session.selectOne( "getPatientByRecordNumber", medicalRecordNumber );
		session.close();
		return applicationUserVO;
	}
	
	public Integer getMaxUserId() throws Exception  {
		
		SqlSession session = getSqlSessionFactory().openSession();
		int userId = session.selectOne( "getMaxUserId" );
		session.close();
		return userId;
	}
	
	public int insertUser(ApplicationUserVO user) throws Exception {
		
		SqlSession session = getSqlSessionFactory().openSession();
		int rowsModified = session.insert( "insertUser", user );
		if ( rowsModified == 1 ) {
			
			session.commit();
		} else {
			
			session.rollback();
		}
		session.close();
		return rowsModified;
	}
	
	public int insertAppointment(PatientAppointmentInfoVO patientAppointmentInfoVO)throws Exception {
		
		SqlSession session = getSqlSessionFactory().openSession();
		int rowsModified = session.insert( "insertAppointment", patientAppointmentInfoVO );
		if ( rowsModified == 1 ) {
			
			session.commit();
		} else {
			
			session.rollback();
		}
		session.close();
		return rowsModified;
	}
	
	public List<DoctorInfoVO> getAllDoctors() throws Exception {
		
		SqlSession session = getSqlSessionFactory().openSession();
		List<DoctorInfoVO> doctorInfoVOs = session.selectList("getAllDoctors");
		session.close();
		return doctorInfoVOs;
	}
	
	public List<ApplicationUserVO> getAllPatients() throws Exception {
		
		SqlSession session = getSqlSessionFactory().openSession();
		List<ApplicationUserVO> patientVOs = session.selectList("getAllPatients");
		session.close();
		return patientVOs;
	}
	
	public DoctorInfoVO getDoctorById(String doctorId) throws Exception {
		
		SqlSession session = getSqlSessionFactory().openSession();
		DoctorInfoVO doctorInfoVO = session.selectOne("getDoctorById", doctorId);
		session.close();
		return doctorInfoVO;
	}
	
	public List<PatientAppointmentInfoVO> getAllAppointments() throws Exception {
		
		List<PatientAppointmentInfoVO> appointmentInfoVOs = null;
		ApplicationUserVO currentUser = PatientAppointmentsApplicationSession.INSTANCE.getCurrentSessionInfoVO();
		SqlSession session = getSqlSessionFactory().openSession();
		if ("D".equals(currentUser.getIsAdmin())) {
			
			appointmentInfoVOs = session.selectList( "getAllAppointmentsForDoctor", "DOC-" + (int)currentUser.getMedicalRecordNumber() );
		} else {
			
			appointmentInfoVOs = session.selectList( "getAllAppointments" );
		}
		session.close();
		return appointmentInfoVOs;
	}
	
	public DoctorScheduleVO getDoctorScheduleForDate( DoctorScheduleVO doctorScheduleVO ) throws Exception {
		
		SqlSession session = getSqlSessionFactory().openSession();
		DoctorScheduleVO scheduleVO = session.selectOne( "getDoctorScheduleForDate", doctorScheduleVO );
		session.close();
		return scheduleVO;
	}

	public List<AppointmentCountVO> getBookedAppointments( String doctorId ) throws Exception {
		
		SqlSession session = getSqlSessionFactory().openSession();
		List<AppointmentCountVO> appointments = session.selectList( "getBookedAppointments", doctorId );
		session.close();
		return appointments;
	}
	
	
	public int deleteAppointment(String appointmentId) {

		SqlSession session = getSqlSessionFactory().openSession();
		int rowsModified = session.delete( "deleteAppointmentWithId", appointmentId );
		session.commit();
		session.close();
		return rowsModified;
	}
	
	public int respondToAppointment(PatientAppointmentInfoVO appointment) {

		SqlSession session = getSqlSessionFactory().openSession();
		int rowsModified = session.update( "respondToAppointment", appointment );
		session.commit();
		session.close();
		return rowsModified;
	}
	
	public int rescheduleAppointment(PatientAppointmentInfoVO appointment) {

		SqlSession session = getSqlSessionFactory().openSession();
		int rowsModified = session.update( "rescheduleAppointment", appointment );
		session.commit();
		session.close();
		return rowsModified;
	}
}
