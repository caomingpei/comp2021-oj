package hk.edu.polyu.comp.comp2021.cvfs.model.type;

/**
 * all possible type of command input
 */
public enum CommandType {
    /**
     * invalid
     */
    invalid,
    /**
     * [REQ1] newDisk: create a disk
     */
    newDisk,
    /**
     * [REQ2] newDoc: create a document
     */
    newDoc,
    /**
     * [REQ3] newDir: create a directory
     */
    newDir,
    /**
     * [REQ4] delete: delete a file
     */
    delete,
    /**
     * [REQ5] rename: rename a file
     */
    rename,
    /**
     * [REQ6] changeDir: change the working directory
     */
    changeDir,
    /**
     * [REQ7] list: list files directly contained in the working directory
     */
    list,
    /**
     * [REQ8] rList: list files contained in the working directory
     */
    rList,
    /**
     * [REQ9] newSimpleCri: create simple criterion
     */
    newSimpleCri,
    /**
     * [REQ11] newNegation: construct a composite criterion
     */
    newNegation,
    /**
     * [REQ11] newBinaryCri: construct a criterion with logical AND or logical
     * OR
     */
    newBinaryCri,
    /**
     * [REQ12] printAllCriteria: print all criteria
     */
    printAllCriteria,
    /**
     * [REQ13] search: search for files in the working directory based on an
     * existing criterion.
     */
    search,
    /**
     * [REQ14] rSearch: search for files contained by the working directory
     * based on an existing criterion.
     */
    rSearch,
    /**
     * [REQ15] save: saving the working virtual disk (together with the files in
     * it) into a file on the local file system.
     */
    save,
    /**
     * [REQ16] load: loading a virtual disk from the local file system
     */
    load,
    /**
     * [REQ17] quit: quit the CVFS
     */
    quit,
    /**
     * [BON2] undo: undo the last operation
     */
    undo,
    /**
     * [BON2] redo: redo the last undone operation
     */
    redo,
}
