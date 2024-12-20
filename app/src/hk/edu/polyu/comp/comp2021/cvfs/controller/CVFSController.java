// package hk.edu.polyu.comp.comp2021.cvfs.controller;

// import hk.edu.polyu.comp.comp2021.cvfs.model.*;
// import hk.edu.polyu.comp.comp2021.cvfs.view.CVFSView;

// import java.util.HashMap;
// import java.util.Scanner;

// /**
//  * the control element of the CVFS
//  */
// public class CVFSController {

//     private final CVFS cvfs;
//     private final CVFSView view;
//     private final loggerParser logger = new loggerParser();
//     /**
//      * To deal with the user input.
//      */
//     private final Scanner scanner = new Scanner(System.in);
//     private final static String numParamError = "Wrong number of parameter(s), please follow command template: \033[4m";

//     /**
//      * Initialize the CVFS Controller.
//      *
//      * @param cvfs the CVFS model to be used.
//      * @param view the CVFS view to be used.
//      */
//     public CVFSController(CVFS cvfs, CVFSView view) {
//         this.cvfs = cvfs;
//         this.view = view;
//     }

//     /**
//      * @return command input from the keyboard or the system
//      */
//     public String getCommand() {
//         return scanner.nextLine();
//     }

//     /**
//      * @param type type of command input
//      * @param command command input
//      */
//     public void processCommand(CommandType type, String command) {

//         String[] elements = command.split(" ");
//         Directory twd;
//         String tname;
//         Object[] tres;

//         switch (type) {
//             case newDisk:
//                 if (elements.length != 2) {
//                     throw new IllegalArgumentException(numParamError + "[newDisk diskSize]");
//                 }
//                 if (!elements[1].matches("^[1-9]\\d*$")) {
//                     throw new IllegalArgumentException("Invalid diskSize, must be a positive integer.");
//                 }

//                 cvfs.newDisk(Integer.parseInt(elements[1]));
//                 return;

//             case newDoc:
//                 if (cvfs.getCwd() == null) {
//                     throw new IllegalStateException("Please first create a disk.");
//                 }
//                 if (elements.length < 4) {
//                     throw new IllegalArgumentException(numParamError + "[newDoc docName docType docContent]");
//                 }
//                 if (!DocType.isValidDocType(elements[2])) {
//                     throw new IllegalArgumentException("Invalid document type: " + elements[2]);
//                 }

//                 StringBuilder docContent = new StringBuilder(elements[3]);
//                 for (int i = 4; i < elements.length; i++) {
//                     docContent.append(' ').append(elements[i]);
//                 }

//                 tres = cvfs.parsePath(elements[1]);
//                 twd = (Directory) tres[0];
//                 tname = (String) tres[1];
//                 if (!Unit.isValidName(tname)) {
//                     throw new IllegalArgumentException("Invalid document name: " + tname);
//                 }

//                 twd.newDoc(tname, DocType.parse(elements[2]), docContent.toString());
//                 return;

//             case newDir:
//                 if (cvfs.getCwd() == null) {
//                     throw new IllegalStateException("Please first create a disk.");
//                 }
//                 if (elements.length != 2) {
//                     throw new IllegalArgumentException(numParamError + "[newDir dirName]");
//                 }
//                 tres = cvfs.parsePath(elements[1]);
//                 twd = (Directory) tres[0];
//                 tname = (String) tres[1];
//                 if (!Unit.isValidName(tname)) {
//                     throw new IllegalArgumentException("Invalid directory name: " + tname);
//                 }

//                 twd.newDir(tname);
//                 return;

//             case list:
//                 if (cvfs.getCwd() == null) {
//                     throw new IllegalStateException("Please first create a disk.");
//                 }
//                 if (elements.length != 1) {
//                     throw new IllegalArgumentException(numParamError + "[list]");
//                 }

//                 cvfs.getCwd().list();
//                 return;

//             case rList:
//                 if (cvfs.getCwd() == null) {
//                     throw new IllegalStateException("Please first create a disk.");
//                 }
//                 if (elements.length != 1) {
//                     throw new IllegalArgumentException(numParamError + "[rList]");
//                 }

