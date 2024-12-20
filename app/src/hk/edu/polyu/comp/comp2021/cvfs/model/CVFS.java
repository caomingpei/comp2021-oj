package hk.edu.polyu.comp.comp2021.cvfs.model;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.util.HashMap;
import java.util.Map;
// file system
import hk.edu.polyu.comp.comp2021.cvfs.model.filesystem.Disk;
import hk.edu.polyu.comp.comp2021.cvfs.model.filesystem.Directory;
import hk.edu.polyu.comp.comp2021.cvfs.model.filesystem.Node;
// criteria
import hk.edu.polyu.comp.comp2021.cvfs.model.criteria.Criterion;
import hk.edu.polyu.comp.comp2021.cvfs.model.criteria.BinaryCriterion;
// command logger
import hk.edu.polyu.comp.comp2021.cvfs.model.terminal.HistoryLogger;


/**
 * The Virtual File System.
 */
public class CVFS {
    // the implementation of the CVFS system.
    /**
     * A hashmap storing all criteria.
     */
    private final Map<String, Criterion> criteria = new HashMap<>();
    /**
     * Stores the current disk in use.
     */
    private Disk disk;
    /**
     * Stores the reference to the current working directory.
     */
    private Directory cwd;

    {
        criteria.put("IsDocument", Criterion.getIsDocument());
    }

    /**
     * @return The sets of criteria.
     */
    public Map<String, Criterion> getCriteria() {
        return criteria;
    }

    /**
     * Create a new disk and return its reference.
     *
     * @param diskSize The capacity of the disk.
     */
    public void newDisk(int diskSize) {
        Disk tmp = disk;
        disk = new Disk(diskSize);
        HistoryLogger.getInstance().newLog(HistoryLogger.OpType.SD, tmp, disk, this);
        cwd = disk;
        System.out.println("\033[32mNew disk created, size: \033[33m" + diskSize + "\033[0m");
    }

    /**
     * Set the current disk to another one.
     *
     * @param disk The disk to be switched to.
     */
    public void setDisk(Disk disk) {
        this.disk = disk;
        cwd = disk;
    }

    /**
     * @return The reference to the current working directory.
     */
    public Directory getCwd() {
        return cwd;
    }

    /**
     * Set the current working directory to a new place.
     *
     * @param cwd New location of the current working directory.
     */
    public void setCwd(Directory cwd) {
        this.cwd = cwd;
    }

    /**
     * Stores the current disk in local storage.
     * Throw an exception if the name is invalid OR
     * such file already exists.
     *
     * @param name The name of the file.
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void save(String name) {
        try {
            if (!Node.isValidName(name))
                throw new IllegalArgumentException("Invalid name.");
            String path = System.getProperty("user.dir") + "\\disks\\";
            File dir = new File(path);
            dir.mkdirs();
            path += name + ".cvfs";
            File file = new File(path);
            if (file.exists())
                throw new FileAlreadyExistsException("File Already Exists.");
            //noinspection ResultOfMethodCallIgnored
            file.createNewFile();
            FileOutputStream buffer = new FileOutputStream(path);
            ObjectOutputStream out = new ObjectOutputStream(buffer);
            out.writeObject(disk);
            out.close();
            buffer.close();
            System.out.println("Current disk stored in " + path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete a local copy of the disk.
     *
     * @param name The name of the file to be deleted.
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void delDisk(String name) {
        File file = new File(System.getProperty("user.dir") + "\\disks\\" + name + ".cvfs");
        file.delete();
    }

    /**
     * Load a disk from local storage
     *
     * @param name the name of the disk to be loaded.
     */
    public void load(String name) {
        try {
            String path = System.getProperty("user.dir") + "\\disks\\" + name + ".cvfs";
            if (!new File(path).exists())
                throw new FileNotFoundException("File Not Found.");
            FileInputStream buffer = new FileInputStream(path);
            ObjectInputStream in = new ObjectInputStream(buffer);
            Disk tmp = (Disk) in.readObject();
            HistoryLogger.getInstance().newLog(HistoryLogger.OpType.SD, disk, tmp, this);
            setDisk(tmp);
            in.close();
            buffer.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException ignored) {
        }
        System.out.println("Disk " + name + " Loaded.");
    }

    /**
     * Return a criterion by name.
     *
     * @param criName The name of the criterion
     * @return The reference to the criterion.
     */
    public Criterion getCri(String criName) {
        return criteria.get(criName);
    }

