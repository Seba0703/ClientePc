package Builders;

import java.util.List;

public interface Builder {

    /**Correr siempre en el thread de JavaFX
     * */
    void build();

    /**Correr siempre en el thread de JavaFX
     * */
    void bindSuggestions(List<String> suggestions);
}
