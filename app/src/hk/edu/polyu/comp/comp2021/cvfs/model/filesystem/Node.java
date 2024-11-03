package hk.edu.polyu.comp.comp2021.cvfs.model.filesystem;

import java.io.Serializable;

/**
 * The basic properties of a file.
 */
public abstract class Node implements Serializable {

    /**
     * A constant size is 40 bytes for prefix allocated for each file.
     */
    protected static final int SIZE_PREFIX = 40;
    /**
     * A non-null string. Only numbers and English letters are allowed. No more
     * than 10 chars. Can't be empty except for the disk.
     */
    private String name;

    /**
     * The size of the file.
     */
    private int size;

    /**
     * Construct a new file.
     *
     * @param name The name of the file.
     */
    public Node(String name) {
        this.name = name;
    }

    /**
     * To check if the name is valid.Only numbers and English letters are
     * allowed. No more than 10 chars.
     *
     * @param name The string to be checked.
     * @return true if it is valid.
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean isValidName(String name) {
        return name.matches("^[a-zA-Z0-9]{1,10}$");
    }

    /**
     * Safely get the name of the file.
     *
     * @return the name of the file.
     */
    public String getName() {
        return name;
    }

    /**
     * Rename the file.
     *
     * @param newName The new name to be used.
     */
    public void setName(String newName) {
        name = newName;
    }

    /**
     * safely get the parent of the file.
     *
     * @return the reference of the parent.
     */
    public abstract Node getParent();

    /**
     * Set a new parent to the current unit.
     *
     * @param newParent The new parent of the file.
     */
    @SuppressWarnings("unused")
    public abstract void setParent(Node newParent);

    /**
     * Get the size of the file.
     *
     * @return the size of the file.
     */
    public int getSize() {
        return size;
    }

    /**
     * Resize the file.
     *
     * @param newSize The new size.
     */
    public void setSize(int newSize) {
        size = newSize;
    }

    /**
     * get level index of this
     *
     * @return The level index of this file;
     */
    public int getLevel() {
        int level = 0;
        Node current = this;

        while (current.getParent() != null) {
            level++;
            current = current.getParent();
        }

        return level;
    }
}