//                 cvfs.getCwd().rList();
//                 return;

//             case search:
//                 if (cvfs.getCwd() == null) {
//                     throw new IllegalStateException("Please first create a disk.");
//                 }
//                 if (elements.length != 2) {
//                     throw new IllegalArgumentException(numParamError + "[search criName]");
//                 }
//                 if (cvfs.getCri(elements[1]) == null) {
//                     throw new IllegalArgumentException("Invalid criterion name: " + elements[1]);
//                 }

//                 cvfs.getCwd().search(cvfs.getCri(elements[1]));
//                 return;

//             case rSearch:
//                 if (cvfs.getCwd() == null) {
//                     throw new IllegalStateException("Please first create a disk.");
//                 }
//                 if (elements.length != 2) {
//                     throw new IllegalArgumentException(numParamError + "[rSearch criName]");
//                 }
//                 if (cvfs.getCri(elements[1]) == null) {
//                     throw new IllegalArgumentException("Invalid criterion name: " + elements[1]);
//                 }

//                 cvfs.getCwd().rSearch(cvfs.getCri(elements[1]));
//                 return;

//             case rename:
//                 if (cvfs.getCwd() == null) {
//                     throw new IllegalStateException("Please first create a disk.");
//                 }
//                 if (elements.length != 3) {
//                     throw new IllegalArgumentException(numParamError + "[rename oldFileName newFileName]");
//                 }
//                 if (!Unit.isValidName(elements[2])) {
//                     throw new IllegalArgumentException("Invalid new name: " + elements[2]);
//                 }

//                 tres = cvfs.parsePath(elements[1]);
//                 twd = (Directory) tres[0];
//                 tname = (String) tres[1];
//                 if (!Unit.isValidName(tname)) {
//                     throw new IllegalArgumentException("Invalid old name: " + tname);
//                 }

//                 twd.rename(tname, elements[2]);
//                 return;

//             case delete:
//                 if (cvfs.getCwd() == null) {
//                     throw new IllegalStateException("Please first create a disk.");
//                 }
//                 if (elements.length != 2) {
//                     throw new IllegalArgumentException(numParamError + "[delete fileName]");
//                 }

//                 tres = cvfs.parsePath(elements[1]);
//                 twd = (Directory) tres[0];
//                 tname = (String) tres[1];
//                 if (!Unit.isValidName(tname)) {
//                     throw new IllegalArgumentException("Invalid file name: " + tname);
//                 }

//                 twd.delete(tname);
//                 return;

//             case changeDir:
//                 if (cvfs.getCwd() == null) {
//                     throw new IllegalStateException("Please first create a disk.");
//                 }
//                 if (elements.length != 2) {
//                     throw new IllegalArgumentException(numParamError + "[changeDir dirName]");
//                 }

//                 cvfs.changeDir(elements[1]);
//                 return;

//             case newNegation:
//                 if (elements.length != 3) {
//                     throw new IllegalArgumentException(numParamError + "[newNegation criName1 criName2]");
//                 }
//                 for (int i = 1; i <= 2; i++) {
//                     if (!Criterion.isValidCriName(elements[i])) {
//                         throw new IllegalArgumentException("Invalid Criterion name: " + elements[i]);
//                     }
//                 }

//                 cvfs.newNegation(elements[1], elements[2]);
//                 return;

//             case newBinaryCri:
//                 if (elements.length != 5) {
//                     throw new IllegalArgumentException(numParamError
//                             + "[newBinaryCri criName1 criName3 logicOp criName4]");
//                 }
//                 // check 1, 2, 4 cri's name validality
//                 for (int i = 1; i <= 4; i *= 2) {
//                     if (!Criterion.isValidCriName(elements[i])) {
//                         throw new IllegalArgumentException("Invalid Criterion name: " + elements[i]);
//                     }
//                 }
//                 if (!BinCri.isValidOperator(elements[3])) {
//                     throw new IllegalArgumentException("Invalid logic operator, must be && or ||");
//                 }

//                 cvfs.newBinaryCri(elements[1], elements[2], elements[3], elements[4]);
//                 return;

