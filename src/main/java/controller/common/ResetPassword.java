package controller.common;

import entity.User;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import session.UserFacade;

@WebServlet(name = "ResetPassword", urlPatterns = {"/common/ResetPassword"})
public class ResetPassword extends HttpServlet {

    @EJB
    private UserFacade userFacade;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null
                || !Boolean.TRUE.equals(session.getAttribute("resetVerified"))
                || session.getAttribute("resetEmail") == null) {

            response.sendRedirect(request.getContextPath() + "/common/ForgotPassword");
            return;
        }

        request.getRequestDispatcher("/common/resetPassword.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null
                || !Boolean.TRUE.equals(session.getAttribute("resetVerified"))
                || session.getAttribute("resetEmail") == null) {

            response.sendRedirect(request.getContextPath() + "/common/ForgotPassword");
            return;
        }

        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        if (password == null || password.length() < 6) {
            request.setAttribute("error", "Password must be at least 6 characters.");
            request.getRequestDispatcher("/common/resetPassword.jsp").forward(request, response);
            return;
        }

        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Passwords do not match.");
            request.getRequestDispatcher("/common/resetPassword.jsp").forward(request, response);
            return;
        }

        String email = (String) session.getAttribute("resetEmail");
        User user = userFacade.findByEmail(email);

        if (user == null) {
            session.removeAttribute("resetVerified");
            session.removeAttribute("resetEmail");

            request.setAttribute("error", "Account could not be found.");
            request.getRequestDispatcher("/common/forgotPassword.jsp").forward(request, response);
            return;
        }

        user.setPassword(password);
        userFacade.edit(user);

        session.removeAttribute("resetVerified");
        session.removeAttribute("resetEmail");

        response.sendRedirect(
                request.getContextPath()
                + "/common/login.jsp?resetSuccess=true"
        );
    }
}