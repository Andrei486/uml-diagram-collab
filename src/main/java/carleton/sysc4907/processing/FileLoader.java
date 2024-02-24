package carleton.sysc4907.processing;

import carleton.sysc4907.DiagramEditorLoader;
import javafx.stage.Stage;

import java.io.*;

public class FileLoader {

    private final DiagramEditorLoader diagramEditorLoader;

    public FileLoader(DiagramEditorLoader diagramEditorLoader) {
        this.diagramEditorLoader = diagramEditorLoader;
    }

    public Object[] deserializeArgsFromFile(File file) throws IOException {
        Object[] objects;
        try (ObjectInputStream outputStream = new ObjectInputStream(new FileInputStream(file))) {
            objects = (Object[]) outputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            // File did not exist and could not be created
            e.printStackTrace();
            throw new IOException("Error while reading file " + file.getAbsolutePath());
        }
        return objects;
    }

    public void open(File file, String roomCode, String username, Stage stage) throws IOException {
        Object[] argsList = deserializeArgsFromFile(file);
        diagramEditorLoader.createAndLoad(stage, username, roomCode, argsList);
        diagramEditorLoader.getLoadedDiagramModel().setLoadedFilePath(file.getPath());
    }
}