//             case newSimpleCri:
//                 if (elements.length != 5) {
//                     throw new IllegalArgumentException(numParamError + "[newSimpleCri criName attrName op val]");
//                 }
//                 if (!Criterion.isValidCriName(elements[1])) {
//                     throw new IllegalArgumentException("Invalid Criterion name '" + elements[1] + "'");
//                 }
//                 if (!Criterion.isValidCri(elements[1], elements[2], elements[3], elements[4])) {
//                     throw new IllegalArgumentException("Invalid Criterion argument "
//                             + "'" + elements[2] + " " + elements[3] + " " + elements[4] + "'");
//                 }

//                 cvfs.newSimpleCri(elements[1], elements[2], elements[3], elements[4]);
//                 return;

//             case printAllCriteria:
//                 if (elements.length != 1) {
//                     throw new IllegalArgumentException(numParamError + "[printAllCriteria]");
//                 }

//                 cvfs.printAllCriteria();
//                 return;

//             case undo:
//                 if (elements.length != 1) {
//                     throw new IllegalArgumentException(numParamError + "[undo]");
//                 }

//                 logger.undo();
//                 return;

//             case redo:
//                 if (elements.length != 1) {
//                     throw new IllegalArgumentException(numParamError + "[redo]");
//                 }

//                 logger.redo();
//                 return;

//             case load:
//                 if (elements.length != 2) {
//                     throw new IllegalArgumentException(numParamError + "[load diskStoreName]");
//                 }

//                 cvfs.load(elements[1]);
//                 return;

//             case save:
//                 if (cvfs.getCwd() == null) {
//                     throw new IllegalStateException("Please first create a disk.");
//                 }
//                 if (elements.length != 2) {
//                     throw new IllegalArgumentException(numParamError + "[save diskStoreName]");
//                 }

//                 cvfs.save(elements[1]);
//                 TraceLogger.getInstance().newLog(TraceLogger.OpType.DD, elements[1], cvfs);
//                 return;
//             case quit:
//                 System.exit(0);
//         }

//     }

//     /**
//      * The user-interface, receiving and handling user requests.
//      */
//     public void terminal() {

//         view.updateDir(cvfs.getCwd());

//         String command;

//         view.printPrompt();
//         command = getCommand();

//         CommandType type = CommandSwitch.getType(command);

//         while (type == CommandType.invalid) {
//             view.printPrompt();
//             command = getCommand();
//             type = CommandSwitch.getType(command);
//         }
//         try {
//             processCommand(type, command);
//         } catch (Exception e) {
//             System.out.println("\033[91m" + "Error: " + e.getLocalizedMessage() + "\033[0m");
//         }
//     }

//     /**
//      * Used to parse and operate undo and redo command
//      */
//     public static class loggerParser {

//         /**
//          * Add an object.
//          */
//         private final Ops add = args -> {
//             Object obj = args[0];
//             if (obj instanceof Unit) {
//                 Unit unit = (Unit) obj;
//                 Directory parent = (Directory) args[1];
//                 parent.getCatalog().put(unit.getName(), unit);
//                 parent.updateSizeBy(unit.getSize());
//             } else {
//                 Criterion cri = (Criterion) obj;
//                 CVFS cvfs = (CVFS) args[1];
//                 cvfs.getCriteria().put(cri.getName(), cri);
//             }
//         };
//         /**
//          * Delete an Object.
//          */
//         private final Ops del = args -> {
//             Object obj = args[0];
//             if (obj instanceof Unit) {
//                 Unit unit = (Unit) obj;
//                 Directory parent = (Directory) args[1];
//                 parent.getCatalog().remove(unit.getName());
//                 parent.updateSizeBy(-unit.getSize());
//             } else {
//                 Criterion cri = (Criterion) obj;
//                 CVFS cvfs = (CVFS) args[1];
//                 cvfs.getCriteria().remove(cri.getName());
//             }
//         };
//         /**
//          * Rename an object.
//          */
//         private final Ops ren = args -> {
//             Unit unit = (Unit) args[0];
//             String newName = (String) args[1];
//             String oldName = (String) args[2];
//             unit.setName(newName);
//             Directory parent = (Directory) unit.getParent();
//             parent.getCatalog().remove(oldName);
//             parent.getCatalog().put(newName, unit);
//         };
//         /**
//          * Change directory.
//          */
//         private final Ops cd = args -> {
//             Directory newDir = (Directory) args[0];
//             CVFS cvfs = (CVFS) args[2];
//             cvfs.setCwd(newDir);
//         };
//         /**
//          * Switch to another disk.
//          */
//         private final Ops sd = args -> {
//             Disk newDisk = (Disk) args[0];
//             CVFS cvfs = (CVFS) args[2];
//             cvfs.setDisk(newDisk);
//         };
//         /**
//          * Delete a disk.
//          */
//         private final Ops dd = args -> {
//             String name = (String) args[0];
//             CVFS cvfs = (CVFS) args[1];
//             cvfs.delDisk(name);

