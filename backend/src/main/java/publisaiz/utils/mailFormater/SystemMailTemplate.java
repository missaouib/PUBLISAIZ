package publisaiz.utils.mailFormater;

import publisaiz.entities.User;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

class SystemMailTemplate {

    private User user;

    public String getTemplate(String templateName) throws IOException {
        File file = new File("templates/mailing/" + templateName + ".html");
        List<String> result = new ArrayList<>();
        return readFileAsText(file, result);
    }

    private String readFileAsText(File file, List<String> result) throws IOException {
        if (file.exists() && file.isFile())
            result = Files.readAllLines(file.toPath());
        return String.join("", result);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
