package carleton.sysc4907.command;

import carleton.sysc4907.command.args.ChangeTextStyleCommandArgs;
import carleton.sysc4907.command.args.EditTextCommandArgs;
import carleton.sysc4907.controller.element.EditableLabelController;
import carleton.sysc4907.processing.ElementIdManager;
import javafx.beans.property.Property;
import javafx.scene.text.Font;
import org.w3c.dom.Text;

import java.util.EnumMap;
import java.util.HashMap;

public class ChangeTextStyleCommand implements Command<ChangeTextStyleCommandArgs>{

    private ElementIdManager idManager;

    private ChangeTextStyleCommandArgs args;

    public ChangeTextStyleCommand(ChangeTextStyleCommandArgs args, ElementIdManager idManager) {
        this.idManager = idManager;
        this.args = args;
    }

    @Override
    public void execute() {
        TextStyleProperty propertyToChange = args.property();
        Object valueToApply = args.value();
        EditableLabelController controller = (EditableLabelController) idManager.getElementById(args.elementId()).getProperties().get("controller");
        HashMap<TextStyleProperty, String> oldFontProperties = parseOldFontStyle(controller.getLabel().getFont());

        switch (propertyToChange) {
            case BOLD -> {
                return;
            }
            case SIZE -> {
                return;
            }
            case ITALICS -> {
                return;
            }
            case UNDERLINE -> {
                return;
            }
            case FONT_FAMILY -> {
                return;
            }
        }
    }

    private HashMap<TextStyleProperty, String> parseOldFontStyle(Font oldFont) {
        System.out.print(oldFont.getStyle());
        return null;
    }

    @Override
    public ChangeTextStyleCommandArgs getArgs() {
        return args;
    }

}