//         };
//         /**
//          * Store a disk to local storage.
//          */
//         private final Ops ld = args -> {
//             String name = (String) args[0];
//             CVFS cvfs = (CVFS) args[1];
//             cvfs.store(name);

//         };
//         private final TraceLogger logger = TraceLogger.getInstance();
//         private final HashMap<TraceLogger.OpType, Ops> typeMap = new HashMap<>();

//         {
//             typeMap.put(TraceLogger.OpType.ADD, add);
//             typeMap.put(TraceLogger.OpType.DEL, del);
//             typeMap.put(TraceLogger.OpType.REN, ren);
//             typeMap.put(TraceLogger.OpType.CD, cd);
//             typeMap.put(TraceLogger.OpType.SD, sd);
//             typeMap.put(TraceLogger.OpType.DD, dd);
//             typeMap.put(TraceLogger.OpType.LD, ld);
//         }

//         /**
//          * To parse and operate a log.
//          *
//          * @param log The log to be parsed.
//          */
//         public void parse(TraceLogger.Tracelog log) {
//             TraceLogger.OpType type = log.getType();
//             Object[] args = log.getArgs();
//             Ops ops = typeMap.get(type);
//             ops.operate(args);
//         }

//         /**
//          * To undo a step.
//          */
//         public void undo() {
//             TraceLogger.Tracelog log;
//             log = logger.getlog();
//             parse(log);
//         }

//         /**
//          * To redo a step.
//          */
//         public void redo() {
//             TraceLogger.Tracelog log;
//             log = logger.getRlog();
//             parse(log);

//         }

//         /**
//          * Reflect on different kinds of optypes.
//          */
//         public interface Ops {

//             /**
//              * To operate on an logger.
//              *
//              * @param args The arguments of the operation.
//              */
//             void operate(Object[] args);
//         }
//     }

// }
package hk.edu.polyu.comp.comp2021.cvfs.controller;


import hk.edu.polyu.comp.comp2021.cvfs.model.*;
import hk.edu.polyu.comp.comp2021.cvfs.model.type.CommandType;
import hk.edu.polyu.comp.comp2021.cvfs.model.criteria.Criterion;
import hk.edu.polyu.comp.comp2021.cvfs.model.criteria.BinaryCriterion;
import hk.edu.polyu.comp.comp2021.cvfs.view.ConsoleView;
import hk.edu.polyu.comp.comp2021.cvfs.model.filesystem.Directory;
import hk.edu.polyu.comp.comp2021.cvfs.model.filesystem.Node;
import hk.edu.polyu.comp.comp2021.cvfs.model.filesystem.Disk;
import hk.edu.polyu.comp.comp2021.cvfs.model.type.DocumentType;
import hk.edu.polyu.comp.comp2021.cvfs.model.terminal.HistoryLogger;

import java.util.HashMap;
import java.util.Scanner;

/**
 * the control element of the CVFS
 */
public class CVFSController {
    private final CVFS cvfs;
    private final ConsoleView view;
    private final loggerParser logger = new loggerParser();
    /**
     * To deal with the user input.
     */
    private final Scanner scanner = new Scanner(System.in);
    private final static String numParamError = "Wrong number of parameter(s), please follow command template: \033[4m";


