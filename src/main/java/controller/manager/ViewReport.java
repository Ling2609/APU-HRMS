package controller.manager;

import entity.ArrivalDepartureReport;
import entity.BookingLog;
import entity.CommentFeedbackReport;
import entity.FinancialReport;
import entity.Report;
import entity.RoomStatusReport;
import entity.TransactionReport;
import jakarta.ejb.EJB;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import session.ManagerFacade;

@WebServlet(name = "ViewReport", urlPatterns = {"/manager/ViewReport"})
public class ViewReport extends HttpServlet {
    
    @EJB
    private ManagerFacade managerFacade;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        
        String action = request.getParameter("action");
        String reportID = request.getParameter("reportID");
        
        if(!"view".equals(action)) {
            request.setAttribute("error", "Invalid action.");
            request.getRequestDispatcher("/manager/ManageReport").forward(request, response);
            return;
        }
        
        Report report = managerFacade.getReportById(reportID);
        System.out.println(report);
        
        switch(report.getReportType()) {
            case FINANCIAL:
                FinancialReport financialReport = new FinancialReport(report);
                financialReport.setBookingsLogs(managerFacade.getReportBookings(report));
                financialReport.setSalaryLogs(managerFacade.getReportSalary(report));
                request.setAttribute("viewingReport", financialReport);
                break;
            case TRANSACTION:
                TransactionReport transactionReport = new TransactionReport(report);
                transactionReport.setBookingsLogs(managerFacade.getReportBookings(report));
                request.setAttribute("viewingReport", transactionReport);
                break;
            case ARRIVAL_DEPARTURE:
                ArrivalDepartureReport arrivalDepartureReport = new ArrivalDepartureReport(report);
                arrivalDepartureReport.setBookingsLogs(managerFacade.getReportBookings(report));
                arrivalDepartureReport.setRoomLogs(managerFacade.getReportRoom(report));
                request.setAttribute("viewingReport", arrivalDepartureReport);
                break;
            case COMMENT_FEEDBACK:
                CommentFeedbackReport commentFeedbackReport = new CommentFeedbackReport(report);
                commentFeedbackReport.setBookingsLogs(managerFacade.getReportBookings(report));
                commentFeedbackReport.setMessages(managerFacade.getAllMessages());
                request.setAttribute("viewingReport", commentFeedbackReport);
                break;
            case ROOM_STATUS:
                RoomStatusReport roomStatusReport = new RoomStatusReport(report);
                roomStatusReport.setBookingsLogs(managerFacade.getReportBookings(report));
                roomStatusReport.setRoomLogs(managerFacade.getReportRoom(report));
                request.setAttribute("viewingReport", roomStatusReport);
                break;
        }
                
        request.getRequestDispatcher("/manager/report.jsp").forward(request, response);
        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
