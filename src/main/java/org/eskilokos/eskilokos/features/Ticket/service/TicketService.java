package org.eskilokos.eskilokos.features.Ticket.service;

import java.io.IOException;

public interface TicketService {
    byte[] generarTicket(Integer idPedido) throws IOException;
}