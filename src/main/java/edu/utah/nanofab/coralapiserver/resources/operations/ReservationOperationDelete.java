package edu.utah.nanofab.coralapiserver.resources.operations;

import edu.utah.nanofab.coralapiserver.core.ReservationRequest;


public class ReservationOperationDelete extends ResourceOperation  {
  @Override
  public void performOperationImpl() throws Exception {
      ReservationRequest request = (ReservationRequest)(this.postedObject.get());
      System.out.println("Reservation creation for " + request.getItem());
      this.api.deleteReservation(user.getUsername(), request.getMember(), 
          request.getProject(), request.getItem(), request.getBdate(), request.getLengthInMinutes());
      this.setReturnValue(request);
  }

  @Override
  public String errorMessage() {
    return "Error while trying to cancel reservation" ;
  }

}