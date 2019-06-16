package publisaiz.tools.mailFormater;

import publisaiz.datasources.database.entities.User;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SystemMailTemplate {

    private User user;

    public String getTemplate(String templateName) throws IOException {
        File file = new File("templates/mailing/" + templateName + ".html");
        List<String> result = new ArrayList<>();
        if (file.exists() && file.isFile())
            result = Files.readAllLines(file.toPath());
        return result.stream().collect(Collectors.joining());
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
