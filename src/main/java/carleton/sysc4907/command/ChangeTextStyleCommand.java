package carleton.sysc4907.command;

import carleton.sysc4907.command.args.ChangeTextStyleCommandArgs;
import carleton.sysc4907.command.args.EditTextCommandArgs;
import carleton.sysc4907.controller.element.EditableLabelController;
import carleton.sysc4907.processing.ElementIdManager;
import javafx.beans.property.Property;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.text.Font;
import org.w3c.dom.Text;

import java.util.EnumMap;
import java.util.HashMap;

/**
 * A command to change the styling of the text in a label.
 */
public class ChangeTextStyleCommand implements Command<ChangeTextStyleCommandArgs>{

    private final ElementIdManager idManager;

    private final ChangeTextStyleCommandArgs args;

    /**
     * Constructs a ChangeTextStyleCommand.
     * @param args the arguments for the command.
     * @param idManager the element ID manager.
     */
    public ChangeTextStyleCommand(ChangeTextStyleCommandArgs args, ElementIdManager idManager) {
        this.idManager = idManager;
        this.args = args;
    }

    @Override
    public void execute() {
        if (args.elementId() == null) {
            return;
        }
        TextStyleProperty propertyToChange = args.property();
        Object valueToApply = args.value();

        //get the label from the ID
        Node labelNode = idManager.getElementById(args.elementId());
        if (labelNode == null) {
            return;
        }
        if (!(labelNode instanceof Label label)) {
            System.out.println("Error: ID was not for a label (in ChangeTextStyleCommand)");
            return;
        }

        //get the controller for the label
        if (!(label.getParent().getProperties().get("controller") instanceof EditableLabelController controller)) {
            System.out.println("Error: The controller for the given ID was not found (in ChangeTextStyleCommand)");
            return;
        }

        TextArea textField = controller.getEditableText();

        // The action taken depends on the property to change.
        // Bold, underline, and italics are done by adding and removing style classes as changing the style directly
        // overwrites all other styling.
        // Size and font family cannot be done with classes so they both need to set font family and size.
        switch (propertyToChange) {
            case BOLD -> {
                System.out.println("bold");

                if (!(valueToApply instanceof Boolean value)) {
                    return;
                }

                if (value) {
                    textField.getStyleClass().add("bolded");
                    label.getStyleClass().add("bolded");

                } else {
                    textField.getStyleClass().removeAll("bolded");
                    label.getStyleClass().removeAll("bolded");
                }
                break;
            }
            case SIZE -> {
                System.out.println("size");
                String fontFamily = label.getFont().getFamily();
                textField.setStyle("-fx-font-family: \"" + fontFamily + "\"; -fx-font-size: " + valueToApply + ";");
                label.setStyle("-fx-font-family: \"" + fontFamily + "\"; -fx-font-size: " + valueToApply + ";");
                break;
            }
            case ITALICS -> {
                System.out.println("italics");

                if (!(valueToApply instanceof Boolean value)) {
                    return;
                }

                if (value) {
                    textField.getStyleClass().add("italicized");
                    label.getStyleClass().add("italicized");

                } else {
                    textField.getStyleClass().removeAll("italicized");
                    label.getStyleClass().removeAll("italicized");
                }
                break;
            }
            case UNDERLINE -> {
                System.out.println("underline " + valueToApply);

                if (!(valueToApply instanceof Boolean value)) {
                    return;
                }

                if (value) {
                    textField.getStyleClass().add("underlined");
                    label.getStyleClass().add("underlined");

                } else {
                    textField.getStyleClass().removeAll("underlined");
                    label.getStyleClass().removeAll("underlined");
                }
                break;
            }
            case FONT_FAMILY -> {
                System.out.println("font-family");
                double oldFontSize = label.getFont().getSize();
                textField.setStyle("-fx-font-family: \"" + valueToApply + "\"; -fx-font-size: " + oldFontSize + ";");
                label.setStyle("-fx-font-family: \"" + valueToApply + "\"; -fx-font-size: " + oldFontSize + ";");
                break;
            }
        }
    }

    @Override
    public ChangeTextStyleCommandArgs getArgs() {
        return args;
    }

}