    /**
     * Initialize the CVFS Controller.
     *
     * @param cvfs the CVFS model to be used.
     * @param view the CVFS view to be used.
     */
    public CVFSController(CVFS cvfs, ConsoleView view) {
        this.cvfs = cvfs;
        this.view = view;
    }

    /**
     * @return command input from the keyboard or the system
     */
    public String getCommand() {
        return scanner.nextLine();
    }


    /**
     * @param type    type of command input
     * @param command command input
     */
    public void processCommand(CommandType type, String command) {

        String[] elements = command.split(" ");
        Directory twd;
        String tname;
        Object[] tres;

        switch (type) {
            case newDisk:
                if (elements.length != 2)
                    throw new IllegalArgumentException(numParamError + "[newDisk diskSize]");
                if (!elements[1].matches("^[1-9]\\d*$"))
                    throw new IllegalArgumentException("Invalid diskSize, must be a positive integer.");

                cvfs.newDisk(Integer.parseInt(elements[1]));
                return;

            case newDoc:
                if (cvfs.getCwd() == null)
                    throw new IllegalStateException("Please first create a disk.");
                if (elements.length < 4)
                    throw new IllegalArgumentException(numParamError + "[newDoc docName docType docContent]");
                if (!DocumentType.isValidDocumentType(elements[2]))
                    throw new IllegalArgumentException("Invalid document type: " + elements[2]);

                StringBuilder docContent = new StringBuilder(elements[3]);
                for (int i = 4; i < elements.length; i++)
                    docContent.append(' ').append(elements[i]);

                tres = cvfs.parsePath(elements[1]);
                twd = (Directory) tres[0];
                tname = (String) tres[1];
                if (!Node.isValidName(tname))
                    throw new IllegalArgumentException("Invalid document name: " + tname);

                twd.newDoc(tname, DocumentType.parse(elements[2]), docContent.toString());
                return;

            case newDir:
                if (cvfs.getCwd() == null)
                    throw new IllegalStateException("Please first create a disk.");
                if (elements.length != 2)
                    throw new IllegalArgumentException(numParamError + "[newDir dirName]");
                tres = cvfs.parsePath(elements[1]);
                twd = (Directory) tres[0];
                tname = (String) tres[1];
                if (!Node.isValidName(tname))
                    throw new IllegalArgumentException("Invalid directory name: " + tname);

                twd.newDir(tname);
                return;

            case list:
                if (cvfs.getCwd() == null)
                    throw new IllegalStateException("Please first create a disk.");
                if (elements.length != 1)
                    throw new IllegalArgumentException(numParamError + "[list]");

                cvfs.getCwd().list();
                return;

            case rList:
                if (cvfs.getCwd() == null)
                    throw new IllegalStateException("Please first create a disk.");
                if (elements.length != 1)
                    throw new IllegalArgumentException(numParamError + "[rList]");

                cvfs.getCwd().rList();
                return;

            case search:
                if (cvfs.getCwd() == null)
                    throw new IllegalStateException("Please first create a disk.");
                if (elements.length != 2)
                    throw new IllegalArgumentException(numParamError + "[search criName]");
                if (cvfs.getCri(elements[1]) == null)
                    throw new IllegalArgumentException("Invalid criterion name: " + elements[1]);

                cvfs.getCwd().search(cvfs.getCri(elements[1]));
                return;

            case rSearch:
                if (cvfs.getCwd() == null)
                    throw new IllegalStateException("Please first create a disk.");
                if (elements.length != 2)
                    throw new IllegalArgumentException(numParamError + "[rSearch criName]");
                if (cvfs.getCri(elements[1]) == null)
                    throw new IllegalArgumentException("Invalid criterion name: " + elements[1]);

                cvfs.getCwd().rSearch(cvfs.getCri(elements[1]));
                return;

            case rename:
                if (cvfs.getCwd() == null)
                    throw new IllegalStateException("Please first create a disk.");
                if (elements.length != 3)
                    throw new IllegalArgumentException(numParamError + "[rename oldFileName newFileName]");
                if (!Node.isValidName(elements[2]))
                    throw new IllegalArgumentException("Invalid new name: " + elements[2]);

                tres = cvfs.parsePath(elements[1]);
                twd = (Directory) tres[0];
                tname = (String) tres[1];
                if (!Node.isValidName(tname))
                    throw new IllegalArgumentException("Invalid old name: " + tname);

                twd.rename(tname, elements[2]);
                return;

            case delete:
                if (cvfs.getCwd() == null)
                    throw new IllegalStateException("Please first create a disk.");
                if (elements.length != 2)
                    throw new IllegalArgumentException(numParamError + "[delete fileName]");

                tres = cvfs.parsePath(elements[1]);
                twd = (Directory) tres[0];
                tname = (String) tres[1];
                if (!Node.isValidName(tname))
                    throw new IllegalArgumentException("Invalid file name: " + tname);

                twd.delete(tname);
                return;

            case changeDir:
                if (cvfs.getCwd() == null)
                    throw new IllegalStateException("Please first create a disk.");
                if (elements.length != 2)
                    throw new IllegalArgumentException(numParamError + "[changeDir dirName]");

                cvfs.changeDir(elements[1]);
                return;

            case newNegation:
                if (elements.length != 3)
                    throw new IllegalArgumentException(numParamError + "[newNegation criName1 criName2]");
                for (int i = 1; i <= 2; i++)
                    if (!Criterion.isValidCriName(elements[i]))
                        throw new IllegalArgumentException("Invalid Criterion name: " + elements[i]);

                cvfs.newNegation(elements[1], elements[2]);
                return;

            case newBinaryCri:
                if (elements.length != 5)
                    throw new IllegalArgumentException(numParamError +
                            "[newBinaryCri criName1 criName3 logicOp criName4]");
                // check 1, 2, 4 cri's name validality
                for (int i = 1; i <= 4; i *= 2)
                    if (!Criterion.isValidCriName(elements[i]))
                        throw new IllegalArgumentException("Invalid Criterion name: " + elements[i]);
                if (!BinaryCriterion.isValidOperator(elements[3]))
                    throw new IllegalArgumentException("Invalid logic operator, must be && or ||");

                cvfs.newBinaryCri(elements[1], elements[2], elements[3], elements[4]);
                return;

            case newSimpleCri:
                if (elements.length != 5)
                    throw new IllegalArgumentException(numParamError + "[newSimpleCri criName attrName op val]");
                if (!Criterion.isValidCriName(elements[1]))
                    throw new IllegalArgumentException("Invalid Criterion name '" + elements[1] + "'");
                if (!Criterion.isValidCri(elements[1], elements[2], elements[3], elements[4]))
                    throw new IllegalArgumentException("Invalid Criterion argument " +
                            "'" + elements[2] + " " + elements[3] + " " + elements[4] + "'");

                cvfs.newSimpleCri(elements[1], elements[2], elements[3], elements[4]);
                return;

            case printAllCriteria:
                if (elements.length != 1)
                    throw new IllegalArgumentException(numParamError + "[printAllCriteria]");

                cvfs.printAllCriteria();
                return;


            case undo:
                if (elements.length != 1)
                    throw new IllegalArgumentException(numParamError + "[undo]");

                logger.undo();
                return;

            case redo:
                if (elements.length != 1)
                    throw new IllegalArgumentException(numParamError + "[redo]");

                logger.redo();
                return;

            case load:
                if (elements.length != 2)
                    throw new IllegalArgumentException(numParamError + "[load diskStoreName]");

                cvfs.load(elements[1]);
                return;

            case save:
                if (cvfs.getCwd() == null)
                    throw new IllegalStateException("Please first create a disk.");
                if (elements.length != 2)
                    throw new IllegalArgumentException(numParamError + "[store diskStoreName]");

                cvfs.save(elements[1]);
                HistoryLogger.getInstance().newLog(HistoryLogger.OpType.DD, elements[1], cvfs);
                return;
            case quit:
                System.exit(0);
        }

    }


