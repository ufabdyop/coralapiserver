package edu.utah.nanofab.coralapiserver.resources.operations;

import edu.utah.nanofab.coralapi.collections.Reservations;
import edu.utah.nanofab.coralapiserver.core.ReservationRequest;


public class ReservationOperationDelete extends ResourceOperation  {
  @Override
  public void performOperationImpl() throws Exception {
      ReservationRequest request = (ReservationRequest)(this.postedObject.get());
      System.out.println("Reservation deletion for " + request.getItem() + " " + request.getBdate());
      this.api.deleteReservation(user.getUsername(), request.getMember(), 
          request.getProject(), request.getItem(), request.getBdate(), request.getLengthInMinutes());
      
      //checkRemoved = this.api.getReservations(request.getMember(), request.getItem(), request.getBdate(), request.getLengthInMinutes());
      Reservations checkRemoved = this.api.getReservations(request.getMember(), request.getItem(), request.getBdate(), request.getLengthInMinutes());
      if (checkRemoved.size() != 0) {
          System.err.println("Reservation deletion FAILED for " + request.getItem() + " " + request.getBdate());          
      }
      
      this.setReturnValue(request);
  }

  @Override
  public String errorMessage() {
    return "Error while trying to cancel reservation" ;
  }

}