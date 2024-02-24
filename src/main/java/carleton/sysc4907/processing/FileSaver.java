package carleton.sysc4907.processing;

import carleton.sysc4907.command.Command;
import carleton.sysc4907.model.DiagramModel;
import carleton.sysc4907.model.ExecutedCommandList;

import java.io.*;

public class FileSaver {

    private final DiagramModel diagramModel;
    private final ExecutedCommandList executedCommandList;
    public FileSaver(DiagramModel diagramModel, ExecutedCommandList executedCommandList) {
        this.diagramModel = diagramModel;
        this.executedCommandList = executedCommandList;
    }

    public boolean save() {
        return saveAs(diagramModel.getLoadedFilePath());
    }

    public boolean saveAs(String filePath) {
        File saveFile = new File(filePath);
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(saveFile, false))) {
            saveFile.createNewFile();
            outputStream.writeObject(getSaveableCommandList());
        } catch (IOException e) {
            // File did not exist and could not be created
            e.printStackTrace();
            return false;
        }
        diagramModel.setLoadedFilePath(filePath);
        return true;
    }

    /**
     * Returns a serializable object that can be saved to a file to represent the commands that have been run.
     * @return the object to write to the save file
     */
    private Object getSaveableCommandList() {
        var commandList = executedCommandList.getCommandList();
        return commandList.stream().map(Command::getArgs).toArray();
    }
}