    /**
     * The user-interface, receiving and handling user requests.
     */
    public void terminal() {

        view.updateDir(cvfs.getCwd());

        String command;

        view.printPrompt();
        command = getCommand();

        CommandType type = CommandParser.parseType(command);

        while (type == CommandType.invalid) {
            view.printPrompt();
            command = getCommand();
            type = CommandParser.parseType(command);
        }
        try {
            processCommand(type, command);
        } catch (Exception e) {
            System.out.println("\033[91m" + "Error: " + e.getLocalizedMessage() + "\033[0m");
        }
    }

    /**
     * Used to parse and operate undo and redo command
     */
    public static class loggerParser {
        /**
         * Add an object.
         */
        private final Ops add = args -> {
            Object obj = args[0];
            if (obj instanceof Node) {
                Node unit = (Node) obj;
                Directory parent = (Directory) args[1];
                parent.getCatalog().put(unit.getName(), unit);
                parent.updateSizeBy(unit.getSize());
            } else {
                Criterion cri = (Criterion) obj;
                CVFS cvfs = (CVFS) args[1];
                cvfs.getCriteria().put(cri.getName(), cri);
            }
        };
        /**
         * Delete an Object.
         */
        private final Ops del = args -> {
            Object obj = args[0];
            if (obj instanceof Node) {
                Node unit = (Node) obj;
                Directory parent = (Directory) args[1];
                parent.getCatalog().remove(unit.getName());
                parent.updateSizeBy(-unit.getSize());
            } else {
                Criterion cri = (Criterion) obj;
                CVFS cvfs = (CVFS) args[1];
                cvfs.getCriteria().remove(cri.getName());
            }
        };
        /**
         * Rename an object.
         */
        private final Ops ren = args -> {
            Node unit = (Node) args[0];
            String newName = (String) args[1];
            String oldName = (String) args[2];
            unit.setName(newName);
            Directory parent = (Directory) unit.getParent();
            parent.getCatalog().remove(oldName);
            parent.getCatalog().put(newName, unit);
        };
        /**
         * Change directory.
         */
        private final Ops cd = args -> {
            Directory newDir = (Directory) args[0];
            CVFS cvfs = (CVFS) args[2];
            cvfs.setCwd(newDir);
        };
        /**
         * Switch to another disk.
         */
        private final Ops sd = args -> {
            Disk newDisk = (Disk) args[0];
            CVFS cvfs = (CVFS) args[2];
            cvfs.setDisk(newDisk);
        };
        /**
         * Delete a disk.
         */
        private final Ops dd = args -> {
            String name = (String) args[0];
            CVFS cvfs = (CVFS) args[1];
            cvfs.delDisk(name);

        };
        /**
         * Store a disk to local storage.
         */
        private final Ops ld = args -> {
            String name = (String) args[0];
            CVFS cvfs = (CVFS) args[1];
            cvfs.save(name);

        };
        private final HistoryLogger logger = HistoryLogger.getInstance();
        private final HashMap<HistoryLogger.OpType, Ops> typeMap = new HashMap<>();

