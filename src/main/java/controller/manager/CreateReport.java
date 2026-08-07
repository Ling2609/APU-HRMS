package controller.manager;

import entity.Report;
import entity.Report.ReportType;
import jakarta.ejb.EJB;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import session.ManagerFacade;
import session.ReportFacade;

@WebServlet(name = "CreateReport", urlPatterns = {"/manager/CreateReport"})
public class CreateReport extends HttpServlet {

    @EJB
    private ReportFacade reportFacade;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        
        try {
            
            LocalDate startTime = LocalDate.parse(request.getParameter("startTime").trim());
            LocalDate endTime = LocalDate.parse(request.getParameter("endTime").trim());
            ReportType type = Report.getReportType(request.getParameter("reportType").trim());
            LocalDateTime generateTime = LocalDateTime.now();
           
            Report report = new Report();
            report.setReportType(type);
            report.setStartTime(startTime);
            report.setEndTime(endTime);
            report.setGenerateTime(generateTime);
                        
            switch(type) {
                case FINANCIAL:
                    reportFacade.create(report);
                    Report financialReport = reportFacade.createFinancialReport(report);
                    break;
                case ARRIVAL_DEPARTURE:
                    reportFacade.create(report);
                    Report arrivalDepartureReport = reportFacade.createArrivalDepartureReport(report);
                    break;
                case COMMENT_FEEDBACK:
                    reportFacade.create(report);
                    Report noteReport = reportFacade.createNotesReport(report);
                    break;
                case TRANSACTION:
                    reportFacade.create(report);
                    Report transactionReport = reportFacade.createTransactionReport(report);
                    break;
                case ROOM_STATUS:
                    reportFacade.create(report);
                    Report roomReport = reportFacade.createRoomStatusReport(report);
                    break;
                default:
                    request.setAttribute("error", "Invalid report type for generation.");
                    request.getRequestDispatcher("/manager/ManageReport").forward(request, response);
            }
            
            request.setAttribute("success", "Report created successfully.");
            request.getRequestDispatcher("/manager/ManageReport").forward(request, response);
            
        }
        catch (Exception e) {
            request.setAttribute("error", "Invalid report details.");
            request.getRequestDispatcher("/manager/ManageReport").forward(request, response);
        }
        
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
