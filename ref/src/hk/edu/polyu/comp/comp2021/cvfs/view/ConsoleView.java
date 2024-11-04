package hk.edu.polyu.comp.comp2021.cvfs.view;

import hk.edu.polyu.comp.comp2021.cvfs.model.filesystem.Directory;

public class ConsoleView {

    /**
     * The string storing the current path of the working directory.
     */
    private String curPath;

    public void start() {
        System.out.println("\n"
                + "██╗  ██╗███████╗██╗     ██╗      ██████╗         ██████╗██╗   ██╗███████╗███████╗\n"
                + "██║  ██║██╔════╝██║     ██║     ██╔═══██╗       ██╔════╝██║   ██║██╔════╝██╔════╝\n"
                + "███████║█████╗  ██║     ██║     ██║   ██║       ██║     ██║   ██║█████╗  ███████╗\n"
                + "██╔══██║██╔══╝  ██║     ██║     ██║   ██║       ██║     ╚██╗ ██╔╝██╔══╝  ╚════██║\n"
                + "██║  ██║███████╗███████╗███████╗╚██████╔╝▄█╗    ╚██████╗ ╚████╔╝ ██║     ███████║\n"
                + "╚═╝  ╚═╝╚══════╝╚══════╝╚══════╝ ╚═════╝ ╚═╝     ╚═════╝  ╚═══╝  ╚═╝     ╚══════╝\n"
        );
    }

    /**
     * Update the path of current working directory after changing.
     *
     * @param cur The current working directory.
     */
    public void updateDir(Directory cur) {
        if (cur == null) {
            curPath = "$ ";
            return;
        }
        StringBuilder str = cur.getPath();
        str.append("$ ");
        curPath = str.toString();
    }

    /**
     * Print a prompt including the current working directory.
     */
    public void printPrompt() {
        System.out.print(curPath);
    }
}
