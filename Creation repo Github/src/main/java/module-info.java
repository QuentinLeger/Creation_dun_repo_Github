module com.example.creationrepogithub {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.example.creationrepogithub to javafx.fxml;
    exports com.example.creationrepogithub;
}