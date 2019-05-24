package com.leave.project.SERVICES;

import java.time.LocalDate;



import com.leave.project.MODELS.LeaveHistoryDetails;


public class LeaveAppService{

	public static boolean compareDate(LeaveHistoryDetails Leave)
	{
		LocalDate start = Leave.getStartDate().toLocalDate();
		LocalDate end = Leave.getEndDate().toLocalDate();
				if(end.isAfter(start))
					return true;
				else
					return false;
	}
	
	public static boolean numberOfDays(LeaveHistoryDetails Leave)
	{
		LocalDate start = Leave.getStartDate().toLocalDate();
		LocalDate end = Leave.getEndDate().toLocalDate();
		
		return true;
	}
}
