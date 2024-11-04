package hk.edu.polyu.comp.comp2021.cvfs.model.filesystem;

import hk.edu.polyu.comp.comp2021.cvfs.model.terminal.HistoryLogger;
import hk.edu.polyu.comp.comp2021.cvfs.model.type.DocumentType;
import hk.edu.polyu.comp.comp2021.cvfs.model.criteria.Criterion;

import java.util.HashMap;
import java.util.Map;

/**
 * This class implement directory that can stored documents or other directory
 * to form a file system's storage tree with disk as root. This class also
 * provids methods for revision, move, copy, delete, list, search and so on.
 */
public class Directory extends Node {

    /**
     * The contents in the directory.
     */
    private final Map<String, Node> catalog = new HashMap<>();
    private static final String noFileWarning = "Warning: No files/folders in the current direcotry";
    private static final String unchangedRenameWarning = "Warning: File name unchanged during rename.";

    /**
     * A reference to the parent directory. Not null except for the disk.
     */
    private Directory parent;

    /**
     * Construct a new directory.
     *
     * @param name The name of the directory.
     * @param parent The parent of this directory.
     */
    public Directory(String name, Node parent) {
        super(name);
        setParent(parent);
        setSize(SIZE_PREFIX);
    }

    /**
     * @return The reference to the parent.
     */
    @Override
    public Directory getParent() {
        return parent;
    }

    /**
     * Set a new parent to the current file.
     *
     * @param newParent The new parent of the file.
     */
    @Override
    public void setParent(Node newParent) {
        parent = (Directory) newParent;
    }

    /**
     * Format the output
     *
     * @return the formatted output in green.
     */
    @Override
    public String toString() {
        return String.format("\033[32m%-14s \033[33m%d\033[0m", getName(), getSize());
    }

    /**
     * @return The catalog of the current directory.
     */
    public Map<String, Node> getCatalog() {
        return catalog;
    }

    /**
     * Make a new directory in the current directory. First update the size of
     * the current directory, then create the file.
     *
     * @param name The name of the new directory.
     * @return The reference to the new directory.
     */
    public Directory newDir(String name) {
        if (catalog.get(name) != null) {
            throw new IllegalArgumentException("A file with the same name already exists");
        }
        Directory tmp = new Directory(name, this);
        updateSizeBy(tmp.getSize());
        catalog.put(name, tmp);
        HistoryLogger.getInstance().newLog(HistoryLogger.OpType.DEL, tmp, this);
        return tmp;
    }

    /**
     * Make a new document in the directory. First update the size of the
     * current directory, then create the file.
     *
     * @param name The name of the document.
     * @param type The type of the document.
     * @param content The content of the document.
     * @return The reference to the new Document.
     */
    public Document newDoc(String name, DocumentType type, String content) {
        if (catalog.get(name) != null) {
            throw new IllegalArgumentException("A file with the same name already exists.");
        }
        Document tmp = new Document(name, this, type, content);
        updateSizeBy(tmp.getSize());
        catalog.put(name, tmp);
        HistoryLogger.getInstance().newLog(HistoryLogger.OpType.DEL, tmp, this);
        return tmp;
    }

    /**
     * Delete a file in the current directory and move it to bin. Print a
     * warning and return if there is no such file. Then update the size of
     * current directory
     *
     * @param name The name of the file to be deleted.
     */
    public void delete(String name) {
        if (catalog.get(name) == null) {
            throw new IllegalArgumentException("Can't find " + name + " in this directory.");
        }
        updateSizeBy(-catalog.get(name).getSize());
        HistoryLogger.getInstance().newLog(HistoryLogger.OpType.ADD, catalog.get(name), this);
        catalog.remove(name);
    }

    /**
     * Rename a file in the current directory. Print a warning and return is
     * there is no such file OR There exists some file with the same name.
     *
     * @param oldName The old name of the file.
     * @param newName The new name of the file.
     */
    public void rename(String oldName, String newName) {
        if (catalog.get(oldName) == null) {
            throw new IllegalArgumentException("Can't find " + oldName + " in this directory.");
        }
        if (catalog.get(newName) != null) {
            throw new IllegalArgumentException("A file with the same new name already exists in this directory");
        }

        if (newName.equals(oldName)) {
            System.out.println(unchangedRenameWarning);
        }

        Node renamedItem = catalog.get(oldName);
        renamedItem.setName(newName);
        HistoryLogger.getInstance().newLog(HistoryLogger.OpType.REN, renamedItem, oldName, newName);
        catalog.remove(oldName);
        catalog.put(newName, renamedItem);

    }

    /**
     * List all files in the directory and report the total number and size of
     * files listed. For each document, list the name, type, and size. For each
     * directory, list the name and size.
     */
    public void list() {
        System.out.println("\033[4m" + this);
        if (catalog.isEmpty()) {
            System.out.println(noFileWarning);
        }
        for (Node unit : catalog.values()) {
            System.out.println(" ├─ " + unit);
        }
    }

    /**
     * Recursively list the files in the directory. Use indentation to indicate
     * the level of each line. Report the total number and size of files listed.
     */
    public void rList() {
        System.out.println("\033[4m" + this);
        if (catalog.isEmpty()) {
            System.out.println(noFileWarning);
        }
        rList(this, 0);
    }

    /**
     * Recursively list the files in the directory. Use indentation to indicate
     * the level of each line. Report the total number and size of files listed.
     *
     * @param currDir The currDir of each recursive level.
     * @param level The level of each recursive.
     */
    public void rList(Directory currDir, int level) {
        for (Node unit : currDir.getCatalog().values()) {
            for (int i = 0; i < level; i++) {
                System.out.print("\t");
            }
            System.out.println(" ├─ " + unit);
            if (unit instanceof Directory) {
                rList((Directory) unit, level + 1);
            }
        }
    }

    /**
     * A list with a filter.
     *
     * @param criterion The filter.
     */
    public void search(Criterion criterion) {
        if (catalog.isEmpty()) {
            System.out.println(noFileWarning);
            return;
        }
        System.out.println("\033[4m" + this);
        for (Node unit : catalog.values()) {
            if (criterion.check(unit)) {
                System.out.println(unit);
            }
        }
    }

    /**
     * A rList with a filter.
     *
     * @param criterion The filter.
     */
    public void rSearch(Criterion criterion) {
        if (catalog.isEmpty()) {
            System.out.println(noFileWarning);
            return;
        }
        System.out.println("\033[4m" + this);
        rSearch(this, criterion);
    }

    /**
     * A rList with a filter.
     *
     * @param criName The filter.
     * @param currDir The current Directory of each recursive level.
     */
    public static void rSearch(Directory currDir, Criterion criName) {
        for (Node unit : currDir.getCatalog().values()) {
            if (criName.check(unit)) {
                System.out.println(unit);
            }
            if (unit instanceof Directory) {
                rSearch((Directory) unit, criName);
            }
        }
    }

    /**
     * Recursively update the size of the directory by a certain number. First
     * update the size of parent, then the current directory.
     *
     * @param offset Positive if the size increases, vice versa.
     */
    public void updateSizeBy(int offset) {
        getParent().updateSizeBy(offset);
        setSize(getSize() + offset);
    }

    /**
     * Get the full path of the current directory.
     *
     * @return The StringBuilder containing the full path
     */
    public StringBuilder getPath() {
        StringBuilder str = getParent().getPath();
        str.append(':').append(getName());
        return str;
    }
}
