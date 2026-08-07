package controller.common;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "VerifyResetCode", urlPatterns = {"/common/VerifyResetCode"})
public class VerifyResetCode extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("resetCode") == null) {
            response.sendRedirect(request.getContextPath() + "/common/ForgotPassword");
            return;
        }

        request.getRequestDispatcher("/common/verifyResetCode.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("resetCode") == null) {
            response.sendRedirect(request.getContextPath() + "/common/ForgotPassword");
            return;
        }

        String enteredCode = request.getParameter("code");
        String correctCode = (String) session.getAttribute("resetCode");
        Long expiry = (Long) session.getAttribute("resetCodeExpiry");

        if (expiry == null || System.currentTimeMillis() > expiry) {
            session.removeAttribute("resetCode");
            session.removeAttribute("resetCodeExpiry");
            session.removeAttribute("resetEmail");

            request.setAttribute("error", "Verification code has expired. Please request a new code.");
            request.getRequestDispatcher("/common/forgotPassword.jsp").forward(request, response);
            return;
        }

        if (enteredCode == null || !enteredCode.equals(correctCode)) {
            request.setAttribute("error", "Invalid verification code.");
            request.getRequestDispatcher("/common/verifyResetCode.jsp").forward(request, response);
            return;
        }

        session.setAttribute("resetVerified", true);
        session.removeAttribute("resetCode");
        session.removeAttribute("resetCodeExpiry");

        response.sendRedirect(request.getContextPath() + "/common/ResetPassword");
    }
}