package fr.eurecom.quotes;
import java.io.IOException;
import javax.servlet.http.*;

@SuppressWarnings("serial")
public class QuotesServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println("Hello, world");
	}
}


// auto-import: Ctrl + Shift + O
// auto-format: Ctrl + Shift + F
// comment:		Ctrl + Shift + :
// move code:	Alt + up/down