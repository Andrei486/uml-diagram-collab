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

public class ChangeTextStyleCommand implements Command<ChangeTextStyleCommandArgs>{

    private final ElementIdManager idManager;

    private final ChangeTextStyleCommandArgs args;

    public ChangeTextStyleCommand(ChangeTextStyleCommandArgs args, ElementIdManager idManager) {
        this.idManager = idManager;
        this.args = args;
    }

    @Override
    public void execute() {
        TextStyleProperty propertyToChange = args.property();
        Object valueToApply = args.value();

        //after null check cast to label
        Node labelNode = idManager.getElementById(args.elementId());
        if (labelNode == null) {
            return;
        }
        Label label = (Label) labelNode;

        //check if cast fails?
        EditableLabelController controller = (EditableLabelController) label.getParent().getProperties().get("controller");

        TextArea textField = controller.getEditableText();

        switch (propertyToChange) {
            case BOLD -> {
                System.out.println("bold");
                //should probably handle this potential casting error
                boolean value = (boolean) valueToApply;

                if (value) {
                    textField.getStyleClass().add("bolded");
                    label.getStyleClass().add("bolded");

                } else {
                    textField.getStyleClass().removeAll("bolded");
                    label.getStyleClass().removeAll("bolded");
                }
                /*
                textField.setStyle("-fx-font-weight: " + valueToApply);
                label.setStyle("-fx-font-weight: "+ valueToApply);

                 */
                break;
            }
            case SIZE -> {
                System.out.println("size");
                String fontFamily = label.getFont().getFamily();
                Font newFont = new Font(fontFamily, (Double) valueToApply);

                //textField.setFont(newFont);
                //label.setFont(newFont);
                break;
            }
            case ITALICS -> {
                System.out.println("italics");
                //should probably handle this potential casting error
                boolean value = (boolean) valueToApply;

                if (value) {
                    textField.getStyleClass().add("italicized");
                    label.getStyleClass().add("italicized");

                } else {
                    textField.getStyleClass().removeAll("italicized");
                    label.getStyleClass().removeAll("italicized");
                }
                /*
                textField.setStyle("-fx-font-style: " + valueToApply);
                label.setStyle("-fx-font-style: "+ valueToApply);

                 */
                break;
            }
            case UNDERLINE -> {
                System.out.println("underline " + valueToApply);
                //should probably handle this potential casting error
                boolean value = (boolean) valueToApply;

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

                //set font to normal font first (it will not change otherwise)
                //Font normalFont = Font.font(20);
                //label.setFont(normalFont);
                //textField.setFont(normalFont);

                //try 1
                //textField.setFont(Font.font ((String) valueToApply, oldFontSize));
                //label.setFont(Font.font ((String) valueToApply, oldFontSize));

                //try 2
                //textField.setStyle("-fx-font-family: " + valueToApply + "; -fx-font-size: " + oldFontSize + ";");

                //try 3
                //Font newFont = new Font((String) valueToApply, oldFontSize);
                //textField.setFont(newFont);
                //label.setFont(newFont);

                //try 4
                textField.setStyle("-fx-font-family: " + valueToApply + "; -fx-font-size: " + oldFontSize + ";");
                label.setStyle("-fx-font-family: " + valueToApply + "; -fx-font-size: " + oldFontSize + ";");
                break;
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
