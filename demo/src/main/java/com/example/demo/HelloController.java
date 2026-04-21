package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.File;
import java.nio.file.Files;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    private TextField repoNameField;

    @FXML
    private TextField descriptionField;

    @FXML
    private TextField readmeField;

    @FXML
    public void runCommand(String command, File directory) throws Exception{
        ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c",command);
        builder.directory(directory);
        builder.redirectErrorStream(true);
        Process process = builder.start();
        process.waitFor();

    }
    @FXML
    public void onCreateProject() {
        try {
            File projectFolder = new File("C:\\Users\\atomi\\OneDrive\\Bureau\\Informatique\\Perso\\Portfolio\\Creation_dun_repo_Github\\resultatTest");
            projectFolder.mkdirs();
            Files.writeString(new File(projectFolder,"README.md").toPath(),"#Mon projet");
            runCommand("git init", projectFolder);
            runCommand("git add .", projectFolder);
            runCommand("git commit -m \"Initial Commit\"", projectFolder);
            System.out.println("Projet crée");

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    public void onCreateProjectAPI(){
        try {

            GitHubService gitHub = new GitHubService("");

            int response = gitHub.createRepository(
                    repoNameField.getText(),
                    descriptionField.getText(),
                    false
            );

            System.out.println("Github API response :" + response);

            if (response == 201){
                System.out.println("Repo crée sur github !");
            }
            else {
                System.out.println("Erreur API Github: " + response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
