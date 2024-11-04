package hk.edu.polyu.comp.comp2021.cvfs.controller;

import hk.edu.polyu.comp.comp2021.cvfs.model.type.CommandType;
import java.util.HashMap;
import java.util.Map;

/**
 * The class to parse commands and return the type of the command. []
 */
public class CommandParser {

    private static final Map<String, CommandType> COMMAND_MAP = new HashMap<>();

    static {
        COMMAND_MAP.put("newDisk", CommandType.newDisk);
        COMMAND_MAP.put("newDoc", CommandType.newDoc);
        COMMAND_MAP.put("newDir", CommandType.newDir);
        COMMAND_MAP.put("delete", CommandType.delete);
        COMMAND_MAP.put("rename", CommandType.rename);
        COMMAND_MAP.put("changeDir", CommandType.changeDir);
        COMMAND_MAP.put("list", CommandType.list);
        COMMAND_MAP.put("rList", CommandType.rList);
        COMMAND_MAP.put("newSimpleCri", CommandType.newSimpleCri);
        COMMAND_MAP.put("newNegation", CommandType.newNegation);
        COMMAND_MAP.put("newBinaryCri", CommandType.newBinaryCri);
        COMMAND_MAP.put("printAllCriteria", CommandType.printAllCriteria);
        COMMAND_MAP.put("search", CommandType.search);
        COMMAND_MAP.put("rSearch", CommandType.rSearch);
        COMMAND_MAP.put("save", CommandType.save);
        COMMAND_MAP.put("load", CommandType.load);
        COMMAND_MAP.put("quit", CommandType.quit);
        COMMAND_MAP.put("undo", CommandType.undo);
        COMMAND_MAP.put("redo", CommandType.redo);
    }

    /**
     * @param command input
     * @return CommandType.invalid, or the valid type of the command
     */
    public static CommandType parseType(String command) {
        String[] elements = command.split(" ");
        if (command.equals("")) {
            System.out.println("Please Input Command");
            return CommandType.invalid;
        }

        CommandType type = COMMAND_MAP.get(elements[0]);
        if (type == null) {
            System.out.println("Unsupported Command: " + elements[0]);
            return CommandType.invalid;
        }
        return type;
    }
}