    /**
     * Create a new criterion. Print a warning and return if one of the arguments is invalid.
     *
     * @param name The name of the criterion.
     * @param attr The name of the attribute.
     * @param op   The name of the operation.
     * @param val  The value of the operation.
     */
    public void newSimpleCri(String name, String attr, String op, String val) {
        if (criteria.containsKey(name))
            throw new IllegalArgumentException("Already exists Criterion " + name);

        Criterion newCri = new Criterion(name, attr, op, val);
        criteria.put(name, newCri);
        HistoryLogger.getInstance().newLog(HistoryLogger.OpType.DEL, newCri, this);
    }

    /**
     * Create a negated criterion of name2.
     * Print a warning and return if name2 can't be found OR
     * name1 is invalid.
     *
     * @param name1 The name of the new criterion.
     * @param name2 The name of the criterion to be negated.
     */
    public void newNegation(String name1, String name2) {
        if (criteria.containsKey(name1))
            throw new IllegalArgumentException("Already exists Criterion " + name1);
        if (!criteria.containsKey(name2))
            throw new IllegalArgumentException("No matching Criterion " + name2);

        Criterion newCri = criteria.get(name2).getNegCri(name1);
        HistoryLogger.getInstance().newLog(HistoryLogger.OpType.DEL, newCri, this);
        criteria.put(name1, newCri);
    }

    /**
     * Create a combined criterion.
     * Print a warning and return if name1 is invalid OR
     * name3 or name4 cannot be found OR
     * op is invalid.
     *
     * @param name1 The name of the new criterion.
     * @param name3 The name of the first criterion to be combined.
     * @param op    The logic operation of the combination.
     * @param name4 The name of the second criterion to be combined.
     */
    public void newBinaryCri(String name1, String name3, String op, String name4) {
        if (criteria.containsKey(name1))
            throw new IllegalArgumentException("Already exists Criterion " + name1);
        if (!criteria.containsKey(name3) || name3 == null)
            throw new IllegalArgumentException("Cannot find Criterion " + name3);
        if (!criteria.containsKey(name4) || name4 == null)
            throw new IllegalArgumentException("Cannot find Criterion " + name4);

        BinaryCriterion newCri = new BinaryCriterion(name1, criteria.get(name3), op, criteria.get(name4));
        HistoryLogger.getInstance().newLog(HistoryLogger.OpType.DEL, newCri, this);
        criteria.put(name1, newCri);
    }

    /**
     * Print all criteria in the memory in a formatted form.
     */
    public void printAllCriteria() {
        System.out.println("\033[32mCriteria: \033[33m" + criteria.size() + " in total.\033[34m");
        for (Criterion criterion : criteria.values())
            System.out.println("  ╟ " + criterion);
        System.out.print("\033[0m");
    }

    /**
     * Change the current working directory to the desired one.
     * Print a warning and return if the desired directory does not exist.
     *
     * @param name The new directory.
     */
    public void changeDir(String name) {
        if (name.equals("..")) {
            if (cwd.getParent() == null)
                throw new IllegalArgumentException("This is the root directory.");
            HistoryLogger.getInstance().newLog(HistoryLogger.OpType.CD, getCwd(), getCwd().getParent(), this);
            setCwd(cwd.getParent());
            return;
        }
        Object[] res = parsePath(name);
        Directory tmpDir = (Directory) res[0];
        String tname = (String) res[1];
        Node newDir = tmpDir.getCatalog().get(tname);
        if (newDir == null)
            throw new IllegalArgumentException("Invalid path.");
        if (!(newDir instanceof Directory))
            throw new IllegalArgumentException("This is not a directory.");
        HistoryLogger.getInstance().newLog(HistoryLogger.OpType.CD, getCwd(), newDir, this);
        setCwd((Directory) newDir);
    }

    /**
     * Parse the path and return the directory of the target file and the name of the target file.
     *
     * @param path The string to be parsed.
     * @return The parent of the target file and the name of the file..
     */
    public Object[] parsePath(String path) {
        String[] paths = path.split(":");
        Directory cur = getCwd();
        for (int i = 0; i < paths.length - 1; i++) {
            String s = paths[i];
            if (s.equals("$")) continue;
            cur = (Directory) cur.getCatalog().get(s);
            if (cur == null) throw new IllegalArgumentException("Invalid Path, please use $:<dir>:...:<file> format.");
        }
        Object[] result = new Object[2];
        result[0] = cur;
        result[1] = paths[paths.length - 1];
        return result;
    }
}
