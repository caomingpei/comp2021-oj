package hk.edu.polyu.comp.comp2021.cvfs.model.criteria;

import hk.edu.polyu.comp.comp2021.cvfs.model.filesystem.Document;
import hk.edu.polyu.comp.comp2021.cvfs.model.filesystem.Node;
import java.util.Objects;

/**
 * This class implements simple criterion with name, attribute, operation and
 * value, which support instantiating and checking units with a simple
 * criterion; and provide <code>static</code> methods to perform valid criteria
 * check or IsDocument check.
 */
public class Criterion implements Cloneable {

    /**
     * The special criterion isDocument
     */
    private final static Criterion isDocument = new Criterion();
    /**
     * The name of the criterion, containing exactly 2 English letters.
     */
    private String name;
    /**
     * The name of the attribute. One of ["name","type","size"].
     */
    private String attr;
    /**
     * The name of the option. name -> contains type -> equals size -> a
     * comparison mark
     */
    private String op;
    /**
     * name,type -> A string in double quote size -> An integer
     */
    private String val;
    /**
     * To decide whether the result of check should be negated, false by
     * default.
     */
    private boolean negation = false;
    /**
     * Used to mark the special criterion IsDocument
     */
    private boolean isDocumentMark = false;

    /**
     * Create a criterion.
     *
     * @param name The name of the criterion.
     * @param attr The name of the attribute.
     * @param op The name of the operation.
     * @param val The value of the operation.
     */
    public Criterion(String name, String attr, String op, String val) {
        this.name = name;
        this.attr = attr;
        this.op = op;
        this.val = val;
    }

    /**
     * Constructor with only name initialized.
     *
     * @param name new Criterion name
     */
    public Criterion(String name) {
        this.name = name;
    }

    /**
     * A clone constructor
     */
    private Criterion(Criterion x) {
        name = x.getName();
        attr = x.getAttr();
        op = x.getOp();
        val = x.getVal();
        negation = x.isNeg();
        isDocumentMark = x.isDocumentCri();
    }

    /**
     * Special Constructor for IsDocument Criterion
     */
    private Criterion() {
        this.name = "IsDocument";
        this.isDocumentMark = true;
    }

    /**
     * Get the special criterion isDocument
     *
     * @return the reference to isDocument
     */
    public static Criterion getIsDocument() {
        return isDocument;
    }

    /**
     * Check whether all parameters are valid. Rubrics over the declarations of
     * fields.
     *
     * @param name name
     * @param attr attribute
     * @param op operation
     * @param val value
     * @return True if all parameters are valid.
     * @throws IllegalArgumentException if null param detected
     */
    public static boolean isValidCri(String name, String attr, String op, String val) throws IllegalArgumentException {
        if (name == null || attr == null || op == null || val == null) {
            throw new IllegalArgumentException("Null criterion checked by isValidCri() checker.");
        }

        return (isValidCriName(name) && isValidCriContent(attr, op, val));
    }

    /**
     * Check whether CriName is valid.
     *
     * @param name the name of the criterion
     * @return boolean, whether it contains exactly 2 English letters.
     */
    public static boolean isValidCriName(String name) {
        if (name == null) {
            return false;
        }
        return (name.matches("^[a-zA-Z]{2}$") || name.equals("IsDocument"));
    }

    /**
     * Check whether CriContent (attr, op, val) is valid.
     *
     * @param attr 'name' | 'type' | 'size'
     * @param op 'contains' | 'equals' | 6 logOps
     * @param val '"text"' | '"text"' | integer
     * @return boolean
     */
    private static boolean isValidCriContent(String attr, String op, String val) {
        switch (attr) {
            case "name":
                return op.equals("contains")
                        && val.matches("^\"\\S+\"$");

            case "type":
                if (op.equals("equals") && val.matches("^\"\\S+\"$")) {
                    if (!val.matches("^\"(txt|html|css|java)\"$")) {
                        System.out.println("\033[31;mWarning: Unsupported file type " + val + ".\033[0m");
                    }
                    // Intended warning, not error
                    // if type not valid show *warning*, then return.
                    return true;
                }
                return false;

            case "size":
                boolean flagOp,
                 flagVal;
                flagOp = op.matches("^(([><]=?)|([!=]=))$");

                try {
                    Integer.parseInt(val);
                    flagVal = true;
                } catch (NumberFormatException e) {
                    flagVal = false;
                }

                return flagOp && flagVal;
        }
        return false;
    }

