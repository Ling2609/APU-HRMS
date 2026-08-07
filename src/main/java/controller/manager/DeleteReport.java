package controller.manager;

import entity.Report;
import jakarta.ejb.EJB;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import session.ManagerFacade;
import session.ReportFacade;

@WebServlet(name = "DeleteReport", urlPatterns = {"/manager/DeleteReport"})
public class DeleteReport extends HttpServlet {

    @EJB
    private ManagerFacade managerFacade;
    
    @EJB
    private ReportFacade reportFacade;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
               
        String action = request.getParameter("action");
        String reportID = request.getParameter("reportID");
        
        if(!"delete".equals(action)) {
            request.setAttribute("error", "Invalid action.");
            request.getRequestDispatcher("/manager/ManageReport").forward(request, response);
            return;
        }
        
        Report report = (Report)managerFacade.getReportById(reportID);
        reportFacade.deleteReport(report);
        
        request.setAttribute("success", "Report deleted successfully.");
        request.getRequestDispatcher("/manager/ManageReport").forward(request, response);
        
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