        {
            typeMap.put(HistoryLogger.OpType.ADD, add);
            typeMap.put(HistoryLogger.OpType.DEL, del);
            typeMap.put(HistoryLogger.OpType.REN, ren);
            typeMap.put(HistoryLogger.OpType.CD, cd);
            typeMap.put(HistoryLogger.OpType.SD, sd);
            typeMap.put(HistoryLogger.OpType.DD, dd);
            typeMap.put(HistoryLogger.OpType.LD, ld);
        }

        /**
         * To parse and operate a log.
         *
         * @param log The log to be parsed.
         */
        public void parse(HistoryLogger.Tracelog log) {
            HistoryLogger.OpType type = log.getType();
            Object[] args = log.getArgs();
            Ops ops = typeMap.get(type);
            ops.operate(args);
        }

        /**
         * To undo a step.
         */
        public void undo() {
            HistoryLogger.Tracelog log;
            log = logger.getlog();
            parse(log);
        }

        /**
         * To redo a step.
         */
        public void redo() {
            HistoryLogger.Tracelog log;
            log = logger.getRlog();
            parse(log);

        }

        /**
         * Reflect on different kinds of optypes.
         */
        public interface Ops {
            /**
             * To operate on an logger.
             *
             * @param args The arguments of the operation.
             */
            void operate(Object[] args);
        }
    }

}