    /**
     * clone method
     *
     * @return a new cloned Criterion object
     */
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public Object clone() {
        return new Criterion(this);
    }

    /**
     * @return The name of the criterion
     */
    public String getName() {
        return name;
    }

    /**
     * set the this.name to param without valid-name-checking
     *
     * @param name new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The attribute of the criterion.
     */
    public String getAttr() {
        return attr;
    }

    /**
     * @return The operation of the criterion.
     */
    public String getOp() {
        return op;
    }

    /**
     * @return The value of the operation.
     */
    public String getVal() {
        return val;
    }

    /**
     * Set this Criterion to its Negative
     */
    public void setNeg() {
        negation = !negation;
    }

    /**
     * @return True if the check result is negated.
     */
    public boolean isNeg() {
        return negation;
    }

    /**
     * @return whether the criterion is document
     */
    public boolean isDocumentCri() {
        return isDocumentMark;
    }

    /**
     * Check if the unit x fits the criterion. Print a warning and return false
     * if x is null.
     *
     * @param unit The unit to be checked.
     * @return True if the condition holds.
     * @throws RuntimeException if unit null or expression error
     */
    public boolean check(Node unit) {
        if (unit == null) {
            throw new RuntimeException("Null unit checked by check() checker.");
        }

        if (isDocumentMark) {
            return unit instanceof Document;
        }

        boolean result;
        switch (attr) {
            case "name":
                // Remove quotes from val and check if name contains the value
                String nameValue = val.substring(1, val.length() - 1);
                result = unit.getName().contains(nameValue);
                break;
            case "type":
                if (!(unit instanceof Document)) {
                    return false;
                }
                String typeValue = val.substring(1, val.length() - 1);
                result = ((Document) unit).getType().toString().equals(typeValue);
                break;
            case "size":
                int size = unit.getSize();
                int compareValue = Integer.parseInt(val);
                switch (op) {
                    case ">":
                        result = size > compareValue;
                        break;
                    case ">=":
                        result = size >= compareValue;
                        break;
                    case "<":
                        result = size < compareValue;
                        break;
                    case "<=":
                        result = size <= compareValue;
                        break;
                    case "==":
                        result = size == compareValue;
                        break;
                    case "!=":
                        result = size != compareValue;
                        break;
                    default:
                        throw new RuntimeException("Invalid operator: " + op);
                }
                break;
            default:
                throw new RuntimeException("Invalid attribute: " + attr);
        }

        return negation ? !result : result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Criterion criterion = (Criterion) o;
        return isNeg() == criterion.isNeg()
                && Objects.equals(getName(), criterion.getName())
                && Objects.equals(getAttr(), criterion.getAttr())
                && Objects.equals(getOp(), criterion.getOp())
                && Objects.equals(getVal(), criterion.getVal());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, attr, op, val, negation);
    }

    @Override
    public String toString() {
        if (isDocumentMark) {
            if (!negation) {
                return "Criterion { IsDocument }";
            }
            return "Criterion { !(IsDocument) }";
        }

        return "Criterion '" + getName() + "', { " + criToString() + " }";
    }

    // ===================================== private and protected methods for implementation
    /**
     * Get a negative copy of this
     *
     * @param negName the name of the negative criterion
     * @return a negative criterion of this
     */
    public Criterion getNegCri(String negName) {
        Criterion that = new Criterion(this);
        that.setNeg();
        that.setName(negName);
        return that;
    }

    /**
     * Convert this to a js string
     *
     * @return js String
     * @throws RuntimeException if attribute invalid
     */
    private String toJsString() throws RuntimeException {
        String base;
        switch (getAttr()) {
            case "name":
                // js: name.contains(matcher), matcher = [ "sth" ]
                base = getAttr() + ".contains(" + getVal() + ")";
                break;
            case "type":
                // js: type == matcher, matcher = [ "sth" ]
                base = getAttr() + "==" + getVal();
                break;
            case "size":
                // js: size >= 30
                base = getAttr() + getOp() + getVal();
                break;
            default:
                throw new RuntimeException("Invalid attribute in " + this + ".");
        }

        if (isNeg()) {
            base = "!(" + base + ")";
        }

        return base;
    }

    /**
     * toString for a single Criterion object
     *
     * @return toString
     */
    protected String criToString() {
        String base;
        base = getAttr() + ' ' + getOp() + ' ' + getVal();

        if (isNeg()) {
            base = "!(" + base + ")";
        }

        return base;
    }

}
