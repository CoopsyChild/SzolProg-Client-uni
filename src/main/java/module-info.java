module com.example.progkornyjavafxuni {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    //requires mysql.connector.j;
    requires com.google.gson;
    requires unirest.java.core;
    requires unirest.object.mappers.gson;

    opens com.example.progkornyjavafxuni to javafx.fxml;
    exports com.example.progkornyjavafxuni;
    exports app;
    opens app to javafx.fxml,com.google.gson;
}