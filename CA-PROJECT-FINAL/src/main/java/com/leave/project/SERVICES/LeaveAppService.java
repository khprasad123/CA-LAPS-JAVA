package com.leave.project.SERVICES;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.leave.project.MODELS.LeaveHistoryDetails;


public class LeaveAppService{
	

	public static int SUCCESS = 0;
	public static int ENDDATE_LESS_THAN_ERROR = 1;
	public static int WEEKEND_OR_HOLIDAY_ERROR = 2;
	
	


	public static int compareDate(LeaveHistoryDetails Leave, int numberofHoliday)
	{
		LocalDate start = Leave.getStartDate().toLocalDate();
		LocalDate end = Leave.getEndDate().toLocalDate();
				if(end.isAfter(start) )
				{
					 if(start.getDayOfWeek().getValue() < 6 && end.getDayOfWeek().getValue() < 6 && numberofHoliday == 0 ) {
						return SUCCESS;
					}else {
						return WEEKEND_OR_HOLIDAY_ERROR;
					}
				}
				else
				{
					return ENDDATE_LESS_THAN_ERROR;
				}
					
	}
	
	public static boolean numberOfDays(LeaveHistoryDetails Leave, int numberofpublic)
	{
//		List<PublicHollyday> list = new ArrayList<PublicHollyday>();
//		list = publicrepo.findByStartDateBetween(Leave.getStartDate(),Leave.getEndDate());
//		int numberofpublic =  list.size();
//		
//		System.out.println("numberofpublic" +numberofpublic);
		LocalDate start = Leave.getStartDate().toLocalDate();
		LocalDate end = Leave.getEndDate().toLocalDate();
		
		 int days =(int) ChronoUnit.DAYS.between(start, end);
		 days+=1;
		 System.out.println("days before checking" + days);
		 int sundays = 0;
	        int saturday = 0;

	        while (! start.isAfter(end)) {
	            if (start.getDayOfWeek().getValue() == 6){
	                saturday++; 
	            }
	         
	            if(start.getDayOfWeek().getValue() == 7){
	                sundays++;
	            }

	           start = start.plusDays(1);
	           System.out.println("saturday" +saturday );
	           System.out.println("sunday" +sundays );
	           System.out.println("day" +start.getDayOfWeek());
	        }
		 if(days<=14)
		 {
			 days = days -(saturday +sundays) - numberofpublic ;
		 }
		System.out.println(days);
		
	    if(Leave.getLeaveType().getType().equals("Medical Leave") && Leave.getEmployee().getMedicalLeaveCount()>=days)
		{
			return true;
		}
		else if(Leave.getLeaveType().getType().equals("Compensation Leave") && Leave.getEmployee().getCompensationLeaveCount()>=days)
		{
			return true;
		}
		else if(Leave.getLeaveType().getType().equals("Annual Leave")  && Leave.getEmployee().getAnnualLeaveCount()>=days)
		{
			return true;
		}
        
      
		return false;
	}
}
