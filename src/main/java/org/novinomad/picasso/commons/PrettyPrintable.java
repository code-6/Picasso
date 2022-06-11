package org.novinomad.picasso.commons;

public interface PrettyPrintable {
    default String toString(boolean pretty) {
        String string = this.toString();
        String className = this.getClass().getSimpleName();
        return !pretty
                ? string
                : string.replace(className + "{", "\n" + className + "{\n\t")
                .replaceAll(", ", ",\n\t")
                .replace("}", "\n}");
    }
}
