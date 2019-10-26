package publisaiz.functionalities.error;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
class ErrorControllerImpl implements ErrorController {

    @RequestMapping("/error")
    @ResponseBody
    public String handleError(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");
        return String.format("<html>\n<body><h2>Error Page</h2>\n<div>Status code: <b>%s</b></div>\n"
                        + "<div>Exception Message: <b>%s</b>\n</div><body></html>",
                statusCode, exception == null ? "N/A" : exception.getMessage());
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
