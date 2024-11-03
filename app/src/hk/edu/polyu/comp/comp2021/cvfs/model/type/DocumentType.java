package hk.edu.polyu.comp.comp2021.cvfs.model.type;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Four different types of a document file.
 */
public enum DocumentType {
    TXT("txt"),
    JAVA("java"),
    HTML("html"),
    CSS("css");

    /**
     * The extension of the document type.
     */
    private final String extension;

    private static final Set<String> VALID_EXTENSIONS
            = Arrays.stream(values())
                    .map(DocumentType::toString)
                    .collect(Collectors.toSet());

    /**
     * Link the enum with the text.
     */
    DocumentType(String extension) {
        this.extension = extension;
    }

    /**
     * Check whether extension is valid or not
     *
     * @param extension The string to be checked.
     * @return True if the extension is valid.
     */
    public static boolean isValidDocumentType(String extension) {
        return VALID_EXTENSIONS.contains(extension.toLowerCase());
    }

    /**
     * Parse the string and return a DocumentType. Return null if the string is
     * invalid
     *
     * @param extension The string to be parsed
     * @return The DocumentType of the string.
     */
    public static DocumentType parse(String extension) {
        return Arrays.stream(values())
                .filter(type -> type.extension.equalsIgnoreCase(extension))
                .findFirst()
                .orElse(null);
    }

    /**
     * Return the text form of the enum
     *
     * @return The text of the enum.
     */
    @Override
    public String toString() {
        return extension;
    }
}
