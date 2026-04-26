package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.nio.file.Files;

public class GithubRepoController {
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
    private ComboBox<String> typeProjetField;

    @FXML
    private TextField GitignoreField;

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
            System.out.println("Project created");

        } catch (Exception e){
            e.printStackTrace();
        }
    }




    ///  Creatio of the repo Github
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
                System.out.println("Repo created on gitHub !");
            }
            else {
                System.out.println("API Github error: " + response);
                return;
            }


            File dest = cloneRepo();
            creationFichier(dest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    @FXML
    public File cloneRepo() {
        try {
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle("Choose a directory");
            File destination = chooser.showDialog(null);

            if (destination == null) {
                System.out.println("No directory chosen");
                return null;
            }

            String repoUrl = "https://github.com/QuentinLeger/" + repoNameField.getText() + ".git";
            runCommand("git clone " + repoUrl, destination);

            System.out.println("Repo cloned in " + destination.getAbsolutePath());


            return new File(destination, repoNameField.getText());

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    @FXML
    public void creationFichier(File dest){
        switch (typeProjetField.getValue()) {
            case "Application Java" -> {
                System.out.println("Java project creation:");
                generateJavaProject(dest);
            }
        }
    }


    @FXML
    public void generateJavaProject(File repoFolder){
        try {
            File src = new File(repoFolder, "src/main/java");
            File ressources = new File(repoFolder, "src/main/ressources");

            src.mkdirs();
            ressources.mkdirs();

            File mainClass = new File(src, "Main.java");
            String mainContent =             "public class Main {\n" +
                    "    public static void main(String[] args) {\n" +
                    "        System.out.println(\"Java project generated !\");\n" +
                    "    }\n" +
                    "}\n";

            Files.writeString(mainClass.toPath(),mainContent);

            File gitignore = new File(repoFolder, ".gitignore");
            String gitignoreContent =
                    ".idea/\n" +
                            "out/\n" +
                            "*.iml\n";
            Files.writeString(gitignore.toPath(),gitignoreContent);

            runCommand("git add .", repoFolder);
            runCommand("git commit -m \"Java structure created\"",repoFolder);

            System.out.println("Project initialized");
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
