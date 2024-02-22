package carleton.sysc4907.command;

import carleton.sysc4907.command.args.ChangeTextStyleCommandArgs;
import carleton.sysc4907.command.args.EditTextCommandArgs;
import carleton.sysc4907.processing.ElementIdManager;
import javafx.beans.property.Property;
import org.w3c.dom.Text;

import java.util.EnumMap;

public class ChangeTextStyleCommand implements Command<ChangeTextStyleCommandArgs>{

    private EnumMap<TextStyleProperty, Property> propertyEnumMap = new EnumMap<TextStyleProperty, Property>();

    public ChangeTextStyleCommand(ChangeTextStyleCommandArgs args, ElementIdManager idManager) {
        propertyEnumMap.put(TextStyleProperty.BOLD, idManager.getElementById(args.elementId()).)
    }
    //use an enum map to map the enum properties to real properties
}
