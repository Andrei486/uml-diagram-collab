package carleton.sysc4907.command;

import carleton.sysc4907.command.Command;
import carleton.sysc4907.model.DiagramModel;
import carleton.sysc4907.processing.ElementIdManager;

import java.util.*;
import java.util.stream.Collectors;

public class CommandListCompressor {

    private final DiagramModel diagramModel;
    private final ElementIdManager elementIdManager;

    public CommandListCompressor(DiagramModel diagramModel, ElementIdManager elementIdManager) {
        this.diagramModel = diagramModel;
        this.elementIdManager = elementIdManager;
    }

    public List<Command<?>> compressCommandList(List<Command<?>> commandList) {
        var indices = new HashSet<Integer>();
        var usedIds = elementIdManager.getUsedIds();
        for (Long id : usedIds) {
            var idCompressedList = compressCommandListForId(filterById(commandList, id)); //TODO get only relevant commands
            indices.addAll(idCompressedList.stream().map(commandList::indexOf).toList());
        }
        return indices.stream().map(commandList::get).collect(Collectors.toList());
    }

    private List<Command<?>> filterById(List<Command<?>> commandList, Long id) {
        return commandList.stream().filter(
                cmd -> Arrays.stream(cmd.getArgs().getElementIds()).anyMatch(
                        id::equals
                )).toList();
    }

    private List<Command<?>> compressCommandListForId(List<Command<?>> idCommandList) {
        return idCommandList;
    }
}
