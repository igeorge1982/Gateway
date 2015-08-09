package com.jeet.rest;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.jeet.api.Movie;
import com.jeet.api.Ticket;
import com.jeet.service.BookingHandlerImpl;

@Path("/")
public class BookController {
	

	@PUT
	@Path("/book/{movieName}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	
	public Response getMovieTicket(@PathParam(value = "movieName") String movieName) {
		
		Ticket ticket = new BookingHandlerImpl().bookTicket(movieName);
		
		if (ticket != null) {
			return Response.ok().entity(ticket).build();
		
		} else {
		
			return Response.status(404).build();
		}
	}
	
	@GET
	@Path("/book/{detail}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	
	public Response movieGenre(@PathParam(value = "detail") String detail) {
		
		Movie movie = new BookingHandlerImpl().movieGenre(detail);
		
		if (movie != null) {
			return Response.ok().entity(movie).build();
		
		} else {
		
			return Response.status(404).build();
		}
	}
	
	@DELETE
	@Path("/book/{ticketId}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	
	public Response cancelTicket(@PathParam(value = "ticketId") int ticketId) {
		
		new BookingHandlerImpl().deleteTicket(ticketId);
		
		return Response.ok().build();
	}
}
