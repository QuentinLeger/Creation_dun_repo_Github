package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

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
    private String repoUrl = "https://github.com/QuentinLeger/";

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

            GitHubService gitHub = new GitHubService("YOUR TOKEN");

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
                return;
            }

            cloneRepo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void cloneRepo() {
        try {
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle("Choisir un dossier de destination");
            File destination = chooser.showDialog(null);

            if (destination == null) {
                System.out.println("Aucun dossier choisi");
                return;
            }
            String repoUrl = "https://github.com/QuentinLeger/" + repoNameField.getText() + ".git";
            runCommand("git clone " + repoUrl, destination);

            System.out.println("Repo cloné dans " + destination.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
